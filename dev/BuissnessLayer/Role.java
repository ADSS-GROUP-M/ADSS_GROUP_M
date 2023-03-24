package dev.BuissnessLayer;

public enum Role {
    CASHIER("Cashier"),
    STOREKEEPER("Storekeeper"), //מחסנאי
    SHIFT_MANAGER("Shift Manager"),
    GENERAL_WORKER("General worker");

    private String name;

    private Role(String n){
        this.name = n;
    }
}
