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
    public void logParameterCreation(RepositoryName repository, UserProfile responsible, ParameterKey key, Parameter initialState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterCreationLog(repository, responsible, key, initialState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logParameterChange(RepositoryName repository, UserProfile responsible, String action, ParameterKey key, Parameter previousState, Parameter currentState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterChangeLog(repository, responsible, action, key, previousState, currentState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logParameterDeletion(RepositoryName repository, UserProfile responsible, ParameterKey parameterKey) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterDeletionLog(repository, responsible, parameterKey);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryCreation(RepositoryName repository, UserProfile responsible, ParameterEntryKey key, ParameterEntry initialState) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryCreationLog(repository, responsible, key, initialState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryChange(RepositoryName repository, UserProfile responsible, ParameterEntryKey key, ParameterEntry previousState, ParameterEntry currentState) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryChangeLog(repository, responsible, key, previousState, currentState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryDeletion(RepositoryName repository, UserProfile responsible, ParameterEntryKey key) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryDeletionLog(repository, responsible, key);
        eventLogRepository.save(entry);
    }
}
