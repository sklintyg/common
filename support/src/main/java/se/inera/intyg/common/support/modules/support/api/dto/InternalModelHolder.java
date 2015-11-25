package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class InternalModelHolder {

    private final String internalModel;

    private final String xmlModel;

    public InternalModelHolder(String internalModel, String xmlModel) {
        hasText(internalModel, "'internalModel' must not be empty");
        this.internalModel = internalModel;
        this.xmlModel = xmlModel;
    }

    public InternalModelHolder(String internalModel) {
        this(internalModel, null);
    }

    public String getInternalModel() {
        return internalModel;
    }

    public String getXmlModel() {
        return xmlModel;
    }

}
