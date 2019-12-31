package artie.sensor.common.config;

import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
public class DataSourceConfig {
	
	/**
	 * Function to create a jdbcTemplate in base of the dynamic parameters
	 * @param paramDriver
	 * @param paramUrl
	 * @param paramUser
	 * @param paramPasswd
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public JdbcTemplate jdbcTemplate(String paramDriver, String paramUrl, String paramUser, String paramPasswd) throws IllegalAccessException, InvocationTargetException, InstantiationException {
	    
		//Extracting the needed parameters
	    final String driverClassName = paramDriver;
	    final String jdbcUrl = paramUrl;
	    final String username = paramUser;
	    final String password = paramPasswd;
	    
	    // Build dataSource manually:
	    final Class<?> driverClass = ClassUtils.resolveClassName(driverClassName, this.getClass().getClassLoader());
	    final Driver driver = (Driver) ClassUtils.getConstructorIfAvailable(driverClass).newInstance();
	    final DataSource dataSource = new SimpleDriverDataSource(driver, jdbcUrl, username, password);
	    
	    // and make the jdbcTemplate
	    return new JdbcTemplate(dataSource);
	}

}
