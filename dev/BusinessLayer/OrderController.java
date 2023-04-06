package BusinessLayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




public class OrderController {

    public Map<Supplier, Pair<List<Integer>, Double>> order(Map<Integer, Integer> order, List<Supplier> suppliers){
        Map<Supplier, Pair<List<Integer>, Double>> orderPerSupplier = divideOrder(order, new LinkedList<>(), suppliers);
        for(Map.Entry<Supplier, Pair<List<Integer>, Double>> suppliersOrder : orderPerSupplier.entrySet()){
            Supplier supplier = suppliersOrder.getKey();
            List<Integer> products = suppliersOrder.getValue().getFirst();
            Double price = suppliersOrder.getValue().getSecond();
            Order o = new Order();
            for (Integer productId : products)
                o.addProduct(productId, order.get(productId));
            supplier.addOrder(o);
        }
        return orderPerSupplier;
    }

    private Map<Supplier, Pair<List<Integer>, Double>> divideOrder(Map<Integer, Integer> order, List<Integer> alreadySupplied, List<Supplier> suppliersToUse){
        Map<Supplier, List<Integer>> suppliersCanSupply = mapSuppliersToProducts(order, alreadySupplied, suppliersToUse);
        Supplier maxCanBeOrderedSupplier = suppliersCanSupply.keySet().stream().reduce(suppliersToUse.get(0) ,(acc, s) ->
                suppliersCanSupply.get(acc).size() > suppliersCanSupply.get(s).size() ? acc : s);
        List<Supplier> suppliers = suppliersCanSupply.keySet().stream().filter((s) -> suppliersCanSupply.get(s).size() == suppliersCanSupply.get(maxCanBeOrderedSupplier).size()).collect(Collectors.toList());
        //find cheapest supplier
        Supplier cheapest = suppliers.get(0);
        double cheapestPrice = 0;
        for(Supplier s : suppliers){
            cheapestPrice = computePriceAfterDiscount(cheapest, order, suppliersCanSupply.get(cheapest));
            double sPrice = computePriceAfterDiscount(s,order,suppliersCanSupply.get(s));
            if(sPrice < cheapestPrice)
                cheapest = s;
        }
        //remove max supplier's products from the order
        List<Integer> maxProductsCanBeSupplied = suppliersCanSupply.get(cheapest);
        for(Integer productId: maxProductsCanBeSupplied)
            alreadySupplied.add(productId);
        suppliersToUse.remove(cheapest);
        Map<Supplier, Pair<List<Integer>, Double>> res = divideOrder(order, alreadySupplied, suppliersToUse);
        res.put(cheapest, new Pair<>(maxProductsCanBeSupplied, cheapestPrice));
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

    private double computePriceAfterDiscount(Supplier supplier, Map<Integer, Integer> order, List<Integer> productsToSupply){
        double sum = 0;
        int amountOfProducts = 0;
        for(Integer productId : productsToSupply) {
            Product product = supplier.getAgreement().getProduct(productId);
            int amount = order.get(productId);
            sum += supplier.getAgreement().getBillOfQuantities().getProductPriceAfterDiscount(productId, amount,product.getPrice() );
            amountOfProducts += amount;
        }
        return supplier.getAgreement().getBillOfQuantities().getPriceAfterDiscounts(amountOfProducts, sum);
    }

}
