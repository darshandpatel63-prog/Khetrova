package com.example.domain

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

data class GpsLatLng(
    val lat: Double,
    val lng: Double,
    val label: String = ""
)

data class GeodesicEdgeMeasurement(
    val fromIndex: Int,
    val toIndex: Int,
    val label: String,
    val lengthMeters: Double,
    val lengthFeet: Double,
    val lengthVaar: Double,
    val bearingDegrees: Double,
    val compassDirection: String
)

data class LocalPoint2D(
    val eastMeters: Double,
    val northMeters: Double,
    val eastFeet: Double,
    val northFeet: Double
)

data class GeodesicPolygonSurveyResult(
    val points: List<GpsLatLng>,
    val localPoints: List<LocalPoint2D>,
    val edges: List<GeodesicEdgeMeasurement>,
    val areaSqMeters: Double,
    val areaSqFeet: Double,
    val perimeterMeters: Double,
    val perimeterFeet: Double,
    val centroid: GpsLatLng,
    val countryConversions: List<ConvertedUnitItem>,
    val equivalentFourSidesFt: List<Double> // [North, East, South, West, Diagonal] for CAD transfer
)

data class GlobalLocationPreset(
    val name: String,
    val countryProfileName: String,
    val centerLat: Double,
    val centerLng: Double,
    val defaultPolygon: List<GpsLatLng>
)

object GeodesicMapAnd3DEngine {

    private const val EARTH_RADIUS_METERS = 6378137.0
    private const val FEET_PER_METER = 3.280839895
    private const val SQ_FEET_PER_SQ_METER = 10.7639104167

    val globalLocationPresets: List<GlobalLocationPreset> = listOf(
        GlobalLocationPreset(
            name = "Gujarat Agricultural Parcel (Rajkot / Saurashtra)",
            countryProfileName = "Gujarat (Saurashtra / Central - 16 Guntha)",
            centerLat = 22.2916,
            centerLng = 70.7932,
            defaultPolygon = listOf(
                GpsLatLng(22.29210, 70.79270, "P1 (NW)"),
                GpsLatLng(22.29215, 70.79365, "P2 (NE)"),
                GpsLatLng(22.29120, 70.79375, "P3 (SE)"),
                GpsLatLng(22.29115, 70.79265, "P4 (SW)")
            )
        ),
        GlobalLocationPreset(
            name = "Surat / Ahmedabad TP Scheme Corner Plot",
            countryProfileName = "Gujarat (Saurashtra / Central - 16 Guntha)",
            centerLat = 21.1702,
            centerLng = 72.8311,
            defaultPolygon = listOf(
                GpsLatLng(21.17045, 72.83085, "P1 (NW)"),
                GpsLatLng(21.17048, 72.83135, "P2 (NE)"),
                GpsLatLng(21.17002, 72.83142, "P3 (SE)"),
                GpsLatLng(21.16998, 72.83080, "P4 (SW)")
            )
        ),
        GlobalLocationPreset(
            name = "Tokyo Urban Tsubo Parcel (Japan 日本)",
            countryProfileName = "Japan (日本 - Tsubo / Tatami / Tan / Cho)",
            centerLat = 35.6812,
            centerLng = 139.7671,
            defaultPolygon = listOf(
                GpsLatLng(35.68145, 139.76685, "P1"),
                GpsLatLng(35.68148, 139.76735, "P2"),
                GpsLatLng(35.68105, 139.76740, "P3"),
                GpsLatLng(35.68100, 139.76680, "P4")
            )
        ),
        GlobalLocationPreset(
            name = "Texas / Midwest Ranch Acreage (USA / UK)",
            countryProfileName = "USA, UK, Canada & Australia (Acre / Rood / Perch)",
            centerLat = 30.2672,
            centerLng = -97.7431,
            defaultPolygon = listOf(
                GpsLatLng(30.26780, -97.74380, "P1"),
                GpsLatLng(30.26785, -97.74240, "P2"),
                GpsLatLng(30.26660, -97.74235, "P3"),
                GpsLatLng(30.26655, -97.74385, "P4")
            )
        ),
        GlobalLocationPreset(
            name = "Bangkok Rai-Ngan-Wa Estate (Thailand)",
            countryProfileName = "Thailand (ประเทศไทย - Rai / Ngan / Tarang Wa)",
            centerLat = 13.7563,
            centerLng = 100.5018,
            defaultPolygon = listOf(
                GpsLatLng(13.75670, 100.50140, "P1"),
                GpsLatLng(13.75675, 100.50220, "P2"),
                GpsLatLng(13.75595, 100.50225, "P3"),
                GpsLatLng(13.75590, 100.50135, "P4")
            )
        ),
        GlobalLocationPreset(
            name = "Dubai / Cairo Dunam & Feddan Parcel (Middle East)",
            countryProfileName = "Middle East, Turkey & Egypt (Feddan / Dunam / Qirat)",
            centerLat = 25.2048,
            centerLng = 55.2708,
            defaultPolygon = listOf(
                GpsLatLng(25.20525, 55.27035, "P1"),
                GpsLatLng(25.20530, 55.27125, "P2"),
                GpsLatLng(25.20445, 55.27130, "P3"),
                GpsLatLng(25.20440, 55.27030, "P4")
            )
        )
    )

