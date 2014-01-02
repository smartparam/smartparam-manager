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
package org.smartparam.manager;

import org.smartparam.editor.editor.ParamEditor;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.EditableParameter;
import org.smartparam.editor.model.ParameterKey;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.audit.EventsLogger;
import org.smartparam.manager.authz.AuthorizationCheckpoint;
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.validation.BasicMessages;
import org.smartparam.manager.validation.Messages;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamManager implements ParamManager {

    private final ParamEditor paramEditor;

    private final ParamViewer paramViewer;

    private final EventsLogger eventsLogger;

    private final AuthorizationCheckpoint authorizationCheckpoint;

    public BasicParamManager(ParamEditor paramEditor, ParamViewer paramViewer,
            EventsLogger eventsLogger, AuthorizationCheckpoint authorizationCheckpoint) {
        this.paramEditor = paramEditor;
        this.paramViewer = paramViewer;
        this.eventsLogger = eventsLogger;
        this.authorizationCheckpoint = authorizationCheckpoint;
    }

    @Override
    public Messages createParameter(UserProfile responsible, RepositoryName in, Parameter newParameter) {
        boolean authorized = authorizationCheckpoint.authorize(responsible, Action.CREATE_PARAMETER, in, newParameter.getName());
        if (!authorized) {
            return BasicMessages.error("sp.authz.nonAuthorized", Action.CREATE_PARAMETER, responsible);
        }

        ParameterKey key = paramEditor.createParameter(in, newParameter);
        eventsLogger.logParameterCreation(responsible, in, key, newParameter);

        return BasicMessages.ok();
    }

    @Override
    public Messages updateParameter(UserProfile responsible, RepositoryName in, String parameterName, Parameter newState) {
        boolean authorized = authorizationCheckpoint.authorize(responsible, Action.UPDATE_PARAMETER, in, parameterName);
        if (!authorized) {
            return BasicMessages.error("sp.authz.nonAuthorized", Action.UPDATE_PARAMETER, responsible);
        }

        EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
        paramEditor.updateParameter(in, parameterName, newState);
        eventsLogger.logParameterChange(responsible, Action.UPDATE_PARAMETER, in, previousState.getKey(), previousState, newState);

        return BasicMessages.ok();
    }
}
