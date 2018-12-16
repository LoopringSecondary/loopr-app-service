package org.loopring.mobi.persistence.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Transient
    @JsonProperty("created_at")
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Transient
    @JsonProperty("updated_at")
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @JsonProperty("address")
    @Column(length = 1024, nullable = false)
    private String address;

    @JsonProperty("bundle_identifier")
    @Column(name = "bundle_identifier", length = 128, nullable = false)
    private String bundleIdentifier;

    @JsonProperty("device_token")
    @Column(name = "device_token", nullable = false, columnDefinition = "TEXT")
    private String deviceToken;

    @Builder.Default
    @JsonProperty("is_enabled")
    @Column(name = "is_enabled", columnDefinition = "tinyint(1) NOT NULL DEFAULT '1'")
    private Boolean isEnabled = true;

    @Builder.Default
    @JsonProperty("is_release_mode")
    @Column(name = "is_release_mode", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private Boolean isReleaseMode = false;

    @JsonProperty("current_language")
    @Column(name = "current_language", length = 32)
    private String currentLanguage;
}
