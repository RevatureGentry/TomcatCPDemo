package com.revature.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.BasicConnectionPool;
import com.revature.ConnectionPool;
import com.revature.ConnectionUtil;
import com.revature.service.ConnectionPoolService;
import com.revature.service.ConnectionPoolServiceImpl;

public class ConnectionTest {

	private static final Properties props = getProperties();
	private static final ConnectionPool basicConnectionPool = BasicConnectionPool.getInstance();
	private static ConnectionPoolService tomcatConnectionPool;
	
	@BeforeClass
	public static void setUpTomcatConnectionPool() {
		/*
		 *	Here, we are not using the actual Connection Pool found in the Tomcat Container because we must have this test deployed into 
		 *	the Servlet Container to get the actual CP. Here, we use the same credentials and test if we can grab a Connection in the following
		 *	manner 
		 */
		final PoolConfiguration poolProps = new PoolProperties();
		poolProps.setUrl(props.getProperty("jdbc.url"));
		poolProps.setUsername(props.getProperty("jdbc.username"));
		poolProps.setPassword(props.getProperty("jdbc.password"));
		poolProps.setDriverClassName("oracle.jdbc.OracleDriver");
		final DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProps);
		tomcatConnectionPool = ConnectionPoolServiceImpl.getInstance(dataSource);
	}
	
	@Test
	public void basicConnectionPool_WhenProducingAConnection_ShouldNotBeNull() {
		assertTrue(basicConnectionPool.getConnection() instanceof Connection);
	}
	
	@Test
	public void connectionUtil_WhenProducingAConnection_ShouldNotBeNull() {
		assertTrue(ConnectionUtil.getConnection() instanceof Connection);
	}
	
	@Test
	public void tomcatConnectionPool_WhenProducingAConnection_ShouldNotBeNull() {
		assertTrue(tomcatConnectionPool.getConnection() instanceof Connection);
	}
	
	private static Properties getProperties() {
		Properties props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return props;
	}
}
