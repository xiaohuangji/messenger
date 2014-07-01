/**
 * 
 */
package com.dajie.message.service;

import java.util.List;

import com.dajie.message.annotation.mcp.ParamNullable;
import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.job.JobModel;
import com.dajie.message.model.map.JobMapObject;
import com.dajie.message.model.map.PersonMapObject;
import com.dajie.message.model.map.PointOnMap;
import com.dajie.message.model.user.UserBase;

/**
 * @author tianyuan.zhu
 *
 */
@RestBean
public interface IPointOnMapService {
	
	/**
	 * 通过坐标等获取地图上的点
	 * @param latitude 纬度
	 * @param longitude经度
	 * @param zoomLevel 缩放等级（1：城市级别 2：地区级别 3：详细级别）
	 * @param pointType 该点类型(1:职位 2:人才)
	 * @param distance 从该距离开始拉取（单位：m）
	 * @param distanceStep 拉取从distance开始到distance+distanceStep距离范围内的内容
	 * */
	public List<PointOnMap> getPointsOnMap(int _userId,double latitude,double longitude,int zoomLevel,@ParamNullable(true)int pointType,int distance,int distanceStep);
	
	
	
	/**
	 * 通过坐标等获取地图上的职位
	 * @param lat 纬度
	 * @param lon经度
	 * @param distance 从该距离开始拉取（单位：m）
	 * @param pageSize	一页最多有多少条职位
	 * @param jobName 职位名称字段
	 * @param companyName 公司名称字段
	 * @param tag 职位描述
	 * @param salaryMin 薪水最小
	 * @param salaryMax 薪水最大
	 * @param experience 工作经验(枚举)
	 * @param educationMin 最低学历
	 * @param industry 该职位所属行业
	 * @param address 地址
	 * @param district 区域名称
	 * @param city 城市名称
	 * @param uniqueId poi的唯一id
	 * @param nameOfPOI poi的名称
	 * */
	public List<JobMapObject> searchJob(int _userId,double lat,double lon,@ParamNullable(true)int distance,@ParamNullable(true)int distanceStep,@ParamNullable(true)int pageNumber,
			@ParamNullable(true)int pageSize,@ParamNullable(true)String jobName,
			@ParamNullable(true)String companyName,@ParamNullable(true)String tag,@ParamNullable(true)int salaryMin,@ParamNullable(true)int salaryMax,
			@ParamNullable(true)int experience,@ParamNullable(true)int educationMin,@ParamNullable(true)int industry,@ParamNullable(true)int jobType,
			@ParamNullable(true)String address,@ParamNullable(true)String district,@ParamNullable(true)String city,@ParamNullable(true)String uniqueId,
			@ParamNullable(true)String nameOfPOI);
	
	public List<PointOnMap> searchJobOnMap(int _userId,double lat,double lon,@ParamNullable(true)int zoomLevel,@ParamNullable(true)int distance,@ParamNullable(true)int distanceStep,@ParamNullable(true)String jobName,
			@ParamNullable(true)String companyName,@ParamNullable(true)String tag,@ParamNullable(true)int salaryMin,@ParamNullable(true)int salaryMax,
			@ParamNullable(true)int experience,@ParamNullable(true)int educationMin,@ParamNullable(true)int industry,@ParamNullable(true)int jobType,
			@ParamNullable(true)String address,@ParamNullable(true)String district,@ParamNullable(true)String city,@ParamNullable(true)String uniqueId,
			@ParamNullable(true)String nameOfPOI);
	
	
	public List<PersonMapObject> searchPerson(int _userId,double lat,double lon,@ParamNullable(true)int distance,@ParamNullable(true)int distanceStep,
			@ParamNullable(true)int pageNumber,@ParamNullable(true)int pageSize,@ParamNullable(true)String userName,@ParamNullable(true)String position,@ParamNullable(true)String companyName,
			@ParamNullable(true)int industry,@ParamNullable(true)int jobType,@ParamNullable(true)int experienceMin,@ParamNullable(true)int experienceMax,@ParamNullable(true)int educationMin,
			@ParamNullable(true)String tag,@ParamNullable(true)int gender,@ParamNullable(true)int isVerified,@ParamNullable(true)int isStudent,
			@ParamNullable(true)String address,@ParamNullable(true)String school,@ParamNullable(true)String schoolLabel,@ParamNullable(true)String major,
			@ParamNullable(true)String district,@ParamNullable(true)String city);
	
	public List<PointOnMap> searchPersonOnMap(int _userId,double lat,double lon,@ParamNullable(true)int zoomLevel,@ParamNullable(true)int distance,@ParamNullable(true)int distanceStep,
			@ParamNullable(true)String userName,@ParamNullable(true)String position,@ParamNullable(true)String companyName,
			@ParamNullable(true)int industry,@ParamNullable(true)int jobType,@ParamNullable(true)int experienceMin,@ParamNullable(true)int experienceMax,@ParamNullable(true)int educationMin,
			@ParamNullable(true)String tag,@ParamNullable(true)int gender,@ParamNullable(true)int isVerified,@ParamNullable(true)int isStudent,
			@ParamNullable(true)String address,@ParamNullable(true)String school,@ParamNullable(true)String schoolLabel,@ParamNullable(true)String major,
			@ParamNullable(true)String district,@ParamNullable(true)String city);
	
	//获取一些红包
	public List<PointOnMap> getSomeCampaignPoint(int _userId);
	
	public List<Integer> searchPersonWithoutGeoInfo(@ParamNullable(true)String userName,@ParamNullable(true)String position,@ParamNullable(true)String companyName,
			@ParamNullable(true)int industry,@ParamNullable(true)int jobType,@ParamNullable(true)int experienceMin,@ParamNullable(true)int experienceMax,@ParamNullable(true)int educationMin,
			@ParamNullable(true)String tag,@ParamNullable(true)int gender,@ParamNullable(true)int isVerified,@ParamNullable(true)int isStudent,
			@ParamNullable(true)String address,@ParamNullable(true)String school,@ParamNullable(true)String schoolLabel,@ParamNullable(true)String major,
			@ParamNullable(true)String district,@ParamNullable(true)String city,int pageNumber,int pageSize);
	

	
	/**
	 * 
	 * 对外接口
	 * */
	public int updatePersonGeoPoint(int _userId,double lat,double lon);

	
}
