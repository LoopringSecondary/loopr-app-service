package org.loopring.mobi.service;

import java.util.List;

import org.loopring.mobi.persistence.model.Device;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 6:20 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IDeviceService {

    void save(Device device);

    void delete(String deviceToken, String address);

    List<Device> getByBundleIdentifier(String bundleIdentifier);

    List<Device> getByAddress(String address);
}
