package com.point.clddock.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DDecimalUtil {
    private static final DecimalFormat formatInt = new DecimalFormat("#");
    private static final DecimalFormat format = new DecimalFormat("#.##");
    public static Integer toInt(BigDecimal bigDecimal){
        return Integer.parseInt(formatInt.format(bigDecimal));
    }
    public static String format (BigDecimal bigDecimal){
        return format.format(bigDecimal);
    }
    public static BigDecimal setScale (BigDecimal bigDecimal){
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

}
