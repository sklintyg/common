/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag7804.v1.model.converter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;

/**
 * Created by marced on 2018-12-14.
 */
public final class CopyFromUtlatandeHelper {

    private CopyFromUtlatandeHelper() {
    }

    public static void copyFrom(LisjpUtlatandeV1 source, Ag7804UtlatandeV1.Builder target) {

        target.setAvstangningSmittskydd(source.getAvstangningSmittskydd())
            .setUndersokningAvPatienten(source.getUndersokningAvPatienten())
            .setTelefonkontaktMedPatienten(source.getTelefonkontaktMedPatienten())
            .setJournaluppgifter(source.getJournaluppgifter())
            .setAnnatGrundForMU(source.getAnnatGrundForMU())
            .setAnnatGrundForMUBeskrivning(source.getAnnatGrundForMUBeskrivning())

            .setSysselsattning(convertSysselsattning(source.getSysselsattning()))

            .setNuvarandeArbete(source.getNuvarandeArbete())

            .setDiagnoser(convertDiagnos(source.getDiagnoser()))
            .setFunktionsnedsattning(source.getFunktionsnedsattning())
            .setAktivitetsbegransning(source.getAktivitetsbegransning())
            .setPagaendeBehandling(source.getPagaendeBehandling())
            .setPlaneradBehandling(source.getPlaneradBehandling())

            .setSjukskrivningar(convertSjukSkrivningar(source.getSjukskrivningar()))

            .setForsakringsmedicinsktBeslutsstod(source.getForsakringsmedicinsktBeslutsstod())
            .setArbetstidsforlaggning(source.getArbetstidsforlaggning())
            .setArbetstidsforlaggningMotivering(source.getArbetstidsforlaggningMotivering())
            .setArbetstidsforlaggningMotivering(source.getArbetstidsforlaggningMotivering())
            .setArbetsresor(source.getArbetsresor())
            .setPrognos(convertPrognos(source.getPrognos()))

            .setArbetslivsinriktadeAtgarder(convertAtagarder(source.getArbetslivsinriktadeAtgarder()))
            .setArbetslivsinriktadeAtgarderBeskrivning(source.getArbetslivsinriktadeAtgarderBeskrivning())
            .setOvrigt(source.getOvrigt())

            .setOnskarFormedlaDiagnos(null)
            .setKontaktMedAg(null)
            .setAnledningTillKontakt(null);

    }

    /*
     * Converts Fk7804 ArbetslivsinriktadeAtgarder to corresponding ArbetslivsinriktadeAtgarder in AG7804, filtering out
     * those id's missing in AG7804. Both enum id spaces are base on the same kodverk.
     */
    private static List<se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder> convertAtagarder(
        ImmutableList<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder) {
        return arbetslivsinriktadeAtgarder.stream()
            .map(source -> Arrays
                .stream(se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal
                    .values())
                .filter(arbetslivsinriktadeAtgarderVal -> arbetslivsinriktadeAtgarderVal.getId()
                    .equals(source.getTyp().getId()))
                .findFirst().orElse(null))
            .filter(Objects::nonNull)
            .map(se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder::create)
            .collect(Collectors.toList());
    }

    private static se.inera.intyg.common.ag7804.model.internal.Prognos convertPrognos(Prognos prognos) {
        se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp dagTyp = null;
        if (prognos != null && prognos.getTyp() != null) {
            se.inera.intyg.common.ag7804.model.internal.PrognosTyp typ = se.inera.intyg.common.ag7804.model.internal.PrognosTyp
                .fromId(prognos.getTyp().getId());

            if (prognos.getDagarTillArbete() != null) {
                dagTyp = se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp.fromId(prognos.getDagarTillArbete().getId());
            }
            return se.inera.intyg.common.ag7804.model.internal.Prognos.create(typ, dagTyp);
        }
        return null;
    }

    private static List<se.inera.intyg.common.ag7804.model.internal.Sjukskrivning> convertSjukSkrivningar(
        ImmutableList<Sjukskrivning> sjukskrivningar) {
        return sjukskrivningar.stream()
            .map(ss -> se.inera.intyg.common.ag7804.model.internal.Sjukskrivning
                .create(se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad
                    .fromId(ss.getSjukskrivningsgrad().getId()), ss.getPeriod()))
            .collect(Collectors.toList());
    }

    private static List<se.inera.intyg.common.agparent.model.internal.Diagnos> convertDiagnos(ImmutableList<Diagnos> diagnoser) {
        return diagnoser.stream()
            .map(d -> se.inera.intyg.common.agparent.model.internal.Diagnos.create(d.getDiagnosKod(), d.getDiagnosKodSystem(),
                d.getDiagnosBeskrivning(), d.getDiagnosDisplayName()))
            .collect(Collectors.toList());
    }

    /*
     * Both enum type id's share same kodverk, so all id's are shared.
     */
    private static List<se.inera.intyg.common.ag7804.model.internal.Sysselsattning> convertSysselsattning(
        ImmutableList<Sysselsattning> sysselsattning) {
        return sysselsattning.stream()
            .map(s -> se.inera.intyg.common.ag7804.model.internal.Sysselsattning
                .create(se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp.fromId(s.getTyp().getId())))
            .collect(Collectors.toList());
    }
}
