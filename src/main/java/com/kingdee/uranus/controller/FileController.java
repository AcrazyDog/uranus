package com.kingdee.uranus.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.redis.RedisPool;
import com.kingdee.uranus.util.ImageUtil;
import com.kingdee.uranus.util.ShellUtils;
import com.kingdee.uranus.util.ShellUtils.CommandResult;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("api/file")
public class FileController {

	private static final String redis_url = "172.17.4.94";

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResultMap uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		logger.info("begin uploadFile >>>>>>>>>");

		if (!file.isEmpty()) {
			try {
				String contextPath = request.getServletContext().getRealPath("/");
				logger.info("contextPath is {}", contextPath);

				// TODO
				// 暂时写死
				String dirPath = "/data/uranus";

				// 文件存放服务端的位置
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 写文件到服务器
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());

				logger.info("serverFile path:{}", serverFile.getAbsolutePath());

				file.transferTo(serverFile);
				logger.info("You successfully uploaded file {}", file.getOriginalFilename());

				// 执行脚本，同步jar到远程服务器，并启动远程服务
				String command = "expect " + dirPath + File.separator + "remote.sh";

				logger.info("command is :{}", command);
				CommandResult execCommand = ShellUtils.execCommand(command, false, true);

				if (!StringUtils.isEmpty(execCommand.errorMsg)) {
					throw new Exception(execCommand.errorMsg);
				}
				// 执行shell脚本，启动服务
				command = "sh " + dirPath + File.separator + "run.sh restart";
				logger.info("command is :{}", command);
				execCommand = ShellUtils.execCommand(command, false, true);

				if (!StringUtils.isEmpty(execCommand.errorMsg)) {
					throw new Exception(execCommand.errorMsg);
				}
				return ResultMap.ok();
			} catch (Exception e) {
				return ResultMap.error(e.getMessage());
			}
		} else {
			return ResultMap.error("the file was empty");
		}
	}

	@RequestMapping(value = "/uploadFileAndUploadDmp", method = RequestMethod.POST)
	@ResponseBody
	public ResultMap uploadFileAndUploadDmp(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		logger.info("begin uploadFile >>>>>>>>>");

		if (!file.isEmpty()) {
			try {

				// 文件存放服务端的位置
				File dir = new File("//172.17.4.95:2828");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 写文件到服务器
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());

				logger.info("serverFile path:{}", serverFile.getAbsolutePath());

				file.transferTo(serverFile);
				logger.info("You successfully uploaded file {}", file.getOriginalFilename());

				// 将文件存放到redis
				String base64 = ImageUtil.encodeImgageToBase64(file.getInputStream());
				JedisPool redisPool = RedisPool.getRedisPool(redis_url, 7000);
				Jedis jedis = redisPool.getResource();
				String key = UUID.randomUUID().toString();
				jedis.set(key, base64);
				return ResultMap.ok(key);
			} catch (Exception e) {
				return ResultMap.error(e.getMessage());
			}
		} else {
			return ResultMap.error("the file was empty");
		}
	}
}
