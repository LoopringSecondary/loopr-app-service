package org.loopring.mobi.controller;

import org.loopring.mobi.dto.ResponseResult;
import org.loopring.mobi.persistence.model.AndroidVersion;
import org.loopring.mobi.persistence.model.IosVersion;
import org.loopring.mobi.service.IAndroidVersionService;
import org.loopring.mobi.service.IIosVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 5:05 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@RestController
@RequestMapping("/api/v1/version")
public class AppVersionController {

    @Autowired
    private IIosVersionService iosVersionService;

    @Autowired
    private IAndroidVersionService androidVersionService;

    @ResponseBody
    @RequestMapping(value = "/ios/new", method = RequestMethod.POST)
    public ResponseResult newVersion(@RequestBody IosVersion iosVersion) {
        iosVersionService.newVersion(iosVersion);
        return ResponseResult.generateResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "/android/new", method = RequestMethod.POST)
    public ResponseResult newVersion(@RequestBody AndroidVersion androidVersion) {
        androidVersionService.newVersion(androidVersion);
        return ResponseResult.generateResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "/ios/getLatest")
    public ResponseResult getIosLatestVersion() {
        return ResponseResult.generateResult(iosVersionService.getLatestVersion());
    }

    @ResponseBody
    @RequestMapping(value = "/android/getLatest")
    public ResponseResult getAndroidLatestVersion() {
        return ResponseResult.generateResult(androidVersionService.getLatestVersion());
    }
}
