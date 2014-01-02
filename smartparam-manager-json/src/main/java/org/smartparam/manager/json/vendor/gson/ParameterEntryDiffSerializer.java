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
import org.smartparam.editor.model.simple.SimpleParameterEntry;
import org.smartparam.manager.audit.diff.ParameterEntryDiff;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryDiffSerializer implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> type) {
        if (ParameterEntryDiff.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter) new TypeAdapter<ParameterEntryDiff>() {

                @Override
                public void write(JsonWriter out, ParameterEntryDiff value) throws IOException {
                    out.beginObject()
                            .name("previous").value(gson.toJson(value.previous()))
                            .name("current").value(gson.toJson(value.current()))
                            .endObject();
                }

                @Override
                public ParameterEntryDiff read(JsonReader in) throws IOException {
                    in.beginObject();
                    in.nextName();
                    SimpleParameterEntry previous = gson.fromJson(in, SimpleParameterEntry.class);
                    in.nextName();
                    SimpleParameterEntry current = gson.fromJson(in, SimpleParameterEntry.class);
                    in.endObject();

                    return new ParameterEntryDiff(previous, current);
                }
            };
        }
        return null;
    }

}
