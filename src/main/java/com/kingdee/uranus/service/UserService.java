package com.kingdee.uranus.service;

import java.util.List;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.core.exception.ParameterException;
import com.kingdee.uranus.model.User;

/**
 * UserService
 * 
 * @author wangfan
 * @date 2017-3-24 下午4:12:31
 */
public interface UserService {

	/**
	 * 查询所有用户
	 */
	public PageResult<User> getUsers(int pageNum, int pageSize, Integer status, String searchKey, String searchValue);

	/**
	 * 根据账号查询用户
	 */
	public List<User> getUserByAccount(String userAccount);

	/**
	 * 根据Dmp账号查询用户
	 */
	public User getUserByDmpUserAccount(String userAccount);

	/**
	 * 根据id查询用户
	 */
	public User getUserById(String userId);

	/**
	 * 添加用户
	 */
	public boolean addUser(User user) throws BusinessException;

	/**
	 * 修改用户
	 */
	public boolean updateUser(User user);

	/**
	 * 修改用户状态
	 */
	public boolean updateUserStatus(String userId, int status) throws ParameterException;

	/**
	 * 修改密码
	 */
	public boolean updateUserPsw(String userId, String newPassword);

	/**
	 * 删除用户
	 */
	public boolean deleteUser(String userId) throws BusinessException;

	/**
	 * 获得所以用户
	 * 
	 * @return
	 */
	public List<User> getAllUsers();
}
