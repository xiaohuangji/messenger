package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.OrFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.dajie.message.elasticsearch.map.constants.ElasticSearchConstant;

public class ELasticSearchQueryBuilderUtil {
	
	public static QueryBuilder queryListBuilderForJob(double lat,double lon,int distance,int distanceStep,int distanceUnit,
			String jobName,String companyName,String tag,int salaryMin,int salaryMax,
			int experience,int educationMin,int industry, int jobType,String address,String district,String city,String uniqueId,
			String nameOfPOI)
	{
		
		//query
		BoolQueryBuilder  boolq = QueryBuilders.boolQuery();
		
		//只取未过期
		QueryBuilder rangeQueryForExpire = QueryBuilders.rangeQuery("endTime").gte(System.currentTimeMillis());
		boolq.must(rangeQueryForExpire);
		
		//必须是未被禁止的职位
		boolq.must(QueryBuilders.termQuery("status", 0));
		
		if(StringUtils.isNotEmpty(jobName))
		{
			QueryBuilder matchQueryForJobName = QueryBuilders.matchQuery("name", jobName);
			boolq.must(matchQueryForJobName);
		}
		
		if(StringUtils.isNotEmpty(companyName))
		{
			QueryBuilder matchQueryForCompanyName = QueryBuilders.matchQuery("companyName", companyName);
			boolq.must(matchQueryForCompanyName);
		}
		
		if(StringUtils.isNotEmpty(tag))
		{
			if(tag.contains(","))
			{
				tag.replaceAll(",", " ");
			}
			
			QueryBuilder matchQueryForTag = QueryBuilders.matchQuery("tag", tag);
			boolq.must(matchQueryForTag);
		}
		
		
		if(industry!=0)
		{
			QueryBuilder matchQueryForIndustry = QueryBuilders.termQuery("industry", industry);
			boolq.must(matchQueryForIndustry);
		}
		
		if(jobType!=0)
		{
			QueryBuilder matchQueryForJobType = QueryBuilders.termQuery("jobType", jobType);
			boolq.must(matchQueryForJobType);
		}
		
		if(StringUtils.isNotEmpty(address))
		{
			QueryBuilder matchQueryForAddress = QueryBuilders.matchQuery("address", address);
			boolq.must(matchQueryForAddress);
		}
			
		if(StringUtils.isNotEmpty(district))
		{
			MultiMatchQueryBuilder matchQueryForDistrict = QueryBuilders.multiMatchQuery(district, "city","district");
			boolq.must(matchQueryForDistrict);
		}
		
		if(StringUtils.isNotEmpty(city))
		{
			MultiMatchQueryBuilder matchQueryForCity = QueryBuilders.multiMatchQuery(city, "city","province");
			boolq.must(matchQueryForCity);
		}
		
		if(StringUtils.isNotEmpty(nameOfPOI))
		{
			QueryBuilder matchQueryForNameOfPOI = QueryBuilders.matchQuery("nameOfPOI", nameOfPOI);
			boolq.must(matchQueryForNameOfPOI);
		}
		if(StringUtils.isNotEmpty(uniqueId))
		{
			QueryBuilder termQueryForNameOfPOI = QueryBuilders.termQuery("uidOfPOI", uniqueId);
			boolq.must(termQueryForNameOfPOI);
		}
		
		if(experience!=0)
		{
			QueryBuilder termQueryForExperience = QueryBuilders.termQuery("experience", experience);
			boolq.must(termQueryForExperience);
		}
		
		if(educationMin > 0)
		{
			if(educationMin >=50)
			{
				QueryBuilder rangeQueryForEducation = QueryBuilders.termQuery("education", educationMin);
				boolq.must(rangeQueryForEducation);
			}else
			{
				QueryBuilder rangeQueryForEducation = QueryBuilders.rangeQuery("education").lte(educationMin);
				boolq.must(rangeQueryForEducation);
			}
		}
		
		QueryBuilder rangeQueryForJobId = QueryBuilders.rangeQuery("jobId").gt(0);
		boolq.must(rangeQueryForJobId);
		
		//筛选地理位置
		FilterBuilder geoFilterBuilder = null;
		if(lat!=0&&lon!=0)
		{
			String unit = ElasticSearchConstant.DISTANCE_UNIT_METER_STRING;
			
			if(distanceUnit == ElasticSearchConstant.DISTANCE_UNIT_METER_CODE)
			{
				unit = ElasticSearchConstant.DISTANCE_UNIT_METER_STRING;
			}else if(distanceUnit == ElasticSearchConstant.DISTANCE_UNIT_KILOMETER_CODE)
			{
				unit = ElasticSearchConstant.DISTANCE_UNIT_KILOMETER_STRING;
			}

			if(distanceStep > 0)
			{
				geoFilterBuilder = FilterBuilders.geoDistanceRangeFilter("location").
					point(lat, lon)
					.from(distance+unit)
					.to((distance + distanceStep)+unit);
			}else
			{
				geoFilterBuilder = FilterBuilders.geoDistanceRangeFilter("location").
						point(lat, lon)
						.from(distance+unit);
			}
		}
		//筛选薪水 最小值小于上限 或 最大值大于下线
		if(salaryMax ==0) salaryMax = Integer.MAX_VALUE;
		FilterBuilder salaryMinBuilder = FilterBuilders.rangeFilter("salaryMin").lte(salaryMax);
		FilterBuilder salaryMaxBuilder = FilterBuilders.rangeFilter("salaryMax").gte(salaryMin);
		
		FilterBuilder salaryBuilder = FilterBuilders.andFilter(salaryMinBuilder,salaryMaxBuilder);
		
		
		FilterBuilder filterBuilder = null;
		//filter汇总
		if(geoFilterBuilder != null)
		{
			filterBuilder = FilterBuilders.andFilter(geoFilterBuilder,salaryBuilder).cache(true);
		}
		else
		{
			filterBuilder = FilterBuilders.andFilter(salaryBuilder).cache(true);
		}
		
		
		
		//汇总query和filter
		QueryBuilder query = QueryBuilders.filteredQuery(boolq, filterBuilder);
		
		return query;
	}
	
