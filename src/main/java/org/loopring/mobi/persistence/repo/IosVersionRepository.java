package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.IosVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 4:55 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IosVersionRepository extends JpaRepository<IosVersion, Long> {

    IosVersion getByVersion(String version);

    @Query(nativeQuery = true, value = "select * from tbl_ios_versions v order by v.id desc limit 1")
    IosVersion getLatestVersion();
}
