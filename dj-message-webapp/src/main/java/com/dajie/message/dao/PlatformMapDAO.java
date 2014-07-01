package com.dajie.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.PlatformMap;

/**
 * 
 * @author li.hui
 * 
 */
@DBBean
public interface PlatformMapDAO {
	static final String TABLE = "platform_map";

	static final String select_sql = "select mapId,userId,platformType,platformUid,accessToken,secretToken,tokenType,source,status,createTime,updateTime from "
			+ TABLE + " ";

	@Insert("insert into "
			+ TABLE
			+ " (userId,platformType,platformUid,accessToken,secretToken,tokenType,source,status,createTime) "
			+ " values(#{platformMap.userId},#{platformMap.platformType},#{platformMap.platformUid},#{platformMap.accessToken},#{platformMap.secretToken},#{platformMap.tokenType},#{platformMap.source},#{platformMap.status},#{platformMap.createTime})")
	int Insert(@Param("platformMap") PlatformMap platformMap);

	@Delete("delete from " + TABLE
			+ " where userId = #{userId} and platformType=#{platformType}")
	int delete(@Param("userId") int userId,
			@Param("platformType") int platformType);

	@Select(select_sql + " where userId=#{userId}")
	List<PlatformMap> getPlatformMapsByUserId(@Param("userId") int userId);

	@Select(select_sql
			+ " where platformUid=#{platformUid} and platformType=#{platformType}")
	PlatformMap getPlatformMap(@Param("platformType") int platformType,
			@Param("platformUid") String platformUid);

	@Select(select_sql
			+ "  where userId=#{userId} and platformType=#{platformType}")
	PlatformMap getPlatformMapByUserIdAndType(@Param("userId") int userId,
			@Param("platformType") int platformType);

	@Update("update "
			+ TABLE
			+ " set accessToken=#{accessToken} where userId=#{userId} and platformType=#{platformType}")
	int updateToken(@Param("userId") int userId,
			@Param("accessToken") String accessToken,
			@Param("platformType") int platformType);

	@Update("update " + TABLE + " set accessToken=#{accessToken},platformUid=#{platformUid} where userId and platformType=#{platformType}")
	int updateByUserIdAndType(@Param("userId") int userId,
			@Param("platformUid") String platformUid,
			@Param("accessToken") String accessToken,
			@Param("platformType") int platformType);

}
