package dormease.dormeasedev.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // BaseEntity 사용 위함
public class JpaConfig {
}
