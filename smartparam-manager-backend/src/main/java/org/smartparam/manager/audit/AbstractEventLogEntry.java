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
import org.smartparam.engine.core.parameter.ParameterEntryKey;
import org.smartparam.engine.core.parameter.ParameterKey;
import org.smartparam.manager.authz.Action;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractEventLogEntry<T> implements EventLogEntry {

    private final long timestamp;

    private final EventLogEntryType type;

    private final RepositoryName repository;

    private final Action action;

    private final String responsibleLogin;

    private final ParameterKey parameterKey;

    private final ParameterEntryKey entryKey;

    private final T eventDetails;

    private final String serializedEventDetails;

    protected AbstractEventLogEntry(long timestamp,
            RepositoryName repository, Action action, String responsibleLogin,
            ParameterKey parameterKey, ParameterEntryKey parameterEntryKey,
            T eventDetails, String serializedEventDetails) {
        this.timestamp = timestamp;
        this.repository = repository;
        this.action = action;
        this.responsibleLogin = responsibleLogin;
        this.parameterKey = parameterKey;
        this.entryKey = parameterEntryKey;
        this.eventDetails = eventDetails;
        this.serializedEventDetails = serializedEventDetails;

        this.type = parameterEntryKey != null ? EventLogEntryType.ENTRY : EventLogEntryType.PARAMETER;
    }

    protected AbstractEventLogEntry(long timestamp,
            EventDescription description, Action action, ParameterEntryKey parameterEntryKey,
            T eventDetails, String serializedEventDetails) {
        this(timestamp, description.repository(), action, description.responsibleLogin(), description.parameterKey(),
                parameterEntryKey, eventDetails, serializedEventDetails);
    }

    protected AbstractEventLogEntry(long timestamp,
            EventDescription description, Action action,
            T eventDetails, String serializedEventDetails) {
        this(timestamp, description, action, null, eventDetails, serializedEventDetails);
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public EventLogEntryType type() {
        return type;
    }

    @Override
    public RepositoryName repository() {
        return repository;
    }

    @Override
    public Action action() {
        return action;
    }

    @Override
    public String responsibleLogin() {
        return responsibleLogin;
    }

    @Override
    public ParameterKey parameterKey() {
        return parameterKey;
    }

    @Override
    public ParameterEntryKey entryKey() {
        return entryKey;
    }

    @Override
    public T eventDetails() {
        return eventDetails;
    }

    @Override
    public String serializedEventDetails() {
        return serializedEventDetails;
    }

}
