package se.inera.intyg.common.support.modules.support.facade.dto;

public class CertificatePatientDTO {

    private String personId;
    private String firstName;
    private String lastName;
    private String fullName;
    private boolean coordinationNumber;
    private boolean testIndicated;
    private boolean protectedPerson;
    private boolean deceased;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isCoordinationNumber() {
        return coordinationNumber;
    }

    public void setCoordinationNumber(boolean coordinationNumber) {
        this.coordinationNumber = coordinationNumber;
    }

    public boolean isTestIndicated() {
        return testIndicated;
    }

    public void setTestIndicated(boolean testIndicated) {
        this.testIndicated = testIndicated;
    }

    public boolean isProtectedPerson() {
        return protectedPerson;
    }

    public void setProtectedPerson(boolean protectedPerson) {
        this.protectedPerson = protectedPerson;
    }

    public boolean isDeceased() {
        return deceased;
    }

    public void setDeceased(boolean deceased) {
        this.deceased = deceased;
    }
}
