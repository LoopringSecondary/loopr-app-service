package org.loopring.mobi.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.service.IUserService;

@Controller
public class UserController {

    @Autowired
    private IUserService service;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String getLoggedUsers(final Locale locale, final Model model, @RequestParam String userName) {

        User user = new User();
        user.setUserName(userName);
        service.saveUser(user);
        return "success";
    }
}
