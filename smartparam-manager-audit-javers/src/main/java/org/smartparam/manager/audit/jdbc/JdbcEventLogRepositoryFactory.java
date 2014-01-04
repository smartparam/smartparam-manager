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

import javax.sql.DataSource;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.pico.PicoContainerUtil;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcEventLogRepositoryFactory {

    public static JdbcEventLogRepository jdbcEventLogRepository(DataSource dataSource, JdbcEventLogConfig config) {
        return new JdbcEventLogRepositoryFactory().createRepository(dataSource, config);
    }

    public JdbcEventLogRepository createRepository(DataSource dataSource, JdbcEventLogConfig config) {
        return createRepository(new JdbcEventLogRepositoryConfig(dataSource, config));
    }

    public JdbcEventLogRepository createRepository(JdbcEventLogRepositoryConfig config) {
        PicoContainer container = createContainer(config);
        return container.getComponent(JdbcEventLogRepository.class);
    }

    PicoContainer createContainer(JdbcEventLogRepositoryConfig config) {
        MutablePicoContainer container = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(container, config.getConfig(), config.getConfig().dialect(), config.getDataSource());
        PicoContainerUtil.injectImplementations(container, config.getComponents());

        return container;
    }

}
