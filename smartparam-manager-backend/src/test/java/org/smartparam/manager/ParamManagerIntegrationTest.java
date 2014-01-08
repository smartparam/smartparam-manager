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
package org.smartparam.manager;

import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.audit.InMemoryEventLogRepository;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.authz.AuthorizationConfig;
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.config.ParamManagerConfig;
import org.smartparam.manager.config.ParamManagerConfigBuilder;
import org.smartparam.manager.config.ParamManagerFactory;
import org.smartparam.repository.memory.InMemoryParamRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.smartparam.manager.test.SmartParamManagerBackedAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerIntegrationTest {

    private static final RepositoryName REPOSITORY_NAME = RepositoryName.from("InMemoryParamRepository");

    private final InMemoryParamRepository inMemoryParamRepository = new InMemoryParamRepository();

    private final InMemoryEventLogRepository inMemoryEventLogRepository = new InMemoryEventLogRepository();

    private ParamManager paramManager;

    @BeforeClass
    public void setUpClass() {
        ParamEngineConfig paramEngineConfig = ParamEngineConfigBuilder.paramEngineConfig()
                .withPackagesToScan("org.smartparam.manager.authz")
                .withParameterRepositories(inMemoryParamRepository).build();
        ParamEngine paramEngine = ParamEngineFactory.paramEngine(paramEngineConfig);

        ParamManagerConfig paramManagerConfig = ParamManagerConfigBuilder.paramManagerConfig(paramEngine)
                .enableAuthorization(new AuthorizationConfig(REPOSITORY_NAME))
                .enableAuditing(inMemoryEventLogRepository, new FakeJsonAdapter())
                .build();
        paramManager = ParamManagerFactory.paramManager(paramManagerConfig);
    }

    @BeforeMethod
    public void setUp() {
        inMemoryParamRepository.clearExcept("sp.manager.authz.login", "sp.manager.authz.role");
        inMemoryEventLogRepository.clear();
    }

    @Test
    public void shouldAuthorizeCreateNewParameterAndSaveEvent() {
        // given
        SimpleParameter parameter = new SimpleParameter().withName("test")
                .withInputLevels(1);

        // when
        paramManager.createParameter(new UserProfile("login", "ROLE"), REPOSITORY_NAME, parameter);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.CREATE_PARAMETER)).isNotNull();
    }
}
