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

import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.editor.model.ParameterKey;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.manager.Action;
import org.smartparam.manager.authz.UserProfile;

/**
 *
 * @author Adam Dubiel
 */
public class BasicEventsLogger implements EventsLogger {

    private final EventLogRepository eventLogRepository;

    private final EventLogEntryFactory eventLogEntryFactory;

    public BasicEventsLogger(EventLogRepository eventLogRepository, EventLogEntryFactory eventLogEntryFactory) {
        checkCompatibility(eventLogRepository, eventLogEntryFactory);

        this.eventLogRepository = eventLogRepository;
        this.eventLogEntryFactory = eventLogEntryFactory;
    }

    private void checkCompatibility(EventLogRepository eventLogRepository, EventLogEntryFactory eventLogEntryFactory) {
        if (!eventLogRepository.supports(eventLogEntryFactory.produces())) {
            throw new EntryLogTypeNotSupported(eventLogRepository, eventLogEntryFactory, eventLogEntryFactory.produces());
        }
    }

    @Override
    public void logParameterCreation(UserProfile responsible, RepositoryName repository, ParameterKey key, Parameter initialState) {
        ParameterEventLogEntry entry = eventLogEntryFactory.produceParameterCreationLog(responsible, repository, key, initialState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logParameterChange(UserProfile responsible, Action action, RepositoryName repository, ParameterKey key, Parameter previousState, Parameter currentState) {
        ParameterEventLogEntry entry = eventLogEntryFactory.produceParameterChangeLog(responsible, action, repository, key, previousState, currentState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logParameterDeletion(UserProfile responsible, RepositoryName repository, ParameterKey key, Parameter lastState) {
        ParameterEventLogEntry entry = eventLogEntryFactory.produceParameterDeletionLog(responsible, repository, key, lastState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryCreation(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry initialState) {
        ParameterEventLogEntry entry = eventLogEntryFactory.produceEntryCreationLog(responsible, repository, key, entryKey, initialState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryChange(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry previousState, ParameterEntry currentState) {
        ParameterEventLogEntry entry = eventLogEntryFactory.produceEntryChangeLog(responsible, repository, key, entryKey, previousState, currentState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryDeletion(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry lastState) {
        ParameterEventLogEntry entry = eventLogEntryFactory.produceEntryDeletionLog(responsible, repository, key, entryKey, lastState);
        eventLogRepository.save(entry);
    }
}
