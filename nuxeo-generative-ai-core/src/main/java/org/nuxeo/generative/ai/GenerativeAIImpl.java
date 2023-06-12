/*
 * (C) Copyright 2023 Hyland (http://hyland.com/)  and others.
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.model.Extension;

public class GenerativeAIImpl extends DefaultComponent implements GenerativeAI {

    private static final Log log = LogFactory.getLog(GenerativeAIImpl.class);

    protected static final String CONFIG_EXT_POINT = "configuration";

    protected static final String PROVIDER_EXT_POINT = "provider";

    protected GenerativeAIDescriptor config = null;

    protected Map<String, GenerativeAIProvider> providers = new HashMap<>();

    /**
     * Component activated notification.
     * Called when the component is activated. All component dependencies are resolved at that moment.
     * Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification.
     * Called before a component is unregistered.
     * Use this method to do cleanup if any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Registers the given extension.
     *
     * @param extension the extension to register
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
                                                                                       .getConstructor(String.class, Map.class)
                                                                                       .newInstance(
                                                                                               providerDesc.getName(),
                                                                                               providerDesc.getParameters());
                    providers.put(providerDesc.name, provider);
                } catch (ReflectiveOperationException e) {
                    throw new NuxeoException(e);
                }
            }
            break;
        }
    }

    /**
     * Unregisters the given extension.
     *
     * @param extension the extension to unregister
     */
    @Override
    public void unregisterExtension(Extension extension) {
        super.unregisterExtension(extension);
    }

    /**
     * Start the component. This method is called after all the components were resolved and activated
     *
     * @param context the component context. Use it to get the current bundle context
     */
    @Override
    public void start(ComponentContext context) {
        // do nothing by default. You can remove this method if not used.
    }

    /**
     * Stop the component.
     *
     * @param context the component context. Use it to get the current bundle context
     * @throws InterruptedException
     */
    @Override
    public void stop(ComponentContext context) throws InterruptedException {
        // do nothing by default. You can remove this method if not used.
    }
    
    @Override
    public GenerativeAIProvider getProvider(String name) {
        if(StringUtils.isBlank(name)) {
            name = GenerativeAI.DEFAULT_PROVIDER_NAME;
        }
        return providers.get(name);
    }

    @Override
    public Blob generateImage(String provider, String prompt) {
        GenerativeAIProvider aiProvider = getProviderOrDefault(provider);
        
        return aiProvider.generateImage(prompt);
    }

    @Override
    public String generateText(String provider, String prompt) {
        GenerativeAIProvider aiProvider = getProviderOrDefault(provider);
        
        return aiProvider.generateText(prompt);
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
