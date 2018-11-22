package org.loopring.mobi.service.impl;

import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.AndroidVersion;
import org.loopring.mobi.persistence.repo.AndroidVersionRepository;
import org.loopring.mobi.service.IAndroidVersionService;
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
public class AndroidVersionService implements IAndroidVersionService {

    @Autowired
    private AndroidVersionRepository repository;

    @Override
    public void newVersion(AndroidVersion androidVersion) {
        AndroidVersion existVersion = repository.getByVersion(androidVersion.getVersion());
        if (existVersion != null) {
            existVersion.setBaiduUri(androidVersion.getBaiduUri());
            existVersion.setGoogleUri(androidVersion.getGoogleUri());
            existVersion.setDescription(androidVersion.getDescription());
            repository.save(existVersion);
        } else {
            repository.save(androidVersion);
        }
    }

    @Override
    public AndroidVersion getLatestVersion() {
        return repository.getLatestVersion();
    }
}
