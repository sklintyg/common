package se.inera.intyg.common.support.modules.support.facade.dto;

public class UserDTO {

    private String hsaId;
    private String name;
    private String role;
    private String loggedInUnit;
    private String loggedInCareProvider;

    public String getHsaId() {
        return hsaId;
    }

    public void setHsaId(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLoggedInUnit() {
        return loggedInUnit;
    }

    public void setLoggedInUnit(String loggedInUnit) {
        this.loggedInUnit = loggedInUnit;
    }

    public String getLoggedInCareProvider() {
        return loggedInCareProvider;
    }

    public void setLoggedInCareProvider(String loggedInCareProvider) {
        this.loggedInCareProvider = loggedInCareProvider;
    }
}
