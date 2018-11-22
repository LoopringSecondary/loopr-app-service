package org.loopring.mobi.service;

import java.util.List;

import org.loopring.mobi.persistence.model.AppVersionsV1;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-22 11:21 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IAppVersionServiceV1 {

    void save(AppVersionsV1 appVersions);

    List<AppVersionsV1> getAll();
}
