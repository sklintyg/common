package se.inera.intyg.common.support.facade.model.config;

public enum IcfCodesPropertyEnum {
  FUNKTIONSNEDSETTNINGAR("disability"),
  AKTIVITETSBEGRANSNINGAR("activityLimitation");

  private final String icfCodePropertyName;

  IcfCodesPropertyEnum(String icfCodePropertyName) {
    this.icfCodePropertyName = icfCodePropertyName;
  }

  public String getIcfCodePropertyName() {
    return icfCodePropertyName;
  }

  public static IcfCodesPropertyEnum fromValue(String value) {
    for (IcfCodesPropertyEnum icfCode : IcfCodesPropertyEnum.values()) {
      if (icfCode.name().equalsIgnoreCase(value) || icfCode.getIcfCodePropertyName()
          .equalsIgnoreCase(value)) {
        return icfCode;
      }
    }
    throw new IllegalArgumentException("No enum constant for value: " + value);
  }
}
