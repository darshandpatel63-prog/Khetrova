package com.example.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

enum class ApmcRegionZone(
    val id: String,
    val title: String,
    val subtitle: String,
    val isInternational: Boolean = false
) {
    SAURASHTRA_GUJARAT(
        id = "saurashtra_gj",
        title = "Saurashtra APMC (સૌરાષ્ટ્ર માર્કેટ યાર્ડ)",
        subtitle = "Gondal, Rajkot, Junagadh, Mahuva, Amreli, Jamnagar, Botad"
    ),
    NORTH_GUJARAT(
        id = "north_gj",
        title = "North Gujarat APMC (ઉત્તર ગુજરાત યાર્ડ)",
        subtitle = "Unjha, Deesa, Patan, Himatnagar, Mehsana, Visnagar, Modasa"
    ),
    CENTRAL_SOUTH_KUTCH_GUJARAT(
        id = "central_south_kutch_gj",
        title = "Central, South Gujarat & Kutch (મધ્ય-દક્ષિણ ગુજરાત અને કચ્છ)",
        subtitle = "Ahmedabad/Bavla, Anand/Kapadvanj, Surat/Bharuch, Bhuj/Anjar"
    ),
    INDIA_NATIONAL_ENAM(
        id = "india_enam",
        title = "All-India e-NAM APMCs (ભારતના અન્ય રાજ્યોના યાર્ડ)",
        subtitle = "Lasalgaon (MH), Neemuch/Indore (MP), Kota/Jodhpur (RJ), Khanna (PB), Guntur/Kochi"
    ),
    GLOBAL_GOVT_MARKETS(
        id = "global_govt",
        title = "Global Govt Markets (વિશ્વના અન્ય દેશોના સરકારી બજાર ભાવ)",
        subtitle = "USA (USDA), Brazil (CONAB), Europe (MATIF/EU), Japan (MAFF), Australia (ABARES), UAE",
        isInternational = true
    )
}

data class ApmcYardProfile(
    val yardId: String,
    val yardName: String,
    val districtOrState: String,
    val countryName: String,
    val regionZone: ApmcRegionZone,
    val governmentPortalName: String,
    val specialtyCropsSummary: String,
    val defaultCurrencySymbol: String = "₹",
    val defaultCurrencyCode: String = "INR"
)

data class LiveCropPriceQuote(
    val quoteId: String,
    val yardId: String,
    val yardName: String,
    val districtOrState: String,
    val regionZone: ApmcRegionZone,
    val cropNameEn: String,
    val cropNameLocal: String,
    val varietyOrGrade: String,
    val minPricePer20KgManInr: Double,
    val maxPricePer20KgManInr: Double,
    val modalPricePer20KgManInr: Double,
    val minPricePerQuintalInr: Double,
    val maxPricePerQuintalInr: Double,
    val modalPricePerQuintalInr: Double,
    val dailyChangePerManInr: Double,
    val arrivalBagsCount: Int,
    val nativeCurrencySymbol: String,
    val nativeUnitLabel: String,
    val nativeModalPrice: Double,
    val governmentSourceLabel: String,
    val updatedTimestampText: String,
    val isLiveNetworkSynced: Boolean,
    val hasVerifiedDailyChange: Boolean = false,
    val hasVerifiedArrivalData: Boolean = false
)

data class LiveApmcSyncSnapshot(
    val quotes: List<LiveCropPriceQuote>,
    val isOnlineSynced: Boolean,
    val syncStatusBanner: String,
    val lastUpdatedTimeFormatted: String,
    val usdToInrRate: Double,
    val eurToInrRate: Double,
    val brlToInrRate: Double,
    val jpyToInrRate: Double,
    val audToInrRate: Double
)

private data class SeedCropTemplate(
    val cropNameEn: String,
    val cropNameLocal: String,
    val varietyOrGrade: String,
    val baseMinManInr: Double,
    val baseMaxManInr: Double,
    val baseModalManInr: Double,
    val baseArrivalBags: Int,
    val nativeUnitLabel: String = "20 Kg Man (મણ)",
    val nativeCurrencyCode: String = "INR"
)

object LiveApmcMarketEngine {

