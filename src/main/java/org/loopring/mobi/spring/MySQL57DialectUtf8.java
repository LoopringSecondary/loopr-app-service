package org.loopring.mobi.spring;

import org.hibernate.dialect.MySQL57Dialect;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 12:24 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class MySQL57DialectUtf8 extends MySQL57Dialect {

    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
