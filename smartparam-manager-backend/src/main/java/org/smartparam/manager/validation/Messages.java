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

import java.util.Collection;

/**
 *
 * @author Adam Dubiel
 */
public interface Messages {

    void discardMessages();

    FieldMessage fieldMessage(String fieldPath);

    Collection<FieldMessage> fieldMessages();

    Collection<Message> globalMessages();

    int messageCount();

    void message(String message, Object... arguments);

    void message(String message, boolean resolveMessageArguments, Object... arguments);

    void messageField(String path, String message, Object... arguments);

    void messageField(String path, String message, boolean resolveMessageArguments, Object... arguments);

    void popNestedPath();

    void popNestedPath(int pathCount);

    void pushNestedPath(String segment);

    void pushNestedPath(String... segments);

    void replaceNestedPath(String newNestedPath);

    void restoreNestedPath();

    void mergeWith(Messages otherMessages);

    String currentNestedPath();

    boolean hasMessages();

    Collection<Message> allMessages();

    String root();

    boolean hasFieldMessages(String fieldPath);

    void discardMatchingFieldMessages(String pathPattern);
}