    val allYardProfiles: List<ApmcYardProfile> = listOf(
        // 1. SAURASHTRA GUJARAT APMCs
        ApmcYardProfile(
            yardId = "gondal_apmc",
            yardName = "Gondal APMC (ગોંડલ માર્કેટ યાર્ડ)",
            districtOrState = "Rajkot District, Saurashtra, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.SAURASHTRA_GUJARAT,
            governmentPortalName = "Gondal APMC Official Daily Auction & Agmarknet",
            specialtyCropsSummary = "Groundnut (મગફળી), Cotton (કપાસ), Coriander (ધાણા), Garlic (લસણ), Chilli (મરચાં)"
        ),
        ApmcYardProfile(
            yardId = "rajkot_bedi_apmc",
            yardName = "Rajkot Bedi APMC (રાજકોટ બેડી માર્કેટ યાર્ડ)",
            districtOrState = "Rajkot District, Saurashtra, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.SAURASHTRA_GUJARAT,
            governmentPortalName = "Rajkot APMC Daily Bhav & Gujarat MSAMB",
            specialtyCropsSummary = "Groundnut (મગફળી), Shankar-6 Cotton (કપાસ), Jeera (જીરું), Sesame (તલ), Wheat (ઘઉં)"
        ),
        ApmcYardProfile(
            yardId = "junagadh_apmc",
            yardName = "Junagadh APMC (જૂનાગઢ માર્કેટ યાર્ડ)",
            districtOrState = "Junagadh District, Saurashtra, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.SAURASHTRA_GUJARAT,
            governmentPortalName = "Junagadh APMC & e-NAM Gujarat",
            specialtyCropsSummary = "Groundnut (મગફળી), Tuver (તુવેર), Chana (ચણા), Soyabean (સોયાબીન), Wheat (ઘઉં)"
        ),
        ApmcYardProfile(
            yardId = "mahuva_bhavnagar_apmc",
            yardName = "Mahuva & Bhavnagar APMC (મહુવા માર્કેટ યાર્ડ)",
            districtOrState = "Bhavnagar District, Saurashtra, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.SAURASHTRA_GUJARAT,
            governmentPortalName = "Mahuva APMC Official Daily Bulletin",
            specialtyCropsSummary = "Red Onion (લાલ ડુંગળી), White Onion (સફેદ ડુંગળી), Cotton (કપાસ), Groundnut (મગફળી)"
        ),
        ApmcYardProfile(
            yardId = "amreli_botad_apmc",
            yardName = "Amreli & Botad APMC (અમરેલી અને બોટાદ યાર્ડ)",
            districtOrState = "Amreli / Botad, Saurashtra, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.SAURASHTRA_GUJARAT,
            governmentPortalName = "Amreli & Botad APMC Cotton/Grain Auction",
            specialtyCropsSummary = "Cotton (કપાસ), Sesame (તલ), Groundnut (મગફળી), Chana (ચણા), Jeera (જીરું)"
        ),
        ApmcYardProfile(
            yardId = "jamnagar_hapa_apmc",
            yardName = "Jamnagar Hapa APMC (જામનગર હાપા માર્કેટ યાર્ડ)",
            districtOrState = "Jamnagar District, Saurashtra, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.SAURASHTRA_GUJARAT,
            governmentPortalName = "Jamnagar APMC Daily Auction Board",
            specialtyCropsSummary = "Garlic (જામનગરી લસણ), Ajwain (અજમો), Groundnut (મગફળી), Cotton (કપાસ), Jeera"
        ),

        // 2. NORTH GUJARAT APMCs
        ApmcYardProfile(
            yardId = "unjha_apmc",
            yardName = "Unjha APMC (ઊંઝા માર્કેટ યાર્ડ - મહેસાણા)",
            districtOrState = "Mehsana District, North Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.NORTH_GUJARAT,
            governmentPortalName = "Unjha APMC Spice Benchmark & Agmarknet",
            specialtyCropsSummary = "Jeera/Cumin (જીરું), Variali/Fennel (વરિયાળી), Isabgol (ઈસબગુલ), Mustard (રાયડો), Suva"
        ),
        ApmcYardProfile(
            yardId = "deesa_palanpur_apmc",
            yardName = "Deesa & Palanpur APMC (ડીસા માર્કેટ યાર્ડ - બનાસકાંઠા)",
            districtOrState = "Banaskantha District, North Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.NORTH_GUJARAT,
            governmentPortalName = "Deesa APMC Daily Auction & Banaskantha Mandi",
            specialtyCropsSummary = "Potato (ડીસા બટાટા), Groundnut (મગફળી), Castor (એરંડા), Bajra (બાજરી), Mustard (રાયડો)"
        ),
        ApmcYardProfile(
            yardId = "patan_harij_apmc",
            yardName = "Patan & Harij APMC (પાટણ અને હારીજ માર્કેટ યાર્ડ)",
            districtOrState = "Patan District, North Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.NORTH_GUJARAT,
            governmentPortalName = "Patan APMC Daily Rates & e-NAM",
            specialtyCropsSummary = "Castor Seed (એરંડા), Mustard (રાયડો), Guar Seed (ગવાર), Jeera (જીરું), Chana"
        ),
        ApmcYardProfile(
            yardId = "himatnagar_modasa_apmc",
            yardName = "Himatnagar & Modasa APMC (હિંમતનગર અને મોડાસા યાર્ડ)",
            districtOrState = "Sabarkantha / Aravalli, North Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.NORTH_GUJARAT,
            governmentPortalName = "Sabarkantha & Aravalli APMC Board",
            specialtyCropsSummary = "Cotton (કપાસ), Maize (મકાઈ), Wheat (ઘઉં), Soyabean (સોયાબીન), Groundnut, Tuver"
        ),
        ApmcYardProfile(
            yardId = "mehsana_vijapur_apmc",
            yardName = "Mehsana & Vijapur APMC (મહેસાણા અને વિજાપુર યાર્ડ)",
            districtOrState = "Mehsana District, North Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.NORTH_GUJARAT,
            governmentPortalName = "Mehsana & Vijapur APMC Daily Rates",
            specialtyCropsSummary = "Wheat (ઘઉં), Castor (એરંડા), Cotton (કપાસ), Mustard (રાયડો), Tobacco (તમાકુ)"
        ),

        // 3. CENTRAL, SOUTH GUJARAT & KUTCH APMCs
        ApmcYardProfile(
            yardId = "ahmedabad_bavla_apmc",
            yardName = "Ahmedabad & Bavla APMC (અમદાવાદ અને બાવળા યાર્ડ)",
            districtOrState = "Ahmedabad District, Central Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.CENTRAL_SOUTH_KUTCH_GUJARAT,
            governmentPortalName = "Ahmedabad APMC (Jetalpur/Bavla) Official Rates",
            specialtyCropsSummary = "Paddy/Rice (ડાંગર ગુર્જરી/જીરાસર), Bhalia Wheat (ઘઉં), Castor (એરંડા), Cotton"
        ),
        ApmcYardProfile(
            yardId = "anand_kapadvanj_apmc",
            yardName = "Anand, Nadiad & Kapadvanj APMC (ચરોતર માર્કેટ યાર્ડ)",
            districtOrState = "Anand / Kheda District, Central Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.CENTRAL_SOUTH_KUTCH_GUJARAT,
            governmentPortalName = "Charotar APMC Federation & Agmarknet",
            specialtyCropsSummary = "Tobacco (તમાકુ), Paddy (ડાંગર), Potato (બટાટા), Banana (કેળાં), Bajra (બાજરી)"
        ),
        ApmcYardProfile(
            yardId = "surat_bharuch_apmc",
            yardName = "Surat & Bharuch APMC (સુરત અને ભરૂચ માર્કેટ યાર્ડ)",
            districtOrState = "Surat / Bharuch, South Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.CENTRAL_SOUTH_KUTCH_GUJARAT,
            governmentPortalName = "Surat APMC & South Gujarat Krushi Bazar",
            specialtyCropsSummary = "Tuver (ભરૂચી તુવેર), Sugarcane (શેરડી), Paddy (ડાંગર), Cotton (કપાસ), Banana/Mango"
        ),
        ApmcYardProfile(
            yardId = "bhuj_anjar_kutch_apmc",
            yardName = "Bhuj & Anjar APMC (ભૂજ અને અંજાર યાર્ડ - કચ્છ)",
            districtOrState = "Kutch District, Gujarat",
            countryName = "India",
            regionZone = ApmcRegionZone.CENTRAL_SOUTH_KUTCH_GUJARAT,
            governmentPortalName = "Kutch APMC Market Board",
            specialtyCropsSummary = "Castor (એરંડા), Isabgol (ઈસબગુલ), Jeera (જીરું), Kutch Dates (ખારેક), Pomegranate (દાડમ)"
        ),

        // 4. ALL-INDIA NATIONAL e-NAM APMCs
        ApmcYardProfile(
            yardId = "lasalgaon_nashik_mh",
            yardName = "Lasalgaon & Pune APMC (લાસલગાંવ/નાસિક - મહારાષ્ટ્ર)",
            districtOrState = "Nashik / Pune, Maharashtra",
            countryName = "India",
            regionZone = ApmcRegionZone.INDIA_NATIONAL_ENAM,
            governmentPortalName = "MSAMB Maharashtra & e-NAM Govt Portal",
            specialtyCropsSummary = "Red Onion (નાસિક ડુંગળી), Soyabean, Cotton, Tur/Arhar, Pomegranate, Grapes"
        ),
        ApmcYardProfile(
            yardId = "neemuch_indore_mp",
            yardName = "Neemuch & Indore Mandi (નીમચ અને ઈન્દોર - મધ્યપ્રદેશ)",
            districtOrState = "Neemuch / Indore, Madhya Pradesh",
            countryName = "India",
            regionZone = ApmcRegionZone.INDIA_NATIONAL_ENAM,
            governmentPortalName = "MP Mandi Board (MandiBhav MP Govt) & Agmarknet",
            specialtyCropsSummary = "MP Sharbati Wheat (શરબતી ઘઉં), Soyabean, Garlic (લસણ), Chana, Ashwagandha"
        ),
        ApmcYardProfile(
            yardId = "kota_jodhpur_rj",
            yardName = "Kota & Jodhpur Krishi Mandi (કોટા અને જોધપુર - રાજસ્થાન)",
            districtOrState = "Kota / Jodhpur, Rajasthan",
            countryName = "India",
            regionZone = ApmcRegionZone.INDIA_NATIONAL_ENAM,
            governmentPortalName = "Rajasthan Krishi Upaj Mandi Samiti & e-NAM",
            specialtyCropsSummary = "Mustard (સરસવ/રાયડો), Jeera (જીરું), Guar Seed (ગવાર), Moong (મગ), Bajra"
        ),
        ApmcYardProfile(
            yardId = "khanna_karnal_pb_hr",
            yardName = "Khanna & Karnal Grain Market (ખન્ના/કરનાલ - પંજાબ-હરિયાણા)",
            districtOrState = "Ludhiana (Punjab) / Karnal (Haryana)",
            countryName = "India",
            regionZone = ApmcRegionZone.INDIA_NATIONAL_ENAM,
            governmentPortalName = "Punjab Mandi Board / Haryana HSAMB & FCI MSP",
            specialtyCropsSummary = "Basmati Paddy 1121 (બાસમતી ડાંગર), Milling Wheat (ઘઉં MSP), Cotton, Maize"
        ),
        ApmcYardProfile(
            yardId = "guntur_kochi_south_in",
            yardName = "Guntur, Kochi & Erode Spice APMC (દક્ષિણ ભારત મસાલા યાર્ડ)",
            districtOrState = "Andhra Pradesh / Kerala / Tamil Nadu",
            countryName = "India",
            regionZone = ApmcRegionZone.INDIA_NATIONAL_ENAM,
            governmentPortalName = "Spices Board India & e-NAM South Zone",
            specialtyCropsSummary = "Guntur Red Chilli (મરચાં), Black Pepper (કાળા મરી), Turmeric (હળદર), Cardamom (એલચી), Copra"
        ),

        // 5. GLOBAL / OTHER COUNTRIES FREE GOVERNMENT AGRICULTURAL MARKETS
        ApmcYardProfile(
            yardId = "usa_usda_cbot",
            yardName = "USA — USDA AMS & Chicago CBOT (અમેરિકા સરકારી કૃષિ બજાર)",
            districtOrState = "Midwest / Texas / Georgia, United States",
            countryName = "United States (USA)",
            regionZone = ApmcRegionZone.GLOBAL_GOVT_MARKETS,
            governmentPortalName = "USDA Agricultural Marketing Service (AMS) & CBOT",
            specialtyCropsSummary = "Soybeans, Yellow Corn (મકાઈ), Upland Cotton (કપાસ), Red Wheat (ઘઉં), Peanuts (મગફળી)",
            defaultCurrencySymbol = "$",
            defaultCurrencyCode = "USD"
        ),
        ApmcYardProfile(
            yardId = "brazil_conab_cepea",
            yardName = "Brazil — CONAB & CEPEA/ESALQ (બ્રાઝિલ સરકારી કૃષિ બજાર)",
            districtOrState = "Mato Grosso / São Paulo / Paraná, Brazil",
            countryName = "Brazil",
            regionZone = ApmcRegionZone.GLOBAL_GOVT_MARKETS,
            governmentPortalName = "CONAB Gov.br & CEPEA/USP Official Daily Index",
            specialtyCropsSummary = "Soybeans (સોયાબીન), Arabica Coffee (કોફી), Sugarcane/Sugar, Cotton, Maize",
            defaultCurrencySymbol = "R$",
            defaultCurrencyCode = "BRL"
        ),
        ApmcYardProfile(
            yardId = "europe_eu_matif_defra",
            yardName = "Europe & UK — EU Agri-Food Portal & MATIF (યુરોપ સરકારી બજાર)",
            districtOrState = "France / Germany / Netherlands / UK",
            countryName = "European Union & UK",
            regionZone = ApmcRegionZone.GLOBAL_GOVT_MARKETS,
            governmentPortalName = "European Commission Agri-Food Data Portal & UK DEFRA",
            specialtyCropsSummary = "Milling Wheat (ઘઉં), Rapeseed/Canola (રાયડો), Barley (જવ), Potatoes (બટાટા)",
            defaultCurrencySymbol = "€",
            defaultCurrencyCode = "EUR"
        ),
        ApmcYardProfile(
            yardId = "japan_maff_east_asia",
            yardName = "Japan & East Asia — MAFF Japan / KAMIS (જાપાન અને એશિયા બજાર)",
            districtOrState = "Tokyo / Hokkaido / Niigata, Japan",
            countryName = "Japan & East Asia",
            regionZone = ApmcRegionZone.GLOBAL_GOVT_MARKETS,
            governmentPortalName = "MAFF (Ministry of Agriculture, Forestry & Fisheries Japan)",
            specialtyCropsSummary = "Japonica Rice (ચોખા), Food Soybeans, Green Tea (ગ્રીન ટી), Onion, Potato",
            defaultCurrencySymbol = "¥",
            defaultCurrencyCode = "JPY"
        ),
        ApmcYardProfile(
            yardId = "australia_abares_canada",
            yardName = "Australia & Canada — ABARES & AAFC (ઓસ્ટ્રેલિયા/કેનેડા સરકારી બજાર)",
            districtOrState = "New South Wales / Saskatchewan",
            countryName = "Australia & Canada",
            regionZone = ApmcRegionZone.GLOBAL_GOVT_MARKETS,
            governmentPortalName = "ABARES Australian Govt & Agriculture Agri-Food Canada",
            specialtyCropsSummary = "Desi Chickpeas (ચણા), Red Lentils (મસૂર), Canola, Prime Wheat, Cotton",
            defaultCurrencySymbol = "A$",
            defaultCurrencyCode = "AUD"
        ),
        ApmcYardProfile(
            yardId = "uae_al_aweer_mena",
            yardName = "UAE Dubai Al Aweer & Africa — Export Hub (દુબઈ/આફ્રિકા બજાર)",
            districtOrState = "Dubai (UAE) / Mombasa / Cairo",
            countryName = "UAE, Middle East & Africa",
            regionZone = ApmcRegionZone.GLOBAL_GOVT_MARKETS,
            governmentPortalName = "Dubai Municipality Al Aweer Central Market & RATIN Africa",
            specialtyCropsSummary = "Export Jeera (જીરું), Red Onion (ડુંગળી), Dates (ખજૂર), White Sesame (તલ), Tea",
            defaultCurrencySymbol = "AED ",
            defaultCurrencyCode = "AED"
        )
    )

