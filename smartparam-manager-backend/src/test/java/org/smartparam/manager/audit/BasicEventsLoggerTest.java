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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.editor.model.map.ParameterEntryMap;
import org.smartparam.editor.model.simple.SimpleParameterEntryKey;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.authz.Action;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.fail;

/**
 *
 * @author Adam Dubiel
 */
public class BasicEventsLoggerTest {

    private EventLogRepository repository;

    private EventLogEntryFactory factory;

    private BasicEventsLogger logger;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        repository = mock(EventLogRepository.class);
        factory = mock(EventLogEntryFactory.class);

        when(repository.supports(any(Class.class))).thenReturn(true);
        logger = new BasicEventsLogger(repository, factory);
    }

    @Test
    public void shouldThrowUnsupportedEventTypeExceptionWhenLogRepositoryDoesNotSupportFactory() {
        // given
        doReturn(EventLogEntry.class).when(factory).produces();
        when(repository.supports(EventLogEntry.class)).thenReturn(false);

        // when
        try {
            new BasicEventsLogger(repository, factory);
            fail();
        } catch (EventLogEntryTypeNotSupported exception) {
            // then passed
        }
    }

    @Test
    public void shouldAskFactoryToProduceEventEntryAndSaveItWhenLoggingParameterCreation() {
        // when
        logger.logParameterCreation(null, null);

        // then
        verify(factory).produceParameterCreationLog(any(EventDescription.class), any(Parameter.class));
        verify(repository).save(any(EventLogEntry.class));
    }

    @Test
    public void shouldAskFactoryToProduceEventEntryAndSaveItWhenLoggingParameterChange() {
        // when
        logger.logParameterChange(null, Action.ADD_LEVEL, null, null);

        // then
        verify(factory).produceParameterChangeLog(any(EventDescription.class), eq(Action.ADD_LEVEL), any(Parameter.class), any(Parameter.class));
        verify(repository).save(any(EventLogEntry.class));
    }

    @Test
    public void shouldAskFactoryToProduceEventEntryAndSaveItWhenLoggingParameterDeleted() {
        // when
        logger.logParameterDeletion(null, null);

        // then
        verify(factory).produceParameterDeletionLog(any(EventDescription.class), any(Parameter.class));
        verify(repository).save(any(EventLogEntry.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSaveAsManyEventsAsEntriesCreated() {
        // given
        List<ParameterEntryKey> keys = new ArrayList<ParameterEntryKey>(Arrays.asList(new SimpleParameterEntryKey("1"), new SimpleParameterEntryKey("2")));
        List<ParameterEntryMap> entries = Arrays.asList(new ParameterEntryMap(), new ParameterEntryMap());

        // when
        logger.logEntryCreation(null, keys, entries);

        // then
        verify(factory, times(2)).produceEntryCreationLog(any(EventDescription.class), any(ParameterEntryKey.class), any(ParameterEntryMap.class));
        verify(repository).save(anyList());
    }

    @Test
    public void shouldAskFactoryToProduceEventEntryAndSaveItWhenLoggingEntryChange() {
        // when
        logger.logEntryChange(null, null, null, null);

        // then
        verify(factory).produceEntryChangeLog(any(EventDescription.class), any(ParameterEntryKey.class), any(ParameterEntryMap.class), any(ParameterEntryMap.class));
        verify(repository).save(any(EventLogEntry.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSaveAsManyEventsAsEntriesDeleted() {
        // given
        List<ParameterEntryKey> keys = new ArrayList<ParameterEntryKey>(Arrays.asList(new SimpleParameterEntryKey("1"), new SimpleParameterEntryKey("2")));
        List<ParameterEntryMap> entries = Arrays.asList(new ParameterEntryMap(), new ParameterEntryMap());

        // when
        logger.logEntryDeletion(null, keys, entries);

        // then
        verify(factory, times(2)).produceEntryDeletionLog(any(EventDescription.class), any(ParameterEntryKey.class), any(ParameterEntryMap.class));
        verify(repository).save(anyList());
    }
}
