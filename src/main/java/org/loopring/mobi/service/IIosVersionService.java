package org.loopring.mobi.service;

import org.loopring.mobi.persistence.model.IosVersion;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 5:02 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IIosVersionService {

    void newVersion(IosVersion iosVersion);

    IosVersion getLatestVersion();
}
