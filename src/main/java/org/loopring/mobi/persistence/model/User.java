package org.loopring.mobi.persistence.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 11:20 AM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("created_at")
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @JsonProperty("updated_at")
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @JsonProperty("account_token")
    @Column(name = "account_token", unique = true, nullable = false)
    private String accountToken;

    @JsonProperty("config")
    @Column(columnDefinition = "json")
    private String config;

    @Builder.Default
    @JsonProperty("is_deleted")
    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean isDeleted = false;
}
