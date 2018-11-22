package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByAccountToken(String accountToken);

    User getUserByAccountTokenAndIsDeleted(String accountToken, boolean isDeleted);
}
