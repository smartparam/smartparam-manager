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
import org.polyjdbc.core.util.TheCloser;

/**
 *
 * @author Adam Dubiel
 */
public class SchemaCreator {

    private final JdbcEventLogConfig config;

    private final SchemaManagerFactory schemaManagerFactory;

    public SchemaCreator(JdbcEventLogConfig config, SchemaManagerFactory schemaManagerFactory) {
        this.config = config;
        this.schemaManagerFactory = schemaManagerFactory;
    }

    public void createSchema() {
        SchemaManager schemaManager = null;
        SchemaInspector schemaInspector = null;
        try {
            schemaManager = schemaManagerFactory.createManager();
            schemaInspector = schemaManagerFactory.createInspector();

            Schema schema = new Schema(config.dialect());
            String entityName = config.entityName();
            if (!schemaInspector.relationExists(entityName)) {
                schema.addRelation(entityName)
                        .withAttribute().longAttr("id").withAdditionalModifiers("AUTO_INCREMENT").notNull().and()
                        .withAttribute().timestamp("timestamp").notNull().and()
                        .withAttribute().string("entry_type").withMaxLength(50).notNull().and()
                        .withAttribute().string("event_type").withMaxLength(50).notNull().and()
                        .withAttribute().string("responsible").withMaxLength(300).notNull().and()
                        .withAttribute().string("repository").withMaxLength(300).notNull().and()
                        .withAttribute().string("parameter_key").withMaxLength(300).notNull().and()
                        .withAttribute().string("parameter_entry_key").withMaxLength(300).and()
                        .withAttribute().text("event_details").notNull().and()
                        .primaryKey(config.primaryKeyPrefix() + entityName).using("id").and()
                        .build();
                schema.addIndex(config.indexPrefix() + entityName + "_timestamp").indexing("timestamp").on(entityName).build();
                schema.addIndex(config.indexPrefix() + entityName + "_entry_type").indexing("entry_type").on(entityName).build();
                schema.addIndex(config.indexPrefix() + entityName + "_event_type").indexing("event_type").on(entityName).build();
                schema.addIndex(config.indexPrefix() + entityName + "_responsible").indexing("responsible").on(entityName).build();
                schema.addIndex(config.indexPrefix() + entityName + "_parameter_key").indexing("parameter_key").on(entityName).build();
                schema.addIndex(config.indexPrefix() + entityName + "_parameter_entry_key").indexing("parameter_entry_key").on(entityName).build();
                schema.addSequence(config.sequenceName()).build();
            }

            schemaManager.create(schema);
        } finally {
            TheCloser.close(schemaManager, schemaInspector);
        }
    }

    public void dropSchema() {
        SchemaManager schemaManager = null;
        try {
            schemaManager = schemaManagerFactory.createManager();

            Schema schema = new Schema(config.dialect());
            schema.addRelation(config.entityName()).build();
            schema.addSequence(config.sequenceName()).build();

            schemaManager.drop(schema);
        } finally {
            TheCloser.close(schemaManager);
        }
    }
}
