package org.chenzr.javaee.ejb;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	com.alibaba.druid.pool.DruidDataSource dataSource = new DruidDataSource();
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	dataSource.setDbType("mysql");
    	com.alibaba.druid.pool.DruidDataSourceFactory factory = new DruidDataSourceFactory();
    	dataSource.setEnable(true);
    	dataSource.setName("mysql");
    	dataSource.setUsername("root");
    	dataSource.setPassword("root");
    	dataSource.setUrl("jdbc:mysql://localhost:3306/test");
    	dataSource.setInitialSize(10);
    	dataSource.setMaxIdle(100);
    	dataSource.setMaxActive(100);
    	dataSource.setMinIdle(10);
    	try {
			dataSource.init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "Hello World!" );
        for (int i = 0; i < 10000; i++) {
            Test(dataSource);
		}
    }
    
    public static void Test( javax.sql.DataSource datasource) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT username FROM my_user");
			while (rs.next()) {
				String string = rs.getString(1);
				System.out.println(string);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
	}

}
