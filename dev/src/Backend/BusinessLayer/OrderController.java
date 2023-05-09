package Backend.BusinessLayer;

import java.util.*;
import java.util.stream.Collectors;




public class OrderController {
    private SupplierController supplierController;


    public OrderController(){
        this.supplierController = SupplierController.getInstance();
    }
    /***
     *
     * @param order maps between productId to the amount to be ordered
     * @return map between supplier to the products he supplies from the order
     */
    public Map<String, Pair<Map<Integer, Integer>, Double>> order(Map<Integer, Integer> order){
        List<Supplier> suppliers = supplierController.getCopyOfSuppliers();
        Map<Integer, Map<Supplier, Integer>> cantBeOrderedByOneSupplier = findProductsWithoutSupplierToFullySupply(order, suppliers);
        Map<Integer, Integer> notOneSupplierOrder = new HashMap<>();
        for(Integer productId : cantBeOrderedByOneSupplier.keySet()) {
            int amount = order.remove(productId);
            notOneSupplierOrder.put(productId, amount);
        }
        Map<Supplier, List<Integer>> fullProductPerSupplier = divideOrder(order, new LinkedList<>(), suppliers);
        Map<Supplier, Map<Integer, Integer>> notOneSupplierProductsOrder = divideOrder(notOneSupplierOrder, cantBeOrderedByOneSupplier, fullProductPerSupplier);
        Map<Supplier, Map<Integer, Integer>> finalOrder = new HashMap<>();
        for(Supplier supplier : fullProductPerSupplier.keySet()){
            Map<Integer, Integer> supplierOrder = new HashMap<>();
            for(Integer productId : fullProductPerSupplier.get(supplier))
                supplierOrder.put(productId, order.get(productId));
            finalOrder.put(supplier, supplierOrder);
        }
        for(Supplier supplier : notOneSupplierProductsOrder.keySet()){
            if(!finalOrder.containsKey(supplier))
                finalOrder.put(supplier, new HashMap<>());
            for(Integer productId : notOneSupplierProductsOrder.get(supplier).keySet()){
                int amountForSupplier = notOneSupplierProductsOrder.get(supplier).get(productId);
                finalOrder.get(supplier).put(productId, amountForSupplier);
            }
        }

        Map<String, Pair<Map<Integer, Integer>, Double>> finalOrderWithPrice = new HashMap<>();
        for(Map.Entry<Supplier, Map<Integer, Integer>> supplierToOrder : finalOrder.entrySet()){
            Supplier supplier = supplierToOrder.getKey();
            double price = computePriceAfterDiscount(supplier, supplierToOrder.getValue());
            finalOrderWithPrice.put(supplier.getBnNumber(), new Pair<>(supplierToOrder.getValue(), price));
            supplier.addOrder(new Order(supplierToOrder.getValue()));
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
    private Map<Supplier, Map<Integer, Integer>> divideOrder(Map<Integer, Integer> notOneSupplierProductsOrder,
                                                             Map<Integer, Map<Supplier, Integer>> cantBeOrderedByOneSupplier,
                                                             Map<Supplier, List<Integer>> orderOfFullyProducts){
        Map<Supplier, Map<Integer, Integer>> order = new HashMap<>();
        for (Integer productId : notOneSupplierProductsOrder.keySet()){
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
    private Map<Integer, Map<Supplier, Integer>> findProductsWithoutSupplierToFullySupply(Map<Integer, Integer> order, List<Supplier> suppliers){
        Map<Integer, Map<Supplier, Integer>> productToSuppliers = new HashMap<>();
        for(Map.Entry<Integer, Integer> productOrder : order.entrySet()){
            int productId = productOrder.getKey();
            int amount = productOrder.getValue();
            boolean found = false;
            int totalAmountCanBeOrdered = 0;
            Map<Supplier, Integer> suppliersCanSupply = new HashMap<>();
            for(Supplier supplier : suppliers){
                if(supplier.getAgreement().getProduct(productId) != null){
                    if(supplier.getAgreement().getProduct(productId).getNumberOfUnits() >= amount)
                        found = true;
                    else
                       suppliersCanSupply.put(supplier, supplier.getAgreement().getProduct(productId).getNumberOfUnits());
                    totalAmountCanBeOrdered += supplier.getAgreement().getProduct(productId).getNumberOfUnits();
                }
            }
            if(!found)
                productToSuppliers.put(productId, suppliersCanSupply);
            if(totalAmountCanBeOrdered < amount)
                throw new RuntimeException("order failed - product - " + productId + " cant be fully ordered");
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

    private Map<Supplier, List<Integer>> divideOrder(Map<Integer, Integer> order, List<Integer> alreadySupplied, List<Supplier> suppliersToUse){
        if(suppliersToUse.size() == 0 | alreadySupplied.size() == order.size())
            return new HashMap<>();
        Map<Supplier, List<Integer>> suppliersCanSupply = mapSuppliersToProducts(order, alreadySupplied, suppliersToUse);
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
        List<Integer> maxProductsCanBeSupplied = suppliersCanSupply.get(cheapest);
        for(Integer productId: maxProductsCanBeSupplied)
            alreadySupplied.add(productId);
        suppliersToUse.remove(cheapest);
        Map<Supplier, List<Integer>> res = divideOrder(order, alreadySupplied, suppliersToUse);
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
    private  Map<Supplier, List<Integer>> mapSuppliersToProducts(Map<Integer, Integer> order, List<Integer> alreadySupplied, List<Supplier> suppliersToUse){
        Map<Supplier, List<Integer>> suppliersCanSupply = new HashMap<>();
        for(Supplier supplier : suppliersToUse){
            List<Integer> productsCanBeSupplied = new LinkedList<>();
            Map<Integer, Product> suppliersProduct = supplier.getAgreement().getProducts();
            for(Map.Entry<Integer, Integer> productOrder : order.entrySet()){
                int productId = productOrder.getKey();
                int amount = productOrder.getValue();
                if(!alreadySupplied.contains(productId) && suppliersProduct.get(productId) != null && suppliersProduct.get(productId).getNumberOfUnits() >= amount)
                    productsCanBeSupplied.add(productId);
            }
            suppliersCanSupply.put(supplier, productsCanBeSupplied);
        }
        return suppliersCanSupply;
    }

    private double computePriceAfterDiscount(Supplier supplier, Map<Integer, Integer> orderFromSupplier){
        double sum = 0;
        int amountOfProducts = 0;
        for(Integer productId : orderFromSupplier.keySet()) {
            Product product = supplier.getAgreement().getProduct(productId);
            int amount = orderFromSupplier.get(productId);
            if(supplier.getAgreement().getBillOfQuantities() != null)
                sum += supplier.getAgreement().BillOfQuantities().getProductPriceAfterDiscount(productId, amount,product.getPrice() * amount);
            else
                sum += product.getPrice() * amount;
            amountOfProducts += amount;
        }
        if(supplier.getAgreement().getBillOfQuantities() != null)
            return supplier.getAgreement().BillOfQuantities().getPriceAfterDiscounts(amountOfProducts, sum);
        return sum;
    }

    private double computePriceAfterDiscount(Supplier supplier, Map<Integer, Integer> order, List<Integer> productsToSupply){
        double sum = 0;
        int amountOfProducts = 0;
        for(Integer productId : productsToSupply) {
            Product product = supplier.getAgreement().getProduct(productId);
            int amount = order.get(productId);
            if(supplier.getAgreement().getBillOfQuantities() != null)
                sum += supplier.getAgreement().BillOfQuantities().getProductPriceAfterDiscount(productId, amount,product.getPrice() * amount);
            else
                sum += product.getPrice() * amount;
            amountOfProducts += amount;
        }
        if(supplier.getAgreement().getBillOfQuantities() != null)
            return supplier.getAgreement().BillOfQuantities().getPriceAfterDiscounts(amountOfProducts, sum);
        return sum;
    }

}
