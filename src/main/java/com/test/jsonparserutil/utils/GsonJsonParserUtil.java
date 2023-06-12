package com.test.jsonparserutil.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public final class GsonJsonParserUtil {

    private GsonJsonParserUtil(){}

    /**
     * Convert JsonString to Json Object of Type T by Passing JsonStr and ClassOfT
     *
     * @param <T>
     * @param jsonStr
     * @return
     */
    public static <T> T getObjectByJsonStr(String jsonStr, Class<T> classOfT){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, classOfT);
    }

    public static <T> List<T> getList(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(jsonArray, typeOfT);
    }

    /**
     * Convert Object to Json String
     *
     * @param src
     * @return
     */
    public static String toJsonString(Object src){
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(src);
    }

    /**
     * Creation of Object(Type T) based on Json file and class name
     *
     * @param <T>
     * @param filePath
     * @param classOfT
     */
    public static <T> T getObjectByJsonFile(String filePath, Class<T> classOfT) {
        return getObjectByJsonStr(readFileAsString(filePath), classOfT);
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
}
