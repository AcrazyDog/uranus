package com.kingdee.uranus.model;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年5月9日 上午11:11:49
 * @version
 */
public enum LogType {

	CLOSE(0, "关闭"), FORWARD(1, "转发"), PASS(2, "验证通过");

	private Integer value;

	private String desc;

	private LogType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static void main(String[] args) {
		System.out.println(LogType.valueOf(LogType.class, "1"));
	}
}
