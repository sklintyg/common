package se.inera.intyg.common.support.modules.support.facade;

import java.io.IOException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDTO;

public interface ModuleFacadeApi {

    CertificateDTO getCertificateDTOFromJson(String certificateAsJson) throws ModuleException, IOException;

    String getJsonFromCertificateDTO(CertificateDTO certificate, String certificateAsJson) throws ModuleException, IOException;
}
