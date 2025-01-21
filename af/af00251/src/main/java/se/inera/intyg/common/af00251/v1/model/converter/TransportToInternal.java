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
package se.inera.intyg.common.af00251.v1.model.converter;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_41;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_4;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_21;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_22;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_23;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_2;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_71;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_72;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.BEGRANSNING_SJUKFRANVARO_SVAR_ID_7;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.FORHINDER_DELSVAR_ID_51;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.FORHINDER_SVAR_ID_5;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_3;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_DELSVAR_ID_11;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_DELSVAR_ID_12;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_DELSVAR_ID_13;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.PROGNOS_ATERGANG_DELSVAR_ID_81;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.PROGNOS_ATERGANG_DELSVAR_ID_82;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.PROGNOS_ATERGANG_SVAR_ID_8;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.SJUKFRANVARO_DELSVAR_ID_61;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.SJUKFRANVARO_DELSVAR_ID_62;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.SJUKFRANVARO_SVAR_ID_6;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.UnderlagsTyp;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getDatePeriodTypeContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getPQSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram;
import se.inera.intyg.common.af00251.v1.model.internal.BegransningSjukfranvaro;
import se.inera.intyg.common.af00251.v1.model.internal.PrognosAtergang;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static AF00251UtlatandeV1 convert(Intyg source) throws ConverterException {
        AF00251UtlatandeV1.Builder utlatande = AF00251UtlatandeV1.builder();
        utlatande.setId(source.getIntygsId()
            .getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, PatientInfo.BASIC));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(AF00251UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {

        List<Sjukfranvaro> sjukfranvaroList = new ArrayList<>();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case MEDICINSKUNDERLAG_SVAR_ID_1:
                    handleMedicinsktUnderlag(utlatande, svar);
                    break;
                case ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_2:
                    handleArbetsmarknadspolitisktProgram(utlatande, svar);
                    break;
                case FUNKTIONSNEDSATTNING_SVAR_ID_3:
                    handleFunktionsnedsattning(utlatande, svar);
                    break;
                case AKTIVITETSBEGRANSNING_SVAR_ID_4:
                    handleAktivitetsbegransning(utlatande, svar);
                    break;
                case FORHINDER_SVAR_ID_5:
                    handleForhinder(utlatande, svar);
                    break;
                case SJUKFRANVARO_SVAR_ID_6:
                    handleSjukfranvaro(sjukfranvaroList, svar);
                    break;
                case BEGRANSNING_SJUKFRANVARO_SVAR_ID_7:
                    handleBegransningSjukfranvaro(utlatande, svar);
                    break;
                case PROGNOS_ATERGANG_SVAR_ID_8:
                    handlePrognosAtergang(utlatande, svar);
                    break;
                default:
                    break;
            }
        }

        utlatande.setSjukfranvaro(sjukfranvaroList);

    }

    private static void handleMedicinsktUnderlag(AF00251UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        UnderlagsTyp underlagsTyp = UnderlagsTyp.ANNAT;
        InternalDate datum = null;
        String beskrivning = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case MEDICINSKUNDERLAG_DELSVAR_ID_11:
                    underlagsTyp = UnderlagsTyp.fromId(getCVSvarContent(delsvar).getCode());

                    break;
                case MEDICINSKUNDERLAG_DELSVAR_ID_12:
                    datum = new InternalDate(getStringContent(delsvar));
                    break;
                case MEDICINSKUNDERLAG_DELSVAR_ID_13:
                    beskrivning = getStringContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        switch (underlagsTyp) {
            case ANNAT:
                utlatande.setAnnatDatum(datum);
                utlatande.setAnnatBeskrivning(beskrivning);
                break;
            case UNDERSOKNING:
                utlatande.setUndersokningsDatum(datum);
                break;
            default: // Do nothing

        }
    }

    private static void handleArbetsmarknadspolitisktProgram(AF00251UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {

        final ArbetsmarknadspolitisktProgram.Builder builder = ArbetsmarknadspolitisktProgram.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_21:
                    builder.setMedicinskBedomning(getStringContent(delsvar));
                    break;
                case ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_22:
                    builder.setOmfattning(ArbetsmarknadspolitisktProgram.Omfattning.fromId(getCVSvarContent(delsvar).getCode()));
                    break;
                case ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_23:
                    builder.setOmfattningDeltid((int) getPQSvarContent(delsvar).getValue());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        utlatande.setArbetsmarknadspolitisktProgram(builder.build());
    }

    private static void handleFunktionsnedsattning(AF00251UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FUNKTIONSNEDSATTNING_DELSVAR_ID_31:
                    utlatande.setFunktionsnedsattning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAktivitetsbegransning(AF00251UtlatandeV1.Builder utlatande, Svar svar) {

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case AKTIVITETSBEGRANSNING_DELSVAR_ID_41:
                    utlatande.setAktivitetsbegransning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }


    private static void handleForhinder(AF00251UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FORHINDER_DELSVAR_ID_51:
                    utlatande.setHarForhinder(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSjukfranvaro(List<Sjukfranvaro> sjukfranvaroList, Svar svar) throws ConverterException {
        final Sjukfranvaro.Builder builder = Sjukfranvaro.builder();
        builder.setChecked(true);
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SJUKFRANVARO_DELSVAR_ID_61:
                    builder.setNiva((int) getPQSvarContent(delsvar).getValue());
                    break;
                case SJUKFRANVARO_DELSVAR_ID_62:
                    final DatePeriodType datePeriod = getDatePeriodTypeContent(delsvar);
                    builder.setPeriod(new InternalLocalDateInterval(new InternalDate(datePeriod.getStart()),
                        new InternalDate(datePeriod.getEnd())));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        sjukfranvaroList.add(builder.build());
    }

    private static void handleBegransningSjukfranvaro(AF00251UtlatandeV1.Builder utlatande, Svar svar) {
        BegransningSjukfranvaro.Builder builder = BegransningSjukfranvaro.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_71:
                    builder.setKanBegransas(getBooleanContent(delsvar));
                    break;
                case BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_72:
                    builder.setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        utlatande.setBegransningSjukfranvaro(builder.build());
    }

    private static void handlePrognosAtergang(AF00251UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        PrognosAtergang.Builder builder = PrognosAtergang.builder();

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PROGNOS_ATERGANG_DELSVAR_ID_81:
                    builder.setPrognos(PrognosAtergang.Prognos.fromId(getCVSvarContent(delsvar).getCode()));
                    break;
                case PROGNOS_ATERGANG_DELSVAR_ID_82:
                    builder.setAnpassningar(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        utlatande.setPrognosAtergang(builder.build());
    }

}
