package Backend.DataAccessLayer.InventoryModule;

public class ProductPairBranchDAO {
    String branch_name;
    String catalog_number;
    double original_store_price;
    int notification_min;

    public ProductPairBranchDAO(String branch_name, String catalog_number, double original_store_price, int notification_min) {
        this.branch_name = branch_name;
        this.catalog_number = catalog_number;
        this.original_store_price = original_store_price;
        this.notification_min = notification_min;
    }

}
