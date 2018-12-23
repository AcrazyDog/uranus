package com.kingdee.uranus.handler;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年5月9日 下午2:11:54
 * @version
 */
public interface BaseEnum<E extends Enum<?>, T> {
	public T getValue();

	public String getDisplayName();
}