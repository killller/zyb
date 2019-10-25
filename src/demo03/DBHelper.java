package demo03;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.fabric.xmlrpc.base.Array;

public class DBHelper{
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
//	private static String driverName = "oracle.jdbc.driver.OracleDriver";
//	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
//	private String user = "scott";
//	private String password = "zyb";
	
	//加载驱动
	static {
		try {
			Class.forName(MyPropertise.getInstance().getProperty("driverName"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//获取数据库的连接
	public Connection getConn() throws SQLException{
		conn = DriverManager.getConnection(MyPropertise.getInstance().getProperty("url"), 
				MyPropertise.getInstance());
		return conn;
	}
	
	//关闭资源       （先开启的后关闭，后开启的）
	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs){
		//关闭资源
		if (rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回多条记录查询操作  select * from table_name
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectMutil(String sql, List<Object> params) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsList (pstmt, params);
			//获取结果集
			rs = pstmt.executeQuery();
			//根据结果集对象获取到所有结果集中所有列名
			List<String> columnNames = getAllColumnNames (rs);
			while (rs.next()){
				map = new HashMap<String, Object>();
				String typeName  =null;  //值的类型
				Object obj = null; //获取的值
				//循环所有的列名
				for (String name : columnNames){
					obj = rs.getObject(name);
					if (obj != null)
						typeName = obj.getClass().getName();
					if ("oracle.sql.BLOB".equals(typeName)){
//						//对图片进行处理
//						BLOB blob = (BLOB)obj;
//						InputStream in = blob.getBinaryStream();
//						byte [] bt = new byte[(int)blob.length()];
//						in.read(bt);
//						map.put(name, bt); //将blob类型值以字节数组的形式存储
					}else 
						map.put(name, obj);
				}
				list.add(map);
			}
		} finally{
			closeAll(conn, pstmt, rs);
		}		
		return list;
	}
	
	/**
	 * 单记录的查询  select * from table_name where id = ?
	 * @param SQL  查询的SQL语句
	 * @param params 传入的参数  集合中  集合的参数的顺序必须和？书序一致
	 * @param rs2
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public Map<String, Object> selectSingle(String sql, List<Object> params) throws Exception{
		Map<String, Object> map = null;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsList(pstmt, params);
			//获取结果集
			rs = pstmt.executeQuery();
			//根据结果集对象获取到所有结果集中所有列名
			List<String> columnNames = getAllColumnNames(rs);
			if (rs.next()){
				map = new HashMap<String, Object>();
				String typeName = null;  //值的类型
				Object obj = null; //获取的值
				//循环所有列名
				for (String name : columnNames){
					obj = rs.getObject(name);
					if (obj != null)
						typeName = obj.getClass().getName();
					if ("oracle.sql.BLOB".equals(typeName)){
//						//对图片进行处理
//						BLOB blob = (BLOB)obj;
//						InputStream in = blob.getBinaryStream();
//						byte [] bt = new byte[(int)blob.length()];
//						in.read(bt);
//						map.put(name, bt); //将blob类型值以字节数组的形式存储
					}else 
						map.put(name, obj);
				}
			}
		} finally{
			closeAll(conn, pstmt, rs);
		}
		return map;
	}
	
	/**
	 * 获取查询后的字段名
	 * @param rs2
	 * @return
	 * @throws SQLException 
	 */
	public List<String> getAllColumnNames(ResultSet rs) throws SQLException {
		List<String> list = new ArrayList<String>();
		ResultSetMetaData data = rs.getMetaData();
		int count = data.getColumnCount();
		for (int i = 1; i <= count; i++){
			String str = data.getColumnName(i); //获取指定列的名称
			//添加列名到 List 集合中
			list.add(str);
		}
		
		return list;
	}

	//将集合设置到预编译对象中
	public void setParamsList(PreparedStatement pstmt, List<Object> params) throws SQLException {
		if (params == null || params.size() <= 0)
			return ;
		for (int i = 0; i < params.size(); i++)
			pstmt.setObject(i + 1, params.get(i));
	}
	/**
	 * 批处理操作  多个insert update delete      同一个事务
	 * @param sqls		多条SQL语句
	 * @param params		多条SQL语句的参数	每条SQL语句参数小LIst集合中	多个再封装到打的list集合  一一对应
	 * @return
	 * @throws SQLException
	 */
	public int update (List<String> sqls, List<List<Object>> params) throws SQLException{
		int result = 0;
		try {
			conn = getConn();
			//设置事务手动提交
			conn.setAutoCommit(false);
			//循环SQL语句
			if (sqls == null || sqls.size() <= 0)
				return result;
			for (int i = 0; i < sqls.size(); i++){
				//获取SQL语句并创建预编译对象
				pstmt = conn.prepareStatement(sqls.get(i));
				//获取对应的SQL语句参数集合
				List<Object> param = params.get(i);
				//设置参数
				setParamsList(pstmt, param);
				//执行更新
				result = pstmt.executeUpdate();
				if (result <= 0)
					return result;
			}
			//手动提交
			conn.commit();
		} catch (SQLException e) {
			//设置回滚
			conn.rollback();
			result = 0;
		}finally {
			//还原事务状态
			conn.setAutoCommit(true);
			closeAll(conn, pstmt, rs);
		}
		
		return result;
	}
	
	/**
	 * 更新操作 增删改
	 * @param sql    更新语句
	 * @param params   传入的参数  不定长的对象数组	传入的参数顺序与？顺序一致
	 * @throws SQLException 
	 * 
	 */
	public int update (String sql, Object...params) throws SQLException{
		int result = 0;
		try {
			conn = getConn();//获取连接对象
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsObject(pstmt, params);
			//执行
			result = pstmt.executeUpdate();
		} finally{
			closeAll(conn, pstmt, null);
		}
				
		return result;
	}

	//不定长参数	设置参数	传入的参数顺序必须和问号的顺序一致
	public void setParamsObject(PreparedStatement pstmt, Object...params) throws SQLException {
		if (params == null || params.length <= 0)
			return ;
		for (int i = 0; i < params.length; i++)
			pstmt.setObject(i + 1, params[i]);	//将数组中的第i个元素的值设置为第i + 1 个问号
	}
	
	/**
	 * 聚合函数操作	select count(*) from table_name
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public double getPloymer(String sql, List<Object> params) throws SQLException{
		double result = 0;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			setParamsList(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getDouble(1);
		} finally{
			closeAll(conn, pstmt, rs);
		}
		return result;
	}
	
	/**
	 * 返回一行记录	select * from table_name where id = ?
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> T findSingle(String sql, List<Object> params, Class<T> cls) throws Exception{
		T t = null;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsList(pstmt, params);
			rs = pstmt.executeQuery();
			//通过反射获取实体类中所有的方法
			Method [] methods = cls.getDeclaredMethods();
			List<String> columnNames = getAllColumnNames(rs);
			Object obj = null;
			if (rs.next()){
				//创建对象	通过反射
				t = cls.newInstance();//默认diaoy9ong对象的无参数的构造函数
				//循环列
				for (String name : columnNames){
					obj = rs.getObject(name);//获取值
					//循环方法 set + name 	setUname
					for(Method m : methods){
						if (("set" + name).equalsIgnoreCase(m.getName())){
							//set方法的形参类型进行判断	set方法的形参的数据类型
							String typeName = m.getParameterTypes()[0].getName();
							if ("java.lang.Integer".equals(typeName))
								m.invoke(t, rs.getInt(name));//激活此方法	传入的参数必须和底层方法的数据类型一致
							else if ("java.lang.Double".equals(typeName))
								m.invoke(t, rs.getDouble(name));
							else if ("java.lang.Float".equals(typeName))
								m.invoke(t, rs.getFloat(name));
							else if ("java.lang.Long".equals(typeName))
								m.invoke(t, rs.getLong(name));
							else 
								m.invoke(t, rs.getString(name));
						}
					}
				}
			}
			
		}finally{
			closeAll(conn, pstmt, rs);
		}
		
		return t;
	}
	
	/**
	 * 查询语句	返回多条记录
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findMutil(String sql, List<Object> params, Class<T> cls) throws Exception{
		List<T> list = new ArrayList<T>();
		T t = null;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsList(pstmt, params);
			rs = pstmt.executeQuery();
			//通过反射获取实体类中所有的方法
			Method [] methods = cls.getDeclaredMethods();
			List<String> columnNames = getAllColumnNames(rs);
			Object obj = null;
			while(rs.next()){
				//创建对象	通过反射
				t = cls.newInstance();//默认diaoy9ong对象的无参数的构造函数
				//循环列
				for (String name : columnNames){
					obj = rs.getObject(name);//获取值
					//循环方法 set + name 	setUname
					for(Method m : methods){
						if (("set" + name).equalsIgnoreCase(m.getName())){
							//set方法的形参类型进行判断	set方法的形参的数据类型
							String typeName = m.getParameterTypes()[0].getName();
							if ("java.lang.Integer".equals(typeName))
								m.invoke(t, rs.getInt(name));//激活此方法	传入的参数必须和底层方法的数据类型一致
							else if ("java.lang.Double".equals(typeName))
								m.invoke(t, rs.getDouble(name));
							else if ("java.lang.Float".equals(typeName))
								m.invoke(t, rs.getFloat(name));
							else if ("java.lang.Long".equals(typeName))
								m.invoke(t, rs.getLong(name));
							else 
								m.invoke(t, rs.getString(name));
						}
					}
				}
			list.add(t);
			}
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return list;
	}
	
}