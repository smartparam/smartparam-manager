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

import org.smartparam.editor.config.ParamEditorConfig;
import org.smartparam.editor.config.ParamEditorConfigBuilder;
import org.smartparam.editor.config.ParamEditorFactory;
import org.smartparam.editor.core.ParamEditor;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.index.Star;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.editor.core.filters.ParameterEntriesFilter;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.output.ParamValue;
import org.smartparam.engine.core.output.entry.MapEntry;
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
                .withParameterRepository(REPOSITORY_NAME, inMemoryParamRepository).build();
        paramEngine = ParamEngineFactory.paramEngine(paramEngineConfig);
        ParamEditorConfig paramEditorConfig = ParamEditorConfigBuilder.paramEditorConfig(paramEngine).build();
        ParamEditorFactory factory = new ParamEditorFactory(paramEditorConfig);

        paramViewer = factory.viewer();
        paramEditor = factory.editor();

        paramCreator = new AuthorizationParamCreator(new AuthorizationConfig(REPOSITORY_NAME), paramViewer, paramEditor);
    }

    @BeforeMethod
    public void setUp() {
        inMemoryParamRepository.clear();
        paramEngine.runtimeConfiguration().getParamCache().invalidate();
    }

    @Test
    public void shouldCreateBothAuthzParametersWhenNonExisting() {
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
        paramEditor.addEntry(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, new MapEntry());
        paramEditor.addEntry(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, new MapEntry());

        // when
        paramCreator.createNonExisting();

        // then
        assertThat(paramViewer.parameterExists(ROLE_AUTHZ_PARAMETER)).isTrue();
        // check that entry has not been removed - meaning the param persisted
        assertThat(paramViewer.listParameterEntries(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, ParameterEntriesFilter.empty())).hasSize(2);
    }

    @Test
    public void shouldCreateLoginAuthorizationParameterWithNoEntries() {
        // when
        paramCreator.createNonExisting();

        // then
        boolean empty = paramViewer.listParameterEntries(REPOSITORY_NAME, LOGIN_AUTHZ_PARAMETER, ParameterEntriesFilter.empty()).isEmpty();
        assertThat(empty).isTrue();
    }

    @Test
    public void shouldCreateValidNullableLoginAuthorizationParameterThatCanBeCalledFromParamEngine() {
        // when
        paramCreator.createNonExisting();

        // then
        ParamValue value = paramEngine.get(LOGIN_AUTHZ_PARAMETER, "testLogin", "testAction", "someParameter");
        assertThat(value.isEmpty()).isTrue();
    }

    @Test
    public void shouldCreateRoleAuthorizationParameterThatPermitsAllOperationsByDefault() {
        // when
        paramCreator.createNonExisting();

        // then
        MapEntry initialEntry = paramViewer.listParameterEntries(REPOSITORY_NAME, ROLE_AUTHZ_PARAMETER, ParameterEntriesFilter.empty()).firstItem();
        assertThat(initialEntry.get("role")).isEqualTo(Star.star());
        assertThat(initialEntry.get("action")).isEqualTo(Star.star());
        assertThat(initialEntry.get("parameter")).isEqualTo(Star.star());
        assertThat(initialEntry.get("authorized")).isEqualTo(true);
    }

    @Test
    public void shouldCreateValidRoleAuthorizationParameterThatCanBeCalledFromParamEngine() {
        // given
        paramCreator.createNonExisting();
        MapEntry map = new MapEntry()
                .put("role", "testRole")
                .put("action", "testAction")
                .put("parameter", Star.star())
                .put("authorized", false);
        paramEditor.addEntry(REPOSITORY_NAME, ROLE_AUTHZ_PARAMETER, map);

        // when
        boolean authorized = paramEngine.get(ROLE_AUTHZ_PARAMETER, "testRole", "testAction", "someParameter").get();

        // then
        assertThat(authorized).isFalse();
    }
}
