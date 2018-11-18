package org.loopring.mobi.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.loopring.mobi.persistence.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    @Override
    void delete(User user);

}
