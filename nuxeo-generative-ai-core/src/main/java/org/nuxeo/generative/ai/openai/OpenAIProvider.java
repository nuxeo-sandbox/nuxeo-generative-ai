/* Copyright 2023 Maretha Solutions LLC - https://maretha.io.
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
package org.nuxeo.generative.ai.openai;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.generative.ai.GenerativeAIProvider;
import org.nuxeo.generative.ai.GenerativeAIProviderDescriptor;
import org.nuxeo.runtime.api.Framework;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;

/**
 * This is the defaiult provider. Name is defined in For authentication ot the service, we need an apikey and opetionaly
 * an organization Values are checked in this order: <br>
 * 1. The xml configuration <extension target="org.nuxeo.generative.ai.GenerativeAI" point="provider" >
 * <provider class="org.nuxeo.generative.ai.openai.OpenAIProvider" name="openai"> <parameters>
 * <parameter name="organization">MY_ORG</parameter> <parameter name="apikey">1234-ABCD...EF</parameter> </parameters>
 * </provider> </extension> <br>
 * 2. The nuxeo configuration parameters, generative.ai.openai.organization and generative.ai.openai.apikey Notice they
 * can also be used in the XML, see the generativeai-service.xml file <br>
 * 3. Environment variables NUXEO_GENERATIVE_AI_OPENAI_ORGANIZATION and ENV_VAR_APIKEY
 * 
 * @since TODO
 */
public class OpenAIProvider implements GenerativeAIProvider {

    private static final Log log = LogFactory.getLog(OpenAIProvider.class);

    public static final String ORGANIZATION_CONF_PARAM = "generative.ai.openai.organization";

    public static final String API_KEY_CONF_PARAM = "generative.ai.openai.apikey";

    // You can also use the following environment variables:
    public static final String ENV_VAR_ORGANIZATION = "NUXEO_GENERATIVE_AI_OPENAI_ORGANIZATION";

    public static final String ENV_VAR_APIKEY = "NUXEO_GENERATIVE_AI_OPENAI_APIKEY";

    protected String organization = null;

    protected String apiKey = null;

    protected OpenAiService service = null;

    protected GenerativeAIProviderDescriptor descriptor;

    public OpenAIProvider(GenerativeAIProviderDescriptor desc) {

        descriptor = desc;

        Map<String, String> params = desc.getParameters();

        if (params != null) {
            organization = params.get("organization");
            if (StringUtils.isBlank(organization)) {
                organization = Framework.getProperty(ORGANIZATION_CONF_PARAM);
            }
            if (StringUtils.isBlank(organization)) {
                organization = System.getProperty(ENV_VAR_ORGANIZATION);
            }

            apiKey = params.get("apiKey");
            if (StringUtils.isBlank(apiKey)) {
                apiKey = Framework.getProperty(API_KEY_CONF_PARAM);
            }
            if (StringUtils.isBlank(apiKey)) {
                apiKey = System.getProperty(ENV_VAR_APIKEY);
            }

            // Organization can be empty, not apikey
            if (StringUtils.isBlank(apiKey)) {
                log.error("Api Key for calling OpenAI is empty => all call will fail and will not be performed");
            } else {
                service = new OpenAiService(apiKey);
            }
        }
    }

    protected boolean checkApiKey() {
        if (StringUtils.isBlank(apiKey)) {
            log.warn("OpenAI API Key is not valid => not calling the service");
            return false;
        }
        return true;
    }

    @Override
    public Blob generateImage(String prompt, String size) throws IOException {

        Blob result = null;
        if (!checkApiKey()) {
            log.error("No API key configured, returning null");
            return null;
        }
        CreateImageRequest request = CreateImageRequest.builder().prompt(prompt).n(1).size(size).build();
        List<Image> images = null;
        try {
            images = service.createImage(request).getData();
            Image image = null;
            if (images.size() > 0) {
                image = images.get(0);
            }
            if (image != null) {
                image.getUrl();
                result = GenerativeAIProvider.downloadFile(image.getUrl(), "", "OpenAI");
            }
        } catch (OpenAiHttpException e) {
            log.error("Can not generate image: ", e);
            throw new NuxeoException(e);
        }
        return result;
    }

    @Override
    public String getName() {
        return descriptor.getName();
    }

}
