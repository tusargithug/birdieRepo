package net.thrymr;

import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import net.thrymr.services.impl.MutationResolver;
import net.thrymr.services.impl.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment,
                basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
                                               String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.LocalTime);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigureUpload() {
        return wiringBuilder -> wiringBuilder.scalar(ApolloScalars.Upload);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigure() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurJson() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Json);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurObject() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Object);
    }
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurByte() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.GraphQLByte);
    }

    @Bean
    public GraphQLScalarType date() {
        return ExtendedScalars.LocalTime;
    }

    @Bean
    public GraphQLScalarType time() {
        return ExtendedScalars.Date;
    }

    @Bean
    public GraphQLScalarType objectType() {
        return ExtendedScalars.Object;
    }

    @Bean
    public GraphQLScalarType jsonType() {
        return ExtendedScalars.Json;
    }

    @Bean
    public GraphQLScalarType byteType() {
        return ExtendedScalars.GraphQLByte;
    }

    @Bean
    public GraphQLScalarType uploadScalarDefine() {
        return ApolloScalars.Upload;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("124MB"));
        factory.setMaxRequestSize(DataSize.parse("124MB"));
        return factory.createMultipartConfig();
    }

}