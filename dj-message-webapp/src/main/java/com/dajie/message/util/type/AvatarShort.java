package com.dajie.message.util.type;

public class AvatarShort {

	
	public static String shortAvatar(String avatar){
		int index = avatar.indexOf("group");
		if(index == -1)
			index = avatar.indexOf("n/mobile_avatar/");
		if(index == -1)
			return "";
		else
			return avatar.substring(index);
	}
	
}
