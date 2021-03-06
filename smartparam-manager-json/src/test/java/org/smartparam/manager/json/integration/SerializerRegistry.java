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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adam Dubiel
 */
public class SerializerRegistry {

    private final Map<String, Serializer> serializers = new HashMap<String, Serializer>();

    public SerializerRegistry() {
        register("jackson", JacksonSerializerFactory.serializer());
        register("gson", GsonSerializerFactory.serializer());
        register("jason", JasonSerializerFactory.serializer());
    }

    public final void register(String name, Serializer serializer) {
        this.serializers.put(name, serializer);
    }

    public Serializer get(String name) {
        return serializers.get(name);
    }
}
