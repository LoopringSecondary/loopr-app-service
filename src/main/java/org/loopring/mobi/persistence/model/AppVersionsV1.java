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
 * Time: 2018-11-22 11:15 AM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_app_versions")
public class AppVersionsV1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @Column(length = 128, unique = true, nullable = false)
    private String version;

    @Column(name = "must_update", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private Boolean mustUpdate = false;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description = "";

    @Column(name = "current_installed_version", columnDefinition = "varchar(128) NOT NULL DEFAULT '0.0.1'")
    private String currentInstalledVersion = "0.0.1";

    @Column(name = "baidu_uri", columnDefinition = "TEXT")
    private String baiduUri;

    @Column(name = "google_uri", columnDefinition = "TEXT")
    private String googleUri;

    @Column(name = "ios_uri", columnDefinition = "TEXT")
    private String iosUri;
}
