package org.loopring.mobi.persistence.repo;

import java.util.List;

import org.loopring.mobi.persistence.model.AppVersionsV1;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-22 11:22 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface AppVersionRepository extends JpaRepository<AppVersionsV1, Long> {

    List<AppVersionsV1> findAllByOrderByCreateAtAsc();
}
