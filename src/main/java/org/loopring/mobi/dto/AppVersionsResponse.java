package org.loopring.mobi.dto;

import java.util.List;

import org.loopring.mobi.persistence.model.AppVersionsV1;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-22 11:29 AM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class AppVersionsResponse {

    private Integer count;

    private List<AppVersionsV1> app_versions;
}
