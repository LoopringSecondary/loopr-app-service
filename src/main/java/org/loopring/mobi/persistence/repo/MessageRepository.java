package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-20 3:24 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface MessageRepository extends JpaRepository<Message, Long> {


}
