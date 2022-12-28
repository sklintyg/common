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

package se.inera.intyg.common.fk7263.model.converter;

public class RespConstants {

    public static final String AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_CATEGORY_ID = "avstangningEnligtSmittskyddslagen";
    public static final String AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_CATEGORY_TEXT_ID = "fk7263.label.smittskydd.kategori";
    public static final String AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_SVAR_ID = "1";
    public static final String DIAGNOS_CATEGORY_ID = "diagnos";
    public static final String DIAGNOS_CATEGORY_TEXT_ID = "fk7263.label.diagnosis";
    public static final String HUVUDDIAGNOSKOD_SVAR_ID = "2";
    public static final String HUVUDDIAGNOSKOD_SVAR_TEXT_ID = "fk7263.label.diagnosisCode";
    public static final String DIAGNOS_FORTYDLIGANDE_SVAR_ID = "3";
    public static final String DIAGNOS_FORTYDLIGANDE_SVAR_TEXT_ID = "fk7263.label.diagnosfortydligande";

    public static final String SJUKDOMSFORLOPP_CATEGORY_ID = "sjukdomforlopp";
    public static final String SJUKDOMSFORLOPP_CATEGORY_TEXT_ID = "fk7263.label.progressofdesease";

    public static final String SJUKDOMSFORLOPP_SVAR_ID = "4";

    public static final String FUNKTIONSNEDSATTNING_CATEGORY_ID = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID = "fk7263.label.disabilities";

    public static final String FUNKTIONSNEDSATTNING_SVAR_ID = "5";
    public static final String INTYGET_BASERAS_PA_CATEGORY_ID = "intygetBaserasPa";
    public static final String INTYGET_BASERAS_PA_CATEGORY_TEXT_ID = "fk7263.label.basedon";

    public static final String UNDERSOKNING_AV_PATIENTEN_DELSVAR_ID = "6.1";
    public static final String UNDERSOKNING_AV_PATIENTEN_DELSVAR_TEXT = "Min undersökning av patienten den";

    public static final String TELEFONKONTAKT_MED_PATIENTEN_DELSVAR_ID = "6.2";
    public static final String TELEFONKONTAKT_MED_PATIENTEN_DELSVAR_TEXT = "Min telefonkontakt med patienten den";

    public static final String JOURNALUPPGIFTER_DELSVAR_ID = "6.3";
    public static final String JOURNALUPPGIFTER_DELSVAR_TEXT = "Journaluppgifter, den";

    public static final String ANNAT_DELSVAR_ID = "6.4";
    public static final String ANNAT_DELSVAR_TEXT = "Annat, den";
    public static final String AKTIVITETSBEGRANSNINGAR_CATEGORY_ID = "aktivitetsbegransningar";
    public static final String AKTIVITETSBEGRANSNINGAR_CATEGORY_TEXT_ID = "fk7263.label.limitation";
    public static final String AKTIVITETSBEGRANSNINGAR_SVAR_ID = "7";

    public static final String REKOMMENDATIONER_CATEGORY_ID = "rekommendationer";
    public static final String REKOMMENDATIONER_CATEGORY_TEXT_ID = "fk7263.label.recommendations";
    public static final String REKOMMENDATIONER_KONTAKT_MED_AF_DELSVAR_ID = "8.1";
    public static final String REKOMMENDATIONER_KONTAKT_MED_AF_DELSVAR_TEXT_ID = "fk7263.label.recommendations.contact.jobcenter";
    public static final String REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_ID = "8.2";
    public static final String REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_TEXT_ID = "fk7263.label.recommendations.contact.healthdepartment";
    public static final String REKOMMENDATIONER_OVRIGT_DELSVAR_ID = "8.3";
    public static final String REKOMMENDATIONER_OVRIGT_DELSVAR_TEXT_ID = "fk7263.label.recommendations.contact.other";

}
