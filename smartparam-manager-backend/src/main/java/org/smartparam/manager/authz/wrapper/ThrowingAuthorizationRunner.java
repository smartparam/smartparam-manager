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

/**
 *
 * @author Adam Dubiel
 */
public class ThrowingAuthorizationRunner implements AuthorizationRunner {

    private final AuthorizationCheckpoint checkpoint;

    public ThrowingAuthorizationRunner(AuthorizationCheckpoint checkpoint) {
        this.checkpoint = checkpoint;
    }

    @Override
    public <T extends Result> T runAction(UserProfile responsible, RepositoryName in, Action action, String parameterName, AuthorizedAction<T> afterAuthorizationAction) {
        boolean authorized = checkpoint.authorize(responsible, action, in, parameterName);
        if (!authorized) {
            throw new AuthorizationFailedException(responsible, in, action, parameterName);
        }
        return afterAuthorizationAction.perform();
    }

}
