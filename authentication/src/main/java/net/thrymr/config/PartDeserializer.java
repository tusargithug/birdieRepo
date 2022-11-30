package net.thrymr.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.Part;
import java.io.IOException;

public class PartDeserializer extends JsonDeserializer {

  @Override
  public Part deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

    return null;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Part.class, new PartDeserializer());
    objectMapper.registerModule(module);
    return objectMapper;
  }
}