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

import org.smartparam.editor.config.ParamEditorConfig;
import org.smartparam.editor.config.ParamEditorConfigBuilder;
import org.smartparam.editor.config.ParamEditorFactory;
import org.smartparam.editor.core.ParamEditor;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.ParamEngine;
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

    private ParamManagerConfigBuilder(ParamEngine paramEngine, ParamEditor paramEditor, ParamViewer paramViewer) {
        this.config = new ParamManagerConfig(paramEngine);
        this.config.addComponent(component(ParamEditor.class, paramEditor));
        this.config.addComponent(component(ParamViewer.class, paramViewer));
    }

    public static ParamManagerConfigBuilder paramManagerConfig(ParamEngine paramEngine, ParamEditor paramEditor, ParamViewer paramViewer) {
        return new ParamManagerConfigBuilder(paramEngine, paramEditor, paramViewer);
    }

    public static ParamManagerConfigBuilder paramManagerConfig(ParamEngine paramEngine, ParamEditorConfig editorConfig) {
        ParamEditorFactory factory = new ParamEditorFactory(editorConfig);
        return paramManagerConfig(paramEngine, factory.editor(), factory.viewer());
    }

    public static ParamManagerConfigBuilder paramManagerConfig(ParamEngine paramEngine) {
        ParamEditorConfig editorConfig = ParamEditorConfigBuilder.paramEditorConfig(paramEngine).build();
        return paramManagerConfig(paramEngine, editorConfig);
    }

    public ParamManagerConfig build() {
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

    public ParamManagerConfigBuilder withAuthorizationRunner(AuthorizationRunner authorizationRunner) {
        config.addComponent(component(AuthorizationRunner.class, authorizationRunner));
        return this;
    }

    public ParamManagerConfigBuilder withTimeProvider(TimeProvider timeProvider) {
        config.addComponent(component(TimeProvider.class, timeProvider));
        return this;
    }

    public ParamManagerConfigBuilder withJsonAdapter(JsonAdapter jsonAdapter) {
        config.addComponent(component(JsonAdapter.class, jsonAdapter));
        return this;
    }
}
