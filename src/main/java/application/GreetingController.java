package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.*;

import javax.net.ssl.SSLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rds.DatabaseConnection;
import models.Message;

import com.turo.pushy.apns.*;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;

import java.io.File;
import java.io.IOException;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	
    	JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	String searchMessageSql = String.format(
    			"SELECT * " +
    			"FROM test");
    	
    	System.out.println(searchMessageSql);

    	List<Message> messages = jdbcTemplate.query(
    			searchMessageSql,
    			new RowMapper<Message>() {
                    @Override
                    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
                    	Message message = new Message();
                    	return message;
                    }
    			}
    	);
    	
    	File f = new File("./aps_certificates/leaf.prod.app.p12");
    	System.out.println(f.exists());
    	
    	try {
			final ApnsClient apnsClient = new ApnsClientBuilder()
			        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
			        .setClientCredentials(new File("./aps_certificates/leaf.prod.app.p12"), "123456")
			        .build();
			
			final SimpleApnsPushNotification pushNotification;
			{
				final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
				payloadBuilder.setAlertBody("Example!");
				payloadBuilder.setSound("default");

				final String payload = payloadBuilder.buildWithDefaultMaximumLength();
				final String token = TokenUtil.sanitizeTokenString("...");

				pushNotification = new SimpleApnsPushNotification(token, "leaf.prod.app", payload);
		    }
			final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
		    sendNotificationFuture = apnsClient.sendNotification(pushNotification);
			
			try {
			    final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
			            sendNotificationFuture.get();

			    if (pushNotificationResponse.isAccepted()) {
			        System.out.println("Push notification accepted by APNs gateway.");
			    } else {
			        System.out.println("Notification rejected by the APNs gateway: " +
			                pushNotificationResponse.getRejectionReason());

			        if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
			            System.out.println("\t…and the token is invalid as of " +
			                pushNotificationResponse.getTokenInvalidationTimestamp());
			        }
			    }
			} catch (final ExecutionException | InterruptedException e) {
			    System.err.println("Failed to send push notification.");
			    e.printStackTrace();
			}
			
		} catch (SSLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
