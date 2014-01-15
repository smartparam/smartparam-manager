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

import java.util.List;
import org.smartparam.editor.core.model.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.authz.Action;

/**
 *
 * @author Adam Dubiel
 */
public class DisabledEventsLogger implements EventsLogger {

    @Override
    public void logParameterCreation(EventDescription description, Parameter initialState) {
    }

    @Override
    public void logParameterChange(EventDescription description, Action action, Parameter previousState, Parameter currentState) {
    }

    @Override
    public void logParameterDeletion(EventDescription description, Parameter lastState) {
    }

    @Override
    public void logEntryCreation(EventDescription description, List<ParameterEntryKey> entryKey, List<ParameterEntryMap> initialState) {
    }

    @Override
    public void logEntryChange(EventDescription description, ParameterEntryKey entryKey, ParameterEntryMap previousState, ParameterEntryMap currentState) {
    }

    @Override
    public void logEntryDeletion(EventDescription description, List<ParameterEntryKey> entryKey, List<ParameterEntryMap> lastState) {
    }

}
