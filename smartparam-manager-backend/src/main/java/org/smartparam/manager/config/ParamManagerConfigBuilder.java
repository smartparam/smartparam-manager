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
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.audit.BasicEventsLogger;
import org.smartparam.manager.audit.EventLogEntryFactory;
import org.smartparam.manager.audit.EventLogRepository;
import org.smartparam.manager.audit.EventsLogger;
import org.smartparam.manager.authz.AuthorizationCheckpoint;
import org.smartparam.manager.authz.AuthorizationConfig;
import org.smartparam.manager.authz.ParamAuthorizationCheckpoint;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamManagerConfigBuilder {

    private final ParamManagerConfig config;

    private ParamManagerConfigBuilder(ParamEngine paramEngine) {
        this.config = new ParamManagerConfig(paramEngine, null, null);
    }

    public static ParamManagerConfigBuilder paramManagerConfig(ParamEngine paramEngine) {
        return new ParamManagerConfigBuilder(paramEngine);
    }

    public ParamManagerConfigBuilder withComponent(ComponentDefinition component) {
        config.addComponent(component);
        return this;
    }

    public ParamManagerConfigBuilder enableAuthorization(AuthorizationConfig authorizationConfig) {
        config.addComponent(component(AuthorizationConfig.class, authorizationConfig));
        config.addComponent(component(AuthorizationCheckpoint.class, ParamAuthorizationCheckpoint.class));
        return this;
    }

    public ParamManagerConfigBuilder enableAuditing(EventLogRepository eventLogRepository) {
        config.addComponent(component(EventsLogger.class, BasicEventsLogger.class));
        config.addComponent(component(EventLogRepository.class, eventLogRepository));
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

    public ParamManagerConfigBuilder withEvenLogEntryFactory(EventLogEntryFactory eventLogEntryFactory) {
        config.addComponent(component(EventLogEntryFactory.class, eventLogEntryFactory));
        return this;
    }
}
