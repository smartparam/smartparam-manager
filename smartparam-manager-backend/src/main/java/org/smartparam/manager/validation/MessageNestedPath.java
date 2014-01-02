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

import java.util.Stack;

/**
 *
 * @author Adam Dubiel
 */
public class MessageNestedPath {

    private static final String PATH_DELIMITER = ".";

    private final String root;

    private String currentNestedPath;

    private final Stack<String> nestedPathStack = new Stack<String>();

    public MessageNestedPath(String root) {
        this.root = root;
        currentNestedPath = generateNestedPath();
    }

    public void popNestedPath() {
        if (!nestedPathStack.empty()) {
            nestedPathStack.pop();
        }
        this.currentNestedPath = generateNestedPath();
    }

    public void pushNestedPath(String segment) {
        nestedPathStack.push(segment);
        this.currentNestedPath = generateNestedPath();
    }

    public String getNestedPath() {
        return currentNestedPath;
    }

    public String getRoot() {
        return root;
    }

    public String concatenate(String segment) {
        if (currentNestedPath != null && !currentNestedPath.isEmpty()) {
            return currentNestedPath + segment;
        }
        return segment;
    }

    private String generateNestedPath() {
        StringBuilder generatedNestedPath = new StringBuilder(root);
        if (!root.isEmpty()) {
            generatedNestedPath.append(PATH_DELIMITER);
        }

        for (String nestedPathSegment : nestedPathStack) {
            generatedNestedPath.append(nestedPathSegment).append(PATH_DELIMITER);
        }

        return generatedNestedPath.toString();
    }
}
