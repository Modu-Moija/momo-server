package com.momo.server.Config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import static java.util.Collections.singletonList;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${dbname}")
    private String dbName;

    @Value("${dbusername}")
    private String dbUsername;

    @Value("${dbpassword}")
    private String dbPassword;

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {

        Environment environment;

        builder
                .credential(MongoCredential.createCredential(dbUsername, dbName, dbPassword.toCharArray()))
                .applyToClusterSettings(settings  -> {
                    settings.hosts(singletonList(new ServerAddress("visualup.koreacentral.cloudapp.azure.com", 5599)));
                });
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }
}
