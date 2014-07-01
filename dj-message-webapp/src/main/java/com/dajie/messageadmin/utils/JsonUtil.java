package com.dajie.messageadmin.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	
	 private static Gson gson= new GsonBuilder().create();
	
	 public static <T> String toJson(T o){
		return gson.toJson(o);
	 }
	
	 public static <T> T fromJson(String jsonstr,Class<T> clazz){
		return gson.fromJson(jsonstr, clazz);
	 }
}
