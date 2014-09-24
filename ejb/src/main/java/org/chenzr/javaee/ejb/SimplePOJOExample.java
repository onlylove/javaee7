package org.chenzr.javaee.ejb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class SimplePOJOExample {

	public static void main(String[] args) throws Exception {
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://localhost:3306/test");
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername("root");
		p.setPassword("root");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);
		for (int i = 0; i < 1000000000; i++) {
			Test(datasource);
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
//				System.out.println(rs.getString(1));
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