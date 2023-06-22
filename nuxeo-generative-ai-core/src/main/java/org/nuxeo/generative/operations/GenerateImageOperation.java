package org.nuxeo.generative.operations;

import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.server.jaxrs.batch.Batch;
import org.nuxeo.ecm.automation.server.jaxrs.batch.BatchManager;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.generative.ai.GenerativeAI;
import org.nuxeo.generative.ai.GenerativeAIProvider;
import org.nuxeo.generative.commons.ProjectConstant;
import org.nuxeo.runtime.api.Framework;

import java.io.IOException;
import java.util.Base64;

@Operation(id = GenerateImageOperation.ID, category = ProjectConstant.CAT_GENERATIVE_AI, label = "Generate an image",
        description = "Generate an image based on given conditions")
public class GenerateImageOperation {

    public static final String ID = "GenerativeAi.GenerateImage";

    @Param(name = "prompt")
    protected String prompt;


    @Param(name = "imageType", required = false)
    protected String imageType;


    @Param(name = "exclusion", required = false)
    protected String exclusion;

    @OperationMethod
    public Blob run() {
        try {
            Blob result = Framework.getService(GenerativeAI.class)
                    .generateImage(null, prompt, GenerativeAIProvider.IMG_1024);
            if (result != null) {
                BatchManager bm = Framework.getService(BatchManager.class);
                String batchId = bm.initBatch();
                Batch batch = bm.getBatch(batchId);
                batch.addFile("0", result, result.getFilename(), result.getMimeType());
                return new StringBlob(toJson(result.getFilename(), batchId, result).toString(), "application/json");
            }
        } catch (Exception e) {
            return new StringBlob(messageToJson(e.getMessage()).toString(), "application/json");
        }
        return new StringBlob(messageToJson("No image generated, please try again").toString(), "application/json");
    }

    private JSONObject toJson(String title, String batchId, Blob image) throws JSONException, IOException {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("batchId", batchId);
        json.put("content", Base64.getEncoder().encodeToString(image.getByteArray()));
        return json;
    }

    private JSONObject messageToJson(String message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("message", message);
        return json;
    }
}
