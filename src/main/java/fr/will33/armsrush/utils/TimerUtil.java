package fr.will33.armsrush.utils;

public class TimerUtil {

    /**
     * Format time mm:ss
     * @param seconds
     * @return
     */
    public static String format(int seconds){
        int m = seconds / 60;
        int s = seconds % 60;
        return ((m <= 9) ? ("0" + m) : m) + ":" + (s <= 9 ? ("0" + s) : s);
    }

}