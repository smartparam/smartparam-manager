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

import java.util.Arrays;
import javax.sql.DataSource;
import org.picocontainer.PicoContainer;
import org.polyjdbc.core.infrastructure.PolyDatabaseTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import static org.smartparam.manager.audit.jdbc.JdbcEventLogConfigBuilder.jdbcEventLogConfig;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseTest extends PolyDatabaseTest {

    private SchemaCreator schemaCreator;

    private PicoContainer container;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    protected DatabaseAssert assertDatabase() {
        return new DatabaseAssert(queryRunner(), get(JdbcEventLogEntryDAO.class));
    }

    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws Exception {
        DataSource dataSource = createDatabase("H2", "jdbc:h2:mem:test", "smartparam", "smartparam");

        JdbcEventLogConfig configuration = jdbcEventLogConfig(dialect())
                .withSufix("audit").build();

        this.schemaCreator = new SchemaCreator(configuration, schemaManagerFactory());
        schemaCreator.createSchema();

        JdbcEventLogRepositoryFactory factory = new JdbcEventLogRepositoryFactory();
        this.container = factory.createContainer(new JdbcEventLogRepositoryConfig(dataSource, configuration));
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanDatabse() {
        JdbcEventLogConfig config = get(JdbcEventLogConfig.class);
        super.deleteFromRelations(Arrays.asList(config.entityName()));
    }

    @AfterClass(alwaysRun = true)
    public void tearDownDatabase() throws Exception {
        dropDatabase();
        this.container = null;
    }

    @Override
    protected void dropSchema() {
        schemaCreator.dropSchema();
    }

}
