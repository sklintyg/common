package se.inera.intyg.common.support.facade.model.validation;

public class CertificateDataValidation {

    private CertificateDataValidationType type;
    private String questionId; // TODO: Should it be named elementId?
    private String expression;

    public CertificateDataValidationType getType() {
        return type;
    }

    public void setType(CertificateDataValidationType type) {
        this.type = type;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