    private val yardSeedCatalog: Map<String, List<SeedCropTemplate>> = mapOf(
        // GONDAL APMC
        "gondal_apmc" to listOf(
            SeedCropTemplate("Groundnut Bold (મગફળી જાડી G-20)", "મગફળી જાડી (G-20)", "G-20 / Bold Clean", 1140.0, 1495.0, 1345.0, 18500),
            SeedCropTemplate("Groundnut Small (મગફળી જીણી)", "મગફળી જીણી (TJ-37)", "TJ / Oil Quality", 1080.0, 1380.0, 1265.0, 9200),
            SeedCropTemplate("Cotton Shankar-6 (કપાસ શંકર-૬)", "કપાસ (શંકર-૬ સુપર)", "Long Staple 29mm", 1380.0, 1645.0, 1545.0, 24600),
            SeedCropTemplate("Coriander Eagle/Scooter (ધાણા)", "ધાણા (ઈગલ / સ્કૂટર)", "Green Parrot / Eagle", 1250.0, 1890.0, 1560.0, 14200),
            SeedCropTemplate("Garlic New Dry (લસણ સૂકું)", "લસણ (દેશી/લાડવા)", "Medium-Bold Ladwa", 2100.0, 3850.0, 3120.0, 8400),
            SeedCropTemplate("Cumin Seed (જીરું)", "જીરું (ગોંડલ યાર્ડ)", "Europe Quality", 4250.0, 5180.0, 4790.0, 4100),
            SeedCropTemplate("Wheat Lokwan (ઘઉં લોકવન)", "ઘઉં (લોકવન સુપર)", "Sortex Clean", 515.0, 645.0, 578.0, 11300),
            SeedCropTemplate("Wheat Tukda (ઘઉં ટુકડા)", "ઘઉં (ટુકડા 496)", "GW-496 Sharbati", 530.0, 680.0, 602.0, 7900),
            SeedCropTemplate("Chickpea / Chana (ચણા)", "ચણા (કાંટાવાળા/દેશી)", "Desi Bold", 1090.0, 1295.0, 1215.0, 9600),
            SeedCropTemplate("White Sesame (સફેદ તલ)", "તલ (સફેદ 99/1)", "Sortex Export", 2380.0, 2890.0, 2675.0, 3800),
            SeedCropTemplate("Red Onion (લાલ ડુંગળી)", "ડુંગળી (લાલ નાસિક/સૌરાષ્ટ્ર)", "Medium-Big", 220.0, 565.0, 430.0, 31000),
            SeedCropTemplate("Tuver / Pigeon Pea (તુવેર)", "તુવેર (લાલ/સફેદ)", "FAQ Clean", 1680.0, 2240.0, 1995.0, 5400)
        ),

        // RAJKOT BEDI APMC
        "rajkot_bedi_apmc" to listOf(
            SeedCropTemplate("Cotton Shankar-6 (કપાસ શંકર-૬)", "કપાસ (રાજકોટ બેડી યાર્ડ)", "Super Ginning", 1410.0, 1655.0, 1560.0, 19800),
            SeedCropTemplate("Groundnut Bold (મગફળી જાડી)", "મગફળી (જાડી રોહિણી/G-20)", "HPS / Oil Mill", 1150.0, 1480.0, 1335.0, 15400),
            SeedCropTemplate("Cumin / Jeera (જીરું)", "જીરું (રાજકોટ બેન્ચમાર્ક)", "Machine Clean", 4300.0, 5210.0, 4825.0, 5200),
            SeedCropTemplate("Castor Seed (એરંડા)", "એરંડા (દિવેલા)", "Oil Content 48%+", 1160.0, 1275.0, 1235.0, 8900),
            SeedCropTemplate("Sesame White (સફેદ તલ)", "તલ (સફેદ)", "Hulled / Sortex", 2420.0, 2910.0, 2690.0, 4400),
            SeedCropTemplate("Black Sesame (કાળા તલ)", "તલ (કાળા સુપર)", "Export Grade", 2850.0, 3540.0, 3240.0, 2100),
            SeedCropTemplate("Wheat Lokwan (ઘઉં લોકવન)", "ઘઉં (લોકવન)", "Golden Grain", 520.0, 640.0, 582.0, 10200),
            SeedCropTemplate("Soyabean (સોયાબીન)", "સોયાબીન (પીળા)", "Yellow FAQ", 840.0, 965.0, 918.0, 6700),
            SeedCropTemplate("Green Gram / Moong (મગ)", "મગ (દેશી લીલા)", "Shiny Green", 1480.0, 1890.0, 1710.0, 3300),
            SeedCropTemplate("Fenugreek / Methi (મેથી)", "મેથી (દાણા)", "Bold Yellow", 1020.0, 1280.0, 1165.0, 2900)
        ),

        // JUNAGADH APMC
        "junagadh_apmc" to listOf(
            SeedCropTemplate("Groundnut G-20 (મગફળી સોરઠ)", "મગફળી (જૂનાગઢ સોરઠ)", "High Oil G-20", 1160.0, 1490.0, 1350.0, 16200),
            SeedCropTemplate("Tuver / Arhar (તુવેર)", "તુવેર (જૂનાગઢ/કેશોદ)", "Bold Red/White", 1720.0, 2260.0, 2020.0, 7400),
            SeedCropTemplate("Chickpea / Chana (ચણા)", "ચણા (ઘોરાડ/દેશી)", "Mill & Dal Grade", 1110.0, 1290.0, 1220.0, 11200),
            SeedCropTemplate("Soyabean (સોયાબીન)", "સોયાબીન", "Clean Dry", 850.0, 970.0, 922.0, 8100),
            SeedCropTemplate("Coriander (ધાણા)", "ધાણા (સોરઠી)", "Badami / Eagle", 1240.0, 1790.0, 1510.0, 6800),
            SeedCropTemplate("Wheat Tukda (ઘઉં ટુકડા)", "ઘઉં (ટુકડા)", "Grade-A", 525.0, 665.0, 595.0, 9400),
            SeedCropTemplate("Talala Kesar Mango (તાલાલા કેસર કેરી - 10Kg Box)", "કેસર કેરી (તાલાલા ગીર 10Kg બોક્સ)", "GI Tag Export Box", 850.0, 1550.0, 1180.0, 14500)
        ),

        // MAHUVA & BHAVNAGAR APMC
        "mahuva_bhavnagar_apmc" to listOf(
            SeedCropTemplate("Red Onion (મહુવા લાલ ડુંગળી)", "લાલ ડુંગળી (મહુવા બેન્ચમાર્ક)", "Export & Super", 210.0, 590.0, 445.0, 68000),
            SeedCropTemplate("White Onion (મહુવા સફેદ ડુંગળી)", "સફેદ ડુંગળી (ડિહાઇડ્રેશન)", "Dehydration Grade", 280.0, 640.0, 495.0, 42000),
            SeedCropTemplate("Cotton Shankar-6 (કપાસ)", "કપાસ (ભાવનગર/મહુવા)", "Clean Staple", 1390.0, 1630.0, 1530.0, 14200),
            SeedCropTemplate("Groundnut (મગફળી)", "મગફળી (ઉનાળુ/ચોમાસુ)", "Bold & TJ", 1130.0, 1460.0, 1320.0, 8900),
            SeedCropTemplate("Bajra / Pearl Millet (બાજરી)", "બાજરી (દેશી મીઠી)", "Green Bold", 440.0, 565.0, 512.0, 5100),
            SeedCropTemplate("Coconut (મહુવા નાળિયેર - 100 નંગ)", "નાળિયેર (૧૦૦ નંગ ભાવ)", "Mahuva Sweet Water", 1450.0, 2350.0, 1890.0, 9500)
        ),

        // AMRELI & BOTAD APMC
        "amreli_botad_apmc" to listOf(
            SeedCropTemplate("Cotton Botad Benchmark (બોટાદ કપાસ)", "કપાસ (બોટાદ/અમરેલી સુપર)", "A-Grade Long Staple", 1420.0, 1675.0, 1575.0, 32500),
            SeedCropTemplate("Groundnut (મગફળી)", "મગફળી (અમરેલી)", "G-20 Bold", 1140.0, 1475.0, 1330.0, 13800),
            SeedCropTemplate("White Sesame (સફેદ તલ)", "તલ (સફેદ અમરેલી)", "99/1 Sortex", 2400.0, 2930.0, 2710.0, 6100),
            SeedCropTemplate("Chickpea / Chana (ચણા)", "ચણા (દેશી/ડ્રીપ)", "Bold Clean", 1100.0, 1285.0, 1210.0, 8700),
            SeedCropTemplate("Cumin / Jeera (જીરું)", "જીરું (અમરેલી/સાવરકુંડલા)", "Clean", 4200.0, 5150.0, 4760.0, 3400),
            SeedCropTemplate("Wheat (ઘઉં)", "ઘઉં (લોકવન/ટુકડા)", "Mill & Home", 515.0, 650.0, 585.0, 7600)
        ),

        // JAMNAGAR HAPA APMC
        "jamnagar_hapa_apmc" to listOf(
            SeedCropTemplate("Jamnagari Garlic (જામનગરી લસણ)", "લસણ (જામનગર હાપા સ્પેશિયલ)", "Export Ladwa Bold", 2250.0, 4100.0, 3280.0, 12400),
            SeedCropTemplate("Ajwain / Carom Seed (અજમો)", "અજમો (જામનગર બેન્ચમાર્ક)", "Super Green Aroma", 2400.0, 3850.0, 3190.0, 5600),
            SeedCropTemplate("Groundnut (મગફળી)", "મગફળી (હાપા યાર્ડ)", "Bold G-20", 1145.0, 1485.0, 1340.0, 11900),
            SeedCropTemplate("Cotton (કપાસ)", "કપાસ (શંકર-૬)", "Clean Lint", 1395.0, 1640.0, 1540.0, 15300),
            SeedCropTemplate("Cumin / Jeera (જીરું)", "જીરું (હાપા)", "Export Grade", 4280.0, 5190.0, 4810.0, 3900)
        ),

        // UNJHA APMC (NORTH GUJARAT)
        "unjha_apmc" to listOf(
            SeedCropTemplate("Cumin Seed / Jeera (ઊંઝા જીરું બેન્ચમાર્ક)", "જીરું (ઊંઝા એશિયા બેન્ચમાર્ક)", "Singapore / Europe 99%", 4400.0, 5480.0, 4960.0, 28500),
            SeedCropTemplate("Fennel Seed / Variali (ઊંઝા વરિયાળી)", "વરિયાળી (ઊંઝા નવી ગ્રીન)", "Lucknowi / Abu Road Green", 1650.0, 3650.0, 2480.0, 16400),
            SeedCropTemplate("Psyllium Husk / Isabgol (ઈસબગુલ)", "ઈસબગુલ (ઊંઝા ફોતરી/દાણા)", "99% Pure Export", 2450.0, 3120.0, 2840.0, 11800),
            SeedCropTemplate("Mustard Seed / Raydo (રાયડો)", "રાયડો (ઉત્તર ગુજરાત 42% તેલ)", "Bold Black 42% Oil", 1040.0, 1195.0, 1135.0, 14200),
            SeedCropTemplate("Dill Seed / Suva (સુવા દાણા)", "સુવા (ઊંઝા)", "Clean Split/Whole", 1750.0, 2240.0, 2010.0, 4300),
            SeedCropTemplate("White Sesame (સફેદ તલ)", "તલ (સફેદ ઊંઝા)", "Sortex 99.95%", 2450.0, 2980.0, 2740.0, 5800),
            SeedCropTemplate("Fenugreek / Methi (મેથી)", "મેથી (ઊંઝા દાણા)", "Yellow Bold", 1050.0, 1310.0, 1190.0, 4900),
            SeedCropTemplate("Ajwain (અજમો)", "અજમો (ઊંઝા)", "Best Quality", 2350.0, 3680.0, 3080.0, 3200)
        ),

        // DEESA & PALANPUR APMC
        "deesa_palanpur_apmc" to listOf(
            SeedCropTemplate("Deesa Potato (ડીસા બટાટા - પોખરાજ/LR)", "બટાટા (ડીસા પોખરાજ / LR / બાદશાહ)", "Cold Storage & Fresh Table", 240.0, 465.0, 368.0, 54000),
            SeedCropTemplate("Groundnut Banaskantha (બનાસકાંઠા મગફળી)", "મગફળી (ડીસા/પાલનપુર)", "Summer & Kharif Bold", 1170.0, 1510.0, 1365.0, 22400),
            SeedCropTemplate("Castor Seed (એરંડા - દિવેલા)", "એરંડા (ડીસા બેન્ચમાર્ક)", "High Oil Grade", 1180.0, 1290.0, 1248.0, 19500),
            SeedCropTemplate("Mustard Seed (રાયડો)", "રાયડો (બનાસકાંઠા)", "Bold 42% Oil", 1050.0, 1205.0, 1142.0, 17800),
            SeedCropTemplate("Bajra / Pearl Millet (બાજરી)", "બાજરી (બનાસકાંઠા દેશી)", "Sweet Green Grain", 450.0, 575.0, 522.0, 12600),
            SeedCropTemplate("Rajgaro / Amaranth (રાજગરો)", "રાજગરો (ડીસા/થરાદ)", "Fasting Clean White", 1350.0, 1740.0, 1580.0, 6400),
            SeedCropTemplate("Guar Seed (ગવાર)", "ગવાર (ગમ ગ્રેડ)", "Split Gum Quality", 980.0, 1115.0, 1065.0, 7200)
        ),

        // PATAN & HARIJ APMC
        "patan_harij_apmc" to listOf(
            SeedCropTemplate("Castor Seed (પાટણ/હારીજ એરંડા)", "એરંડા (પાટણ અને હારીજ યાર્ડ)", "NCDEX Delivery Grade", 1185.0, 1295.0, 1252.0, 24000),
            SeedCropTemplate("Mustard Seed (રાયડો)", "રાયડો (પાટણ)", "42% Conditioning", 1045.0, 1200.0, 1138.0, 15600),
            SeedCropTemplate("Guar Seed (ગવાર)", "ગવાર (હારીજ)", "FAQ Gum Grade", 990.0, 1120.0, 1070.0, 8900),
            SeedCropTemplate("Cumin / Jeera (જીરું)", "જીરું (પાટણ/સમી)", "Clean Machine", 4320.0, 5280.0, 4850.0, 7400),
            SeedCropTemplate("Wheat (ઘઉં)", "ઘઉં (ટુકડા 496)", "Sortex Quality", 520.0, 655.0, 588.0, 8300),
            SeedCropTemplate("Chickpea / Chana (ચણા)", "ચણા (દેશી)", "Mill Quality", 1095.0, 1275.0, 1205.0, 6100)
        ),

        // HIMATNAGAR & MODASA APMC
        "himatnagar_modasa_apmc" to listOf(
            SeedCropTemplate("Cotton (સાબરકાંઠા કપાસ)", "કપાસ (હિંમતનગર/ઈડર)", "Long Staple Clean", 1400.0, 1650.0, 1550.0, 16800),
            SeedCropTemplate("Yellow Maize / Corn (મકાઈ)", "મકાઈ (મોડાસા/હિંમતનગર પીળી)", "Poultry & Starch Grade", 440.0, 535.0, 492.0, 19400),
            SeedCropTemplate("Wheat Sabarkantha (ઘઉં)", "ઘઉં (હિંમતનગર 496/લોકવન)", "Premium Clean", 530.0, 670.0, 598.0, 14500),
            SeedCropTemplate("Soyabean (સોયાબીન)", "સોયાબીન (અરવલ્લી/મોડાસા)", "Oil & Meal Grade", 855.0, 975.0, 928.0, 11200),
            SeedCropTemplate("Groundnut (મગફળી)", "મગફળી (હિંમતનગર)", "Bold HPS", 1150.0, 1490.0, 1345.0, 9800),
            SeedCropTemplate("Castor Seed (એરંડા)", "એરંડા (મોડાસા)", "Clean Dry", 1170.0, 1280.0, 1240.0, 10600),
            SeedCropTemplate("Tuver (તુવેર)", "તુવેર (ઈડર/ખેડબ્રહ્મા)", "White/Red", 1710.0, 2230.0, 1985.0, 5900)
        ),

        // MEHSANA & VIJAPUR APMC
        "mehsana_vijapur_apmc" to listOf(
            SeedCropTemplate("Wheat Vijapur Benchmark (વિજાપુર ઘઉં)", "ઘઉં (વિજાપુર ટુકડા/લોકવન બેન્ચમાર્ક)", "Gold Sortex 496", 540.0, 695.0, 618.0, 21500),
            SeedCropTemplate("Castor Seed (એરંડા)", "એરંડા (મહેસાણા/વિસનગર)", "Mill Delivery", 1180.0, 1288.0, 1246.0, 16400),
            SeedCropTemplate("Mustard Seed (રાયડો)", "રાયડો (મહેસાણા)", "Bold Black", 1045.0, 1198.0, 1136.0, 11900),
            SeedCropTemplate("Cotton (કપાસ)", "કપાસ (કડી/મહેસાણા)", "Ginning Grade", 1410.0, 1655.0, 1555.0, 18200),
            SeedCropTemplate("Tobacco (વિજાપુર તમાકુ)", "તમાકુ (કલિયા/બિડી)", "Cured Leaf", 1850.0, 2650.0, 2280.0, 5400),
            SeedCropTemplate("Green Lemon (મહેસાણા લીંબુ)", "લીંબુ (કાગદી લીંબુ)", "Fresh Export", 650.0, 1450.0, 1020.0, 4800)
        ),

        // AHMEDABAD & BAVLA APMC
        "ahmedabad_bavla_apmc" to listOf(
            SeedCropTemplate("Paddy Gurjari (ડાંગર ગુર્જરી)", "ડાંગર (ગુર્જરી - બાવળા/સાણંદ)", "Rice Mill Grade", 460.0, 565.0, 522.0, 26500),
            SeedCropTemplate("Paddy Jirasar / Surti Kolam (ડાંગર જીરાસર/કોલમ)", "ડાંગર (જીરાસર / ગુજરાત-૧૭)", "Fine Aromatic", 580.0, 745.0, 675.0, 18900),
            SeedCropTemplate("Bhalia GI Wheat (ભાલિયા/દાઉદખાની ઘઉં)", "ઘઉં (ભાલિયા GI Tag / ધંધુકા-બાવળા)", "Long Grain Protein Rich", 720.0, 980.0, 860.0, 9500),
            SeedCropTemplate("Castor Seed (એરંડા)", "એરંડા (સાણંદ/બાવળા)", "Export Mill", 1175.0, 1285.0, 1242.0, 11400),
            SeedCropTemplate("Cotton (કપાસ)", "કપાસ (વીરમગામ/ધોળકા)", "Shankar-6 / Kalyan", 1370.0, 1620.0, 1515.0, 13200),
            SeedCropTemplate("Tomato Fresh (અમદાવાદ જમાલપુર/જેતલપુર ટામેટાં)", "ટામેટાં (જેતલપુર શાકભાજી યાર્ડ)", "Hybrid A-Grade", 280.0, 640.0, 450.0, 15800)
        ),

        // ANAND, NADIAD & KAPADVANJ APMC
        "anand_kapadvanj_apmc" to listOf(
            SeedCropTemplate("Charotar Tobacco (ચરોતર તમાકુ)", "તમાકુ (આણંદ/ધર્મજ/પેટલાદ)", "Golden Leaf Grade-1", 1950.0, 2850.0, 2440.0, 14600),
            SeedCropTemplate("Paddy Masuri / Jaya (ડાંગર મસૂરી/જયા)", "ડાંગર (ખેડા/નડિયાદ/તારાપુર)", "Clean Dry Paddy", 470.0, 590.0, 538.0, 22400),
            SeedCropTemplate("Potato (ખેડા/કપડવંજ બટાટા)", "બટાટા (કપડવંજ/નડિયાદ વેફર અને ટેબલ)", "Kufri Lauvkar / Pokhraj", 250.0, 480.0, 375.0, 19200),
            SeedCropTemplate("Banana (આણંદ ટીશ્યુ કલ્ચર કેળાં)", "કેળાં (આણંદ/બોરસદ એક્સપોર્ટ)", "Grand Naine G-9", 220.0, 410.0, 325.0, 16500),
            SeedCropTemplate("Tuver (કપડવંજ તુવેર)", "તુવેર (કપડવંજ સફેદ/લાલ)", "Dal Mill Special", 1700.0, 2250.0, 2010.0, 7800),
            SeedCropTemplate("Bajra (બાજરી)", "બાજરી (ચરોતર)", "Summer Bold", 455.0, 570.0, 518.0, 6900)
        ),

        // SURAT & BHARUCH APMC
        "surat_bharuch_apmc" to listOf(
            SeedCropTemplate("Bharuchi Tuver (ભરૂચી તુવેર)", "તુવેર (ભરૂચ/અંકલેશ્વર સ્પેશિયલ)", "GI Style Dal Grade", 1780.0, 2340.0, 2090.0, 11200),
            SeedCropTemplate("Sugarcane Co-Operative Rate (દક્ષિણ ગુજરાત શેરડી - ટન મુજબ)", "શેરડી (સુરત/બારડોલી સુગર મિલ FRP+બોનસ)", "High Recovery 11.5%", 68.0, 82.0, 76.0, 85000),
            SeedCropTemplate("Paddy Jaya / Kolam (ડાંગર ઓલપાડ/નવસારી)", "ડાંગર (દક્ષિણ ગુજરાત કોલમ)", "Super Fine", 510.0, 680.0, 605.0, 17400),
            SeedCropTemplate("Cotton Bharuch/Karjan (કપાસ)", "કપાસ (ભરૂચ/કરજણ)", "Long Staple", 1410.0, 1645.0, 1548.0, 10800),
            SeedCropTemplate("Navsari Chiku / Sapota (નવસારી/વલસાડ ચીકુ)", "ચીકુ (અમલસાડી/નવસારી એક્સપોર્ટ)", "Kalipatti A-Grade", 520.0, 960.0, 740.0, 12300),
            SeedCropTemplate("Banana South Gujarat (કામરેજ/રાજપીપળા કેળાં)", "કેળાં (એક્સપોર્ટ બોક્સ)", "G-9 Premium", 230.0, 430.0, 340.0, 18600)
        ),

        // BHUJ & ANJAR KUTCH APMC
        "bhuj_anjar_kutch_apmc" to listOf(
            SeedCropTemplate("Castor Seed Kutch (કચ્છ એરંડા)", "એરંડા (અંજાર/ભૂજ/ગાંધીધામ કંડલા પોર્ટ)", "Kandla Export Delivery", 1190.0, 1305.0, 1260.0, 21000),
            SeedCropTemplate("Isabgol Kutch (કચ્છ ઈસબગુલ)", "ઈસબગુલ (રાપર/ભચાઉ)", "Export Clean", 2420.0, 3090.0, 2810.0, 8900),
            SeedCropTemplate("Cumin / Jeera (કચ્છ જીરું)", "જીરું (રાપર/અંજાર)", "Dry Bold", 4350.0, 5320.0, 4880.0, 9400),
            SeedCropTemplate("Kutch Barhi Dates (કચ્છી ખારેક - 20Kg)", "કચ્છી ખારેક (મુંદ્રા/ભૂજ બરહી ખારેક)", "Sweet Red & Yellow Barhi", 950.0, 2200.0, 1540.0, 11200),
            SeedCropTemplate("Bhagwa Pomegranate (કચ્છ દાડમ)", "દાડમ (ભગવા એક્સપોર્ટ ગ્રેડ)", "Export Carton Quality", 1200.0, 2450.0, 1860.0, 13500)
        ),

        // LASALGAON & PUNE APMC (MAHARASHTRA)
        "lasalgaon_nashik_mh" to listOf(
            SeedCropTemplate("Lasalgaon Red Onion (લાસલગાંવ/નાસિક લાલ ડુંગળી)", "નાસિક ડુંગળી (એશિયા બેન્ચમાર્ક યાર્ડ)", "Summer Storage / Export", 260.0, 620.0, 468.0, 92000),
            SeedCropTemplate("Soyabean Maharashtra (મહારાષ્ટ્ર સોયાબીન)", "સોયાબીન (લાતુર/અકોલા/નાસિક)", "Yellow FAQ Oil Mill", 860.0, 985.0, 934.0, 34000),
            SeedCropTemplate("Tur / Arhar Dal (લાતુર/અમરાવતી તુવેર)", "તુવેર (મહારાષ્ટ્ર લાલ/સફેદ)", "MSP + Market Premium", 1760.0, 2310.0, 2065.0, 16800),
            SeedCropTemplate("Cotton Vidarbha/Khandesh (કપાસ)", "કપાસ (જળગાંવ/યવતમાળ)", "Long Staple CCI", 1415.0, 1650.0, 1552.0, 28400),
            SeedCropTemplate("Nashik Export Grapes (નાસિક દ્રાક્ષ)", "દ્રાક્ષ (થોમ્પસન/સોનાકા એક્સપોર્ટ)", "Europe/Local Grade", 950.0, 1850.0, 1380.0, 21500),
            SeedCropTemplate("Solapur/Pune Pomegranate (સોલાપુર દાડમ)", "દાડમ (ભગવા સુપર)", "A-Grade", 1250.0, 2500.0, 1890.0, 14200)
        ),

        // NEEMUCH & INDORE MANDI (MP)
        "neemuch_indore_mp" to listOf(
            SeedCropTemplate("MP Sharbati Wheat (MP શરબતી ઘઉં)", "ઘઉં (સિહોર/ઈન્દોર અસલી શરબતી)", "Golden Lustre Grade-1", 680.0, 940.0, 815.0, 31000),
            SeedCropTemplate("Indore Soyabean Benchmark (ઈન્દોર સોયાબીન)", "સોયાબીન (ઈન્દોર NCDEX બેન્ચમાર્ક)", "Yellow Plant Delivery", 870.0, 995.0, 942.0, 45000),
            SeedCropTemplate("Neemuch Ooty Garlic (નીમચ/મંદસૌર ઊંચું લસણ)", "લસણ (નીમચ Ooty/Riyawan Silver)", "Extra Bold White", 2400.0, 4450.0, 3490.0, 22000),
            SeedCropTemplate("Dollar Chana / Kabuli Chickpea (ઈન્દોર ડોલર ચણા)", "ડોલર ચણા (કાબુલી એક્સપોર્ટ)", "42-44 Count Export", 2150.0, 2780.0, 2490.0, 12800),
            SeedCropTemplate("Ashwagandha Roots (નીમચ અશ્વગંધા મૂળ)", "અશ્વગંધા (નીમચ ઔષધીય યાર્ડ)", "Pencil Root A-Grade", 3800.0, 6400.0, 5150.0, 4600),
            SeedCropTemplate("Coriander MP (નીમચ/ગુના ધાણા)", "ધાણા (ગ્રીન ઈગલ)", "Sortex Clean", 1280.0, 1860.0, 1550.0, 15400)
        ),

        // KOTA & JODHPUR MANDI (RAJASTHAN)
        "kota_jodhpur_rj" to listOf(
            SeedCropTemplate("Rajasthan Mustard Seed (રાજસ્થાન સરસવ/રાયડો)", "રાયડો/સરસવ (કોટા/ભરતપુર 42% તેલ)", "42% Lab Condition", 1060.0, 1220.0, 1152.0, 38000),
            SeedCropTemplate("Jodhpur Jeera (જોધપુર/મેડતા જીરું)", "જીરું (જોધપુર અને મેડતા સિટી)", "Bold Aroma", 4350.0, 5380.0, 4890.0, 19500),
            SeedCropTemplate("Jodhpur/Bikaner Guar Seed (જોધપુર ગવાર)", "ગવાર (જોધપુર ગમ બેન્ચમાર્ક)", "NCDEX Gum Grade", 1010.0, 1145.0, 1088.0, 26400),
            SeedCropTemplate("Merta Green Moong (મેડતા/નાગૌર લીલા મગ)", "મગ (મેડતા પોલિશ ગ્રેડ)", "Bold Shiny", 1520.0, 1920.0, 1740.0, 14800),
            SeedCropTemplate("Bikaner Groundnut (બીકાનેર મગફળી)", "મગફળી (બીકાનેર HPS)", "Bold Table", 1140.0, 1470.0, 1325.0, 18200)
        ),

        // KHANNA & KARNAL MANDI (PUNJAB & HARYANA)
        "khanna_karnal_pb_hr" to listOf(
            SeedCropTemplate("Basmati Paddy 1121 (બાસમતી ડાંગર 1121)", "બાસમતી ડાંગર (1121 Steam/Sella Grade)", "Extra Long Export", 780.0, 965.0, 885.0, 52000),
            SeedCropTemplate("Basmati Paddy 1509/1718 (બાસમતી ડાંગર 1509)", "બાસમતી ડાંગર (1509 / 1718)", "Export Mill", 640.0, 790.0, 725.0, 41000),
            SeedCropTemplate("Milling Wheat MSP+ (પંજાબ/હરિયાણા ઘઉં)", "ઘઉં (ખન્ના એશિયાની સૌથી મોટી અનાજ મંડી)", "FCI / Roller Flour Mill", 495.0, 575.0, 538.0, 78000),
            SeedCropTemplate("Cotton North Zone (પંજાબ/હરિયાણા કપાસ)", "કપાસ (નરમા J-34)", "Roller Ginning", 1380.0, 1610.0, 1510.0, 16500),
            SeedCropTemplate("Hybrid Yellow Maize (મકાઈ)", "મકાઈ (હોશિયારપુર/કરનાલ)", "Dry 14% Moisture", 445.0, 530.0, 490.0, 14200)
        ),

        // GUNTUR, KOCHI & ERODE SPICE APMC (SOUTH INDIA)
        "guntur_kochi_south_in" to listOf(
            SeedCropTemplate("Guntur Red Chilli Teja (ગુંટૂર લાલ મરચાં તેજા/334)", "લાલ મરચાં (ગુંટૂર એશિયા બેન્ચમાર્ક)", "Teja S-17 Export / Cold Store", 2950.0, 4350.0, 3680.0, 65000),
            SeedCropTemplate("Kochi Black Pepper (કોચી કાળા મરી)", "કાળા મરી (કોચી સ્પાઈસ બોર્ડ)", "Garbled MG-1", 11800.0, 13600.0, 12850.0, 4800),
            SeedCropTemplate("Erode / Sangli Turmeric (ઈરોડ/સાંગલી હળદર)", "હળદર (ફિંગર અને ગાંઠો)", "Polished Salem/Erode", 2450.0, 3380.0, 2940.0, 18500),
            SeedCropTemplate("Idukki Green Cardamom (કેરળ લીલી એલચી)", "લીલી એલચી (8mm બોલ્ડ)", "Auction Average", 38000.0, 49500.0, 44200.0, 2200),
            SeedCropTemplate("Milling Copra / Coconut (કોપરા)", "કોપરા (તમિલનાડુ/કેરળ NAFED)", "Ball & Milling Copra", 2150.0, 2680.0, 2420.0, 9600)
        ),

        // GLOBAL: USA (USDA AMS & CBOT)
        "usa_usda_cbot" to listOf(
            SeedCropTemplate("US Soybeans #1 Yellow (અમેરિકન સોયાબીન)", "US Soybeans (USDA Illinois/CBOT)", "No. 1 Yellow Export", 740.0, 860.0, 805.0, 125000, "USD / Bushel (27.2 Kg)", "USD"),
            SeedCropTemplate("US Upland Cotton (અમેરિકન કપાસ)", "US Cotton (USDA Texas/Delta Lint Eqv.)", "SLM 1-1/16 Inch", 1460.0, 1720.0, 1595.0, 84000, "USD / CWT (45.36 Kg)", "USD"),
            SeedCropTemplate("US Yellow Corn #2 (અમેરિકન મકાઈ)", "US Yellow Corn (USDA Midwest)", "No. 2 Yellow Feed/Ethanol", 380.0, 460.0, 422.0, 195000, "USD / Bushel (25.4 Kg)", "USD"),
            SeedCropTemplate("US Hard Red Winter Wheat (અમેરિકન ઘઉં)", "US Wheat (USDA Kansas/Chicago)", "HRW 12% Protein", 440.0, 535.0, 488.0, 110000, "USD / Bushel (27.2 Kg)", "USD"),
            SeedCropTemplate("US Georgia Runner Peanuts (અમેરિકન મગફળી)", "US Runner Peanuts (USDA Georgia)", "Shelled/Farmer Stock", 1090.0, 1340.0, 1225.0, 46000, "USD / Ton", "USD")
        ),

        // GLOBAL: BRAZIL (CONAB & CEPEA)
        "brazil_conab_cepea" to listOf(
            SeedCropTemplate("Brazil Paranaguá Soybeans (બ્રાઝિલ સોયાબીન)", "Brazil Soybeans (CEPEA/CONAB Index)", "Export Port Paranaguá", 720.0, 845.0, 788.0, 160000, "BRL / 60 Kg Bag", "BRL"),
            SeedCropTemplate("Brazil Arabica Coffee (બ્રાઝિલ અરેબિકા કોફી)", "Arabica Coffee (CEPEA São Paulo)", "Type 6 Hard Drink", 6800.0, 8200.0, 7540.0, 58000, "BRL / 60 Kg Bag", "BRL"),
            SeedCropTemplate("Brazil Mato Grosso Cotton (બ્રાઝિલ કપાસ)", "Brazil Cotton (CEPEA/ESALQ)", "Export Lint Equivalent", 1420.0, 1660.0, 1545.0, 72000, "BRL / Arroba (15 Kg)", "BRL"),
            SeedCropTemplate("Brazil Yellow Maize (બ્રાઝિલ મકાઈ)", "Brazil Corn (CONAB Campinas)", "Dry Export Grade", 370.0, 455.0, 415.0, 115000, "BRL / 60 Kg Bag", "BRL"),
            SeedCropTemplate("Brazil White Crystal Sugar (બ્રાઝિલ ખાંડ/શેરડી)", "Crystal Sugar (CEPEA São Paulo)", "ICUMSA 150", 710.0, 840.0, 778.0, 94000, "BRL / 50 Kg Bag", "BRL")
        ),

        // GLOBAL: EUROPE & UK (EU AGRI-FOOD PORTAL / MATIF / DEFRA)
        "europe_eu_matif_defra" to listOf(
            SeedCropTemplate("EU Milling Wheat (યુરોપિયન ઘઉં - MATIF)", "EU Milling Wheat (Rouen/Paris MATIF)", "11.5% Protein Euronext", 460.0, 555.0, 512.0, 105000, "EUR / Metric Ton (1000 Kg)", "EUR"),
            SeedCropTemplate("EU Rapeseed / Canola (યુરોપિયન રાયડો/કેનોલા)", "EU Rapeseed (Euronext / Germany)", "40% Oil Content", 980.0, 1160.0, 1075.0, 68000, "EUR / Metric Ton (1000 Kg)", "EUR"),
            SeedCropTemplate("EU Malting Barley (યુરોપિયન જવ)", "EU Feed & Malting Barley (France/UK)", "Export Standard", 410.0, 495.0, 454.0, 54000, "EUR / Metric Ton (1000 Kg)", "EUR"),
            SeedCropTemplate("European Processing Potatoes (યુરોપિયન બટાટા)", "EU Processing Potatoes (NL/BE/UK DEFRA)", "Fontane / Fry Grade", 390.0, 580.0, 485.0, 82000, "EUR / 100 Kg Quintal", "EUR")
        ),

        // GLOBAL: JAPAN & EAST ASIA (MAFF JAPAN)
        "japan_maff_east_asia" to listOf(
            SeedCropTemplate("Japan Koshihikari Rice (જાપાન કોશીહિકારી ચોખા)", "Koshihikari Rice (MAFF Tokyo/Niigata)", "Grade-1 Brown Rice", 3200.0, 4150.0, 3690.0, 48000, "JPY / 60 Kg Tawara", "JPY"),
            SeedCropTemplate("Japan Hokkaido Food Soybeans (જાપાન સોયાબીન)", "Hokkaido Tofu Soybeans (MAFF Official)", "Yukihomare Large", 2150.0, 2780.0, 2460.0, 22000, "JPY / 60 Kg Bag", "JPY"),
            SeedCropTemplate("Shizuoka/Kagoshima Green Tea (જાપાન ગ્રીન ટી)", "Sencha Green Tea Aracha (MAFF Japan)", "First Flush Leaf", 4800.0, 7400.0, 6100.0, 14500, "JPY / 20 Kg Equivalent", "JPY"),
            SeedCropTemplate("Hokkaido Onion & Potato (જાપાન ડુંગળી/બટાટા)", "Hokkaido Onion (Tokyo Ota Market)", "L-Size Carton", 920.0, 1380.0, 1150.0, 36000, "JPY / 20 Kg Carton", "JPY")
        ),

        // GLOBAL: AUSTRALIA & CANADA (ABARES / AAFC)
        "australia_abares_canada" to listOf(
            SeedCropTemplate("Australian Desi Chickpeas (ઓસ્ટ્રેલિયન દેશી ચણા)", "Desi Chickpeas (ABARES Brisbane/Sydney)", "Container Export Grade", 1120.0, 1320.0, 1225.0, 64000, "AUD / Metric Ton", "AUD"),
            SeedCropTemplate("Canadian/Australian Red Lentils (લાલ મસૂર)", "Red Lentils (Saskatchewan / Victoria)", "No. 2 Crimson", 1180.0, 1410.0, 1295.0, 58000, "AUD / Metric Ton", "AUD"),
            SeedCropTemplate("Australian Canola Seed (ઓસ્ટ્રેલિયન કેનોલા/રાયડો)", "Canola Non-GM (ABARES Kwinana)", "Export Oilseed", 990.0, 1170.0, 1085.0, 72000, "AUD / Metric Ton", "AUD"),
            SeedCropTemplate("Australian Prime Hard Wheat (ઓસ્ટ્રેલિયન ઘઉં)", "APW / APH Wheat (ABARES Port Kembla)", "High Protein Milling", 480.0, 585.0, 535.0, 96000, "AUD / Metric Ton", "AUD")
        ),

        // GLOBAL: UAE DUBAI AL AWEER & AFRICA EXPORT HUB
        "uae_al_aweer_mena" to listOf(
            SeedCropTemplate("Dubai Import Unjha Jeera (દુબઈ ઈમ્પોર્ટ ઊંઝા જીરું)", "Indian Cumin 99% (Al Aweer Dubai Market)", "Gulf Export Pack", 4650.0, 5780.0, 5240.0, 19500, "AED / 20 Kg Bag", "AED"),
            SeedCropTemplate("Dubai Import Nashik/Mahuva Onion (દુબઈ ઈમ્પોર્ટ ડુંગળી)", "Indian Red Onion (Al Aweer Central Market)", "55mm+ Gulf Export", 380.0, 780.0, 585.0, 48000, "AED / 20 Kg Mesh Bag", "AED"),
            SeedCropTemplate("Arabian Medjool & Khalas Dates (અરેબિયન ખજૂર)", "Khalas & Medjool Dates (UAE/Saudi Board)", "Jumbo Crown Grade", 1850.0, 3600.0, 2680.0, 31000, "AED / 20 Kg Box", "AED"),
            SeedCropTemplate("African White Sesame (સુદાન/નાઈજીરિયા સફેદ તલ)", "Whitish Sesame (Port Sudan / Mombasa)", "99% Hulled/Natural", 2320.0, 2840.0, 2590.0, 27000, "AED / 20 Kg Eqv.", "AED")
        )
    )

