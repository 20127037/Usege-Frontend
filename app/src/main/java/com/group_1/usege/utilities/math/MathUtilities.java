package com.group_1.usege.utilities.math;

public class MathUtilities {

    public static double kbToGb(long kb)
    {
        return kb/1048576.0;
    }

    public static long gbToKb(long gb)
    {
        return gb * 1024 * 1024;
    }
}
