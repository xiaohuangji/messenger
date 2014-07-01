package com.dajie.message.service;

import java.util.List;

import com.dajie.message.model.map.PersonMapObject;
import com.dajie.message.model.user.UserBase;

public interface IPersonMapObjectService {
	
	
	public String addNewPersonMapObject(UserBase userBase,double lat,double lon);
	
	public String addNewPersonMapObject(PersonMapObject personMapObject);
	
	public List<PersonMapObject> getPersonMapObjectById(int userId);
	
	public int updatePersonMapObject(int userId);
	
	
	public int deletePersonMapObject(int userId);

}
