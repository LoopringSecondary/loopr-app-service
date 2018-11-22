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
 * Time: 2018-11-20 3:21 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @Column(name = "bundle_identifier", length = 128, nullable = false)
    private String bundleIdentifier;

    @Column(name = "device_token", nullable = false, columnDefinition = "TEXT")
    private String deviceToken;

    @Column(name = "alert_body", nullable = false, columnDefinition = "TEXT")
    private String alertBody;
}
