package com.example.techmentor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
// Estas propiedades tienen prioridad sobre la configuración y las variables del entorno local.
// El esquema de las entidades se comprueba en memoria sin tocar PostgreSQL ni sus datos.
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:techmentor_context_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.hbm2ddl.halt_on_error=true",
        "spring.sql.init.mode=never",
        "jwt.secret=dGVjaG1lbnRvci10ZXN0LWtleS1vbmx5LW5vdC1wcm9kdWN0aW9uLTIwMjY="
})
class TechMentorApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertEquals("H2", connection.getMetaData().getDatabaseProductName());
            assertTrue(connection.isValid(2));
        }
    }

}
