package net.thrymr.config;

import net.thrymr.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;

@Configuration
public class FunctionalEndPointConfig {

    @Autowired
    private FileService service;

//    @Bean
//    public RouterFunction<ServerResponse> router(){
//        return RouterFunctions.route()
//                .GET("fun-ep/video/{title}", this::videoHandler)
//                .build();
//    }

    private Mono<ServerResponse> videoHandler(ServerRequest serverRequest) throws IOException {
        String title = serverRequest.pathVariable("title");
        return ServerResponse.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(this.service.getVideo(title), Resource.class);
    }

}