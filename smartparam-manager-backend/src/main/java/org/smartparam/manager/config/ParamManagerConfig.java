/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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

import java.util.Set;
import org.smartparam.editor.editor.BasicParamEditor;
import org.smartparam.editor.editor.ParamEditor;
import org.smartparam.editor.viewer.BasicParamViewer;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.audit.DisabledEventsLogger;
import org.smartparam.manager.audit.EventLogEntryFactory;
import org.smartparam.manager.audit.diff.DiffEventLogEntryFactory;
import org.smartparam.manager.authz.AuthorizationCheckpoint;
import org.smartparam.manager.authz.DisabledAuthorizationCheckpoint;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerConfig extends ComponentConfig {

    private final ParamEngine paramEngine;

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(ParamViewer.class, BasicParamViewer.class));
        components.add(component(ParamEditor.class, BasicParamEditor.class));
        components.add(component(EventLogEntryFactory.class, DiffEventLogEntryFactory.class));
        components.add(component(AuthorizationCheckpoint.class, DisabledAuthorizationCheckpoint.class));
        components.add(component(DisabledEventsLogger.class, DisabledEventsLogger.class));
    }

    ParamManagerConfig(ParamEngine paramEngine) {
        this.paramEngine = paramEngine;
    }

    ParamEngine paramEngine() {
        return paramEngine;
    }
}