    fun getYardsByZone(zone: ApmcRegionZone): List<ApmcYardProfile> {
        return allYardProfiles.filter { it.regionZone == zone }
    }

    fun getYardById(yardId: String): ApmcYardProfile {
        return allYardProfiles.firstOrNull { it.yardId == yardId } ?: allYardProfiles.first()
    }

    /**
     * Generates an immediate offline-ready snapshot calibrated to today's date so the UI
     * renders instantaneously on opening, even before the background HTTP sync completes.
     */
    /**
     * Starts empty until an official price record is fetched. Seed templates are not market quotes
     * and must never be presented as live prices.
     */
    fun getInstantCalibratedSnapshot(): LiveApmcSyncSnapshot {
        return LiveApmcSyncSnapshot(
            quotes = emptyList(),
            isOnlineSynced = false,
            syncStatusBanner = "NOT SYNCED • Tap Sync Live to fetch official daily India APMC records. Unsupported country feeds are not shown as live.",
            lastUpdatedTimeFormatted = "Not synced",
            usdToInrRate = 0.0,
            eurToInrRate = 0.0,
            brlToInrRate = 0.0,
            jpyToInrRate = 0.0,
            audToInrRate = 0.0
        )
    }

    /**
     * Fetches actual daily Agmarknet records from India's Open Government Data API.
     * Currency exchange rates are retrieved separately and never used to invent crop prices.
     * This is a daily market-price source, not tick-by-tick trading data.
     */
    suspend fun fetchAndSyncLiveApmcRates(): LiveApmcSyncSnapshot = withContext(Dispatchers.IO) {
        var usdToInr = 0.0
        var eurToInr = 0.0
        var brlToInr = 0.0
        var jpyToInr = 0.0
        var audToInr = 0.0
        var fxSynced = false

        try {
            val connection = (URL("https://open.er-api.com/v6/latest/USD").openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("Accept", "application/json")
            }
            try {
                if (connection.responseCode == 200) {
                    val json = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
                    val rates = json.optJSONObject("rates")
                    if (rates != null) {
                        val inr = rates.optDouble("INR", 0.0)
                        val eur = rates.optDouble("EUR", 0.0)
                        val brl = rates.optDouble("BRL", 0.0)
                        val jpy = rates.optDouble("JPY", 0.0)
                        val aud = rates.optDouble("AUD", 0.0)
                        if (inr > 50.0 && eur > 0.0 && brl > 0.0 && jpy > 0.0 && aud > 0.0) {
                            usdToInr = inr
                            eurToInr = inr / eur
                            brlToInr = inr / brl
                            jpyToInr = inr / jpy
                            audToInr = inr / aud
                            fxSynced = true
                        }
                    }
                }
            } finally {
                connection.disconnect()
            }
        } catch (_: Exception) {
            // FX is optional and must not affect whether a crop price is considered live.
        }

        // The Gujarat query prioritizes the local APMCs this app lists; the general query adds
        // other Indian market records. Both are official daily records from data.gov.in.
        val rowsByKey = LinkedHashMap<String, JSONObject>()
        listOf("Gujarat", null).forEach { stateFilter ->
            fetchAgmarknetRecords(stateFilter).forEach { row ->
                val key = listOf("state", "district", "market", "commodity", "variety", "grade", "arrival_date")
                    .joinToString("|") { row.optString(it, "").trim().lowercase(Locale.US) }
                rowsByKey[key] = row
            }
        }

        val quotes = rowsByKey.values.mapNotNull { row -> row.toVerifiedApmcQuote() }
            .distinctBy { it.quoteId }
            .sortedWith(compareBy<LiveCropPriceQuote>({ it.regionZone.ordinal }, { it.yardName }, { it.cropNameEn }))

        val recentCount = quotes.count { it.isLiveNetworkSynced }
        val nowFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(Date())
        val status = when {
            recentCount > 0 -> "OFFICIAL AGMARKNET DATA • ${recentCount} recent price records • Market date and source shown on each quote. India daily feed only."
            quotes.isNotEmpty() -> "OFFICIAL AGMARKNET DATA LOADED • All returned records are dated; check each market date before using a rate."
            fxSynced -> "CURRENCY RATES SYNCED, BUT NO VERIFIED CROP PRICES RETURNED • Exchange rates are not crop prices. Retry the India APMC feed."
            else -> "NO VERIFIED CROP PRICES RECEIVED • Check internet/API availability and tap Sync Live again. Unavailable country feeds are not replaced with sample prices."
        }

        LiveApmcSyncSnapshot(
            quotes = quotes,
            isOnlineSynced = recentCount > 0,
            syncStatusBanner = status,
            lastUpdatedTimeFormatted = nowFormatted,
            usdToInrRate = usdToInr,
            eurToInrRate = eurToInr,
            brlToInrRate = brlToInr,
            jpyToInrRate = jpyToInr,
            audToInrRate = audToInr
        )
    }

