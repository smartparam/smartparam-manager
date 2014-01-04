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

import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.audit.EventLogEntry;
import org.testng.annotations.Test;
import static org.smartparam.manager.audit.EventLogEntryTestBuilder.parameterEventLogEntry;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcEventLogEntryDAOTest extends DatabaseTest {

    @Test
    public void shouldSaveParameterEventLogEntry() {
        // given
        JdbcEventLogEntryDAO dao = get(JdbcEventLogEntryDAO.class);
        EventLogEntry logEntry = parameterEventLogEntry()
                .withTimestamp(10000).responsible("responsibleUser").withAction(Action.CREATE_PARAMETER)
                .withRepository("repository").forParameter("parameter").withSerializedDetails("some details").build();
        QueryRunner runner = queryRunner();

        // when
        dao.save(runner, logEntry);
        runner.close();

        // then
        assertDatabase().hasEntryForParameter("parameter").close();
    }

    @Test
    public void shouldSaveAllFieldsOfEventLogEntry() {
        // given
        JdbcEventLogEntryDAO dao = get(JdbcEventLogEntryDAO.class);
        EventLogEntry logEntry = parameterEventLogEntry()
                .withTimestamp(10000).responsible("responsibleUser").withAction(Action.CREATE_PARAMETER)
                .withRepository("repository").forParameter("parameter").withSerializedDetails("some details").build();
        QueryRunner runner = queryRunner();

        // when
        dao.save(runner, logEntry);
        runner.close();

        // then
        assertDatabase().hasParameterEntry("parameter").hasTimestamp(10000).hasRepository("repository").hasResponsible("responsibleUser")
                .hasAction(Action.CREATE_PARAMETER).hasSerializedEventDetails("some details")
                .forParmeter("parameter");
    }

    @Test
    public void shouldSaveParameterEntryKeyWhenSavingEntryEvent() {
        // given
        JdbcEventLogEntryDAO dao = get(JdbcEventLogEntryDAO.class);
        EventLogEntry logEntry = parameterEventLogEntry()
                .withTimestamp(10000).responsible("responsibleUser").withAction(Action.CREATE_PARAMETER)
                .withRepository("repository").forParameterEntry("parameter", "entry").withSerializedDetails("some details").build();
        QueryRunner runner = queryRunner();

         // when
        dao.save(runner, logEntry);
        runner.close();

        // then
        assertDatabase().hasEntryForParameterEntry("entry").close();
    }

}
