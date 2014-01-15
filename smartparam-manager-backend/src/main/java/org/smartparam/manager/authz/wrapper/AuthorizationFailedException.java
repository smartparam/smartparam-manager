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

import org.smartparam.editor.core.identity.RepositoryName;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.authz.UserProfile;

/**
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class AuthorizationFailedException extends RuntimeException {

    private final UserProfile responsible;

    private final RepositoryName repository;

    private final Action action;

    private final String parameterName;

    public AuthorizationFailedException(UserProfile responsible, RepositoryName repository, Action action, String parameterName) {
        this.responsible = responsible;
        this.repository = repository;
        this.action = action;
        this.parameterName = parameterName;
    }

    public Action action() {
        return action;
    }

    public RepositoryName repository() {
        return repository;
    }

    public String parameterName() {
        return parameterName;
    }

    public UserProfile responsible() {
        return responsible;
    }

}
