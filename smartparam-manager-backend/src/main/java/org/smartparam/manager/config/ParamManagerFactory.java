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

import org.picocontainer.MutablePicoContainer;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.BasicParamManager;
import org.smartparam.manager.ParamManager;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerFactory {

    public static ParamManager paramManager(ParamManagerConfig config) {
        return new ParamManagerFactory().createParamManager(config);
    }

    public ParamManager createParamManager(ParamManagerConfig config) {
        config.addComponent(component(ParamManager.class, BasicParamManager.class));
        config.addComponent(component(ParamEngine.class, config.paramEngine()));

        MutablePicoContainer container = PicoContainerUtil.createContainer(config);
        return container.getComponent(ParamManager.class);
    }
}
