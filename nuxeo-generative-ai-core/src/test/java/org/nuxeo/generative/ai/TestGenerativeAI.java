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
package org.nuxeo.generative.ai;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy("org.nuxeo.generative.ai.nuxeo-generative-ai-core")
public class TestGenerativeAI {

    @Inject
    protected GenerativeAI generativeai;

    @Test
    public void testService() {
        assertNotNull(generativeai);
    }
    
    @Test
    public void shouldHaveADefaultProvider() {
        GenerativeAIProvider defaultProvider = generativeai.getProvider(null);
        
        assertNotNull(defaultProvider);
    }
    
    @Test
    public void shouldGenerateImageWithDefaultProvider() throws Exception {
        
        Blob result = generativeai.generateImage(null, "a woman wearing a red jacket with the name of a fake brand named Rainier, she has sunglasses and a cap and she is smiling, we see all the jacket", GenerativeAIProvider.IMG_512);
        assertNotNull(result);
        
    }
}
