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
package org.smartparam.manager.authz;

import org.smartparam.engine.core.context.BaseParamContext;

/**
 *
 * @author Adam Dubiel
 */
class AuthorizationParamContext extends BaseParamContext {

    private final String action;

    private final UserProfile userProfile;

    AuthorizationParamContext(String action, UserProfile userProfile) {
        this.action = action;
        this.userProfile = userProfile;
    }

    UserProfile userProfile() {
        return userProfile;
    }

    String action() {
        return action;
    }
}
