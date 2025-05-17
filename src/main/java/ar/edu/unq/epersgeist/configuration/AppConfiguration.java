package ar.edu.unq.epersgeist.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@Configuration
@EnableJpaAuditing
@EnableNeo4jRepositories(basePackages = "ar.edu.unq.epersgeist.persistencia.dao.neo4j")
@EnableJpaRepositories(basePackages = "ar.edu.unq.epersgeist.persistencia.dao.jpa")
public class AppConfiguration { }