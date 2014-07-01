package com.dajie.message.elasticsearch.map.utils;

import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.dajie.common.dictionary.DictEnum;
import com.dajie.common.dictionary.DictManager;
import com.dajie.common.dictionary.model.Dict;
import com.dajie.common.dictionary.model.DictItem;

public class DictionaryUtils {
	
	
	/**
	 * 转换以逗号隔开的cityIds成为以逗号隔开的城市名称
	 * */
	public static String exchageCityIdsToCityNames(String cids)
	{
		if(StringUtils.isNotEmpty(cids))
		{
			
			Map<Integer, DictItem> cityMap = DictManager.getInstance().getDict(DictEnum.DICT_CITY).getMap();
			StringBuilder stringBuilder = new StringBuilder();
			String [] cityIdList = StringUtils.split(cids, ",");
			for (String cityId : cityIdList) {
				if ((StringUtils.isNumeric(cityId))
						&& (cityMap.containsKey(Integer.valueOf(cityId)))) {
					String name = ((DictItem) cityMap.get(Integer.valueOf(cityId)))
							.getName();
					stringBuilder.append(name).append(",");
				}
			}
			if (stringBuilder.length() > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				return stringBuilder.toString();
			}
		}
		return null;
	}

	/**
	 * 将枚举型薪水转换为数字
	 * */
	public static int exchangeSalaryCharToSalaryPrise(String salaryString) throws Exception
	{
		String salary = null;
		int result = 0;
		if(StringUtils.isNotEmpty(salaryString))
		{
				salary = DictManager.getInstance().getDict(DictEnum.DICT_SALARY).getNamesByIds(salaryString).replaceAll("及以上", "");
				
				if(StringUtils.isNotEmpty(salary)&&!salary.equals("面议"))
				{
					result = Integer.valueOf(salary);
				}
				else
				{
					result = 1;
				}
		}
		return result;
	}
	
	/**将枚举型专业转换成专业名称*/
	public static String exchangeIndustryIdToIndustryName(String industryIds)
	{
		Dict dict = DictManager.getInstance().getDict(DictEnum.DICT_INDUSTRY);
		return dict.getNamesByIds(industryIds, ",", null);
	}
	
	public static int exchangeExperienceIdToExperienceName(int experience)
	{
		
		String experienceString = null;
		Dict dict = DictManager.getInstance().getDict(DictEnum.DICT_EXPERIENCE);
		experienceString = dict.getNamesByIds(experience+"");

		if(StringUtils.isEmpty(experienceString)||experienceString.equals("不限")) return 9999;
		
		char year = experienceString.charAt(0);

		return year-'0';
	}
	
	public static String exchangeMajorIdToMajorName(String majorIds)
	{

		
		Dict dict = DictManager.getInstance().getDict(DictEnum.DICT_MAJOR);
		return  dict.getNamesByIds(majorIds, ",");

	}
	
	public static String exchangeSchoolIdToSchoolName(String schoolIds)
	{
		Dict dict = DictManager.getInstance().getDict(DictEnum.DICT_SCHOOL);
		return dict.getNamesByIds(schoolIds, ",");
	}
	
	public static String exchangeEducationIdToEducationName(String educationDegree)
	{
		Dict dict = DictManager.getInstance().getDict(DictEnum.DICT_DEGREE);
		return dict.getNamesByIds(educationDegree, ",");
		
	}

}
