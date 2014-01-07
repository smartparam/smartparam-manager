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

import java.util.Set;
import javax.sql.DataSource;
import org.polyjdbc.core.query.QueryRunnerFactory;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.manager.audit.EventLogRepository;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcEventLogRepositoryConfig extends ComponentConfig {

    private final DataSource dataSource;

    private final JdbcEventLogConfig config;

    public JdbcEventLogRepositoryConfig(DataSource dataSource, JdbcEventLogConfig config) {
        this.dataSource = dataSource;
        this.config = config;
    }

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(TransactionManager.class, DataSourceTransactionManager.class));
        components.add(component(QueryRunnerFactory.class, QueryRunnerFactory.class));
        components.add(component(SchemaManagerFactory.class, SchemaManagerFactory.class));
        components.add(component(TransactionRunner.class, TransactionRunner.class));
        components.add(component(SchemaCreator.class, SchemaCreator.class));
        components.add(component(JdbcEventLogEntryDAO.class, JdbcEventLogEntryDAO.class));
        components.add(component(EventLogRepository.class, JdbcEventLogRepository.class));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JdbcEventLogConfig getConfig() {
        return config;
    }

}
