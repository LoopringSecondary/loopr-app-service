package rds;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;


import org.flywaydb.core.Flyway;

public class DatabaseConnection {
	
	private static String RDS_HOSTNAME = "";
	private static String RDS_PORT = "";
	private static String RDS_DB_NAME = "";
	private static String RDS_USERNAME = "";
	private static String RDS_PASSWORD = "";
	
	// TODO: simple DS for test (not for production!)
	private static SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
	
	public static void init() {
        Map<String, String> env = System.getenv();
        
        // Get RDS info from environment variables
        RDS_HOSTNAME = env.get("RDS_HOSTNAME");
        RDS_PORT = env.get("RDS_PORT");
        RDS_DB_NAME = env.get("RDS_DB_NAME");
        RDS_USERNAME = env.get("RDS_USERNAME");
        RDS_PASSWORD = env.get("RDS_PASSWORD");

        if (RDS_HOSTNAME.length() == 0 ||
        		RDS_PORT.length() == 0 ||
        		RDS_DB_NAME.length() == 0 ||
        		RDS_USERNAME.length() == 0 ||
        		RDS_PASSWORD.length() == 0) {
        	System.out.println("Failed to get RDS info from environment "
        			+ "variables. Some are empty. "
        			+ "It may cause the failure of the database connection.");
        }
        
        System.out.println("========== RDS INFO ==========");
        System.out.println("RDS_HOSTNAME   : " + RDS_HOSTNAME);
        System.out.println("RDS_PORT       : " + RDS_PORT);
        System.out.println("RDS_DB_NAME    : " + RDS_DB_NAME);
        System.out.println("RDS_USERNAME   : " + RDS_USERNAME);
        System.out.println("RDS_PASSWORD   : " + RDS_PASSWORD);
        System.out.println(" ");
        
        // Update dataSource
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl("jdbc:postgresql://" +
            RDS_HOSTNAME + ":" +
            RDS_PORT + "/" +
            RDS_DB_NAME);
        dataSource.setUsername(RDS_USERNAME);
        dataSource.setPassword(RDS_PASSWORD);
        
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
        flyway.setLocations("classpath:/db/migration/");
	}
	
	public static JdbcTemplate getJdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}
	
}