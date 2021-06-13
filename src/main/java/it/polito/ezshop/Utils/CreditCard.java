package it.polito.ezshop.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CreditCard {

    public static boolean checkCreditCard(String creditCardNumber, double amount) {

        Scanner in = null;
        try {
            in = new Scanner(new FileReader("src/main/java/it/polito/ezshop/Utils/CreditCards.txt"));
        } catch (IOException ex) {
        }

        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        String s = sb.toString();
        String[] parts = s.split(",");

        for (int i = 0; i < parts.length; i += 2) {
            if (parts[i].equals(creditCardNumber) && Double.parseDouble(parts[i + 1]) >= amount)
                return true;
        }
        return false;
    }

    public static boolean checkExistCreditCard(String creditCardNumber) {

        Scanner in = null;
        try {
            in = new Scanner(new FileReader("src/main/java/it/polito/ezshop/Utils/CreditCards.txt"));
        } catch (IOException ex) {
        }

        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        String s = sb.toString();
        String[] parts = s.split(",");

        for (int i = 0; i < parts.length; i += 2) {
            if (parts[i].equals(creditCardNumber))
                return true;
        }
        return false;
    }

}
