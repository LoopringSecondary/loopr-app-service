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
 * Time: 2018-11-19 4:50 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "tbl_ios_versions")
public class IosVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("created_at")
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @JsonProperty("updated_at")
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    @JsonProperty("version")
    @Column(length = 128, unique = true, nullable = false)
    private String version;

    @JsonProperty("description")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @JsonProperty("release_note_en")
    @Column(name = "release_note_en", nullable = false, columnDefinition = "TEXT")
    private String releaseNoteEN;

    @JsonProperty("release_note_chs")
    @Column(name = "release_note_chs", nullable = false, columnDefinition = "TEXT")
    private String releaseNoteCHS;

    @JsonProperty("release_note_cht")
    @Column(name = "release_note_cht", columnDefinition = "TEXT")
    private String releaseNoteCHT;

    @JsonProperty("uri")
    @Column(columnDefinition = "TEXT")
    private String uri;
}
