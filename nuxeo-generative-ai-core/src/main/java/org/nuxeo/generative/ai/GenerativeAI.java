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

import java.io.IOException;

import org.nuxeo.ecm.core.api.Blob;

public interface GenerativeAI {

    public static final String DEFAULT_PROVIDER_NAME = "openai";
    
    // Returns the default provider is name is empty or null
    public GenerativeAIProvider getProvider(String name);
    
    // If provider is null or empty => use the default provider
    public Blob generateImage(String provider, String prompt, String size) throws IOException;
    
    // If provider is null or empty => use the default provider
    public Blob generateImages(String provider, String prompt, int howMany, String size) throws IOException;
    
 // If provider is null or empty => use the default provider
    public String generateText(String provider, String prompt) throws IOException;
}
