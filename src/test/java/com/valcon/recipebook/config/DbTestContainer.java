package com.valcon.recipebook.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class DbTestContainer extends PostgreSQLContainer<DbTestContainer> {

    private static final String IMAGE_VERSION = "postgres:10-alpine";

    private static DbTestContainer container;

    private DbTestContainer() {
        super(IMAGE_VERSION);
    }

    public static DbTestContainer getInstance() {
        if (container == null) {
            container = new DbTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        // let JVM clean up the container
    }
}
