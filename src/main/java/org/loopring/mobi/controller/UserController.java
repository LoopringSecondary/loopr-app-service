package org.loopring.mobi.controller;

import org.loopring.mobi.dto.ResponseResult;
import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc/v1/user")
public class UserController {

    @Autowired
    private IUserService service;

    @ResponseBody
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResponseResult addUser(@RequestBody User user) {
        User user1 = service.getUser(user.getAccountToken());
        if (user1 != null) {
            user1.setConfig(user.getConfig());
        } else {
            user1 = user;
        }
        service.saveUser(user1);
        return ResponseResult.generateResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "/getUser")
    public ResponseResult getUser(@RequestParam("account_token") String accountToken) {
        User user = service.getUser(accountToken);
        return ResponseResult.generateResult(user != null, user);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public ResponseResult deleteUser(@RequestParam("account_token") String accountToken) {
        service.deleteUser(accountToken);
        return ResponseResult.generateResult(true);
    }
}
