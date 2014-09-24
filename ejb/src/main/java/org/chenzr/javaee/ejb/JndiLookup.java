package org.chenzr.javaee.ejb;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.naming.java.javaURLContextFactory;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class JndiLookup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Context ctx = null;
		DataSource ds = null;
		try {
			org.apache.naming.java.javaURLContextFactory factory = new javaURLContextFactory();
			Properties props=new Properties(); 
			//tomcat-catalina
//			props.put(Context.INITIAL_CONTEXT_FACTORY,"org.apache.naming.java.javaURLContextFactory"); 
//			props.put(Context.URL_PKG_PREFIXES, "org.apache.naming");
			//jetty-jndi
//			props.put(Context.INITIAL_CONTEXT_FACTORY,"org.eclipse.jetty.jndi.InitialContextFactory");
//			props.put(Context.URL_PKG_PREFIXES, "org.eclipse.jetty.jndi");
			// wildfly-naming
			props.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			props.put(Context.PROVIDER_URL,"localhost:4447");
			props.put("jboss.naming.client.ejb.context", true);

			ctx = new InitialContext(props);
//			ctx = new InitialContext();
			DataSource dataSource = getDataSource();
			
			ctx.bind("java:jdbc:mydatasource", dataSource );
			ds = (DataSource)ctx.lookup("java:jdbc:mydatasource");
			try {
				for (int i = 0; i < 1000; i++) {
					Connection conn = ds.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select username from my_user");
					while (rs.next()) {
						System.out.println(rs.getString(1));
					}
					rs.close();
					rs = null;
					stmt.close();
					stmt = null;
					conn.close();
					conn = null;
				}
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}finally{
			ds = null;
			try {
				ctx.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static DataSource getDataSource() {
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
		org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
		datasource.setPoolProperties(p);
		return datasource;
	}

}
