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

import java.util.List;
import org.smartparam.editor.editor.BasicParamEditor;
import org.smartparam.editor.viewer.BasicParamViewer;
import org.smartparam.engine.config.pico.ComponentConfig;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerContainerConfig extends ComponentConfig {

    @Override
    protected void injectDefaults(List<Object> components) {
        components.add(BasicParamViewer.class);
        components.add(BasicParamEditor.class);
    }
}
