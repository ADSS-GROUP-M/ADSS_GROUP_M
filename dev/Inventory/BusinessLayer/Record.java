package dev.Inventory.BusinessLayer;

import java.util.List;

public class Record {
    private int catalog_number;
    private int serial_number;
    private String name;
    private String branch;
    private String manufacturer;
    private double supplier_price;
    private double store_price; // Product.soldPrice != -1 ? Product.soldPrice : ProductType.originalStorePrice
    private Category category;
    private List<Category> subCategories;
    private String location;


    //catalog_number - ProductTypeID
    //serial_number - ProductID
    public Record(int catalog_number, int serial_number, String name, String branch, String manufacturer, double supplier_price, double store_price, Category category, List<Category> subCategories, String location) {
        this.catalog_number = catalog_number;
        this.serial_number = serial_number;
        this.name = name;
        this.branch = branch;
        this.manufacturer = manufacturer;
        this.supplier_price = supplier_price;
        this.store_price = store_price;
        this.category = category;
        this.subCategories = subCategories;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Record{" +
                "catalog_number=" + catalog_number +
                ", serial_number=" + serial_number +
                ", name='" + name + '\'' +
                ", branch='" + branch + '\'' +
                ", manufacturer=" + manufacturer +
                ", supplier_price=" + supplier_price +
                ", store_price=" + store_price +
                ", category=" + category.toString() +
                ", subCategories=" + subCategories.toString() +
                ", location='" + location + '\'' +
                '}';
    }
}
