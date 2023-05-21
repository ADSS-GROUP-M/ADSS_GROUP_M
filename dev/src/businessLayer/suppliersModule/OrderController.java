package businessLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryByInvitation;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryFixedDays;
import dataAccessLayer.suppliersModule.OrderHistoryDataMappers.OrderHistoryDataMapper;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;




public class OrderController {
    private static OrderController instance;
    private SupplierController supplierController;
    private AgreementController agreementController;
    private BillOfQuantitiesController billOfQuantitiesController;
    private OrderHistoryDataMapper orderHistoryDataMapper;
    /**
     * maps between supplier bn number to a pair of <number of orders - counter, List - of his order history>
     */
    private Map<String, Pair<Integer, List<Order>>> suppliersOrderHistory;
    private OrderController(){
        this.supplierController = SupplierController.getInstance();
        this.agreementController = AgreementController.getInstance();
        billOfQuantitiesController = BillOfQuantitiesController.getInstance();
        orderHistoryDataMapper = new OrderHistoryDataMapper();

        suppliersOrderHistory = new HashMap<>();
    }

    public static OrderController getInstance(){
        if(instance == null)
            instance = new OrderController();
        return instance;
    }
    /***
     *
     * @param order maps between productId to the amount to be ordered
     * @return map between supplier to the products he supplies from the order
     */
    public Map<String, Pair<Map<String, Integer>, Double>> order(Map<String, Integer> order, Branch branch) throws SQLException {
        List<Supplier> suppliers = supplierController.getCopyOfSuppliers();
        Map<String, Map<Supplier, Integer>> cantBeOrderedByOneSupplier = findProductsWithoutSupplierToFullySupply(order, suppliers);
        Map<String, Integer> notOneSupplierOrder = new HashMap<>();
        for(String catalogNumber : cantBeOrderedByOneSupplier.keySet()) {
            int amount = order.remove(catalogNumber);
            notOneSupplierOrder.put(catalogNumber, amount);
        }
        Map<Supplier, List<String>> fullProductPerSupplier = divideOrder(order, new LinkedList<>(), suppliers);
        Map<Supplier, Map<String, Integer>> notOneSupplierProductsOrder = divideOrder(notOneSupplierOrder, cantBeOrderedByOneSupplier, fullProductPerSupplier);
        Map<Supplier, Map<String, Integer>> finalOrder = new HashMap<>();
        for(Supplier supplier : fullProductPerSupplier.keySet()){
            Map<String, Integer> supplierOrder = new HashMap<>();
            for(String catalogNumber : fullProductPerSupplier.get(supplier))
                supplierOrder.put(catalogNumber, order.get(catalogNumber));
            finalOrder.put(supplier, supplierOrder);
        }
        for(Supplier supplier : notOneSupplierProductsOrder.keySet()){
            if(!finalOrder.containsKey(supplier))
                finalOrder.put(supplier, new HashMap<>());
            for(String catalogNumber : notOneSupplierProductsOrder.get(supplier).keySet()){
                int amountForSupplier = notOneSupplierProductsOrder.get(supplier).get(catalogNumber);
                finalOrder.get(supplier).put(catalogNumber, amountForSupplier);
            }
        }

        Map<String, Pair<Map<String, Integer>, Double>> finalOrderWithPrice = new HashMap<>();
        for(Map.Entry<Supplier, Map<String, Integer>> supplierToOrder : finalOrder.entrySet()){
            Supplier supplier = supplierToOrder.getKey();
            double price = computePriceAfterDiscount(supplier, supplierToOrder.getValue());
            finalOrderWithPrice.put(supplier.getBnNumber(), new Pair<>(supplierToOrder.getValue(), price));
            addSuppliersOrder(supplier.getBnNumber(), new Order(supplierToOrder.getValue()));
        }

        return finalOrderWithPrice;
    }

