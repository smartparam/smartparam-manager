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

import org.smartparam.editor.core.model.ParameterKey;
import org.smartparam.manager.validation.BasicMessages;
import org.smartparam.manager.validation.Messages;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterAdditionResult extends AbstractResult {

    private final ParameterKey parameterKey;

    public ParameterAdditionResult(ParameterKey newParameterKey, Messages messages) {
        super(messages);
        this.parameterKey = newParameterKey;
    }

    public static ParameterAdditionResult added(ParameterKey newParameterKey) {
        return new ParameterAdditionResult(newParameterKey, BasicMessages.ok());
    }

    public ParameterKey parameterKey() {
        return parameterKey;
    }

}
