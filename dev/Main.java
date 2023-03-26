package dev;
import java.util.*;
import java.util.Calendar;
import java.util.Date;

import dev.BuissnessLayer.User;
import dev.BuissnessLayer.UserController;
public class Main {

    public static void main(String[] args)
    {
        Scanner reader = new Scanner(System.in);
        UserController uc = null;
        try{
        uc = UserController.getInstance();
        }catch (Exception e){System.out.println("couldnt get user controller");}
        int key = -1;
        while(true){
            String read = reader.nextLine();
            String[] input = read.split(" ",-1);
            String output = "default output";
            try{
            if(!uc.amIloggedIn(key)){
                if(input.length == 0 || !input[0].equals("login"))
                    {
                        output = "please log in first";
                       
                    }
                key = uc.login(input[1], input[2]);
                output = "login successful";
            }
            else
                output = uc.serve(key, read);
            }catch(Exception e){output = e.getMessage();}
            System.out.println(output);
        }

    }
    
}