    /***
     * maps product that cant be fully ordered from one supplier, to the suppliers that will supply it
     * @param notOneSupplierProductsOrder order of the products as described
     * @param cantBeOrderedByOneSupplier map between product to the suppliers that can supply it, and the amount they can supply
     * @param orderOfFullyProducts the order of all the products
     * @return the order of the not-one-supplier products
     */
    private Map<Supplier, Map<String, Integer>> divideOrder(Map<String, Integer> notOneSupplierProductsOrder,
                                                             Map<String, Map<Supplier, Integer>> cantBeOrderedByOneSupplier,
                                                             Map<Supplier, List<String>> orderOfFullyProducts){
        Map<Supplier, Map<String, Integer>> order = new HashMap<>();
        for (String productId : notOneSupplierProductsOrder.keySet()){
            Map<Supplier, Integer> suppliersCanSupply = cantBeOrderedByOneSupplier.get(productId);
            //sort suppliers by requirements
            Comparator<Supplier> supplierComparator = (s1, s2) ->
            {
                if(orderOfFullyProducts.get(s1) == null){
                    if(orderOfFullyProducts.get(s2) != null)
                        return 1;
                    return 0;
                }
                if(orderOfFullyProducts.get(s2) == null){
                    if(orderOfFullyProducts.get(s1) != null)
                        return -1;
                    return 0;
                }
                //sort by the amount of products they are supplying
                if(orderOfFullyProducts.get(s1).size() > orderOfFullyProducts.get(s2).size()) {
                    return -1;
                }
                else if(orderOfFullyProducts.get(s1).size() < orderOfFullyProducts.get(s2).size()) {
                    return 1;
                }
                //sort by the amount of not-full-amount-products they can supply
                if(cantBeOrderedByOneSupplier.get(productId).get(s1) > cantBeOrderedByOneSupplier.get(productId).get(s2))
                    return -1;
                if(cantBeOrderedByOneSupplier.get(productId).get(s1) < cantBeOrderedByOneSupplier.get(productId).get(s2))
                    return 1;
                if(computePriceAfterDiscount(s1, new HashMap<>(){{put(productId, Math.min(notOneSupplierProductsOrder.get(productId), suppliersCanSupply.get(s1)));}})
                > computePriceAfterDiscount(s2, new HashMap<>(){{put(productId, Math.min(notOneSupplierProductsOrder.get(productId), suppliersCanSupply.get(s2)));}}))
                    return 1;
                if(computePriceAfterDiscount(s1, new HashMap<>(){{put(productId, Math.min(notOneSupplierProductsOrder.get(productId), suppliersCanSupply.get(s1)));}})
                < computePriceAfterDiscount(s2, new HashMap<>(){{put(productId, Math.min(notOneSupplierProductsOrder.get(productId), suppliersCanSupply.get(s2)));}}))
                    return -1;

                return 0;
            };

            List<Supplier> sortedSupplierList = suppliersCanSupply.keySet().stream().sorted(supplierComparator).collect(Collectors.toList());

            //order
            int amountLeft = notOneSupplierProductsOrder.get(productId);
            for (Supplier supplier : sortedSupplierList){
                if(amountLeft > 0) {
                    if(order.get(supplier) == null)
                        order.put(supplier, new HashMap<>());
                    int canSupply = Math.min(suppliersCanSupply.get(supplier), amountLeft);
                    order.get(supplier).put(productId, canSupply);
                    amountLeft -= canSupply;
                }
            }
        }
        return order;
    }

    /***
     * finds the products that cant be fully supplied by one supplier
     * @param order the order
     * @param suppliers the suppliers
     * @return map between product as described to map of suppliers who supply the product and how much they can supply
     * @throws RuntimeException iff there is a product in the order that cant be supplied fully
     */
    private Map<String, Map<Supplier, Integer>> findProductsWithoutSupplierToFullySupply(Map<String, Integer> order, List<Supplier> suppliers){
        Map<String, Map<Supplier, Integer>> productToSuppliers = new HashMap<>();
        for(Map.Entry<String, Integer> productOrder : order.entrySet()){
            String catalogNumber = productOrder.getKey();
            int amount = productOrder.getValue();
            boolean found = false;
            int totalAmountCanBeOrdered = 0;
            Map<Supplier, Integer> suppliersCanSupply = new HashMap<>();
            for(Supplier supplier : suppliers){
                try {
                    if(agreementController.productExist(supplier.getBnNumber(), catalogNumber)){
                        if(agreementController.getNumberOfUnits(supplier.getBnNumber(), catalogNumber) >= amount)
                            found = true;
                        else
                            suppliersCanSupply.put(supplier, agreementController.getNumberOfUnits(supplier.getBnNumber(), catalogNumber));
                        totalAmountCanBeOrdered += agreementController.getNumberOfUnits(supplier.getBnNumber(), catalogNumber);
                    }
                }
                catch (Exception ignored){}
            }
            if(!found)
                productToSuppliers.put(catalogNumber, suppliersCanSupply);
            if(totalAmountCanBeOrdered < amount)
                throw new RuntimeException("order failed - product - " + catalogNumber + " cant be fully ordered");
        }
        return productToSuppliers;
    }

