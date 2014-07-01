package com.dajie.message.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.ContactView;

@DBBean
public interface RecommendTaskDAO {

	static String join_sql = "select p.platformUid, u.name, u.email, u.mobile, count(distinct(a.fromUserId)) as personCount from push_push_info a "
			+ "join user_base u on a.userId = u.userId "
			+ "join platform_map p on u.userId = p.userId ";

	static String where_condition = "where a.createTime > #{startTime} and a.createTime < #{endTime} "
			+ "and a.fromUserId is not null "	
			+ "and u.lastLogin is null "
			+ "and p.source = 'Dajie import' "
			+ "group by p.platformUid, u.name, u.email, u.mobile";


	@Select(join_sql + where_condition)
	List<ContactView> getRecommendUser(@Param("startTime") Date startTime,
			@Param("endTime") Date endTime);

	
}