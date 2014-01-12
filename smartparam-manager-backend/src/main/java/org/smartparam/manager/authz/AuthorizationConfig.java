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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.smartparam.editor.identity.RepositoryName;

/**
 *
 * @author Adam Dubiel
 */
public class AuthorizationConfig implements Iterable<AuthorizationMethod> {

    private final RepositoryName defaultWriteRepository;

    private final List<AuthorizationMethod> authorizationMethods = new ArrayList<AuthorizationMethod>();

    public AuthorizationConfig(RepositoryName defaulWriteRepository) {
        this.defaultWriteRepository = defaulWriteRepository;
        this.authorizationMethods.addAll(Arrays.asList(AuthorizationMethod.values()));
    }

    public AuthorizationConfig(RepositoryName defaulWriteRepository, List<AuthorizationMethod> authorizationMethods) {
        this.defaultWriteRepository = defaulWriteRepository;
        this.authorizationMethods.addAll(authorizationMethods);
        Collections.sort(this.authorizationMethods);
    }

    public Iterator<AuthorizationMethod> iterator() {
        return authorizationMethods.iterator();
    }

    public RepositoryName defaultWriteRepository() {
        return defaultWriteRepository;
    }
}
