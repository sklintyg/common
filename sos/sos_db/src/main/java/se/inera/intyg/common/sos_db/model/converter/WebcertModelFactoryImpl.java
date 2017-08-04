package se.inera.intyg.common.sos_db.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import se.inera.intyg.common.sos_db.model.internal.DbUtlatande;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

public class WebcertModelFactoryImpl implements WebcertModelFactory<DbUtlatande> {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Override
    public DbUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        DbUtlatande.Builder template = DbUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);

        template.setGrundData(grundData);
        template.setTextVersion("1.0");

        return template.build();
    }

    @Override
    public DbUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        throw new UnsupportedOperationException("Cannot create a copy of a signed DB certificate");
    }

    private void populateWithId(DbUtlatande.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.nullToEmpty(utlatandeId).trim().isEmpty()) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }
}
