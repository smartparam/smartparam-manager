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

import java.util.Arrays;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.output.DefaultParamValue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.smartparam.test.ParamValueBuilder.paramValue;

/**
 *
 * @author Adam Dubiel
 */
public class ParamAuthorizationCheckpointTest {

    private ParamEngine paramEngine;

    @BeforeMethod
    public void setUp() {
        paramEngine = mock(ParamEngine.class);
    }

    @Test
    public void shouldListenOnlyToMethodsThatReturnNonNullResult() {
        // given
        AuthorizationConfig config = new AuthorizationConfig(new RepositoryName(""), Arrays.asList(AuthorizationMethod.ROLE, AuthorizationMethod.LOGIN));
        ParamAuthorizationCheckpoint checkpoint = new ParamAuthorizationCheckpoint(paramEngine, config);

        // when
        when(paramEngine.get(anyString(), any(ParamContext.class))).thenReturn(DefaultParamValue.empty());
        when(paramEngine.get(eq(ParamAuthorizationCheckpoint.ROLE_AUTHZ_PARAMETER), any(ParamContext.class))).thenReturn(paramValue(true));

        // then
        boolean authorized = checkpoint.authorize(null, Action.ADD_ENTRY, null, "someParameter");

        // then
        assertThat(authorized).isEqualTo(true);
    }

    @Test
    public void shouldReturnFalseIfNoneMethodAuthorizesAction() {
        // given
        AuthorizationConfig config = new AuthorizationConfig(new RepositoryName(""), Arrays.asList(AuthorizationMethod.ROLE, AuthorizationMethod.LOGIN));
        ParamAuthorizationCheckpoint checkpoint = new ParamAuthorizationCheckpoint(paramEngine, config);

        when(paramEngine.get(anyString(), any(ParamContext.class))).thenReturn(paramValue(false));

        // when
        boolean authorized = checkpoint.authorize(null, Action.ADD_ENTRY, null, "someParameter");

        // then
        assertThat(authorized).isEqualTo(false);
    }

    @Test
    public void shouldAlwaysIterateInOrderFromLeastSpecificToMostSpecific() {
        // given
        AuthorizationConfig config = new AuthorizationConfig(new RepositoryName(""), Arrays.asList(AuthorizationMethod.LOGIN, AuthorizationMethod.ROLE));
        ParamAuthorizationCheckpoint checkpoint = new ParamAuthorizationCheckpoint(paramEngine, config);

        when(paramEngine.get(anyString(), any(ParamContext.class))).thenReturn(paramValue(false));
        when(paramEngine.get(eq("sp.manager.authz.login"), any(ParamContext.class))).thenReturn(paramValue(true));

        // when
        boolean authorized = checkpoint.authorize(null, Action.ADD_ENTRY, null, "someParameter");

        // then
        assertThat(authorized).isEqualTo(true);
        verify(paramEngine, times(2)).get(anyString(), any(ParamContext.class));
    }
}
