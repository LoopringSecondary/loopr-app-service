package org.loopring.mobi.service.impl;

import java.util.List;
import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.Device;
import org.loopring.mobi.persistence.repo.DeviceRepository;
import org.loopring.mobi.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 6:20 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Service
@Transactional
public class DeviceService implements IDeviceService {

    @Autowired
    private DeviceRepository repository;

    @Override
    public void save(Device device) {
        if (repository.count(device) > 0) {
            repository.update(device);
        } else {
            repository.save(device);
        }
    }

    @Override
    public void delete(String deviceToken, String address) {
        repository.delete(deviceToken, address);
    }

    @Override
    public List<Device> getByBundleIdentifier(String bundleIdentifier) {
        return repository.getAllByBundleIdentifierAndIsEnabled(bundleIdentifier, true);
    }

    @Override
    public List<Device> getByAddress(String address) {
        return repository.getAllByAddressAndIsEnabled(address, true);
    }
}
