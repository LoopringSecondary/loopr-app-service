package org.loopring.mobi.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.AppVersionsV1;
import org.loopring.mobi.persistence.repo.AppVersionRepository;
import org.loopring.mobi.service.IAppVersionServiceV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-22 11:21 AM
 * Cooperation: loopring.org 路印协议基金会
 */
@Service
@Transactional
public class AppVersionServiceV1 implements IAppVersionServiceV1 {

    @Autowired
    private AppVersionRepository repository;

    @Override
    public void save(AppVersionsV1 appVersions) {
        repository.save(appVersions);
    }

    @Override
    public List<AppVersionsV1> getAll() {
        return repository.findAllByOrderByCreateAtAsc();
    }
}
