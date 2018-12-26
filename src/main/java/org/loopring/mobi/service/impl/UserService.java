package org.loopring.mobi.service.impl;

import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.persistence.repo.UserRepository;
import org.loopring.mobi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;
    // API

    @Override
    public User getUser(String accountToken) {
        return repository.findUserByAccountTokenAndIsDeleted(accountToken, false);
    }

    @Override
    public void saveUser(User user) {
        User existUser = repository.findUserByAccountToken(user.getAccountToken());
        if (existUser != null) {
            // update
            existUser.setIsDeleted(false);
            existUser.setConfig(user.getConfig());
            repository.save(existUser);
        } else {
            // add
            repository.save(user);
        }
    }

    @Override
    public void deleteUser(String accountToken) {
        User user = repository.findUserByAccountTokenAndIsDeleted(accountToken, false);
        if (user != null) {
            user.setIsDeleted(true);
            repository.save(user);
        }
    }
}
