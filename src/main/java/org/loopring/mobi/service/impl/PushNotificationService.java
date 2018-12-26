package org.loopring.mobi.service.impl;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import javax.net.ssl.SSLException;

import org.loopring.mobi.persistence.model.Device;
import org.loopring.mobi.persistence.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;

@Service
public class PushNotificationService {

    @Autowired
    private MessageService messageService;
    //    public void process(String address, String alertBody) {
    //        System.out.println("PushNotificationService.process: address " + address);
    //        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    //        // Check whether it exists.
    //        String selectSQL = String.format("SELECT * " + "FROM devices " + "WHERE address='%s'", address);
    //        List<JSONObject> items = jdbcTemplate.query(selectSQL, new RowMapper<JSONObject>() {
    //            @Override
    //            public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
    //                JSONObject item = new JSONObject();
    //                ResultSetMetaData rsmd = rs.getMetaData();
    //                int numColumns = rsmd.getColumnCount();
    //                for (int i = 1; i <= numColumns; i++) {
    //                    String column_name = rsmd.getColumnName(i);
    //                    item.put(column_name, rs.getObject(column_name));
    //                }
    //                return item;
    //            }
    //        });
    //        for (JSONObject item : items) {
    //            String bundleIdentifier = item.getString("bundle_identifier");
    //            String deviceToken = item.getString("device_token");
    //            Boolean isReleaseMode = item.getBoolean("is_release_mode");
    //            PushNotificationService service = new PushNotificationService();
    //            // Disable due to the device token is not found in the released app.
    //            // service.send(bundleIdentifier, deviceToken, isReleaseMode, alertBody);
    //        }
    //    }

    public void send(Device device, String alertBody) {    	
        if (device == null)
            return;
        String filePath;
        String APNS_HOST;
        if (device.getIsReleaseMode()) {
            filePath = String.format("AuthKey_U7D7Z7GLF4.p8");
            APNS_HOST = ApnsClientBuilder.PRODUCTION_APNS_HOST;
        } else {
            filePath = String.format("AuthKey_U7D7Z7GLF4.p8");
            APNS_HOST = ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
        }
        try {
            System.out.println(filePath);
            File f = new File(filePath);
            System.out.println(f.exists());

            final ApnsClient apnsClient = new ApnsClientBuilder()
            		.setApnsServer(APNS_HOST)
            		.setSigningKey(ApnsSigningKey.loadFromPkcs8File(f, "5WDWHF88Y8", "U7D7Z7GLF4"))
                    .build();
            final SimpleApnsPushNotification pushNotification;
            {
                final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
                payloadBuilder.setAlertBody(alertBody);
                payloadBuilder.setSound("default");
                final String payload = payloadBuilder.buildWithDefaultMaximumLength();
                final String token = TokenUtil.sanitizeTokenString(device.getDeviceToken());
                pushNotification = new SimpleApnsPushNotification(token, device.getBundleIdentifier(), payload);
            }
            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient
                    .sendNotification(pushNotification);
            try {
                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture
                        .get();
                if (pushNotificationResponse.isAccepted()) {
                    System.out.println("Push notification accepted by APNs gateway.");
                    messageService.save(Message.builder()
                            .bundleIdentifier(device.getBundleIdentifier())
                            .deviceToken(device.getDeviceToken())
                            .alertBody(alertBody)
                            .build());
                    //                        recordPushNotification(device.getBundleIdentifier(), device.getDeviceToken(), alertBody);
                } else {
                    System.out.println("Notification rejected by the APNs gateway: " + pushNotificationResponse.getRejectionReason());
                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        System.out.println("\t…and the token is invalid as of " + pushNotificationResponse.getTokenInvalidationTimestamp());
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
        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        } catch (InvalidKeyException e) {
        	e.printStackTrace();
        }
    }
    //    private static void recordPushNotification(String bundleIdentifier, String deviceToken, String alertBody) {
    //        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    //        // insert new data
    //        String insertSQL = String.format("INSERT INTO messages (bundle_identifier, device_token, alert_body) " + "VALUES (?, ?, ?)");
    //        // define query arguments
    //        Object[] params = new Object[]{bundleIdentifier, deviceToken, alertBody};
    //        // define SQL types of the arguments
    //        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
    //        int row = jdbcTemplate.update(insertSQL, params, types);
    //        System.out.println(row + " row inserted.");
    //        JSONObject response = new JSONObject();
    //        response.put("success", true);
    //    }
}
