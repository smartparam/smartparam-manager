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
public interface EventsLogger {

    void logParameterCreation(UserProfile responsible, RepositoryName repository, ParameterKey key, Parameter initialState);

    void logParameterChange(UserProfile responsible, Action action, RepositoryName repository, ParameterKey key, Parameter previousState, Parameter currentState);

    void logParameterDeletion(UserProfile responsible, RepositoryName repository, ParameterKey key, Parameter lastState);

    void logEntryCreation(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry initialState);

    void logEntryChange(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry previousState, ParameterEntry currentState);

    void logEntryDeletion(UserProfile responsible, RepositoryName repository, ParameterKey key, ParameterEntryKey entryKey, ParameterEntry lastState);

}
