package com.alemdianov.api.utils;


import java.util.Arrays;

import static com.alemdianov.api.services.BaseService.BASE_URL;


public class Utils {

    public static String buildUrl(String path) {
        return BASE_URL + "/" + path;
    }

    public static String buildUrl(Object... paths) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        Arrays.stream(paths).forEach(path -> sb.append("/").append(path));

        return sb.toString();

    }

}
