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

import java.util.EnumMap;
import java.util.Map;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.manager.Action;

/**
 *
 * @author Adam Dubiel
 */
public class ParamAuthorizationCheckpoint implements AuthorizationCheckpoint {

    static final String LOGIN_AUTHZ_PARAMETER = "sp.manager.authz.login";

    static final String ROLE_AUTHZ_PARAMETER = "sp.manager.authz.role";

    static final String PERMISSION_AUTHZ_PARAMETER = "sp.manager.authz.permission";

    private static final Map<AuthorizationMethod, String> METHOD_PARAMETER = new EnumMap<AuthorizationMethod, String>(AuthorizationMethod.class);

    static {
        METHOD_PARAMETER.put(AuthorizationMethod.LOGIN, LOGIN_AUTHZ_PARAMETER);
        METHOD_PARAMETER.put(AuthorizationMethod.ROLE, ROLE_AUTHZ_PARAMETER);
//        METHOD_PARAMETER.put(AuthorizationMethod.PERMISSION, PERMISSION_AUTHZ_PARAMETER);
    }

    private final ParamEngine paramEngine;

    private final AuthorizationConfig authorizationConfig;

    public ParamAuthorizationCheckpoint(ParamEngine paramEngine, AuthorizationConfig authorizationConfig) {
        this.paramEngine = paramEngine;
        this.authorizationConfig = authorizationConfig;
    }

    @Override
    public boolean authorize(UserProfile userProfile, Action action, RepositoryName repository, String parameter) {
        boolean authorized;
        for (AuthorizationMethod method : authorizationConfig) {
            authorized = authorizeUsing(method, userProfile, action, repository, parameter);
            if (authorized) {
                return true;
            }
        }
        return false;
    }

    private boolean authorizeUsing(AuthorizationMethod method, UserProfile profile, Action action, RepositoryName repository, String parameter) {
        String parameterName = METHOD_PARAMETER.get(method);
        return paramEngine.get(parameterName, new AuthorizationParamContext(parameter, action, profile)).getHolder().booleanValue();
    }
}
