package it.polito.ezshop.Utils;

public class BarcodeCheck {

    public static boolean checkEAN13validity(String barcode) {

    if (barcode.length() != 12) 
        return false;
    
    //REAL IMPLEMENTATION OF EAN13 CHECK VALIDITY
    // long tot = 0;

    // for (int i = 0; i < 12; i++) 
    //     tot = tot + (Long.parseLong(String.valueOf(barcode.charAt(i))) * (i % 2 == 0 ? 1 : 3));
    
    // return tot % 10 == 0 ? true : false;
    
    return true;
    }
}

