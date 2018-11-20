package org.loopring.mobi.service;

import org.loopring.mobi.persistence.model.AndroidVersion;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 5:02 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IAndroidVersionService {

    void newVersion(AndroidVersion androidVersion);

    AndroidVersion getLatestVersion();
}
