/*
 * Copyright 2023 Maretha Solutions LLC - https://maretha.io.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.nuxeo.generative.operations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.generative.ai.GenerativeAI;
import org.nuxeo.runtime.mockito.MockitoFeature;
import org.nuxeo.runtime.mockito.RuntimeService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class, MockitoFeature.class })
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("org.nuxeo.generative.ai.nuxeo-generative-ai-core")
public class GenerateImageOperationTest {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Mock
    @RuntimeService
    private GenerativeAI generativeAI;

    @Test
    public void shouldCallTheOperation() throws OperationException, IOException {
        Blob blob = Blobs.createBlob(FileUtils.getResourceFileFromContext("files/test.png"));
        blob.setMimeType("image/png");
        blob.setFilename("test");
        when(generativeAI.generateImage(any(String.class), any(String.class), any(String.class))).thenReturn(blob);

        OperationContext ctx = new OperationContext(session);
        Map<String, String> params = new HashMap<>();
        params.put("prompt", "test");
        automationService.run(ctx, GenerateImageOperation.ID, params);
    }
}
