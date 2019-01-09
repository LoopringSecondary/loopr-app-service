package org.loopring.mobi.controller;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import java.util.HashSet;

import org.loopring.mobi.dto.ResponseResult;
import org.loopring.mobi.persistence.model.Device;
import org.loopring.mobi.service.IDeviceService;
import org.loopring.mobi.service.impl.PushNotificationService;
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

    @Autowired
    private PushNotificationService pushNotificationService;
    
    @RequestMapping(value = "/addDevice", method = RequestMethod.POST, produces = "application/json")
    public ResponseResult add(@RequestBody Device device) {
        deviceService.save(device);
        return ResponseResult.generateResult(true);
    }

    @RequestMapping(value = "/deleteDevice", method = RequestMethod.DELETE)
    public ResponseResult delete(String deviceToken, String address) {
        deviceService.delete(deviceToken, address);
        return ResponseResult.generateResult(true);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.POST, produces = "application/json")
    public void greeting(String alertBody) {
    	System.out.println(String.format("Alert body: %s", alertBody));
    	List<Device> devices = deviceService.getByBundleIdentifier("io.upwallet.app");
    	HashSet<String> deviceTokenSet = new HashSet<>();
    	List<Device> devicesWithoutDuplicateDeviceToken = devices.stream()
    	            .filter(e -> deviceTokenSet.add(e.getDeviceToken()))
    	            .collect(Collectors.toList());
    	for(Device device: devicesWithoutDuplicateDeviceToken) {
    		pushNotificationService.send(device, alertBody);
    	}
    }
    
    @RequestMapping(value = "/testNotification", method = RequestMethod.POST, produces = "application/json")
    public void test(String alertBody, String address) {
    	System.out.println(String.format("Alert body: %s", alertBody));
    	List<Device> devices = deviceService.getByBundleIdentifier("io.upwallet.app");
    	HashSet<String> deviceTokenSet = new HashSet<>();
    	List<Device> devicesWithoutDuplicateDeviceToken = devices.stream()
    	            .filter(e -> deviceTokenSet.add(e.getDeviceToken()))
    	            .collect(Collectors.toList());
    	for(Device device: devicesWithoutDuplicateDeviceToken) {
    		if(device.getAddress().equals(address)) {
    			pushNotificationService.send(device, alertBody);
    		}
    	}
    }
}
