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
 * Contributors:
 *     Thibaud Arguillere
 */
package org.nuxeo.generative.ai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.model.Extension;

public class GenerativeAIImpl extends DefaultComponent implements GenerativeAI {

    private static final Log log = LogFactory.getLog(GenerativeAIImpl.class);

    protected static final String CONFIG_EXT_POINT = "configuration";

    protected static final String PROVIDER_EXT_POINT = "provider";

    protected GenerativeAIDescriptor config = null;

    protected Map<String, GenerativeAIProvider> providers = new HashMap<>();

    /**
     * Registers the given extension.
     */
    @Override
    public void registerExtension(Extension extension) {
        super.registerExtension(extension);

        Object[] contribs = extension.getContributions();
        if (contribs == null || contribs.length == 0) {
            log.warn("No contribution for the '" + extension.getExtensionPoint()
                    + "' extension point => calls to generative AI will fail.");
            return;
        }

        switch (extension.getExtensionPoint()) {
        case CONFIG_EXT_POINT:
            // There should be only one contrib
            config = (GenerativeAIDescriptor) contribs[0];
            break;

        case PROVIDER_EXT_POINT:
            for (Object contrib : contribs) {
                GenerativeAIProviderDescriptor providerDesc = (GenerativeAIProviderDescriptor) contrib;
                try {
                    GenerativeAIProvider provider = (GenerativeAIProvider) providerDesc.getKlass()
                                                                                       .getConstructor(
                                                                                               GenerativeAIProviderDescriptor.class)
                                                                                       .newInstance(providerDesc);
                    providers.put(providerDesc.name, provider);
                } catch (ReflectiveOperationException e) {
                    throw new NuxeoException(e);
                }
            }
            break;
        }
    }

    @Override
    public GenerativeAIProvider getProvider(String name) {
        if (StringUtils.isBlank(name)) {
            name = GenerativeAI.DEFAULT_PROVIDER_NAME;
        }
        return providers.get(name);
    }

    @Override
    public Blob generateImage(String provider, String prompt, String size) throws IOException {
        GenerativeAIProvider aiProvider = getProviderOrDefault(provider);

        return aiProvider.generateImage(prompt, size);
    }

    /*
     * Utility to handle a null/empty provider name
     */
    protected GenerativeAIProvider getProviderOrDefault(String name) {
        if (StringUtils.isBlank(name)) {
            name = GenerativeAI.DEFAULT_PROVIDER_NAME;
        }
        return getProvider(name);
    }
}
