package org.loopring.mobi.service.impl;

import javax.transaction.Transactional;

import org.loopring.mobi.persistence.model.Message;
import org.loopring.mobi.persistence.repo.MessageRepository;
import org.loopring.mobi.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-20 3:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Service
@Transactional
public class MessageService implements IMessageService {

    @Autowired
    private MessageRepository repository;

    @Override
    public void save(Message message) {
        repository.save(message);
    }
}
