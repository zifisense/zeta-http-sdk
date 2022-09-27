package com.zifisense.zeta.http.api.util;

import java.util.Objects;

/**
 * @author PomZWJ
 * @date 2022-09-27
 */
public class StringUtils {
    public static boolean isEmpty(String val){
        if(Objects.isNull(val) || val.trim().equals("") ){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNotEmpty(String val){
        return !isEmpty(val);
    }
}
