package com.teatro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@Profile({"default", "dev", "prod"})
@EnableJpaAuditing
public class JpaAuditingConfig {
} 