/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.manager.audit.jdbc;

import java.util.List;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.query.TransactionWrapper;
import org.polyjdbc.core.query.VoidTransactionWrapper;
import org.smartparam.engine.config.initialization.InitializableComponent;
import org.smartparam.manager.audit.EventLogFilters;
import org.smartparam.manager.audit.EventLogRepository;
import org.smartparam.manager.audit.EventLogEntry;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcEventLogRepository implements EventLogRepository, InitializableComponent {

    private TransactionRunner transactionRunner;

    private JdbcEventLogEntryDAO jdbcEventLogEntryDAO;

    private SchemaCreator schemaCreator;

    @Override
    public boolean supports(Class<? extends EventLogEntry> entryClass) {
        return true;
    }

    @Override
    public void initialize() {
        schemaCreator.createSchema();
    }

    public List<EventLogEntry> list(final EventLogFilters filters) {
        return transactionRunner.run(new TransactionWrapper<List<EventLogEntry>>() {
            @Override
            public List<EventLogEntry> perform(QueryRunner queryRunner) {
                return null;
            }
        });
    }

    @Override
    public void save(final EventLogEntry eventLogEntry) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                jdbcEventLogEntryDAO.save(queryRunner, eventLogEntry);
            }
        });
    }
}
