package com.kingdee.uranus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kingdee.uranus.model.User;

@Mapper
public interface UserMapper {

	int deleteByPrimaryKey(String userId);

	int insert(User record);

	User selectByPrimaryKey(String userId);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	public List<User> selectUsers(@Param("status") Integer status, @Param("searchKey") String searchKey,
			@Param("searchValue") String searchValue);

	public List<User> selectUserByAccount(String userAccount);

	public User selectByDmpAccount(String userAccount);

	List<User> selectAllUsers();
}