	public static QueryBuilder queryListBuilderForPerson(boolean showAll,int searchUserCompanyId,int showCollegue,
			double lat, double lon,
			int distance, int distanceStep,int distanceUnit,String userName, String position,String companyName,
			int industry, int jobType, int experienceMin,int experienceMax, int educationMin,
			String tag, int gender, int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address,String district,String city)
		{
		//query
		BoolQueryBuilder  boolq = QueryBuilders.boolQuery();
		
		if(StringUtils.isNotEmpty(userName))
		{
			QueryBuilder matchQueryForUserName = QueryBuilders.matchQuery("name", userName);
			boolq.must(matchQueryForUserName);
		}
		
		if(StringUtils.isNotEmpty(position))
		{
			QueryBuilder matchQueryForPosition = QueryBuilders.matchQuery("position", position);
			boolq.must(matchQueryForPosition);
		}
		
		if(StringUtils.isNotEmpty(companyName))
		{
			QueryBuilder matchQueryForCompanyName = QueryBuilders.matchQuery("companyName", companyName);
			boolq.must(matchQueryForCompanyName);
		}
		
		if(StringUtils.isNotEmpty(tag))
		{
			if(tag.contains(","))
			{
				tag.replaceAll(",", " ");
			}
			
			QueryBuilder matchQueryForTag = QueryBuilders.matchQuery("tag", tag);
			boolq.must(matchQueryForTag);
		}
		
		
		if(industry!=0)
		{
			QueryBuilder matchQueryForIndustry = QueryBuilders.termQuery("industry", industry);
			boolq.must(matchQueryForIndustry);
		}
		
		if(StringUtils.isNotEmpty(school))
		{
			QueryBuilder matchQueryForSchool = QueryBuilders.matchQuery("school", school);
			boolq.must(matchQueryForSchool);
		}
		
		if(StringUtils.isNotEmpty(schoolLabel))
		{
			QueryBuilder matchQueryForSchoolLabel = QueryBuilders.matchQuery("schoolLabel", schoolLabel);
			boolq.must(matchQueryForSchoolLabel);
		}
		
		if(StringUtils.isNotEmpty(major))
		{
			QueryBuilder matchQueryForMajor = QueryBuilders.matchQuery("major", major);
			boolq.must(matchQueryForMajor);
		}
		
		if(jobType!=0)
		{
			QueryBuilder matchQueryForJobType = QueryBuilders.termQuery("jobType", jobType);
			boolq.must(matchQueryForJobType);
		}
		
		if(StringUtils.isNotEmpty(address))
		{
			QueryBuilder matchQueryForAddress = QueryBuilders.matchQuery("address", address);
			boolq.must(matchQueryForAddress);
		}
			
		if(StringUtils.isNotEmpty(district))
		{
			MultiMatchQueryBuilder matchQueryForDistrict = QueryBuilders.multiMatchQuery(district, "city","district");
			boolq.must(matchQueryForDistrict);
		}
		
		if(StringUtils.isNotEmpty(city))
		{
			MultiMatchQueryBuilder matchQueryForCity = QueryBuilders.multiMatchQuery(city, "city","province");
			boolq.must(matchQueryForCity);
		}

		if(gender>0)
		{
			QueryBuilder termQueryForGender = QueryBuilders.termQuery("gender", gender);
			boolq.must(termQueryForGender);
		}
		if(isVerified>0)
		{
			QueryBuilder termQueryForVerified = QueryBuilders.termQuery("isVerified", isVerified);
			boolq.must(termQueryForVerified);
		}
		if(isStudent>0)
		{
			QueryBuilder termQueryForStudent = QueryBuilders.termQuery("isStudent", isStudent);
			boolq.must(termQueryForStudent);
		}
		
		if(experienceMin > 0 || experienceMax >0)
		{
			QueryBuilder termQueryForExperience = null;
			
			if(experienceMax == 0)
			{
				termQueryForExperience = QueryBuilders.rangeQuery("experience").gte(experienceMin);
			}else
			{
				termQueryForExperience = QueryBuilders.rangeQuery("experience").gte(experienceMin).lte(experienceMax);
			}
			
			boolq.must(termQueryForExperience);
		}
		
		if(educationMin > 0)
		{
			
			if(educationMin >=50)
			{
				QueryBuilder rangeQueryForEducation = QueryBuilders.termQuery("education", educationMin);
				boolq.must(rangeQueryForEducation);
			}else
			{
				QueryBuilder rangeQueryForEducation = QueryBuilders.rangeQuery("education").lte(educationMin);
				boolq.must(rangeQueryForEducation);
			}
		}

		
		QueryBuilder rangeQueryForUserId = QueryBuilders.rangeQuery("userId").gt(0);
		boolq.must(rangeQueryForUserId);
		
		
		
		//筛选地理位置
		FilterBuilder geoFilterBuilder = null;
		
		if(lat!=0&&lon!=0)
		{
			String unit = ElasticSearchConstant.DISTANCE_UNIT_METER_STRING;
			
			if(distanceUnit == ElasticSearchConstant.DISTANCE_UNIT_METER_CODE)
			{
				unit = ElasticSearchConstant.DISTANCE_UNIT_METER_STRING;
			}else if(distanceUnit == ElasticSearchConstant.DISTANCE_UNIT_KILOMETER_CODE)
			{
				unit = ElasticSearchConstant.DISTANCE_UNIT_KILOMETER_STRING;
			}
			
			if(distanceStep > 0)
			{
				geoFilterBuilder = FilterBuilders.geoDistanceRangeFilter("location").
					point(lat, lon)
					.from(distance+unit)
					.to((distance + distanceStep)+unit);
			}else
			{
				geoFilterBuilder = FilterBuilders.geoDistanceRangeFilter("location").
						point(lat, lon)
						.from(distance+unit);
			}
		}
		
		FilterBuilder showAllFilter = null;
		if(!showAll)
		{
			
			FilterBuilder schoolExistsFilter = FilterBuilders.existsFilter("school");
			FilterBuilder majorExistsFilter = FilterBuilders.existsFilter("major");
			FilterBuilder companyExistsFilter = FilterBuilders.existsFilter("companyName");
			FilterBuilder positionExistsFilter = FilterBuilders.existsFilter("position");

			FilterBuilder schoolAndMajorExistsFilter = FilterBuilders.andFilter(schoolExistsFilter,majorExistsFilter);
			FilterBuilder companyAndPositionExistsFilter = FilterBuilders.andFilter(companyExistsFilter,positionExistsFilter);
			
			FilterBuilder orExistsFilter = FilterBuilders.orFilter(companyAndPositionExistsFilter,schoolAndMajorExistsFilter);
			
			
			FilterBuilder visibleFilter = FilterBuilders.rangeFilter("isVisibility").from(1);
			
			FilterBuilder showableFilter = FilterBuilders.termFilter("isShowable",0);
			
			if(searchUserCompanyId!=0)
			{
				FilterBuilder companyNotSameFilter = FilterBuilders.queryFilter(QueryBuilders.termQuery("companyId", searchUserCompanyId));
				FilterBuilder colleagueVisibilityFilter = FilterBuilders.termFilter("isColleagueVisibility", 0);
				
				FilterBuilder colleagueFilter = null;
				if(showCollegue > 0)
				{
					colleagueFilter = FilterBuilders.notFilter(FilterBuilders.andFilter(companyNotSameFilter,colleagueVisibilityFilter));
				}else
				{
					colleagueFilter = FilterBuilders.notFilter(companyNotSameFilter);
				}
				
				showAllFilter = FilterBuilders.andFilter(orExistsFilter,visibleFilter,showableFilter,colleagueFilter);
			}else
			{
				showAllFilter = FilterBuilders.andFilter(orExistsFilter,visibleFilter,showableFilter);
			}
			
			
		}
		
		
		
		//filter汇总
		FilterBuilder filterBuilder = null;
		
		if(geoFilterBuilder!=null&&showAllFilter!=null)
		{
			filterBuilder = FilterBuilders.andFilter(geoFilterBuilder,showAllFilter).cache(true);
		}
		else if(geoFilterBuilder!=null&&showAllFilter == null)
		{
			filterBuilder = FilterBuilders.andFilter(geoFilterBuilder).cache(true);
		}
		else if(geoFilterBuilder== null&&showAllFilter != null)
		{
			filterBuilder = FilterBuilders.andFilter(showAllFilter).cache(true);
		}else
		{
			filterBuilder = FilterBuilders.existsFilter("userId");
		}
		
		//汇总query和filter
		QueryBuilder query = QueryBuilders.filteredQuery(boolq, filterBuilder);
		
		return query;
		}
	
