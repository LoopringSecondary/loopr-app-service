package application;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;

import rds.DatabaseConnection;

public class PushNotificationService {
	
	public static void process(String address) {
		System.out.println("PushNotificationService.process: address " + address);
		JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	// Check whether it exists.
    	String selectSQL = String.format(
    			"SELECT * " +
    			"FROM devices " +
    			"WHERE address='%s' AND is_release_mode=%s",
    			address, true);
    	
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
    		send(bundleIdentifier, deviceToken, "Received ETH!");
    	}
	}

	public static void send(String bundleIdentifier, String deviceToken, String alertBody) {
		String filePath = String.format("./aps_certificates/%s.p12", bundleIdentifier);
		File f = new File(filePath);
    	System.out.println(f.exists());
    	
    	try {
			final ApnsClient apnsClient = new ApnsClientBuilder()
			        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
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
