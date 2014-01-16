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
package org.smartparam.manager.json.vendor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.smartparam.engine.core.parameter.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryKeySerializer extends StdSerializer<ParameterEntryKey> {

    public ParameterEntryKeySerializer() {
        super(ParameterEntryKey.class);
    }

    @Override
    public void serialize(ParameterEntryKey key, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(key.value());
    }

}
