package uranus;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年6月15日 下午4:22:46
 * @version
 */
public class EsTest {

	private String HOST = "172.17.52.49";
	private int PORT = 9200;

	@Test
	public void test() throws IOException {

		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

		IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
				.setSource(XContentFactory.jsonBuilder().startObject().field("user", "kimchy")
						.field("postDate", new Date()).field("message", "trying out Elasticsearch").endObject())
				.get();

		GetResponse getResponse = client.prepareGet("twitter", "tweet", "1").get();

	}

	// private void showResult(IndexResponse responsse) {
	//
	// SearchHits searchHits = response.();
	// float maxScore = searchHits.getMaxScore(); // 查询结果中的最大文档得分
	// System.out.println("maxScore: " + maxScore);
	// long totalHits = searchHits.getTotalHits(); // 查询结果记录条数
	// System.out.println("totalHits: " + totalHits);
	// SearchHit[] hits = searchHits.getHits(); // 查询结果
	// System.out.println("当前返回结果记录条数：" + hits.length);
	// for (SearchHit hit : hits) {
	// long version = hit.version();
	// String id = hit.getId();
	// String index = hit.getIndex();
	// String type = hit.getType();
	// float score = hit.getScore();
	// System.out.println("===================================================");
	// String source = hit.getSourceAsString();
	// System.out.println("version: " + version);
	// System.out.println("id: " + id);
	// System.out.println("index: " + index);
	// System.out.println("type: " + type);
	// System.out.println("score: " + score);
	// System.out.println("source: " + source);
	// }
	// }
}
