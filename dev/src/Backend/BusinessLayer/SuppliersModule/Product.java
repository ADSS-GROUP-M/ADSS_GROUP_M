package Backend.BusinessLayer.SuppliersModule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Product {
    private String name;
    /***
     * unique id in the system
     */
    private int id;
    /***
     * the catalog number in the supplier's system
     */
    private String catalogNumber;
    private double price;
    private int numberOfUnits;
    /***
     * for making a unique id for same products
     */
    private static Map<String, Integer> productsExist = new HashMap<>();
    private static int idCounter = 0;

    public Product(String name,String catalogNumber, double price, int numberOfUnits){
        this.catalogNumber = catalogNumber;
        if(productsExist.containsKey(name))
            id = productsExist.get(name);
        else
            productsExist.put(name, id = idCounter++);
        this.name = name;
        this.price = price;
        this.numberOfUnits = numberOfUnits;
    }


    public void setCatalogNumber(String catalogNumber){
        this.catalogNumber = catalogNumber;
    }

    public void setNumberOfUnits(int numberOfUnits){
        this.numberOfUnits = numberOfUnits;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public int getId(){
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String toString(){
        return "Name: " + name + " Id in the system: " + id + " Supplier's catalog number: " + catalogNumber + " Price: " + price + " Number of units: " + numberOfUnits;
    }
}
