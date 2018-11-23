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
@RequestMapping("/rpc/v1/users")
public class UserControllerV1 {

    @Autowired
    private IUserService service;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseResult addUser(@RequestBody User user) {
        service.saveUser(user);
        return ResponseResult.generateResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseResult getUser(String accountToken) {
        User user = service.getUser(accountToken);
        return ResponseResult.generateResult(user != null, user);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseResult deleteUser(String accountToken) {
        service.deleteUser(accountToken);
        return ResponseResult.generateResult(true);
    }
}
