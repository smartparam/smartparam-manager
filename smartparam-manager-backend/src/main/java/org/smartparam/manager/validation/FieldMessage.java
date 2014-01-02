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

/**
 *
 * @author Adam Dubiel <adam.dubiel@allegro.pl>
 * @since 1.0.0
 */
public class FieldMessage extends Message {

    private final String path;

    public FieldMessage(String path, String messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
        this.path = path;
    }

    public FieldMessage(String path, String messageCode, boolean resolveMessageArguments, Object... messageArguments) {
        super(messageCode, resolveMessageArguments, messageArguments);
        this.path = path;
    }

    FieldMessage(FieldMessage other, Object... messageArguments) {
        super(other, messageArguments);
        this.path = other.path();
    }

    public String path() {
        return path;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.path != null ? this.path.hashCode() : 0);
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
        final FieldMessage other = (FieldMessage) obj;
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        return true;
    }

}
