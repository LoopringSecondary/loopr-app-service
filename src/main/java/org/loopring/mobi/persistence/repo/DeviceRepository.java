package org.loopring.mobi.persistence.repo;

import java.util.List;

import org.loopring.mobi.persistence.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 6:22 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select count(a) from Device a where a.address = :#{#device.address} " +
            "and a.deviceToken = :#{#device.deviceToken}")
    int count(@Param("device") Device device);

    @Modifying
    @Query("update Device a set a.isEnabled = :#{#device.isEnabled}, a.currentLanguage = :#{#device.currentLanguage} " +
            "where a.address = :#{#device.address} and a.bundleIdentifier = :#{#device.bundleIdentifier} and a.deviceToken = :#{#device.deviceToken} and a.isReleaseMode = :#{#device.isReleaseMode}")
    void update(@Param("device") Device device);

    @Modifying
    @Query("update Device a set a.isEnabled = false where a.deviceToken = :deviceToken and a.address = :address")
    void delete(@Param("deviceToken") String deviceToken, @Param("address") String address);

    List<Device> findAllByBundleIdentifierAndIsEnabled(String bundleIdentifier, Boolean isEnabled);

    List<Device> findAllByAddressAndIsEnabled(String address, Boolean isEnabled);
}
