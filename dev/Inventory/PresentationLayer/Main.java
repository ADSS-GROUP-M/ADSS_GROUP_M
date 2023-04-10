package dev.Inventory.PresentationLayer;

import dev.Inventory.BusinessLayer.Category;
import dev.Inventory.BusinessLayer.ProductType;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Inventory CLI");
        new MainMenu().run();
    }
}
