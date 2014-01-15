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
package org.smartparam.manager.json.vendor.jason;

import org.jasonjson.core.JsonElement;
import org.jasonjson.core.JsonObject;
import org.jasonjson.core.JsonSerializationContext;
import org.jasonjson.core.JsonSerializer;
import java.lang.reflect.Type;
import org.smartparam.editor.core.identity.DescribedEntity;

/**
 *
 * @author Adam Dubiel
 */
public class DescribedEntitySerializer implements JsonSerializer<DescribedEntity> {

    @Override
    public JsonElement serialize(DescribedEntity entity, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("source", context.serialize(entity.source()));
        object.add("data", context.serialize(entity.data()));
        return object;
    }

}
