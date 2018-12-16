package org.loopring.mobi.spring;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        prepareAPNSCertificates();
        SpringApplication.run(Application.class, args);
    }

    private static void prepareAPNSCertificates() {
        String[] fileNames = {"leaf.prod.app.p12", "leaf.prod.app.development.p12"};
        for (String fileName : fileNames) {
            File dest = new File(String.format("./%s", fileName));
            ClassPathResource cpr = new ClassPathResource(String.format("static/aps_certificates/%s", fileName));
            try {
                byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
                FileCopyUtils.copy(bdata, dest);
                System.out.println("Found APNS Certificates");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
