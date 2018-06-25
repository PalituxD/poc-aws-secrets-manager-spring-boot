package org.pjas.poc.integration.aws.secretsmanager.config;

/*
 * Created by: patoche
 * on 25 junio 2018 - 09:06 AM
 */

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DummyService {

    private static Logger LOG = LogManager.getLogger(DummyService.class);

    @Value("${property.one}")
    private String propertyOne;
    @Value("${property.two}")
    private String propertyTwo;
    @Value("${property.three}")
    private String propertyThree;
    @Value("${property.four}")
    private String propertyFour;
    @Value("${property.five}")
    private String propertyFive;
    @Value("${property.six}")
    private String propertySix;

    @PostConstruct
    private void init(){
        LOG.info("propertyOne:"+propertyOne);
        LOG.info("propertyTwo:"+propertyTwo);
        LOG.info("propertyThree:"+propertyThree);
        LOG.info("propertyFour:"+propertyFour);
        LOG.info("propertyFive:"+propertyFive);
        LOG.info("propertySix:"+propertySix);
    }
}