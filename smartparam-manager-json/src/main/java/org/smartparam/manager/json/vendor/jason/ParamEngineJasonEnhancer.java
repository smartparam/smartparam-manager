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
package org.smartparam.manager.json.vendor.jason;

import org.jasonjson.core.Jason;
import org.jasonjson.core.JasonBuilder;
import org.smartparam.editor.core.identity.DescribedCollection;
import org.smartparam.editor.core.identity.DescribedEntity;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.editor.core.entry.ParameterEntryMap;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamEngineJasonEnhancer {

    private ParamEngineJasonEnhancer() {
    }

    public static void enhance(JasonBuilder builder) {
        builder.registerTypeAdapter(DescribedCollection.class, new DescribedCollectionSerializer())
                .registerTypeAdapter(DescribedEntity.class, new DescribedEntitySerializer())
                .registerTypeAdapterFactory(new LevelKeySerializer())
                .registerTypeAdapterFactory(new ParameterEntryKeySerializer())
                .registerTypeAdapter(RepositoryName.class, new RepositoryNameSerializer())
                .registerTypeAdapter(ParameterEntryMap.class, new ParameterEntryMapSerializer())
                .registerTypeAdapterFactory(new ParameterDiffSerializer())
                .registerTypeAdapterFactory(new ParameterEntryDiffSerializer());
    }

    public static JasonBuilder createEnhancedBuilder() {
        JasonBuilder builder = new JasonBuilder();
        enhance(builder);
        return builder;
    }

    public static Jason createEnhanced() {
        return createEnhancedBuilder().create();
    }

}
