package application;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class UserController {

	@RequestMapping(value = "/api/v1/users", method = RequestMethod.POST, produces = "application/json")
    public String greeting(@RequestBody Map<String, Object> postPayload) {
		
		System.out.println("POST /api/v1/users "+ postPayload);
		
		String accountToken = postPayload.get("account_token").toString();

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
    		return put(postPayload);
    	}
 
    	// insert new data
    	String insertSQL = String.format(
    			"INSERT INTO users (account_token) " +
    			"VALUES (?)");
    	
    	// define query arguments
    	Object[] params = new Object[] {accountToken};
    	
    	// define SQL types of the arguments
    	int[] types = new int[] { Types.VARCHAR};
    	
    	int row = jdbcTemplate.update(insertSQL, params, types);
    	System.out.println(row + " row inserted.");
    	    	
        JSONObject response = new JSONObject();
		response.put("success", true);

        return response.toString();
    }
	
	@RequestMapping(value = "/api/v1/users", method = RequestMethod.PUT, produces = "application/json")
    public String put(@RequestBody Map<String, Object> payload) {
		
		System.out.println("PUT /api/v1/users "+ payload);
		
		// Make it flexible?
		String accountToken = payload.get("account_token").toString();
		String language = payload.get("language").toString();
		String currency = payload.get("currency").toString();

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
    	
    	if(items.size() != 1) {
    		System.out.println("no data");
    		JSONObject response = new JSONObject();
    		response.put("success", false);
    		response.put("message", "no account token");
    		return response.toString();
    	}

    	
    	// insert new data    	 
    	String insertSQL = String.format(
    			"UPDATE users " +
    			"SET language='%s', currency='%s', updated_at=NOW() " +
    			"WHERE account_token='%s'",
    			language, currency, accountToken);

    	int row = jdbcTemplate.update(insertSQL);
    	System.out.println(row + " row inserted.");
    	    	
        JSONObject response = new JSONObject();
		response.put("success", true);

        return response.toString();
    }
	
	@RequestMapping(value = "/api/v1/users", method = RequestMethod.GET, produces = "application/json")
    public String greeting(@RequestParam("account_token") String accountToken) {
    	
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
