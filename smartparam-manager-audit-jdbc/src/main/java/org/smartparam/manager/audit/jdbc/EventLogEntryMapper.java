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

import java.sql.ResultSet;
import java.sql.SQLException;
import org.polyjdbc.core.query.mapper.ObjectMapper;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.simple.SimpleParameterEntryKey;
import org.smartparam.editor.model.simple.SimpleParameterKey;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.audit.EventLogEntry;
import org.smartparam.manager.audit.EventLogEntryType;

/**
 *
 * @author Adam Dubiel
 */
public class EventLogEntryMapper implements ObjectMapper<EventLogEntry> {

    public EventLogEntry createObject(ResultSet resultSet) throws SQLException {
        EventLogEntryType entryType = EventLogEntryType.fromCode(resultSet.getString("entry_type"));

        return new DehydratedEventLogEntry(
                resultSet.getTimestamp("timestamp").getTime(),
                new RepositoryName(resultSet.getString("repository")),
                Action.valueOf(resultSet.getString("event_type")),
                resultSet.getString("responsible"),
                new SimpleParameterKey(resultSet.getString("parameter_key")),
                entryType == EventLogEntryType.ENTRY ? new SimpleParameterEntryKey(resultSet.getString("parameter_entry_key")) : null,
                resultSet.getString("event_details"));
    }

}
