/**
 * Copyright (c) 2006-2015 Kingdee Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Kingdee. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Kingdee,http://www.Kingdee.com.
 *  
 */
package com.kingdee.close.bug;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Administrator
 * @date 2018年6月19日 下午5:03:07
 * @version
 */
public class ElasticsearchTest {

	private static Logger logger = LoggerFactory.getLogger(ElasticsearchTest.class);

	public final static String HOST = "172.17.52.49";

	public final static int PORT = 9300;// http请求的端口是9200，客户端是9300

	private static AbstractClient client;
	static {
		try {
			client = new PreBuiltTransportClient(org.elasticsearch.common.settings.Settings.EMPTY)
					.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 测试Elasticsearch客户端连接
	 * 
	 * @Title: test1
	 * @author sunt
	 * @date 2017年11月22日
	 * @return void
	 * @throws UnknownHostException
	 */
	@SuppressWarnings("resource")
	@Test
	public void test1() throws UnknownHostException {
		// 创建客户端
		client = new PreBuiltTransportClient(org.elasticsearch.common.settings.Settings.EMPTY)
				.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));
		logger.info("Elasticsearch connect info:" + client.toString());
		// 关闭客户端
		client.close();
	}

	public static void main(String[] args) throws UnknownHostException {
		ElasticsearchTest.addDate();
		ElasticsearchTest.getDataByID("45");

		// 关闭客户端
		client.close();
	}

	/**
	 * 插入单条数据
	 * {"account_number":44,"balance":34487,"firstname":"Aurelia","lastname":
	 * "Harding","age":37,"gender":"M","address":"502 Baycliff
	 * Terrace","employer":"Orbalix","email":"aureliaharding@orbalix.
	 * com","city":"Yardville","state":"DE"}
	 * 
	 * @param client
	 */
	public static void addDate() {
		logger.info("Elasticsearch connect info:" + client.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_number", 45);
		map.put("balance", 34487);
		map.put("firstname", "Aurelia");
		map.put("lastname", "Harding");
		map.put("age", 37);
		map.put("gender", "M");
		map.put("address", "浙江省杭州市江干区人民公园中国梦");
		map.put("gender", "M");
		map.put("employer", "小李");
		map.put("email", "aureliaharding@orbalix.com");
		map.put("city", "Yardville");
		map.put("state", "DE");

		try {
			// json格式
			// String json=JSON.toJSONString(map);
			// IndexResponse response = client.prepareIndex("accounts",
			// "person","47").setSource(json, XContentType.JSON).get();
			// map格式
			IndexResponse response = client.prepareIndex("accounts", "person", "45") // UUID.randomUUID().toString()
					.setSource(map).execute().actionGet();
			System.out.println(response.toString());
			System.out.println("写入数据结果=" + response.status().getStatus() + "！id=" + response.getId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 获取单条数据
	public static void getDataByID(String id) {
		try {
			// 搜索数据
			GetResponse response = client.prepareGet("accounts", "person", id).execute().actionGet();
			// 输出结果
			System.out.println(response.getSource());
			// 关闭client
			// client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
