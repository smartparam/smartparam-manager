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
public class JdbcEventLogConfig {

    private final Dialect dialect;

    private String entityPrefix = "sp_";

    private String sequencePrefix = "seq_";

    private String indexPrefix = "idx_";

    private String primaryKeyPrefix = "pk_";

    private String foreignKeyPrefix = "fk_";

    private String sufix = "audit";

    public JdbcEventLogConfig(Dialect dialect) {
        this.dialect = dialect;
    }

    public Dialect dialect() {
        return dialect;
    }

    public String entityName() {
        return entityPrefix + sufix;
    }

    public String sequenceName() {
        return sequencePrefix + sufix;
    }

    void sufix(String sufix) {
        this.sufix = sufix;
    }

    void entityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }

    void sequencePrefix(String sequencePrefix) {
        this.sequencePrefix = sequencePrefix;
    }

    public String indexPrefix() {
        return indexPrefix;
    }

    void indexPrefix(String indexPrefix) {
        this.indexPrefix = indexPrefix;
    }

    public String primaryKeyPrefix() {
        return primaryKeyPrefix;
    }

    void primaryKeyPrefix(String primaryKeyPrefix) {
        this.primaryKeyPrefix = primaryKeyPrefix;
    }

    public String foreignKeyPrefix() {
        return foreignKeyPrefix;
    }

    void foreignKeyPrefix(String foreignKeyPrefix) {
        this.foreignKeyPrefix = foreignKeyPrefix;
    }
}
