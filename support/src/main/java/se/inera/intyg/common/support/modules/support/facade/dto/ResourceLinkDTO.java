package se.inera.intyg.common.support.modules.support.facade.dto;

public class ResourceLinkDTO {

    private ResourceLinkTypeDTO type;
    private String name;
    private String description;
    private boolean enabled;

    public static ResourceLinkDTO create(ResourceLinkTypeDTO type, String name, String description, boolean enabled) {
        final var resourceLink = new ResourceLinkDTO();
        resourceLink.setType(type);
        resourceLink.setName(name);
        resourceLink.setDescription(description);
        resourceLink.setEnabled(enabled);
        return resourceLink;
    }
    
    public ResourceLinkTypeDTO getType() {
        return type;
    }

    public void setType(ResourceLinkTypeDTO type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
