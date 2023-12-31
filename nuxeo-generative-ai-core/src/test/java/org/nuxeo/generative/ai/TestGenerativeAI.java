package org.nuxeo.generative.ai;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

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
        // . . . more test . . .
        
    }
}
