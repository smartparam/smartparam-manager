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

import org.smartparam.engine.core.parameter.Parameter;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterDiff {

    private final Parameter previous;

    private final Parameter current;

    public static ParameterDiff initialState(Parameter initialState) {
        return new ParameterDiff(null, initialState);
    }

    public static ParameterDiff lastKnownState(Parameter lastKnownState) {
        return new ParameterDiff(lastKnownState, null);
    }

    public ParameterDiff(Parameter previous, Parameter current) {
        this.previous = previous;
        this.current = current;
    }

    public Parameter previous() {
        return previous;
    }

    public Parameter current() {
        return current;
    }
}
