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
package org.smartparam.manager.config;

import org.smartparam.editor.editor.ParamEditor;
import org.smartparam.editor.store.ParamRepositoryNaming;
import org.smartparam.editor.store.ParamRepositoryNamingBuilder;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.manager.adapter.JsonAdapter;
import org.smartparam.manager.audit.BasicEventsLogger;
import org.smartparam.manager.audit.EventLogEntryFactory;
import org.smartparam.manager.audit.EventLogRepository;
import org.smartparam.manager.audit.EventsLogger;
import org.smartparam.manager.audit.diff.DiffEventLogEntryFactory;
import org.smartparam.manager.authz.AuthorizationCheckpoint;
import org.smartparam.manager.authz.AuthorizationConfig;
import org.smartparam.manager.authz.AuthorizationParamCreator;
import org.smartparam.manager.authz.ParamAuthorizationCheckpoint;
import org.smartparam.manager.authz.wrapper.AuthorizationRunner;
import org.smartparam.manager.time.TimeProvider;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamManagerConfigBuilder {

    private final ParamManagerConfig config;

    private final ParamRepositoryNamingBuilder repositoryNamingBuilder = ParamRepositoryNamingBuilder.repositoryNaming();

    private ParamManagerConfigBuilder(ParamEngine paramEngine) {
        this.config = new ParamManagerConfig(paramEngine);
    }

    public static ParamManagerConfigBuilder paramManagerConfig(ParamEngine paramEngine) {
        return new ParamManagerConfigBuilder(paramEngine);
    }

    public ParamManagerConfig build() {
        config.addComponent(component(ParamRepositoryNaming.class, repositoryNamingBuilder.build()));
        return config;
    }

    public ParamManagerConfigBuilder withComponent(ComponentDefinition component) {
        config.addComponent(component);
        return this;
    }

    public ParamManagerConfigBuilder enableAuthorization(AuthorizationConfig authorizationConfig) {
        config.addComponent(component(AuthorizationConfig.class, authorizationConfig));
        config.addComponent(component(AuthorizationParamCreator.class, AuthorizationParamCreator.class));
        config.addComponent(component(AuthorizationCheckpoint.class, ParamAuthorizationCheckpoint.class));
        return this;
    }

    public ParamManagerConfigBuilder enableAuditing(EventLogRepository eventLogRepository, JsonAdapter jsonAdapter) {
        config.addComponent(component(EventsLogger.class, BasicEventsLogger.class));
        config.addComponent(component(EventLogRepository.class, eventLogRepository));
        config.addComponent(component(EventLogEntryFactory.class, DiffEventLogEntryFactory.class));
        withJsonAdapter(jsonAdapter);
        return this;
    }

    public ParamManagerConfigBuilder enableAuditing(EventLogRepository eventLogRepository, EventLogEntryFactory eventLogEntryFactory) {
        config.addComponent(component(EventsLogger.class, BasicEventsLogger.class));
        config.addComponent(component(EventLogRepository.class, eventLogRepository));
        config.addComponent(component(EventLogEntryFactory.class, eventLogEntryFactory));
        return this;
    }

    public ParamManagerConfigBuilder withRepositoryKnownAs(Class<? extends ParamRepository> reposioryClass, String... consequentNames) {
        repositoryNamingBuilder.registerAs(reposioryClass, consequentNames);
        return this;
    }

    public ParamManagerConfigBuilder withAuthorizationRunner(AuthorizationRunner authorizationRunner) {
        config.addComponent(component(AuthorizationRunner.class, authorizationRunner));
        return this;
    }

    public ParamManagerConfigBuilder withTimeProvider(TimeProvider timeProvider) {
        config.addComponent(component(TimeProvider.class, timeProvider));
        return this;
    }

    public ParamManagerConfigBuilder withParamViewer(ParamViewer viewer) {
        config.addComponent(component(ParamViewer.class, viewer));
        return this;
    }

    public ParamManagerConfigBuilder withParamEditor(ParamEditor editor) {
        config.addComponent(component(ParamEditor.class, editor));
        return this;
    }

    public ParamManagerConfigBuilder withJsonAdapter(JsonAdapter jsonAdapter) {
        config.addComponent(component(JsonAdapter.class, jsonAdapter));
        return this;
    }
}