    /***
     * divide the products that can be fully ordered from one supplier - to the best supplier by price
     * @param order the order
     * @param alreadySupplied products that already been supplied
     * @param suppliersToUse the suppliers that can be used
     * @return map between the best supplier to the products of the order that he will supply
     */

    private Map<Supplier, List<String>> divideOrder(Map<String, Integer> order, List<String> alreadySupplied, List<Supplier> suppliersToUse){
        if(suppliersToUse.size() == 0 | alreadySupplied.size() == order.size())
            return new HashMap<>();
        Map<Supplier, List<String>> suppliersCanSupply = mapSuppliersToProducts(order, alreadySupplied, suppliersToUse);
        Supplier maxCanBeOrderedSupplier = suppliersCanSupply.keySet().stream().reduce(suppliersToUse.get(0) ,(acc, s) ->
                suppliersCanSupply.get(acc).size() > suppliersCanSupply.get(s).size() ? acc : s);
        List<Supplier> suppliers = suppliersCanSupply.keySet().stream().filter((s) -> suppliersCanSupply.get(s).size() == suppliersCanSupply.get(maxCanBeOrderedSupplier).size()).collect(Collectors.toList());
        //find cheapest supplier
        Supplier cheapest = suppliers.get(0);
        double cheapestPrice = 0;
        for(Supplier supplier : suppliers){
            cheapestPrice = computePriceAfterDiscount(cheapest, order, suppliersCanSupply.get(cheapest));
            double sPrice = computePriceAfterDiscount(supplier,order,suppliersCanSupply.get(supplier));
            if(sPrice < cheapestPrice)
                cheapest = supplier;
        }
        //remove max supplier's products from the order
        List<String> maxProductsCanBeSupplied = suppliersCanSupply.get(cheapest);
        for(String catalogNumber: maxProductsCanBeSupplied)
            alreadySupplied.add(catalogNumber);
        suppliersToUse.remove(cheapest);
        Map<Supplier, List<String>> res = divideOrder(order, alreadySupplied, suppliersToUse);
        res.put(cheapest, maxProductsCanBeSupplied);
        return res;
    }


    /***
     * maps for every supplier the products he can supply
     * @param order
     * @param alreadySupplied
     * @param suppliersToUse
     * @return
     */
    private  Map<Supplier, List<String>> mapSuppliersToProducts(Map<String, Integer> order, List<String> alreadySupplied, List<Supplier> suppliersToUse){
        Map<Supplier, List<String>> suppliersCanSupply = new HashMap<>();
        for(Supplier supplier : suppliersToUse){
            List<String> productsCanBeSupplied = new LinkedList<>();
            Map<String, Product> suppliersProduct = agreementController.getProducts(supplier.getBnNumber());
            for(Map.Entry<String, Integer> productOrder : order.entrySet()){
                String productId = productOrder.getKey();
                int amount = productOrder.getValue();
                if(!alreadySupplied.contains(productId) && suppliersProduct.get(productId) != null && suppliersProduct.get(productId).getNumberOfUnits() >= amount)
                    productsCanBeSupplied.add(productId);
            }
            suppliersCanSupply.put(supplier, productsCanBeSupplied);
        }
        return suppliersCanSupply;
    }

    private double computePriceAfterDiscount(Supplier supplier, Map<String, Integer> orderFromSupplier) {
        double sum = 0;
        int amountOfProducts = 0;
        for(String catalogNumber : orderFromSupplier.keySet()) {
            Product product = agreementController.getProduct(supplier.getBnNumber(), catalogNumber);
            int amount = orderFromSupplier.get(catalogNumber);
            try {
                sum += billOfQuantitiesController.getProductPriceAfterDiscount(supplier.getBnNumber(), catalogNumber, amount,product.getPrice() * amount);
            }
            catch (Exception e){// if the supplier doesn't have Bill Of Quantities
                sum += product.getPrice() * amount;
            }
            amountOfProducts += amount;
        }
        try {
            return billOfQuantitiesController.getPriceAfterDiscounts(supplier.getBnNumber(), amountOfProducts, sum);
        }
        catch (Exception e){
            return sum;
        }
    }

    /***
     * computes the price of the products the supplier can supply after discounts
     * @param supplier the supplier that his price is being computed
     * @param order the total order
     * @param productsToSupply the products of the order the supplier can supply
     * @return the price of the products the supplier can supply after discounts
     * @throws SQLException
     * @throws DalException
     */

