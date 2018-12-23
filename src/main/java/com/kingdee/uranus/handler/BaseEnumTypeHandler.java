package com.kingdee.uranus.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.alibaba.druid.util.StringUtils;
import com.kingdee.uranus.model.LogType;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年5月9日 下午2:53:17
 * @version
 */
@MappedTypes(value = { LogType.class })
@MappedJdbcTypes(JdbcType.CHAR)
public class BaseEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

	private Class<E> type;

	public BaseEnumTypeHandler() {
	}

	public BaseEnumTypeHandler(Class<E> type) {
		this.type = type;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		if (jdbcType == null) {
			ps.setString(i, parameter.toString());
		} else {
			ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE);
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return get(rs.getString(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return get(rs.getString(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return get(cs.getString(columnIndex));
	}

	private <E extends Enum<E>> E get(String v) {
		if (v == null) {
			return null;
		}
		if (StringUtils.isNumber(v)) {
			return (E) get(type, Integer.parseInt(v));
		} else {
			return (E) Enum.valueOf(type, v);
		}
	}

	private <E extends Enum<E>> E get(Class<E> type, int v) {
		Method method = null;
		E result = null;

		try {
			method = type.getMethod("get", int.class);
			result = (E) method.invoke(type, v);
		} catch (NoSuchMethodException e) {
			result = Enum.valueOf(type, String.valueOf(v));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
}