package org.loopring.mobi.service;

import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.persistence.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;
    // API

    @Override
    public User getUser(final String userName) {
        final User user = repository.findByUserName(userName);
        return user;
    }

    @Override
    public void saveUser(final User user) {
        repository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        repository.delete(user);
    }
}