	public static QueryBuilder countPersonByMapQuery(Map<String,Object> map)
	{
		BoolQueryBuilder  boolQuery = QueryBuilders.boolQuery();
		
		for(Entry<String, Object> e : map.entrySet())
		{
				MatchQueryBuilder KVMatcher= QueryBuilders.matchQuery(e.getKey(), e.getValue());
				boolQuery.must(KVMatcher);
		}
		
		FilterBuilder schoolExistsFilter = FilterBuilders.existsFilter("school");
		FilterBuilder majorExistsFilter = FilterBuilders.existsFilter("major");
		FilterBuilder companyExistsFilter = FilterBuilders.existsFilter("companyName");
		FilterBuilder positionExistsFilter = FilterBuilders.existsFilter("position");

		FilterBuilder schoolAndMajorExistsFilter = FilterBuilders.andFilter(schoolExistsFilter,majorExistsFilter);
		FilterBuilder companyAndPositionExistsFilter = FilterBuilders.andFilter(companyExistsFilter,positionExistsFilter);
		
		OrFilterBuilder orExistsFilter = FilterBuilders.orFilter(companyAndPositionExistsFilter,schoolAndMajorExistsFilter);
		
		FilterBuilder filterBuilder = FilterBuilders.andFilter(orExistsFilter).cache(true);
		
		QueryBuilder query = QueryBuilders.filteredQuery(boolQuery, filterBuilder);
		
		return query;
		
	}
	
	public static SortBuilder sorterBuider(double lat,double lon)
	{
		if(lat == 0 && lon == 0) return null;
		
		//排序
		SortBuilder sortBuilder = SortBuilders.geoDistanceSort("location")
				.point(lat, lon).order(SortOrder.ASC).unit(DistanceUnit.METERS);
		
		return sortBuilder;
	}
	

}
