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
 * Time: 2018-11-19 4:50 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "tbl_android_versions")
public class AndroidVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @Column(length = 128, unique = true, nullable = false)
    private String version;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "baidu_uri", columnDefinition = "TEXT")
    private String baiduUri;

    @Column(name = "google_uri", columnDefinition = "TEXT")
    private String googleUri;
}
