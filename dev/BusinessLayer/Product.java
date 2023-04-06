package BusinessLayer;

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

    public Product(String name, int id, String catalogNumber, double price, int numberOfUnits){
        this.catalogNumber = catalogNumber;
        this.id = id;
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
}
