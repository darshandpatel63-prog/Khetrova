package com.example.domain

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

data class LandUnitDefinition(
    val id: String,
    val name: String,
    val localName: String,
    val sqFeetPerUnit: Double,
    val description: String
)

data class StateLandProfile(
    val stateName: String,
    val regionNote: String,
    val vighaSqFt: Double,
    val stampDutyPercent: Double,
    val units: List<LandUnitDefinition>,
    val countryGroup: String = "India (States)"
)

data class ConvertedUnitItem(
    val unit: LandUnitDefinition,
    val convertedValue: Double
)

data class LandConversionResult(
    val stateProfile: StateLandProfile,
    val inputUnit: LandUnitDefinition,
    val inputValue: Double,
    val totalSqFeet: Double,
    val totalSqMeters: Double,
    val convertedItems: List<ConvertedUnitItem>,
    val baseLandValueInr: Double,
    val stampDutyInr: Double,
    val totalLandCostInr: Double
)

data class Vertex2D(
    val x: Float,
    val y: Float,
    val label: String,
    val angleDegrees: Double
)

data class IrregularPlotResult(
    val northFt: Double,
    val eastFt: Double,
    val southFt: Double,
    val westFt: Double,
    val diagonalFt: Double,
    val usedDiagonal: Double,
    val minValidDiagonal: Double,
    val maxValidDiagonal: Double,
    val isGeometryValid: Boolean,
    val validationMessage: String,
    val triangle1AreaSqFt: Double,
    val triangle2AreaSqFt: Double,
    val exactAreaSqFt: Double,
    val naiveAvgAreaSqFt: Double,
    val contractorErrorSqFt: Double,
    val contractorErrorInr: Double,
    val perimeterFt: Double,
    val vertices: List<Vertex2D>,
    val regionalConversions: Map<String, Double>,
    val estimatedPlotValueInr: Double
)

enum class ConstructionModuleType(val title: String, val subtitle: String) {
    TILES_FLOORING("Flooring & Tiles", "Room tiles, boxes, skirting, cement & river sand bedding"),
    BRICKWORK_MASONRY("Brickwork Wall", "Red/fly-ash bricks, 4.5\" or 9\" wall, mortar cement & sand"),
    WALL_PLASTERING("Wall Plastering", "12mm/15mm/20mm internal & external plaster cement & sand"),
    PAINT_PUTTY("Paint & Putty", "Wall putty (kg), primer (L) & emulsion paint (L) for rooms")
}

data class ConstructionBoqItem(
    val materialName: String,
    val specification: String,
    val quantityFormatted: String,
    val unitRateInr: Double,
    val totalCostInr: Double
)

data class ConstructionBoqResult(
    val moduleType: ConstructionModuleType,
    val netWorkAreaSqFt: Double,
    val volumeCuFt: Double,
    val items: List<ConstructionBoqItem>,
    val totalEstimatedCostInr: Double,
    val technicalFormulaNote: String
)

object LandAndConstructionEngine {

