/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.manager.json.vendor.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.smartparam.manager.adapter.JsonAdapter;
import org.smartparam.manager.json.JsonAdapterException;

/**
 *
 * @author Adam Dubiel
 */
public class JacksonJsonAdapter implements JsonAdapter {

    private final ObjectMapper objectMapper;

    public JacksonJsonAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonJsonAdapter() {
        this.objectMapper = ParamEngineJacksonEnhancer.createEnhanced();
    }

    @Override
    public String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new JsonAdapterException("Failed to serialize object using Jackson.", exception);
        }
    }

    @Override
    public <T> T deserialize(String json, Class<T> objectClass) {
        try {
            return objectMapper.readValue(json, objectClass);
        } catch (IOException exception) {
            throw new JsonAdapterException("Failed to deserialize object using Jackson.", exception);
        }
    }

}
