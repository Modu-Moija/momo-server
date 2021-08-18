package com.momo.server.config;

import static java.util.Collections.singletonList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//	return new MongoTransactionManager(dbFactory);
//    }

    @Value("${dbname}")
    private String dbName;

    @Value("${dbusername}")
    private String dbUsername;

    @Value("${dbpassword}")
    private String dbPassword;

    @Value("${dbhost}")
    private String dbHost;

    @Value("${dbport}")
    private int dbPort;

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {

        builder.credential(
                MongoCredential.createCredential(dbUsername, dbName, dbPassword.toCharArray()))
            .applyToClusterSettings(settings -> {
                settings.hosts(singletonList(new ServerAddress(dbHost, dbPort)));
            });

    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }
}