package com.training.socialnetwork.utils;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSonHelper {

	public static Optional<String> toJson(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            objectMapper.registerModule(simpleModule);
            String jsonInString = objectMapper.writeValueAsString(obj);
            return Optional.of(jsonInString);
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
