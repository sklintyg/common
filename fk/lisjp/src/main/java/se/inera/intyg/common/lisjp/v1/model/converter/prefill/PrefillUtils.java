/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_BOOLEAN_FIELD;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_CVTYPE;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_CVTYPE_CODESYSTEM;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_CVTYPE_CODE_VALUE;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_DATEPERIOD_CONTENT;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_DATE_CONTENT;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_STRING_FIELD;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler.WARNING_INVALID_STRING_MAXLENGTH;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.childElements;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.isStringContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.parseDelsvarType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.StringUtils;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.PrefillEventType;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

final class PrefillUtils {

    private PrefillUtils() {
    }

    static Boolean getValidatedBoolean(Delsvar delsvar) throws PrefillWarningException {
        if (!isStringContent(delsvar)) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_BOOLEAN_FIELD);
        }
        String potentialBooleanString = getStringContent(delsvar);
        if (!Boolean.TRUE.toString().equalsIgnoreCase(potentialBooleanString) && !Boolean.FALSE.toString()
            .equalsIgnoreCase(potentialBooleanString)) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_BOOLEAN_FIELD);
        }
        return Boolean.valueOf(potentialBooleanString);
    }

    static String getValidatedString(Delsvar delsvar, int validMaxLength) throws PrefillWarningException {
        if (!isStringContent(delsvar)) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_STRING_FIELD);
        }
        final String validatedMaxLengthString = getStringContent(delsvar);

        if (validatedMaxLengthString != null && validatedMaxLengthString.length() > validMaxLength) {
            throw new PrefillWarningException(delsvar,
                String.format(WARNING_INVALID_STRING_MAXLENGTH, validMaxLength, validatedMaxLengthString.length()));
        }
        return validatedMaxLengthString;
    }

    static String getValidatedDateString(Delsvar delsvar) throws PrefillWarningException {
        if (!isStringContent(delsvar)) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_STRING_FIELD);
        }

        final String dateString = getStringContent(delsvar);

        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (Exception e) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_DATE_CONTENT);
        }
        return dateString;
    }

    /**
     * Tries to parse a CVType for an element that at least has a code and (valid) codeSystem.
     * Throws {@link PrefillWarningException} if code or codeSystem is missing, or if {@link ConverterException} occurs
     */
    static CVType getValidatedCVTypeContent(Delsvar delsvar, List<String> validCodeSystems) throws PrefillWarningException {
        try {
            final CVType cv = getCVSvarContent(delsvar);

            if (cv == null) {
                throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE);
            }
            if (!validCodeSystems.contains(cv.getCodeSystem())) {
                throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODESYSTEM);
            }
            if (StringUtils.isEmpty(cv.getCode())) {
                throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
            }

            return cv;
        } catch (ConverterException e) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE);
        }
    }

    /**
     * Tries to parse a CVType code for an element that at least has a code and (valid) codeSystem.
     */
    static String getValidatedCVTypeCodeContent(Delsvar delsvar, String validCodeSystem) throws PrefillWarningException {
        return getValidatedCVTypeContent(delsvar, Arrays.asList(validCodeSystem)).getCode();

    }

    static DatePeriodType getValidatedDatePeriodTypeContent(Delsvar delsvar, LocalDate defaultStartDateIfMissing, PrefillResult pr)
        throws PrefillWarningException {
        try {
            DatePeriodType datePeriodType = parseDelsvarType(delsvar, dpNode -> {
                final DatePeriodType tempPeriod = new DatePeriodType();
                childElements(dpNode, child -> {
                    switch (child.getLocalName()) {
                        case "start":
                            tempPeriod.setStart(LocalDate.parse(child.getTextContent()));
                            break;
                        case "end":
                            tempPeriod.setEnd(LocalDate.parse(child.getTextContent()));
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid DatePeriodType element " + child.getLocalName());
                    }
                });

                return tempPeriod;
            });
            //Default startdate handling
            if (StringUtils.isEmpty(datePeriodType.getStart())) {
                pr.addMessage(PrefillEventType.INFO, delsvar, "No startdate provided - defaulting to " + defaultStartDateIfMissing);
                datePeriodType.setStart(defaultStartDateIfMissing);
            }

            return datePeriodType;

        } catch (ConverterException e) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_DATEPERIOD_CONTENT + " (" + e.getMessage() + ")");
        } catch (Exception e) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_DATEPERIOD_CONTENT + " (" + e.getMessage() + ")");
        }
    }

    static String nullToEmpty(LocalDate localDate) {
        return localDate == null ? "" : localDate.toString();
    }
}
