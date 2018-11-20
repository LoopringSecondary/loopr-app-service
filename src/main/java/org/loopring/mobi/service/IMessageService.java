package org.loopring.mobi.service;

import org.loopring.mobi.persistence.model.Message;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-20 3:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IMessageService {

    void save(Message message);
}
