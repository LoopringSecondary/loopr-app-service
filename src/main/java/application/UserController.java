package application;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rds.DatabaseConnection;

@RestController
public class UserController {

	@RequestMapping(value = "/api/v1/users", method = RequestMethod.POST, produces = "application/json")
    public String post(@RequestBody Map<String, Object> postPayload) {
		
		System.out.println("POST /api/v1/users "+ postPayload);
		
		String accountToken;
		if (postPayload.containsKey("account_token")) {
			accountToken = postPayload.get("account_token").toString();
		} else {
			return "";
		}
		
		String language = null;
		if (postPayload.containsKey("language")) {
			language = postPayload.get("language").toString();
		}
		
		String currency = null;
		if (postPayload.containsKey("currency")) {
			currency = postPayload.get("currency").toString();
		}

		Double lrcRatioFee = null;
		if (postPayload.containsKey("lrc_ratio_fee")) {
			lrcRatioFee = (Double) postPayload.get("lrc_ratio_fee");
		}

		System.out.println("Verified params");
		
    	JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	// Check whether it exists.
    	String selectSQL = String.format(
    			"SELECT * " +
    			"FROM users " +
    			"WHERE account_token='%s'",
    			accountToken);
    	
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
    	
    	// Update if exists
    	if(items.size() == 1) {
    		System.out.println("Update users");
    		// insert new data
    		
        	String insertSQL = String.format(
        			"UPDATE users " +
        			"SET language=?, currency=?, lrc_ratio_fee=?, updated_at=NOW() " +
        			"WHERE account_token=?");

        	// define query arguments
        	Object[] params = new Object[] {language, currency, lrcRatioFee, accountToken};
        	int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.VARCHAR};

        	int row = jdbcTemplate.update(insertSQL, params, types);
        	System.out.println(row + " row updated.");

            JSONObject response = new JSONObject();
    		response.put("success", true);
            return response.toString();

    	} else {
        	// insert new data
        	String insertSQL = String.format(
        			"INSERT INTO users (account_token) " +
        			"VALUES (?, ?, ?, ?)");
        	
        	// define query arguments
        	Object[] params = new Object[] {accountToken, language, currency, lrcRatioFee};
        	
        	// define SQL types of the arguments
        	int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DOUBLE};
        	
        	int row = jdbcTemplate.update(insertSQL, params, types);
        	System.out.println(row + " row inserted.");
        	    	
            JSONObject response = new JSONObject();
    		response.put("success", true);

            return response.toString();    		
    	}
    }

	
	@RequestMapping(value = "/api/v1/users", method = RequestMethod.GET, produces = "application/json")
    public String get(@RequestParam("account_token") String accountToken) {
    	
    	JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    	
    	System.out.println("account_token: " + accountToken);
    	
    	String sql = String.format(
    			"SELECT * " +
    			"FROM users " +
    			"WHERE account_token='%s'",
    			accountToken);
    	
    	System.out.println(sql);
    	
    	List<JSONObject> items = jdbcTemplate.query(
    			sql,
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
    	
    	if(items.size() != 1) {
    		System.out.println("no data");
    		JSONObject response = new JSONObject();
    		response.put("success", false);
    		response.put("message", "no account token");
    		return response.toString();
    	}
    	
    	return items.get(0).toString();
    }

}
