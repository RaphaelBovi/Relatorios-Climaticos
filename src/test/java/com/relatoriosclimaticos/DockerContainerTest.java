package com.relatoriosclimaticos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
        "spring.datasource.url=jdbc:postgresql://localhost:2222/postgres",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgres"
})
public class DockerContainerTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.2-alpine")
            .withExposedPorts(5432);

    @Test
    public void testDatabaseConnection() throws SQLException {
        postgreSQLContainer.start();

        String jdbcUrl = "jdbc:postgresql://localhost:" + postgreSQLContainer.getMappedPort(5432) + "/postgres";
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Verificar se a conexão com o banco de dados foi estabelecida com sucesso
            assert connection != null && !connection.isClosed();
        }

        postgreSQLContainer.stop();
    }
}




