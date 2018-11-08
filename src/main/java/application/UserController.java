package application;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
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
    public String post(@RequestBody String postPayloadString) {

        JSONObject postPayload = new JSONObject(postPayloadString);
        System.out.println("POST /api/v1/users " + postPayload);

        String accountToken;
        if (postPayload.has("account_token")) {
            accountToken = postPayload.get("account_token").toString();
        } else {
            JSONObject response = new JSONObject();
            response.put("success", false);
            response.put("message", "no account token");
            return response.toString();
        }

        // Key is config, however it will be stored into config_json_string
        String config = null;
        if (postPayload.has("config")) {
            config = postPayload.get("config").toString();
            System.out.println("Config: " + config);
        }

        System.out.println("Verified params");

        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();

        // Check whether it exists.
        String selectSQL = String.format("SELECT * " + "FROM tbl_users " + "WHERE account_token='%s'", accountToken);

        List<JSONObject> items = jdbcTemplate.query(selectSQL, (rs, rowNum) -> {
            JSONObject item = new JSONObject();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                item.put(column_name, rs.getObject(column_name));
            }
            return item;
        });

        // Update if exists
        if (items.size() == 1) {
            System.out.println("Update tbl_users");
            // insert new data

            String insertSQL = String.format("UPDATE tbl_users SET config=?, is_deleted=False, updated_at=NOW() WHERE account_token=?");

            // define query arguments
            Object[] params = new Object[]{config, accountToken};
            int[] types = new int[]{Types.VARCHAR, Types.VARCHAR};

            int row = jdbcTemplate.update(insertSQL, params, types);
            System.out.println(row + " row updated.");

            JSONObject response = new JSONObject();
            response.put("success", true);
            return response.toString();

        } else {
            // insert new data
            String insertSQL = String.format("INSERT INTO tbl_users (config, account_token) " + "VALUES (?,?)");
            Object[] params = new Object[]{config, accountToken};
            int[] types = new int[]{Types.VARCHAR, Types.VARCHAR};
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

        String sql = String.format("SELECT * " + "FROM tbl_users " + "WHERE account_token='%s' AND is_deleted=False", accountToken);

        System.out.println(sql);

        List<JSONObject> items = jdbcTemplate.query(sql, (rs, rowNum) -> {
            JSONObject item = new JSONObject();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                item.put(column_name, rs.getObject(column_name));
            }
            return item;
        });

        if (items.size() != 1) {
            System.out.println("no data");
            JSONObject response = new JSONObject();
            response.put("success", false);
            response.put("message", "no account token");
            return response.toString();
        }

        JSONObject item = items.get(0);
        // Keep config_json_string
        if (item.has("config")) {
            item.put("config", item.get("config"));
        } else {
            item.remove("config");
        }
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("message", item.toString());
        return response.toString();
    }

    @RequestMapping(value = "/api/v1/users", method = RequestMethod.DELETE, produces = "application/json")
    public String delete(@RequestBody Map<String, Object> payload) {

        System.out.println("DELETE /api/v1/users " + payload);

        String accountToken;
        if (payload.containsKey("account_token")) {
            accountToken = payload.get("account_token").toString();
        } else {
            JSONObject response = new JSONObject();
            response.put("success", false);
            response.put("message", "no account token");
            return response.toString();
        }

        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();

        // Check whether it exists.
        String selectSQL = String.format("SELECT * " + "FROM tbl_users " + "WHERE account_token='%s'", accountToken);

        List<JSONObject> items = jdbcTemplate.query(selectSQL, new RowMapper<JSONObject>() {
            @Override
            public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {

                JSONObject item = new JSONObject();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numColumns = rsmd.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    item.put(column_name, rs.getObject(column_name));
                }
                return item;
            }
        });

        // Update if exists
        if (items.size() > 0) {
            System.out.println("Update tbl_users");
            // insert new data

            String insertSQL = String.format("UPDATE tbl_users " + "SET is_deleted=True, updated_at=NOW() " + "WHERE account_token=?");

            // define query arguments
            Object[] params = new Object[]{accountToken};
            int[] types = new int[]{Types.VARCHAR};
            int row = jdbcTemplate.update(insertSQL, params, types);
            System.out.println(row + " row updated.");
            JSONObject response = new JSONObject();
            response.put("success", true);
            return response.toString();
        } else {
            System.out.println("no data");
            JSONObject response = new JSONObject();
            response.put("success", false);
            response.put("message", "no account token");
            return response.toString();
        }
    }

}
