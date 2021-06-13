package it.polito.ezshop.Utils;

public class LuhnAlgorithm {

    public static Boolean chackCreditCard(String creditCard){
        int nDigits = creditCard.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = creditCard.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        if (!(nSum % 10 == 0))
            return false;
        else
        return true;
    }
}
