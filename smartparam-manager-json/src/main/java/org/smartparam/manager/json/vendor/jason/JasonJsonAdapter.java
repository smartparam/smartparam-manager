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
package org.smartparam.manager.json.vendor.jason;

import org.jasonjson.core.Jason;
import org.jasonjson.core.JasonBuilder;
import org.smartparam.manager.adapter.JsonAdapter;

/**
 *
 * @author Adam Dubiel
 */
public class JasonJsonAdapter implements JsonAdapter {

    private final Jason jason;

    public JasonJsonAdapter(Jason jason) {
        this.jason = jason;
    }

    public JasonJsonAdapter(JasonBuilder jasonBuilder) {
        ParamEngineJasonEnhancer.enhance(jasonBuilder);
        this.jason = jasonBuilder.create();
    }

    public JasonJsonAdapter() {
        this.jason = ParamEngineJasonEnhancer.createEnhanced();
    }

    @Override
    public String serialize(Object object) {
        return jason.toJson(object);
    }

    @Override
    public <T> T deserialize(String json, Class<T> objectClass) {
        return jason.fromJson(json, objectClass);
    }

}
