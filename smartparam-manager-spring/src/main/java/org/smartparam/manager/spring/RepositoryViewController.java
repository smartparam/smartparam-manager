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
import java.util.List;
import org.smartparam.editor.capabilities.RepositoryCapabilities;
import org.smartparam.editor.identity.DescribedCollection;
import org.smartparam.editor.identity.DescribedEntity;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.viewer.LevelSorting;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.editor.viewer.ParameterEntriesFilter;
import org.smartparam.editor.viewer.ParameterFilter;
import org.smartparam.editor.viewer.SortDirection;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Adam Dubiel
 */
@Controller
@RequestMapping("/repository")
public class RepositoryViewController {

    private final ParamViewer paramViewer;

    @Autowired
    public RepositoryViewController(ParamViewer paramViewer) {
        this.paramViewer = paramViewer;
    }

    @RequestMapping(value = "{from}/list", method = RequestMethod.GET)
    @ResponseBody
    public DescribedCollection<String> list(@PathVariable("from") String from,
            @RequestParam(required = false, defaultValue = "DESC", value = "order") SortDirection order,
            @RequestParam(required = false, defaultValue = "", value = "filter") String filter) {
        return paramViewer.listParameters(new RepositoryName(from), new ParameterFilter(filter, order));
    }

    @RequestMapping(value = "{from}/capabilites", method = RequestMethod.GET)
    @ResponseBody
    public DescribedEntity<RepositoryCapabilities> capabilities(@PathVariable("from") String from) {
        return paramViewer.capabilities(new RepositoryName(from));
    }

    @RequestMapping(value = "{from}/parameters/{name}", method = RequestMethod.GET)
    @ResponseBody
    public DescribedEntity<Parameter> parameterMetadata(@PathVariable("from") String from,
            @PathVariable("name") String name) {
        return paramViewer.getParameterMetadata(new RepositoryName(from), name);
    }

    @RequestMapping(value = "{from}/parameters/{name}/entries", method = RequestMethod.GET)
    @ResponseBody
    public DescribedCollection<ParameterEntry> parameterEntries(@PathVariable("from") String from,
            @PathVariable("name") String name,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "100", value = "pageSize") int pageSize,
            @RequestParam(required = false, defaultValue = "", value = "filters") String[] filters,
            @RequestParam(required = false, defaultValue = "", value = "orderBy") int[] orderBy,
            @RequestParam(required = false, defaultValue = "", value = "order") SortDirection[] order) {

        List<LevelSorting> sorting = new ArrayList<LevelSorting>();
        for (int index = 0; index < orderBy.length; ++index) {
            sorting.add(
                    index < order.length
                    ? new LevelSorting(orderBy[index], order[index])
                    : new LevelSorting(orderBy[index])
            );
        }

        ParameterEntriesFilter entiresFilter = new ParameterEntriesFilter(page, pageSize, filters, sorting);
        return paramViewer.listParameterEntries(new RepositoryName(from), name, entiresFilter);
    }
}
