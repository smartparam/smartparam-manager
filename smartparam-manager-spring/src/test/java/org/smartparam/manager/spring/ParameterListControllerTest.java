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
import org.mockito.Mock;
import org.smartparam.editor.core.identity.DescribedCollection;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.editor.core.filters.ParameterFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterListControllerTest extends MockMvcTest {

    private MockMvc mvc;

    @Mock
    private ParamViewer viewer;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        mvc = mockMvc(new ParameterListController(viewer));
    }

    @Test
    public void shouldReturnListOfAllParametersInAllRepositories() throws Exception {
        // given
        List<DescribedCollection<String>> parameters = new ArrayList<DescribedCollection<String>>();
        parameters.add(new DescribedCollection<String>(new RepositoryName("test"), "1", "2"));

        when(viewer.listParameters(any(ParameterFilter.class))).thenReturn(parameters);

        // when then
        mvc.perform(get("/list")).andExpect(status().isOk())
                .andExpect(jsonPath("$.").isArray())
                .andExpect(jsonPath("$.[0].source").value("test"))
                .andExpect(jsonPath("$.[0].items").isArray())
                .andExpect(jsonPath("$.[0].items[0]").value("1"))
                .andExpect(jsonPath("$.[0].items[1]").value("2"));
    }

}
