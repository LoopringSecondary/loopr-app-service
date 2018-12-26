package org.loopring.mobi.spring;

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
import org.loopring.mobi.service.impl.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Crawlers {

    private static final Logger log = LoggerFactory.getLogger(Crawlers.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final int rateInSecond = 60;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private PushNotificationService pushNotificationService;

    // loopring_getTransactions
    public void processTransactions(int currentTime, Device device, String txType) {
        // TODO: support only three tokens in the first version
        String[] symbols = new String[]{"ETH", "WETH", "LRC"};
        for (String symbol : symbols) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("method", "loopring_getTransactions");
            jsonObject.put("id", UUID.randomUUID().toString());
            Map<String, String> params = new HashMap<>();
            params.put("owner", device.address);
            params.put("symbol", symbol);
            params.put("txType", txType);
            jsonObject.put("params", new Map[]{params});

            // TODO: add retry
            String response = sendPostRequest("https://relay1.loopr.io/rpc/v2", jsonObject.toString());
            // System.out.println(response);
            JSONObject responseObject = JSONObject.parseObject(response);
            // Valid the rpc response.
            if (!responseObject.containsKey("result")) {
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

                    String alertBody;
                    String alertBodyFormat = "";
                    
                    // Supported types: receive, buy, sell.
                    if (txType.equals("receive")) {
                    	if (device.currentLanguage.equals("zh-Hans")) {
                    		alertBodyFormat = "地址%s收到%s %s。";
                    	} else if (device.currentLanguage.equals("zh-Hant")) {
                    		alertBodyFormat = "地址%s收到%s %s。";
                    	} else {
                    		alertBodyFormat = "Address %s received %s %s.";
                    	}
                    } else if (txType.equals("buy")) {
                    	if (device.currentLanguage.equals("zh-Hans")) {
                    		alertBodyFormat = "地址%sma买入%s %s。";
                    	} else if (device.currentLanguage.equals("zh-Hant")) {
                    		alertBodyFormat = "地址%sma買入%s %s。";
                    	} else {
                    		alertBodyFormat = "Address %s bought %s %s.";
                    	}
                    } else if (txType.equals("sell")) {
                    	if (device.currentLanguage.equals("zh-Hans")) {
                    		alertBodyFormat = "地址%sma卖出%s %s。";
                    	} else if (device.currentLanguage.equals("zh-Hant")) {
                    		alertBodyFormat = "地址%sma賣出%s %s。";
                    	} else {
                    		alertBodyFormat = "Address %s sold %s %s.";
                    	}
                    } else {
                    	return;
                    }

                    alertBody = String.format(alertBodyFormat, device.address, stringToBigInteger(value), symbol);
                    System.out.println(alertBody);
                    pushNotificationService.send(device, alertBody);
                    System.out.println(".....");
                }
            }
        }
    }

    // loopring_getOrders
    public void processOrders(int currentTime, Device device, String orderType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "loopring_getOrders");
        jsonObject.put("id", UUID.randomUUID().toString());
        Map<String, String> params = new HashMap<>();
        params.put("owner", device.address);
        params.put("status", "ORDER_FINISHED");
        params.put("orderType", orderType);
        jsonObject.put("params", new Map[]{params});
        // TODO: add retry
        String response = sendPostRequest("https://relay1.loopr.io/rpc/v2", jsonObject.toString());
        // System.out.println(response);
        JSONObject responseObject = JSONObject.parseObject(response);
        // Valid the rpc response.
        if (!responseObject.containsKey("result")) {
            log.info("No transactions found.");
            return;
        }
        System.out.println(String.format("processOrders %s for %s", device.address, orderType));
        JSONArray results = responseObject.getJSONObject("result").getJSONArray("data");
        for (int n = 0; n < results.size(); n++) {
            JSONObject object = results.getJSONObject(n).getJSONObject("originalOrder");
            // do some stuff....
            // Couldn't do createTime
            int createTime = object.getInteger("createTime");

            System.out.println(object.toString());
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
                
                // Prepare the format of alert body.
                String buyFormat = "";
                String sellFormat = "";
                String p2pOrderTrailingFormat = "";
                String marketorderTrailingFormat = "";
                if (device.currentLanguage.equals("zh-Hans")) {
                	buyFormat = "地址%sma买入%s %s";
                	sellFormat = "地址%sma賣出%s %s";
                	p2pOrderTrailingFormat = "在一次场外交易中";
                	marketorderTrailingFormat = "在一次市场交易中";
                } else if (device.currentLanguage.equals("zh-Hant")) {
                	buyFormat = "地址%sma买入%s %s";
                	sellFormat = "地址%sma賣出%s %s";
                	p2pOrderTrailingFormat = "在一次場外交易中";
                	marketorderTrailingFormat = "在一次市場交易中";
                } else {
                	buyFormat = "Address %s bought %s %s";
                	sellFormat = "Address %s sold %s %s";
                	p2pOrderTrailingFormat = " in a P2P order.";
                	marketorderTrailingFormat = " in a market order.";
                }
                
                if (side.equals("sell")) {
                    String amountS = object.getString("amountS");
                    String tokenS = object.getString("tokenS");
                    alertBody = String.format(buyFormat, device.address, stringToBigInteger(amountS), tokenS);
                } else {
                    String amountB = object.getString("amountB");
                    String tokenB = object.getString("tokenB");
                    alertBody = String.format(sellFormat, device.address, stringToBigInteger(amountB), tokenB);
                }
                if (orderType.equals("p2p_order")) {
                	if (device.currentLanguage.equals("zh-Hans") || device.currentLanguage.equals("zh-Hant")) {
                		alertBody = String.format("%s, %s", p2pOrderTrailingFormat, alertBody);
                	} else {
                		alertBody = alertBody + p2pOrderTrailingFormat;
                	}
                } else {
                	if (device.currentLanguage.equals("zh-Hans") || device.currentLanguage.equals("zh-Hant")) {
                		alertBody = String.format("%s, %s", marketorderTrailingFormat, alertBody);
                	} else {
                		alertBody = alertBody + marketorderTrailingFormat;
                	}
                }
                System.out.println(alertBody);
                pushNotificationService.send(device, alertBody);
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

        Long longTime = new Long(now.toEpochMilli() / 1000);
        int currentTime = longTime.intValue();
        System.out.println("Current time: " + now.toString());
        
        List<Device> devices = deviceService.getByBundleIdentifier("io.upwallet.app");
        
        for (Device device: devices) {
        	System.out.println(device.address);
        	processTransactions(currentTime, device, "receive");
        	processTransactions(currentTime, device, "buy");
        	processTransactions(currentTime, device, "sell");
        	// processOrders(currentTime, device, "p2p_order");
        	// processOrders(currentTime, device, "market_order");
        }
    }

}
