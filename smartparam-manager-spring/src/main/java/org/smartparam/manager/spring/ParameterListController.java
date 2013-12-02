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

import java.util.List;
import org.smartparam.editor.identity.DescribedCollection;
import org.smartparam.editor.viewer.ParamViewer;
import org.smartparam.editor.viewer.ParameterFilter;
import org.smartparam.editor.viewer.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Adam Dubiel
 */
@Controller
@RequestMapping("/list")
public class ParameterListController {

    private final ParamViewer paramViewer;

    @Autowired
    public ParameterListController(ParamViewer paramViewer) {
        this.paramViewer = paramViewer;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<DescribedCollection<String>> listAll(@RequestParam(required = false, defaultValue = "DESC", value = "order") SortDirection order,
            @RequestParam(required = false, defaultValue = "", value = "filter") String filter) {
        return paramViewer.listParameters(new ParameterFilter(filter, order));
    }
}