    val stateProfiles: List<StateLandProfile> = listOf(
        StateLandProfile(
            stateName = "Gujarat (Saurashtra / Central - 16 Guntha)",
            regionNote = "1 Vigha = 16 Guntha = 17,424 Sq.Ft | 1 Vaar = 9 Sq.Ft",
            vighaSqFt = 17424.0,
            stampDutyPercent = 5.9,
            units = listOf(
                LandUnitDefinition("vigha_saurashtra", "Vigha (16 Guntha)", "વીઘા (૧૬ ગુંઠા)", 17424.0, "Standard Saurashtra, Kutch & Central Gujarat Vigha"),
                LandUnitDefinition("guntha", "Guntha", "ગુંઠા (33x33 ft)", 1089.0, "1 Guntha = 33 ft × 33 ft = 121 Vaar"),
                LandUnitDefinition("vaar", "Vaar / Sq.Yard", "વાર (ગજ)", 9.0, "1 Vaar = 3 ft × 3 ft = 9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "ચોરસ ફૂટ", 1.0, "Standard Imperial Engineering Unit"),
                LandUnitDefinition("sqmeter", "Square Meter", "ચોરસ મીટર", 10.7639104, "7/12 Utara & TP Scheme Metric Standard"),
                LandUnitDefinition("acre", "Acre", "એકર (40 Guntha)", 43560.0, "1 Acre = 40 Guntha = 2.5 Saurashtra Vigha"),
                LandUnitDefinition("hectare", "Hectare", "હેક્ટર (10,000 Sq.M)", 107639.104, "Revenue Record (7/12 & 8A) Standard"),
                LandUnitDefinition("vigha_pucca_guj", "Pucca Vigha (North Guj)", "પાકા વીઘા (ઉત્તર ગુજરાત)", 23232.0, "North Gujarat / Mehsana / Banaskantha Pucca Vigha")
            )
        ),
        StateLandProfile(
            stateName = "Gujarat (North / Pucca Vigha - 23,232 Sq.Ft)",
            regionNote = "1 Pucca Vigha = 23,232 Sq.Ft (2,581.33 Vaar) | 1 Vaar = 9 Sq.Ft",
            vighaSqFt = 23232.0,
            stampDutyPercent = 5.9,
            units = listOf(
                LandUnitDefinition("vigha_pucca_guj", "Pucca Vigha", "પાકા વીઘા", 23232.0, "North Gujarat Standard = 23,232 Sq.Ft"),
                LandUnitDefinition("vigha_kachha_guj", "Kachha Vigha (16 Guntha)", "કચ્ચા વીઘા (૧૬ ગુંઠા)", 17424.0, "16 Guntha = 17,424 Sq.Ft"),
                LandUnitDefinition("guntha", "Guntha", "ગુંઠા", 1089.0, "33 ft × 33 ft = 1,089 Sq.Ft"),
                LandUnitDefinition("vaar", "Vaar / Gaj", "વાર (ગજ)", 9.0, "1 Vaar = 9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "ચોરસ ફૂટ", 1.0, "1 ft × 1 ft"),
                LandUnitDefinition("sqmeter", "Square Meter", "ચોરસ મીટર", 10.7639104, "1 Sq.M = 10.7639 Sq.Ft"),
                LandUnitDefinition("acre", "Acre", "એકર", 43560.0, "43,560 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare", "હેક્ટર", 107639.104, "10,000 Sq.Meters")
            )
        ),
        StateLandProfile(
            stateName = "Maharashtra & Goa (Guntha / Acre / Hectare)",
            regionNote = "1 Guntha = 1,089 Sq.Ft | 40 Guntha = 1 Acre (43,560 Sq.Ft)",
            vighaSqFt = 17424.0,
            stampDutyPercent = 6.0,
            units = listOf(
                LandUnitDefinition("guntha", "Guntha (R / Are)", "गुंठा (Guntha)", 1089.0, "Standard 7/12 Utara unit in Maharashtra"),
                LandUnitDefinition("acre", "Acre (40 Guntha)", "एकर", 43560.0, "1 Acre = 40 Guntha"),
                LandUnitDefinition("hectare", "Hectare (100 R)", "हेक्टर", 107639.104, "1 Hectare = ~98.84 Guntha"),
                LandUnitDefinition("vaar", "Vaar / Gaj (Sq.Yard)", "वार / गज", 9.0, "1 Sq.Yard = 9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "चौरस फूट", 1.0, "Standard residential carpet/built-up unit"),
                LandUnitDefinition("sqmeter", "Square Meter", "चौरस मीटर", 10.7639104, "RERA Carpet Area Standard")
            )
        ),
        StateLandProfile(
            stateName = "Rajasthan & Madhya Pradesh (Pucca / Kachha Bigha)",
            regionNote = "1 Pucca Bigha = 27,225 Sq.Ft (165x165 ft) | 1 Biswa = 1,361.25 Sq.Ft",
            vighaSqFt = 27225.0,
            stampDutyPercent = 6.5,
            units = listOf(
                LandUnitDefinition("pucca_bigha", "Pucca Bigha", "पक्का बीघा", 27225.0, "165 ft × 165 ft = 3,025 Gaj = 20 Biswa"),
                LandUnitDefinition("kachha_bigha_raj", "Kachha Bigha", "कच्चा बीघा", 17424.0, "132 ft × 132 ft = 1,936 Gaj"),
                LandUnitDefinition("biswa", "Biswa (Pucca)", "बिस्वा", 1361.25, "1/20th of Pucca Bigha"),
                LandUnitDefinition("gaj", "Gaj / Vaar (Sq.Yard)", "गज", 9.0, "1 Gaj = 9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "वर्ग फुट", 1.0, "Standard engineering unit"),
                LandUnitDefinition("acre", "Acre", "एकड़", 43560.0, "1 Acre = 1.6 Pucca Bigha"),
                LandUnitDefinition("hectare", "Hectare", "हेक्टेयर", 107639.104, "1 Hectare = 3.95 Pucca Bigha")
            )
        ),
        StateLandProfile(
            stateName = "Uttar Pradesh, Bihar & Uttarakhand (Bigha / Biswa / Dhur)",
            regionNote = "1 Pucca Bigha = 27,225 Sq.Ft | 1 Biswa = 1,361.25 Sq.Ft | 1 Dhur = 68.06 Sq.Ft",
            vighaSqFt = 27225.0,
            stampDutyPercent = 7.0,
            units = listOf(
                LandUnitDefinition("pucca_bigha", "Pucca Bigha", "पक्का बीघा", 27225.0, "20 Biswa = 27,225 Sq.Ft"),
                LandUnitDefinition("kachha_bigha_up", "Kachha Bigha (1/3 Pucca)", "कच्चा बीघा", 9075.0, "1/3rd of Pucca Bigha = 9,075 Sq.Ft"),
                LandUnitDefinition("biswa", "Biswa / Katha", "बिस्वा / कट्ठा", 1361.25, "20 Dhur = 1,361.25 Sq.Ft"),
                LandUnitDefinition("dhur", "Dhur", "धुर", 68.0625, "1/20th of a Biswa = 68.06 Sq.Ft"),
                LandUnitDefinition("gaj", "Gaj (Sq.Yard)", "गज", 9.0, "9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "वर्ग फुट", 1.0, "1 Sq.Ft"),
                LandUnitDefinition("acre", "Acre", "एकड़", 43560.0, "43,560 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare", "हेक्टेयर", 107639.104, "10,000 Sq.Meters")
            )
        ),
        StateLandProfile(
            stateName = "Punjab, Haryana & Himachal (Killa / Kanal / Marla)",
            regionNote = "1 Killa (Acre) = 8 Kanal = 160 Marla = 43,560 Sq.Ft",
            vighaSqFt = 10890.0,
            stampDutyPercent = 6.0,
            units = listOf(
                LandUnitDefinition("killa_acre", "Killa / Acre", "किल्ला / एकड़", 43560.0, "8 Kanal = 43,560 Sq.Ft"),
                LandUnitDefinition("kanal", "Kanal (20 Marla)", "कनाल", 5445.0, "20 Marla = 605 Gaj = 5,445 Sq.Ft"),
                LandUnitDefinition("marla", "Marla (9 Sarsahi)", "मरला", 272.25, "30.25 Gaj = 272.25 Sq.Ft"),
                LandUnitDefinition("sarsahi", "Sarsahi / Karam²", "सरसाही", 30.25, "5.5 ft × 5.5 ft = 30.25 Sq.Ft"),
                LandUnitDefinition("gaj", "Gaj (Sq.Yard)", "गज", 9.0, "9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "वर्ग फुट", 1.0, "1 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare", "हेक्टेयर", 107639.104, "10,000 Sq.Meters")
            )
        ),
        StateLandProfile(
            stateName = "West Bengal, Assam & Odisha (Bigha / Katha / Decimal)",
            regionNote = "1 Bigha = 20 Katha = 14,400 Sq.Ft | 1 Satak (Decimal) = 435.6 Sq.Ft",
            vighaSqFt = 14400.0,
            stampDutyPercent = 6.0,
            units = listOf(
                LandUnitDefinition("bigha_wb", "Bigha (20 Katha)", "বিঘা (Bigha)", 14400.0, "120 ft × 120 ft = 14,400 Sq.Ft"),
                LandUnitDefinition("katha_wb", "Katha (16 Chatak)", "কাঠা (Katha)", 720.0, "1 Katha = 720 Sq.Ft"),
                LandUnitDefinition("decimal_satak", "Decimal / Satak", "শতক (Decimal)", 435.6, "1/100th of an Acre = 435.6 Sq.Ft"),
                LandUnitDefinition("chatak", "Chatak", "ছটাক (Chatak)", 45.0, "1/16th of a Katha = 45 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "বর্গফুট", 1.0, "1 Sq.Ft"),
                LandUnitDefinition("acre", "Acre (100 Decimal)", "একর", 43560.0, "100 Decimal = 43,560 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare", "হেক্টর", 107639.104, "247.1 Decimal")
            )
        ),
        StateLandProfile(
            stateName = "Tamil Nadu, Kerala, Karnataka & AP (Cent / Ground / Ankanam)",
            regionNote = "1 Ground = 2,400 Sq.Ft | 1 Cent = 435.6 Sq.Ft | 1 Guntha = 1,089 Sq.Ft",
            vighaSqFt = 17424.0,
            stampDutyPercent = 7.0,
            units = listOf(
                LandUnitDefinition("cent", "Cent (1/100 Acre)", "Cent (சென்ட்)", 435.6, "100 Cents = 1 Acre (43,560 Sq.Ft)"),
                LandUnitDefinition("ground", "Ground (Chennai/TN)", "Ground (கிரவுண்ட்)", 2400.0, "60 ft × 40 ft = 2,400 Sq.Ft"),
                LandUnitDefinition("guntha", "Guntha (Karnataka/AP)", "Guntha (ಗುಂಟೆ)", 1089.0, "33 ft × 33 ft = 1,089 Sq.Ft"),
                LandUnitDefinition("ankanam", "Ankanam (AP/Telangana)", "Ankanam (అంకణం)", 72.0, "8 Sq.Yards = 72 Sq.Ft"),
                LandUnitDefinition("vaar", "Sq.Yard / Gajam", "Gajam (ಗಜ)", 9.0, "9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "Square Feet", 1.0, "1 Sq.Ft"),
                LandUnitDefinition("acre", "Acre (100 Cents)", "Acre", 43560.0, "43,560 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare (247.1 Cents)", "Hectare", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "India (States)"
        ),
        StateLandProfile(
            stateName = "Gujarat (South - Surat / Navsari / Valsad - 23.5 Guntha)",
            regionNote = "1 South Gujarat Vigha = 23.5 Guntha = 25,591.5 Sq.Ft | 1 Vaar = 9 Sq.Ft",
            vighaSqFt = 25591.5,
            stampDutyPercent = 5.9,
            units = listOf(
                LandUnitDefinition("vigha_south_guj", "South Gujarat Vigha (23.5 Guntha)", "દક્ષિણ ગુજરાત વીઘા (૨૩.૫ ગુંઠા)", 25591.5, "Surat, Navsari, Valsad, Bharuch standard = 25,591.5 Sq.Ft"),
                LandUnitDefinition("vigha_saurashtra", "Saurashtra Vigha (16 Guntha)", "સૌરાષ્ટ્ર વીઘા (૧૬ ગુંઠા)", 17424.0, "16 Guntha = 17,424 Sq.Ft"),
                LandUnitDefinition("guntha", "Guntha", "ગુંઠા", 1089.0, "33 ft × 33 ft = 1,089 Sq.Ft"),
                LandUnitDefinition("vaar", "Vaar / Sq.Yard", "વાર (ગજ)", 9.0, "9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "ચોરસ ફૂટ", 1.0, "1 Sq.Ft"),
                LandUnitDefinition("sqmeter", "Square Meter", "ચોરસ મીટર", 10.7639104, "1 Sq.M = 10.7639 Sq.Ft"),
                LandUnitDefinition("acre", "Acre", "એકર", 43560.0, "43,560 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare", "હેક્ટર", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "India (States)"
        ),
        StateLandProfile(
            stateName = "USA — Texas, California & Southwest (Spanish Vara / Labor / League)",
            regionNote = "Texas Vara² = 7.716 Sq.Ft | 1 Labor = 177.1 Acres | 1 League (Sitio) = 4,428.4 Acres",
            vighaSqFt = 43560.0,
            stampDutyPercent = 2.5,
            units = listOf(
                LandUnitDefinition("acre_us", "US Survey Acre", "Acre (US)", 43560.0, "Standard US Acre = 43,560 Sq.Ft"),
                LandUnitDefinition("texas_vara_sq", "Square Vara (Texas / Southwest)", "Texas Vara²", 7.716049, "1 Texas Vara = 33.333 inches -> 1 Vara² = 7.716 Sq.Ft"),
                LandUnitDefinition("texas_labor", "Labor (Texas / Mexico Ranch Unit)", "Labor (177.1 ac)", 7716049.0, "1,000,000 Square Varas = 177.136 Acres"),
                LandUnitDefinition("texas_league", "League / Sitio (Texas Land Grant)", "League (4,428 ac)", 192901234.0, "25 Labors = 4,428.4 Acres"),
                LandUnitDefinition("sqft", "Square Feet (ft²)", "Sq.Ft", 1.0, "US Residential / Commercial Building Unit"),
                LandUnitDefinition("section_us", "Section / Sq.Mile (640 Acres)", "Section (640 ac)", 27878400.0, "1 Square Mile = 640 Acres"),
                LandUnitDefinition("hectare", "Hectare (ha)", "Hectare", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "USA & Canada (States & Provinces)"
        ),
        StateLandProfile(
            stateName = "USA — Midwest, Plains & Western States (Section / Quarter / Township)",
            regionNote = "1 Township = 36 Sections | 1 Section = 640 Acres | 1 Quarter-Section = 160 Acres",
            vighaSqFt = 43560.0,
            stampDutyPercent = 2.0,
            units = listOf(
                LandUnitDefinition("acre", "Acre (US PLSS)", "Acre", 43560.0, "10 Square Chains = 160 Sq.Rods = 43,560 Sq.Ft"),
                LandUnitDefinition("quarter_section", "Quarter-Section (Homestead 160 Acres)", "Quarter-Section", 6969600.0, "1/4th of a Section = 160 Acres"),
                LandUnitDefinition("forty_us", "Forty (Quarter-Quarter = 40 Acres)", "Forty (40 ac)", 1742400.0, "1/16th of a Section = 40 Acres"),
                LandUnitDefinition("section_us", "Section / Square Mile (640 Acres)", "Section (mi²)", 27878400.0, "US Public Land Survey System = 640 Acres"),
                LandUnitDefinition("perch_rod", "Square Rod / Perch / Pole", "Sq.Rod / Perch", 272.25, "16.5 ft × 16.5 ft = 272.25 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet (ft²)", "Sq.Ft", 1.0, "1 Sq.Ft"),
                LandUnitDefinition("sqyard", "Square Yard (yd²)", "Sq.Yd", 9.0, "9 Sq.Ft")
            ),
            countryGroup = "USA & Canada (States & Provinces)"
        ),
        StateLandProfile(
            stateName = "USA (Louisiana) & Canada (Quebec) — French Arpent / Perche",
            regionNote = "1 Arpent Carré = 100 Perches = 36,801 Sq.Ft (0.8448 Acre = 3,418.9 Sq.M)",
            vighaSqFt = 36800.6,
            stampDutyPercent = 3.0,
            units = listOf(
                LandUnitDefinition("arpent_qc", "Arpent Carré (Quebec / Louisiana)", "Arpent (0.845 ac)", 36800.6, "180 French Feet × 180 French Feet = 3,418.89 Sq.Meters"),
                LandUnitDefinition("perche_qc", "Perche Carrée (1/100 Arpent)", "Perche²", 368.006, "18 French Feet × 18 French Feet = 34.19 Sq.Meters"),
                LandUnitDefinition("acre", "Acre (English / Canadian)", "Acre", 43560.0, "43,560 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet (Pieds Carrés)", "pi² / Sq.Ft", 1.0, "Standard residential floor area"),
                LandUnitDefinition("sqmeter", "Square Meter (Mètre Carré)", "m²", 10.7639104, "Official Cadastre du Québec metric unit"),
                LandUnitDefinition("hectare", "Hectare (ha)", "Hectare", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "USA & Canada (States & Provinces)"
        ),
        StateLandProfile(
            stateName = "UK — England, Scotland & Ireland (Imperial / Scottish / Irish Acre)",
            regionNote = "English Acre = 43,560 Sq.Ft | Scottish Acre = 54,937 Sq.Ft | Irish Acre = 70,560 Sq.Ft",
            vighaSqFt = 43560.0,
            stampDutyPercent = 4.5,
            units = listOf(
                LandUnitDefinition("acre_uk", "English Statute Acre", "Statute Acre", 43560.0, "4 Roods = 160 Perches = 43,560 Sq.Ft"),
                LandUnitDefinition("scottish_acre", "Scottish Acre (Scots Measure)", "Scottish Acre", 54937.1, "1.261 English Acres = 5,103.8 Sq.Meters"),
                LandUnitDefinition("irish_acre", "Irish / Plantation Acre", "Irish Acre", 70560.0, "1.6198 English Acres = 6,555.2 Sq.Meters"),
                LandUnitDefinition("rood_uk", "Rood (1/4 Statute Acre)", "Rood", 10890.0, "40 Perches = 10,890 Sq.Ft"),
                LandUnitDefinition("perch_rod", "Perch / Pole / Lug", "Perch", 272.25, "16.5 ft × 16.5 ft = 272.25 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet (ft²)", "Sq.Ft", 1.0, "UK Commercial & Home Area"),
                LandUnitDefinition("hectare", "Hectare (UK Land Registry)", "Hectare (ha)", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "UK, Europe & Russia (States & Regions)"
        ),
        StateLandProfile(
            stateName = "Germany & Austria — Bavaria, Prussia & Vienna (Tagwerk / Morgen / Joch)",
            regionNote = "Bavarian Tagwerk = 3,407 m² | Prussian Morgen = 2,553 m² | Austrian Joch = 5,755 m²",
            vighaSqFt = 36675.8,
            stampDutyPercent = 5.0,
            units = listOf(
                LandUnitDefinition("tagwerk_by", "Bayerisches Tagwerk (Bavaria)", "Tagwerk (Bayern)", 36675.8, "Bavarian state land unit = 3,407.27 Sq.Meters"),
                LandUnitDefinition("morgen_pr", "Preußischer Morgen (Prussia / North DE)", "Morgen (Preußen)", 27482.6, "Prussian state unit = 2,553.22 Sq.Meters"),
                LandUnitDefinition("joch_at", "Österreichisches Joch (Austria)", "Joch (Austria)", 61942.0, "1,600 Quadratklafter = 5,754.64 Sq.Meters"),
                LandUnitDefinition("are_eu", "Ar (a - 100 m²)", "Ar (100 m²)", 1076.391, "10m × 10m = 100 Sq.Meters"),
                LandUnitDefinition("hectare", "Hektar (ha - 10,000 m²)", "Hektar (ha)", 107639.104, "100 Ar = 10,000 Sq.Meters"),
                LandUnitDefinition("sqmeter", "Quadratmeter (m²)", "m²", 10.7639104, "1 Sq.Meter")
            ),
            countryGroup = "UK, Europe & Russia (States & Regions)"
        ),
        StateLandProfile(
            stateName = "Japan — Tokyo (Kanto) & Kyoto/Osaka (Kansai) (Tsubo / Edo-ma / Kyo-ma)",
            regionNote = "1 Tsubo (坪) = 3.3058 Sq.M | Kanto Edo-ma Jō = 1.548 m² | Kansai Kyō-ma Jō = 1.824 m²",
            vighaSqFt = 10674.96,
            stampDutyPercent = 4.0,
            units = listOf(
                LandUnitDefinition("tsubo", "Tsubo (坪)", "坪 (Tsubo)", 35.58317, "Standard Japanese real estate unit = 2 Tatami = 3.3058 m²"),
                LandUnitDefinition("tatami_edo", "Edo-ma Jō (江戸間畳 - Tokyo/Kanto)", "江戸間畳 (Tokyo)", 16.6625, "Tokyo/Kanto Tatami mat = 1.548 m² (16.66 Sq.Ft)"),
                LandUnitDefinition("tatami_kyo", "Kyō-ma Jō (京間畳 - Kyoto/Osaka)", "京間畳 (Kansai)", 19.6334, "Kyoto/Osaka/Kansai Tatami mat = 1.824 m² (19.63 Sq.Ft)"),
                LandUnitDefinition("se_jp", "Se (畝 - 30 Tsubo)", "畝 (Se)", 1067.495, "30 Tsubo = 99.17 m² agricultural unit"),
                LandUnitDefinition("tan_jp", "Tan (反 - 300 Tsubo)", "反 (Tan)", 10674.95, "10 Se = 300 Tsubo = 991.74 m²"),
                LandUnitDefinition("cho_jp", "Chō (町 - 3,000 Tsubo)", "町 (Chō)", 106749.5, "10 Tan = 3,000 Tsubo = 9,917.36 m² (~1 Hectare)"),
                LandUnitDefinition("sqmeter", "Square Meter (平米)", "㎡ (Heibei)", 10.7639104, "Statutory Japanese registry unit"),
                LandUnitDefinition("hectare", "Hectare (ヘクタール)", "ha", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "South Korea & Taiwan (대한민국 / 台灣 - Pyeong / Ping / Chia)",
            regionNote = "1 Pyeong (평 / 坪) = 3.3058 Sq.M (35.58 Sq.Ft) | 1 Chia (甲) = 2,934 Ping",
            vighaSqFt = 10674.96,
            stampDutyPercent = 4.6,
            units = listOf(
                LandUnitDefinition("pyeong_kr", "Pyeong / Ping (평 / 坪)", "평 / 坪", 35.58317, "Standard Korean & Taiwanese apartment/land unit = 3.3058 m²"),
                LandUnitDefinition("danbo_kr", "Danbo (단보 - 300 Pyeong)", "단보 (段步)", 10674.95, "300 Pyeong = 991.74 m²"),
                LandUnitDefinition("jeongbo_kr", "Jeongbo (정보 - 3,000 Pyeong)", "정보 (町步)", 106749.5, "3,000 Pyeong = 9,917.36 m²"),
                LandUnitDefinition("chia_tw", "Chia (甲 - Taiwan Farmland)", "甲 (Chia)", 104398.9, "Taiwan agricultural unit = 2,934 Ping = 9,699.17 m²"),
                LandUnitDefinition("sqmeter", "Square Meter (제곱미터)", "㎡", 10.7639104, "Official cadastral metric unit"),
                LandUnitDefinition("sqft", "Square Feet", "Sq.Ft", 1.0, "International reference")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "China, Hong Kong & Macau (中国 / 香港 / 澳门 - Mu / Fen / Qing / Sq.Ft)",
            regionNote = "1 Mu (亩) = 666.67 Sq.M (7,175.94 Sq.Ft) | 15 Mu = 1 Hectare",
            vighaSqFt = 7175.94,
            stampDutyPercent = 3.0,
            units = listOf(
                LandUnitDefinition("mu_cn", "Mu (亩)", "亩 (Mu)", 7175.94, "1/15th of a Hectare = 666.67 Sq.Meters"),
                LandUnitDefinition("fen_cn", "Fen (分 - 1/10 Mu)", "分 (Fen)", 717.594, "66.67 Sq.Meters"),
                LandUnitDefinition("qing_cn", "Qing (顷 - 100 Mu)", "顷 (Qing)", 717594.0, "100 Mu = 6.667 Hectares"),
                LandUnitDefinition("sq_chi", "Square Chi (平方尺)", "平方尺", 1.19599, "Traditional Chinese square foot = 1/9 Sq.Meter"),
                LandUnitDefinition("sqft", "Square Feet (香港平方呎)", "平方呎 (Sq.Ft)", 1.0, "Hong Kong standard property measurement"),
                LandUnitDefinition("sqmeter", "Square Meter (平方米)", "平方米 (㎡)", 10.7639104, "Mainland China statutory property unit"),
                LandUnitDefinition("hectare", "Hectare (公顷)", "公顷 (ha)", 107639.104, "15 Mu = 10,000 Sq.Meters")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "Pakistan — Punjab, Sindh & KPK (Marla 272 vs Lahore 225 / Kanal / Jareeb)",
            regionNote = "Revenue Marla = 272.25 Sq.Ft | Urban Lahore Marla = 225 Sq.Ft | 1 Kanal = 20 Marla",
            vighaSqFt = 21780.0,
            stampDutyPercent = 4.5,
            units = listOf(
                LandUnitDefinition("marla_rev", "Revenue Marla (272.25 Sq.Ft)", "مرلہ (272.25 ft²)", 272.25, "Standard Revenue Marla = 9 Karam² = 30.25 Sq.Yd"),
                LandUnitDefinition("marla_lhr", "Urban / Lahore Marla (225 Sq.Ft)", "لاہور مرلہ (225 ft²)", 225.0, "Lahore / Urban Housing Society Marla = 25 Sq.Yd"),
                LandUnitDefinition("kanal_pk", "Kanal (20 Revenue Marla)", "کنال (Kanal)", 5445.0, "20 Marla = 605 Sq.Yards = 5,445 Sq.Ft"),
                LandUnitDefinition("jareeb_sindh", "Jareeb (Sindh - 1/2 Acre)", "جریب (Jareeb)", 21780.0, "Sindh agricultural unit = 4 Kanal = 21,780 Sq.Ft"),
                LandUnitDefinition("killa_ghumaon", "Killa / Ghumaon / Acre (8 Kanal)", "کلہ / ایکڑ", 43560.0, "8 Kanal = 160 Marla = 43,560 Sq.Ft"),
                LandUnitDefinition("sqyard", "Gaj / Square Yard (گز)", "گز (Sq.Yd)", 9.0, "Karachi / Sindh urban plot standard = 9 Sq.Ft"),
                LandUnitDefinition("sqft", "Square Feet", "Sq.Ft", 1.0, "1 Sq.Ft")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "Thailand (ประเทศไทย - Rai / Ngan / Tarang Wa)",
            regionNote = "1 Rai (ไร่) = 4 Ngan (งาน) = 400 Tarang Wa (ตารางวา) = 1,600 Sq.M",
            vighaSqFt = 17222.256,
            stampDutyPercent = 3.3,
            units = listOf(
                LandUnitDefinition("rai_th", "Rai (ไร่)", "ไร่ (Rai)", 17222.256, "4 Ngan = 1,600 Sq.Meters = 17,222.26 Sq.Ft"),
                LandUnitDefinition("ngan_th", "Ngan (งาน)", "งาน (Ngan)", 4305.564, "100 Tarang Wa = 400 Sq.Meters"),
                LandUnitDefinition("tarang_wa", "Tarang Wa (ตารางวา)", "ตารางวา (Sq.Wa)", 43.05564, "2m × 2m = 4 Sq.Meters = 43.06 Sq.Ft"),
                LandUnitDefinition("sqmeter", "Square Meter (ตารางเมตร)", "ตร.ม. (Sq.M)", 10.7639104, "1 Sq.Meter"),
                LandUnitDefinition("acre", "Acre (เอเคอร์)", "Acre", 43560.0, "1 Acre = 2.529 Rai"),
                LandUnitDefinition("hectare", "Hectare (เฮกตาร์)", "Hectare", 107639.104, "1 Hectare = 6.25 Rai")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "Nepal (Hills Ropani vs Terai Bigha), Sri Lanka & Bangladesh",
            regionNote = "Nepal Hills: 1 Ropani = 16 Aana (5,476 Sq.Ft) | Terai: 1 Bigha = 20 Kattha (72,900 Sq.Ft)",
            vighaSqFt = 72900.0,
            stampDutyPercent = 5.0,
            units = listOf(
                LandUnitDefinition("ropani_np", "Ropani (रोपनी - Nepal Hills)", "रोपनी (Ropani)", 5476.0, "16 Aana = 64 Paisa = 508.74 Sq.Meters"),
                LandUnitDefinition("aana_np", "Aana (आना - 1/16 Ropani)", "आना (Aana)", 342.25, "4 Paisa = 31.80 Sq.Meters"),
                LandUnitDefinition("paisa_np", "Paisa (पैसा - 1/4 Aana)", "पैसा (Paisa)", 85.5625, "4 Daam = 7.95 Sq.Meters"),
                LandUnitDefinition("bigha_np", "Terai Bigha (तराई बिघा - Nepal)", "बिघा (72,900 ft²)", 72900.0, "20 Kattha = 6,772.63 Sq.Meters"),
                LandUnitDefinition("kattha_np", "Terai Kattha (कठ्ठा - 20 Dhur)", "कठ्ठा (Kattha)", 3645.0, "20 Dhur = 338.63 Sq.Meters"),
                LandUnitDefinition("dhur_np", "Terai Dhur (धुर)", "धुर (Dhur)", 182.25, "16.93 Sq.Meters"),
                LandUnitDefinition("perch_lk", "Perch (Sri Lanka પર્ચ)", "Perch (පර්චසය)", 272.25, "Sri Lanka standard = 25.29 Sq.Meters"),
                LandUnitDefinition("kani_bd", "Kani (কানি - Bangladesh)", "কানি (Kani)", 17280.0, "20 Ganda = 120 Decimal = 17,280 Sq.Ft")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "Australia & New Zealand — NSW, Victoria, QLD & WA (Hectare / Perch / Acre)",
            regionNote = "1 Hectare = 2.471 Acres | 1 Acre = 4 Roods = 160 Perches (43,560 Sq.Ft)",
            vighaSqFt = 43560.0,
            stampDutyPercent = 4.2,
            units = listOf(
                LandUnitDefinition("hectare", "Hectare (AU/NZ Rural Standard)", "Hectare (ha)", 107639.104, "10,000 Sq.Meters = 2.471 Acres"),
                LandUnitDefinition("sqmeter", "Square Meter (AU/NZ Urban Block)", "Sq.M (m²)", 10.7639104, "Standard suburban block unit (e.g. 600 m²)"),
                LandUnitDefinition("perch_au", "Perch (Queensland / Older Titles)", "Perch (25.29 m²)", 272.25, "16.5 ft × 16.5 ft = 272.25 Sq.Ft (16 Perches = 405 m²)"),
                LandUnitDefinition("rood_au", "Rood (Quarter-Acre Block = 1,012 m²)", "Rood (1/4 ac)", 10890.0, "Traditional Aussie/Kiwi Quarter-Acre = 10,890 Sq.Ft"),
                LandUnitDefinition("acre", "Acre (40 Perches × 4 Roods)", "Acre", 43560.0, "43,560 Sq.Ft = 4,046.86 m²"),
                LandUnitDefinition("sqft", "Square Feet", "Sq.Ft", 1.0, "Commercial leasing reference")
            ),
            countryGroup = "Asia-Pacific (Countries & Regions)"
        ),
        StateLandProfile(
            stateName = "Middle East, Turkey & Egypt (UAE / Saudi / Egypt / Jordan - Feddan / Dunam)",
            regionNote = "1 Feddan (فدان) = 24 Qirat = 4,200.83 Sq.M | 1 Dunam/Dönüm = 1,000 Sq.M",
            vighaSqFt = 45217.4,
            stampDutyPercent = 4.0,
            units = listOf(
                LandUnitDefinition("feddan_eg", "Feddan (فدان - Egypt/Sudan/Arabia)", "فدان (Feddan)", 45217.4, "24 Qirat = 4,200.83 Sq.Meters = 1.038 Acres"),
                LandUnitDefinition("qirat_eg", "Qirat (قيراط - 1/24 Feddan)", "قيراط (Qirat)", 1884.06, "24 Sahm = 175.035 Sq.Meters"),
                LandUnitDefinition("sahm_eg", "Sahm (سهم - 1/24 Qirat)", "سهم (Sahm)", 78.50, "7.293 Sq.Meters"),
                LandUnitDefinition("dunam_metric", "Metric Dunam / Dönüm (دونم)", "دونم / Dönüm", 10763.91, "Jordan, Palestine, Israel, Iraq, Turkey = 1,000 Sq.Meters"),
                LandUnitDefinition("donum_cy", "Cypriot Donum (Κύπρος)", "Donum (14,400 ft²)", 14400.0, "Cyprus standard = 1,337.8 Sq.Meters"),
                LandUnitDefinition("sqmeter", "Square Meter (متر مربع)", "م² (Sq.M)", 10.7639104, "Gulf / UAE / Saudi Municipality Standard"),
                LandUnitDefinition("sqft", "Square Feet (قدم مربع)", "Sq.Ft", 1.0, "Dubai DLD Property Standard")
            ),
            countryGroup = "Middle East, Africa & Latin America"
        ),
        StateLandProfile(
            stateName = "Spain & Latin America (Manzana / Fanegada / Cuadra / Vara²)",
            regionNote = "1 Manzana = 10,000 Varas² (6,988.96 Sq.M) | 1 Fanegada = 6,400 Sq.M",
            vighaSqFt = 75228.5,
            stampDutyPercent = 4.5,
            units = listOf(
                LandUnitDefinition("manzana_latam", "Manzana (Central America)", "Manzana", 75228.5, "10,000 Varas² = 6,988.96 Sq.Meters"),
                LandUnitDefinition("fanegada_co", "Fanegada / Plaza (Colombia / Castile)", "Fanegada", 68889.0, "80m × 80m = 6,400 Sq.Meters"),
                LandUnitDefinition("cuadra_ar", "Cuadra (Argentina / Uruguay)", "Cuadra", 181630.0, "130m × 130m = 16,874 Sq.Meters"),
                LandUnitDefinition("tahulla_es", "Tahúlla (Murcia / Valencia Spain)", "Tahúlla", 12034.0, "1,118 Sq.Meters irrigated orchard unit"),
                LandUnitDefinition("vara_cuadrada", "Vara Cuadrada (vara²)", "Vara²", 7.52285, "0.8359m × 0.8359m = 0.6989 Sq.Meters"),
                LandUnitDefinition("caballeria", "Caballería (Mexico / Cuba)", "Caballería", 4843760.0, "45 Hectares = 450,000 Sq.Meters"),
                LandUnitDefinition("hectare", "Hectárea (ha)", "Hectárea", 107639.104, "10,000 Sq.Meters"),
                LandUnitDefinition("sqmeter", "Metro Cuadrado (m²)", "m²", 10.7639104, "1 Sq.Meter")
            ),
            countryGroup = "Middle East, Africa & Latin America"
        ),
        StateLandProfile(
            stateName = "Brazil — São Paulo, Minas Gerais, Goiás & Bahia (Alqueires by State)",
            regionNote = "SP Paulista = 24,200 m² | MG/GO Mineiro = 48,400 m² | BA Baiano = 96,800 m²",
            vighaSqFt = 260486.6,
            stampDutyPercent = 3.5,
            units = listOf(
                LandUnitDefinition("alqueire_sp", "Alqueire Paulista (São Paulo / Paraná)", "Alqueire SP", 260486.6, "2.42 Hectares = 24,200 Sq.Meters"),
                LandUnitDefinition("alqueire_mg", "Alqueire Mineiro / Goiano (MG / GO)", "Alqueire MG/GO", 520973.2, "4.84 Hectares = 48,400 Sq.Meters"),
                LandUnitDefinition("alqueire_ba", "Alqueire Baiano (Bahia State)", "Alqueire BA", 1041946.4, "9.68 Hectares = 96,800 Sq.Meters"),
                LandUnitDefinition("alqueire_norte", "Alqueire do Norte (North Brazil)", "Alqueire Norte", 293046.2, "2.7225 Hectares = 27,225 Sq.Meters"),
                LandUnitDefinition("tarefa_br", "Tarefa (Nordeste / Sergipe / Ceará)", "Tarefa", 46887.4, "4,356 Sq.Meters (~1 Acre)"),
                LandUnitDefinition("hectare", "Hectare (ha)", "Hectare", 107639.104, "10,000 Sq.Meters"),
                LandUnitDefinition("sqmeter", "Metro Quadrado (m²)", "m²", 10.7639104, "1 Sq.Meter")
            ),
            countryGroup = "Middle East, Africa & Latin America"
        ),
        StateLandProfile(
            stateName = "Russia, Ukraine & Central Asia (Sotka / Dessiatina / Hectare)",
            regionNote = "1 Sotka (Сотка) = 100 Sq.M (1,076.39 Sq.Ft) | 100 Sotka = 1 Hectare",
            vighaSqFt = 10763.91,
            stampDutyPercent = 3.0,
            units = listOf(
                LandUnitDefinition("sotka_ru", "Sotka / Are (Сотка)", "Сотка (100 м²)", 1076.391, "10m × 10m = 100 Sq.Meters Dacha/Garden plot unit"),
                LandUnitDefinition("hectare", "Hectare (Гектар - 100 Sotka)", "Гектар (га)", 107639.104, "10,000 Sq.Meters"),
                LandUnitDefinition("dessiatina_ru", "Dessiatina (Десятина)", "Десятина", 117600.0, "2,400 Sq.Sazhen = 10,925.4 Sq.Meters"),
                LandUnitDefinition("sqmeter", "Square Meter (Квадратный метр)", "м² (Sq.M)", 10.7639104, "1 Sq.Meter"),
                LandUnitDefinition("sqft", "Square Feet", "Sq.Ft", 1.0, "International reference")
            ),
            countryGroup = "UK, Europe & Russia (States & Regions)"
        ),
        StateLandProfile(
            stateName = "Continental Europe — France, Italy & Greece (Hectare / Arpent / Stremma)",
            regionNote = "1 Hectare = 100 Are = 10,000 Sq.M | 1 Morgen = 2,500 Sq.M | 1 Stremma = 1,000 Sq.M",
            vighaSqFt = 26909.78,
            stampDutyPercent = 5.0,
            units = listOf(
                LandUnitDefinition("hectare", "Hectare (ha)", "Hectare (ha)", 107639.104, "100 Are = 10,000 Sq.Meters"),
                LandUnitDefinition("are_eu", "Are (a - 100 m²)", "Are (a)", 1076.391, "10m × 10m = 100 Sq.Meters"),
                LandUnitDefinition("morgen_de", "Morgen (Poland / Netherlands)", "Morgen", 26909.78, "1/4 Hectare = 2,500 Sq.Meters"),
                LandUnitDefinition("stremma_gr", "Stremma (Στρέμμα - Greece)", "Στρέμμα", 10763.91, "Royal Greek Stremma = 1,000 Sq.Meters"),
                LandUnitDefinition("arpent_fr", "Arpent / Journal (France)", "Arpent", 36800.6, "Traditional French acre = 3,418.89 Sq.Meters"),
                LandUnitDefinition("giornata_it", "Giornata (Piedmont Italy)", "Giornata", 41010.5, "100 Tavole = 3,810 Sq.Meters"),
                LandUnitDefinition("sqmeter", "Square Meter (m²)", "m²", 10.7639104, "EU Cadastral Standard")
            ),
            countryGroup = "UK, Europe & Russia (States & Regions)"
        ),
        StateLandProfile(
            stateName = "Africa, Indonesia & Malaysia (Nigeria Plots / SA Morgen / Java Tumbak)",
            regionNote = "Nigeria Plot = 50×100 ft | SA Morgen = 8,565 Sq.M | Indonesia Tumbak = 14 Sq.M",
            vighaSqFt = 30976.0,
            stampDutyPercent = 4.5,
            units = listOf(
                LandUnitDefinition("plot_ng_50x100", "Nigerian Standard Plot (50×100 ft)", "Plot (464.5 m²)", 5000.0, "Standard residential plot in Nigeria/Ghana = 5,000 Sq.Ft"),
                LandUnitDefinition("plot_ng_60x120", "Nigerian Full Plot (60×120 ft)", "Full Plot (668.9 m²)", 7200.0, "6 plots per Acre = 7,200 Sq.Ft"),
                LandUnitDefinition("morgen_sa", "Cape Morgen (South Africa)", "Cape Morgen", 92196.3, "South African farmland unit = 8,565.32 Sq.Meters"),
                LandUnitDefinition("gasha_et", "Gasha (Ethiopia)", "Gasha (40 ha)", 4305564.0, "40 Hectares = 400,000 Sq.Meters"),
                LandUnitDefinition("tumbak_id", "Tumbak / Bata / Ru (Java Indonesia)", "Tumbak / Ru", 150.695, "Java/Sunda traditional unit = 14 Sq.Meters"),
                LandUnitDefinition("relong_my", "Relong (Kedah / Penang Malaysia)", "Relong", 30976.0, "0.711 Acre = 2,877.8 Sq.Meters"),
                LandUnitDefinition("acre", "Acre / Ekar", "Acre / Ekar", 43560.0, "43,560 Sq.Ft"),
                LandUnitDefinition("hectare", "Hectare / Hektar", "Hectare", 107639.104, "10,000 Sq.Meters")
            ),
            countryGroup = "Middle East, Africa & Latin America"
        )
    )

    fun convertLandUnits(
        stateProfile: StateLandProfile,
        inputUnit: LandUnitDefinition,
        inputValue: Double,
        ratePerInputUnitInr: Double
    ): LandConversionResult {
        val safeValue = max(0.0, inputValue)
        val safeRate = max(0.0, ratePerInputUnitInr)
        val totalSqFeet = safeValue * inputUnit.sqFeetPerUnit
        val totalSqMeters = totalSqFeet / 10.7639104

        val converted = stateProfile.units.map { target ->
            ConvertedUnitItem(
                unit = target,
                convertedValue = if (target.sqFeetPerUnit > 0.0) totalSqFeet / target.sqFeetPerUnit else 0.0
            )
        }

        val baseValue = safeValue * safeRate
        val stampDuty = baseValue * (stateProfile.stampDutyPercent / 100.0)

        return LandConversionResult(
            stateProfile = stateProfile,
            inputUnit = inputUnit,
            inputValue = safeValue,
            totalSqFeet = totalSqFeet,
            totalSqMeters = totalSqMeters,
            convertedItems = converted,
            baseLandValueInr = baseValue,
            stampDutyInr = stampDuty,
            totalLandCostInr = baseValue + stampDuty
        )
    }

    fun heronTriangleArea(a: Double, b: Double, c: Double): Double {
        if (a <= 0.0 || b <= 0.0 || c <= 0.0) return 0.0
        if (a + b <= c || a + c <= b || b + c <= a) return 0.0
        val s = 0.5 * (a + b + c)
        return sqrt(max(0.0, s * (s - a) * (s - b) * (s - c)))
    }

    fun calculateOptimalDiagonal(north: Double, east: Double, south: Double, west: Double): Double {
        val minD = max(abs(north - east), abs(south - west)) + 0.5
        val maxD = min(north + east, south + west) - 0.5
        val rmsD = sqrt(0.5 * (north * north + east * east + south * south + west * west))
        return if (maxD > minD) rmsD.coerceIn(minD, maxD) else rmsD
    }

    fun evaluateIrregularPlot(
        northFt: Double,
        eastFt: Double,
        southFt: Double,
        westFt: Double,
        diagonalInputFt: Double,
        vighaSqFt: Double = 17424.0,
        ratePerSqFtInr: Double = 1500.0
    ): IrregularPlotResult {
        val a = max(1.0, northFt)
        val b = max(1.0, eastFt)
        val c = max(1.0, southFt)
        val d = max(1.0, westFt)

        // Diagonal connects Top-Right (NE) and Bottom-Left (SW) or Top-Left (NW) and Bottom-Right (SE).
        // Let's split into Triangle 1 (North 'a', East 'b', Diagonal 'diag') and Triangle 2 (South 'c', West 'd', Diagonal 'diag').
        val minDiag = max(abs(a - b), abs(c - d))
        val maxDiag = min(a + b, c + d)

        val autoDiag = calculateOptimalDiagonal(a, b, c, d)
        val effectiveDiag = if (diagonalInputFt > 0.0) diagonalInputFt else autoDiag

        val isGeomValid = maxDiag > minDiag && effectiveDiag > minDiag && effectiveDiag < maxDiag

        val t1Area: Double
        val t2Area: Double
        val exactArea: Double
        val validationMsg: String

        if (isGeomValid) {
            t1Area = heronTriangleArea(a, b, effectiveDiag)
            t2Area = heronTriangleArea(c, d, effectiveDiag)
            exactArea = t1Area + t2Area
            validationMsg = if (diagonalInputFt > 0.0) {
                "Exact Survey Triangulation (Heron's Formula with measured diagonal ${formatDec(effectiveDiag)} ft)"
            } else {
                "Cyclic Quadrilateral Triangulation (Auto-calibrated diagonal ${formatDec(effectiveDiag)} ft)"
            }
        } else {
            // Fallback to Brahmagupta cyclic approximation if user typed an impossible diagonal
            val clampedDiag = autoDiag
            t1Area = heronTriangleArea(a, b, clampedDiag)
            t2Area = heronTriangleArea(c, d, clampedDiag)
            exactArea = t1Area + t2Area
            validationMsg = "Note: Diagonal must be between ${formatDec(minDiag + 0.1)} ft and ${formatDec(maxDiag - 0.1)} ft. Using calibrated diagonal ${formatDec(clampedDiag)} ft."
        }

        val usedDiag = if (isGeomValid) effectiveDiag else autoDiag
        val naiveAvgArea = 0.5 * (a + c) * 0.5 * (b + d)
        val errorSqFt = naiveAvgArea - exactArea
        val errorInr = errorSqFt * max(0.0, ratePerSqFtInr)

        // Compute true 2D CAD coordinates for vertices A(NW), B(NE), C(SE), D(SW)
        // Place D(SW) at (0,0) and C(SE) along positive X-axis? Or place A(NW) at (0,0) and B(NE) at (a, 0):
        // Let's place A(NW) = (0, 0), B(NE) = (a, 0) in survey coordinates (X right, Y down).
        // Diagonal connects A(NW) to C(SE) with length = usedDiag, and B(NE) to C(SE) has length = b (East).
        val cosAngleBAC = ((a * a + usedDiag * usedDiag - b * b) / (2.0 * a * usedDiag)).coerceIn(-0.999, 0.999)
        val angleBAC = acos(cosAngleBAC)
        val cx = usedDiag * cos(angleBAC)
        val cy = usedDiag * sin(angleBAC)

        // Triangle 2 has vertices A(0,0), C(cx, cy) with distance usedDiag, and D(SW) at distance d from A and c from C:
        val cosAngleCAD = ((usedDiag * usedDiag + d * d - c * c) / (2.0 * usedDiag * d)).coerceIn(-0.999, 0.999)
        val angleCAD = acos(cosAngleCAD)
        val totalAngleAD = angleBAC + angleCAD
        val dx = d * cos(totalAngleAD)
        val dy = d * sin(totalAngleAD)

        // Interior angles in degrees
        val angleB = acos(((a * a + b * b - usedDiag * usedDiag) / (2.0 * a * b)).coerceIn(-0.999, 0.999)) * 180.0 / PI
        val angleD = acos(((c * c + d * d - usedDiag * usedDiag) / (2.0 * c * d)).coerceIn(-0.999, 0.999)) * 180.0 / PI
        val angleA = totalAngleAD * 180.0 / PI
        val angleC = (360.0 - angleA - angleB - angleD).coerceIn(10.0, 340.0)

        val vertices = listOf(
            Vertex2D(0f, 0f, "NW", angleA),
            Vertex2D(a.toFloat(), 0f, "NE", angleB),
            Vertex2D(cx.toFloat(), cy.toFloat(), "SE", angleC),
            Vertex2D(dx.toFloat(), dy.toFloat(), "SW", angleD)
        )

        val conversions = linkedMapOf(
            "Square Feet (Sq.Ft)" to exactArea,
            "Vaar / Gaj (Sq.Yards)" to (exactArea / 9.0),
            "Square Meters (Sq.M)" to (exactArea / 10.7639104),
            "Guntha (1,089 Sq.Ft)" to (exactArea / 1089.0),
            "Vigha (16 Guntha)" to (exactArea / vighaSqFt),
            "Pucca Bigha (27,225 Sq.Ft)" to (exactArea / 27225.0),
            "Acre (40 Guntha)" to (exactArea / 43560.0),
            "Hectare (10,000 Sq.M)" to (exactArea / 107639.104),
            "Marla (272.25 Sq.Ft)" to (exactArea / 272.25),
            "Cent (435.6 Sq.Ft)" to (exactArea / 435.6)
        )

        return IrregularPlotResult(
            northFt = a,
            eastFt = b,
            southFt = c,
            westFt = d,
            diagonalFt = diagonalInputFt,
            usedDiagonal = usedDiag,
            minValidDiagonal = minDiag,
            maxValidDiagonal = maxDiag,
            isGeometryValid = isGeomValid,
            validationMessage = validationMsg,
            triangle1AreaSqFt = t1Area,
            triangle2AreaSqFt = t2Area,
            exactAreaSqFt = exactArea,
            naiveAvgAreaSqFt = naiveAvgArea,
            contractorErrorSqFt = errorSqFt,
            contractorErrorInr = errorInr,
            perimeterFt = a + b + c + d,
            vertices = vertices,
            regionalConversions = conversions,
            estimatedPlotValueInr = exactArea * max(0.0, ratePerSqFtInr)
        )
    }

    fun estimateConstructionMaterials(
        moduleType: ConstructionModuleType,
        lengthFt: Double,
        widthOrHeightFt: Double,
        tileLengthFt: Double = 2.0,
        tileWidthFt: Double = 2.0,
        wastagePercent: Double = 7.0,
        wallThicknessInches: Double = 9.0,
        plasterThicknessMm: Double = 12.0,
        deductionSqFt: Double = 0.0,
        cementBagRateInr: Double = 390.0,
        sandPerCuFtRateInr: Double = 55.0,
        tilePerSqFtRateInr: Double = 48.0,
        brickUnitRateInr: Double = 8.5,
        paintPerLiterRateInr: Double = 320.0
    ): ConstructionBoqResult {
        val l = max(1.0, lengthFt)
        val w = max(1.0, widthOrHeightFt)
        val rawArea = l * w
        val netArea = max(1.0, rawArea - max(0.0, deductionSqFt))

        return when (moduleType) {
            ConstructionModuleType.TILES_FLOORING -> {
                val skirtingArea = 2.0 * (l + w) * (4.0 / 12.0) // 4 inch skirting around room
                val totalTiledArea = netArea + skirtingArea
                val singleTileArea = max(0.25, tileLengthFt * tileWidthFt)
                val rawTiles = ceil(totalTiledArea / singleTileArea)
                val totalTiles = ceil(rawTiles * (1.0 + wastagePercent / 100.0)).toInt()
                val tilesPerBox = when {
                    singleTileArea >= 8.0 -> 2
                    singleTileArea >= 4.0 -> 4
                    else -> 6
                }
                val totalBoxes = ceil(totalTiles.toDouble() / tilesPerBox).toInt()

                // Bedding mortar: 2 inch (0.166 ft) thickness, 1.33 dry volume factor, 1:6 cement:sand ratio
                val dryMortarCuFt = netArea * 0.166 * 1.33
                val cementBags = ceil((dryMortarCuFt / 7.0) / 1.226).toInt()
                val whiteCementKg = ceil(totalTiledArea * 0.08).toInt()
                val sandCuFt = (dryMortarCuFt * 6.0) / 7.0
                val sandBrass = sandCuFt / 100.0

                val tileCost = totalTiles * singleTileArea * tilePerSqFtRateInr
                val cementCost = cementBags * cementBagRateInr
                val sandCost = sandCuFt * sandPerCuFtRateInr
                val groutCost = whiteCementKg * 35.0

                val items = listOf(
                    ConstructionBoqItem(
                        "Vitrified / Ceramic Tiles (${formatDec(tileLengthFt)}×${formatDec(tileWidthFt)} ft)",
                        "Includes 4\" skirting (${formatDec(skirtingArea)} sq.ft) + ${formatDec(wastagePercent)}% cutting wastage",
                        "$totalTiles Tiles ($totalBoxes Boxes)",
                        tilePerSqFtRateInr,
                        tileCost
                    ),
                    ConstructionBoqItem(
                        "OPC/PPC Cement Bags (50 kg)",
                        "1:6 Bedding Mortar (50mm bed, 1.33 dry factor)",
                        "$cementBags Bags (${cementBags * 50} kg)",
                        cementBagRateInr,
                        cementCost
                    ),
                    ConstructionBoqItem(
                        "Screened River Sand (Bedding)",
                        "100 Cu.Ft = 1 Brass standard truck measure",
                        "${formatDec(sandCuFt)} Cu.Ft (${formatDec(sandBrass)} Brass)",
                        sandPerCuFtRateInr,
                        sandCost
                    ),
                    ConstructionBoqItem(
                        "Tile Joint Grout & White Cement",
                        "Epoxy / White cement joint filler",
                        "$whiteCementKg kg",
                        35.0,
                        groutCost
                    )
                )

                ConstructionBoqResult(
                    moduleType = moduleType,
                    netWorkAreaSqFt = totalTiledArea,
                    volumeCuFt = dryMortarCuFt,
                    items = items,
                    totalEstimatedCostInr = items.sumOf { it.totalCostInr },
                    technicalFormulaNote = "IS 1443 Flooring Standard: Floor (${formatDec(netArea)} sq.ft) + 4\" Skirting (${formatDec(skirtingArea)} sq.ft) + ${formatDec(wastagePercent)}% wastage. Mortar 1:6 with 1.33 dry bulkage factor."
                )
            }

            ConstructionModuleType.BRICKWORK_MASONRY -> {
                val thicknessFt = wallThicknessInches / 12.0
                val wallVolumeCuFt = netArea * thicknessFt
                // Standard modular Indian brick with mortar = ~13.5 bricks per Cu.Ft
                val bricksWithWastage = ceil(wallVolumeCuFt * 13.5 * (1.0 + wastagePercent / 100.0)).toInt()
                // Dry mortar is ~30% of masonry volume * 1.33 dry factor, 1:6 ratio
                val dryMortarCuFt = wallVolumeCuFt * 0.30 * 1.33
                val cementBags = ceil((dryMortarCuFt / 7.0) / 1.226).toInt()
                val sandCuFt = (dryMortarCuFt * 6.0) / 7.0
                val sandBrass = sandCuFt / 100.0

                val brickCost = bricksWithWastage * brickUnitRateInr
                val cementCost = cementBags * cementBagRateInr
                val sandCost = sandCuFt * sandPerCuFtRateInr

                val items = listOf(
                    ConstructionBoqItem(
                        "First-Class Clay / Fly-Ash Bricks",
                        "${formatDec(wallThicknessInches)}\" Wall (${formatDec(wallVolumeCuFt)} Cu.Ft) + ${formatDec(wastagePercent)}% breakage buffer",
                        "$bricksWithWastage Bricks",
                        brickUnitRateInr,
                        brickCost
                    ),
                    ConstructionBoqItem(
                        "Masonry Cement Bags (50 kg)",
                        "1:6 Cement-Sand Mortar (1.226 Cu.Ft per 50kg bag)",
                        "$cementBags Bags (${cementBags * 50} kg)",
                        cementBagRateInr,
                        cementCost
                    ),
                    ConstructionBoqItem(
                        "Masonry River / M-Sand",
                        "Clean masonry grade sand",
                        "${formatDec(sandCuFt)} Cu.Ft (${formatDec(sandBrass)} Brass)",
                        sandPerCuFtRateInr,
                        sandCost
                    )
                )

                ConstructionBoqResult(
                    moduleType = moduleType,
                    netWorkAreaSqFt = netArea,
                    volumeCuFt = wallVolumeCuFt,
                    items = items,
                    totalEstimatedCostInr = items.sumOf { it.totalCostInr },
                    technicalFormulaNote = "IS 2212 Brickwork Formula: Volume = Net Area × (${formatDec(wallThicknessInches)}/12) = ${formatDec(wallVolumeCuFt)} Cu.Ft. 13.5 bricks/Cu.Ft + 30% wet mortar × 1.33 dry volume factor."
                )
            }

            ConstructionModuleType.WALL_PLASTERING -> {
                val thicknessFt = (plasterThicknessMm / 1000.0) * 3.28084
                val wetVolumeCuFt = netArea * thicknessFt
                // 20% joint filling & unevenness factor * 1.33 dry volume factor
                val dryMortarCuFt = wetVolumeCuFt * 1.20 * 1.33
                // 1:4 ratio for external/ceiling or 1:5 for internal (using 1:5 average = 6 parts)
                val cementBags = ceil((dryMortarCuFt / 6.0) / 1.226).toInt()
                val sandCuFt = (dryMortarCuFt * 5.0) / 6.0
                val sandBrass = sandCuFt / 100.0
                val waterproofingLiters = ceil(cementBags * 0.2)

                val cementCost = cementBags * cementBagRateInr
                val sandCost = sandCuFt * sandPerCuFtRateInr
                val wpCost = waterproofingLiters * 180.0

                val items = listOf(
                    ConstructionBoqItem(
                        "Plaster Cement Bags (50 kg)",
                        "${formatDec(plasterThicknessMm)} mm Plaster (1:5 Mortar Ratio)",
                        "$cementBags Bags (${cementBags * 50} kg)",
                        cementBagRateInr,
                        cementCost
                    ),
                    ConstructionBoqItem(
                        "Fine Plaster Sand (Sieving Grade)",
                        "Fine river sand / plaster M-sand",
                        "${formatDec(sandCuFt)} Cu.Ft (${formatDec(sandBrass)} Brass)",
                        sandPerCuFtRateInr,
                        sandCost
                    ),
                    ConstructionBoqItem(
                        "Integral Liquid Waterproofing",
                        "200 ml per 50 kg bag of cement",
                        "${formatDec(waterproofingLiters)} Liters",
                        180.0,
                        wpCost
                    )
                )

                ConstructionBoqResult(
                    moduleType = moduleType,
                    netWorkAreaSqFt = netArea,
                    volumeCuFt = dryMortarCuFt,
                    items = items,
                    totalEstimatedCostInr = items.sumOf { it.totalCostInr },
                    technicalFormulaNote = "IS 1661 Plastering Standard: Wet Vol = ${formatDec(netArea)} sq.ft × ${formatDec(plasterThicknessMm)}mm. Includes 20% frog/joint filling & 1.33 dry shrinkage factor (1:5 mix)."
                )
            }

            ConstructionModuleType.PAINT_PUTTY -> {
                // Wall Putty (2 coats): ~14 sq.ft per kg
                // Wall Primer (1 coat): ~120 sq.ft per liter
                // Emulsion Paint (2 coats): ~65 sq.ft per liter
                val puttyKg = ceil(netArea / 14.0)
                val puttyBags40Kg = ceil(puttyKg / 40.0).toInt()
                val primerLiters = ceil(netArea / 120.0)
                val paintLiters = ceil(netArea / 65.0)

                val puttyCost = puttyKg * 28.0
                val primerCost = primerLiters * 190.0
                val paintCost = paintLiters * paintPerLiterRateInr

                val items = listOf(
                    ConstructionBoqItem(
                        "White Cement Wall Putty (2 Coats)",
                        "Coverage: ~14 Sq.Ft / kg (2 coats smooth finish)",
                        "${puttyKg.toInt()} kg ($puttyBags40Kg Bags of 40kg)",
                        28.0,
                        puttyCost
                    ),
                    ConstructionBoqItem(
                        "Interior/Exterior Wall Primer (1 Coat)",
                        "Coverage: ~120 Sq.Ft / Liter",
                        "${primerLiters.toInt()} Liters",
                        190.0,
                        primerCost
                    ),
                    ConstructionBoqItem(
                        "Acrylic Emulsion Paint (2 Coats)",
                        "Coverage: ~65 Sq.Ft / Liter for 2 rich coats",
                        "${paintLiters.toInt()} Liters",
                        paintPerLiterRateInr,
                        paintCost
                    )
                )

                ConstructionBoqResult(
                    moduleType = moduleType,
                    netWorkAreaSqFt = netArea,
                    volumeCuFt = 0.0,
                    items = items,
                    totalEstimatedCostInr = items.sumOf { it.totalCostInr },
                    technicalFormulaNote = "CPWD Finishing Norms: Net Paintable Area = ${formatDec(netArea)} Sq.Ft. Putty @ 14 sq.ft/kg (2 coats), Primer @ 120 sq.ft/L (1 coat), Emulsion @ 65 sq.ft/L (2 coats)."
                )
            }
        }
    }

    fun formatDec(value: Double, decimals: Int = 2): String {
        return String.format("%.${decimals}f", value)
    }

    fun formatCurrency(amountInr: Double): String {
        val rounded = max(0.0, amountInr)
        return when {
            rounded >= 10000000.0 -> String.format("₹%.2f Cr", rounded / 10000000.0)
            rounded >= 100000.0 -> String.format("₹%.2f Lakh", rounded / 100000.0)
            else -> String.format("₹%,.0f", rounded)
        }
    }
}
