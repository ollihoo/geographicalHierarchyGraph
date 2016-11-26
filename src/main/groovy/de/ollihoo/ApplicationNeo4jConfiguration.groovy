package de.ollihoo

import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@ComponentScan("de.ollihoo.domain")
@EnableNeo4jRepositories(basePackages = "de.ollihoo.repository")
class ApplicationNeo4jConfiguration extends Neo4jConfiguration {

    @Value('${neo4j.host}')
    private String neo4jHost

    @Value('${neo4j.user}')
    private String neo4jUser

    @Value('${neo4j.password}')
    private String neo4jPassword

    @Override
    SessionFactory getSessionFactory() {

        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration();
        configuration.driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
                .setURI("bolt://${neo4jUser}:${neo4jPassword}@${neo4jHost}")
                .setEncryptionLevel("NONE")
        new SessionFactory(configuration, "org.ollihoo.domain")
    }
}
