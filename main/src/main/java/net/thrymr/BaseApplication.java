package net.thrymr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@ComponentScan(basePackages = {"net.thrymr.*"})
@EntityScan(basePackages = {"net.thrymr.*"})
@EnableJpaRepositories(basePackages = {"net.thrymr.*"})
@PropertySource("message.properties")



@SpringBootApplication
public class BaseApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);

        LOGGER.info("***************************************************");
        LOGGER.info("	    BIRDIE BACKEND SERVER STARTED    ");
        LOGGER.info("***************************************************");
    }
    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*");
            }
        };
    }
}
