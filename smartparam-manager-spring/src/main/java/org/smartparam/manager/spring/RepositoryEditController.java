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
package org.smartparam.manager.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.smartparam.editor.core.ParamEditor;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.level.LevelKey;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.editor.model.simple.SimpleLevel;
import org.smartparam.editor.model.simple.SimpleLevelKey;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.model.simple.SimpleParameterEntryKey;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Adam Dubiel
 */
@RequestMapping("/repository")
@Controller
public class RepositoryEditController {

    private final ParamEditor paramEditor;

    @Autowired
    public RepositoryEditController(ParamEditor paramEditor) {
        this.paramEditor = paramEditor;
    }

    @RequestMapping(value = "{in}/parameters", method = RequestMethod.PUT)
    @ResponseBody
    public void create(@PathVariable("in") String in, @RequestBody SimpleParameter parameter) {
        paramEditor.createParameter(new RepositoryName(in), parameter);
    }

    @RequestMapping(value = "{in}/parameters/{name}", method = RequestMethod.POST)
    @ResponseBody
    public void update(@PathVariable("in") String in, @PathVariable("name") String parameterName, @RequestBody SimpleParameter parameter) {
        paramEditor.updateParameter(new RepositoryName(in), parameterName, parameter);
    }

    @RequestMapping(value = "{in}/parameters/{name}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("in") String in, @PathVariable("name") String parameter) {
        paramEditor.deleteParameter(new RepositoryName(in), parameter);
    }

    @RequestMapping(value = "{in}/parameters/{name}/levels", method = RequestMethod.PUT)
    @ResponseBody
    public LevelKey addLevel(@PathVariable("in") String in, @PathVariable("name") String parameter, @RequestBody SimpleLevel level) {
        return paramEditor.addLevel(new RepositoryName(in), parameter, level).data();
    }

    @RequestMapping(value = "{in}/parameters/{name}/levels/reorder", method = RequestMethod.POST)
    @ResponseBody
    public void reorderLevels(@PathVariable("in") String in, @PathVariable("name") String parameter, @RequestBody List<LevelKey> levels) {
        paramEditor.reorderLevels(new RepositoryName(in), parameter, levels);
    }

    @RequestMapping(value = "{in}/parameters/{name}/levels/{levelKey}", method = RequestMethod.POST)
    @ResponseBody
    public void updateLevel(@PathVariable("in") String in, @PathVariable("name") String parameter, @PathVariable("levelKey") String levelKey, @RequestBody SimpleLevel level) {
        paramEditor.updateLevel(new RepositoryName(in), parameter, new SimpleLevelKey(levelKey), level);
    }

    @RequestMapping(value = "{in}/parameters/{name}/levels/{levelKey}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteLevel(@PathVariable("in") String in, @PathVariable("name") String parameter, @PathVariable("levelKey") String levelKey) {
        paramEditor.deleteLevel(new RepositoryName(in), parameter, new SimpleLevelKey(levelKey));
    }

    @RequestMapping(value = "{in}/parameters/{name}/entries", method = RequestMethod.PUT)
    @ResponseBody
    public Collection<ParameterEntryKey> addEntries(@PathVariable("in") String in, @PathVariable("name") String parameter, @RequestBody List<MapEntry> entries) {
        return paramEditor.addEntries(new RepositoryName(in), parameter, new ArrayList<MapEntry>(entries)).items();
    }

    @RequestMapping(value = "{in}/parameters/{name}/entries/{entryKey}", method = RequestMethod.POST)
    @ResponseBody
    public void updateEntry(@PathVariable("in") String in, @PathVariable("name") String parameter, @PathVariable("entryKey") String entryKey, @RequestBody MapEntry entry) {
        paramEditor.updateEntry(new RepositoryName(in), parameter, new SimpleParameterEntryKey(entryKey), entry);
    }

    @RequestMapping(value = "{in}/parameters/{name}/entries/{entryKey}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteEntry(@PathVariable("in") String in, @PathVariable("name") String parameter, @PathVariable("entryKey") String entryKey) {
        paramEditor.deleteEntry(new RepositoryName(in), parameter, new SimpleParameterEntryKey(entryKey));
    }

    @RequestMapping(value = "{in}/parameters/{name}/entries", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteEntries(@PathVariable("in") String in, @PathVariable("name") String parameter, @RequestParam("keys") String[] entryKeys) {
        List<ParameterEntryKey> keys = new ArrayList<ParameterEntryKey>(entryKeys.length);
        for (String entryKey : entryKeys) {
            keys.add(new SimpleParameterEntryKey(entryKey));
        }

        paramEditor.deleteEntries(new RepositoryName(in), parameter, keys);
    }
}
