package application;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rds.DatabaseConnection;

@RestController
public class DeviceController {

	@RequestMapping(value = "/api/v1/devices", method = RequestMethod.POST, produces = "application/json")
    public String greeting(@RequestBody Map<String, Object> postPayload) {
		
		System.out.println(postPayload);
		
		String address = postPayload.get("address").toString();
		String bundleIdentifier = postPayload.get("bundleIdentifier").toString();
		String deviceToken = postPayload.get("deviceToken").toString();
		boolean isReleaseMode = (Boolean) postPayload.get("isReleaseMode");
		
		// not in the request...
		// String currentInstalledVersion = postPayload.get("currentInstalledVersion").toString();

    	JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	// Check whether it exists.
    	String selectSQL = String.format(
    			"SELECT * " +
    			"FROM devices " +
    			"WHERE address='%s' AND bundle_identifier='%s' AND device_token='%s' AND is_release_mode=%s",
    			address, bundleIdentifier, deviceToken, isReleaseMode);
    	
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
    	
    	if(items.size() > 0) {
    		// update new data
        	String updateSQL = String.format(
        			"UPDATE devices " +
        			"SET is_enabled=True " +
        			"WHERE address='%s' AND bundle_identifier='%s' AND device_token='%s' AND is_release_mode=%s",
        			address, bundleIdentifier, deviceToken, isReleaseMode);
        	int row = jdbcTemplate.update(updateSQL);
        	System.out.println(row + " row updated.");
        	
    		System.out.println("data exists");
    		JSONObject response = new JSONObject();
    		response.put("success", true);
    		response.put("message", "exists");
    		return response.toString();
    	}
    	
    	// insert new data
    	String insertSQL = String.format(
    			"INSERT INTO devices (address, bundle_identifier, device_token, is_release_mode, current_installed_version) " +
    			"VALUES (?, ?, ?, ?, ?)");
    	
    	// define query arguments
    	Object[] params = new Object[] {address, bundleIdentifier, deviceToken, isReleaseMode, "0.9.9"};
    	
    	// define SQL types of the arguments
    	int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR };
    	
    	int row = jdbcTemplate.update(insertSQL, params, types);
    	System.out.println(row + " row inserted.");
    	    	
        JSONObject response = new JSONObject();
		response.put("success", true);

        return response.toString();
    }
	
	// deviceToken is unique in devices table
	@RequestMapping(value = "/api/v1/devices/{deviceToken}/{address}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("deviceToken") String deviceToken,
			@PathVariable("address") String address) {
    	JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	// TODO: not need to check if it exists.
    	// Check whether it exists.
    	String selectSQL = String.format(
    			"SELECT * " +
    			"FROM devices " +
    			"WHERE device_token='%s' AND address='%s'",
    			deviceToken, address);
    	
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
    	
    	if(items.size() > 0) {
    		System.out.println("data exists");
    		
    		// update new data
        	String updateSQL = String.format(
        			"UPDATE devices SET is_enabled=False " +
        			"WHERE device_token='%s' AND address='%s'",
        	    	deviceToken, address);
        	int row = jdbcTemplate.update(updateSQL);
        	System.out.println(row + " row updated.");
    		
    		JSONObject response = new JSONObject();
    		response.put("success", true);
    		return response.toString();
    	}
	    
        JSONObject response = new JSONObject();
		response.put("success", false);
		response.put("message", "data not found");

		return response.toString();
	}
}
