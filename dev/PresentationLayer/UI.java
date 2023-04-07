package PresentationLayer;

import Backend.BankAccount;
import Backend.BusinessLayer.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.DeliveryAgreements.DeliveryFixedDays;
import Backend.BusinessLayer.Pair;
import Backend.BusinessLayer.Product;
import Backend.ServiceLayer.OrderService;
import Backend.ServiceLayer.SupplierService;
import com.google.gson.Gson;

import java.util.*;

public class UI {
    private OrderService orderService;
    private SupplierService supplierService;
    private Scanner sc;
    private Gson gson;
    public UI(){
        orderService = new OrderService();
        supplierService = new SupplierService();
        sc = new Scanner(System.in);
        gson = new Gson();
    }

    private void printMethods(){
        System.out.println("Hello to SUPER-LI suppliers system!\nchoose action from the menu, by clicking the number of the action\n");
        System.out.println("1. add supplier");
        System.out.println("2. remove Supplier");
        System.out.println("3. set supplier's name");
        System.out.println("4. set supplier's bn number");
    }

    public void run(){
        printMethods();

        int choice = sc.nextInt();
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
        }
    }

    private void addSupplier(){
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
        while (true){
            System.out.println("enter field or enter exit to submit list of fields");
            String field = sc.nextLine();
            if(field.equals("exit"))
                break;
            fields.add(field);
        }
        System.out.println("enter supplier's contact's info");
        Map<String, Pair<String, String>> contactsInfo = new HashMap<>();
        while (true){
            System.out.println("enter contact info:<name> <email> <phone number>. or enter exit to submit all contact's info");
            String input = sc.nextLine();
            if(input.equals("exit"))
                break;
            String[] contactInfo = input.split(" ");
            contactsInfo.put(contactInfo[0], new Pair<>(contactInfo[1], contactInfo[2]));
        }
        System.out.println("enter supplier's products");
        List<Product> products = new LinkedList<>();
        while (true){
            System.out.println("enter product:<name> <supplier's catalog number> <price> <number of units>. or enter exit submit the products");
            String input = sc.nextLine();
            if(input.equals("exit"))
                break;
            String[] product = input.split(" ");
            try {
                products.add(new Product(product[0], product[1], Double.parseDouble(product[2]), Integer.parseInt(product[3])));
            }
            catch (Exception exception){
                System.out.println("invalid input");
            }
        }
        System.out.println("enter the delivery agreement with the supplier");
        System.out.println("does he have transport? y/n");
        boolean haveTransport = sc.nextLine().equals("y");
        System.out.println("does he arrives in fixed days? y/n");
        String input = sc.nextLine();
        DeliveryAgreement deliveryAgreement = null;
        if(input.equals("y")){
            List<Integer> days = new LinkedList<>();
            while (true){
                System.out.println("enter day in the week (0 - sunday, 6 - saturday), or enter exit to submit");
                input = sc.nextLine();
                if(input.equals("exit"))
                    break;
                try {
                    days.add(Integer.parseInt(input));
                }
                catch (Exception e){
                    System.out.println("invalid input");
                }
            }
            deliveryAgreement = new DeliveryFixedDays(haveTransport, days);
        }
        else {
            System.out.println("enter the number of days takes from order to supplier visit");
            int numOfDays = sc.nextInt();
            deliveryAgreement = new DeliveryByInvitation(haveTransport, numOfDays);
        }
        System.out.println(gson.fromJson(supplierService.addSupplier(name, bnNumber, bankAccount, paymentMethod, fields,
                contactsInfo, products, deliveryAgreement), Response.class).getMsg() + "\n");
    }

    private void removeSupplier(){
        System.out.println("enter bn number of the supplier to be removed");
        String bnNumber = sc.nextLine();
        System.out.println(gson.fromJson(supplierService.removeSupplier(bnNumber), Response.class).getMsg()+"\n");
    }

    private void setSupplierName(){
        System.out.println("enter bn number of the supplier");
        String bnNumber = sc.nextLine();
        System.out.println("enter new name");
        String name = sc.nextLine();
        System.out.println(gson.fromJson(supplierService.setSupplierName(bnNumber, name), Response.class).getMsg());
    }

    private void setSupplierBnNumber(){
        System.out.println("enter bn number of the supplier");
        String bnNumber = sc.nextLine();
        System.out.println("enter new bn number");
        String newBnNumber = sc.nextLine();
        System.out.println(gson.fromJson(supplierService.setSupplierBnNumber(bnNumber, newBnNumber), Response.class).getMsg());
    }














}
