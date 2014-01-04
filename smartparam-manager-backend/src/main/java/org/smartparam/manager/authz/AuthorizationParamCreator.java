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

import org.smartparam.editor.editor.ParamEditor;
import org.smartparam.editor.model.simple.SimpleLevel;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.model.simple.SimpleParameterEntry;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.engine.types.bool.BooleanType;
import org.smartparam.engine.types.string.StringType;
import org.smartparam.manager.config.ManagerConfig;

/**
 *
 * @author Adam Dubiel
 */
public class AuthorizationParamCreator {

    private final ManagerConfig managerConfig;

    private final ParamViewer paramViewer;

    private final ParamEditor paramEditor;

    public AuthorizationParamCreator(ManagerConfig managerConfig, ParamViewer paramViewer, ParamEditor paramEditor) {
        this.managerConfig = managerConfig;
        this.paramViewer = paramViewer;
        this.paramEditor = paramEditor;
    }

    public void createNonExisting() {
        createLoginAuthzParameter();
        createRoleAuthzParameter();
    }

    private void createLoginAuthzParameter() {
        if (!paramViewer.parameterExists(ParamAuthorizationCheckpoint.LOGIN_AUTHZ_PARAMETER)) {
            SimpleParameter parameter = new SimpleParameter()
                    .withName(ParamAuthorizationCheckpoint.LOGIN_AUTHZ_PARAMETER)
                    .withInputLevels(3)
                    .withLevel(new SimpleLevel().withName("login").withType(StringType.TYPE_NAME).withLevelCreator(AuthorizationLevelCreators.LOGIN_LEVEL_CREATOR))
                    .withLevel(new SimpleLevel().withName("action").withType(StringType.TYPE_NAME).withLevelCreator(AuthorizationLevelCreators.ACTION_LEVEL_CREATOR))
                    .withLevel(new SimpleLevel().withName("parameter").withType(StringType.TYPE_NAME).withLevelCreator(AuthorizationLevelCreators.PARAMETER_LEVEL_CREATOR))
                    .withLevel(new SimpleLevel().withName("authorized").withType(BooleanType.TYPE_NAME));

            paramEditor.createParameter(managerConfig.defaultWriteRepository(), parameter);
            paramEditor.addEntry(managerConfig.defaultWriteRepository(), ParamAuthorizationCheckpoint.LOGIN_AUTHZ_PARAMETER, new SimpleParameterEntry("*", "*", "*", "true"));
        }
    }

    private void createRoleAuthzParameter() {
        if (!paramViewer.parameterExists(ParamAuthorizationCheckpoint.ROLE_AUTHZ_PARAMETER)) {
            SimpleParameter parameter = new SimpleParameter()
                    .withName(ParamAuthorizationCheckpoint.ROLE_AUTHZ_PARAMETER)
                    .withInputLevels(3)
                    .withLevel(new SimpleLevel().withName("role").withType(StringType.TYPE_NAME).withLevelCreator(AuthorizationLevelCreators.ROLE_LEVEL_CREATOR))
                    .withLevel(new SimpleLevel().withName("action").withType(StringType.TYPE_NAME).withLevelCreator(AuthorizationLevelCreators.ACTION_LEVEL_CREATOR))
                    .withLevel(new SimpleLevel().withName("parameter").withType(StringType.TYPE_NAME).withLevelCreator(AuthorizationLevelCreators.PARAMETER_LEVEL_CREATOR))
                    .withLevel(new SimpleLevel().withName("authorized").withType(BooleanType.TYPE_NAME));

            paramEditor.createParameter(managerConfig.defaultWriteRepository(), parameter);
            paramEditor.addEntry(managerConfig.defaultWriteRepository(), ParamAuthorizationCheckpoint.LOGIN_AUTHZ_PARAMETER, new SimpleParameterEntry("*", "*", "*", "true"));
        }
    }
}
