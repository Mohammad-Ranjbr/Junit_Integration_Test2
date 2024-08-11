package com.example.spring_boot_testing.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

// Sometimes it might be useful to define a container that is only started once for several test classes.
// The singleton container is started only once when the base class is loaded.
// In this case, we manually perform the life cycle of the container

public abstract class AbstractionBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("ems")
                .withUsername("postgres")
                .withPassword("123456");
        POSTGRE_SQL_CONTAINER.start();
    }

    // The @DynamicPropertySource annotation is used in Spring Boot to dynamically set configuration values during test execution.
    // This feature is especially useful when you want to automatically and dynamically set configuration
    // values that depend on the environment or depend on external resources.
    // In your example, @DynamicPropertySource is used to set database values (such as URL, username and password) dynamically.
    // These values are taken from a PostgreSQL container started by Testcontainers.
    // @DynamicPropertySource: This annotation tells Spring Boot that the specified method (dynamicPropertySource) should be called during the test startup
    // process to set some configuration values dynamically.
    // DynamicPropertyRegistry: This class provides an interface that allows you to dynamically register Spring configuration values.
    // registry.add(...): These methods are used to add or overwrite configuration values in the DynamicPropertyRegistry.
    // For example, registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl); Sets the value of
    // spring.datasource.url to the dynamic database URL managed by Testcontainers.
    // Application: Communication with external sources during testing:
    // Let's say you've set up a PostgreSQL container for your tests with Testcontainers. Since the database URL, username,
    // and password may change each time the test is run (because the container may be started on a different port), you need to set these values dynamically.
    // Automatically set Spring configuration values:
    // Instead of manually and statically defining these values in application.properties or application.yml,
    // this code allows Spring Boot to automatically set these values at runtime.
    // Instead of storing configuration values statically in configuration files (such as application.properties), you can set them based on runtime conditions.
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password",POSTGRE_SQL_CONTAINER::getPassword);
    }

}
