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
package se.inera.intyg.common.doi.v1.pdf;

import java.io.File;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import static se.inera.intyg.common.doi.v1.pdf.DoiPdfGenerator.DEFAULT_PDF_TEMPLATE;

/**
 * Created by marced on 2017-10-11.
 * The purpose of this class is to help getting the fieldsnames and structure from a template PDF and it's acroforms
 * fields.
 */
public class ListAcrofields {

    public static void main(String[] args) throws Exception {

        final File file = new ClassPathResource(DEFAULT_PDF_TEMPLATE).getFile();
        PdfReader pdfReader = new PdfReader(file.getAbsolutePath());
        final AcroFields acroFields = pdfReader.getAcroFields();
        final Set<String> fieldNames = acroFields.getFields().keySet();

        fieldNames.stream().forEach(s -> {
            String rawFileName = acroFields.getTranslatedFieldName(s);
            String constantName = rawFileName.toUpperCase().replace(" ", "_").replace("Å", "A").replace("Ä", "A").replace("Ö", "O")
                .replace("/", "_");
            String possibleValues = getStates(acroFields, s);
            String type = getType(acroFields.getFieldType(s));
            String comment = "\n//Type " + type + (possibleValues.length() > 0 ? " - values [" + possibleValues + "]" : "");
            System.out.println(comment + "\nprivate static final String FIELD_" + constantName + " = \"" + rawFileName + "\";");
        });

        fieldNames.stream().forEach(s -> {
            String possibleValues = getStates(acroFields, s);
            String type = getType(acroFields.getFieldType(s));
            System.out.println(acroFields.getTranslatedFieldName(s) + " " + type + " values [" + possibleValues + "]");
        });
    }

    private static String getType(int fieldType) {
        switch (fieldType) {
            case AcroFields.FIELD_TYPE_NONE:
                return "NONE";
            case AcroFields.FIELD_TYPE_PUSHBUTTON:
                return "BUTTON";
            case AcroFields.FIELD_TYPE_CHECKBOX:
                return "CHECKBOX";
            case AcroFields.FIELD_TYPE_RADIOBUTTON:
                return "RADIOBUTTON";
            case AcroFields.FIELD_TYPE_TEXT:
                return "TEXT";
            case AcroFields.FIELD_TYPE_LIST:
                return "LIST";
            case AcroFields.FIELD_TYPE_COMBO:
                return "COMBO";
            case AcroFields.FIELD_TYPE_SIGNATURE:
                return "SIGNATURE";
            default:
                return "?";

        }

    }

    private static String getStates(AcroFields fields, String fieldName) {
        String[] values = fields.getAppearanceStates(fieldName);
        return String.join(",", values);
    }

}
