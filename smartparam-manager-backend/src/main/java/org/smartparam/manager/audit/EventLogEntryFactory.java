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

import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.authz.Action;

/**
 *
 * @author Adam Dubiel
 */
public interface EventLogEntryFactory {

    Class<? extends EventLogEntry> produces();

    EventLogEntry produceParameterCreationLog(EventDescription description, Parameter initialState);

    EventLogEntry produceParameterChangeLog(EventDescription description, Action action, Parameter previousState, Parameter currentState);

    EventLogEntry produceParameterDeletionLog(EventDescription description, Parameter lastState);

    EventLogEntry produceEntryCreationLog(EventDescription description, ParameterEntryKey entryKey, MapEntry initialState);

    EventLogEntry produceEntryChangeLog(EventDescription description, ParameterEntryKey entryKey, MapEntry previousState, MapEntry currentState);

    EventLogEntry produceEntryDeletionLog(EventDescription description, ParameterEntryKey entryKey, MapEntry lastState);

}
