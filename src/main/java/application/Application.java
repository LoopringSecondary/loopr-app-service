package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import rds.DatabaseConnection;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
    	
    	// Init DataSource in DatabaseConnection
    	DatabaseConnection.init();

    	// Init environment variables
    	// ApiKey.init();
    	
    	System.getProperties().put( "server.port", 5000 );
        SpringApplication.run(Application.class, args);
    }
}
