package com.springboot.tools.util;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {

    private static Map<String, String> config = new HashMap() {{
        config.put("sender_address", "948453219@qq.com");
    }};

    public static String getConfigVal(String name) {
        return config.get(name);
    }
}
