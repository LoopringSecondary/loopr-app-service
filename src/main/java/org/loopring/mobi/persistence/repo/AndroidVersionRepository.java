package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.AndroidVersion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 4:55 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface AndroidVersionRepository extends JpaRepository<AndroidVersion, Long> {

    AndroidVersion findByVersion(String version);

    AndroidVersion findFirstByOrderByIdDesc();
}
