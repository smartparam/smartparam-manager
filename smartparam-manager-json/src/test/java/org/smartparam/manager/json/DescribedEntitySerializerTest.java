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
package org.smartparam.manager.json;

import org.smartparam.manager.json.integration.SerializerTest;
import com.jayway.jsonassert.JsonAssert;
import org.smartparam.editor.identity.DescribedEntity;
import org.smartparam.editor.identity.RepositoryName;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel
 */
public class DescribedEntitySerializerTest extends SerializerTest {

    @Test
    public void shouldSerializeAsSourceNameAndDataValue() {
        // given
        DescribedEntity<String> collection = new DescribedEntity<String>(new RepositoryName("repository"), "1");

        // when
        String json = serialize(collection);

        // then
        JsonAssert.with(json)
                .assertEquals("$.source", "repository")
                .assertEquals("$.data", "1");
    }

}
