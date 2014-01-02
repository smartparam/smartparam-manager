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

import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.editor.model.ParameterKey;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamEventsLogger implements ParamEventsLogger {

    private final EventLogRepository eventLogRepository;

    private final EventLogEntryFactory eventLogEntryFactory;

    public BasicParamEventsLogger(EventLogRepository eventLogRepository, EventLogEntryFactory eventLogEntryFactory) {
        checkCompatibility(eventLogRepository, eventLogEntryFactory);

        this.eventLogRepository = eventLogRepository;
        this.eventLogEntryFactory = eventLogEntryFactory;
    }

    private void checkCompatibility(EventLogRepository eventLogRepository, EventLogEntryFactory eventLogEntryFactory) {
        if (!eventLogRepository.supports(eventLogEntryFactory.produces())) {
            throw new EntryLogTypeNotSupported(eventLogRepository, eventLogEntryFactory, eventLogEntryFactory.produces());
        }
    }

    public void logParameterCreation(ParameterKey key, Parameter initialState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterCreationLog(key, initialState);
        eventLogRepository.save(entry);
    }

    public void logParameterChange(String action, ParameterKey key, Parameter previousState, Parameter currentState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterChangeLog(action, key, previousState, currentState);
        eventLogRepository.save(entry);
    }

    public void logParameterDeletion(ParameterKey parameterKey) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterDeletionLog(parameterKey);
        eventLogRepository.save(entry);
    }

    public void logEntryCreation(ParameterEntryKey key, ParameterEntry initialState) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryCreationLog(key, initialState);
        eventLogRepository.save(entry);
    }

    public void logEntryChange(ParameterEntryKey key, ParameterEntry previousState, ParameterEntry currentState) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryChangeLog(key, previousState, currentState);
        eventLogRepository.save(entry);
    }

    public void logEntryDeletion(ParameterEntryKey key) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryDeletionLog(key);
        eventLogRepository.save(entry);
    }
}
