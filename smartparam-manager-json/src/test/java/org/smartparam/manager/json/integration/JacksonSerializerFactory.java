/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.manager.json.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartparam.manager.json.vendor.jackson.ParamEngineJacksonEnhancer;

/**
 *
 * @author Adam Dubiel
 */
public class JacksonSerializerFactory {

    public static Serializer serializer() {
        return new JacksonSerializerFactory().jackson();
    }

    private Serializer jackson() {
        final ObjectMapper jackson = ParamEngineJacksonEnhancer.createEnhanced();

        return new Serializer() {
            @Override
            public String serialize(Object object) {
                try {
                    return jackson.writeValueAsString(object);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

}
