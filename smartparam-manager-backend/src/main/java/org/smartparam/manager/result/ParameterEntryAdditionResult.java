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
package org.smartparam.manager.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.manager.validation.BasicMessages;
import org.smartparam.manager.validation.Messages;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryAdditionResult extends AbstractResult {

    private final List<ParameterEntryKey> parameterEntryKeys;

    public ParameterEntryAdditionResult(List<ParameterEntryKey> newParameterEntryKeys, Messages messages) {
        super(messages);
        this.parameterEntryKeys = new ArrayList<ParameterEntryKey>(newParameterEntryKeys);
    }

    public static ParameterEntryAdditionResult added(List<ParameterEntryKey> newParameterEntryKeys) {
        return new ParameterEntryAdditionResult(newParameterEntryKeys, BasicMessages.ok());
    }

    public List<ParameterEntryKey> parameterEntryKeys() {
        return Collections.unmodifiableList(parameterEntryKeys);
    }

    public ParameterEntryKey firstEntryKey() {
        return parameterEntryKeys.get(0);
    }

}
