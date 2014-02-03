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
package org.smartparam.manager.json;

import com.jayway.jsonassert.JsonAssert;
import org.smartparam.engine.core.index.Star;
import org.smartparam.engine.matchers.type.Range;
import org.smartparam.manager.json.integration.SerializerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel
 */
public class RangeSerializerTest extends SerializerTest {

    @Test
    public void shouldSerializeRangeWithBothFiniteEndsWithSimpleClassNameAsType() {
        // given
        Range<Integer> range = new Range<Integer>(5, 10);

        // when
        String json = serialize(range);

        // then
        JsonAssert.with(json)
                .assertEquals("$.type", "Integer")
                .assertEquals("$.from", 5)
                .assertEquals("$.to", 10);
    }

    @Test
    public void shouldSerializeRangeWithInfiniteFromDrawingTypeFromToField() {
        // given
        Range<Integer> range = new Range<Integer>(Star.star(), 10);

        // when
        String json = serialize(range);

        // then
        JsonAssert.with(json)
                .assertEquals("$.type", "Integer")
                .assertEquals("$.from", null)
                .assertEquals("$.to", 10);
    }

    @Test
    public void shouldSerializeRangeWithInfiniteToDrawingTypeFromFromField() {
        // given
        Range<Integer> range = new Range<Integer>(5, Star.star());

        // when
        String json = serialize(range);

        // then
        JsonAssert.with(json)
                .assertEquals("$.type", "Integer")
                .assertEquals("$.from", 5)
                .assertEquals("$.to", null);
    }

    @Test
    public void shouldPutInfinityAsRangeTypeWhenNoneEndIsFinite() {
        // given
        Range<Integer> range = new Range<Integer>(Star.star(), Star.star());

        // when
        String json = serialize(range);

        // then
        JsonAssert.with(json)
                .assertEquals("$.type", "Infinity")
                .assertEquals("$.from", null)
                .assertEquals("$.to", null);
    }
}
