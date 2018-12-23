package picture;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年7月10日 下午8:39:01
 * @version
 */
public class DownPicture {

	public static void main(String[] args) {

		List<String> urls = getUrls();

		String prexUrl = "http://172.17.5.184:83/fileserver/";
		for (String url : urls) {
			downloadPicture(prexUrl + url, url.replaceAll("/", "linsan"));
		}
	}

	private static List<String> getUrls() {

		List<String> result = new ArrayList<String>();

		Connection conn = getConn();
		String sql = "SELECT furl0 FROM t_bas_pictureresource";
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

	private static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		// String url =
		// "jdbc:mysql://172.18.5.30:3306/sys_cloud_30_devbizkd?useUnicode=true&characterEncoding=utf8";
		String url = "jdbc:mysql://172.17.52.49:3306/feiyongzu_20180608?useUnicode=true&characterEncoding=utf8";
		String username = "root";
		String password = "123456";
		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 
	 * <p>
	 * 链接url下载图片
	 * </p>
	 * 
	 * @param urlList
	 * @param name
	 *
	 */
	private static void downloadPicture(String urlList, String name) {
		URL url = null;
		try {
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			String imageName = "D:/picture/old/" + name;
			File file = new File(imageName);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			byte[] context = output.toByteArray();
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
