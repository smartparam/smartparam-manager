/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.manager.json.vendor.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.smartparam.engine.matchers.type.Range;

/**
 *
 * @author Adam Dubiel
 */
public class RangeSerializer implements JsonSerializer<Range<?>> {

    @Override
    public JsonElement serialize(Range<?> range, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("type", evaluateType(range));
        object.add("from", context.serialize(range.from()));
        object.add("to", context.serialize(range.to()));
        return object;
    }

    private String evaluateType(Range<?> range) {
        if (range.isFromInfinity() && range.isToInfinity()) {
            return "Infinity";
        }
        if (!range.isFromInfinity()) {
            return range.from().getClass().getSimpleName();
        }
        if (!range.isToInfinity()) {
            return range.to().getClass().getSimpleName();
        }
        throw new IllegalArgumentException("How can it be that range " + range + " is not infinite yet none of it ends is finite? ");
    }
}
