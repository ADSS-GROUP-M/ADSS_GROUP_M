package businessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;

import java.util.*;

public class Category {
    private final String categoryName;
    // map<catalog_number, Product obj>
    private final List<Product> productsRelated;


    //map<category_name, Category obj>
    private final Map<String, Category> subCategories;


    public Category(String nameCategory, List<Category> subcategories){
        this.categoryName = nameCategory;
        this.subCategories = new HashMap<String, Category>();
        this.productsRelated = new ArrayList<Product>();
        if(!subcategories.isEmpty()){
            for(Category category: subcategories)
                subCategories.put(category.getCategoryName(), category);
        }
    }

    public void addProductToCategory(Product product){
        if(!isProductIDRelated(product.getCatalogNumber(),product.getBranch()))
            productsRelated.add(product);
    }


    public Boolean isProductIDRelated(String catalog_number, Branch branch){
        for(Product product: productsRelated){
            if(product.getBranch() == branch && product.getCatalogNumber().equals(catalog_number))
                return true;
        }
        return false;
    }

    public String getCategoryName(){return this.categoryName;}

    public Boolean isRelatedProductEmpty(){return productsRelated.isEmpty();}

    public List<Product> getProductsRelated(Branch branch){
        List<Product> productsRelatedList = new ArrayList<Product>();
        for(Product product : productsRelated){
            if(product.getBranch() == branch)
                productsRelatedList.add(product);
        }
        return productsRelatedList;
    }

//            for(Product productRelated: productsRelated){
//        if(productRelated.getBranch() == product.getBranch() && productRelated.getCatalogNumber() == product.getCatalogNumber())
//            productsRelated.remove(product);
//    }
    public void removeProduct(Product product){
        Iterator<Product> iterator = productsRelated.iterator();
        while (iterator.hasNext()) {
            Product productRelated = iterator.next();
            if (productRelated.getBranch() == product.getBranch() && productRelated.getCatalogNumber().equals(product.getCatalogNumber())) {
                iterator.remove();
            }
        }

    }

    public boolean isSubcategory(String subcategoryName){
        return subCategories.containsKey(subcategoryName);
    }

    public Map<String, Category> getSubcategories(){return subCategories;}
    public void removeSubCategory(String subcategoryName){
        if(isSubcategory(subcategoryName))
            subCategories.remove(subcategoryName);
    }

    public void addSubcategories(List<Category> subcategories){
        if(!subcategories.isEmpty()){
            for(Category category: subcategories)
                subCategories.put(category.getCategoryName(), category);
        }
    }

    private String  getSubCategoriesName(){
        if(subCategories.isEmpty())
            return "there is no subCategory";
        else{
            StringBuilder ans = new StringBuilder("[ ");
            for(Category subcategory: subCategories.values()){
                ans.append(subcategory.getCategoryName()).append(" ,");
            }
            ans = new StringBuilder(ans.substring(0, ans.length() - 1) + " ]");
            return ans.toString();
        }
    }
    @Override
    public String toString() {
        return "Category{" +
                "nameCategory='" + categoryName + '\'' +
                ", subCategories=" + getSubCategoriesName() +
                '}';
    }
}
