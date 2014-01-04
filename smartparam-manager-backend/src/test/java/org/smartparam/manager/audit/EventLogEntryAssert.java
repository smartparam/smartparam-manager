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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.smartparam.manager.authz.Action;

/**
 *
 * @author Adam Dubiel
 */
public class EventLogEntryAssert extends AbstractAssert<EventLogEntryAssert, EventLogEntry> {

    private EventLogEntryAssert(EventLogEntry actual) {
        super(actual, EventLogEntryAssert.class);
    }

    public static EventLogEntryAssert assertThat(EventLogEntry actual) {
        return new EventLogEntryAssert(actual);
    }

    public EventLogEntryAssert hasTimestamp(long timestamp) {
        Assertions.assertThat(actual.timestamp()).isEqualTo(timestamp);
        return this;
    }

    public EventLogEntryAssert hasRepository(String repository) {
        Assertions.assertThat(actual.repository().name()).isEqualTo(repository);
        return this;
    }

    public EventLogEntryAssert hasAction(Action action) {
        Assertions.assertThat(actual.action()).isEqualTo(action);
        return this;
    }

    public EventLogEntryAssert hasResponsible(String responsible) {
        Assertions.assertThat(actual.responsibleLogin()).isEqualTo(responsible);
        return this;
    }

    public EventLogEntryAssert forParmeter(String parameterKey) {
        Assertions.assertThat(actual.parameterKey().value()).isEqualTo(parameterKey);
        Assertions.assertThat(actual.type()).isEqualTo(EventLogEntryType.PARAMETER);
        return this;
    }

    public EventLogEntryAssert forParameterEntry(String parameterKey, String parameterEntryKey) {
        Assertions.assertThat(actual.parameterKey().value()).isEqualTo(parameterKey);
        Assertions.assertThat(actual.entryKey().value()).isEqualTo(parameterKey);
        Assertions.assertThat(actual.type()).isEqualTo(EventLogEntryType.ENTRY);
        return this;
    }

    public EventLogEntryAssert hasEventDetails(Object eventDetails) {
        Assertions.assertThat(actual.eventDetails()).isEqualTo(eventDetails);
        return this;
    }

    public EventLogEntryAssert hasSerializedEventDetails(String serializedDetails) {
        Assertions.assertThat(actual.serializedEventDetails()).isEqualTo(serializedDetails);
        return this;
    }
}
