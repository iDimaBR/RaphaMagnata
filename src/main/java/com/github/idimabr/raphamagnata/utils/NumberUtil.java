package com.github.idimabr.raphamagnata.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberUtil {

    private static String format(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
        return decimalFormat.format(value);
    }

    public static String getFormat(double value) {
        String[] simbols = new String[]{"Centavos", "Mil", "Milhões", "Bilhões", "Trilhões", "Quadrilhões", "Quinquilhões", "Sextilhões", "Septilhões", "Octilhões", "Nonilhões", "Decilhões"};
        int index;
        for (index = 0; value / 1000.0 >= 1.0; value /= 1000.0, ++index) {
        }
        return format(value) + " " + simbols[index];
    }

}
