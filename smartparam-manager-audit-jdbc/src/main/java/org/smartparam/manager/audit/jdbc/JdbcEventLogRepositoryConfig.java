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
import javax.sql.DataSource;
import org.polyjdbc.core.query.QueryRunnerFactory;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.smartparam.engine.config.pico.ComponentConfig;

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
    protected void injectDefaults(List<Object> components) {
        components.add(DataSourceTransactionManager.class);
        components.add(QueryRunnerFactory.class);
        components.add(SchemaManagerFactory.class);
        components.add(TransactionRunner.class);
        components.add(SchemaCreator.class);
        components.add(JdbcEventLogEntryDAO.class);
        components.add(JdbcEventLogRepository.class);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JdbcEventLogConfig getConfig() {
        return config;
    }

}
