/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.util;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;

public final class CertificateTextFactory {
    private static String CATEGORY_ID_CONSTANT = "KAT";
    private static String QUESTION_ID_CONSTANT = "FRG";
    private static String SUB_QUESTION_ID_CONSTANT = "DFR";
    private static String HEADLINE_CONSTANT = ".RBK";
    private static String DESCRIPTION_CONSTANT = ".HLP";
    private static String YES_ID = "SVAR_JA.RBK";
    private static String NO_ID = "SVAR_NEJ.RBK";

    public static String getDefaultSelectedText(CertificateTextProvider textProvider) {
        return textProvider.get(YES_ID);
    }

    public static String getDefaultUnselectedText(CertificateTextProvider textProvider) {
        return textProvider.get(NO_ID);
    }

    public static String categoryText(CertificateTextProvider textProvider, String id) {
        return getCompleteText(CATEGORY_ID_CONSTANT, HEADLINE_CONSTANT, id, textProvider);
    }

    public static String categoryDescription(CertificateTextProvider textProvider, String id) {
        return getCompleteText(CATEGORY_ID_CONSTANT, DESCRIPTION_CONSTANT, id, textProvider);
    }

    public static String questionText(CertificateTextProvider textProvider, String id) {
        if (isSubQuestion(id)) {
            return getCompleteText(SUB_QUESTION_ID_CONSTANT, HEADLINE_CONSTANT, id, textProvider);
        }
        return getCompleteText(QUESTION_ID_CONSTANT, HEADLINE_CONSTANT, id, textProvider);
    }

    public static String questionDescription(CertificateTextProvider textProvider, String id) {
        if (isSubQuestion(id)) {
            return getCompleteText(SUB_QUESTION_ID_CONSTANT, DESCRIPTION_CONSTANT, id, textProvider);
        }
        return getCompleteText(QUESTION_ID_CONSTANT, DESCRIPTION_CONSTANT, id, textProvider);
    }

    private static String getCompleteText(String constant, String ending, String id, CertificateTextProvider textProvider) {
        return textProvider.get(constant + "_" + id + ending);
    }

    private static boolean isSubQuestion(String id) {
        return id.contains(".");
    }
}
