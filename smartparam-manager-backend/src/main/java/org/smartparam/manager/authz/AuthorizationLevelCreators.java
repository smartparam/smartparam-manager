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

import org.smartparam.engine.annotated.annotations.JavaPlugin;

/**
 *
 * @author Adam Dubiel
 */
class AuthorizationLevelCreators {

    static final String LOGIN_LEVEL_CREATOR = "sp.manager.authz.login";

    static final String ROLE_LEVEL_CREATOR = "sp.manager.authz.role";

    static final String ACTION_LEVEL_CREATOR = "sp.manager.authz.action";

    static final String PARAMETER_LEVEL_CREATOR = "sp.manager.authz.parameter";

    private AuthorizationLevelCreators() {
    }

    @JavaPlugin(LOGIN_LEVEL_CREATOR)
    String login(AuthorizationParamContext context) {
        return context.userProfile().login();
    }

    @JavaPlugin(ROLE_LEVEL_CREATOR)
    String role(AuthorizationParamContext context) {
        return context.userProfile().role();
    }

    @JavaPlugin(PARAMETER_LEVEL_CREATOR)
    String parameter(AuthorizationParamContext context) {
        return context.parameter();
    }

    @JavaPlugin(ACTION_LEVEL_CREATOR)
    String action(AuthorizationParamContext context) {
        return context.action().name();
    }
}
