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
package org.smartparam.manager;

import java.util.List;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.LevelKey;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.validation.Messages;

/**
 *
 * @author Adam Dubiel
 */
public interface ParamManager {

    Messages createParameter(UserProfile responsible, RepositoryName in, Parameter newState);

    Messages updateParameter(UserProfile responsible, RepositoryName in, String parameterName, Parameter newState);

    Messages deleteParameter(UserProfile responsible, RepositoryName in, String parameterName);

    Messages addLevel(UserProfile responsible, RepositoryName in, String parameterName, Level level);

    Messages reorderLevels(UserProfile responsible, RepositoryName in, String parameterName, List<LevelKey> levels);

    Messages updateLevel(UserProfile responsible, RepositoryName in, String parameterName, LevelKey levelKey, Level level);

    Messages deleteLevel(UserProfile responsible, RepositoryName in, String parameterName, LevelKey levelKey);

    Messages addEntries(UserProfile responsible, RepositoryName in, String parameterName, List<ParameterEntry> entries);

    Messages updateEntry(UserProfile responsible, RepositoryName in, String parameterName, ParameterEntryKey entryKey, ParameterEntry entry);

    Messages deleteEntries(UserProfile responsible, RepositoryName in, String parameterName, List<ParameterEntryKey> entryKeys);
}
