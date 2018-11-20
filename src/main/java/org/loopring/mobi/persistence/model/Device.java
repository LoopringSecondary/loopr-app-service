package org.loopring.mobi.persistence.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 6:14 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @Column(length = 1024, nullable = false)
    private String address;

    @Column(name = "bundle_identifier", length = 128, nullable = false)
    private String bundleIdentifier;

    @Column(name = "device_token", nullable = false, columnDefinition = "TEXT")
    private String deviceToken;

    @Column(name = "is_enabled", columnDefinition = "tinyint(1) NOT NULL DEFAULT '1'")
    private Boolean isEnabled = true;

    @Column(name = "is_release_mode", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private Boolean isReleaseMode = false;

    @Column(name = "current_language", length = 32)
    private String currentLanguage;
}
