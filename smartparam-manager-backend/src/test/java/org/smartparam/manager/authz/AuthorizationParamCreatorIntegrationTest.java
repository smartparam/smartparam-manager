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
package org.smartparam.manager.authz;

import org.smartparam.editor.editor.BasicParamEditor;
import org.smartparam.editor.editor.ParamEditor;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.model.simple.SimpleParameterEntry;
import org.smartparam.editor.viewer.BasicParamViewer;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.editor.viewer.ParameterEntriesFilter;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.repository.memory.InMemoryParamRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class AuthorizationParamCreatorIntegrationTest {

    private static final RepositoryName REPOSITORY_NAME = RepositoryName.from("InMemoryParamRepository");

    private static final String LOGIN_AUTHZ_PARAMETER = "sp.manager.authz.login";

    private static final String ROLE_AUTHZ_PARAMETER = "sp.manager.authz.role";

    private final InMemoryParamRepository inMemoryParamRepository = new InMemoryParamRepository();

    private ParamEngine paramEngine;

    private ParamEditor paramEditor;

    private ParamViewer paramViewer;

    private AuthorizationParamCreator paramCreator;

    @BeforeClass
    public void setUpClass() {
        ParamEngineConfig paramEngineConfig = ParamEngineConfigBuilder.paramEngineConfig()
                .withPackagesToScan("org.smartparam.manager.authz")
                .withParameterRepositories(inMemoryParamRepository).build();
        paramEngine = ParamEngineFactory.paramEngine(paramEngineConfig);

        paramEditor = new BasicParamEditor(paramEngine);
        paramViewer = new BasicParamViewer(paramEngine);

        paramCreator = new AuthorizationParamCreator(new AuthorizationConfig(REPOSITORY_NAME), paramViewer, paramEditor);
    }

    @BeforeMethod
    public void setUp() {
        inMemoryParamRepository.clear();
        paramEngine.runtimeConfiguration().getParamCache().invalidate();
    }

    @Test
    public void shouldCreateBothAuthzParametersWhenNoenExisting() {
        // when
        paramCreator.createNonExisting();

        // then
        assertThat(paramViewer.parameterExists(LOGIN_AUTHZ_PARAMETER)).isTrue();
        assertThat(paramViewer.parameterExists(ROLE_AUTHZ_PARAMETER)).isTrue();
    }

    @Test
    public void shouldCreateOnlyMissingAuthzParameters() {
        // given
        paramEditor.createParameter(REPOSITORY_NAME, new SimpleParameter().withName(LOGIN_AUTHZ_PARAMETER));
        paramEditor.addEntry(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, new SimpleParameterEntry());
        paramEditor.addEntry(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, new SimpleParameterEntry());

        // when
        paramCreator.createNonExisting();

        // then
        assertThat(paramViewer.parameterExists(ROLE_AUTHZ_PARAMETER)).isTrue();
        // check that entry has not been removed - meaning the param persisted
        assertThat(paramViewer.listParameterEntries(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, ParameterEntriesFilter.empty())).hasSize(2);
    }

    @Test
    public void shouldCreateLoginAuthorizationParameterThatPermitsAllOperationsByDefault() {
        // when
        paramCreator.createNonExisting();

        // then
        ParameterEntry initialEntry = paramViewer.listParameterEntries(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, ParameterEntriesFilter.empty()).firstItem();
        assertThat(initialEntry.getLevels()).containsExactly("*", "*", "*", "true");
    }

    @Test
    public void shouldCreateValidLoginAuthorizationParameterThatCanBeCalledFromparamEngine() {
        // given
        paramCreator.createNonExisting();
        paramEditor.addEntry(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, new SimpleParameterEntry("testLogin", "testAction", "*", "false"));

        // when
        boolean authorized = paramEngine.get(LOGIN_AUTHZ_PARAMETER, "testLogin", "testAction", "someParameter").get();

        // then
        assertThat(authorized).isFalse();
    }

    @Test
    public void shouldCreateRoleAuthorizationParameterThatPermitsAllOperationsByDefault() {
        // when
        paramCreator.createNonExisting();

        // then
        ParameterEntry initialEntry = paramViewer.listParameterEntries(REPOSITORY_NAME, ROLE_AUTHZ_PARAMETER, ParameterEntriesFilter.empty()).firstItem();
        assertThat(initialEntry.getLevels()).containsExactly("*", "*", "*", "true");
    }

    @Test
    public void shouldCreateValidRoleAuthorizationParameterThatCanBeCalledFromparamEngine() {
        // given
        paramCreator.createNonExisting();
        paramEditor.addEntry(REPOSITORY_NAME, ROLE_AUTHZ_PARAMETER, new SimpleParameterEntry("testLogin", "testAction", "*", "false"));

        // when
        boolean authorized = paramEngine.get(ROLE_AUTHZ_PARAMETER, "testLogin", "testAction", "someParameter").get();

        // then
        assertThat(authorized).isFalse();
    }
}
