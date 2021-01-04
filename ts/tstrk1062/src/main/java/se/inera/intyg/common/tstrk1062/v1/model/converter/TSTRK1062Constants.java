/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

public abstract class TSTRK1062Constants {

    public static final String NOT_AVAILABLE = "N/A";
    public static final String PUNKT = ".";
    public static final String SB = "[";
    public static final String EB = "]";


    public static final String ID_JSON_ID = "id";
    public static final String GRUNDDATA_JSON_ID = "grundData";
    public static final String TEXT_VERSION_JSON_ID = "textVersion";
    public static final String SIGNATURE_JSON_ID = "signature";
    public static final String KOMMENTAR_JSON_ID = "kommentar";

    // Kat 1 - Intyget avser
    public static final String INTYG_AVSER_CATEGORY = "intygAvser";
    public static final String INTYG_AVSER_SVAR_ID_1 = "1";
    public static final String INTYG_AVSER_SVAR_JSON_ID = "intygAvser";
    public static final String INTYG_AVSER_DELSVAR_ID_1 = "1.1";
    public static final String INTYG_AVSER_DELSVAR_JSON_ID = "behorigheter";

    // Kat 2 - Id kontroll
    public static final String ID_KONTROLL_CATEGORY = "idKontroll";
    public static final String ID_KONTROLL_SVAR_ID_1 = "2";
    public static final String ID_KONTROLL_SVAR_JSON_ID = "idKontroll";
    public static final String ID_KONTROLL_DELSVAR_ID_1 = "2.1";
    public static final String ID_KONTROLL_DELSVAR_JSON_ID = "typ";

    // Kat 3 - Allmänt
    public static final String ALLMANT_KATEGORI = "allmant";
    public static final String ALLMANT_INMATNING_SVAR_ID = "50";
    public static final String ALLMANT_INMATNING_SVAR_JSON_ID = "diagnosRegistrering";
    public static final String ALLMANT_INMATNING_DELSVAR_ID = "50.1";
    public static final String ALLMANT_INMATNING_DELSVAR_JSON_ID = "typ";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID = "51";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID = "diagnosKodad";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID = "51.1";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID = "diagnosKod";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID = "51.2";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_JSON_ID = "diagnosBeskrivning";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID = "51.3";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID = "diagnosArtal";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID = "diagnosKodSystem";
    public static final String ALLMANT_DIAGNOSKOD_KODAD_KOD_DISPLAYNAME_JSON_ID = "diagnosDisplayName";
    public static final String ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID = "52";
    public static final String ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID = "diagnosFritext";
    public static final String ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID = "52.1";
    public static final String ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID = "diagnosFritext";
    public static final String ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID = "52.2";
    public static final String ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID = "diagnosArtal";

    // Kat 4 - Läkemedelsbehandling
    public static final String LAKEMEDELSBEHANDLING_KATEGORI = "lakemedelsbehandling";
    public static final String LAKEMEDELSBEHANDLING_JSON_ID = "lakemedelsbehandling";
    public static final String LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID = "53";
    public static final String LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID = "53.1";
    public static final String LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID = "harHaft";
    public static final String LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID = "54";
    public static final String LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID = "54.1";
    public static final String LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_JSON_ID = "pagar";
    public static final String LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID = "55";
    public static final String LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID = "55.1";
    public static final String LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID = "aktuell";
    public static final String LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID = "56";
    public static final String LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID = "56.1";
    public static final String LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_JSON_ID = "pagatt";
    public static final String LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID = "57";
    public static final String LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID = "57.1";
    public static final String LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_JSON_ID = "effekt";
    public static final String LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID = "58";
    public static final String LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID = "58.1";
    public static final String LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_JSON_ID = "foljsamhet";
    public static final String LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID = "59";
    public static final String LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID = "59.1";
    public static final String LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID = "avslutadTidpunkt";
    public static final String LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID = "59.2";
    public static final String LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID = "avslutadOrsak";

    // Kat 5 - Symptom, funktionshinder och prognos
    public static final String SYMPTOM_KATEGORI = "symptom";
    public static final String SYMPTOM_BEDOMNING_SVAR_ID = "60";
    public static final String SYMPTOM_BEDOMNING_DELSVAR_ID = "60.1";
    public static final String SYMPTOM_BEDOMNING_DELSVAR_JSON_ID = "bedomningAvSymptom";
    public static final String SYMPTOM_PROGNOS_SVAR_ID = "61";
    public static final String SYMPTOM_PROGNOS_SVAR_JSON_ID = "prognosTillstand";
    public static final String SYMPTOM_PROGNOS_DELSVAR_ID = "61.1";
    public static final String SYMPTOM_PROGNOS_DELSVAR_JSON_ID = "typ";

    // Kat 6 - Övrigt
    public static final String OVRIGT_KATEGORI = "ovrigt";
    public static final String OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID = "32";
    public static final String OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID = "32.1";
    public static final String OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_JSON_ID = "ovrigaKommentarer";

    // Kat 7 - Bedomning
    public static final String BEDOMNING_KATEGORI = "bedomning";
    public static final String BEDOMNING_UPPFYLLER_SVAR_ID = "33";
    public static final String BEDOMNING_UPPFYLLER_SVAR_JSON_ID = "bedomning";
    public static final String BEDOMNING_UPPFYLLER_DELSVAR_ID = "33.1";
    public static final String BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID = "uppfyllerBehorighetskrav";
}