    private double computePriceAfterDiscount(Supplier supplier, Map<String, Integer> order, List<String> productsToSupply){
        double sum = 0;
        int amountOfProducts = 0;
        for(String catalogNumber : productsToSupply) {
            Product product = agreementController.getProduct(supplier.getBnNumber(), catalogNumber);
            int amount = order.get(catalogNumber);
            try {
                sum += billOfQuantitiesController.getProductPriceAfterDiscount(supplier.getBnNumber(), catalogNumber, amount,product.getPrice() * amount);
            }
            catch (Exception e){
                sum += product.getPrice() * amount;
            }
            amountOfProducts += amount;
        }
        try {
            return billOfQuantitiesController.getPriceAfterDiscounts(supplier.getBnNumber(), amountOfProducts, sum);
        }
        catch (Exception e){
            return sum;
        }
    }

    private int getDay(String bnNumber) throws SQLException {
        int dayNow = Calendar.DAY_OF_WEEK;
        DeliveryAgreement deliveryAgreement = agreementController.getDeliveryAgreement(bnNumber);
        if(deliveryAgreement instanceof DeliveryByInvitation){
            DeliveryByInvitation deliveryByInvitation = (DeliveryByInvitation) deliveryAgreement;
            return (deliveryByInvitation.getNumberOfDays() + dayNow) % 7;
        }
        else {
            DeliveryFixedDays deliveryFixedDays = (DeliveryFixedDays) deliveryAgreement;
            if(deliveryFixedDays.getDaysOfTheWeek().contains(dayNow))
                return 0;
            //try to look for day greater than now, but in this week
            int dayChosen = dayNow;
            int maxDay = 7;
            for(int day : deliveryFixedDays.getDaysOfTheWeek())
                if(day > dayNow && day < maxDay) {
                    dayChosen = day;
                    maxDay = day;
                }
            if(dayChosen != dayNow)
                return dayChosen - dayChosen;
            //try to look for day smaller than now, that in next week
            for(int day : deliveryFixedDays.getDaysOfTheWeek())
                if(day < dayChosen)
                    dayChosen = day;
            return (dayChosen + 7) - dayNow;
        }
    }

