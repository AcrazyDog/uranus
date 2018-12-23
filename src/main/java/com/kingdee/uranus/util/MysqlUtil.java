package com.kingdee.uranus.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年7月17日 上午9:33:56
 * @version
 */
public class MysqlUtil {

	public static List<ConnectSet> list = Lists.newArrayList();

	private static MysqlUtil util = new MysqlUtil();

	public class ConnectSet {

		private String redisUrl;

		private SQLConectInfo sQLConectInfo;

		public ConnectSet(String redisUrl, SQLConectInfo sQLConectInfo) {
			this.redisUrl = redisUrl;
			this.sQLConectInfo = sQLConectInfo;
		}

		public String getRedisUrl() {
			return redisUrl;
		}

		public void setRedisUrl(String redisUrl) {
			this.redisUrl = redisUrl;
		}

		public SQLConectInfo getsQLConectInfo() {
			return sQLConectInfo;
		}

		public void setsQLConectInfo(SQLConectInfo sQLConectInfo) {
			this.sQLConectInfo = sQLConectInfo;
		}

	}

	public class SQLConectInfo {

		private String url;

		private String username;

		private String password;

		private SQLConectInfo(String url, String username, String password) {
			super();
			this.url = url;
			this.username = username;
			this.password = password;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

	static {
		list.add(util.new ConnectSet("172.17.4.124",
				util.new SQLConectInfo(
						"jdbc:mysql://172.17.4.124:3306/sys_cloud_30_tenant?useUnicode=true&characterEncoding=utf8",
						"root", "kingdee")));
		list.add(util.new ConnectSet("172.17.51.97",
				util.new SQLConectInfo(
						"jdbc:mysql://172.17.3.105:3306/dev_biz_trunk_sys?useUnicode=true&characterEncoding=utf8",
						"root", "kingdee")));
		list.add(util.new ConnectSet("172.17.51.97",
				util.new SQLConectInfo(
						"jdbc:mysql://172.17.3.104:3306/dev_biz_branch_sys?useUnicode=true&characterEncoding=utf8",
						"root", "kingdee")));

	}

	private static Connection getConn(SQLConectInfo sqlConectInfo) {
		String driver = "com.mysql.jdbc.Driver";
		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(sqlConectInfo.getUrl(), sqlConectInfo.getUsername(),
					sqlConectInfo.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static List<Long> getUserIdByNames(ConnectSet connectSet) {

		List<Long> result = new ArrayList<Long>();

		SQLConectInfo sqlConectInfo = connectSet.getsQLConectInfo();

		Connection conn = getConn(sqlConectInfo);
		String sql = "SELECT t1.fid FROM t_sec_user t1 INNER JOIN t_sec_user_l t2 ON t1.fid = t2.fid WHERE FTRUENAME in (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, "聂康");
			pstmt.setString(2, "段友爱");
			pstmt.setString(3, "司彦浓");
			pstmt.setString(4, "杨凯");
			pstmt.setString(5, "赵诣");
			pstmt.setString(6, "钱锋");
			pstmt.setString(7, "赵鹏辉");
			pstmt.setString(8, "迟文乐");
			pstmt.setString(9, "从占启");
			pstmt.setString(10, "刘清茂");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void insertUserManager(List<Long> userIds) {
		// TODO Auto-generated method stub

	}

	public static List<String> getAllMenuIds(ConnectSet connectSet) {
		List<String> result = new ArrayList<String>();

		SQLConectInfo sqlConectInfo = connectSet.getsQLConectInfo();

		Connection conn = getConn(sqlConectInfo);
		String sql = "SELECT * FROM `t_meta_bizapp`";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}