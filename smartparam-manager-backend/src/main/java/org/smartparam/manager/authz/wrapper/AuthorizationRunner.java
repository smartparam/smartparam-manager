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
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.result.Result;

/**
 *
 * @author Adam Dubiel
 */
public interface AuthorizationRunner {

    <T extends Result> T runAction(UserProfile responsible, RepositoryName in, Action action, String parameterName, AuthorizedAction<T> afterAuthorizationAction);

}
