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
import java.util.Collections;
import java.util.List;
import org.smartparam.manager.authz.Action;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryEventLogRepository implements EventLogRepository {

    private final List<EventLogEntry> repository = new ArrayList<EventLogEntry>();

    @Override
    public boolean supports(Class<? extends EventLogEntry> entryClass) {
        return true;
    }

    @Override
    public List<EventLogEntry> list(EventLogFilters filters) {
        return Collections.unmodifiableList(repository);
    }

    @Override
    public void save(EventLogEntry eventLogEntry) {
        repository.add(eventLogEntry);
    }

    @Override
    public void save(List<EventLogEntry> eventLogEntries) {
        repository.addAll(eventLogEntries);
    }

    public void clear() {
        repository.clear();
    }

    public List<EventLogEntry> listAll() {
        return Collections.unmodifiableList(repository);
    }

    public EventLogEntry findFirstEvent(Action action) {
        for (EventLogEntry entry : repository) {
            if (entry.action() == action) {
                return entry;
            }
        }
        return null;
    }
}
