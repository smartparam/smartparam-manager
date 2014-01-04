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

import org.polyjdbc.core.dialect.Dialect;

/**
 *
 * @author Adam Dubiel
 */
public final class JdbcEventLogConfigBuilder {

    private final JdbcEventLogConfig config;

    private JdbcEventLogConfigBuilder(Dialect dialect) {
        this.config = new JdbcEventLogConfig(dialect);
    }

    public static JdbcEventLogConfigBuilder jdbcEventLogConfig(Dialect dialect) {
        return new JdbcEventLogConfigBuilder(dialect);
    }

    public JdbcEventLogConfig build() {
        return config;
    }

    public JdbcEventLogConfigBuilder withSufix(String sufix) {
        config.sufix(sufix);
        return this;
    }

    public JdbcEventLogConfigBuilder withEntityPrefix(String entityPrefix) {
        config.entityPrefix(entityPrefix);
        return this;
    }

    public JdbcEventLogConfigBuilder withSequencePrefix(String sequencePrefix) {
        config.sequencePrefix(sequencePrefix);
        return this;
    }

    public JdbcEventLogConfigBuilder withIndexPrefix(String indexPrefix) {
        config.indexPrefix(indexPrefix);
        return this;
    }

    public JdbcEventLogConfigBuilder withPrimaryKeyPrefix(String primaryKeyPrefix) {
        config.primaryKeyPrefix(primaryKeyPrefix);
        return this;
    }

    public JdbcEventLogConfigBuilder withForeignKeyPrefix(String foreignKeyPrefix) {
        config.foreignKeyPrefix(foreignKeyPrefix);
        return this;
    }
}
