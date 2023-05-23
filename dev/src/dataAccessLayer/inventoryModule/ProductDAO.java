package dataAccessLayer.inventoryModule;

public class ProductDAO {
    private final String catalog_number;
    private final String name;
    private String manufacture;
    private String category;

    public ProductDAO(String catalog_number, String name) {
        this.catalog_number = catalog_number;
        this.name = name;
    }

    public void setManufacture(String manufacture){
        this.manufacture = manufacture;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getCatalog_number(){return this.catalog_number;}
    public String getName() {return this.name;}
    public String getManufacture() {return this.manufacture;}
    public String GetCategory() {return this.category;}

}
