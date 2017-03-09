package br.cin.ufpe.dass.matchers.util;

import java.net.URI;
import java.text.DecimalFormat;

/**
 * Created by diego on 07/03/17.
 */
public class FormatHelper {

    public static boolean isStringNumber(String number) {
        boolean isNumber = true;

        try {
            Double.parseDouble(number);
        } catch (Exception var3) {
            isNumber = false;
        }

        return isNumber;
    }

    public static boolean isValidURI(String uri) {
        boolean isValid = true;

        try {
            URI.create(uri);
        } catch (Exception var3) {
            isValid = false;
        }

        return isValid;
    }

    public static boolean isEmpty(String text) {
        boolean isEmpty = false;
        if(text == null || text.trim().length() == 0) {
            isEmpty = true;
        }

        return isEmpty;
    }

    public static String formatFilePathToApi(String filePath, boolean removeSpaces) {
        String result = null;
        if(filePath != null) {
            result = replaceSlashs(filePath);
            if(removeSpaces) {
                result = result.replace(" ", "%20");
            }
        }

        return result;
    }

    public static String formatFilePathToCMD(String filePath) {
        String result = null;
        if(filePath != null) {
            result = "file:///" + replaceSlashs(filePath).replace(" ", "%20");
        }

        return result;
    }

    public static String replaceSlashs(String filePath) {
        String result = null;
        if(filePath != null) {
            result = filePath.replace("\\", "/");
        }

        return result;
    }

    public static double formatDouble(double value) {
        DecimalFormat nf = new DecimalFormat("#.00");
        String number = nf.format(value);
        if(number.charAt(0) == 44) {
            number = "0" + number;
        }

        double formatted = Double.parseDouble(number.replace(',', '.'));
        return formatted;
    }

}