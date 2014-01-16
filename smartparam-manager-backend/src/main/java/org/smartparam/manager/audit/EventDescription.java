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
package org.smartparam.manager.audit;

import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.ParameterKey;
import org.smartparam.manager.authz.UserProfile;

/**
 *
 * @author Adam Dubiel
 */
public class EventDescription {

    private final String responsibleLogin;

    private final RepositoryName repository;

    private final ParameterKey parameterKey;

    public EventDescription(UserProfile responsible, RepositoryName repository, ParameterKey parameterKey) {
        this.responsibleLogin = responsible.login();
        this.repository = repository;
        this.parameterKey = parameterKey;
    }

    public String responsibleLogin() {
        return responsibleLogin;
    }

    public RepositoryName repository() {
        return repository;
    }

    public ParameterKey parameterKey() {
        return parameterKey;
    }
}
