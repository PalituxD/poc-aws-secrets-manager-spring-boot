package org.pjas.poc.integration.aws.secretsmanager.config.postprocessor;

/*
 * Created by: patoche
 * on 25 junio 2018 - 08:23 AM
 */

import java.util.Map;

public interface PropertySourceLoadable {
    String getSourceName();

    Map<String, Object> load() throws Exception;
}
