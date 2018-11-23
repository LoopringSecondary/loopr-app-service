package org.loopring.mobi.controller;

import org.loopring.mobi.dto.ResponseResult;
import org.loopring.mobi.persistence.model.Device;
import org.loopring.mobi.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc/v1/device")
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @RequestMapping(value = "/greeting", method = RequestMethod.POST, produces = "application/json")
    public ResponseResult greeting(@RequestBody Device device) {
        deviceService.save(device);
        return ResponseResult.generateResult(true);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseResult delete(String deviceToken, String address) {
        deviceService.delete(deviceToken, address);
        return ResponseResult.generateResult(true);
    }
    //    @RequestMapping(value = "/greeting", method = RequestMethod.POST, produces = "application/json")
    //    public String greeting(@RequestBody Map<String, Object> postPayload) {
    //        System.out.println("/api/devices " + postPayload);
    //        String address = postPayload.get("address").toString();
    //        String bundleIdentifier = postPayload.get("bundleIdentifier").toString();
    //        String deviceToken = postPayload.get("deviceToken").toString();
    //        boolean isReleaseMode = (Boolean) postPayload.get("isReleaseMode");
    //        String currentInstalledVersion = postPayload.get("currentInstalledVersion").toString();
    //        String currentLanguage = postPayload.get("currentLanguage").toString();
    //        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    //        // Check whether it exists.
    //        String selectSQL = String.format("SELECT * " + "FROM devices " + "WHERE address='%s' AND bundle_identifier='%s' AND device_token='%s' AND is_release_mode=%s", address, bundleIdentifier, deviceToken, isReleaseMode);
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
    //        if (items.size() > 0) {
    //            // update new data
    //            String updateSQL = String.format("UPDATE devices " + "SET is_enabled=True, " + "current_language='%s' " + "WHERE address='%s' AND bundle_identifier='%s' AND device_token='%s' AND is_release_mode=%s", currentLanguage, address, bundleIdentifier, deviceToken, isReleaseMode);
    //            int row = jdbcTemplate.update(updateSQL);
    //            System.out.println(row + " row updated.");
    //            System.out.println("data exists");
    //            JSONObject response = new JSONObject();
    //            response.put("success", true);
    //            response.put("message", "exists");
    //            return response.toString();
    //        }
    //        // insert new data
    //        String insertSQL = String.format("INSERT INTO tbl_devices (address, bundle_identifier, device_token, is_release_mode, current_installed_version, current_language) " + "VALUES (?, ?, ?, ?, ?, ?)");
    //        // define query arguments
    //        // define SQL types of the arguments
    //        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.VARCHAR};
    //        int row = jdbcTemplate.update(insertSQL, params, types);
    //        System.out.println(row + " row inserted.");
    //        JSONObject response = new JSONObject();
    //        response.put("success", true);
    //        return response.toString();
    //    }
    // deviceToken is unique in devices table
    //    @RequestMapping(value = "/api/devices/{deviceToken}/{address}", method = RequestMethod.DELETE)
    //    public String delete(@PathVariable("deviceToken") String deviceToken, @PathVariable("address") String address) {
    //        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
    //        // TODO: not need to check if it exists.
    //        // Check whether it exists.
    //        String selectSQL = String.format("SELECT * " + "FROM tbl_devices " + "WHERE device_token='%s' AND address='%s'", deviceToken, address);
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
    //        if (items.size() > 0) {
    //            System.out.println("data exists");
    //            // update new data
    //            String updateSQL = String.format("UPDATE tbl_devices SET is_enabled=False " + "WHERE device_token='%s' AND address='%s'", deviceToken, address);
    //            int row = jdbcTemplate.update(updateSQL);
    //            System.out.println(row + " row updated.");
    //            JSONObject response = new JSONObject();
    //            response.put("success", true);
    //            return response.toString();
    //        }
    //        JSONObject response = new JSONObject();
    //        response.put("success", false);
    //        response.put("message", "data not found");
    //        return response.toString();
    //    }
}
