/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.manager.validation;

import java.util.Arrays;

/**
 *
 * @author Adam Dubiel <adam.dubiel@allegro.pl>
 * @since 1.0.0
 */
public class Message {

    private final String messageCode;

    private final Object[] messageArguments;

    private boolean resolveMessageArguments = false;

    private int timesReported = 1;

    public Message(String messageCode, boolean resolveMessageArguments, Object... messageArguments) {
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
        this.resolveMessageArguments = resolveMessageArguments;
    }

    public Message(String messageCode, Object... messageArguments) {
        this(messageCode, false, messageArguments);
    }

    Message(Message other, Object... messageArguments) {
        this(other.messageCode(), other.resolveMessageArguments(), messageArguments);
        this.timesReported = other.counter();
        this.timesReported++;
    }

    public String messageCode() {
        return messageCode;
    }

    public Object[] messageArguments() {
        return Arrays.copyOf(messageArguments, messageArguments.length);
    }

    public boolean resolveMessageArguments() {
        return resolveMessageArguments;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.messageCode != null ? this.messageCode.hashCode() : 0);
        hash = 59 * hash + Arrays.deepHashCode(this.messageArguments);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if ((this.messageCode == null) ? (other.messageCode != null) : !this.messageCode.equals(other.messageCode)) {
            return false;
        }
        if (!Arrays.deepEquals(this.messageArguments, other.messageArguments)) {
            return false;
        }
        return true;
    }

    public int counter() {
        return timesReported;
    }
}
