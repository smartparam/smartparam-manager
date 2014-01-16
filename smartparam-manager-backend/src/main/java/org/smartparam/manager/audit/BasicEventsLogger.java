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

import java.util.LinkedList;
import java.util.List;
import org.smartparam.engine.core.parameter.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.authz.Action;

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
            throw new EventLogEntryTypeNotSupported(eventLogRepository, eventLogEntryFactory, eventLogEntryFactory.produces());
        }
    }

    @Override
    public void logParameterCreation(EventDescription description, Parameter initialState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterCreationLog(description, initialState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logParameterChange(EventDescription description, Action action, Parameter previousState, Parameter currentState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterChangeLog(description, action, previousState, currentState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logParameterDeletion(EventDescription description, Parameter lastState) {
        EventLogEntry entry = eventLogEntryFactory.produceParameterDeletionLog(description, lastState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryCreation(EventDescription description, List<ParameterEntryKey> entryKeys, List<ParameterEntryMap> initialStates) {
        List<EventLogEntry> entries = new LinkedList<EventLogEntry>();

        int index = 0;
        for (ParameterEntryKey key : entryKeys) {
            entries.add(eventLogEntryFactory.produceEntryCreationLog(description, key, initialStates.get(index)));
            index++;
        }

        eventLogRepository.save(entries);
    }

    @Override
    public void logEntryChange(EventDescription description, ParameterEntryKey entryKey, ParameterEntryMap previousState, ParameterEntryMap currentState) {
        EventLogEntry entry = eventLogEntryFactory.produceEntryChangeLog(description, entryKey, previousState, currentState);
        eventLogRepository.save(entry);
    }

    @Override
    public void logEntryDeletion(EventDescription description, List<ParameterEntryKey> entryKeys, List<ParameterEntryMap> lastStates) {
        List<EventLogEntry> entries = new LinkedList<EventLogEntry>();

        int index = 0;
        for (ParameterEntryKey key : entryKeys) {
            entries.add(eventLogEntryFactory.produceEntryDeletionLog(description, key, lastStates.get(index)));
            index++;
        }

        eventLogRepository.save(entries);
    }

}
