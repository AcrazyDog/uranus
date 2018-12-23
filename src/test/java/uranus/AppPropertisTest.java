package uranus;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import eu.bitwalker.useragentutils.Application;

@EnableAutoConfiguration
public class AppPropertisTest {

	@Value("server.port")
	private String port;

	public void test() {
		System.out.println(port);
	}
}
