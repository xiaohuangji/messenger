package com.dajie.message.elasticsearch.map.utils.baidu;

import com.dajie.message.model.map.LocationObject;

import static java.lang.Math.*;

public class BaiduGeoPointConvertUtil {
	
	
	private static double lat = 31.22997;
	private static double lon = 121.640756;
	public static double x_pi = lat * lon / 180.0;
	
	public static LocationObject BD09ToGCJ02(double bd_lat,double bd_lon)
	{
		
		LocationObject lo = new LocationObject();
		
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;  
	    double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);  
	    double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);  
	  
	    lo.setLng(z * cos(theta));
	    lo.setLat(z * sin(theta));

	    return lo;
	}
	
    public static LocationObject GCJ02ToBD09(double gg_lat, double gg_lon)
	{
    	LocationObject lo = new LocationObject();
    	
    	double x = gg_lon, y = gg_lat;
    	double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
    	double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		
		lo.setLng(bd_lon);
	    lo.setLat(bd_lat);

	    return lo;
	}
    

}
