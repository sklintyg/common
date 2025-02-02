/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.model.converter;

import static se.inera.intyg.common.sos_parent.model.converter.SosUtlatandeToIntyg.getSharedSvar;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_CODE_SYSTEM;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_CODE_SYSTEM;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UPPGIFT_SAKNAS_CODE;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UPPGIFT_SAKNAS_DISPLAY_NAME;
import static se.inera.intyg.common.support.Constants.KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContent;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getTypAvIntyg;

import java.util.List;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private static final int ADDRESS_MAX_LENGTH = 50;

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(DoiUtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.EXTENDED_WITH_ADDRESS_DETAILS_SOURCE);
        truncatePatientAddressIfNeeded(intyg);
        intyg.setTyp(getTypAvIntyg(KvIntygstyp.DOI));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static void truncatePatientAddressIfNeeded(Intyg intyg) {
        final var patientAddress = intyg.getPatient().getPostadress();
        if (patientAddress.length() > ADDRESS_MAX_LENGTH) {
            intyg.getPatient().setPostadress(patientAddress.substring(0, ADDRESS_MAX_LENGTH));
        }
    }

    private static List<Svar> getSvar(DoiUtlatandeV1 utlatande) {
        List<Svar> svar = getSharedSvar(utlatande);

        // Svar 8
        if (utlatande.getTerminalDodsorsak() != null && (utlatande.getTerminalDodsorsak().getBeskrivning() != null
            || utlatande.getTerminalDodsorsak().getDatum() != null || utlatande.getTerminalDodsorsak().getSpecifikation() != null)) {
            InternalConverterUtil.SvarBuilder dodsorsak = aSvar(DODSORSAK_SVAR_ID);
            if (utlatande.getTerminalDodsorsak().getBeskrivning() != null) {
                dodsorsak.withDelsvar(DODSORSAK_DELSVAR_ID, utlatande.getTerminalDodsorsak().getBeskrivning());
            }
            if (utlatande.getTerminalDodsorsak().getDatum() != null) {
                dodsorsak.withDelsvar(DODSORSAK_DATUM_DELSVAR_ID, getInternalDateContent(utlatande.getTerminalDodsorsak().getDatum()));
            }
            final Specifikation terminalSpecifikation = utlatande.getTerminalDodsorsak().getSpecifikation();
            if (terminalSpecifikation != null) {
                dodsorsak.withDelsvar(DODSORSAK_SPECIFIKATION_DELSVAR_ID,
                    aCV(terminalSpecifikation.getCodeSystem(), terminalSpecifikation.getId(), terminalSpecifikation.getLabel()));
            }
            svar.add(dodsorsak.build());
        }

        // Svar 9
        if (utlatande.getFoljd() != null && !utlatande.getFoljd().isEmpty()) {
            for (int i = 0; i < utlatande.getFoljd().size(); i++) {
                Dodsorsak dodsorsak = utlatande.getFoljd().get(i);
                if (dodsorsak.getBeskrivning() != null || dodsorsak.getDatum() != null || dodsorsak.getSpecifikation() != null) {
                    InternalConverterUtil.SvarBuilder foljdSvar = aSvar(FOLJD_SVAR_ID, i + 1);
                    if (dodsorsak.getBeskrivning() != null) {
                        foljdSvar.withDelsvar(FOLJD_OM_DELSVAR_ID, dodsorsak.getBeskrivning());
                    }
                    if (dodsorsak.getDatum() != null && dodsorsak.getDatum().isValidDate()) {
                        foljdSvar.withDelsvar(FOLJD_DATUM_DELSVAR_ID, getInternalDateContent(dodsorsak.getDatum()));
                    }

                    final Specifikation dodsorsakSpecifikation = dodsorsak.getSpecifikation();
                    if (dodsorsakSpecifikation != null) {
                        foljdSvar.withDelsvar(FOLJD_SPECIFIKATION_DELSVAR_ID,
                            aCV(dodsorsakSpecifikation.getCodeSystem(), dodsorsakSpecifikation.getId(), dodsorsakSpecifikation.getLabel()));
                    }
                    svar.add(foljdSvar.build());
                }
            }
        }

        // Svar 10
        if (utlatande.getBidragandeSjukdomar() != null && !utlatande.getBidragandeSjukdomar().isEmpty()) {
            for (Dodsorsak bidragandeSjukdom : utlatande.getBidragandeSjukdomar()) {
                if (bidragandeSjukdom.getBeskrivning() != null || bidragandeSjukdom.getDatum() != null
                    || bidragandeSjukdom.getSpecifikation() != null) {
                    InternalConverterUtil.SvarBuilder sjukdomSvar = aSvar(BIDRAGANDE_SJUKDOM_SVAR_ID);
                    if (bidragandeSjukdom.getBeskrivning() != null) {
                        sjukdomSvar.withDelsvar(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID, bidragandeSjukdom.getBeskrivning());
                    }
                    if (bidragandeSjukdom.getDatum() != null && bidragandeSjukdom.getDatum().isValidDate()) {
                        sjukdomSvar.withDelsvar(BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID, getInternalDateContent(bidragandeSjukdom.getDatum()));
                    }

                    final Specifikation bidragandeSpecifikation = bidragandeSjukdom.getSpecifikation();
                    if (bidragandeSpecifikation != null) {
                        sjukdomSvar.withDelsvar(BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID,
                            aCV(bidragandeSpecifikation.getCodeSystem(), bidragandeSpecifikation.getId(),
                                bidragandeSpecifikation.getLabel()));
                    }
                    svar.add(sjukdomSvar.build());
                }
            }
        }

        // Svar 11
        if (utlatande.getOperation() != null || utlatande.getOperationDatum() != null || utlatande.getOperationAnledning() != null) {
            InternalConverterUtil.SvarBuilder operation = aSvar(OPERATION_SVAR_ID);
            if (utlatande.getOperation() != null) {
                switch (utlatande.getOperation()) {
                    case JA:
                        operation.withDelsvar(OPERATION_OM_DELSVAR_ID, Boolean.TRUE.toString());
                        break;
                    case NEJ:
                        operation.withDelsvar(OPERATION_OM_DELSVAR_ID, Boolean.FALSE.toString());
                        break;
                    case UPPGIFT_SAKNAS:
                        operation.withDelsvar(OPERATION_OM_DELSVAR_ID,
                            aCV(KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM, UPPGIFT_SAKNAS_CODE, UPPGIFT_SAKNAS_DISPLAY_NAME));
                        break;
                }
            }
            if (utlatande.getOperationDatum() != null && utlatande.getOperationDatum().isValidDate()) {
                operation.withDelsvar(OPERATION_DATUM_DELSVAR_ID, getInternalDateContent(utlatande.getOperationDatum()));
            }
            if (utlatande.getOperationAnledning() != null) {
                operation.withDelsvar(OPERATION_ANLEDNING_DELSVAR_ID, utlatande.getOperationAnledning());
            }
            svar.add(operation.build());
        }

        // Svar 12
        if (utlatande.getForgiftning() != null || utlatande.getForgiftningOrsak() != null || utlatande.getForgiftningDatum() != null
            || utlatande.getForgiftningUppkommelse() != null) {
            InternalConverterUtil.SvarBuilder forgiftning = aSvar(FORGIFTNING_SVAR_ID);
            if (utlatande.getForgiftning() != null) {
                forgiftning.withDelsvar(FORGIFTNING_OM_DELSVAR_ID, utlatande.getForgiftning().toString());
            }
            if (utlatande.getForgiftningOrsak() != null) {
                forgiftning.withDelsvar(FORGIFTNING_ORSAK_DELSVAR_ID,
                    aCV(FORGIFTNING_ORSAK_CODE_SYSTEM,
                        utlatande.getForgiftningOrsak().name(),
                        utlatande.getForgiftningOrsak().getBeskrivning()));
            }
            if (utlatande.getForgiftningDatum() != null) {
                forgiftning.withDelsvar(FORGIFTNING_DATUM_DELSVAR_ID, getInternalDateContent(utlatande.getForgiftningDatum()));
            }
            if (utlatande.getForgiftningUppkommelse() != null) {
                forgiftning.withDelsvar(FORGIFTNING_UPPKOMMELSE_DELSVAR_ID, utlatande.getForgiftningUppkommelse());
            }
            svar.add(forgiftning.build());
        }

        // Svar 13
        if (utlatande.getGrunder() != null && !utlatande.getGrunder().isEmpty()) {
            for (Dodsorsaksgrund grund : utlatande.getGrunder()) {
                svar.add(aSvar(
                    GRUNDER_SVAR_ID).withDelsvar(
                        GRUNDER_DELSVAR_ID, aCV(GRUNDER_CODE_SYSTEM, grund.name(), grund.getBeskrivning()))
                    .build());
            }
        }

        // Svar 14
        addIfNotBlank(svar, LAND_SVAR_ID, LAND_DELSVAR_ID, utlatande.getLand());

        return svar;
    }
}