    private fun fetchAgmarknetRecords(stateFilter: String?): List<JSONObject> {
        return try {
            val encodedFilter = stateFilter?.let { "&filters%5Bstate%5D=${it.replace(" ", "%20")}" }.orEmpty()
            val endpoint =
                "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070" +
                    "?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b" +
                    "&format=json&limit=1000&offset=0$encodedFilter"
            val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 7000
                readTimeout = 7000
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "Khetrova-Android/1.0")
            }
            try {
                if (connection.responseCode != 200) return emptyList()
                val root = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
                val records = root.optJSONArray("records") ?: return emptyList()
                (0 until records.length()).mapNotNull { records.optJSONObject(it) }
            } finally {
                connection.disconnect()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun JSONObject.toVerifiedApmcQuote(): LiveCropPriceQuote? {
        val state = optString("state", "").trim()
        val district = optString("district", "").trim()
        val market = optString("market", "").trim()
        val commodity = optString("commodity", "").trim()
        val variety = optString("variety", "").trim()
        val grade = optString("grade", "").trim()
        val marketDate = optString("arrival_date", "").trim()
        val minPrice = optString("min_price", "").trim().toDoubleOrNull() ?: return null
        val maxPrice = optString("max_price", "").trim().toDoubleOrNull() ?: return null
        val modalPrice = optString("modal_price", "").trim().toDoubleOrNull() ?: return null

        if (state.isBlank() || district.isBlank() || market.isBlank() || commodity.isBlank() ||
            marketDate.isBlank() || minPrice <= 0.0 || modalPrice <= 0.0 || maxPrice <= 0.0 ||
            minPrice > modalPrice || modalPrice > maxPrice
        ) return null

        val yardId = findConfiguredIndianYardId(state, district, market) ?: return null
        val yard = allYardProfiles.firstOrNull { it.yardId == yardId } ?: return null
        val safeMarket = market.lowercase(Locale.US).replace(Regex("[^a-z0-9]+"), "_").trim('_')
        val safeCommodity = commodity.lowercase(Locale.US).replace(Regex("[^a-z0-9]+"), "_").trim('_')
        val gradeLabel = listOf(variety, grade).filter { it.isNotBlank() && !it.equals("NA", true) }.joinToString(" / ")
            .ifBlank { "As reported by market" }

        // The official feed reports INR per quintal (100 kg). One 20 kg man is one fifth of that.
        return LiveCropPriceQuote(
            quoteId = "${yard.yardId}_${safeMarket}_${safeCommodity}_${marketDate.replace("/", "-")}",
            yardId = yard.yardId,
            yardName = market,
            districtOrState = "$district, $state",
            regionZone = yard.regionZone,
            cropNameEn = commodity,
            cropNameLocal = commodity,
            varietyOrGrade = gradeLabel,
            minPricePer20KgManInr = minPrice / 5.0,
            maxPricePer20KgManInr = maxPrice / 5.0,
            modalPricePer20KgManInr = modalPrice / 5.0,
            minPricePerQuintalInr = minPrice,
            maxPricePerQuintalInr = maxPrice,
            modalPricePerQuintalInr = modalPrice,
            dailyChangePerManInr = 0.0,
            arrivalBagsCount = 0,
            nativeCurrencySymbol = "₹",
            nativeUnitLabel = "INR / Quintal (100 Kg)",
            nativeModalPrice = modalPrice,
            governmentSourceLabel = "Government of India OGD / Agmarknet daily market-price record",
            updatedTimestampText = marketDate,
            isLiveNetworkSynced = isRecentMarketDate(marketDate),
            hasVerifiedDailyChange = false,
            hasVerifiedArrivalData = false
        )
    }

    private fun findConfiguredIndianYardId(state: String, district: String, market: String): String? {
        val stateKey = state.lowercase(Locale.US)
        val place = "$market $district".lowercase(Locale.US)
        return when {
            stateKey.contains("gujarat") -> when {
                "gondal" in place -> "gondal_apmc"
                "rajkot" in place || "bedi" in place -> "rajkot_bedi_apmc"
                "junagadh" in place -> "junagadh_apmc"
                "mahuva" in place || "bhavnagar" in place -> "mahuva_bhavnagar_apmc"
                "amreli" in place || "botad" in place -> "amreli_botad_apmc"
                "jamnagar" in place || "hapa" in place -> "jamnagar_hapa_apmc"
                "unjha" in place -> "unjha_apmc"
                "deesa" in place || "palanpur" in place -> "deesa_palanpur_apmc"
                "patan" in place || "harij" in place -> "patan_harij_apmc"
                "himatnagar" in place || "modasa" in place -> "himatnagar_modasa_apmc"
                "mehsana" in place || "vijapur" in place -> "mehsana_vijapur_apmc"
                "ahmedabad" in place || "bavla" in place || "jetalpur" in place -> "ahmedabad_bavla_apmc"
                "anand" in place || "nadiad" in place || "kapadvanj" in place -> "anand_kapadvanj_apmc"
                "surat" in place || "bharuch" in place -> "surat_bharuch_apmc"
                "bhuj" in place || "anjar" in place || "kutch" in place -> "bhuj_anjar_kutch_apmc"
                else -> null
            }
            stateKey.contains("maharashtra") -> if ("lasalgaon" in place || "pune" in place || "nashik" in place) "lasalgaon_nashik_mh" else null
            stateKey.contains("madhya") -> if ("neemuch" in place || "indore" in place) "neemuch_indore_mp" else null
            stateKey.contains("rajasthan") -> if ("kota" in place || "jodhpur" in place) "kota_jodhpur_rj" else null
            stateKey.contains("punjab") || stateKey.contains("haryana") ->
                if ("khanna" in place || "karnal" in place) "khanna_karnal_pb_hr" else null
            stateKey.contains("andhra") || stateKey.contains("kerala") || stateKey.contains("tamil") ->
                if ("guntur" in place || "kochi" in place || "erode" in place) "guntur_kochi_south_in" else null
            else -> null
        }
    }

    private fun isRecentMarketDate(dateText: String): Boolean {
        val patterns = listOf("dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy", "dd MMM yyyy", "d/M/yyyy")
        val parsed = patterns.firstNotNullOfOrNull { pattern ->
            try {
                SimpleDateFormat(pattern, Locale.US).apply { isLenient = false }.parse(dateText)
            } catch (_: Exception) {
                null
            }
        } ?: return false

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val marketDay = Calendar.getInstance().apply {
            time = parsed
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val daysOld = (today - marketDay) / (24L * 60L * 60L * 1000L)
        return daysOld in 0L..2L
    }

    private fun getTodayDaySeed(): Int {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val hourSlot = cal.get(Calendar.HOUR_OF_DAY) / 3
        return (year * 1000) + (dayOfYear * 10) + hourSlot
    }

    private fun buildQuotesForAllYards(
        daySeed: Int,
        livePulseFactor: Double,
        usdToInr: Double,
        eurToInr: Double,
        brlToInr: Double,
        jpyToInr: Double,
        audToInr: Double,
        aedToInr: Double,
        agmarknetOverrides: Map<String, Double>,
        updatedTimestampText: String,
        isLiveNetworkSynced: Boolean
    ): List<LiveCropPriceQuote> {
        val allQuotes = mutableListOf<LiveCropPriceQuote>()

        for (yard in allYardProfiles) {
            val templates = yardSeedCatalog[yard.yardId].orEmpty()
            templates.forEachIndexed { idx, tpl ->
                val hashSeed = abs((yard.yardId.hashCode() * 31) + (tpl.cropNameEn.hashCode() * 17) + daySeed + idx)
                // Daily regional variation between -2.8% and +3.1% so each yard has realistic daily movement
                val dailyPctDelta = (((hashSeed % 60) - 28) / 1000.0) * livePulseFactor
                val prevDayPctDelta = ((((hashSeed / 7) % 50) - 24) / 1000.0)

                // Check if Agmarknet live override matched this commodity
                val matchedOverrideMan = agmarknetOverrides.entries.firstOrNull { (k, _) ->
                    tpl.cropNameEn.lowercase(Locale.US).contains(k)
                }?.value

                val effectiveBaseModal = if (matchedOverrideMan != null && !yard.regionZone.isInternational) {
                    // Blend 35% national live override + 65% regional yard specialty premium
                    (tpl.baseModalManInr * 0.65) + (matchedOverrideMan * 0.35)
                } else {
                    tpl.baseModalManInr
                }

                val modalManInr = (effectiveBaseModal * (1.0 + dailyPctDelta)).roundToNearestFiveOrOne()
                val minManInr = (tpl.baseMinManInr * (1.0 + (dailyPctDelta * 0.85))).roundToNearestFiveOrOne()
                    .coerceAtMost(modalManInr)
                val maxManInr = (tpl.baseMaxManInr * (1.0 + (dailyPctDelta * 1.12))).roundToNearestFiveOrOne()
                    .coerceAtLeast(modalManInr)

                val yesterdayModalManInr = (effectiveBaseModal * (1.0 + prevDayPctDelta)).roundToNearestFiveOrOne()
                val dailyChangePerManInr = (modalManInr - yesterdayModalManInr)

                // 1 Quintal (100 Kg) = 5 x 20 Kg Man (મણ)
                val minQuintalInr = minManInr * 5.0
                val maxQuintalInr = maxManInr * 5.0
                val modalQuintalInr = modalManInr * 5.0

                val arrivalVariance = 0.88 + ((hashSeed % 28) / 100.0)
                val arrivalBags = (tpl.baseArrivalBags * arrivalVariance).roundToInt().coerceAtLeast(120)

                // Compute native currency modal price for international government markets
                val nativeModalPrice = when (tpl.nativeCurrencyCode) {
                    "USD" -> {
                        when {
                            tpl.nativeUnitLabel.contains("Bushel") -> {
                                // ~1.35 Man per Bushel
                                (modalManInr * 1.35) / usdToInr
                            }
                            tpl.nativeUnitLabel.contains("CWT") -> {
                                // 45.36 Kg = 2.268 Man
                                (modalManInr * 2.268) / usdToInr
                            }
                            else -> (modalQuintalInr * 10.0) / usdToInr
                        }
                    }
                    "BRL" -> {
                        // 60 Kg bag = 3 Man
                        (modalManInr * 3.0) / brlToInr
                    }
                    "EUR" -> {
                        if (tpl.nativeUnitLabel.contains("Metric Ton")) {
                            // 1000 Kg = 50 Man
                            (modalManInr * 50.0) / eurToInr
                        } else {
                            // 100 Kg Quintal = 5 Man
                            modalQuintalInr / eurToInr
                        }
                    }
                    "JPY" -> {
                        if (tpl.nativeUnitLabel.contains("60 Kg")) {
                            (modalManInr * 3.0) / jpyToInr
                        } else {
                            modalManInr / jpyToInr
                        }
                    }
                    "AUD" -> {
                        // Metric Ton = 50 Man
                        (modalManInr * 50.0) / audToInr
                    }
                    "AED" -> {
                        modalManInr / aedToInr
                    }
                    else -> modalManInr
                }

                allQuotes.add(
                    LiveCropPriceQuote(
                        quoteId = "${yard.yardId}_${idx}",
                        yardId = yard.yardId,
                        yardName = yard.yardName,
                        districtOrState = yard.districtOrState,
                        regionZone = yard.regionZone,
                        cropNameEn = tpl.cropNameEn,
                        cropNameLocal = tpl.cropNameLocal,
                        varietyOrGrade = tpl.varietyOrGrade,
                        minPricePer20KgManInr = minManInr,
                        maxPricePer20KgManInr = maxManInr,
                        modalPricePer20KgManInr = modalManInr,
                        minPricePerQuintalInr = minQuintalInr,
                        maxPricePerQuintalInr = maxQuintalInr,
                        modalPricePerQuintalInr = modalQuintalInr,
                        dailyChangePerManInr = dailyChangePerManInr,
                        arrivalBagsCount = arrivalBags,
                        nativeCurrencySymbol = yard.defaultCurrencySymbol,
                        nativeUnitLabel = tpl.nativeUnitLabel,
                        nativeModalPrice = (nativeModalPrice * 100.0).roundToInt() / 100.0,
                        governmentSourceLabel = yard.governmentPortalName,
                        updatedTimestampText = updatedTimestampText,
                        isLiveNetworkSynced = isLiveNetworkSynced
                    )
                )
            }
        }

        return allQuotes
    }

    private fun Double.roundToNearestFiveOrOne(): Double {
        return if (this >= 1000.0) {
            (this / 5.0).roundToInt() * 5.0
        } else {
            this.roundToInt().toDouble()
        }
    }
}
