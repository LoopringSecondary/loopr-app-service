package org.loopring.mobi.controller;

import org.loopring.mobi.dto.ResponseResult;
import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService service;

    @ResponseBody
    @RequestMapping(value = "/v1/addUser", method = RequestMethod.POST)
    public ResponseResult addUser(@RequestBody User user) {
        service.saveUser(user);
        return ResponseResult.generateResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "/v1/getUser")
    public ResponseResult getUser(String accountToken) {
        return ResponseResult.generateResult(service.getUser(accountToken));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/deleteUser", method = RequestMethod.DELETE)
    public ResponseResult deleteUser(String accountToken) {
        service.deleteUser(accountToken);
        return ResponseResult.generateResult(true);
    }
}
