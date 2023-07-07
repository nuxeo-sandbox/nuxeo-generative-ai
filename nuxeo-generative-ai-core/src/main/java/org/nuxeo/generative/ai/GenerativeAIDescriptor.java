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

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * @since 2021.37
 */
@XObject("configuration")
public class GenerativeAIDescriptor {

    @XNode("defaultProviderName")
    protected String defaultProviderName = GenerativeAI.DEFAULT_PROVIDER_NAME;

    public String getDefaultProviderName() {
        if (StringUtils.isBlank(defaultProviderName)) {
            defaultProviderName = GenerativeAI.DEFAULT_PROVIDER_NAME;
        }
        return defaultProviderName;
    }
}