    public void orderDueToShortage(Map<String, Integer> order, Branch branch) throws SQLException, DalException {
        List<Supplier> suppliers = supplierController.getCopyOfSuppliers();
        allProductsCanBeSupplied(order, suppliers);
        Map<String, Map<String, Integer>> suppliersToProducts = new HashMap<>();
        for(Map.Entry<String, Integer> product : order.entrySet()){
            String catalogNumber = product.getKey();
            int quantity = product.getValue();
            suppliers = supplierController.getCopyOfSuppliers();
            List<Supplier> suppliersSupplyThisProduct = suppliers.stream().filter((Supplier s) -> {
                try {
                    return agreementController.productExist(s.getBnNumber(), catalogNumber);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            Map<String, Integer> suppliersToQuantity = order.keySet().size() == 1 ? divideProductBetweenSuppliers(catalogNumber, quantity, suppliersSupplyThisProduct, true) : divideProductBetweenSuppliers(catalogNumber, quantity, suppliersSupplyThisProduct, false);
            for(Map.Entry<String, Integer> supplierToQuantity : suppliersToQuantity.entrySet()){
                String bnNumber = supplierToQuantity.getKey();
                int quantityOfSupplier = supplierToQuantity.getValue();
                if(!suppliersToProducts.containsKey(bnNumber))
                    suppliersToProducts.put(bnNumber, new HashMap<>());
                suppliersToProducts.get(bnNumber).put(catalogNumber, quantityOfSupplier);
            }
        }
        //ORDER FLOW - TRANSPORT, ADD TO ORDER HISTORY...
        for(Map.Entry<String, Map<String, Integer>> supplierToProduct : suppliersToProducts.entrySet())
            addSuppliersOrder(supplierToProduct.getKey(), new Order(supplierToProduct.getValue()));

    }

    /***
     * divide the quantity requested of product between the supplier - by priority of time,
     * @param catalogNumber the catalog number of the product ordered
     * @param quantity the quantity of the product to order
     * @param suppliersToUse the suppliers that supplies this product
     * @param oneProductOrder true iff the whole order is of one product - needs to conclude total order discounts
     * @return map between suppliers and the quantities of the product they will supply
     * @throws SQLException
     */
    private Map<String, Integer> divideProductBetweenSuppliers(String catalogNumber, int quantity, List<Supplier> suppliersToUse, boolean oneProductOrder) throws SQLException, DalException {
        if(quantity <= 0)
            return new HashMap<>();

        Supplier supplierShortest = suppliersToUse.get(0);
        for(Supplier supplier : suppliersToUse){
            int quantityCanSupplyShortest = agreementController.getNumberOfUnits(supplierShortest.getBnNumber(), catalogNumber);
            int quantityCanSupplySupplier = agreementController.getNumberOfUnits(supplier.getBnNumber(), catalogNumber);
            boolean daysEquals = getDay(supplier.getBnNumber()) == getDay(supplierShortest.getBnNumber());
            boolean supplierCanSupplyMore = quantityCanSupplySupplier > quantityCanSupplyShortest;
            boolean shortestSupplyLessThanNeeded = quantityCanSupplyShortest < quantity;
            boolean supplierCheaper = false;
            double supplierProductPrice = agreementController.getProduct(supplier.getBnNumber(), catalogNumber).getPrice();
            double shortestProductPrice = agreementController.getProduct(supplierShortest.getBnNumber(), catalogNumber).getPrice();
            if(quantityCanSupplyShortest >= quantity && quantityCanSupplySupplier >= quantity) {
                double supplierProductPriceAfterDiscount = billOfQuantitiesController.getProductPriceAfterDiscount(supplier.getBnNumber(), catalogNumber, quantity, supplierProductPrice * quantity);
                double shortestProductPriceAfterDiscount = billOfQuantitiesController.getProductPriceAfterDiscount(supplierShortest.getBnNumber(), catalogNumber, quantity, shortestProductPrice * quantity);
                if(oneProductOrder)
                    supplierCheaper = billOfQuantitiesController.getPriceAfterDiscounts(supplier.getBnNumber(), quantity, supplierProductPriceAfterDiscount) < billOfQuantitiesController.getPriceAfterDiscounts(supplierShortest.getBnNumber(), quantity, shortestProductPriceAfterDiscount);
                else
                    supplierCheaper = supplierProductPriceAfterDiscount < shortestProductPriceAfterDiscount;
            }
            boolean supplierByPriorityOfTime = getDay(supplier.getBnNumber()) < getDay(supplierShortest.getBnNumber());
            boolean supplierByPriorityOfAmount = daysEquals && shortestSupplyLessThanNeeded && supplierCanSupplyMore;
            boolean supplierByPriorityOfPrice = daysEquals && quantityCanSupplySupplier == quantityCanSupplyShortest && supplierCheaper;
            if(supplierByPriorityOfTime || supplierByPriorityOfAmount || supplierByPriorityOfPrice)
                supplierShortest = supplier;
        }
        int quantityCanSupply = agreementController.getNumberOfUnits(supplierShortest.getBnNumber(), catalogNumber);

        suppliersToUse.remove(supplierShortest);
        Map<String, Integer> supplierToQuantity = divideProductBetweenSuppliers(catalogNumber, quantity - quantityCanSupply, suppliersToUse, oneProductOrder);
        if(quantityCanSupply >= quantity)
            supplierToQuantity.put(supplierShortest.getBnNumber(), quantity);
        else
            supplierToQuantity.put(supplierShortest.getBnNumber(), quantityCanSupply);
        return supplierToQuantity;
    }

    private Map<String, Integer> mapSuppliersToDay(String catalogNumber, List<Supplier> suppliersToUse) throws SQLException {
        Map<String, Integer> supplierToDay = new HashMap<>();
        for(Supplier supplier : suppliersToUse)
            if(agreementController.productExist(supplier.getBnNumber(), catalogNumber))
                supplierToDay.put(supplier.getBnNumber(), getDay(supplier.getBnNumber()));
        return supplierToDay;
    }

    private void allProductsCanBeSupplied(Map<String, Integer> order, List<Supplier> suppliers){
        for(Map.Entry<String, Integer> productOrder : order.entrySet()){
            String catalogNumber = productOrder.getKey();
            int amount = productOrder.getValue();
            boolean found = false;
            int totalAmountCanBeOrdered = 0;
            for(Supplier supplier : suppliers){
                try {
                    if(agreementController.productExist(supplier.getBnNumber(), catalogNumber))
                        totalAmountCanBeOrdered += agreementController.getNumberOfUnits(supplier.getBnNumber(), catalogNumber);
                }
                catch (Exception ignored){}
            }
            if(totalAmountCanBeOrdered < amount)
                throw new RuntimeException("order failed - product - " + catalogNumber + " cant be fully ordered");
        }
    }

    public int getDaysForOrder(String catalogNumber, Branch branch){
        //Todo
        return 0;
    }

    public void addSuppliersOrder(String bnNumber, Order order) throws SQLException {
        orderHistoryDataMapper.insert(bnNumber, order);
    }

    public List<Order> getOrderHistory(String bnNumber) throws SQLException {
        return orderHistoryDataMapper.getOrderHistory(bnNumber);
    }

}
