package se.inera.intyg.common.support.model.common.internal;

import java.io.Serializable;
import lombok.Data;

@Data
public class PaTitle implements Serializable {

    String paTitleCode;
    String paTitleName;
}
