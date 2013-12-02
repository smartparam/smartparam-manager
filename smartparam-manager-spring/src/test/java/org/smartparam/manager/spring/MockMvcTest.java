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
package org.smartparam.manager.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.smartparam.manager.spring.vendor.jackson.DescribedCollectionSerializer;
import org.smartparam.manager.spring.vendor.jackson.LevelKeySerializer;
import org.smartparam.manager.spring.vendor.jackson.ParameterEntryKeySerializer;
import org.smartparam.manager.spring.vendor.jackson.RepositoryNameSerializer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Adam Dubiel
 */
public class MockMvcTest {

    protected MockMvc mockMvc(Object... controllers) {
        return MockMvcBuilders.standaloneSetup(controllers).setMessageConverters(jacksonMessageConverter()).build();
    }

    private HttpMessageConverter<?> jacksonMessageConverter() {
        ObjectMapper jackson = new ObjectMapper();
        SimpleModule serializationModule = new SimpleModule();
        serializationModule.addSerializer(new RepositoryNameSerializer());
        serializationModule.addSerializer(new LevelKeySerializer());
        serializationModule.addSerializer(new ParameterEntryKeySerializer());
        serializationModule.addSerializer(new DescribedCollectionSerializer());
        jackson.registerModule(serializationModule);

        MappingJackson2HttpMessageConverter jacksonConveter = new MappingJackson2HttpMessageConverter();
        jacksonConveter.setObjectMapper(jackson);

        return jacksonConveter;
    }

}
