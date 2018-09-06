package application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import rds.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Component
public class Crawlers {

    private static final Logger log = LoggerFactory.getLogger(Crawlers.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final int rateInSecond = 60;
    
    @Scheduled(fixedRate = rateInSecond*1000)
    public void pushNotificationsForSend() {
    	Instant now = Instant.now();
    	
    	Long longTime = new Long(now.toEpochMilli()/1000);
    	int currentTime = longTime.intValue();
        System.out.println("Current time: "+ now.toString());
        
        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	String selectSQL = String.format(
    			"SELECT address " +
    			"FROM devices " +
    			"WHERE bundle_identifier='%s' AND is_release_mode=%s",
    			"leaf.prod.app", true);
    	
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
    		String address  = item.getString("address");
    		queryDataFromRelay(currentTime, address);
    	}
    }
    
    public static void queryDataFromRelay(int currentTime, String address) {
    	JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "loopring_getTransactions");
        jsonObject.put("id", UUID.randomUUID().toString());
        Map params = new HashMap();
        params.put("owner", address);
        params.put("symbol", "ETH");
        params.put("txType", "receive");
        jsonObject.put("params", new Map[]{params});
        
        String response = sendPostRequest("https://relay1.loopr.io/rpc/v2", jsonObject.toString());
        // System.out.println(response);
        
        JSONObject responseObject = new JSONObject(response);
        JSONArray results = responseObject.getJSONObject("result").getJSONArray("data");
        for(int n = 0; n < results.length(); n++) {
            JSONObject object = results.getJSONObject(n);
            // do some stuff....
            int createTime = object.getInt("createTime");
            if(currentTime - createTime < rateInSecond) {
            	System.out.println("About to send PushNotificationService");
            	System.out.println("CreateTime: " + createTime);
            	System.out.println("CurrentTime: " + currentTime);
            	System.out.println(currentTime - createTime);
            	PushNotificationService.process(address);
            	System.out.println(".....");
            }
        }
    }
    
    public static String sendPostRequest(String requestUrl, String payload) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                    jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
