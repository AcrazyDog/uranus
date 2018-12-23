package com.kingdee.uranus.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisConsumer implements MessageListener {

	@Override
	public void onMessage(Message arg0, byte[] arg1) {
		// TODO
	}

}
