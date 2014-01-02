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
package org.smartparam.manager.json.vendor.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.manager.audit.diff.ParameterDiff;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterDiffSerializer implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> type) {
        if (ParameterDiff.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter) new TypeAdapter<ParameterDiff>() {

                @Override
                public void write(JsonWriter out, ParameterDiff value) throws IOException {
                    out.beginObject()
                            .name("previous").value(gson.toJson(value.previous()))
                            .name("current").value(gson.toJson(value.current()))
                            .endObject();
                }

                @Override
                public ParameterDiff read(JsonReader in) throws IOException {
                    in.beginObject();
                    in.nextName();
                    SimpleParameter previous = gson.fromJson(in, SimpleParameter.class);
                    in.nextName();
                    SimpleParameter current = gson.fromJson(in, SimpleParameter.class);
                    in.endObject();

                    return new ParameterDiff(previous, current);
                }
            };
        }
        return null;
    }

}
