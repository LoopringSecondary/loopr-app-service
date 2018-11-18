package org.loopring.mobi.service;

import org.loopring.mobi.persistence.model.User;

public interface IUserService {

    User getUser(String userName);

    void saveUser(User user);

    void deleteUser(User user);

}
