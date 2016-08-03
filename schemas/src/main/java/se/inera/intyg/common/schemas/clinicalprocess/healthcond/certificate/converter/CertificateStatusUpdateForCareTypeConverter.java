/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.common.enumerations.HandelsekodEnum;
import se.inera.intyg.common.support.modules.support.api.notification.HandelseType;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.riv.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v2.CertificateStatusUpdateForCareType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Handelsekod;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

public final class CertificateStatusUpdateForCareTypeConverter {

    protected static final String TEMPORARY_ARBETSPLATSKOD = "TEMPORARY ARBETSPLATSKOD";
    protected static final String TEMPORARY_POSTADRESS = "TEMPORARY POSTADRESS";
    protected static final String TEMPORARY_POSTNUMMER = "TEMPORARY POSTNUMMER";
    protected static final String TEMPORARY_POSTORT = "TEMPORARY POSTORT";
    protected static final String TEMPORARY_PHONE_NUMBER = "TEMPORARY PHONE NUMBER";

    private static final Logger LOG = LoggerFactory.getLogger(CertificateStatusUpdateForCareTypeConverter.class);

    private static final String HANDELSE_CODESYSTEM = "dfd7bbad-dbe5-4a2f-ba25-f7b9b2cc6b14";

    private CertificateStatusUpdateForCareTypeConverter() {
    }

    public static CertificateStatusUpdateForCareType convert(NotificationMessage notificationMessage, Intyg intyg) {
        CertificateStatusUpdateForCareType destination = new CertificateStatusUpdateForCareType();
        complementIntyg(intyg);
        destination.setIntyg(intyg);
        decorateWithHandelse(destination, notificationMessage);
        decorateWithFragorOchSvar(destination, notificationMessage);
        // TODO Uncomment next line when we have RC6 of schema (and remove log)
        // destination.setRef(notificationMessage.getReference());
        if (notificationMessage.getReference() != null) {
            LOG.error("Here is where we would add the reference '{}', if we had any support in schema. If you see this in production we f'ed up.",
                    notificationMessage.getReference());
        }
        return destination;
    }

    /**
     * This method should only be used for CertificateStatusUpdateForCare. DO NOT MOVE THIS METHOD!
     *
     * It is needed because a utkast might not contain the information needed to meet the requirements of the service
     * contract. And we send not yet signed utkast in CertificateStatusUpdateForCare.
     *
     * The information that is updated in this method is the units address information.
     */
    private static void complementIntyg(Intyg intyg) {
        Enhet enhet = intyg.getSkapadAv().getEnhet();
        if (StringUtils.isBlank(enhet.getArbetsplatskod().getExtension())) {
            enhet.getArbetsplatskod().setExtension(TEMPORARY_ARBETSPLATSKOD);
        }
        if (enhet.getEnhetsnamn() == null) {
            enhet.setEnhetsnamn("");
        }
        if (StringUtils.isBlank(enhet.getPostadress())) {
            enhet.setPostadress(TEMPORARY_POSTADRESS);
        }
        if (StringUtils.isBlank(enhet.getPostnummer())) {
            enhet.setPostnummer(TEMPORARY_POSTNUMMER);
        }
        if (StringUtils.isBlank(enhet.getPostort())) {
            enhet.setPostort(TEMPORARY_POSTORT);
        }
        if (StringUtils.isBlank(enhet.getTelefonnummer())) {
            enhet.setTelefonnummer(TEMPORARY_PHONE_NUMBER);
        }
        if ("".equals(enhet.getEpost())) {
            enhet.setEpost(null);
        }
        if (enhet.getVardgivare().getVardgivarnamn() == null) {
            enhet.getVardgivare().setVardgivarnamn("");
        }
    }

    private static void decorateWithHandelse(CertificateStatusUpdateForCareType statusUpdateType, NotificationMessage notificationMessage) {
        Handelsekod handelseKod = new Handelsekod();
        handelseKod.setCodeSystem(HANDELSE_CODESYSTEM);

        HandelsekodEnum handelseValue = convertToHandelsekod(notificationMessage.getHandelse());
        handelseKod.setCode(handelseValue.value());
        handelseKod.setDisplayName(HandelsekodEnum.getDescription(handelseValue.value()));

        Handelse handelseType = new Handelse();
        handelseType.setHandelsekod(handelseKod);
        handelseType.setTidpunkt(notificationMessage.getHandelseTid());

        statusUpdateType.setHandelse(handelseType);
    }

    private static void decorateWithFragorOchSvar(CertificateStatusUpdateForCareType statusUpdateType, NotificationMessage notificationMessage) {
        FragorOchSvar fosType = new FragorOchSvar();
        fosType.setAntalFragor(notificationMessage.getFragaSvar().getAntalFragor());
        fosType.setAntalHanteradeFragor(notificationMessage.getFragaSvar().getAntalHanteradeFragor());
        fosType.setAntalHanteradeSvar(notificationMessage.getFragaSvar().getAntalHanteradeSvar());
        fosType.setAntalSvar(notificationMessage.getFragaSvar().getAntalSvar());
        statusUpdateType.setFragorOchSvar(fosType);
    }

    protected static HandelsekodEnum convertToHandelsekod(HandelseType handelse) {
        switch (handelse) {
        case FRAGA_FRAN_FK:
            return HandelsekodEnum.NYFRFM;
        case FRAGA_TILL_FK:
            return HandelsekodEnum.NYFRTM;
        case FRAGA_FRAN_FK_HANTERAD:
            return HandelsekodEnum.HANFRA;
        case INTYG_MAKULERAT:
            return HandelsekodEnum.MAKULE;
        case INTYG_SKICKAT_FK:
            return HandelsekodEnum.SKICKA;
        case INTYGSUTKAST_ANDRAT:
            return HandelsekodEnum.ANDRAT;
        case INTYGSUTKAST_RADERAT:
            return HandelsekodEnum.RADERA;
        case INTYGSUTKAST_SIGNERAT:
            return HandelsekodEnum.SIGNAT;
        case INTYGSUTKAST_SKAPAT:
            return HandelsekodEnum.SKAPAT;
        case SVAR_FRAN_FK:
            return HandelsekodEnum.NYSVFM;
        case SVAR_FRAN_FK_HANTERAD:
            return HandelsekodEnum.HANSVA;
        default:
            LOG.error("Could not translate event '{}' to a valid HandelsekodEnum", handelse);
            throw new IllegalArgumentException("Could not translate event " + handelse + " to a valid HandelsekodEnum");
        }
    }

}
