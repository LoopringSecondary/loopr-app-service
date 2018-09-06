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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rds.DatabaseConnection;

@RestController
public class DeviceController {

	@RequestMapping(value = "/api/v1/device", method = RequestMethod.POST, produces = "application/json")
    public String greeting(@RequestBody Map<String, String> postPayload) {
		
		System.out.println(postPayload);
		
		String address = postPayload.get("address");
		String bundleIdentifier = postPayload.get("bundleIdentifier");
		String deviceToken = postPayload.get("deviceToken");

    	JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	String insertSQL = String.format(
    			"INSERT INTO devices (address, bundleIdentifier, deviceToken) " +
    			"VALUES (?, ?, ?)");
    	
    	// define query arguments
    	Object[] params = new Object[] {address, bundleIdentifier, deviceToken};
    	
    	// define SQL types of the arguments
    	int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
    	
    	int row = jdbcTemplate.update(insertSQL, params, types);
    	System.out.println(row + " row inserted.");
    	
        JSONObject response = new JSONObject();
		response.put("success", true);

        return response.toString();
    }
}
