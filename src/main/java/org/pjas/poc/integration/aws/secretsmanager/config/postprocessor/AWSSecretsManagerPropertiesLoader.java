package org.pjas.poc.integration.aws.secretsmanager.config.postprocessor;

/*
 * Created by: patoche
 * on 25 junio 2018 - 08:25 AM
 */

import java.io.IOException;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;

public class AWSSecretsManagerPropertiesLoader implements PropertySourceLoadable {

    private String accessKeyId;
    private String secretKey;
    private String sourceName;
    private String endpoint;
    private String region;

    private String secretsManagerName;

    private AWSSecretsManager client;
    private GetSecretValueRequest secretValueRequest;
    private GetSecretValueResult secretValueResult;


    @Builder
    public AWSSecretsManagerPropertiesLoader(String accessKeyId, String secretKey, String sourceName, String endpoint, String region, String secretsManagerName) {
        this.accessKeyId = accessKeyId;
        this.secretKey = secretKey;
        this.sourceName = sourceName;
        this.endpoint = endpoint;
        this.region = region;
        this.secretsManagerName = secretsManagerName;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public Map<String, Object> load() throws Exception {
        createAWSSecretsManager();
        retrieveSecretKeys();
        if (isBadResult()) {
            return null;
        } else {
            return readResult();
        }
    }

    private void createAWSSecretsManager() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setCredentials(getCredentialsProvider());
        clientBuilder.setEndpointConfiguration(endpointConfiguration);
        client = clientBuilder.build();
    }

    private AWSCredentialsProvider getCredentialsProvider() {
        return new AWSCredentialsProvider() {
            public AWSCredentials getCredentials() {
                return AWSSecretsManagerPropertiesLoader.this.getCredentials();
            }

            public void refresh() {

            }
        };
    }

    private AWSCredentials getCredentials() {
        return new AWSCredentials() {
            public String getAWSAccessKeyId() {
                return accessKeyId;
            }

            public String getAWSSecretKey() {
                return secretKey;
            }
        };
    }

    private void retrieveSecretKeys() {
        secretValueRequest = new GetSecretValueRequest().withSecretId(secretsManagerName);
        secretValueResult = client.getSecretValue(secretValueRequest);
    }

    private boolean isBadResult() {
        return secretValueResult == null || secretValueResult.getSecretString() == null;
    }

    private Map<String, Object> readResult() throws IOException {
        String secret = secretValueResult.getSecretString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(secret, new TypeReference<Map<String, Object>>() {
        });
        return map;
    }
}
