package com.dajie.push.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Map;

public class JsonUtil {

    private static ObjectMapper mapper=new ObjectMapper();

    static{
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <T> String toJson(T o){
        try{
            return mapper.writeValueAsString(o);
        }catch(Exception e){
            return null;
        }
    }

    public static <T> T fromJson(String jsonstr,Class<T> clazz){
        try{
            return mapper.readValue(jsonstr,clazz);
        }catch(Exception e){
            return null;
        }
    }

    public static Map<String, Object> jsonToMap(String data){
        try{
            return mapper.readValue(data,Map.class);
        }catch(Exception e){
            return null;
        }
    }

}