package se.inera.intyg.common.ts_tstrk1062.v1.validator;

import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.ArrayList;
import java.util.List;

public class SvarsWrapper {

    private List<Svar> svarsList;

    public SvarsWrapper(List<Svar> svarsList) {
        initiate(svarsList);
    }

    private void initiate(List<Svar> svarsList) {
        this.svarsList = svarsList;
    }
    
    public List<Svar> getSvarList(String svarsId) {
        List<Svar> svarToReturn = new ArrayList<Svar>();
        for (Svar svar: this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId)) {
                svarToReturn.add(svar);
            }
        }
        return svarToReturn;
    }

    public Svar.Delsvar getDelsvar(String svarsId, String delsvarId) {
        List<Svar> svarToReturn = new ArrayList<Svar>();
        for (Svar svar: this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId)) {
                for (Svar.Delsvar delsvar: svar.getDelsvar()) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        return delsvar;
                    }
                }
            }
        }
        return null;
    }

    public List<Svar.Delsvar> getAllDelsvar(String svarsId, String delsvarId) {
        List<Svar.Delsvar> delSvarToReturn = new ArrayList<Svar.Delsvar>();
        for (Svar svar: this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId)) {
                for (Svar.Delsvar delsvar: svar.getDelsvar()) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        delSvarToReturn.add(delsvar);
                    }
                }
            }
        }
        return delSvarToReturn;
    }

    public Svar.Delsvar getDelsvar(String svarsId, int sequence, String delsvarId) {
        List<Svar> svarToReturn = new ArrayList<Svar>();
        for (Svar svar: this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId) && svar.getInstans() != null && svar.getInstans().intValue() == sequence) {
                for (Svar.Delsvar delsvar: svar.getDelsvar()) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        return delsvar;
                    }
                }
            }
        }
        return null;
    }
}
