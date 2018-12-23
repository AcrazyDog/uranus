package com.kingdee.uranus.search;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年7月6日 上午9:10:20
 * @version
 */
@Component
public class SearchClient {

	@Value("${spring.elastic.host1}")
	private String host1;
	@Value("${spring.elastic.port1}")
	private int port1;
	@Value("${spring.elastic.host2}")
	private String host2;
	@Value("${spring.elastic.port2}")
	private int port2;
	@Value("${spring.elastic.host3}")
	private String host3;
	@Value("${spring.elastic.port3}")
	private int port3;

	/**
	 * logger
	 */
	private static Logger logger = LoggerFactory.getLogger(SearchClient.class);

	public TransportClient getClient() {
		TransportClient client = null;
		try {
			Settings settings = Settings.builder().put("cluster.name", "es-uranus").put("client.transport.sniff", true)
					.build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(host1), port1))
					.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(host2), port2))
					.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(host3), port3));
		} catch (UnknownHostException e) {
			logger.error("获得elasticsearch连接失败,原因：{}", e);
		}
		return client;
	}

	public void close(TransportClient client) {
		if (client != null) {
			client.close();
		}
	}

}
