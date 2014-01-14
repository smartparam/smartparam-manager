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
package org.smartparam.manager.audit.jdbc;

import org.polyjdbc.core.schema.SchemaInspector;
import org.polyjdbc.core.schema.SchemaManager;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.schema.model.Schema;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Adam Dubiel
 */
public class SchemaCreatorTest {

    @Test
    public void shouldNotCreateSchemaIfItAlreadyExists() {
        // given
        SchemaInspector schemaInspector = mock(SchemaInspector.class);
        SchemaManager schemaManager = mock(SchemaManager.class);
        SchemaManagerFactory factory = mock(SchemaManagerFactory.class);
        when(factory.createInspector()).thenReturn(schemaInspector);
        when(factory.createManager()).thenReturn(schemaManager);

        JdbcEventLogConfig eventLogConfig = JdbcEventLogConfigBuilder.jdbcEventLogConfig("H2")
                .withEntityPrefix("sp_")
                .withSequencePrefix("seq_")
                .withIndexPrefix("idx_")
                .withSufix("test_audit")
                .build();
        SchemaCreator creator = new SchemaCreator(eventLogConfig, factory);

        when(schemaInspector.relationExists("sp_test_audit")).thenReturn(true);

        // when
        creator.createSchema();

        // then
        verify(schemaManager, never()).create(any(Schema.class));
    }

}
