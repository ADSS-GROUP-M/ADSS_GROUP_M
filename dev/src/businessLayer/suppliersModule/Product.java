package businessLayer.suppliersModule;

public class Product {
    /***
     * unique catalogNumber in the system
     */
    private final String catalogNumber;
    /***
     * the catalog number in the supplier's system
     */
    private String suppliersCatalogNumber;
    private double price;
    private int numberOfUnits;

    public Product(String catalogNumber, String suppliersCatalogNumber, double price, int numberOfUnits){
        this.suppliersCatalogNumber = suppliersCatalogNumber;
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.numberOfUnits = numberOfUnits;
    }


    public void setSuppliersCatalogNumber(String suppliersCatalogNumber){
        this.suppliersCatalogNumber = suppliersCatalogNumber;
    }

    public void setNumberOfUnits(int numberOfUnits){
        this.numberOfUnits = numberOfUnits;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public String getCatalogNumber(){
        return catalogNumber;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public String getSuppliersCatalogNumber() {
        return suppliersCatalogNumber;
    }

    public String toString(){
        return "Catalog number: " + catalogNumber + ", Supplier's catalog number: " + suppliersCatalogNumber + ", Price: " + price + ", Number of units: " + numberOfUnits;
    }
}
