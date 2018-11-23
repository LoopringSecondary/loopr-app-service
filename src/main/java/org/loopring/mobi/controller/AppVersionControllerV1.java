package org.loopring.mobi.controller;

import java.util.List;

import org.loopring.mobi.dto.AppVersionsResponse;
import org.loopring.mobi.dto.ResponseResult;
import org.loopring.mobi.persistence.model.AppVersionsV1;
import org.loopring.mobi.service.IAppVersionServiceV1;
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
@RequestMapping("/rpc/v1/app_versions")
public class AppVersionControllerV1 {

    @Autowired
    private IAppVersionServiceV1 appVersionService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseResult save(@RequestBody AppVersionsV1 appVersions) {
        appVersionService.save(appVersions);
        return ResponseResult.generateResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public AppVersionsResponse getAll() {
        List<AppVersionsV1> list = appVersionService.getAll();
        return AppVersionsResponse.builder().count(list.size()).app_versions(list).build();
    }
}
