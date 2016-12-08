package org.qf.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBUtil {
   private static DataSource ds;    // ��Ϊ DriverManager���ߵ�����DataSource�����ǻ�ȡ���ӵ���ѡ����
   private static final ThreadLocal<Connection> connLocal=new ThreadLocal<Connection>();
   static{
	   Properties props=new Properties();
	   try{
		   props.load(DBUtil.class.getClassLoader().getResourceAsStream("dbcp.properties"));
		   ds=BasicDataSourceFactory.createDataSource(props);  // ͨ��dbcp���ӳػ�ȡDataSource����
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   public static Connection getConnection()throws SQLException{
	   Connection conn=connLocal.get(); // �Ȳ鿴��ǰ�̵߳ľֲ�����
	   if(conn==null){  // ��������ڣ�������ӳ��л�ȡConnection
		   conn=ds.getConnection();   //  ���Խ������ DataSource��������ʾ������Դ������
		   connLocal.set(conn);   // ����ȡ�������Ӷ������õ��ֲ߳̾�������
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
