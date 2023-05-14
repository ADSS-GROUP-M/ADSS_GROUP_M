package Fronend.PresentationLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.BankAccount;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryFixedDays;
import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.BusinessLayer.SuppliersModule.Product;
import Backend.ServiceLayer.SuppliersModule.AgreementService;
import Backend.ServiceLayer.SuppliersModule.BillOfQuantitiesService;
import Backend.ServiceLayer.SuppliersModule.OrderService;
import Backend.ServiceLayer.SuppliersModule.SupplierService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class UI {
    private OrderService orderService;
    private SupplierService supplierService;
    private AgreementService agreementService;
    private BillOfQuantitiesService billOfQuantitiesService;
    private Scanner sc;
    private Gson gson;
    public UI(){
        orderService = new OrderService();
        supplierService = new SupplierService();
        agreementService = new AgreementService();
        billOfQuantitiesService = new BillOfQuantitiesService();
        sc = new Scanner(System.in);
        gson = new Gson();
    }

    private void printMethods(){
        System.out.println("Hello to SUPER-LI suppliers system!\nchoose action from the menu, by clicking the number of the action\n");
        System.out.println("1. add supplier");
        System.out.println("2. remove Supplier");
        System.out.println("3. set supplier's name");
        System.out.println("4. set supplier's bn number");
        System.out.println("5. set supplier's bank account");
        System.out.println("6. set supplier's payment method");
        System.out.println("7. add supplier's contact info");
        System.out.println("8. remove supplier's contact info");
        System.out.println("9. edit supplier's contact email");
        System.out.println("10. edit supplier's contact phone number");
        System.out.println("11. edit supplier's delivery agreement");
        System.out.println("12. set product supplier's catalog number");
        System.out.println("13. set product price");
        System.out.println("14. set product number of units");
        System.out.println("15. add product");
        System.out.println("16. set product discount");
        System.out.println("17. remove product discount");
        System.out.println("18. remove product discounts - remove all of its discounts");
        System.out.println("19. remove discount on total order");
        System.out.println("20. set discount on total order");
        System.out.println("21. set order of applying discounts");
        System.out.println("22. set product discounts");
        System.out.println("23. set discount on amount of products");
        System.out.println("24. remove discount on amount of products");
        System.out.println("25. get order history");
        System.out.println("26. add field");
        System.out.println("27. remove field");
        System.out.println("28. get catalog number");
        System.out.println("29. get delivery agreement");
        System.out.println("30. remove product");
        System.out.println("31. get supplier details");
        System.out.println("32. order");
        System.out.println("33. load data");
        System.out.println("34. exit");
    }

    public boolean run(){
        printMethods();

        int choice = Integer.parseInt(sc.nextLine());
        switch (choice){
            case 1:
                addSupplier();
                break;
            case 2:
                removeSupplier();
                break;
            case 3:
                setSupplierName();
                break;
            case 4:
                setSupplierBnNumber();
                break;
            case 5:
                setSupplierBankAccount();
                break;
            case 6:
                setSupplierPaymentMethod();
                break;
            case 7:
                addContactInfo();
                break;
            case 8:
                removeContactInfo();
                break;
            case 9:
                setContactsEmail();
                break;
            case 10:
                setContactsPhoneNumber();
                break;
            case 11:
                setDeliveryAgreement();
                break;
            case 12:
                setProductSuppliersCatalogNumber();
                break;
            case 13:
                setProductPrice();
                break;
            case 14:
                setProductAmount();
                break;
            case 15:
                addProduct();
                break;
            case 16:
                setProductDiscount();
                break;
            case 17:
                removeProductDiscount();
                break;
            case 18:
                removeProductDiscounts();
                break;
            case 19:
                removeDiscountOnTotalOrder();
                break;
            case 20:
                setDiscountOnTotalOrder();
                break;
            case 21:
                setOrderOfDiscounts();
                break;
            case 22:
                setProductsDiscounts();
                break;
            case 23:
                setDiscountOnAmountOfProducts();
                break;
            case 24:
                removeDiscountOnAmountOfProducts();
                break;
            case 25:
                getOrderHistory();
                break;
            case 26:
                addField();
                break;
            case 27:
                removeField();
                break;
            case 28:
                getCatalogNumber();
                break;
            case 29:
                getDeliveryAgreement();
                break;
            case 30:
                removeProduct();
                break;
            case 31:
                getSupplierDetails();
                break;
            case 32:
                order();
                break;
            case 33:
                loadData();
                break;
            case 34:
                return false;
        }
        return true;
    }

    private void addSupplier(){
        try {
            System.out.println("enter supplier's name");
            String name = sc.nextLine();
            System.out.println("enter supplier's bn number");
            String bnNumber = sc.nextLine();
            System.out.println("enter supplier's bank account :<bank> <branch> <account number>");
            String[] bankAccountString = sc.nextLine().split(" ");
            BankAccount bankAccount = new BankAccount(bankAccountString[0], bankAccountString[1], bankAccountString[2]);
            System.out.println("enter supplier's payment method");
            String paymentMethod = sc.nextLine();
            System.out.println("enter supplier's fields:");
            List<String> fields = new LinkedList<>();
            while (true) {
                System.out.println("enter field or enter exit to submit list of fields");
                String field = sc.nextLine();
                if (field.equals("exit"))
                    break;
                fields.add(field);
            }
            System.out.println("enter supplier's contact's info");
            Map<String, Pair<String, String>> contactsInfo = new HashMap<>();
            while (true) {
                System.out.println("enter contact info:<name> <email> <phone number>. or enter exit to submit all contact's info");
                String input = sc.nextLine();
                if (input.equals("exit"))
                    break;
                String[] contactInfo = input.split(" ");
                contactsInfo.put(contactInfo[0], new Pair<>(contactInfo[1], contactInfo[2]));
            }
            System.out.println("enter supplier's products");
            List<Product> products = new LinkedList<>();
            while (true) {
                System.out.println("enter product:<name> <supplier's catalog number> <price> <number of units>. or enter exit submit the products");
                String input = sc.nextLine();
                if (input.equals("exit"))
                    break;
                String[] product = input.split(" ");
                try {
                    products.add(new Product(product[0], product[1], Double.parseDouble(product[2]), Integer.parseInt(product[3])));
                } catch (Exception exception) {
                    System.out.println("invalid input");
                }
            }
            System.out.println("enter the delivery agreement with the supplier");
            System.out.println("does he have transport? y/n");
            boolean haveTransport = sc.nextLine().equals("y");
            System.out.println("does he arrives in fixed days? y/n");
            String input = sc.nextLine();
            DeliveryAgreement deliveryAgreement = null;
            if (input.equals("y")) {
                List<Integer> days = new LinkedList<>();
                while (true) {
                    System.out.println("enter day in the week (0 - sunday, 6 - saturday), or enter exit to submit");
                    input = sc.nextLine();
                    if (input.equals("exit"))
                        break;
                    try {
                        days.add(Integer.parseInt(input));
                    } catch (Exception e) {
                        System.out.println("invalid input");
                    }
                }
                deliveryAgreement = new DeliveryFixedDays(haveTransport, days);
            } else {
                System.out.println("enter the number of days takes from order to supplier visit");
                try {
                    int numOfDays = Integer.parseInt(sc.nextLine());
                    deliveryAgreement = new DeliveryByInvitation(haveTransport, numOfDays);
                } catch (Exception e) {
                    System.out.println("invalid input");
                }

            }
            System.out.println(gson.fromJson(supplierService.addSupplier(name, bnNumber, bankAccount, paymentMethod, fields,
                    contactsInfo, products, deliveryAgreement), Response.class).getMsg() + "\n");
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    private void removeSupplier(){
        try {
            System.out.println("enter bn number of the supplier to be removed");
            String bnNumber = sc.nextLine();
            billOfQuantitiesService.removeBillOfQuantities(bnNumber);
            agreementService.removeAgreement(bnNumber);
            System.out.println(gson.fromJson(supplierService.removeSupplier(bnNumber), Response.class).getMsg()+"\n");
        }
        catch (Exception e){
            System.out.println("invalid input");
        }

    }

    private void setSupplierName(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter new name");
            String name = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.setSupplierName(bnNumber, name), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }

    }

    private void setSupplierBnNumber(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter new bn number");
            String newBnNumber = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.setSupplierBnNumber(bnNumber, newBnNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }

    }


    public void setSupplierBankAccount(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter new bank account: <bank> <branch> <account number>");
            String[] bankAccount = sc.nextLine().split(" ");
            System.out.println(gson.fromJson(supplierService.setSupplierBankAccount(bnNumber, bankAccount[0], bankAccount[1], bankAccount[2]), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setSupplierPaymentMethod(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("select payment method:\n1. net\n2. net 30 EOM\n3. net 60 EOM");
            int choice = Integer.parseInt(sc.nextLine());
            String paymentMethod = null;
            switch (choice){
                case 1:
                    paymentMethod = "net";
                    break;
                case 2:
                    paymentMethod = "net 30 EOM";
                    break;
                case 3:
                    paymentMethod = "net 60 EOM";
                    break;
            }
            System.out.println(gson.fromJson(supplierService.setSupplierPaymentMethod(bnNumber, paymentMethod), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void addContactInfo(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter contact info: <name> <email> <phone number>");
            String[] contactInfo = sc.nextLine().split(" ");
            System.out.println(gson.fromJson(supplierService.addContactInfo(bnNumber, contactInfo[0], contactInfo[1], contactInfo[2]), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void removeContactInfo(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter contact email");
            String email = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.removeContactInfo(bnNumber, email), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setContactsEmail(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter contact email");
            String email = sc.nextLine();
            System.out.println("enter new email");
            String newEmail = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.setContactsEmail(bnNumber, email, newEmail), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setContactsPhoneNumber(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter contact email");
            String email = sc.nextLine();
            System.out.println("enter new phone number");
            String phoneNumber = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.setContactsPhoneNumber(bnNumber, email, phoneNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setDeliveryAgreement(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("does he have transport? y/n");
            boolean haveTransport = sc.nextLine().equals("y");
            System.out.println("does he arrives in fixed days? y/n");
            String input = sc.nextLine();
            if (input.equals("y")) {
                List<Integer> days = new LinkedList<>();
                while (true) {
                    System.out.println("enter day in the week (0 - sunday, 6 - saturday), or enter exit to submit");
                    input = sc.nextLine();
                    if (input.equals("exit"))
                        break;
                    try {
                        days.add(Integer.parseInt(input));
                    } catch (Exception e) {
                        System.out.println("invalid input");
                    }
                }
                System.out.println(gson.fromJson(agreementService.setFixedDeliveryAgreement(bnNumber, haveTransport, days), Response.class).getMsg());
            } else {
                System.out.println("enter the number of days takes from order to supplier visit");
                try {
                    int numOfDays = Integer.parseInt(sc.nextLine());
                    System.out.println(gson.fromJson(agreementService.setByInvitationDeliveryAgreement(bnNumber, haveTransport, numOfDays), Response.class).getMsg());
                } catch (Exception e) {
                    System.out.println("invalid input");
                }
            }
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setProductSuppliersCatalogNumber(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product's id");
            String catalogNumber = sc.nextLine();
            System.out.println("enter new catalog number");
            String suppliersCatalogNumber = sc.nextLine();
            System.out.println(gson.fromJson(agreementService.setSuppliersCatalogNumber(bnNumber, catalogNumber, suppliersCatalogNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setProductPrice(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product's id");
            String catalogNumber = sc.nextLine();
            System.out.println("enter new price");
            double price = Double.parseDouble(sc.nextLine());
            System.out.println(gson.fromJson(agreementService.setProductPrice(bnNumber,catalogNumber, price), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setProductAmount(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product's id");
            String catalogNumber = sc.nextLine();
            System.out.println("enter amount");
            int amount = Integer.parseInt(sc.nextLine());
            System.out.println(gson.fromJson(agreementService.setProductAmount(bnNumber,catalogNumber, amount), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void addProduct(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product's name");
            String name = sc.nextLine();
            System.out.println("enter catalog number");
            String catalogNumber = sc.nextLine();
            System.out.println("enter price");
            double price = Double.parseDouble(sc.nextLine());
            System.out.println("enter number of units");
            int amount = Integer.parseInt(sc.nextLine());
            System.out.println(gson.fromJson(agreementService.addProduct(bnNumber,name, catalogNumber, price, amount), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setProductDiscount(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product id");
            String catalogNumber = sc.nextLine();
            System.out.println("enter number of units");
            int amount = Integer.parseInt(sc.nextLine());
            System.out.println("enter the discount\n\t1.discount in percentage\n\t2.discount in cash");
            int choice = Integer.parseInt(sc.nextLine());
            Discount discount = null;
            if(choice == 1){
                System.out.println("enter percentage of discount");
                double percentage = Double.parseDouble(sc.nextLine());
                discount = new PercentageDiscount(percentage);
            }
            else if(choice == 2){
                System.out.println("enter amount of cash");
                double cash = Double.parseDouble(sc.nextLine());
                discount = new CashDiscount(cash);
            }
            System.out.println(gson.fromJson(billOfQuantitiesService.setProductDiscount(bnNumber, catalogNumber, amount, discount), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void removeProductDiscount(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product id");
            String catalogNumber = sc.nextLine();
            System.out.println("enter number of units");
            int amount = Integer.parseInt(sc.nextLine());
            System.out.println(gson.fromJson(billOfQuantitiesService.removeProductDiscount(bnNumber, catalogNumber, amount), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void removeProductDiscounts(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product id");
            String catalogNumber = sc.nextLine();
            System.out.println(gson.fromJson(billOfQuantitiesService.removeProductDiscounts(bnNumber, catalogNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }


    public void removeDiscountOnTotalOrder(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println(gson.fromJson(billOfQuantitiesService.removeDiscountOnTotalOrder(bnNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setDiscountOnTotalOrder(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter price to activate discount");
            double price = Double.parseDouble(sc.nextLine());
            System.out.println("enter the discount\n\t1.discount in percentage\n\t2.discount in cash");
            int choice = Integer.parseInt(sc.nextLine());
            Discount discount = null;
            if(choice == 1){
                System.out.println("enter percentage of discount");
                double percentage = Double.parseDouble(sc.nextLine());
                discount = new PercentageDiscount(percentage);
            }
            else if(choice == 2){
                System.out.println("enter amount of cash");
                double cash = Double.parseDouble(sc.nextLine());
                discount = new CashDiscount(cash);
            }
            System.out.println(gson.fromJson(billOfQuantitiesService.setDiscountOnTotalOrder(bnNumber, price, discount), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setOrderOfDiscounts(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter the order:\n\t1. apply amount discount before total-order-price discount\n\t" +
                    "2. apply total-order-price discount before amount discount");
            int choice = Integer.parseInt(sc.nextLine());
            if(choice == 1)
                System.out.println(gson.fromJson(billOfQuantitiesService.setOrderOfDiscounts(bnNumber, true), Response.class).getMsg());
            else if(choice == 2)
                System.out.println(gson.fromJson(billOfQuantitiesService.setOrderOfDiscounts(bnNumber, false), Response.class).getMsg());
            else
                throw new RuntimeException("invalid input");
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void setProductsDiscounts(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            Map<String, Map<Integer, Discount>> productsDiscounts = new HashMap<>();
            while (true){
                System.out.println("enter product id to set discount or exit to submit");
                String input = sc.nextLine();
                if(input.equals("exit"))
                    break;
                String catalogNumber = (input);
                productsDiscounts.put(catalogNumber, new HashMap<>());
                while(true) {
                    System.out.println("enter the amount to purchase from this product to activate discount or exit to submit");
                    input = sc.nextLine();
                    if(input.equals("exit"))
                        break;
                    int amount = Integer.parseInt(input);
                    if(amount < 0)
                        throw new RuntimeException("amount can't be less than 0");
                    System.out.println("enter the discount\n\t1.discount in percentage\n\t2.discount in cash");
                    int choice = Integer.parseInt(sc.nextLine());
                    Discount discount = null;
                    if (choice == 1) {
                        System.out.println("enter percentage of discount");
                        double percentage = Double.parseDouble(sc.nextLine());
                        discount = new PercentageDiscount(percentage);
                    } else if (choice == 2) {
                        System.out.println("enter amount of cash");
                        double cash = Double.parseDouble(sc.nextLine());
                        discount = new CashDiscount(cash);
                    }
                    else
                        throw new RuntimeException("discount can't be null");
                    productsDiscounts.get(catalogNumber).put(amount, discount);
                }
            }
            System.out.println(gson.fromJson(billOfQuantitiesService.setProductsDiscounts(bnNumber,productsDiscounts), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }


    public void setDiscountOnAmountOfProducts(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter amount to activate discount");
            int amount = Integer.parseInt(sc.nextLine());
            System.out.println("enter the discount\n\t1.discount in percentage\n\t2.discount in cash");
            int choice = Integer.parseInt(sc.nextLine());
            Discount discount = null;
            if(choice == 1){
                System.out.println("enter percentage of discount");
                double percentage = Double.parseDouble(sc.nextLine());
                discount = new PercentageDiscount(percentage);
            }
            else if(choice == 2){
                System.out.println("enter amount of cash");
                double cash = Double.parseDouble(sc.nextLine());
                discount = new CashDiscount(cash);
            }
            System.out.println(gson.fromJson(billOfQuantitiesService.setDiscountOnAmountOfProducts(bnNumber, amount, discount), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void removeDiscountOnAmountOfProducts(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println(gson.fromJson(billOfQuantitiesService.removeDiscountOnAmountOfProducts(bnNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void getOrderHistory(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            Type responseOfList = new TypeToken<Response<List<Order>>>(){}.getType();
            List<Order> orders = ((Response<List<Order>>)gson.fromJson(orderService.getOrderHistory(bnNumber), responseOfList)).getReturnValue();
            if(orders == null)
                System.out.println("no orders have been ordered from this supplier");
            else {
                String res = "ORDER HISTORY:";
                for (Order order : orders)
                    res += "\n\t" + order.toString();
                System.out.println(res);
            }
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void addField(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter field");
            String field = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.addField(bnNumber, field), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void removeField(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter field");
            String field = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.removeField(bnNumber, field), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void getCatalogNumber(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product's id");
            String catalogNumber = sc.nextLine();
            System.out.println(gson.fromJson(agreementService.getSuppliersCatalogNumber(bnNumber, catalogNumber), Response.class).getReturnValue());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void getDeliveryAgreement(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println(gson.fromJson(agreementService.getDeliveryAgreement(bnNumber), Response.class).getReturnValue());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void removeProduct(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println("enter product id");
            String catalogNumber = sc.nextLine();
            System.out.println(gson.fromJson(agreementService.removeProduct(bnNumber, catalogNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }


    public void getSupplierDetails(){
        try {
            System.out.println("enter bn number of the supplier");
            String bnNumber = sc.nextLine();
            System.out.println(gson.fromJson(supplierService.getSupplierDetails(bnNumber), Response.class).getMsg());
        }
        catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void order(){
        /*
        try {
            Map<Integer, Integer> order = new HashMap<>();
            try {
                while (true) {
                    System.out.println("enter product id to purchase or exit to submit");
                    String input = sc.nextLine();
                    if (input.equals("exit"))
                        break;
                    int productId = Integer.parseInt(input);
                    System.out.println("enter amount to order");
                    int amount = Integer.parseInt(sc.nextLine());
                    order.put(productId, amount);
                }
            }
            catch (Exception e){
                System.out.println("invalid input");
            }
            Type orderResponse = new TypeToken<Response< Map<String, Pair<Map<Integer, Integer>, Double>>>>(){}.getType();
//                Response<Map<String, Pair<Map<Integer, Integer>, Double>>> finalOrderResponse =
//                        gson.fromJson(orderService.order(order), orderResponse);
            Response<Map<String, Pair<Map<Integer, Integer>, Double>>> finalOrderResponse =
                    gson.fromJson(orderService.order(order), orderResponse);
            if(finalOrderResponse.getReturnValue() == null)
                throw new RuntimeException(finalOrderResponse.getMsg());
            Map<String, Pair<Map<Integer, Integer>, Double>> finalOrder = finalOrderResponse.getReturnValue();
            double finalPrice = 0;
            String orderString = "ORDER DETAILS:";
            for(Map.Entry<String, Pair<Map<Integer, Integer>, Double>> supplierOrder : finalOrder.entrySet()){
                String supplierBnNumber = supplierOrder.getKey();
                String supplierName = gson.fromJson(supplierService.getSuppliersName(supplierBnNumber), Response.class).getMsg();
                Map<Integer, Integer> supplierOrderDetails = supplierOrder.getValue().getFirst();
                double finalSupplierPrice = supplierOrder.getValue().getSecond();
                String supplierOrderString = "ORDER:";
                int counter = 1;
                for(Map.Entry<Integer, Integer> productOrder : supplierOrderDetails.entrySet())
                    supplierOrderString += "\n\t\t\t" + counter++ + ". Product id: " + productOrder.getKey() + " Amount: " + productOrder.getValue();
                orderString += "\n\tSUPPLIER:\n\t\tName: " + supplierName + "\n\t\tBn number: " + supplierBnNumber +
                        "\n\t\t" + supplierOrderString + "\n\t\tFinal price for supplier: " + finalSupplierPrice;
                finalPrice += finalSupplierPrice;
            }
            orderString += "\n\tFINAL PRICE: " + finalPrice;
            System.out.println(orderString);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

         */
    }
    public void loadData(){
        System.out.println(gson.fromJson(supplierService.loadData(), Response.class).getMsg());
    }











}
