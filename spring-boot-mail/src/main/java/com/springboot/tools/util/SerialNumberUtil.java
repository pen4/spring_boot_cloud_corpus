package com.springboot.tools.util;

import org.apache.commons.lang3.RandomUtils;


public class SerialNumberUtil {
    public static Long create() {
        return RandomUtils.nextLong(0, 100000000);
    }
}
