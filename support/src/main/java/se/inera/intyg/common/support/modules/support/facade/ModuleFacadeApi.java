package se.inera.intyg.common.support.modules.support.facade;

import java.io.IOException;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

public interface ModuleFacadeApi {

    Certificate getCertificateFromJson(String certificateAsJson) throws ModuleException, IOException;

    String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException, IOException;
}
