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
package org.smartparam.manager.authz.wrapper;

import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.authz.AuthorizationCheckpoint;
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.result.Result;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 *
 * @author Adam Dubiel
 */
public class ThrowingAuthorizationRunnerTest {

    private ThrowingAuthorizationRunner authorizationRunner;

    private AuthorizationCheckpoint authorizationCheckpoint;

    @BeforeMethod
    public void setUp() {
        authorizationCheckpoint = mock(AuthorizationCheckpoint.class);
        authorizationRunner = new ThrowingAuthorizationRunner(authorizationCheckpoint);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRunActionCallbackOnSuccessfulAuthorization() {
        // given
        when(authorizationCheckpoint.authorize(any(UserProfile.class), any(Action.class), any(RepositoryName.class), anyString()))
                .thenReturn(true);
        AuthorizedAction<Result> action = mock(AuthorizedAction.class);

        // when
        catchException(authorizationRunner).runAction(null, null, Action.ADD_LEVEL, null, action);

        // then
        assertThat(caughtException()).isNull();
        verify(action).perform();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldThrowExceptionAndNotRunActionWhenAuthorizationFailed() {
        // given
        when(authorizationCheckpoint.authorize(any(UserProfile.class), any(Action.class), any(RepositoryName.class), anyString()))
                .thenReturn(false);
        AuthorizedAction<Result> action = mock(AuthorizedAction.class);

        // when
        catchException(authorizationRunner).runAction(null, null, Action.ADD_LEVEL, null, action);

        // then
        assertThat(caughtException()).isInstanceOf(AuthorizationFailedException.class);
        verify(action, never()).perform();
    }

}
