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
package org.smartparam.manager.audit.diff;

import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.editor.model.ParameterKey;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.manager.Action;
import org.smartparam.manager.JsonAdapter;
import org.smartparam.manager.TimestampProvider;
import org.smartparam.manager.audit.ParameterEventLogEntry;
import org.smartparam.manager.audit.EventLogEntryFactory;
import org.smartparam.manager.audit.ParameterEntryEventLogEntry;
import org.smartparam.manager.authz.UserProfile;

/**
 *
 * @author Adam Dubiel
 */
public class DiffEventLogEntryFactory implements EventLogEntryFactory {

    private final JsonAdapter jsonAdapter;

    private final TimestampProvider timestampProvider;

    public DiffEventLogEntryFactory(JsonAdapter jsonAdapter, TimestampProvider timestampProvider) {
        this.jsonAdapter = jsonAdapter;
        this.timestampProvider = timestampProvider;
    }

    @Override
    public Class<? extends ParameterEventLogEntry> produces() {
        return DiffParameterEventLogEntry.class;
    }

    @Override
    public ParameterEventLogEntry produceParameterCreationLog(UserProfile responsible, RepositoryName repository, ParameterKey key, Parameter initialState) {
        ParameterDiff parameterDiff = ParameterDiff.initialState(initialState);
        return produceEventLog(responsible, Action.CREATE_PARAMETER, repository, key, parameterDiff);
    }

    @Override
    public ParameterEventLogEntry produceParameterChangeLog(UserProfile responsible, Action action, RepositoryName repository, ParameterKey key, Parameter previousState, Parameter currentState) {
        ParameterDiff parameterDiff = new ParameterDiff(previousState, currentState);
        return produceEventLog(responsible, action, repository, key, parameterDiff);
    }

    @Override
    public ParameterEventLogEntry produceParameterDeletionLog(UserProfile responsible, RepositoryName repository, ParameterKey key, Parameter lastState) {
        ParameterDiff parameterDiff = ParameterDiff.lastKnownState(lastState);
        return produceEventLog(responsible, Action.DELETE_PARAMETER, repository, key, parameterDiff);
    }

    @Override
    public ParameterEntryEventLogEntry produceEntryCreationLog(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry initialState) {
        ParameterEntryDiff entryDiff = ParameterEntryDiff.initialState(initialState);
        return produceEventLog(responsible, Action.ADD_LEVEL, repository, key, entryKey, entryDiff);
    }

    @Override
    public ParameterEntryEventLogEntry produceEntryChangeLog(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry previousState, ParameterEntry currentState) {
        ParameterEntryDiff entryDiff = new ParameterEntryDiff(previousState, currentState);
        return produceEventLog(responsible, Action.UPDATE_ENTRY, repository, key, entryKey, entryDiff);
    }

    @Override
    public ParameterEntryEventLogEntry produceEntryDeletionLog(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry lastState) {
        ParameterEntryDiff entryDiff = ParameterEntryDiff.lastKnownState(lastState);
        return produceEventLog(responsible, Action.DELETE_ENTRY, repository, key, entryKey, entryDiff);
    }

    private ParameterEventLogEntry produceEventLog(UserProfile responsible, Action action, RepositoryName repository, ParameterKey key, ParameterDiff parameterDiff) {
        String serializedDiff = jsonAdapter.serialize(parameterDiff);
        return new DiffParameterEventLogEntry(timestampProvider.operationTimestamp(), repository, action, responsible, key, parameterDiff, serializedDiff);
    }

    private ParameterEntryEventLogEntry produceEventLog(UserProfile responsible, Action action, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntryDiff parameterEntryDiff) {
        String serializedDiff = jsonAdapter.serialize(parameterEntryDiff);
        return new DiffParameterEntryEventLogEntry(timestampProvider.operationTimestamp(), repository, action, responsible, key, entryKey, parameterEntryDiff, serializedDiff);
    }

}
