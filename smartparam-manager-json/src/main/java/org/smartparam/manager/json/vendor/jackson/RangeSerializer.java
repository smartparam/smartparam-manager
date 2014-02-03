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
package org.smartparam.manager.json.vendor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.smartparam.engine.matchers.type.Range;

/**
 *
 * @author Adam Dubiel
 */
public class RangeSerializer extends StdSerializer<Range> {

    public RangeSerializer() {
        super(Range.class);
    }

    @Override
    public void serialize(Range value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", evaluateType(value));
        jgen.writeObjectField("from", value.from());
        jgen.writeObjectField("to", value.to());
        jgen.writeEndObject();
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
