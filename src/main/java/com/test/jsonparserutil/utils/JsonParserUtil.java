package com.test.jsonparserutil.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public final class JsonParserUtil {

   private JsonParserUtil(){}

   public static <T> T fromJson(String json, Class<T> classToReturn) {
      try {
         return getObjectMapper().readValue(json, classToReturn);
      }
      catch (JsonProcessingException e) {
         log.error("JsonProcessingException during converting jsonStr to Object and errorMessage: {} ", e.getMessage());
         throw new RuntimeException(e);
      }
   }

   public static <T> List<T> fromJsonArray(String json, Class<T> classToReturn) {
      try {
         CollectionType collectionType = getObjectMapper().getTypeFactory().constructCollectionType(List.class, classToReturn);
         return getObjectMapper().readValue(json, collectionType);
      }
      catch (JsonProcessingException e) {
         log.error("JsonProcessingException during converting jsonStr to Object and errorMessage: {} ", e.getMessage());
         throw new RuntimeException(e);
      }
   }

   public static String toJson(Object obj) {
      try {
         return getObjectMapper().writeValueAsString(obj);
      }
      catch (JsonProcessingException ex) {
         log.error("JsonProcessingException during converting Object to JsonStr and errorMessage: {} ", ex.getMessage());
         throw new RuntimeException(ex);
      }
   }

   public static <T> T getObjectByJsonFile(String filePath, Class<T> classOfT) {
      return fromJson(readFileAsString(filePath), classOfT);
   }

   public static <T> List<T>  getObjectListByJsonFile(String filePath, Class<T> classOfT) {
      return fromJsonArray(readFileAsString(filePath), classOfT);
   }

   /**
    * Read json data from filePath and convert it as String
    *
    * @param filePath
    * @return
    * @throws IOException
    */
   public static String readFileAsString(final String filePath) {
      InputStream inputStream = null;
      try {
         Resource resource = new ClassPathResource(filePath);
         inputStream = resource.getInputStream();
         return new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
      } catch (IOException e) {
         log.error("Error found during reading a file from resources folder and errorMessage: {}", e.getMessage());
         throw new RuntimeException(String.format("Not able to read file %s ", filePath), e);
      } finally {
         if(inputStream != null) {
            try {
               inputStream.close();
            } catch (IOException e) {
               log.error("Error found during inputStream connection close and ErrorMessage: {}", e.getMessage());
               throw new RuntimeException(e);
            }
         }
      }
   }

   private static ObjectMapper getObjectMapper() {
      ObjectMapper aMapper = new ObjectMapper();
      aMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
      aMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      aMapper.enable(SerializationFeature.INDENT_OUTPUT);
      aMapper.registerModule(new JavaTimeModule());
      return aMapper;
   }

}