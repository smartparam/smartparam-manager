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
package org.smartparam.manager.authz;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class UserProfile {

    private final String login;

    private final String role;

    private final Set<String> permissions;

    public UserProfile(String login, String role, Set<String> permissions) {
        this.login = login;
        this.role = role;
        this.permissions = permissions;
    }

    public UserProfile(String login, String role) {
        this(login, role, new HashSet<String>());
    }

    public UserProfile(String login, Set<String> permissions) {
        this(login, null, permissions);
    }

    public String login() {
        return login;
    }

    public String role() {
        return role;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(role);
    }

}
