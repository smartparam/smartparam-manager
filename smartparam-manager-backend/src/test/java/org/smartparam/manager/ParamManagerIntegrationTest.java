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

import java.util.Arrays;
import java.util.List;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.level.LevelKey;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.editor.core.store.ParamRepositoryNaming;
import org.smartparam.editor.core.store.ParamRepositoryNamingBuilder;
import org.smartparam.editor.model.simple.SimpleLevel;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.model.simple.SimpleParameterEntry;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.audit.InMemoryEventLogRepository;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.authz.AuthorizationConfig;
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.authz.wrapper.AuthorizationFailedException;
import org.smartparam.manager.config.ParamManagerConfig;
import org.smartparam.manager.config.ParamManagerConfigBuilder;
import org.smartparam.manager.config.ParamManagerFactory;
import org.smartparam.manager.config.ParamManagerModule;
import org.smartparam.repository.memory.InMemoryParamRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.smartparam.manager.test.SmartParamManagerBackedAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerIntegrationTest {

    private static final RepositoryName REPOSITORY_NAME = RepositoryName.from("memoryRepository");

    private static final UserProfile USER = new UserProfile("login", "ROLE");

    private final InMemoryParamRepository inMemoryParamRepository = new InMemoryParamRepository();

    private final InMemoryEventLogRepository inMemoryEventLogRepository = new InMemoryEventLogRepository();

    private ParamEngine paramEngine;

    private ParamManager paramManager;

    @BeforeClass
    public void setUpClass() {
        ParamEngineConfig paramEngineConfig = ParamEngineConfigBuilder.paramEngineConfig()
                .withParameterRepositories(inMemoryParamRepository)
                .registerModule(new ParamManagerModule()).build();
        paramEngine = ParamEngineFactory.paramEngine(paramEngineConfig);

        ParamRepositoryNaming repositoryNaming = ParamRepositoryNamingBuilder.repositoryNaming()
                .registerAs(InMemoryParamRepository.class, REPOSITORY_NAME.name())
                .build();

        ParamManagerConfig paramManagerConfig = ParamManagerConfigBuilder.paramManagerConfig(paramEngine, repositoryNaming)
                .enableAuthorization(new AuthorizationConfig(REPOSITORY_NAME))
                .enableAuditing(inMemoryEventLogRepository, new FakeJsonAdapter())
                .build();
        paramManager = ParamManagerFactory.paramManager(paramManagerConfig);
    }

    @BeforeMethod
    public void setUp() {
        inMemoryParamRepository.clearExcept("sp.manager.authz.login", "sp.manager.authz.role");
        inMemoryEventLogRepository.clear();
        paramEngine.runtimeConfiguration().getParamCache().invalidate();
    }

    @Test
    public void shouldThrowExceptionWhenAuthorizationFails() {
        // given
        UserProfile user = new UserProfile("unauthorized", "ROOT");
        inMemoryParamRepository.addEntry("sp.manager.authz.login", new SimpleParameterEntry("unauthorized", "*", "*", "false"));

        // when
        catchException(paramManager).deleteParameter(user, REPOSITORY_NAME, "some parameter");

        // then
        assertThat(caughtException()).isInstanceOf(AuthorizationFailedException.class);
    }

    @Test
    public void shouldAuthorizeCreateNewParameterAndSaveEvent() {
        // given
        SimpleParameter parameter = new SimpleParameter().withName("test")
                .withInputLevels(1);

        // when
        paramManager.createParameter(USER, REPOSITORY_NAME, parameter);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.CREATE_PARAMETER)).isNotNull();
        assertThat(inMemoryParamRepository.inspector().hasParameter("test")).isTrue();
    }

    @Test
    public void shouldAuthorizeParameterUpdateAndSaveEvent() {
        // given
        SimpleParameter parameter = new SimpleParameter().withName("test")
                .withInputLevels(1);
        paramManager.createParameter(USER, REPOSITORY_NAME, parameter);

        SimpleParameter parameterUpdate = new SimpleParameter().withInputLevels(2);

        // when
        paramManager.updateParameter(USER, REPOSITORY_NAME, "test", parameterUpdate);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.UPDATE_PARAMETER)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).hasInputLevels(2);
    }

    @Test
    public void shouldAuthorizeParameterDeletionAndSaveEvent() {
        // given
        SimpleParameter parameter = new SimpleParameter().withName("test")
                .withInputLevels(1);
        paramManager.createParameter(USER, REPOSITORY_NAME, parameter);

        // when
        paramManager.deleteParameter(USER, REPOSITORY_NAME, "test");

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.DELETE_PARAMETER)).isNotNull();
        assertThat(inMemoryParamRepository.inspector().hasParameter("test")).isFalse();
    }

    @Test
    public void shouldAuthorizeLevelAdditionAndSaveEvent() {
        // given
        SimpleParameter parameter = new SimpleParameter().withName("test").withInputLevels(1);
        paramManager.createParameter(USER, REPOSITORY_NAME, parameter);

        SimpleLevel level = new SimpleLevel().withName("test-level");

        // when
        paramManager.addLevel(USER, REPOSITORY_NAME, "test", level);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.ADD_LEVEL)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).firstLevel().hasName("test-level");
    }

    @Test
    public void shouldAuthorizeLevelUpdateAndSaveEvent() {
        // given
        paramManager.createParameter(USER, REPOSITORY_NAME, new SimpleParameter().withName("test").withInputLevels(1));
        LevelKey levelKey = paramManager.addLevel(USER, REPOSITORY_NAME, "test", new SimpleLevel().withName("test-level")).levelKey();

        SimpleLevel levelUpdate = new SimpleLevel().withName("test-level").withMatcher("matcher");

        // when
        paramManager.updateLevel(USER, REPOSITORY_NAME, "test", levelKey, levelUpdate);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.UPDATE_LEVEL)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).firstLevel().hasMatcher("matcher");
    }

    @Test
    public void shouldAuthorizeLevelOrderingAndSaveEvent() {
        // given
        paramManager.createParameter(USER, REPOSITORY_NAME, new SimpleParameter().withName("test").withInputLevels(1));
        LevelKey levelKey2 = paramManager.addLevel(USER, REPOSITORY_NAME, "test", new SimpleLevel().withName("test-level2")).levelKey();
        LevelKey levelKey1 = paramManager.addLevel(USER, REPOSITORY_NAME, "test", new SimpleLevel().withName("test-level1")).levelKey();

        List<LevelKey> newOrder = Arrays.asList(levelKey1, levelKey2);

        // when
        paramManager.reorderLevels(USER, REPOSITORY_NAME, "test", newOrder);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.REOREDER_LEVELS)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).namedLevelsInOrder("test-level1", "test-level2");
    }

    @Test
    public void shouldAuthorizeLevelDeletionAndSaveEvent() {
        // given
        paramManager.createParameter(USER, REPOSITORY_NAME, new SimpleParameter().withName("test").withInputLevels(1));
        LevelKey levelKey = paramManager.addLevel(USER, REPOSITORY_NAME, "test", new SimpleLevel().withName("test-level1")).levelKey();

        // when
        paramManager.deleteLevel(USER, REPOSITORY_NAME, "test", levelKey);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.DELETE_LEVEL)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).hasNoLevels();
    }

    @Test
    public void shouldAuthorizeEntryAdditionAndSaveEvent() {
        // given
        paramManager.createParameter(USER, REPOSITORY_NAME, new SimpleParameter().withName("test")
                .withInputLevels(1).withLevel(new SimpleLevel().withName("level")));
        ParameterEntryMap entry = new ParameterEntryMap().put("level", "value");

        // when
        paramManager.addEntries(USER, REPOSITORY_NAME, "test", Arrays.asList(entry));

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.ADD_ENTRY)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).hasEntries(1);
    }

    @Test
    public void shouldAuthorizeEntryUpdateAndSaveEvent() {
        // given
        paramManager.createParameter(USER, REPOSITORY_NAME, new SimpleParameter().withName("test")
                .withInputLevels(1).withLevel(new SimpleLevel().withName("level")));
        ParameterEntryMap entry = new ParameterEntryMap().put("level", "value");
        ParameterEntryKey entryKey = paramManager.addEntries(USER, REPOSITORY_NAME, "test", Arrays.asList(entry)).firstEntryKey();

        ParameterEntryMap entryUpdate = new ParameterEntryMap().put("level", "updated-value");

        // when
        paramManager.updateEntry(USER, REPOSITORY_NAME, "test", entryKey, entryUpdate);

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.UPDATE_ENTRY)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).onlyEntry().hasLevels("updated-value");
    }

    @Test
    public void shouldAuthorizeEntryDeletionAndSaveEvent() {
        // given
        paramManager.createParameter(USER, REPOSITORY_NAME, new SimpleParameter().withName("test")
                .withInputLevels(1).withLevel(new SimpleLevel().withName("level")));
        ParameterEntryMap entry = new ParameterEntryMap().put("level", "value");
        ParameterEntryKey entryKey = paramManager.addEntries(USER, REPOSITORY_NAME, "test", Arrays.asList(entry)).firstEntryKey();

        // when
        paramManager.deleteEntries(USER, REPOSITORY_NAME, "test", Arrays.asList(entryKey));

        // then
        assertThat(inMemoryEventLogRepository.findFirstEvent(Action.DELETE_ENTRY)).isNotNull();
        assertThat(inMemoryParamRepository.load("test")).hasNoEntries();
    }
}
