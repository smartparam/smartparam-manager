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

import org.smartparam.editor.core.identity.RepositoryName;
import org.smartparam.editor.core.model.ParameterEntryKey;
import org.smartparam.editor.core.model.ParameterKey;
import org.smartparam.editor.model.simple.SimpleParameterEntryKey;
import org.smartparam.editor.model.simple.SimpleParameterKey;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.authz.UserProfile;

/**
 *
 * @author Adam Dubiel
 */
public class EventLogEntryTestBuilder {

    private long timestamp;

    private RepositoryName repository;

    private Action action;

    private String responsibleLogin;

    private ParameterKey parameterKey;

    private ParameterEntryKey parameterEntryKey;

    private Object eventDetails;

    private String serializedEventDetails;

    private EventLogEntryTestBuilder() {
    }

    public static EventLogEntryTestBuilder parameterEventLogEntry() {
        return new EventLogEntryTestBuilder();
    }

    public EventLogEntry build() {
        return new FakeEventLogEntry(timestamp, repository, action, responsibleLogin, parameterKey, parameterEntryKey, eventDetails, serializedEventDetails);
    }

    public EventLogEntryTestBuilder withTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public EventLogEntryTestBuilder withRepository(String repository) {
        this.repository = new RepositoryName(repository);
        return this;
    }

    public EventLogEntryTestBuilder withAction(Action action) {
        this.action = action;
        return this;
    }

    public EventLogEntryTestBuilder responsible(String responsibleLogin) {
        this.responsibleLogin = responsibleLogin;
        return this;
    }

    public EventLogEntryTestBuilder forParameter(String key) {
        this.parameterKey = new SimpleParameterKey(key);
        return this;
    }

    public EventLogEntryTestBuilder forParameterEntry(String parameterKey, String entryKey) {
        this.parameterKey = new SimpleParameterKey(parameterKey);
        this.parameterEntryKey = new SimpleParameterEntryKey(entryKey);
        return this;
    }

    public EventLogEntryTestBuilder withDetails(Object eventDetails) {
        this.eventDetails = eventDetails;
        return this;
    }

    public EventLogEntryTestBuilder withSerializedDetails(String serializedEventDetails) {
        this.serializedEventDetails = serializedEventDetails;
        return this;
    }

    private class FakeEventLogEntry extends AbstractEventLogEntry<Object> {

        FakeEventLogEntry(long timestamp,
                RepositoryName repository, Action action, String responsibleLogin,
                ParameterKey parameterKey, ParameterEntryKey entryKey,
                Object eventDetails, String serializedEventDetails) {
            super(timestamp, new EventDescription(new UserProfile(responsibleLogin, "ROOT"), repository, parameterKey),
                    action, entryKey, eventDetails, serializedEventDetails);
        }

    }
}
