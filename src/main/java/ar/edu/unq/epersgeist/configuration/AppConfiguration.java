package ar.edu.unq.epersgeist.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableJpaAuditing
@EnableMongoAuditing
public class AppConfiguration { }