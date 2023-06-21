package org.nuxeo.generative.operations;

import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.generative.ai.GenerativeAI;
import org.nuxeo.generative.ai.GenerativeAIProvider;
import org.nuxeo.generative.commons.ProjectConstant;
import org.nuxeo.runtime.api.Framework;

import java.io.Serializable;

@Operation(id = GenerateImageOperation.ID, category = ProjectConstant.CAT_GENERATIVE_AI, label = "Generate an image",
        description = "Generate an image based on given conditions")
public class GenerateImageOperation {

    public static final String ID = "GenerativeAi.GenerateImage";

    @Context
    protected CoreSession session;

    @Param(name = "path")
    protected String path;

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
                DocumentModel newDoc = session.createDocumentModel(path, result.getFilename(), "Picture");
                newDoc.setPropertyValue("dc:title", result.getFilename());
                newDoc.setPropertyValue("file:content", (Serializable) result);
                newDoc = session.createDocument(newDoc);
                return new StringBlob(toJson(newDoc).toString(), "application/json");
            }
        } catch (Exception e) {
            return new StringBlob(messageToJson(e.getMessage()).toString(), "application/json");
        }
        return new StringBlob(messageToJson("No image generated, please try again").toString(), "application/json");
    }

    private JSONObject toJson(DocumentModel doc) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("uid", doc.getId());
        return json;
    }

    private JSONObject messageToJson(String message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("message", message);
        return json;
    }
}
