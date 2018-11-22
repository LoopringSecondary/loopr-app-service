/**
 * Created with IntelliJ IDEA.
 * User: kenshin chenwang34@creditease.cn
 * Time: 2018-11-22 4:44 PM
 * Cooperation: CreditEase©2017 普信恒业科技发展(北京)有限公司
 */
package org.loopring.mobi.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.persistence.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceJPAConfig.class}, loader = AnnotationConfigContextLoader.class)

public class UserRepoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserRepository repository;

    @Test
    public void testSave() {
        User laiyy = User.builder().accountToken("laiyy")
                .config("{}").isDeleted(false).build();
        laiyy = repository.saveAndFlush(laiyy);
        assertNotNull(laiyy);
    }
}
