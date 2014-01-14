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
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class SchemaCreatorIntegrationTest extends DatabaseTest {

    @Test
    public void shouldCreateSchemaMadeOfEntitySequenceAndIndexes() {
        // given
        JdbcEventLogConfig eventLogConfig = JdbcEventLogConfigBuilder.jdbcEventLogConfig(dialect())
                .withEntityPrefix("sp_")
                .withSequencePrefix("seq_")
                .withIndexPrefix("idx_")
                .withSufix("test_audit")
                .build();
        SchemaCreator schemaCreator = new SchemaCreator(eventLogConfig, schemaManagerFactory());

        // when
        schemaCreator.createSchema();

        // then
        SchemaInspector inspector = schemaManagerFactory().createInspector();
        assertThat(inspector.relationExists("sp_test_audit")).isTrue();
    }

    @Test
    public void shouldDropCreatedSchema() {
        // given
        JdbcEventLogConfig eventLogConfig = JdbcEventLogConfigBuilder.jdbcEventLogConfig(dialect())
                .withEntityPrefix("sp_")
                .withSequencePrefix("seq_")
                .withIndexPrefix("idx_")
                .withSufix("test_audit")
                .build();
        SchemaCreator schemaCreator = new SchemaCreator(eventLogConfig, schemaManagerFactory());
        schemaCreator.createSchema();

        // when
        schemaCreator.dropSchema();

        // then
        SchemaInspector inspector = schemaManagerFactory().createInspector();
        assertThat(inspector.relationExists("sp_test_audit")).isFalse();
    }
}
