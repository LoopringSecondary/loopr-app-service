package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    @Override
    void delete(User user);
}
