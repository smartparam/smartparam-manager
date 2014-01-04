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

import org.assertj.core.api.Assertions;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.editor.model.simple.SimpleParameterEntryKey;
import org.smartparam.editor.model.simple.SimpleParameterKey;
import org.smartparam.manager.audit.EventLogEntry;
import org.smartparam.manager.audit.EventLogEntryAssert;
import org.smartparam.manager.audit.EventLogFilters;
import org.smartparam.manager.test.SmartParamManagerBackedAssertions;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseAssert {

    private final QueryRunner queryRunner;

    private final JdbcEventLogEntryDAO eventLogEntryDAO;

    DatabaseAssert(QueryRunner queryRunner, JdbcEventLogEntryDAO eventLogEntryDAO) {
        this.queryRunner = queryRunner;
        this.eventLogEntryDAO = eventLogEntryDAO;
    }

    public void close() {
        queryRunner.close();
    }

    public DatabaseAssert hasEntryForParameter(String parameterKey) {
        return hasEntriesForParameter(parameterKey, 1);
    }

    public DatabaseAssert hasEntryForParameterEntry(String parameterEntryKey) {
        return hasEntriesForParameterEntry(parameterEntryKey, 1);
    }

    public DatabaseAssert hasEntriesForParameter(String parameterKey, int count) {
        EventLogFilters filters = new EventLogFilters(new SimpleParameterKey(parameterKey));
        Assertions.assertThat(eventLogEntryDAO.list(queryRunner, filters)).hasSize(count);
        return this;
    }

    public DatabaseAssert hasEntriesForParameterEntry(String parameterEntryKey, int count) {
        EventLogFilters filters = new EventLogFilters(new SimpleParameterEntryKey(parameterEntryKey));
        Assertions.assertThat(eventLogEntryDAO.list(queryRunner, filters)).hasSize(count);
        return this;
    }

    public EventLogEntryAssert hasParameterEntry(String parameterKey) {
        EventLogFilters filters = new EventLogFilters(new SimpleParameterKey(parameterKey));
        EventLogEntry entry = eventLogEntryDAO.list(queryRunner, filters).get(0);
        close();

        return SmartParamManagerBackedAssertions.assertThat(entry);
    }
}
