package org.loopring.mobi.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.loopring.mobi.persistence.model.Device;
import org.loopring.mobi.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class Crawlers {

    private static final Logger log = LoggerFactory.getLogger(Crawlers.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final int rateInSecond = 600 * 60;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private PushNotificationService pushNotificationService;

    // loopring_getTransactions
    public void processTransactions(int currentTime, String address) {
        // TODO: support only three tokens in the first version
        String[] symbols = new String[]{"ETH", "WETH", "LRC"};
        for (String symbol : symbols) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("method", "loopring_getTransactions");
            jsonObject.put("id", UUID.randomUUID().toString());
            Map<String, String> params = new HashMap<>();
            params.put("owner", address);
            params.put("symbol", symbol);
            params.put("txType", "receive");
            jsonObject.put("params", new Map[]{params});
            // TODO: add retry
            String response = sendPostRequest("https://relay1.loopr.io/rpc/v2", jsonObject.toString());
            // System.out.println(response);
            JSONObject responseObject = JSONObject.parseObject(response);
            if (responseObject.containsKey("result")) {
                log.info("No transactions found.");
                continue;
            }
            JSONObject results = responseObject.getJSONObject("result");
            JSONArray data = results.getJSONArray("data");
            for (int n = 0; n < data.size(); n++) {
                JSONObject object = data.getJSONObject(n);
                // do some stuff....
                int createTime = object.getInteger("createTime");
                if (currentTime - createTime < rateInSecond) {
                    System.out.println("About to processTransactions");
                    System.out.println("CreateTime: " + createTime);
                    System.out.println("CurrentTime: " + currentTime);
                    System.out.println(currentTime - createTime);
                    System.out.println(object.toString());
                    String value = object.getString("value");
                    String alertBody = String.format("Address %s received %s %s.", address, stringToBigInteger(value), symbol);
                    System.out.println(alertBody);
                    Optional.ofNullable(deviceService.getByAddress(address))
                            .orElse(Collections.emptyList())
                            .forEach(device -> pushNotificationService.send(device, alertBody));
                    //                    PushNotificationService.process(address, alertBody);
                    System.out.println(".....");
                }
            }
        }
    }

    // loopring_getOrders
    public void processOrders(int currentTime, String address, String orderType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "loopring_getOrders");
        jsonObject.put("id", UUID.randomUUID().toString());
        Map<String, String> params = new HashMap<>();
        params.put("owner", address);
        params.put("status", "ORDER_FINISHED");
        params.put("orderType", orderType);
        jsonObject.put("params", new Map[]{params});
        // TODO: add retry
        String response = sendPostRequest("https://relay1.loopr.io/rpc/v2", jsonObject.toString());
        // System.out.println(response);
        JSONObject responseObject = JSONObject.parseObject(response);
        JSONArray results = responseObject.getJSONObject("result").getJSONArray("data");
        for (int n = 0; n < results.size(); n++) {
            JSONObject object = results.getJSONObject(n).getJSONObject("originalOrder");
            // do some stuff....
            int createTime = object.getInteger("createTime");
            // System.out.println(object.toString());
            if (currentTime - createTime < rateInSecond) {
                System.out.println("About to send PushNotificationService");
                System.out.println("CreateTime: " + createTime);
                System.out.println("CurrentTime: " + currentTime);
                System.out.println(currentTime - createTime);
                String side;
                if (orderType.equals("p2p_order")) {
                    if (object.getString("p2pSide").equals("maker")) {
                        side = "sell";
                    } else {
                        side = "buy";
                    }
                } else {
                    side = object.getString("side");
                }
                String alertBody;
                if (side.equals("sell")) {
                    String amountS = object.getString("amountS");
                    String tokenS = object.getString("tokenS");
                    alertBody = String.format("Address %s bought %s %s", address, stringToBigInteger(amountS), tokenS);
                } else {
                    String amountB = object.getString("amountB");
                    String tokenB = object.getString("tokenB");
                    alertBody = String.format("Address %s sold %s %s", address, stringToBigInteger(amountB), tokenB);
                }
                if (orderType.equals("p2p_order")) {
                    alertBody = alertBody + " in a P2P order.";
                } else {
                    alertBody = alertBody + " in a market order.";
                }
                System.out.println(alertBody);
                String finalAlertBody = alertBody;
                Optional.ofNullable(deviceService.getByAddress(address))
                        .orElse(Collections.emptyList())
                        .forEach(device -> pushNotificationService.send(device, finalAlertBody));
//                PushNotificationService.process(address, alertBody);
                System.out.println(".....");
            }
        }
    }

    public String stringToBigInteger(String value) {
        // TODO: consider other decimals.
        int decimals = 18;
        String amount;
        if (value.startsWith("0x")) {
            amount = new BigInteger(value.substring(2), 16).toString();
        } else {
            amount = value;
        }
        if (decimals >= amount.length()) {
            String prepend = String.join("", Collections.nCopies(decimals - amount.length() + 1, "0"));
            amount = prepend + amount;
        }
        System.out.println(amount);
        amount = amount.substring(0, amount.length() - decimals) + "." + amount.substring(amount.length() - decimals, amount
                .length());
        Double amountInDouble = Double.parseDouble(amount);
        return formatDouble(amountInDouble);
    }

    private static String formatDouble(Double value) {
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(18);
        System.out.println(df.format(value));
        return df.format(value);
    }

    public String sendPostRequest(String requestUrl, String payload) {
        try {
            // System.out.println("payload: " + payload);
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

    @Scheduled(fixedRate = rateInSecond * 1000)
    public void pushNotificationsForSend() {
        Instant now = Instant.now();
        Long longTime = now.toEpochMilli() / 1000;
        int currentTime = longTime.intValue();
        System.out.println("Current time: " + now.toString());
        //        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();
        //        String selectSQL = String.format("SELECT address " + "FROM tbl_devices " + "WHERE is_enabled=True AND bundle_identifier='%s'", "leaf.prod.app");
        //        List<JSONObject> items = jdbcTemplate.query(selectSQL, (rs, rowNum) -> {
        //            JSONObject item = new JSONObject();
        //            ResultSetMetaData rsmd = rs.getMetaData();
        //            int numColumns = rsmd.getColumnCount();
        //            for (int i = 1; i <= numColumns; i++) {
        //                String column_name = rsmd.getColumnName(i);
        //                item.put(column_name, rs.getObject(column_name));
        //            }
        //            return item;
        //        });
        List<Device> deviceList = deviceService.getByBundleIdentifier("leaf.prod.app");
        deviceList.forEach(device -> {
            processTransactions(currentTime, device.getAddress());
            // processOrders(currentTime, address, "p2p_order");
            // processOrders(currentTime, address, "market_order");
        });
        //        for (JSONObject item : items) {
        //            String address = item.getString("address");
        //            processTransactions(currentTime, address);
        //            // processOrders(currentTime, address, "p2p_order");
        //            // processOrders(currentTime, address, "market_order");
        //        }
    }
}
