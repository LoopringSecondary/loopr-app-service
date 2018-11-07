package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import models.Message;
import rds.DatabaseConnection;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        JdbcTemplate jdbcTemplate = DatabaseConnection.getJdbcTemplate();

        String searchMessageSql = String.format("SELECT * " + "FROM test");

        System.out.println(searchMessageSql);

        List<Message> messages = jdbcTemplate.query(searchMessageSql, new RowMapper<Message>() {
            @Override
            public Message mapRow(ResultSet rs, int rowNum) throws SQLException {

                Message message = new Message();
                return message;
            }
        });

        // TODO: need a better code to send message.
        // PushNotificationService service = new PushNotificationService();
        // service.send("leaf.prod.app", "...", "Text");

        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
