package org.micg.pivotalembrace.validators;

/**
 *
 *
 * @author fsmicdev
 */
public class GeoCoordValidator {

    private static final double MIN_LAT_VALUE = 0;
    private static final double MAX_LAT_VALUE = 180;
    private static final double MIN_LONG_VALUE = 0;
    private static final double MAX_LONG_VALUE = 180;

    public static boolean isValidGeoCoordPair(final Double latitude, final Double longitude) {
        if ((latitude == null) || (longitude == null) ||
                (!GeoCoordValidator.isLatitudeValid(latitude)) || (!GeoCoordValidator.isLongitudeValid(longitude))) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isLatitudeValid(final double latitude) {
        if (latitude < MIN_LAT_VALUE || latitude > MAX_LAT_VALUE) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isLongitudeValid(final double longitude) {
        if (longitude < MIN_LONG_VALUE || longitude > MAX_LONG_VALUE) {
            return false;
        } else {
            return true;
        }
    }
}
