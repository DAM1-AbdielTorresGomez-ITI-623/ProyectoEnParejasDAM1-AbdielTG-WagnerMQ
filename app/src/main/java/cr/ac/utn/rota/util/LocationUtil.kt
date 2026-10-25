package cr.ac.utn.rota.util

class LocationUtil {
    companion object {
        fun isValidCoordinates(latitude: Double, longitude: Double): Boolean {
            return latitude in -90.0..90.0 && longitude in -180.0..180.0
        }
    }
}
