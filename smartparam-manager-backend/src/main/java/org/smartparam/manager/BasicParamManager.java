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

import org.smartparam.manager.authz.Action;
import java.util.Arrays;
import java.util.List;
import org.smartparam.editor.editor.ParamEditor;
import org.smartparam.editor.identity.DescribedCollection;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.EditableParameter;
import org.smartparam.editor.model.LevelKey;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.editor.model.ParameterKey;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.manager.audit.EventDescription;
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
    public Messages createParameter(final UserProfile responsible, final RepositoryName in, final Parameter newParameter) {
        return authorizedAction(responsible, in, Action.CREATE_PARAMETER, newParameter.getName(), new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                ParameterKey key = paramEditor.createParameter(in, newParameter);
                eventsLogger.logParameterCreation(new EventDescription(responsible, in, key), newParameter);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages updateParameter(final UserProfile responsible, final RepositoryName in, final String parameterName, final Parameter newState) {
        return authorizedAction(responsible, in, Action.UPDATE_PARAMETER, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                paramEditor.updateParameter(in, parameterName, newState);
                eventsLogger.logParameterChange(new EventDescription(responsible, in, previousState.getKey()), Action.UPDATE_PARAMETER, previousState, newState);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages deleteParameter(final UserProfile responsible, final RepositoryName in, final String parameterName) {
        return authorizedAction(responsible, in, Action.UPDATE_PARAMETER, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                paramEditor.deleteParameter(in, parameterName);
                eventsLogger.logParameterDeletion(new EventDescription(responsible, in, previousState.getKey()), previousState);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages addLevel(final UserProfile responsible, final RepositoryName in, final String parameterName, final Level level) {
        final Action action = Action.ADD_LEVEL;

        return authorizedAction(responsible, in, action, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                paramEditor.addLevel(in, parameterName, level);
                Parameter currentState = paramViewer.getParameterMetadata(in, parameterName).data();
                eventsLogger.logParameterChange(new EventDescription(responsible, in, previousState.getKey()), action, previousState, currentState);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages reorderLevels(final UserProfile responsible, final RepositoryName in, final String parameterName, final List<LevelKey> levels) {
        final Action action = Action.REOREDER_LEVELS;

        return authorizedAction(responsible, in, action, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                paramEditor.reorderLevels(in, parameterName, levels);
                Parameter currentState = paramViewer.getParameterMetadata(in, parameterName).data();
                eventsLogger.logParameterChange(new EventDescription(responsible, in, previousState.getKey()), action, previousState, currentState);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages updateLevel(final UserProfile responsible, final RepositoryName in, final String parameterName, final LevelKey levelKey, final Level level) {
        final Action action = Action.UPDATE_LEVEL;

        return authorizedAction(responsible, in, action, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                paramEditor.updateLevel(in, parameterName, levelKey, level);
                Parameter currentState = paramViewer.getParameterMetadata(in, parameterName).data();
                eventsLogger.logParameterChange(new EventDescription(responsible, in, previousState.getKey()), action, previousState, currentState);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages deleteLevel(final UserProfile responsible, final RepositoryName in, final String parameterName, final LevelKey levelKey) {
        final Action action = Action.DELETE_LEVEL;

        return authorizedAction(responsible, in, action, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter previousState = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                paramEditor.deleteLevel(in, parameterName, levelKey);
                Parameter currentState = paramViewer.getParameterMetadata(in, parameterName).data();
                eventsLogger.logParameterChange(new EventDescription(responsible, in, previousState.getKey()), action, previousState, currentState);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages addEntries(final UserProfile responsible, final RepositoryName in, final String parameterName, final List<ParameterEntry> entries) {
        return authorizedAction(responsible, in, Action.ADD_ENTRY, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                DescribedCollection<ParameterEntryKey> entryKeys = paramEditor.addEntries(in, parameterName, entries);
                EditableParameter parameterMetadata = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();

                eventsLogger.logEntryCreation(new EventDescription(responsible, in, parameterMetadata.getKey()), entryKeys.itemsList(), entries);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages updateEntry(final UserProfile responsible, final RepositoryName in, final String parameterName, final ParameterEntryKey entryKey, final ParameterEntry entry) {
        return authorizedAction(responsible, in, Action.UPDATE_ENTRY, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter parameterMetadata = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                ParameterEntry previousState = paramViewer.getParameterEntries(in, parameterName, Arrays.asList(entryKey)).firstItem();
                paramEditor.updateEntry(in, parameterName, entryKey, entry);

                eventsLogger.logEntryChange(new EventDescription(responsible, in, parameterMetadata.getKey()), entryKey, previousState, entry);

                return BasicMessages.ok();
            }
        });
    }

    @Override
    public Messages deleteEntries(final UserProfile responsible, final RepositoryName in, final String parameterName, final List<ParameterEntryKey> entryKeys) {
        return authorizedAction(responsible, in, Action.UPDATE_ENTRY, parameterName, new AuthorizedActionWrapper() {
            @Override
            public BasicMessages perform() {
                EditableParameter parameterMetadata = (EditableParameter) paramViewer.getParameterMetadata(in, parameterName).data();
                DescribedCollection<ParameterEntry> previousState = paramViewer.getParameterEntries(in, parameterName, entryKeys);
                paramEditor.deleteEntries(in, parameterName, entryKeys);

                eventsLogger.logEntryDeletion(new EventDescription(responsible, in, parameterMetadata.getKey()), entryKeys, previousState.itemsList());

                return BasicMessages.ok();
            }
        });
    }

    private BasicMessages authorizedAction(UserProfile responsible, RepositoryName in, Action action, String parameterName, AuthorizedActionWrapper wrapper) {
        boolean authorized = authorizationCheckpoint.authorize(responsible, action, in, parameterName);
        if (!authorized) {
            return BasicMessages.error("sp.authz.nonAuthorized", action, responsible);
        }
        return wrapper.perform();
    }

    private interface AuthorizedActionWrapper {

        BasicMessages perform();
    }
}
