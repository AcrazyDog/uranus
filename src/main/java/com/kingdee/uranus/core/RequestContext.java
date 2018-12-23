package com.kingdee.uranus.core;

import java.util.Map;

import com.google.common.collect.Maps;

public class RequestContext {

	private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

	public static void set(String key, String value) {

		Map<String, String> map = threadLocal.get();

		if (map == null) {
			map = Maps.newHashMap();
		}
		map.put(key, value);
		threadLocal.set(map);
	}

	public static String get(String key) {
		return threadLocal.get().get(key);
	}

	public static void remove() {
		threadLocal.remove();
	}
}
