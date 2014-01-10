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

import java.util.ArrayList;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.manager.ParamManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.smartparam.manager.config.ParamManagerConfigBuilder.paramManagerConfig;

/**
 *
 * @author Adam Dubiel
 */
public class ParamManagerFactoryTest {

    private ParamEngine paramEngine;

    @BeforeMethod
    public void setUp() {
        paramEngine = mock(ParamEngine.class);
        ParamEngineRuntimeConfig runtimeConfig = mock(ParamEngineRuntimeConfig.class);
        when(runtimeConfig.getParamRepositories()).thenReturn(new ArrayList<ParamRepository>());
        when(paramEngine.runtimeConfiguration()).thenReturn(runtimeConfig);
    }

    @Test
    public void shouldCreateParamManagerWithDefaultSettigns() {
        // given
        ParamManagerConfig config = paramManagerConfig(paramEngine).build();

        // when
        ParamManager manager = ParamManagerFactory.paramManager(config);

        // then
        assertThat(manager).isNotNull();
    }

}
