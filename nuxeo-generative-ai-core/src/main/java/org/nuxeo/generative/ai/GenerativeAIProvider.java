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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.platform.mimetype.interfaces.MimetypeRegistry;
import org.nuxeo.runtime.api.Framework;

public interface GenerativeAIProvider {

    String IMG_512 = "512x512";

    String getName();

    Blob generateImage(String prompt, String size) throws IOException;

    Blob generateImages(String prompt, int howMany, String size) throws IOException;

    String generateText(String prompt) throws IOException;

    /**
     * If fileName is passed and valid, it is used (and the extension is added depending on the received content).<br>
     * If fileName is not passed, the filename is built using the provider and a timestamp.
     */
    static Blob downloadFile(String urlStr, String fileName, String provider) throws IOException {

        Blob result;

        result = Blobs.createBlobWithExtension(".tmp");
        File resultFile = result.getFile();

        URL url = URI.create(urlStr).toURL();
        try (InputStream in = url.openStream();
                ReadableByteChannel rbc = Channels.newChannel(in);
                FileOutputStream fos = new FileOutputStream(resultFile.getAbsolutePath())) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            
            URLConnection connection = url.openConnection();
            String mimeType = connection.getContentType();
            result.setMimeType(mimeType);
            
            MimetypeRegistry mimetypeService = Framework.getService(MimetypeRegistry.class);
            List<String> exts = mimetypeService.getExtensionsFromMimetypeName(mimeType);
            String ext = "";
            if(!exts.isEmpty()) {
                ext = exts.getFirst();
            }
            if(StringUtils.isBlank(fileName)) {
                fileName = StringUtils.isBlank(provider) ? "nuxeo-generative-ai-" : provider + "-";
                String formattedDateTime = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd-HHhmm");
                fileName += formattedDateTime;
            }
            fileName += "." + ext;
            result.setFilename(fileName);
        }

        return result;
    }
}
