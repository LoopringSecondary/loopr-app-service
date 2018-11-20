package org.loopring.mobi.spring;

import java.util.Properties;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.loopring.mobi.persistence.model.User;
import org.loopring.mobi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;

@Primary
@ComponentScan({"org.loopring.mobi.*"})
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDbConfig extends PersistenceJPAConfig {

    @Autowired
    private IUserService userService;

    @Override
    public DataSource dataSource() {
        EmbeddedDatabase datasource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        return datasource;
    }

    @Override
    protected Properties additionalProperties() {
        Properties properties = super.additionalProperties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return properties;
    }

    @Test
    public void test() {
        User user = User.builder().accountToken("laiyy").config("{}").build();
        //        User user = new User();
        //        user.setAccountToken("laiyy");
        //        user.setConfig("");
        userService.saveUser(user);
    }
}