    /**
     * Parses a Google Maps URL (e.g. https://www.google.com/maps/@22.2916,70.7932,18z or ?q=22.2916,70.7932)
     * or raw "22.2916, 70.7932" coordinates into a GpsLatLng.
     */
    fun parseGoogleMapsInput(input: String): GpsLatLng? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null

        // Pattern 1: @lat,lng
        val atRegex = Regex("""@(-?\d+\.\d+),(-?\d+\.\d+)""")
        atRegex.find(trimmed)?.let { match ->
            val lat = match.groupValues[1].toDoubleOrNull()
            val lng = match.groupValues[2].toDoubleOrNull()
            if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
                return GpsLatLng(lat, lng)
            }
        }

        // Pattern 2: Any two decimal numbers separated by comma or space
        val pairRegex = Regex("""(-?\d+\.\d+)\s*[,\s]\s*(-?\d+\.\d+)""")
        pairRegex.find(trimmed)?.let { match ->
            val lat = match.groupValues[1].toDoubleOrNull()
            val lng = match.groupValues[2].toDoubleOrNull()
            if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
                return GpsLatLng(lat, lng)
            }
        }
        return null
    }

    /**
     * Builds a 4-corner parcel around a center Lat/Lng with given width & height in meters.
     */
    fun createParcelAroundCenter(
        centerLat: Double,
        centerLng: Double,
        widthMeters: Double = 75.0,
        heightMeters: Double = 60.0
    ): List<GpsLatLng> {
        val dLat = (heightMeters / 2.0) / EARTH_RADIUS_METERS * (180.0 / PI)
        val cosLat = cos(centerLat * PI / 180.0).coerceAtLeast(0.01)
        val dLng = (widthMeters / 2.0) / (EARTH_RADIUS_METERS * cosLat) * (180.0 / PI)

        return listOf(
            GpsLatLng(centerLat + dLat, centerLng - dLng * 0.92, "P1 (NW)"),
            GpsLatLng(centerLat + dLat * 1.04, centerLng + dLng, "P2 (NE)"),
            GpsLatLng(centerLat - dLat, centerLng + dLng * 1.08, "P3 (SE)"),
            GpsLatLng(centerLat - dLat * 0.96, centerLng - dLng, "P4 (SW)")
        )
    }

    fun haversineDistanceMeters(p1: GpsLatLng, p2: GpsLatLng): Double {
        val lat1 = p1.lat * PI / 180.0
        val lat2 = p2.lat * PI / 180.0
        val dLat = (p2.lat - p1.lat) * PI / 180.0
        val dLng = (p2.lng - p1.lng) * PI / 180.0

        val a = sin(dLat / 2.0) * sin(dLat / 2.0) +
            cos(lat1) * cos(lat2) * sin(dLng / 2.0) * sin(dLng / 2.0)
        val c = 2.0 * atan2(sqrt(a.coerceIn(0.0, 1.0)), sqrt((1.0 - a).coerceIn(0.0, 1.0)))
        return EARTH_RADIUS_METERS * c
    }

    fun initialBearingDegrees(p1: GpsLatLng, p2: GpsLatLng): Double {
        val lat1 = p1.lat * PI / 180.0
        val lat2 = p2.lat * PI / 180.0
        val dLng = (p2.lng - p1.lng) * PI / 180.0
        val y = sin(dLng) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLng)
        val brng = atan2(y, x) * 180.0 / PI
        return (brng + 360.0) % 360.0
    }

    private fun compassFromBearing(deg: Double): String {
        val dirs = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val idx = (((deg + 22.5) % 360.0) / 45.0).toInt().coerceIn(0, 7)
        return dirs[idx]
    }

    fun evaluateGeodesicPolygon(
        points: List<GpsLatLng>,
        stateProfile: StateLandProfile
    ): GeodesicPolygonSurveyResult {
        if (points.size < 3) {
            val emptyCentroid = points.firstOrNull() ?: GpsLatLng(22.2916, 70.7932)
            return GeodesicPolygonSurveyResult(
                points = points,
                localPoints = emptyList(),
                edges = emptyList(),
                areaSqMeters = 0.0,
                areaSqFeet = 0.0,
                perimeterMeters = 0.0,
                perimeterFeet = 0.0,
                centroid = emptyCentroid,
                countryConversions = emptyList(),
                equivalentFourSidesFt = listOf(50.0, 40.0, 50.0, 40.0, 64.0)
            )
        }

        val avgLat = points.map { it.lat }.average()
        val avgLng = points.map { it.lng }.average()
        val cosCenter = cos(avgLat * PI / 180.0).coerceAtLeast(0.001)

        // Project onto local tangent plane (East-North meters) relative to first vertex
        val origin = points.first()
        val localPts = points.map { pt ->
            val eastM = (pt.lng - origin.lng) * (PI / 180.0) * EARTH_RADIUS_METERS * cosCenter
            val northM = (pt.lat - origin.lat) * (PI / 180.0) * EARTH_RADIUS_METERS
            LocalPoint2D(
                eastMeters = eastM,
                northMeters = northM,
                eastFeet = eastM * FEET_PER_METER,
                northFeet = northM * FEET_PER_METER
            )
        }

        // Shoelace formula in local tangent plane (exact for land parcels from 1 sq.m to 100 sq.km)
        var signedArea2 = 0.0
        val edges = mutableListOf<GeodesicEdgeMeasurement>()
        var perimeterM = 0.0

        for (i in points.indices) {
            val j = (i + 1) % points.size
            val p1 = localPts[i]
            val p2 = localPts[j]
            signedArea2 += (p1.eastMeters * p2.northMeters - p2.eastMeters * p1.northMeters)

            val distM = haversineDistanceMeters(points[i], points[j])
            val distFt = distM * FEET_PER_METER
            val brng = initialBearingDegrees(points[i], points[j])
            perimeterM += distM

            edges.add(
                GeodesicEdgeMeasurement(
                    fromIndex = i + 1,
                    toIndex = j + 1,
                    label = "Side P${i + 1}→P${j + 1}",
                    lengthMeters = distM,
                    lengthFeet = distFt,
                    lengthVaar = distFt / 3.0,
                    bearingDegrees = brng,
                    compassDirection = compassFromBearing(brng)
                )
            )
        }

        val areaSqM = abs(signedArea2) * 0.5
        val areaSqFt = areaSqM * SQ_FEET_PER_SQ_METER
        val perimeterFt = perimeterM * FEET_PER_METER

        val conversions = stateProfile.units.map { unit ->
            ConvertedUnitItem(
                unit = unit,
                convertedValue = if (unit.sqFeetPerUnit > 0.0) areaSqFt / unit.sqFeetPerUnit else 0.0
            )
        }

        // Extract 4 sides + diagonal for seamless transfer to 4-sided CAD tab
        val fourSides = if (edges.size >= 4) {
            val s0 = max(1.0, edges[0].lengthFeet)
            val s1 = max(1.0, edges[1].lengthFeet)
            val s2 = max(1.0, edges[2].lengthFeet)
            val s3 = max(1.0, edges[3].lengthFeet)
            val diagM = haversineDistanceMeters(points[0], points[2])
            val diagFt = max(1.0, diagM * FEET_PER_METER)
            listOf(s0, s1, s2, s3, diagFt)
        } else {
            val side = sqrt(max(1.0, areaSqFt))
            listOf(side, side, side, side, side * sqrt(2.0))
        }

        return GeodesicPolygonSurveyResult(
            points = points,
            localPoints = localPts,
            edges = edges,
            areaSqMeters = areaSqM,
            areaSqFeet = areaSqFt,
            perimeterMeters = perimeterM,
            perimeterFeet = perimeterFt,
            centroid = GpsLatLng(avgLat, avgLng, "Centroid"),
            countryConversions = conversions,
            equivalentFourSidesFt = fourSides
        )
    }
}
