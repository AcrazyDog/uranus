package com.kingdee.uranus.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.core.exception.ParameterException;
import com.kingdee.uranus.core.utils.Md5Util;
import com.kingdee.uranus.core.utils.UUIDUtil;
import com.kingdee.uranus.mapper.UserMapper;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public PageResult<User> getUsers(int pageNum, int pageSize, Integer status, String searchKey, String searchValue) {
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<User> users = userMapper.selectUsers(status, searchKey, searchValue);
		PageResult<User> result = new PageResult<User>();
		result.setData(users);
		result.setCount(startPage.getTotal());
		return result;
	}

	@Override
	public boolean addUser(User user) throws BusinessException {
		if (getUserByAccount(user.getUserAccount()) != null) {
			throw new BusinessException("账号已经存在");
		}
		user.setUserId(UUIDUtil.randomUUID8());

		String decryptMd5 = Md5Util.MD5(user.getUserPassword());

		user.setUserPassword(decryptMd5);
		user.setUserStatus(0);
		user.setCreateTime(new Date());
		return userMapper.insert(user) > 0;
	}

	@Override
	public boolean updateUser(User user) {
		return userMapper.updateByPrimaryKeySelective(user) > 0;
	}

	@Override
	public boolean updateUserStatus(String userId, int status) throws ParameterException {
		if (status != 0 && status != 1) {
			throw new ParameterException();
		}
		User user = new User();
		user.setUserId(userId);
		user.setUserStatus(status);
		return userMapper.updateByPrimaryKeySelective(user) > 0;
	}

	@Override
	public List<User> getUserByAccount(String userAccount) {
		return userMapper.selectUserByAccount(userAccount);
	}

	@Override
	public boolean updateUserPsw(String userId, String password) {
		User user = new User();
		user.setUserId(userId);
		String decryptMd5 = Md5Util.MD5(password);
		user.setUserPassword(decryptMd5);
		return userMapper.updateByPrimaryKeySelective(user) > 0;
	}

	@Override
	public User getUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public boolean deleteUser(String userId) throws BusinessException {
		try {
			return userMapper.deleteByPrimaryKey(userId) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("用户已被关联");
		}
	}

	@Override
	public List<User> getAllUsers() {
		return userMapper.selectAllUsers();
	}

	@Override
	public User getUserByDmpUserAccount(String userAccount) {
		return userMapper.selectByDmpAccount(userAccount);
	}
}
