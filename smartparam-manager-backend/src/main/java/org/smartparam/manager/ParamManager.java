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
import org.smartparam.editor.core.ParamEditor;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.editor.core.identity.RepositoryName;
import org.smartparam.editor.core.model.LevelKey;
import org.smartparam.editor.core.model.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.manager.authz.UserProfile;
import org.smartparam.manager.result.LevelAdditionResult;
import org.smartparam.manager.result.ParameterAdditionResult;
import org.smartparam.manager.result.ParameterEntryAdditionResult;
import org.smartparam.manager.result.Result;

/**
 *
 * @author Adam Dubiel
 */
public interface ParamManager {

    ParamViewer viewer();

    ParamEditor editor();

    ParameterAdditionResult createParameter(UserProfile responsible, RepositoryName in, Parameter newState);

    Result updateParameter(UserProfile responsible, RepositoryName in, String parameterName, Parameter newState);

    Result deleteParameter(UserProfile responsible, RepositoryName in, String parameterName);

    LevelAdditionResult addLevel(UserProfile responsible, RepositoryName in, String parameterName, Level level);

    Result reorderLevels(UserProfile responsible, RepositoryName in, String parameterName, List<LevelKey> levels);

    Result updateLevel(UserProfile responsible, RepositoryName in, String parameterName, LevelKey levelKey, Level level);

    Result deleteLevel(UserProfile responsible, RepositoryName in, String parameterName, LevelKey levelKey);

    ParameterEntryAdditionResult addEntry(UserProfile responsible, RepositoryName in, String parameterName, ParameterEntryMap entry);

    ParameterEntryAdditionResult addEntries(UserProfile responsible, RepositoryName in, String parameterName, List<ParameterEntryMap> entries);

    Result updateEntry(UserProfile responsible, RepositoryName in, String parameterName, ParameterEntryKey entryKey, ParameterEntryMap entry);

    Result deleteEntries(UserProfile responsible, RepositoryName in, String parameterName, List<ParameterEntryKey> entryKeys);
}
