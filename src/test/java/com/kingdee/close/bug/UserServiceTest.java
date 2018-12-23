package com.kingdee.close.bug;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.druid.util.StringUtils;
import com.kingdee.uranus.UranusServer;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.core.exception.ParameterException;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.service.BugService;
import com.kingdee.uranus.service.UserService;

@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UranusServer.class)
public class UserServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BugService bugService;

	@Test
	public void testSyncUser() throws ClientProtocolException, IOException, BusinessException, ParameterException {
		List<User> allUsers = userService.getAllUsers();

		for (User user : allUsers) {
			if (StringUtils.isEmpty(user.getDmpUserAccount())) {
				List<User> users = bugService.getUsersByUserName(user.getUserNickname());
				if (users != null & users.size() == 1) {
					user.setDmpUserAccount(users.get(0).getDmpUserAccount());
					user.setDmpUserId(users.get(0).getDmpUserId());

					userService.updateUser(user);
				} else if (users.size() > 1) {

					Set<String> collect = users.stream().map(v -> v.getDmpUserAccount()).collect(Collectors.toSet());

					if (collect.size() == 1) {
						logger.warn("找到重名的用户,userId相同：{}", user.getUserNickname());

						user.setDmpUserAccount(users.get(0).getDmpUserAccount());
						user.setDmpUserId(users.get(0).getDmpUserId());

						userService.updateUser(user);

					} else {
						logger.warn("找到重名的用户,userId不同：{}", user.getUserNickname());

						// 处理所有的用户
//						for (User addUser : users) {
//							BeanUtils.copyProperties(user, addUser, "dmpUserAccount", "dmpUserId", "userId");
//							userService.addUser(addUser);
//						}
//
//						userService.updateUserStatus(user.getUserId(), 1);
					}

				} else {
					logger.warn("未找到用户：{}", user.getUserNickname());
				}
			}

		}
	}
}
