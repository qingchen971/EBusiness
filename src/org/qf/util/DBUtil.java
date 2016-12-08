package org.qf.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBUtil {
   private static DataSource ds;    // 作为 DriverManager工具的替代项，DataSource对象是获取连接的首选方法
   private static final ThreadLocal<Connection> connLocal=new ThreadLocal<Connection>();
   static{
	   Properties props=new Properties();
	   try{
		   props.load(DBUtil.class.getClassLoader().getResourceAsStream("dbcp.properties"));
		   ds=BasicDataSourceFactory.createDataSource(props);  // 通过dbcp连接池获取DataSource对象
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   public static Connection getConnection()throws SQLException{
	   Connection conn=connLocal.get(); // 先查看当前线程的局部变量
	   if(conn==null){  // 如果不存在，则从连接池中获取Connection
		   conn=ds.getConnection();   //  尝试建立与此 DataSource对象所表示的数据源的连接
		   connLocal.set(conn);   // 将获取到的连接对象设置到线程局部变量中
	   }
	   return conn;
   }
   
   public static void close() throws SQLException{
	   Connection conn=connLocal.get();
	   connLocal.set(null);
	   if(conn!=null && !conn.isClosed()){
		   conn.close();
	   }
   }
}
