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
package se.inera.intyg.common.af00213.v1.model.converter;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.*;
import static se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes.*;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.util.*;

import java.util.List;
import java.util.stream.Collectors;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(Af00213UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        final var config = createCertificateConfig(texts);
        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate, texts))
            .addElements(getCategories(config))
            .addElement(createHarFunktionsnedsattningsQuestion(internalCertificate.getHarFunktionsnedsattning(), config))
            .addElement(createFunktionsnedsattningsQuestion(internalCertificate.getFunktionsnedsattning(), config))
            .addElement(createHarAktivitetsbegransningsQuestion(internalCertificate.getHarAktivitetsbegransning(), config))
            .addElement(createAktivitetsbegransningsQuestion(internalCertificate.getAktivitetsbegransning(), config))
            .addElement(createHarUtredningBehandlingsQuestion(internalCertificate.getHarUtredningBehandling(), config))
            .addElement(createUtredningBehandlingsQuestion(internalCertificate.getUtredningBehandling(), config))
            .addElement(createHarArbetspaverkansQuestion(internalCertificate.getHarArbetetsPaverkan(), config))
            .addElement(createArbetspaverkansQuestion(internalCertificate.getArbetetsPaverkan(), config))
            .addElement(createOvrigtQuestion(internalCertificate.getOvrigt(), config))
            .build();
    }

    private static CertificateConfig createCertificateConfig(CertificateTextProvider textProvider) {
        final var config = new CertificateConfig();
        config.setTextProvider(textProvider);
        addElementsToConfig(config);
        return config;
    }

    private static List<CertificateDataElement> getCategories(CertificateConfig config) {
        return config.getElements()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getConfigType() == CATEGORY)
                .map((entry -> getCategory(config, entry.getKey())))
                .collect(Collectors.toList());
    }

    private static CertificateDataElement getCategory(CertificateConfig config, String id) {
        return CertificateDataElementFactory.element(id, config);
    }

    private static void addElementsToConfig(CertificateConfig config) {
        int index = 0;

        config.addElement(FUNKTIONSNEDSATTNING_CATEGORY_ID, FUNKTIONSNEDSATTNING_SVAR_ID_1, index++, CATEGORY);
        config.addElement(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, FUNKTIONSNEDSATTNING_SVAR_ID_1, FUNKTIONSNEDSATTNING_CATEGORY_ID, index++, UE_RADIO_BOOLEAN);
        config.addElement(FUNKTIONSNEDSATTNING_DELSVAR_ID_12, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12, FUNKTIONSNEDSATTNING_DELSVAR_ID_12, FUNKTIONSNEDSATTNING_DELSVAR_ID_11, index++, UE_TEXTAREA);

        config.addElement(AKTIVITETSBEGRANSNING_CATEGORY_ID, AKTIVITETSBEGRANSNING_SVAR_ID_2, index++, CATEGORY);
        config.addElement(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, AKTIVITETSBEGRANSNING_SVAR_ID_2, AKTIVITETSBEGRANSNING_CATEGORY_ID, index++, UE_RADIO_BOOLEAN);
        config.addElement(AKTIVITETSBEGRANSNING_DELSVAR_ID_22, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22, AKTIVITETSBEGRANSNING_DELSVAR_ID_22, AKTIVITETSBEGRANSNING_DELSVAR_ID_21, index++, UE_TEXTAREA);

        config.addElement(UTREDNING_BEHANDLING_CATEGORY_ID, UTREDNING_BEHANDLING_SVAR_ID_3, index++, CATEGORY);
        config.addElement(UTREDNING_BEHANDLING_DELSVAR_ID_31, UTREDNING_BEHANDLING_SVAR_JSON_ID_31, UTREDNING_BEHANDLING_SVAR_ID_3, UTREDNING_BEHANDLING_CATEGORY_ID, index++, UE_RADIO_BOOLEAN);
        config.addElement(UTREDNING_BEHANDLING_DELSVAR_ID_32, UTREDNING_BEHANDLING_SVAR_JSON_ID_32, UTREDNING_BEHANDLING_DELSVAR_ID_32, UTREDNING_BEHANDLING_DELSVAR_ID_31, index++, UE_TEXTAREA);

        config.addElement(ARBETETS_PAVERKAN_CATEGORY_ID, ARBETETS_PAVERKAN_SVAR_ID_4, index++, CATEGORY);
        config.addElement(ARBETETS_PAVERKAN_DELSVAR_ID_41, ARBETETS_PAVERKAN_SVAR_JSON_ID_41, ARBETETS_PAVERKAN_SVAR_ID_4, ARBETETS_PAVERKAN_CATEGORY_ID, index++, UE_RADIO_BOOLEAN);
        config.addElement(ARBETETS_PAVERKAN_DELSVAR_ID_42, ARBETETS_PAVERKAN_SVAR_JSON_ID_42, ARBETETS_PAVERKAN_DELSVAR_ID_42, ARBETETS_PAVERKAN_DELSVAR_ID_41, index++, UE_TEXTAREA);

        config.addElement(OVRIGT_CATEGORY_ID, OVRIGT_SVAR_ID_5, index++, CATEGORY);
        config.addElement(OVRIGT_DELSVAR_ID_5, OVRIGT_SVAR_JSON_ID_5, OVRIGT_SVAR_ID_5, OVRIGT_CATEGORY_ID, index, UE_TEXTAREA);
    }

    public static CertificateMetadata createMetadata(Af00213UtlatandeV1 internalCertificate,
        CertificateTextProvider texts) {
        return CertificateMetadataFactory.metadata(
                internalCertificate, "Arbetsförmedlingens medicinska utlåtande", texts.get(DESCRIPTION)
        );
    }

    // should value be mapped to id in config?

    public static CertificateDataElement createHarFunktionsnedsattningsQuestion(Boolean harFunktionsnedsattning, CertificateConfig config) {
        final var id = FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, harFunktionsnedsattning);
    }

    public static CertificateDataElement createFunktionsnedsattningsQuestion(String funktionsnedsattning, CertificateConfig config) {
        final var id = FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config),
                CertificateValidationFactory.showIfParent(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, funktionsnedsattning);
    }

    public static CertificateDataElement createHarAktivitetsbegransningsQuestion(Boolean harAktivitetsbegransning, CertificateConfig config) {
        final var id = AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, harAktivitetsbegransning);
    }

    public static CertificateDataElement createAktivitetsbegransningsQuestion(String aktivitetsbegransning, CertificateConfig config) {
        final var id = AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config),
                CertificateValidationFactory.show(id, singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11)),
                CertificateValidationFactory.showIfParent(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, aktivitetsbegransning);
    }

    public static CertificateDataElement createHarUtredningBehandlingsQuestion(Boolean harUtredningBehandling, CertificateConfig config) {
        final var id = UTREDNING_BEHANDLING_DELSVAR_ID_31;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, harUtredningBehandling);
    }

    public static CertificateDataElement createUtredningBehandlingsQuestion(String utredningBehandling, CertificateConfig config) {
        final var id = UTREDNING_BEHANDLING_DELSVAR_ID_32;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config),
                CertificateValidationFactory.showIfParent(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, utredningBehandling);
    }

    public static CertificateDataElement createHarArbetspaverkansQuestion(Boolean harArbetspaverkan, CertificateConfig config) {
        final var id = ARBETETS_PAVERKAN_DELSVAR_ID_41;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, harArbetspaverkan);
    }

    public static CertificateDataElement createArbetspaverkansQuestion(String arbetspaverkan, CertificateConfig config) {
        final var id = ARBETETS_PAVERKAN_DELSVAR_ID_42;
        final var validations = new CertificateDataValidation[]{
                CertificateValidationFactory.mandatory(id, config),
                CertificateValidationFactory.showIfParent(id, config)
        };

        return CertificateDataElementFactory.element(id, config, validations, arbetspaverkan);
    }

    public static CertificateDataElement createOvrigtQuestion(String ovrigt, CertificateConfig config) {
        return CertificateDataElementFactory.element(OVRIGT_DELSVAR_ID_5, config,  new CertificateDataValidation[]{}, ovrigt);
    }
}
