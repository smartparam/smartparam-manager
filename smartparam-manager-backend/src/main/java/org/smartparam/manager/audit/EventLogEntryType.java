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

/**
 *
 * @author Adam Dubiel
 */
public enum EventLogEntryType {

    PARAMETER("ParameterEvent"),
    ENTRY("EntryEvent");

    private final String typeCode;

    private EventLogEntryType(String typeCode) {
        this.typeCode = typeCode;
    }

    public String typeCode() {
        return typeCode;
    }

    public static EventLogEntryType fromCode(String code) {
        for (EventLogEntryType type : values()) {
            if (type.typeCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown EventLogEntryType code: " + code);
    }

}
