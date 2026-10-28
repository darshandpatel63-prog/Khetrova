package com.example

import com.example.domain.AppLocalizationEngine
import com.example.domain.ConstructionModuleType
import com.example.domain.FarmerAndLandToolsEngine
import com.example.domain.FarmerCropSpec
import com.example.domain.GeodesicMapAnd3DEngine
import com.example.domain.GpsLatLng
import com.example.domain.LandAndConstructionEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun gujaratSaurashtraVighaToGunthaAndVaar_isExact() {
        val gujaratProfile = LandAndConstructionEngine.stateProfiles.first()
        val vighaUnit = gujaratProfile.units.first { it.id == "vigha_saurashtra" }
        val result = LandAndConstructionEngine.convertLandUnits(
            stateProfile = gujaratProfile,
            inputUnit = vighaUnit,
            inputValue = 2.5,
            ratePerInputUnitInr = 1000000.0
        )
        assertEquals(43560.0, result.totalSqFeet, 0.01)
        val guntha = result.convertedItems.first { it.unit.id == "guntha" }.convertedValue
        val vaar = result.convertedItems.first { it.unit.id == "vaar" }.convertedValue
        val acre = result.convertedItems.first { it.unit.id == "acre" }.convertedValue
        assertEquals(40.0, guntha, 0.001)
        assertEquals(4840.0, vaar, 0.01)
        assertEquals(1.0, acre, 0.001)
    }

    @Test
    fun globalCountryProfiles_supportWorldwideTraditionalLandUnits() {
        assertTrue(LandAndConstructionEngine.stateProfiles.size >= 20)
        val japanProfile = LandAndConstructionEngine.stateProfiles.firstOrNull {
            it.stateName.contains("Japan")
        }
        assertNotNull(japanProfile)
        val tsuboUnit = japanProfile!!.units.first { it.id == "tsubo" }
        val res = LandAndConstructionEngine.convertLandUnits(
            stateProfile = japanProfile,
            inputUnit = tsuboUnit,
            inputValue = 100.0,
            ratePerInputUnitInr = 50000.0
        )
        assertEquals(3558.32, res.totalSqFeet, 0.5)
    }

    @Test
    fun geodesicMapAnd3DEngine_calculatesPolygonCutoutSidesAndArea() {
        // Test-only coordinates; the app itself no longer preloads demo plot coordinates.
        val points = listOf(
            GpsLatLng(22.29180, 70.79290, "P1"),
            GpsLatLng(22.29180, 70.79350, "P2"),
            GpsLatLng(22.29140, 70.79350, "P3"),
            GpsLatLng(22.29140, 70.79290, "P4")
        )
        val profile = LandAndConstructionEngine.stateProfiles.first()
        val survey = GeodesicMapAnd3DEngine.evaluateGeodesicPolygon(points, profile)
        assertEquals(4, survey.edges.size)
        assertTrue(survey.areaSqMeters > 2000.0)
        assertTrue(survey.areaSqFeet > 21000.0)
        assertTrue(survey.countryConversions.isNotEmpty())
    }

    @Test
    fun farmerAndLandToolsEngine_calculatesPartitionFencingAndCropYield() {
        val gujaratProfile = LandAndConstructionEngine.stateProfiles.first()
        val partition = FarmerAndLandToolsEngine.calculateLandPartition(
            grossAreaSqFt = 43560.0,
            roadCutPercent = 10.0,
            numberOfHeirs = 3,
            stateProfile = gujaratProfile
        )
        assertEquals(3, partition.shares.size)
        assertEquals(13068.0, partition.shares.first().areaSqFt, 0.5)

        val fencing = FarmerAndLandToolsEngine.calculateFarmFencing(
            perimeterFeet = 800.0,
            poleSpacingFeet = 8.0,
            barbedWireRows = 5
        )
        assertEquals(108, fencing.totalPolesCount)
        assertTrue(fencing.totalBarbedWireWeightKg > 150.0)

        val crop = FarmerAndLandToolsEngine.calculateFarmerCropInputs(
            crop = FarmerCropSpec.COTTON,
            areaSqFt = 43560.0,
            vighaSqFt = 17424.0
        )
        assertEquals(2.5, crop.areaVigha, 0.01)
        assertEquals(55.0, crop.expectedYieldMan20Kg, 0.01)
    }

    @Test
    fun irregularPlotTriangulation_detectsContractorAverageOvercharge() {
        val plot = LandAndConstructionEngine.evaluateIrregularPlot(
            northFt = 50.0,
            eastFt = 40.0,
            southFt = 56.0,
            westFt = 44.0,
            diagonalInputFt = 68.0
        )
        assertTrue(plot.isGeometryValid)
        assertEquals(2226.0, plot.naiveAvgAreaSqFt, 0.01)
        assertEquals(2218.28, plot.exactAreaSqFt, 0.05)
        assertTrue(plot.exactAreaSqFt < plot.naiveAvgAreaSqFt)
        assertEquals(4, plot.vertices.size)
    }

    @Test
    fun constructionEstimator_calculatesTilesCementAndSandAccurately() {
        val boq = LandAndConstructionEngine.estimateConstructionMaterials(
            moduleType = ConstructionModuleType.TILES_FLOORING,
            lengthFt = 20.0,
            widthOrHeightFt = 15.0,
            tileLengthFt = 2.0,
            tileWidthFt = 2.0,
            wastagePercent = 7.0
        )
        assertTrue(boq.netWorkAreaSqFt > 300.0)
        assertEquals(4, boq.items.size)
        assertTrue(boq.totalEstimatedCostInr > 0.0)
    }

    @Test
    fun localizationEngine_supportsOver100GoogleLanguagesOffline() {
        assertTrue(AppLocalizationEngine.allLanguages.size >= 110)
        val guStrings = AppLocalizationEngine.getStringsForLanguage("gu")
        val hiStrings = AppLocalizationEngine.getStringsForLanguage("hi")
        val taStrings = AppLocalizationEngine.getStringsForLanguage("ta")
        val jaStrings = AppLocalizationEngine.getStringsForLanguage("ja")
        assertEquals("જમીન માપ", guStrings.navLandUnits)
        assertEquals("भूमि माप", hiStrings.navLandUnits)
        assertEquals("நில அளவை", taStrings.navLandUnits)
        assertEquals("土地単位", jaStrings.navLandUnits)
    }

    @Test
    fun revenue712AndWaterTankEngine_calculatesExactVighaLitersAndPumpHp() {
        val rev712 = FarmerAndLandToolsEngine.calculate712HectareAreSqm(
            hectares = 1.0,
            ares = 0.0,
            sqMeters = 0.0
        )
        assertEquals(10000.0, rev712.totalSqMeters, 0.01)
        assertEquals(107639.1, rev712.totalSqFeet, 0.5)
        assertTrue(rev712.saurashtraVigha16Guntha > 6.1)
        assertTrue(rev712.kccCropLoanEligibilityInr > 150000.0)

        val tank = FarmerAndLandToolsEngine.calculateWaterTankAndBorewellPump(
            isCircularWell = false,
            lengthOrDiameterFt = 20.0,
            widthFt = 15.0,
            depthFt = 10.0,
            borewellDepthFt = 320.0
        )
        assertEquals(3000.0, tank.volumeCubicFeet, 0.01)
        assertEquals(84950.4, tank.totalCapacityLiters, 1.0)
        assertTrue(tank.recommendedSubmersibleHp >= 5.0)
    }
}

