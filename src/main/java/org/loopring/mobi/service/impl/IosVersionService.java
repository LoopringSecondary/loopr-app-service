package org.loopring.mobi.service.impl;

import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.IosVersion;
import org.loopring.mobi.persistence.repo.IosVersionRepository;
import org.loopring.mobi.service.IIosVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 5:03 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Service
@Transactional
public class IosVersionService implements IIosVersionService {

    @Autowired
    private IosVersionRepository repository;

    @Override
    public void newVersion(IosVersion iosVersion) {
        IosVersion existVersion = repository.findByVersion(iosVersion.getVersion());
        if (existVersion != null) {
            existVersion.setUri(iosVersion.getUri());
            existVersion.setDescription(iosVersion.getDescription());
            repository.save(existVersion);
        } else {
            repository.save(iosVersion);
        }
    }

    @Override
    public IosVersion getLatestVersion() {
        return repository.findFirstByOrderByIdDesc();
    }
}
