package org.loopring.mobi.persistence.repo;

import org.loopring.mobi.persistence.model.AndroidVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 4:55 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface AndroidVersionRepository extends JpaRepository<AndroidVersion, Long> {

    AndroidVersion getByVersion(String version);

    @Query(nativeQuery = true, value = "select * from tbl_android_versions v order by v.id desc limit 1")
    AndroidVersion getLatestVersion();
}
