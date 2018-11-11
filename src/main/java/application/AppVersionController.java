package application;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rds.DatabaseConnection;

@RestController
public class AppVersionController {

    @RequestMapping(value = "/api/v1/app_versions", method = RequestMethod.GET, produces = "application/json")
    public String greeting() {

        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();

        System.out.println("AppVersionController api/v1/app_versions");
        String sql = String.format("SELECT * " + "FROM tbl_app_versions");

        List<JSONObject> items = jdbcTemplate.query(sql, new RowMapper<JSONObject>() {
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

        JSONObject response = new JSONObject();
        response.put("count", items.size());
        response.put("app_versions", items);

        return response.toString();
    }

}
