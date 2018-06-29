package se.inera.intyg.common.pdf.util;

/**
 * Borrowed directly from iText 5.
 */
public final class UnifiedPdfUtil {

    private UnifiedPdfUtil() {

    }

    /**
     * Measurement conversion from millimeters to points.
     * 
     * @param value
     *            a value in millimeters
     * @return a value in points
     * @since 2.1.2
     */
    public static final float millimetersToPoints(final float value) {
        return inchesToPoints(millimetersToInches(value));
    }

    /**
     * Measurement conversion from millimeters to inches.
     * 
     * @param value
     *            a value in millimeters
     * @return a value in inches
     * @since 2.1.2
     */
    public static final float millimetersToInches(final float value) {
        return value / 25.4f;
    }

    /**
     * Measurement conversion from points to millimeters.
     * 
     * @param value
     *            a value in points
     * @return a value in millimeters
     * @since 2.1.2
     */
    public static final float pointsToMillimeters(final float value) {
        return inchesToMillimeters(pointsToInches(value));
    }

    /**
     * Measurement conversion from points to inches.
     * 
     * @param value
     *            a value in points
     * @return a value in inches
     * @since 2.1.2
     */
    public static final float pointsToInches(final float value) {
        return value / 72f;
    }

    /**
     * Measurement conversion from inches to millimeters.
     * 
     * @param value
     *            a value in inches
     * @return a value in millimeters
     * @since 2.1.2
     */
    public static final float inchesToMillimeters(final float value) {
        return value * 25.4f;
    }

    /**
     * Measurement conversion from inches to points.
     * 
     * @param value
     *            a value in inches
     * @return a value in points
     * @since 2.1.2
     */
    public static final float inchesToPoints(final float value) {
        return value * 72f;
    }

}
