package org.pjas.poc.integration.aws.secretsmanager;

/*
 * Created by: patoche
 * on 25 junio 2018 - 08:19 AM
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pjas.poc.integration.aws.secretsmanager.config.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner{

    private static Logger LOG = LogManager.getLogger(Application.class);

    @Autowired
    private DummyService service;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("launching...");
    }
}
