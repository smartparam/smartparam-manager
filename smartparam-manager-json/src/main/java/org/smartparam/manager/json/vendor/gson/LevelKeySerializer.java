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
package org.smartparam.manager.json.vendor.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.smartparam.editor.model.LevelKey;
import org.smartparam.editor.model.simple.SimpleLevelKey;

/**
 *
 * @author Adam Dubiel
 */
public class LevelKeySerializer implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (LevelKey.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter) new TypeAdapter<LevelKey>() {

                @Override
                public void write(JsonWriter out, LevelKey value) throws IOException {
                    out.value(value.value());
                }

                @Override
                public LevelKey read(JsonReader in) throws IOException {
                    return new SimpleLevelKey(in.nextString());
                }
            };
        }
        return null;
    }

}
