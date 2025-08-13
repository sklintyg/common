package se.inera.intyg.common.support.facade.model.config;

public enum IcfCodesPropertyType {
  FUNKTIONSNEDSATTNINGAR("disability"),
  AKTIVITETSBEGRANSNINGAR("activityLimitation");

  private final String icfCodePropertyName;

  IcfCodesPropertyType(String icfCodePropertyName) {
    this.icfCodePropertyName = icfCodePropertyName;
  }
}
