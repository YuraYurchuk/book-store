package com.book.store.custom;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {

    private static final String MYSQL_VERSION = "mysql:8.0";

    private static CustomMySqlContainer container;

    private CustomMySqlContainer() {
        super(MYSQL_VERSION);
        this.withReuse(false);
    }

    public static CustomMySqlContainer getInstance() {
        if (container == null) {
            container = new CustomMySqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
