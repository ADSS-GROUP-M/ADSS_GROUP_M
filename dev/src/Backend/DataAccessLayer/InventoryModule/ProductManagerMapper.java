package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.SuppliersModule.ProductsDataMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductManagerMapper {

    ProductsDataMapper productsDataMapper = new ProductsDataMapper();
    ProductItemDataMapper productItemDataMapper = new ProductItemDataMapper();
    ProductPairBranchDataMapper productPairBranchDataMapper = new ProductPairBranchDataMapper();

    public void crateProduct(String catalog_number, String branch, String name, String manufacture, double originalStorePrice){
        try {
            productsDataMapper.insert(catalog_number, name, manufacture);
            productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice);
        } catch (SQLException e) {
            //TODO: Handle the exception appropriately
        }
    }

    public void crateProductItem(String serialNumber, String catalog_number, String supplierID, String location, LocalDateTime expirationDate, String name, String manufacture, String branch, double originalStorePrice) {
        try {
            if(!productsDataMapper.isExists(catalog_number)){
                productsDataMapper.insert(catalog_number, name, manufacture);
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice);
            }
//            productItemDataMapper.insert(serialNumber, -1, );
        } catch (SQLException e) {
            //TODO: Handle the exception appropriately
        }

    }

    public void addCategoryToProduct() {

    }

}
