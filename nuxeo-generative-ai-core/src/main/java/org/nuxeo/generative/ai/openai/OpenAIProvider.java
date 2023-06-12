package org.nuxeo.generative.ai.openai;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.generative.ai.GenerativeAIDescriptor;
import org.nuxeo.generative.ai.GenerativeAIProvider;
import org.nuxeo.runtime.api.Framework;

/**
 * This is the defaiult provider. Name is defined in 
 * For authentication ot the service, we need an apikey and opetionaly an organization
 * Values are checked in this order:
 * <br>
 * 1. The xml configuration
 * <extension target="org.nuxeo.generative.ai.GenerativeAI" point="provider" >
 * <provider class="org.nuxeo.generative.ai.openai.OpenAIProvider" name="openai">
 * <parameters>
 * <parameter name="organization">MY_ORG</parameter>
 * <parameter name="apikey">1234-ABCD...EF</parameter>
 * </parameters>
 * </provider>
 * </extension>
 * <br>
 * 2. The nuxeo configuration parameters, generative.ai.openai.organization and generative.ai.openai.apikey
 * Notice they can also be used in the XML, see the generativeai-service.xml file
 * <br>
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

    protected Map<String, String> params;
    
    String contributionName;

    protected String organization = null;

    protected String apiKey = null;

    public OpenAIProvider(String contributionName, Map<String, String> parameters) {
        
        this.contributionName = contributionName;
        
        params = parameters;

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
    public Blob generateImage(String prompt) {

        if (!checkApiKey()) {
            return null;
        }

        // TODO Auto-generated method stub
        // return null;
        throw new UnsupportedOperationException();
    }

    @Override
    public String generateText(String prompt) {

        if (!checkApiKey()) {
            return null;
        }

        // TODO Auto-generated method stub
        // return null;
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return contributionName;
    }

}
