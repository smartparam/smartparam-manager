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

import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.adapter.JsonAdapter;
import org.smartparam.manager.time.TimeProvider;
import org.smartparam.manager.audit.EventDescription;
import org.smartparam.manager.audit.EventLogEntry;
import org.smartparam.manager.audit.EventLogEntryFactory;

/**
 *
 * @author Adam Dubiel
 */
public class DiffEventLogEntryFactory implements EventLogEntryFactory {

    private final JsonAdapter jsonAdapter;

    private final TimeProvider timestampProvider;

    public DiffEventLogEntryFactory(JsonAdapter jsonAdapter, TimeProvider timestampProvider) {
        this.jsonAdapter = jsonAdapter;
        this.timestampProvider = timestampProvider;
    }

    @Override
    public Class<? extends EventLogEntry> produces() {
        return DiffEventLogEntry.class;
    }

    @Override
    public EventLogEntry produceParameterCreationLog(EventDescription description, Parameter initialState) {
        ParameterDiff parameterDiff = ParameterDiff.initialState(initialState);
        return produceEventLog(description, Action.CREATE_PARAMETER, parameterDiff);
    }

    @Override
    public EventLogEntry produceParameterChangeLog(EventDescription description, Action action, Parameter previousState, Parameter currentState) {
        ParameterDiff parameterDiff = new ParameterDiff(previousState, currentState);
        return produceEventLog(description, action, parameterDiff);
    }

    @Override
    public EventLogEntry produceParameterDeletionLog(EventDescription description, Parameter lastState) {
        ParameterDiff parameterDiff = ParameterDiff.lastKnownState(lastState);
        return produceEventLog(description, Action.DELETE_PARAMETER, parameterDiff);
    }

    @Override
    public EventLogEntry produceEntryCreationLog(EventDescription description, ParameterEntryKey entryKey, ParameterEntryMap initialState) {
        ParameterEntryDiff entryDiff = ParameterEntryDiff.initialState(initialState);
        return produceEventLog(description, Action.ADD_ENTRY, entryKey, entryDiff);
    }

    @Override
    public EventLogEntry produceEntryChangeLog(EventDescription description, ParameterEntryKey entryKey, ParameterEntryMap previousState, ParameterEntryMap currentState) {
        ParameterEntryDiff entryDiff = new ParameterEntryDiff(previousState, currentState);
        return produceEventLog(description, Action.UPDATE_ENTRY, entryKey, entryDiff);
    }

    @Override
    public EventLogEntry produceEntryDeletionLog(EventDescription description, ParameterEntryKey entryKey, ParameterEntryMap lastState) {
        ParameterEntryDiff entryDiff = ParameterEntryDiff.lastKnownState(lastState);
        return produceEventLog(description, Action.DELETE_ENTRY, entryKey, entryDiff);
    }

    private EventLogEntry produceEventLog(EventDescription description, Action action, ParameterDiff parameterDiff) {
        String serializedDiff = jsonAdapter.serialize(parameterDiff);
        return DiffEventLogEntry.parameterEvent(timestampProvider.operationTimestamp(), description, action, parameterDiff, serializedDiff);
    }

    private EventLogEntry produceEventLog(EventDescription description, Action action, ParameterEntryKey entryKey, ParameterEntryDiff parameterEntryDiff) {
        String serializedDiff = jsonAdapter.serialize(parameterEntryDiff);
        return DiffEventLogEntry.entryEvent(timestampProvider.operationTimestamp(), description, action, entryKey, parameterEntryDiff, serializedDiff);
    }

}
