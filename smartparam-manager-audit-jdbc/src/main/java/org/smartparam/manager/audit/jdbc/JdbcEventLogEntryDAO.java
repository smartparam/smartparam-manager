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

import java.util.List;
import org.polyjdbc.core.query.InsertQuery;
import org.polyjdbc.core.query.QueryFactory;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.SelectQuery;
import org.polyjdbc.core.type.Timestamp;
import org.smartparam.manager.audit.EventLogEntry;
import org.smartparam.manager.audit.EventLogEntryType;
import org.smartparam.manager.audit.EventLogFilters;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcEventLogEntryDAO {

    private final JdbcEventLogConfig config;

    public JdbcEventLogEntryDAO(JdbcEventLogConfig config) {
        this.config = config;
    }

    public void save(QueryRunner queryRunner, EventLogEntry logEntry) {
        InsertQuery insert = QueryFactory.insert().into(config.entityName())
                .sequence("id", config.sequenceName())
                .value("timestamp", Timestamp.from(logEntry.timestamp()))
                .value("entry_type", logEntry.type().typeCode())
                .value("event_type", logEntry.action().name())
                .value("responsible", logEntry.responsibleLogin())
                .value("repository", logEntry.repository().value())
                .value("parameter_key", logEntry.parameterKey().value())
                .value("event_details", logEntry.serializedEventDetails());

        if (logEntry.type() == EventLogEntryType.ENTRY) {
            insert.value("parameter_entry_key", logEntry.entryKey().value());
        }

        queryRunner.insert(insert);
    }

    public List<EventLogEntry> list(QueryRunner queryRunner, EventLogFilters filters) {
        SelectQuery select = QueryFactory.selectAll().from(config.entityName());

        if (filters.parameterKey() != null) {
            select.where("parameter_key = :parameterKey")
                    .withArgument("parameterKey", filters.parameterKey().value());
        }
        if (filters.entryKey() != null) {
            select.where("parameter_entry_key = :parameterEntryKey")
                    .withArgument("parameterEntryKey", filters.entryKey().value());
        }

        return queryRunner.queryList(select, new EventLogEntryMapper());
    }

}
