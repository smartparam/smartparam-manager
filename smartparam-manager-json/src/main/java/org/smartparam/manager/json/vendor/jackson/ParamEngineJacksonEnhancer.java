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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamEngineJacksonEnhancer {

    private ParamEngineJacksonEnhancer() {
    }

    public static void enhance(ObjectMapper jackson) {
        SimpleModule serializationModule = new SimpleModule("paramEngineJson");
        serializationModule.addSerializer(new RepositoryNameSerializer());
        serializationModule.addSerializer(new LevelKeySerializer());
        serializationModule.addSerializer(new ParameterEntryKeySerializer());
        serializationModule.addSerializer(new DescribedCollectionSerializer());
        serializationModule.addSerializer(new DescribedEntitySerializer());
        serializationModule.addSerializer(new ParameterDiffSerializer());
        serializationModule.addSerializer(new ParameterEntryDiffSerializer());


        jackson.registerModule(serializationModule);
    }

    public static ObjectMapper createEnhanced() {
        ObjectMapper jackson = new ObjectMapper();
        enhance(jackson);
        return jackson;
    }

}
