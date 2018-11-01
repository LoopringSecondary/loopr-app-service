package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;

import rds.DatabaseConnection;

public class PushNotificationService {

	public static void process(String address, String alertBody) {
		System.out.println("PushNotificationService.process: address " + address);
		JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	// Check whether it exists.
    	String selectSQL = String.format(
    			"SELECT * " +
    			"FROM devices " +
    			"WHERE address='%s'",
    			address);
    	
    	List<JSONObject> items = jdbcTemplate.query(
    			selectSQL,
    			new RowMapper<JSONObject>() {
                    @Override
                    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
                    	JSONObject item = new JSONObject();

						ResultSetMetaData rsmd = rs.getMetaData();
						int numColumns = rsmd.getColumnCount();
						for (int i=1; i<=numColumns; i++) {
							String column_name = rsmd.getColumnName(i);
							item.put(column_name, rs.getObject(column_name));
						}
						
						return item;
                    }
    			}
    	);
    	
    	for(JSONObject item: items) {
    		String bundleIdentifier  = item.getString("bundle_identifier");
    		String deviceToken  = item.getString("device_token");
    		Boolean isReleaseMode = item.getBoolean("is_release_mode");
    		PushNotificationService service = new PushNotificationService();
    		
    		// Disable due to the device token is not found in the released app.
    		// service.send(bundleIdentifier, deviceToken, isReleaseMode, alertBody);
    	}
	}

	public void send(String bundleIdentifier, String deviceToken, Boolean isReleaseMode, String alertBody) {
		String filePath;
		String APNS_HOST;
		if(isReleaseMode) {
			filePath = String.format("%s.p12", bundleIdentifier);
			APNS_HOST = ApnsClientBuilder.PRODUCTION_APNS_HOST;
		} else {
			filePath = String.format("%s.development.p12", bundleIdentifier);
			APNS_HOST = ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
		}
				
		System.out.println(filePath);
		File f = new File(filePath);
    	System.out.println(f.exists());
    	
    	try {
			final ApnsClient apnsClient = new ApnsClientBuilder()
			        .setApnsServer(APNS_HOST)
			        .setClientCredentials(f, "123456")
			        .build();
			
			final SimpleApnsPushNotification pushNotification;
			{
				final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
				payloadBuilder.setAlertBody(alertBody);
				payloadBuilder.setSound("default");

				final String payload = payloadBuilder.buildWithDefaultMaximumLength();
				final String token = TokenUtil.sanitizeTokenString(deviceToken);

				pushNotification = new SimpleApnsPushNotification(token, bundleIdentifier, payload);
		    }
			final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
		    sendNotificationFuture = apnsClient.sendNotification(pushNotification);
			
			try {
			    final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
			            sendNotificationFuture.get();

			    if (pushNotificationResponse.isAccepted()) {
			        System.out.println("Push notification accepted by APNs gateway.");
			        recordPushNotification(bundleIdentifier, deviceToken, alertBody);
			        
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
		
	}
	
	private static void recordPushNotification(String bundleIdentifier, String deviceToken, String alertBody) {
		JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
		
		// insert new data
    	String insertSQL = String.format(
    			"INSERT INTO messages (bundle_identifier, device_token, alert_body) " +
    			"VALUES (?, ?, ?)");
    	
    	// define query arguments
    	Object[] params = new Object[] {bundleIdentifier, deviceToken, alertBody};
    	
    	// define SQL types of the arguments
    	int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
    	
    	int row = jdbcTemplate.update(insertSQL, params, types);
    	System.out.println(row + " row inserted.");
    	    	
        JSONObject response = new JSONObject();
		response.put("success", true);
	}

}
