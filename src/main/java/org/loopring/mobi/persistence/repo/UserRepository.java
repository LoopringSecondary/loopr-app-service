package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByAccountToken(String accountToken);

    User findUserByAccountTokenAndIsDeleted(String accountToken, boolean isDeleted);
}
