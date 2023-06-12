package org.nuxeo.generative.ai;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
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
}
