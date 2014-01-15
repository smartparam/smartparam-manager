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
import org.smartparam.editor.core.BasicParamEditor;
import org.smartparam.editor.core.ParamEditor;
import org.smartparam.editor.core.BasicParamViewer;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.editor.core.store.ParamRepositoryNaming;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.audit.DisabledEventsLogger;
import org.smartparam.manager.authz.AuthorizationCheckpoint;
import org.smartparam.manager.authz.DisabledAuthorizationCheckpoint;
import org.smartparam.manager.authz.wrapper.AuthorizationRunner;
import org.smartparam.manager.authz.wrapper.ThrowingAuthorizationRunner;
import org.smartparam.manager.time.SimpleTimeProvider;
import org.smartparam.manager.time.TimeProvider;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerConfig extends ComponentConfig {

    private final ParamEngine paramEngine;

    private final ParamRepositoryNaming repositoryNaming;

    ParamManagerConfig(ParamEngine paramEngine, ParamRepositoryNaming repositoryNaming) {
        this.paramEngine = paramEngine;
        this.repositoryNaming = repositoryNaming;
    }

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(ParamViewer.class, BasicParamViewer.class));
        components.add(component(ParamEditor.class, BasicParamEditor.class));
        components.add(component(AuthorizationCheckpoint.class, DisabledAuthorizationCheckpoint.class));
        components.add(component(DisabledEventsLogger.class, DisabledEventsLogger.class));
        components.add(component(TimeProvider.class, SimpleTimeProvider.class));
        components.add(component(AuthorizationRunner.class, ThrowingAuthorizationRunner.class));
    }

    ParamEngine paramEngine() {
        return paramEngine;
    }

    ParamRepositoryNaming repositoryNaming() {
        return repositoryNaming;
    }
}
