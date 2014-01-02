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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Adam Dubiel <adam.dubiel@allegro.pl>
 * @since 1.0.0
 */
public class BasicMessages implements Messages {

    private MessageNestedPath nestedPath;

    private MessageNestedPath replacedNestedPathBackup;

    private boolean nestedPathReplaced;

    private final Set<Message> globalMessages = new LinkedHashSet<Message>();

    private final Map<String, FieldMessage> fieldMessages = new HashMap<String, FieldMessage>();

    private int messageCounter = 0;

    public static Messages ok() {
        return new BasicMessages();
    }

    public static Messages error(String message, Object... arguments) {
        Messages messages = new BasicMessages();
        messages.message(message, arguments);
        return messages;
    }

    public BasicMessages(String root) {
        nestedPath = new MessageNestedPath(root);
    }

    public BasicMessages() {
        this("");
    }

    @Override
    public void pushNestedPath(String segment) {
        nestedPath.pushNestedPath(segment);
    }

    @Override
    public void pushNestedPath(String... segments) {
        for (String segment : segments) {
            nestedPath.pushNestedPath(segment);
        }
    }

    @Override
    public void popNestedPath() {
        nestedPath.popNestedPath();
    }

    @Override
    public void popNestedPath(int pathCount) {
        for (int i = 0; i < pathCount; ++i) {
            nestedPath.popNestedPath();
        }
    }

    @Override
    public void replaceNestedPath(String newNestedPath) {
        nestedPathReplaced = true;
        replacedNestedPathBackup = nestedPath;
        nestedPath = new MessageNestedPath(newNestedPath);
    }

    @Override
    public void restoreNestedPath() {
        if (nestedPathReplaced) {
            nestedPath = replacedNestedPathBackup;
            replacedNestedPathBackup = null;
            nestedPathReplaced = false;
        }
    }

    @Override
    public void message(String message, Object... arguments) {
        message(message, false, arguments);
    }

    @Override
    public void message(String message, boolean resolveMessageArguments, Object... arguments) {
        globalMessages.add(new Message(message, arguments, resolveMessageArguments));
        messageCounter++;
    }

    @Override
    public void messageField(String path, String message, Object... arguments) {
        messageField(path, message, false, arguments);
    }

    @Override
    public void messageField(String path, String message, boolean resolveMessageArguments, Object... arguments) {
        String fullPath = nestedPath.concatenate(path);

        if (fieldMessages.containsKey(fullPath)) {
            FieldMessage messageWrapper = fieldMessages.get(fullPath);
            fieldMessages.put(fullPath, new FieldMessage(messageWrapper, arguments));
        } else {
            fieldMessages.put(fullPath, new FieldMessage(fullPath, message, arguments, resolveMessageArguments));
        }

        messageCounter++;
    }

    @Override
    public void discardMessages() {
        globalMessages.clear();
        fieldMessages.clear();
        messageCounter = 0;
    }

    @Override
    public Collection<Message> globalMessages() {
        return Collections.unmodifiableCollection(globalMessages);
    }

    @Override
    public Collection<FieldMessage> fieldMessages() {
        return fieldMessages.values();
    }

    @Override
    public FieldMessage fieldMessage(String fieldPath) {
        return fieldMessages.get(fieldPath);
    }

    @Override
    public int messageCount() {
        return messageCounter;
    }

    @Override
    public void mergeWith(Messages otherMessages) {
        messageCounter += otherMessages.messageCount();
        for (FieldMessage fieldMessage : otherMessages.fieldMessages()) {
            fieldMessages.put(fieldMessage.path(), fieldMessage);
        }
        globalMessages.addAll(otherMessages.globalMessages());
    }

    @Override
    public String currentNestedPath() {
        return nestedPath.getNestedPath();
    }

    @Override
    public boolean hasMessages() {
        return messageCounter != 0;
    }

    @Override
    public Collection<Message> allMessages() {
        List<Message> messages = new ArrayList<Message>();

        messages.addAll(globalMessages());
        messages.addAll(fieldMessages());

        return messages;
    }

    @Override
    public String root() {
        return nestedPath.getRoot();
    }

    @Override
    public boolean hasFieldMessages(String fieldPath) {
        return fieldMessages.containsKey(nestedPath.concatenate(fieldPath));
    }

    @Override
    public void discardMatchingFieldMessages(String pathPattern) {
        Pattern pattern = Pattern.compile(pathPattern);
        Matcher matcher;

        Iterator<Entry<String, FieldMessage>> iterator = fieldMessages.entrySet().iterator();
        Entry<String, FieldMessage> entry;
        String path;
        while (iterator.hasNext()) {
            entry = iterator.next();
            path = entry.getKey();
            matcher = pattern.matcher(path);
            if (matcher.matches()) {
                messageCounter -= entry.getValue().counter();
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "[BasicMessages message counter: " + messageCounter + " "
                + " global messages: " + globalMessages.size()
                + " field messages: " + fieldMessages.size() + " ]";
    }
}
