package org.pjas.poc.integration.aws.secretsmanager.config.postprocessor;

/*
 * Created by: patoche
 * on 25 junio 2018 - 08:34 AM
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

public class ApplicationEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private List<PropertySourceLoadable> loaders;

    public ApplicationEnvironmentPostProcessor() {
        loaders = new ArrayList<>(Collections.emptyList());
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication application) {
        loadPropertySourceLoaders(configurableEnvironment);
        for (PropertySourceLoadable loader : loaders) {
            MapPropertySource mapPropertySource = generateMapPropertySource(loader);
            addMapPropertySource(configurableEnvironment, mapPropertySource);
        }
    }

    private void loadPropertySourceLoaders(ConfigurableEnvironment configurableEnvironment) {
        PropertySourceLoadable aws = createWalleAWSSecretsManager(configurableEnvironment);
        loaders.clear();
        loaders.add(aws);
    }

    private PropertySourceLoadable createWalleAWSSecretsManager(ConfigurableEnvironment configurableEnvironment) {
        String accessKey = "cloud.aws.credentials.accessKey";
        String secretKey = "cloud.aws.credentials.secretKey";
        String endpointKey = "cloud.aws.endpoint";
        String regionKey = "cloud.aws.region.static";
        String secretsManagerNameKey = "cloud.aws.secretsmanager.name";
        String sourceName = "AWS_SECRETS_MANAGER";

        return AWSSecretsManagerPropertiesLoader.builder()
                .accessKeyId(configurableEnvironment.getProperty(accessKey))
                .secretKey(configurableEnvironment.getProperty(secretKey))
                .endpoint(configurableEnvironment.getProperty(endpointKey))
                .region(configurableEnvironment.getProperty(regionKey))
                .secretsManagerName(configurableEnvironment.getProperty(secretsManagerNameKey))
                .sourceName(sourceName)
                .build();
    }

    private MapPropertySource generateMapPropertySource(PropertySourceLoadable loader) {
        Map<String, Object> properties;
        try {
            properties = loader.load();
            print(String.format("Properties from source %s have been read", loader.getSourceName()));
        } catch (Exception e) {
            properties = new HashMap<>(Collections.emptyMap());
            print(e.getMessage());
        }
        String sourceName = loader.getSourceName();
        MapPropertySource propertySource = new MapPropertySource(sourceName, properties);
        return propertySource;
    }

    private void addMapPropertySource(ConfigurableEnvironment configurableEnvironment, MapPropertySource propertySource) {
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        propertySources.addFirst(propertySource);
    }

    private void print(String text) {
        // CHECKSTYLE:OFF
        System.out.println(text);
        // CHECKSTYLE:ON
    }
}
