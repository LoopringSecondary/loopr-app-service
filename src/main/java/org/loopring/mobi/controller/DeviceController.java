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

    @RequestMapping(value = "/addDevice", method = RequestMethod.POST, produces = "application/json")
    public ResponseResult greeting(@RequestBody Device device) {
        deviceService.save(device);
        return ResponseResult.generateResult(true);
    }

    @RequestMapping(value = "/deleteDevice", method = RequestMethod.DELETE)
    public ResponseResult delete(String deviceToken, String address) {
        deviceService.delete(deviceToken, address);
        return ResponseResult.generateResult(true);
    }
}
