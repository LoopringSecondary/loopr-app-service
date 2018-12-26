package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.IosVersion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 4:55 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IosVersionRepository extends JpaRepository<IosVersion, Long> {

    IosVersion findByVersion(String version);

    IosVersion findFirstByOrderByIdDesc();
}
