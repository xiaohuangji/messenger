package com.dajie.push.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {

    private static ObjectMapper mapper=new ObjectMapper();

    public static <T> String toJson(T o){
        try{
            return mapper.writeValueAsString(o);
        }catch(Exception e){
            return null;
        }
        //return gson.toJson(o);
    }

    public static <T> T fromJson(String jsonstr,Class<T> clazz){
        try{
            return mapper.readValue(jsonstr,clazz);
        }catch(Exception e){
            return null;
        }
        //return gson.fromJson(jsonstr, clazz);
    }

    public static Map<String, Object> jsonToMap(String data){
        try{
            return mapper.readValue(data,Map.class);
        }catch(Exception e){
            return null;
        }
        //return  gson.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
    }

}