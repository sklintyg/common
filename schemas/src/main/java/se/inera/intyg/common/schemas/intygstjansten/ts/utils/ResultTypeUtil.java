package se.inera.intyg.common.schemas.intygstjansten.ts.utils;

import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.inera.intygstjanster.ts.services.v1.ResultatTyp;

public final class ResultTypeUtil {

    private ResultTypeUtil() {
    }

    public static ResultatTyp okResult() {
        ResultatTyp result = new ResultatTyp();
        result.setResultCode(ResultCodeType.OK);
        return result;
    }

    public static ResultatTyp infoResult(String resultText) {
        ResultatTyp result = new ResultatTyp();
        result.setResultCode(ResultCodeType.INFO);
        result.setResultText(resultText);
        return result;
    }

    public static ResultatTyp errorResult(ErrorIdType errorId, String resultText) {
        ResultatTyp result = new ResultatTyp();
        result.setResultCode(ResultCodeType.ERROR);
        result.setErrorId(errorId);
        result.setResultText(resultText);
        return result;
    }
}
