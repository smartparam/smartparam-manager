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

import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.manager.authz.Action;
import org.smartparam.manager.audit.AbstractEventLogEntry;
import org.smartparam.manager.audit.EventDescription;

/**
 *
 * @author Adam Dubiel
 */
public final class DiffEventLogEntry extends AbstractEventLogEntry<Diff<?>> {

    static DiffEventLogEntry parameterEvent(long timestamp,
            EventDescription description, Action action,
            ParameterDiff stateDiff, String serializedDiff) {
        return new DiffEventLogEntry(timestamp, description, action, stateDiff, serializedDiff);
    }

    static DiffEventLogEntry entryEvent(long timestamp,
            EventDescription description, Action action, ParameterEntryKey parameterEntryKey,
            ParameterEntryDiff stateDiff, String serializedDiff) {
        return new DiffEventLogEntry(timestamp, description, action, parameterEntryKey, stateDiff, serializedDiff);
    }

    private DiffEventLogEntry(long timestamp,
            EventDescription description, Action action,
            ParameterDiff stateDiff, String serializedDiff) {
        super(timestamp, description, action, stateDiff, serializedDiff);
    }

    private DiffEventLogEntry(long timestamp,
            EventDescription description, Action action, ParameterEntryKey entryKey,
            ParameterEntryDiff stateDiff, String serializedDiff) {
        super(timestamp, description, action, entryKey, stateDiff, serializedDiff);
    }

}
