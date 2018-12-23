package com.kingdee.uranus.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class RedisProducer {

	List<Customer> customers = new ArrayList<>(Arrays.asList(new Customer(1, "Jack", "Smith"),
			new Customer(2, "Adam", "Johnson"), new Customer(3, "Kim", "Smith"), new Customer(4, "David", "Williams"),
			new Customer(5, "Peter", "Davis")));

	private final AtomicInteger counter = new AtomicInteger(0);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ChannelTopic topic;

	public RedisProducer() {
	}

	public RedisProducer(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	public void publish() {
		Customer customer = customers.get(counter.getAndIncrement());
		System.out.println(
				"Publishing... customer with id=" + customer.getId() + ", " + Thread.currentThread().getName());

		System.out.println(customer.toString());
		redisTemplate.convertAndSend(topic.getTopic(), JSONObject.toJSONString(customer));
	}
}
