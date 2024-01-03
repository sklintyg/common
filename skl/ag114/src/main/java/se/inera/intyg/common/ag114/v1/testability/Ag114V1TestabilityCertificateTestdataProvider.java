/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.ag114.v1.testability;

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_1_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_2_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_3_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.testability.FkDataValueUtil.getDataValueMaximalDiagnosisListFk;
import static se.inera.intyg.common.support.facade.util.DataValueUtil.getDataValueBoolean;
import static se.inera.intyg.common.support.facade.util.DataValueUtil.getDataValueDateListMaximal;
import static se.inera.intyg.common.support.facade.util.DataValueUtil.getDataValueDateListMinimal;
import static se.inera.intyg.common.support.facade.util.DataValueUtil.getDataValueDateRange;
import static se.inera.intyg.common.support.facade.util.DataValueUtil.getDataValueInteger;
import static se.inera.intyg.common.support.facade.util.DataValueUtil.getDataValueText;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.util.TestabilityCertificateTestdataProvider;

public class Ag114V1TestabilityCertificateTestdataProvider implements TestabilityCertificateTestdataProvider {

    @Override
    public Map<String, CertificateDataValue> getMinimumValues() {
        final var values = new HashMap<String, CertificateDataValue>();
        final var intygetBaserasPa = getDataValueDateListMinimal(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
            LocalDate.now());
        values.put(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID, intygetBaserasPa);

        final var angeYrkeOchArbetsuppgifter = getDataValueText(NUVARANDE_ARBETE_SVAR_JSON_ID, "Ange yrke och arbetsuppgifter");
        values.put(NUVARANDE_ARBETE_SVAR_ID, angeYrkeOchArbetsuppgifter);

        final var onskarFormedlaDiagnos = getDataValueBoolean(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID, false);
        values.put(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID, onskarFormedlaDiagnos);

        final var nedsattArbetsformoga = getDataValueText(NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID,
            "På vilket sätt medför sjukdomen nedsatt arbetsförmåga?");
        values.put(NEDSATT_ARBETSFORMAGA_SVAR_ID, nedsattArbetsformoga);

        final var arbetsformagaTrotsSjukdom = getDataValueBoolean(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID, false);
        values.put(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID, arbetsformagaTrotsSjukdom);

        final var sjukskrivningsgrad = getDataValueInteger(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID, "%", 50);
        values.put(SJUKSKRIVNINGSGRAD_SVAR_ID, sjukskrivningsgrad);

        final var sjukskrivningsgradPeriod = getDataValueDateRange(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID, LocalDate.now().minusDays(2),
            LocalDate.now());
        values.put(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID, sjukskrivningsgradPeriod);

        return values;
    }

    @Override
    public Map<String, CertificateDataValue> getMaximumValues() {
        final var values = new HashMap<String, CertificateDataValue>();

        final var intygetBaserasPa = getDataValueDateListMaximal(
            List.of(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
                GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1,
                GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1,
                GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1),
            LocalDate.now(), 4);
        values.put(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID, intygetBaserasPa);

        final var annat = getDataValueText(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID, "Ange vad annat är");
        values.put(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID, annat);

        final var angeYrkeOchArbetsuppgifter = getDataValueText(NUVARANDE_ARBETE_SVAR_JSON_ID, "Ange yrke och arbetsuppgifter");
        values.put(NUVARANDE_ARBETE_SVAR_ID, angeYrkeOchArbetsuppgifter);

        final var onskarFormedlaDiagnos = getDataValueBoolean(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID, true);
        values.put(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID, onskarFormedlaDiagnos);

        final var diagnos = getDataValueMaximalDiagnosisListFk(
            List.of(
                DIAGNOSES_LIST_ITEM_1_ID,
                DIAGNOSES_LIST_ITEM_2_ID,
                DIAGNOSES_LIST_ITEM_3_ID
            ),
            List.of(
                Diagnos.create("A00", "ICD_10_SE", "Kolera", "Kolera"),
                Diagnos.create("A20", "ICD_10_SE", "Pest", "Pest"),
                Diagnos.create("A04", "ICD_10_SE", "Andra bakteriella tarminfektioner", "Andra bakteriella tarminfektioner")
            )
        );
        values.put(TYP_AV_DIAGNOS_SVAR_ID, diagnos);

        final var nedsattArbetsformoga = getDataValueText(NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID,
            "På vilket sätt medför sjukdomen nedsatt arbetsförmåga?");
        values.put(NEDSATT_ARBETSFORMAGA_SVAR_ID, nedsattArbetsformoga);

        final var arbetsformagaTrotsSjukdom = getDataValueBoolean(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID, true);
        values.put(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID, arbetsformagaTrotsSjukdom);

        final var arbetsformogaBeskrivning = getDataValueText(ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID,
            "Beskriv arbetsförmågan");
        values.put(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID, arbetsformogaBeskrivning);

        final var sjukskrivningsgrad = getDataValueInteger(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID, "%", 100);
        values.put(SJUKSKRIVNINGSGRAD_SVAR_ID, sjukskrivningsgrad);

        final var sjukskrivningsgradPeriod = getDataValueDateRange(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID, LocalDate.now().minusDays(15),
            LocalDate.now());
        values.put(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID, sjukskrivningsgradPeriod);

        final var ovrigt = getDataValueText(OVRIGT_SVAR_JSON_ID, "Övriga upplysningar till arbetsgivaren");
        values.put(OVRIGT_SVAR_ID, ovrigt);

        final var kontaktOnskas = getDataValueBoolean(KONTAKT_ONSKAS_SVAR_JSON_ID, true);
        values.put(KONTAKT_ONSKAS_SVAR_ID, kontaktOnskas);

        final var anledningTillKontakt = getDataValueText(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID,
            "Ange varför du vill ha kontakt och vem som i första hand ska kontaktas");
        values.put(ANLEDNING_TILL_KONTAKT_DELSVAR_ID, anledningTillKontakt);

        return values;
    }
}
