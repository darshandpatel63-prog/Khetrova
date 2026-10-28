package com.example.domain

data class AppLanguage(
    val code: String,
    val englishName: String,
    val nativeName: String,
    val regionGroup: String,
    val isRtl: Boolean = false
)

data class LocalizedMenuEntry(
    val category: String,
    val title: String,
    val subtitle: String,
    val badge: String
)

data class LocalizedUiStrings(
    val appTitle: String,
    val appSubtitle: String,
    val zeroPermissionBadge: String,
    val navLandUnits: String,
    val navPlotCad: String,
    val navMaterials: String,
    val navVaultCode: String,
    val selectStateStandard: String,
    val chooseUnitAndArea: String,
    val areaLabel: String,
    val rateLabel: String,
    val instantEquivalents: String,
    val landValuationTitle: String,
    val basePlotValue: String,
    val estStampDuty: String,
    val totalLandCost: String,
    val saveToVault: String,
    val exportPdf: String,
    val liveCadBlueprint: String,
    val triangulatedArea: String,
    val perimeter: String,
    val contractorErrorDetector: String,
    val enterFourSides: String,
    val northSide: String,
    val southSide: String,
    val eastSide: String,
    val westSide: String,
    val diagonalSide: String,
    val autoCalibrateDiagonal: String,
    val selectCivilCategory: String,
    val enterRoomDimensions: String,
    val lengthFeet: String,
    val widthOrHeightFeet: String,
    val billOfQuantities: String,
    val totalMaterialCost: String,
    val savedVaultTab: String,
    val polyglotTab: String,
    val legalTab: String,
    val languagePickerTitle: String,
    val searchLanguageHint: String,
    val navMap3D: String = "Map & 3D",
    val menuAllToolsBtn: String = "Menu • All Tools",
    val allFeaturesRibbonBtn: String = "All Features (24+)",
    val dailyRojmelTab: String = "Daily Rojmel, Mandi & Broker",
    val allFeaturesMenuTitle: String = "All Features & Pages Menu",
    val allFeaturesMenuSubtitle: String = "Tap any tool (Daily Mandi, Labour, Broker, Khet-Talavdi, 7/12, Tiles, 3D Map) to open directly",
    val allFeaturesSearchHint: String = "Search tool (Mandi, Labour, Broker, Khet-Talavdi, 7/12, Tiles, 3D...)"
)

object AppLocalizationEngine {

    // 108+ Google Supported Indian & Global Languages
    val allLanguages: List<AppLanguage> = listOf(
        // Primary Default & Indian Languages
        AppLanguage("en", "English", "English", "Global & India"),
        AppLanguage("gu", "Gujarati", "ગુજરાતી", "India"),
        AppLanguage("hi", "Hindi", "हिन्दी", "India"),
        AppLanguage("mr", "Marathi", "मराठी", "India"),
        AppLanguage("ta", "Tamil", "தமிழ்", "India"),
        AppLanguage("te", "Telugu", "తెలుగు", "India"),
        AppLanguage("kn", "Kannada", "ಕನ್ನಡ", "India"),
        AppLanguage("ml", "Malayalam", "മലയാളം", "India"),
        AppLanguage("bn", "Bengali", "বাংলা", "India"),
        AppLanguage("pa", "Punjabi", "ਪੰਜਾਬੀ", "India"),
        AppLanguage("or", "Odia", "ଓଡ଼ିଆ", "India"),
        AppLanguage("as", "Assamese", "অসমীয়া", "India"),
        AppLanguage("sa", "Sanskrit", "संस्कृतम्", "India"),
        AppLanguage("ur", "Urdu", "اردو", "India & South Asia", isRtl = true),
        AppLanguage("ne", "Nepali", "नेपाली", "South Asia"),
        AppLanguage("si", "Sinhala", "සිංහල", "South Asia"),
        AppLanguage("sd", "Sindhi", "سنڌي", "India & South Asia", isRtl = true),
        AppLanguage("kok", "Konkani", "कोंकणी", "India"),
        AppLanguage("mai", "Maithili", "मैथिली", "India"),
        AppLanguage("doi", "Dogri", "डोगरी", "India"),
        AppLanguage("mni", "Manipuri (Meitei)", "মৈতৈলোন্", "India"),
        AppLanguage("brx", "Bodo", "बर'", "India"),
        AppLanguage("sat", "Santali", "ᱥᱟᱱᱛᱟᱲᱤ", "India"),
        AppLanguage("ks", "Kashmiri", "कॉशुर / کٲشُر", "India"),

        // Major Asian & East Asian Languages
        AppLanguage("ja", "Japanese", "日本語", "Asia-Pacific"),
        AppLanguage("ko", "Korean", "한국어", "Asia-Pacific"),
        AppLanguage("zh-CN", "Chinese (Simplified)", "简体中文", "Asia-Pacific"),
        AppLanguage("zh-TW", "Chinese (Traditional)", "繁體中文", "Asia-Pacific"),
        AppLanguage("id", "Indonesian", "Bahasa Indonesia", "Asia-Pacific"),
        AppLanguage("ms", "Malay", "Bahasa Melayu", "Asia-Pacific"),
        AppLanguage("th", "Thai", "ไทย", "Asia-Pacific"),
        AppLanguage("vi", "Vietnamese", "Tiếng Việt", "Asia-Pacific"),
        AppLanguage("tl", "Filipino (Tagalog)", "Filipino", "Asia-Pacific"),
        AppLanguage("my", "Burmese (Myanmar)", "မြန်မာစာ", "Asia-Pacific"),
        AppLanguage("km", "Khmer", "ភាសាខ្មែរ", "Asia-Pacific"),
        AppLanguage("lo", "Lao", "ລາວ", "Asia-Pacific"),
        AppLanguage("mn", "Mongolian", "Монгол", "Asia-Pacific"),
        AppLanguage("jv", "Javanese", "Basa Jawa", "Asia-Pacific"),
        AppLanguage("su", "Sundanese", "Basa Sunda", "Asia-Pacific"),
        AppLanguage("ceb", "Cebuano", "Binisaya", "Asia-Pacific"),
        AppLanguage("hmv", "Hmong", "Hmoob", "Asia-Pacific"),
        AppLanguage("mi", "Maori", "Te Reo Māori", "Asia-Pacific"),
        AppLanguage("sm", "Samoan", "Gagana Sāmoa", "Asia-Pacific"),
        AppLanguage("haw", "Hawaiian", "ʻŌlelo Hawaiʻi", "Asia-Pacific"),

        // Major European & Americas Languages
        AppLanguage("es", "Spanish", "Español", "Europe & Americas"),
        AppLanguage("fr", "French", "Français", "Europe & Americas"),
        AppLanguage("de", "German", "Deutsch", "Europe"),
        AppLanguage("ru", "Russian", "Русский", "Europe & Eurasia"),
        AppLanguage("pt", "Portuguese", "Português", "Europe & Americas"),
        AppLanguage("it", "Italian", "Italiano", "Europe"),
        AppLanguage("nl", "Dutch", "Nederlands", "Europe"),
        AppLanguage("pl", "Polish", "Polski", "Europe"),
        AppLanguage("uk", "Ukrainian", "Українська", "Europe"),
        AppLanguage("el", "Greek", "Ελληνικά", "Europe"),
        AppLanguage("cs", "Czech", "Čeština", "Europe"),
        AppLanguage("sv", "Swedish", "Svenska", "Europe"),
        AppLanguage("da", "Danish", "Dansk", "Europe"),
        AppLanguage("fi", "Finnish", "Suomi", "Europe"),
        AppLanguage("no", "Norwegian", "Norsk", "Europe"),
        AppLanguage("hu", "Hungarian", "Magyar", "Europe"),
        AppLanguage("ro", "Romanian", "Română", "Europe"),
        AppLanguage("bg", "Bulgarian", "Български", "Europe"),
        AppLanguage("sr", "Serbian", "Српски", "Europe"),
        AppLanguage("hr", "Croatian", "Hrvatski", "Europe"),
        AppLanguage("sk", "Slovak", "Slovenčina", "Europe"),
        AppLanguage("sl", "Slovenian", "Slovenščina", "Europe"),
        AppLanguage("lt", "Lithuanian", "Lietuvių", "Europe"),
        AppLanguage("lv", "Latvian", "Latviešu", "Europe"),
        AppLanguage("et", "Estonian", "Eesti", "Europe"),
        AppLanguage("sq", "Albanian", "Shqip", "Europe"),
        AppLanguage("eu", "Basque", "Euskara", "Europe"),
        AppLanguage("be", "Belarusian", "Беларуская", "Europe"),
        AppLanguage("bs", "Bosnian", "Bosanski", "Europe"),
        AppLanguage("ca", "Catalan", "Català", "Europe"),
        AppLanguage("co", "Corsican", "Corsu", "Europe"),
        AppLanguage("eo", "Esperanto", "Esperanto", "Global"),
        AppLanguage("fy", "Frisian", "Frysk", "Europe"),
        AppLanguage("gl", "Galician", "Galego", "Europe"),
        AppLanguage("is", "Icelandic", "Íslenska", "Europe"),
        AppLanguage("ga", "Irish", "Gaeilge", "Europe"),
        AppLanguage("la", "Latin", "Latina", "Classical"),
        AppLanguage("lb", "Luxembourgish", "Lëtzebuergesch", "Europe"),
        AppLanguage("mk", "Macedonian", "Македонски", "Europe"),
        AppLanguage("mt", "Maltese", "Malti", "Europe"),
        AppLanguage("gd", "Scots Gaelic", "Gàidhlig", "Europe"),
        AppLanguage("cy", "Welsh", "Cymraeg", "Europe"),
        AppLanguage("yi", "Yiddish", "ייִדיש", "Europe", isRtl = true),
        AppLanguage("ht", "Haitian Creole", "Kreyòl Ayisyen", "Americas"),

        // Middle East, Central Asia & Caucasus
        AppLanguage("ar", "Arabic", "العربية", "Middle East & Africa", isRtl = true),
        AppLanguage("tr", "Turkish", "Türkçe", "Middle East & Eurasia"),
        AppLanguage("fa", "Persian (Farsi)", "فارسی", "Middle East", isRtl = true),
        AppLanguage("he", "Hebrew", "עברית", "Middle East", isRtl = true),
        AppLanguage("ps", "Pashto", "پښتو", "Central & South Asia", isRtl = true),
        AppLanguage("ku", "Kurdish", "Kurdî", "Middle East"),
        AppLanguage("ka", "Georgian", "ქართული", "Caucasus"),
        AppLanguage("hy", "Armenian", "Հայերեն", "Caucasus"),
        AppLanguage("az", "Azerbaijani", "Azərbaycan", "Caucasus"),
        AppLanguage("kk", "Kazakh", "Қазақ тілі", "Central Asia"),
        AppLanguage("uz", "Uzbek", "Oʻzbekcha", "Central Asia"),
        AppLanguage("ky", "Kyrgyz", "Кыргызча", "Central Asia"),
        AppLanguage("tg", "Tajik", "Тоҷикӣ", "Central Asia"),
        AppLanguage("tk", "Turkmen", "Türkmençe", "Central Asia"),
        AppLanguage("tt", "Tatar", "Татарча", "Eurasia"),
        AppLanguage("ug", "Uyghur", "ئۇيغۇرچە", "Central Asia", isRtl = true),

        // African Languages
        AppLanguage("sw", "Swahili", "Kiswahili", "Africa"),
        AppLanguage("am", "Amharic", "አማርኛ", "Africa"),
        AppLanguage("ha", "Hausa", "Hausa", "Africa"),
        AppLanguage("yo", "Yoruba", "Èdè Yorùbá", "Africa"),
        AppLanguage("ig", "Igbo", "Ásụ̀sụ́ Ìgbò", "Africa"),
        AppLanguage("zu", "Zulu", "isiZulu", "Africa"),
        AppLanguage("xh", "Xhosa", "isiXhosa", "Africa"),
        AppLanguage("af", "Afrikaans", "Afrikaans", "Africa"),
        AppLanguage("so", "Somali", "Soomaali", "Africa"),
        AppLanguage("mg", "Malagasy", "Malagasy", "Africa"),
        AppLanguage("ny", "Chichewa", "Chichewa", "Africa"),
        AppLanguage("st", "Sesotho", "Sesotho", "Africa"),
        AppLanguage("sn", "Shona", "ChiShona", "Africa")
    )

    val englishDefault = LocalizedUiStrings(
        appTitle = "Khetrova",
        appSubtitle = "Global & Regional Land Survey, Daily Rojmel, 3D Map & Construction BOQ",
        zeroPermissionBadge = "100% Offline • Zero Permissions • Zero Cloud Tracking",
        navLandUnits = "Land Units",
        navPlotCad = "Plot CAD",
        navMaterials = "Materials",
        navVaultCode = "Daily & Vault",
        selectStateStandard = "1. Select Country / State / Province Land Standard",
        chooseUnitAndArea = "2. Choose Input Unit & Enter Land Area",
        areaLabel = "Area in",
        rateLabel = "Rate ₹ /",
        instantEquivalents = "INSTANT REGIONAL & METRIC EQUIVALENTS",
        landValuationTitle = "Land Valuation & Statutory Stamp Duty Estimate",
        basePlotValue = "Base Plot Value:",
        estStampDuty = "Est. Stamp Duty & Reg",
        totalLandCost = "Total Land Acquisition Cost:",
        saveToVault = "Save to Vault",
        exportPdf = "Export PDF",
        liveCadBlueprint = "LIVE 2D CAD SURVEY BLUEPRINT (SCALED)",
        triangulatedArea = "Triangulated Area",
        perimeter = "Perimeter",
        contractorErrorDetector = "Contractor 'Average Formula' Error Detector",
        enterFourSides = "Enter 4 Boundary Sides & Diagonal (Feet)",
        northSide = "North Side (A) ft",
        southSide = "South Side (C) ft",
        eastSide = "East Side (B) ft",
        westSide = "West Side (D) ft",
        diagonalSide = "Diagonal (NW-SE) ft",
        autoCalibrateDiagonal = "Auto-Calibrate Diagonal",
        selectCivilCategory = "1. Select Civil / Mason Work Category",
        enterRoomDimensions = "2. Enter Room / Wall Dimensions & Spec",
        lengthFeet = "Length (Feet)",
        widthOrHeightFeet = "Width / Height (Feet)",
        billOfQuantities = "BILL OF QUANTITIES (BOQ)",
        totalMaterialCost = "Total Est. Material Cost",
        savedVaultTab = "Saved Vault (Offline DB)",
        polyglotTab = "7/12 & Water Tank",
        legalTab = "Legal & Privacy (13 Policies)",
        languagePickerTitle = "Select App Language (115+ Google Languages)",
        searchLanguageHint = "Search language (e.g. English, ગુજરાતી, हिन्दी, 日本語, தமிழ்)...",
        navMap3D = "Map & 3D",
        menuAllToolsBtn = "Menu • All Tools",
        allFeaturesRibbonBtn = "All Features (24+)",
        dailyRojmelTab = "Daily Rojmel, Mandi & Broker",
        allFeaturesMenuTitle = "All Features & Pages Menu",
        allFeaturesMenuSubtitle = "Tap any tool (Daily Mandi, Labour, Broker, Khet-Talavdi, 7/12, Tiles, 3D Map) to open directly",
        allFeaturesSearchHint = "Search tool (Mandi, Labour, Broker, Khet-Talavdi, 7/12, Tiles, 3D...)"
    )

    fun getStringsForLanguage(langCode: String): LocalizedUiStrings {
        return when (langCode) {
            "en" -> englishDefault
            "gu" -> LocalizedUiStrings(
                appTitle = "Khetrova (જમીન, રોજમેળ અને બાંધકામ)",
                appSubtitle = "દેશ-વિદેશના રાજ્યો મુજબ જમીન માપણી, રોજિંદી મંડી-મજૂરી-દલાલી અને બાંધકામ BOQ",
                zeroPermissionBadge = "૧૦૦% ઓફલાઇન • ઝીરો પરમિશન • સંપૂર્ણ સુરક્ષિત",
                navLandUnits = "જમીન માપ",
                navPlotCad = "પ્લોટ નકશો",
                navMaterials = "બાંધકામ",
                navVaultCode = "રોજમેળ અને વોલ્ટ",
                selectStateStandard = "૧. દેશ / રાજ્ય મુજબ જમીન માપણીનું ધોરણ પસંદ કરો",
                chooseUnitAndArea = "૨. માપનો એકમ પસંદ કરો અને ક્ષેત્રફળ લખો",
                areaLabel = "જમીનનું માપ",
                rateLabel = "ભાવ ₹ /",
                instantEquivalents = "તમામ પ્રાદેશિક અને આંતરરાષ્ટ્રીય એકમોમાં રૂપાંતરણ",
                landValuationTitle = "જમીનનું મૂલ્યાંકન અને સ્ટેમ્પ ડ્યુટી અંદાજ",
                basePlotValue = "જમીનની મૂળ કિંમત:",
                estStampDuty = "અંદાજિત સ્ટેમ્પ ડ્યુટી અને રજીસ્ટ્રેશન",
                totalLandCost = "જમીનની કુલ અંદાજિત કિંમત:",
                saveToVault = "વોલ્ટમાં સેવ કરો",
                exportPdf = "PDF રિપોર્ટ કાઢો",
                liveCadBlueprint = "લાઇવ 2D CAD પ્લોટ બ્લુપ્રિન્ટ નકશો (સ્કેલ મુજબ)",
                triangulatedArea = "સાચું ત્રિકોણમિતિ ક્ષેત્રફળ",
                perimeter = "પરિમિતિ",
                contractorErrorDetector = "કોન્ટ્રાક્ટરની 'સરેરાશ ફોર્મ્યુલા' ની ભૂલ પકડનાર",
                enterFourSides = "પ્લોટની ચારેય બાજુ અને કર્ણ (ફૂટમાં) લખો",
                northSide = "ઉત્તર બાજુ (A) ફૂટ",
                southSide = "દક્ષિણ બાજુ (C) ફૂટ",
                eastSide = "પૂર્વ બાજુ (B) ફૂટ",
                westSide = "પશ્ચિમ બાજુ (D) ફૂટ",
                diagonalSide = "કર્ણ (Diagonal) ફૂટ",
                autoCalibrateDiagonal = "કર્ણ ઓટો-કેલિબ્રેટ કરો",
                selectCivilCategory = "૧. બાંધકામ / કારીગરીનો પ્રકાર પસંદ કરો",
                enterRoomDimensions = "૨. રૂમ કે દિવાલની લંબાઈ-પહોળાઈ લખો",
                lengthFeet = "લંબાઈ (ફૂટ)",
                widthOrHeightFeet = "પહોળાઈ / ઊંચાઈ (ફૂટ)",
                billOfQuantities = "બાંધકામ સામગ્રીનું લિસ્ટ (BOQ)",
                totalMaterialCost = "કુલ અંદાજિત મટીરીયલ ખર્ચ",
                savedVaultTab = "સેવ કરેલા રિપોર્ટ (ઓફલાઇન)",
                polyglotTab = "૭/૧૨ ઉતારા અને ખેત-તળાવડી",
                legalTab = "કાયદાકીય અને પ્રાઇવસી (૧૩ પોલિસી)",
                languagePickerTitle = "એપની ભાષા પસંદ કરો (૧૧૫+ ભાષાઓ)",
                searchLanguageHint = "ભાષા શોધો (દા.ત. ગુજરાતી, हिन्दी, English, 日本語)...",
                navMap3D = "નકશો અને 3D",
                menuAllToolsBtn = "મેનુ • બધા સાધનો",
                allFeaturesRibbonBtn = "બધા ફીચર્સ (૨૪+)",
                dailyRojmelTab = "રોજિંદી મંડી, મજૂરી અને દલાલી",
                allFeaturesMenuTitle = "તમામ ફીચર્સ અને પેજનું મેનુ (All Tools)",
                allFeaturesMenuSubtitle = "કોઈ પણ સાધન (મંડી બિલ, મજૂરી-ઉપાડ, જમીન દલાલી, ખેત-તળાવડી, ૭/૧૨, ટાઇલ્સ, 3D મેપ) સીધું ખોલવા ક્લિક કરો",
                allFeaturesSearchHint = "સાધન શોધો (મંડી, મજૂરી, દલાલી, ખેત-તળાવડી, ૭/૧૨, ટાઇલ્સ, 3D)..."
            )
            "hi", "mai", "doi", "kok", "brx", "ne" -> LocalizedUiStrings(
                appTitle = "Khetrova (भूमि, रोज़मेल एवं निर्माण गणना)",
                appSubtitle = "देश-विदेश के राज्यों अनुसार भूमि मापन, दैनिक मंडी-मज़दूरी-दलाली व निर्माण BOQ",
                zeroPermissionBadge = "100% ऑफ़लाइन • ज़ीरो परमिशन • पूर्णतः सुरक्षित",
                navLandUnits = "भूमि माप",
                navPlotCad = "प्लॉट CAD",
                navMaterials = "निर्माण सामग्री",
                navVaultCode = "रोज़मेल व वॉल्ट",
                selectStateStandard = "1. देश / राज्य के अनुसार भूमि मापन मानक चुनें",
                chooseUnitAndArea = "2. माप की इकाई चुनें और क्षेत्रफल दर्ज करें",
                areaLabel = "क्षेत्रफल",
                rateLabel = "दर ₹ /",
                instantEquivalents = "तत्काल क्षेत्रीय और मीट्रिक रूपांतरण",
                landValuationTitle = "भूमि मूल्यांकन और स्टाम्प ड्यूटी अनुमान",
                basePlotValue = "मूल प्लॉट कीमत:",
                estStampDuty = "अनुमानित स्टाम्प ड्यूटी व रजिस्ट्रेशन",
                totalLandCost = "कुल भूमि अधिग्रहण लागत:",
                saveToVault = "वॉल्ट में सेव करें",
                exportPdf = "PDF रिपोर्ट निकालें",
                liveCadBlueprint = "लाइव 2D CAD सर्वेक्षण ब्लूप्रिंट (स्केल अनुसार)",
                triangulatedArea = "सटीक त्रिभुज क्षेत्रफल",
                perimeter = "परिमाप",
                contractorErrorDetector = "ठेकेदार 'औसत फ़ॉर्मूला' त्रुटि जांचकर्ता",
                enterFourSides = "प्लॉट की चारों भुजाएं और विकर्ण (फीट में) दर्ज करें",
                northSide = "उत्तर भुजा (A) फीट",
                southSide = "दक्षिण भुजा (C) फीट",
                eastSide = "पूर्व भुजा (B) फीट",
                westSide = "पश्चिम भुजा (D) फीट",
                diagonalSide = "विकर्ण (Diagonal) फीट",
                autoCalibrateDiagonal = "विकर्ण ऑटो-कैलिब्रेट करें",
                selectCivilCategory = "1. सिविल / निर्माण कार्य श्रेणी चुनें",
                enterRoomDimensions = "2. कमरे / दीवार की लंबाई-चौड़ाई दर्ज करें",
                lengthFeet = "लंबाई (फीट)",
                widthOrHeightFeet = "चौड़ाई / ऊंचाई (फीट)",
                billOfQuantities = "निर्माण सामग्री सूची (BOQ)",
                totalMaterialCost = "कुल अनुमानित सामग्री लागत",
                savedVaultTab = "सेव किए गए रिपोर्ट (ऑफ़लाइन)",
                polyglotTab = "7/12 उतारा व खेत-तालाब",
                legalTab = "कानूनी और गोपनीयता (13 नीतियां)",
                languagePickerTitle = "ऐप की भाषा चुनें (115+ भाषाएं)",
                searchLanguageHint = "भाषा खोजें (जैसे ગુજરાતી, हिन्दी, 日本語, தமிழ்)...",
                navMap3D = "नक्शा व 3D",
                menuAllToolsBtn = "मेनू • सभी टूल्स",
                allFeaturesRibbonBtn = "सभी फ़ीचर्स (24+)",
                dailyRojmelTab = "दैनिक मंडी, मज़दूरी व दलाली",
                allFeaturesMenuTitle = "सभी फ़ीचर्स और पेज मेनू",
                allFeaturesMenuSubtitle = "किसी भी टूल (मंडी बिल, मज़दूरी, ज़मीन दलाली, खेत-तालाब, 7/12, टाइल्स, 3D नक्शा) को सीधे खोलें",
                allFeaturesSearchHint = "टूल खोजें (मंडी, मज़दूरी, दलाली, खेत-तालाब, 7/12, टाइल्स)..."
            )
            "mr" -> LocalizedUiStrings(
                appTitle = "Khetrova (जमीन व बांधकाम मोजणी)",
                appSubtitle = "भारतीय प्रादेशिक जमीन मोजणी, 2D CAD नकाशा आणि बांधकाम साहित्य BOQ",
                zeroPermissionBadge = "१००% ऑफलाइन • झिरो परमिशन • झिरो क्लाउड ट्रॅकिंग",
                navLandUnits = "जमीन एकक",
                navPlotCad = "प्लॉट नकाशा",
                navMaterials = "बांधकाम साहित्य",
                navVaultCode = "व्हॉल्ट व साधने",
                selectStateStandard = "१. राज्यानुसार जमीन मोजणीचे प्रमाण निवडा",
                chooseUnitAndArea = "२. मोजणीचे एकक निवडा आणि क्षेत्रफळ टाका",
                areaLabel = "क्षेत्रफळ",
                rateLabel = "दर ₹ /",
                instantEquivalents = "तात्काळ प्रादेशिक आणि मेट्रिक रूपांतरण",
                landValuationTitle = "जमीन मूल्यांकन आणि मुद्रांक शुल्क (Stamp Duty) अंदाज",
                basePlotValue = "मूळ प्लॉट किंमत:",
                estStampDuty = "अंदाजित स्टॅम्प ड्युटी व नोंदणी",
                totalLandCost = "एकूण जमीन खरेदी खर्च:",
                saveToVault = "व्हॉल्टमध्ये सेव्ह करा",
                exportPdf = "PDF रिपोर्ट काढा",
                liveCadBlueprint = "लाइव्ह 2D CAD प्लॉट ब्लूप्रिंट नकाशा",
                triangulatedArea = "अचूक त्रिकोणमिती क्षेत्रफळ",
                perimeter = "परिमिती",
                contractorErrorDetector = "कंत्राटदार 'सरासरी सूत्र' त्रुटी तपासक",
                enterFourSides = "प्लॉटच्या चारही बाजू आणि कर्ण (फूटमध्ये) टाका",
                northSide = "उत्तर बाजू (A) फूट",
                southSide = "दक्षिण बाजू (C) फूट",
                eastSide = "पूर्व बाजू (B) फूट",
                westSide = "पश्चिम बाजू (D) फूट",
                diagonalSide = "कर्ण (Diagonal) फूट",
                autoCalibrateDiagonal = "कर्ण ऑटो-कॅलिब्रेट करा",
                selectCivilCategory = "१. बांधकाम / गवंडी कामाचा प्रकार निवडा",
                enterRoomDimensions = "२. खोली किंवा भिंतीची लांबी-रुंदी टाका",
                lengthFeet = "लांबी (फूट)",
                widthOrHeightFeet = "रुंदी / उंची (फूट)",
                billOfQuantities = "बांधकाम साहित्य यादी (BOQ)",
                totalMaterialCost = "एकूण अंदाजित साहित्य खर्च",
                savedVaultTab = "सेव्ह केलेले रिपोर्ट (ऑफलाइन)",
                polyglotTab = "७/१२ उतारा व शेतकरी साधने",
                legalTab = "कायदेशीर व गोपनीयता (१३ धोरणे)",
                languagePickerTitle = "ॲपची भाषा निवडा (११५+ भाषा)",
                searchLanguageHint = "भाषा शोधा..."
            )
            "ta" -> LocalizedUiStrings(
                appTitle = "Khetrova (நிலம் & கட்டுமான கணக்கீடு)",
                appSubtitle = "இந்திய மாநில நில அளவை, 2D CAD வரைபடம் & கட்டுமான பொருட்கள் BOQ",
                zeroPermissionBadge = "100% ஆஃப்லைன் • அனுமதி தேவையில்லை • பாதுகாப்பானது",
                navLandUnits = "நில அளவை",
                navPlotCad = "வரைபடம் CAD",
                navMaterials = "கட்டுமானம்",
                navVaultCode = "சேமிப்பு & கோட்",
                selectStateStandard = "1. மாநில நில அளவை முறையைத் தேர்ந்தெடுக்கவும்",
                chooseUnitAndArea = "2. அலகு மற்றும் நிலப்பரப்பை உள்ளிடவும்",
                areaLabel = "பரப்பளவு",
                rateLabel = "விலை ₹ /",
                instantEquivalents = "உடனடி நில அளவை மாற்றங்கள்",
                landValuationTitle = "நில மதிப்பீடு & முத்திரைத்தாள் கட்டணம்",
                basePlotValue = "அடிப்படை நில மதிப்பு:",
                estStampDuty = "முத்திரைத்தாள் மற்றும் பதிவு கட்டணம்",
                totalLandCost = "மொத்த நில செலவு:",
                saveToVault = "சேமிக்கவும்",
                exportPdf = "PDF பதிவிறக்கம்",
                liveCadBlueprint = "நேரடி 2D CAD நில வரைபடம்",
                triangulatedArea = "துல்லியமான பரப்பளவு",
                perimeter = "சுற்றளவு",
                contractorErrorDetector = "சராசரி சூத்திர பிழை கண்டறிதல்",
                enterFourSides = "நான்கு பக்க அளவுகள் & மூலைவிட்டம் (அடி)",
                northSide = "வடக்கு பக்கம் (A) அடி",
                southSide = "தெற்கு பக்கம் (C) அடி",
                eastSide = "கிழக்கு பக்கம் (B) அடி",
                westSide = "மேற்கு பக்கம் (D) அடி",
                diagonalSide = "மூலைவிட்டம் (Diagonal) அடி",
                autoCalibrateDiagonal = "மூலைவிட்டத்தை தானாக கணக்கிடு",
                selectCivilCategory = "1. கட்டுமானப் பணி வகையைத் தேர்ந்தெடுக்கவும்",
                enterRoomDimensions = "2. அறை / சுவர் நீளம் மற்றும் அகலம்",
                lengthFeet = "நீளம் (அடி)",
                widthOrHeightFeet = "அகலம் / உயரம் (அடி)",
                billOfQuantities = "கட்டுமானப் பொருட்கள் பட்டியல் (BOQ)",
                totalMaterialCost = "மொத்த பொருட்கள் செலவு",
                savedVaultTab = "சேமிக்கப்பட்டவை (ஆஃப்லைன்)",
                polyglotTab = "பன்மொழி எஞ்சின் (7 மொழிகள்)",
                legalTab = "சட்டம் & தனியுரிமை (13 கொள்கைகள்)",
                languagePickerTitle = "மொழியைத் தேர்ந்தெடுக்கவும் (115+ மொழிகள்)",
                searchLanguageHint = "மொழியைத் தேடுங்கள்..."
            )
            "te" -> LocalizedUiStrings(
                appTitle = "Khetrova (భూమి & నిర్మాణ గణన)",
                appSubtitle = "భారతీయ ప్రాంతీయ భూమి కొలత, 2D CAD ప్లాట్ & నిర్మాణ సామగ్రి BOQ",
                zeroPermissionBadge = "100% ఆఫ్‌లైన్ • జీరో పర్మిషన్ • పూర్తిగా సురక్షితం",
                navLandUnits = "భూమి కొలతలు",
                navPlotCad = "ప్లాట్ CAD",
                navMaterials = "నిర్మాణ సామగ్రి",
                navVaultCode = "వాల్ట్ & కోడ్",
                selectStateStandard = "1. రాష్ట్ర భూమి కొలత ప్రమాణాన్ని ఎంచుకోండి",
                chooseUnitAndArea = "2. యూనిట్ ఎంచుకోండి & విస్తీర్ణం నమోదు చేయండి",
                areaLabel = "విస్తీర్ణం",
                rateLabel = "ధర ₹ /",
                instantEquivalents = "తక్షణ ప్రాంతీయ & మెట్రిక్ మార్పిడులు",
                landValuationTitle = "భూమి విలువ & స్టాంప్ డ్యూటీ అంచనా",
                basePlotValue = "ప్రాథమిక భూమి విలువ:",
                estStampDuty = "స్టాంప్ డ్యూటీ & రిజిస్ట్రేషన్",
                totalLandCost = "మొత్తం భూమి కొనుగోలు ఖర్చు:",
                saveToVault = "వాల్ట్‌లో సేవ్ చేయండి",
                exportPdf = "PDF రిపోర్ట్",
                liveCadBlueprint = "లైవ్ 2D CAD సర్వే బ్లూప్రింట్",
                triangulatedArea = "ఖచ్చితమైన వైశాల్యం",
                perimeter = "చుట్టుకొలత",
                contractorErrorDetector = "సగటు సూత్రం లోపం గుర్తింపు",
                enterFourSides = "4 భుజాలు & కర్ణం (అడుగులలో) నమోదు చేయండి",
                northSide = "ఉత్తరం (A) అడుగులు",
                southSide = "దక్షిణం (C) అడుగులు",
                eastSide = "తూర్పు (B) అడుగులు",
                westSide = "పడమర (D) అడుగులు",
                diagonalSide = "కర్ణం (Diagonal) అడుగులు",
                autoCalibrateDiagonal = "కర్ణం ఆటో-క్యాలిబ్రేట్",
                selectCivilCategory = "1. నిర్మాణ పని విభాగాన్ని ఎంచుకోండి",
                enterRoomDimensions = "2. గది / గోడ కొలతలు నమోదు చేయండి",
                lengthFeet = "పొడవు (అడుగులు)",
                widthOrHeightFeet = "వెడల్పు / ఎత్తు (అడుగులు)",
                billOfQuantities = "నిర్మాణ సామగ్రి జాబితా (BOQ)",
                totalMaterialCost = "మొత్తం సామగ్రి ఖర్చు",
                savedVaultTab = "సేవ్ చేసినవి (ఆఫ్‌లైన్)",
                polyglotTab = "పాలీగ్లాట్ ఇంజిన్ (7 భాషలు)",
                legalTab = "చట్టపరమైన & గోప్యత (13 విధానాలు)",
                languagePickerTitle = "యాప్ భాషను ఎంచుకోండి (115+ భాషలు)",
                searchLanguageHint = "భాషను వెతకండి..."
            )
            "kn" -> LocalizedUiStrings(
                appTitle = "Khetrova (ಭೂమి ಮತ್ತು ನಿರ್ಮಾಣ ಲೆಕ್ಕಾಚಾರ)",
                appSubtitle = "ಭಾರತೀಯ ಪ್ರಾದೇಶಿಕ ಭೂ ಮಾಪನ, 2D CAD ನಕ್ಷೆ ಮತ್ತು ನಿರ್ಮಾಣ BOQ",
                zeroPermissionBadge = "100% ಆಫ್‌ಲೈನ್ • ಶೂನ್ಯ ಅನುಮತಿ • ಸಂಪೂರ್ಣ സുരക്ഷಿತ",
                navLandUnits = "ಭೂ ಮಾಪನ",
                navPlotCad = "ಪ್ಲಾಟ್ CAD",
                navMaterials = "ನಿರ್ಮಾಣ ಸಾಮಗ್ರಿ",
                navVaultCode = "ವಾಲ್ಟ್ & ಕೋಡ್",
                selectStateStandard = "1. ರಾಜ್ಯದ ಭೂ ಮಾಪನ ಮಾನದಂಡವನ್ನು ಆಯ್ಕೆಮಾಡಿ",
                chooseUnitAndArea = "2. ಘಟಕವನ್ನು ಆಯ್ಕೆಮಾಡಿ ಮತ್ತು ವಿಸ್ತೀರ್ಣ ನಮೂದಿಸಿ",
                areaLabel = "ವಿಸ್ತೀರ್ಣ",
                rateLabel = "ದರ ₹ /",
                instantEquivalents = "ತಕ್ಷಣದ ಪ್ರಾದೇಶಿಕ ಮತ್ತು ಮೆಟ್ರಿಕ್ ಪರಿವರ್ತನೆ",
                landValuationTitle = "ಭೂ ಮೌಲ್ಯಮಾಪನ ಮತ್ತು ಮುದ್ರಾಂಕ ಶುಲ್ಕ ಅಂದಾಜು",
                basePlotValue = "ಮೂಲ ಭೂ ಮೌಲ್ಯ:",
                estStampDuty = "ಅಂದಾಜು ಸ್ಟ್ಯಾಂಪ್ ಡ್ಯೂಟಿ",
                totalLandCost = "ಒಟ್ಟು ಭೂ ವೆಚ್ಚ:",
                saveToVault = "ಉಳಿಸಿ (Save)",
                exportPdf = "PDF ರಫ್ತು ಮಾಡಿ",
                liveCadBlueprint = "ಲೈವ್ 2D CAD ಸರ್ವೆ ಬ್ಲೂಪ್ರಿಂಟ್",
                triangulatedArea = "ನಿಖರವಾದ ವಿಸ್ತೀರ್ಣ",
                perimeter = "ಸುತ್ತಳತೆ",
                contractorErrorDetector = "ಸರಾಸರಿ ಸೂತ್ರ ದೋಷ ಪತ್ತೆಕಾರಕ",
                enterFourSides = "4 ಬದಿಗಳು ಮತ್ತು ಕರ್ಣವನ್ನು (ಅಡಿಗಳಲ್ಲಿ) ನಮೂದಿಸಿ",
                northSide = "ಉತ್ತರ (A) ಅಡಿ",
                southSide = "ದಕ್ಷಿಣ (C) ಅಡಿ",
                eastSide = "ಪೂರ್ವ (B) ಅಡಿ",
                westSide = "ಪಶ್ಚಿಮ (D) ಅಡಿ",
                diagonalSide = "ಕರ್ಣ (Diagonal) ಅಡಿ",
                autoCalibrateDiagonal = "ಕರ್ಣ ಆಟೋ-ಕ್ಯಾಲಿಬ್ರೇಟ್",
                selectCivilCategory = "1. ನಿರ್ಮಾಣ ಕಾರ್ಯ ವಿಭಾಗವನ್ನು ಆಯ್ಕೆಮಾಡಿ",
                enterRoomDimensions = "2. ಕೊಠಡಿ / ಗೋಡೆಯ ಅಳತೆಗಳನ್ನು ನಮೂದಿಸಿ",
                lengthFeet = "ಉದ್ದ (ಅಡಿ)",
                widthOrHeightFeet = "ಅಗಲ / ಎತ್ತರ (ಅಡಿ)",
                billOfQuantities = "ನಿರ್ಮಾಣ ಸಾಮಗ್ರಿ ಪಟ್ಟಿ (BOQ)",
                totalMaterialCost = "ಒಟ್ಟು ಸಾಮಗ್ರಿ ವೆಚ್ಚ",
                savedVaultTab = "ಉಳಿಸಿದ ವರದಿಗಳು",
                polyglotTab = "ಪಾಲಿಗ್ಲಾಟ್ ಎಂಜಿನ್ (7 ಭಾಷೆಗಳು)",
                legalTab = "ಕಾನೂನು ಮತ್ತು ಗೌಪ್ಯತೆ (13 ನೀತಿಗಳು)",
                languagePickerTitle = "ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ (115+ ಭಾಷೆಗಳು)",
                searchLanguageHint = "ಭಾಷೆ ಹುಡುಕಿ..."
            )
            "ml" -> LocalizedUiStrings(
                appTitle = "Khetrova (ഭൂമി & നിർമ്മാണ കണക്കുകൾ)",
                appSubtitle = "ഇന്ത്യൻ പ്രാദേശിക ഭൂമി അളവ്, 2D CAD പ്ലോട്ട് & നിർമ്മാണ BOQ",
                zeroPermissionBadge = "100% ഓഫ്‌ലൈൻ • സീറോ പെർമിഷൻ • സുരക്ഷിതം",
                navLandUnits = "ഭൂമി അളവ്",
                navPlotCad = "പ്ലോട്ട് CAD",
                navMaterials = "നിർമ്മാണ സാമഗ്രികൾ",
                navVaultCode = "വോൾട്ട് & കോഡ്",
                selectStateStandard = "1. സംസ്ഥാന ഭൂമി അളവ് രീതി തിരഞ്ഞെടുക്കുക",
                chooseUnitAndArea = "2. യൂണിറ്റും വിസ്തീർണ്ണവും നൽകുക",
                areaLabel = "വിസ്തീർണ്ണം",
                rateLabel = "വില ₹ /",
                instantEquivalents = "തൽക്ഷണ പ്രാദേശിക & മെട്രിക് മാറ്റങ്ങൾ",
                landValuationTitle = "ഭൂമി മൂല്യനിർണ്ണയവും സ്റ്റാമ്പ് ഡ്യൂട്ടിയും",
                basePlotValue = "അടിസ്ഥാന വില:",
                estStampDuty = "സ്റ്റാമ്പ് ഡ്യൂട്ടി & രജിസ്ട്രേഷൻ",
                totalLandCost = "ആകെ ഭൂമി ചെലവ്:",
                saveToVault = "സേവ് ചെയ്യുക",
                exportPdf = "PDF റിപ്പോർട്ട്",
                liveCadBlueprint = "ലൈവ് 2D CAD സർവേ ബ്ലൂപ്രിന്റ്",
                triangulatedArea = "കൃത്യമായ വിസ്തീർണ്ണം",
                perimeter = "ചുറ്റളവ്",
                contractorErrorDetector = "ശരാശരി ഫോർമുല പിശക് പരിശോധന",
                enterFourSides = "4 വശങ്ങളും വികർണ്ണവും (അടിയിൽ) നൽകുക",
                northSide = "വടക്ക് (A) അടി",
                southSide = "തെക്ക് (C) അടി",
                eastSide = "കിഴക്ക് (B) അടി",
                westSide = "പടിഞ്ഞാറ് (D) അടി",
                diagonalSide = "വികർണ്ണം (Diagonal) അടി",
                autoCalibrateDiagonal = "ഓട്ടോ-കാലിബ്രേറ്റ് വികർണ്ണം",
                selectCivilCategory = "1. നിർമ്മാണ വിഭാഗം തിരഞ്ഞെടുക്കുക",
                enterRoomDimensions = "2. മുറി / മതിൽ അളവുകൾ നൽകുക",
                lengthFeet = "നീളം (അടി)",
                widthOrHeightFeet = "വീതി / ഉയരം (അടി)",
                billOfQuantities = "നിർമ്മാണ സാമഗ്രികളുടെ പട്ടിക (BOQ)",
                totalMaterialCost = "ആകെ സാമഗ്രി ചെലവ്",
                savedVaultTab = "സേവ് ചെയ്തവ",
                polyglotTab = "പോളിഗ്ലോട്ട് എഞ്ചിൻ (7 ഭാഷകൾ)",
                legalTab = "നിയമവും സ്വകാര്യതയും",
                languagePickerTitle = "ഭാഷ തിരഞ്ഞെടുക്കുക (115+ ഭാഷകൾ)",
                searchLanguageHint = "ഭാഷ തിരയുക..."
            )
            "bn", "as", "mni" -> LocalizedUiStrings(
                appTitle = "Khetrova (জমি ও নির্মাণ ক্যালকুলেটর)",
                appSubtitle = "ভারতীয় আঞ্চলিক ভূমি পরিমাপ, 2D CAD নকশা এবং নির্মাণ BOQ ইঞ্জিন",
                zeroPermissionBadge = "১০০% অফলাইন • জিরো পারমিশন • সম্পূর্ণ নিরাপদ",
                navLandUnits = "জমির মাপ",
                navPlotCad = "প্লট CAD",
                navMaterials = "নির্মাণ সামগ্রী",
                navVaultCode = "ভল্ট ও কোড",
                selectStateStandard = "১. রাজ্য অনুযায়ী ভূমি পরিমাপের মান নির্বাচন করুন",
                chooseUnitAndArea = "২. একক নির্বাচন করুন এবং জমির ক্ষেত্রফল লিখুন",
                areaLabel = "ক্ষেত্রফল",
                rateLabel = "দর ₹ /",
                instantEquivalents = "তাৎক্ষণিক আঞ্চলিক ও মেট্রিক রূপান্তর",
                landValuationTitle = "জমির মূল্যায়ন ও স্ট্যাম্প ডিউটি হিসাব",
                basePlotValue = "জমির মূল দাম:",
                estStampDuty = "আনুমানিক স্ট্যাম্প ডিউটি ও রেজিস্ট্রেশন",
                totalLandCost = "মোট জমি ক্রয় খরচ:",
                saveToVault = "ভল্টে সেভ করুন",
                exportPdf = "PDF রিপোর্ট",
                liveCadBlueprint = "লাইভ 2D CAD সার্ভে ব্লুপ্রিন্ট নকশা",
                triangulatedArea = "সঠিক ত্রিভুজ ক্ষেত্রফল",
                perimeter = "পরিসীমা",
                contractorErrorDetector = "ঠিকাদারের 'গড় ফর্মুলা' ত্রুটি পরীক্ষক",
                enterFourSides = "প্লটের ৪টি বাহু ও কর্ণ (ফুট) লিখুন",
                northSide = "উত্তর বাহু (A) ফুট",
                southSide = "দক্ষিণ বাহু (C) ফুট",
                eastSide = "পূর্ব বাহু (B) ফুট",
                westSide = "পশ্চিম বাহু (D) ফুট",
                diagonalSide = "কর্ণ (Diagonal) ফুট",
                autoCalibrateDiagonal = "কর্ণ অটো-ক্যালিব্রেট করুন",
                selectCivilCategory = "১. নির্মাণ কাজের বিভাগ নির্বাচন করুন",
                enterRoomDimensions = "২. ঘর বা দেয়ালের দৈর্ঘ্য-প্রস্থ লিখুন",
                lengthFeet = "দৈর্ঘ্য (ফুট)",
                widthOrHeightFeet = "প্রস্থ / উচ্চতা (ফুট)",
                billOfQuantities = "নির্মাণ সামগ্রীর তালিকা (BOQ)",
                totalMaterialCost = "মোট আনুমানিক উপকরণ খরচ",
                savedVaultTab = "সংরক্ষিত রিপোর্ট",
                polyglotTab = "পলিগ্লট ইঞ্জিন (৭টি ভাষা)",
                legalTab = "আইনি ও গোপনীয়তা (১৩টি নীতি)",
                languagePickerTitle = "অ্যাপের ভাষা নির্বাচন করুন (১১৫+ ভাষা)",
                searchLanguageHint = "ভাষা খুঁজুন..."
            )
            "pa" -> LocalizedUiStrings(
                appTitle = "Khetrova (ਜ਼ਮੀਨ ਅਤੇ ਉਸਾਰੀ ਕੈਲਕੁਲੇਟਰ)",
                appSubtitle = "ਭਾਰਤੀ ਖੇਤਰੀ ਜ਼ਮੀਨ ਮਾਪ, 2D CAD ਨਕਸ਼ਾ ਅਤੇ ਉਸਾਰੀ ਸਮੱਗਰੀ BOQ",
                zeroPermissionBadge = "100% ਆਫ਼ਲਾਈਨ • ਜ਼ੀਰੋ ਪਰਮਿਸ਼ਨ • ਪੂਰੀ ਤਰ੍ਹਾਂ ਸੁਰੱਖਿਅਤ",
                navLandUnits = "ਜ਼ਮੀਨ ਮਾਪ",
                navPlotCad = "ਪਲਾਟ CAD",
                navMaterials = "ਉਸਾਰੀ ਸਮੱਗਰੀ",
                navVaultCode = "ਵਾਲਟ ਅਤੇ ਕੋਡ",
                selectStateStandard = "1. ਰਾਜ ਅਨੁਸਾਰ ਜ਼ਮੀਨ ਮਾਪਣ ਦਾ ਮਿਆਰ ਚੁਣੋ",
                chooseUnitAndArea = "2. ਇਕਾਈ ਚੁਣੋ ਅਤੇ ਖੇਤਰਫਲ ਦਰਜ ਕਰੋ",
                areaLabel = "ਖੇਤਰਫਲ",
                rateLabel = "ਰੇਟ ₹ /",
                instantEquivalents = "ਤੁਰੰਤ ਖੇਤਰੀ ਅਤੇ ਮੈਟ੍ਰਿਕ ਰੂਪਾਂਤਰਣ",
                landValuationTitle = "ਜ਼ਮੀਨ ਦਾ ਮੁੱਲ ਅਤੇ ਸਟੈਂਪ ਡਿਊਟੀ ਅੰਦਾਜ਼ਾ",
                basePlotValue = "ਮੂਲ ਪਲਾਟ ਕੀਮਤ:",
                estStampDuty = "ਅੰਦਾਜ਼ਨ ਸਟੈਂਪ ਡਿਊਟੀ ਅਤੇ ਰਜਿਸਟ੍ਰੇਸ਼ਨ",
                totalLandCost = "ਕੁੱਲ ਜ਼ਮੀਨ ਦੀ ਲਾਗਤ:",
                saveToVault = "ਵਾਲਟ ਵਿੱਚ ਸੇਵ ਕਰੋ",
                exportPdf = "PDF ਰਿਪੋਰਟ",
                liveCadBlueprint = "ਲਾਈਵ 2D CAD ਸਰਵੇ ਬਲੂਪ੍ਰਿੰਟ",
                triangulatedArea = "ਸਹੀ ਤਿਕੋਣਮਿਤੀ ਖੇਤਰਫਲ",
                perimeter = "ਘੇਰਾ (Perimeter)",
                contractorErrorDetector = "ਠੇਕੇਦਾਰ 'ਔਸਤ ਫਾਰਮੂਲਾ' ਗਲਤੀ ਜਾਂਚਕਰਤਾ",
                enterFourSides = "ਪਲਾਟ ਦੀਆਂ ਚਾਰੇ ਭੁਜਾਵਾਂ ਅਤੇ ਵਿਕਰਣ (ਫੁੱਟ) ਦਰਜ ਕਰੋ",
                northSide = "ਉੱਤਰ ਪਾਸਾ (A) ਫੁੱਟ",
                southSide = "ਦੱਖਣ ਪਾਸਾ (C) ਫੁੱਟ",
                eastSide = "ਪੂਰਬ ਪਾਸਾ (B) ਫੁੱਟ",
                westSide = "ਪੱਛਮ ਪਾਸਾ (D) ਫੁੱਟ",
                diagonalSide = "ਵਿਕਰਣ (Diagonal) ਫੁੱਟ",
                autoCalibrateDiagonal = "ਵਿਕਰਣ ਆਟੋ-ਕੈਲੀਬ੍ਰੇਟ ਕਰੋ",
                selectCivilCategory = "1. ਉਸਾਰੀ ਕੰਮ ਦੀ ਸ਼੍ਰੇਣੀ ਚੁਣੋ",
                enterRoomDimensions = "2. ਕਮਰੇ / ਕੰਧ ਦੀ ਲੰਬਾਈ-ਚੌੜਾਈ ਦਰਜ ਕਰੋ",
                lengthFeet = "ਲੰਬਾਈ (ਫੁੱਟ)",
                widthOrHeightFeet = "ਚੌੜਾਈ / ਉਚਾਈ (ਫੁੱਟ)",
                billOfQuantities = "ਉਸਾਰੀ ਸਮੱਗਰੀ ਸੂਚੀ (BOQ)",
                totalMaterialCost = "ਕੁੱਲ ਅੰਦਾਜ਼ਨ ਸਮੱਗਰੀ ਖਰਚਾ",
                savedVaultTab = "ਸੇਵ ਕੀਤੀਆਂ ਰਿਪੋਰਟਾਂ",
                polyglotTab = "ਪੌਲੀਗਲੋਟ ਇੰਜਣ (7 ਭਾਸ਼ਾਵਾਂ)",
                legalTab = "ਕਾਨੂੰਨੀ ਅਤੇ ਗੋਪਨੀਯਤਾ (13 ਨੀਤੀਆਂ)",
                languagePickerTitle = "ਐਪ ਦੀ ਭਾਸ਼ਾ ਚੁਣੋ (115+ ਭਾਸ਼ਾਵਾਂ)",
                searchLanguageHint = "ਭਾਸ਼ਾ ਖੋਜੋ..."
            )
            "or" -> LocalizedUiStrings(
                appTitle = "Khetrova (ଜମି ଓ ନିର୍ମାଣ କାଲକୁଲେଟର)",
                appSubtitle = "ଭାରତୀୟ ଆଞ୍ଚଳିକ ଜମି ମାପ, 2D CAD ନକ୍ସା ଏବଂ ନିର୍ମାଣ ସାମଗ୍ରୀ BOQ",
                zeroPermissionBadge = "100% ଅଫଲାଇନ୍ • ଜିରୋ ପରମିଶନ୍ • ସମ୍ପୂର୍ଣ୍ଣ ସୁରକ୍ଷିତ",
                navLandUnits = "ଜମି ମାପ",
                navPlotCad = "ପ୍ଲଟ୍ CAD",
                navMaterials = "ନିର୍ମାଣ ସାମଗ୍ରୀ",
                navVaultCode = "ଭଲ୍ଟ ଓ କୋଡ୍",
                selectStateStandard = "1. ରାଜ୍ୟ ଅନୁଯାୟୀ ଜମି ମାପ ମାନକ ବାଛନ୍ତୁ",
                chooseUnitAndArea = "2. ଏକକ ବାଛନ୍ତୁ ଏବଂ କ୍ଷେତ୍ରଫଳ ଲେଖନ୍ତୁ",
                areaLabel = "କ୍ଷେତ୍ରଫଳ",
                rateLabel = "ମୂଲ୍ୟ ₹ /",
                instantEquivalents = "ତୁରନ୍ତ ଆଞ୍ଚଳିକ ଓ ମେଟ୍ରିକ୍ ରୂପାନ୍ତର",
                landValuationTitle = "ଜମି ମୂଲ୍ୟାଙ୍କନ ଓ ଷ୍ଟାମ୍ପ ଡ୍ୟୁଟି ହିସାବ",
                basePlotValue = "ମୂଳ ଜମି ମୂଲ୍ୟ:",
                estStampDuty = "ଆନୁମାନିକ ଷ୍ଟାମ୍ପ ଡ୍ୟୁଟି",
                totalLandCost = "ମୋଟ ଜମି ଖର୍ଚ୍ଚ:",
                saveToVault = "ସେଭ୍ କରନ୍ତୁ",
                exportPdf = "PDF ରିପୋର୍ଟ",
                liveCadBlueprint = "ଲାଇଭ୍ 2D CAD ସର୍ଭେ ବ୍ଲୁପ୍ରିଣ୍ଟ",
                triangulatedArea = "ସଠିକ୍ କ୍ଷେତ୍ରଫଳ",
                perimeter = "ପରିସୀମା",
                contractorErrorDetector = "ହାରାହାରି ସୂତ୍ର ତ୍ରୁଟି ପରୀକ୍ଷক",
                enterFourSides = "4 ବାହୁ ଓ କର୍ଣ୍ଣ (ଫୁଟ୍) ଲେଖନ୍ତୁ",
                northSide = "ଉତ୍ତର ବାହୁ (A) ଫୁଟ୍",
                southSide = "ଦକ୍ଷିଣ ବାହୁ (C) ଫୁଟ୍",
                eastSide = "ପୂର୍ବ ବାହୁ (B) ଫୁଟ୍",
                westSide = "ପଶ୍ଚିମ ବାହୁ (D) ଫୁଟ୍",
                diagonalSide = "କର୍ଣ୍ଣ (Diagonal) ଫୁଟ୍",
                autoCalibrateDiagonal = "କର୍ଣ୍ଣ ଅଟୋ-କାଲିବ୍ରେଟ୍",
                selectCivilCategory = "1. ନିର୍ମାଣ କାର୍ଯ୍ୟ ବିଭାଗ ବାଛନ୍ତୁ",
                enterRoomDimensions = "2. ଘର / କାନ୍ଥର ଲମ୍ବ-ଚଉଡ଼ା ଲେଖନ୍ତୁ",
                lengthFeet = "ଲମ୍ବ (ଫୁଟ୍)",
                widthOrHeightFeet = "ଚଉଡ଼ା / ଉଚ୍ଚତା (ଫୁଟ୍)",
                billOfQuantities = "ନିର୍ମାଣ ସାମଗ୍ରୀ ତାଲିକା (BOQ)",
                totalMaterialCost = "ମୋଟ ଆନୁମାନିକ ସାମଗ୍ରୀ ଖର୍ଚ୍ଚ",
                savedVaultTab = "ସଂରକ୍ଷିତ ରିପୋର୍ଟ",
                polyglotTab = "ପଲିଗ୍ଲଟ୍ ଇଞ୍ଜିନ୍ (7 ଭାଷା)",
                legalTab = "ଆଇନଗତ ଓ ଗୋପନୀୟତା",
                languagePickerTitle = "ଭାଷା ବାଛନ୍ତୁ (115+ ଭାଷା)",
                searchLanguageHint = "ଭାଷା ଖୋଜନ୍ତୁ..."
            )
            "sa" -> LocalizedUiStrings(
                appTitle = "Khetrova (भूमि-निर्माण-गणकम्)",
                appSubtitle = "भारतीय-भूमि-मापनम्, 2D CAD क्षेत्र-चित्रणं निर्माण-द्रव्य-गणनं च",
                zeroPermissionBadge = "१००% ऑफलाइन • शून्यानुमतिः • पूर्णसुरक्षितम्",
                navLandUnits = "भूमिमापनम्",
                navPlotCad = "क्षेत्र-CAD",
                navMaterials = "निर्माणद्रव्याणि",
                navVaultCode = "कोशः कूटश्च",
                selectStateStandard = "१. राज्यानुसारं भूमिमापनमानं वृणुत",
                chooseUnitAndArea = "२. एककं चित्वा क्षेत्रफलं लिखत",
                areaLabel = "क्षेत्रफलम्",
                rateLabel = "मूल्यम् ₹ /",
                instantEquivalents = "तत्काल-क्षेत्रीय-रूपांतरणम्",
                landValuationTitle = "भूमिमूल्यांकनं मुद्रांकशुल्कं च",
                basePlotValue = "मूलभूमिमूल्यम्:",
                estStampDuty = "मुद्रांकशुल्कम्",
                totalLandCost = "समग्रभूमिमूल्यम्:",
                saveToVault = "कोशे रक्षत",
                exportPdf = "PDF पत्रम्",
                liveCadBlueprint = "प्रत्यक्षं 2D CAD क्षेत्रचित्रणम्",
                triangulatedArea = "त्रिकोणमितिक्षेत्रफलम्",
                perimeter = "परिधिः",
                contractorErrorDetector = "सामान्यसूत्र-दोषपरीक्षकः",
                enterFourSides = "चतस्रः भुजाः कर्णं च (पादमाने) लिखत",
                northSide = "उत्तरभुजा (A)",
                southSide = "दक्षिणभुजा (C)",
                eastSide = "पूर्वभुजा (B)",
                westSide = "पश्चिमभुजा (D)",
                diagonalSide = "कर्णः (Diagonal)",
                autoCalibrateDiagonal = "स्वतः कर्णगणना",
                selectCivilCategory = "१. निर्माणकार्यविभागं वृणुत",
                enterRoomDimensions = "२. कक्षस्य भित्तेः वा आयामं लिखत",
                lengthFeet = "दैर्घ्यम् (ft)",
                widthOrHeightFeet = "विस्तारः / उच्चता (ft)",
                billOfQuantities = "निर्माणद्रव्यसूची (BOQ)",
                totalMaterialCost = "समग्रद्रव्यव्ययः",
                savedVaultTab = "रक्षितलेखाः",
                polyglotTab = "बहुभाषा-यन्त्राणि (७)",
                legalTab = "विधि-गोपनीयतानीतयः (१३)",
                languagePickerTitle = "भाषां वृणुत (११५+ भाषाः)",
                searchLanguageHint = "भाषां अन्विष्यताम्..."
            )
            "ja" -> LocalizedUiStrings(
                appTitle = "Khetrova (土地測量・建築積算エンジン)",
                appSubtitle = "インド地域土地単位換算・2D CAD不整形地測量・建築資材BOQ計算機",
                zeroPermissionBadge = "100% オフライン • 権限不要 • クラウド送信ゼロ",
                navLandUnits = "土地単位",
                navPlotCad = "CAD測量",
                navMaterials = "建築資材",
                navVaultCode = "保管庫・コード",
                selectStateStandard = "1. 州・地域の土地測量基準を選択",
                chooseUnitAndArea = "2. 入力単位を選択し面積を入力",
                areaLabel = "面積",
                rateLabel = "単価 ₹ /",
                instantEquivalents = "地域単位・メートル法の即時換算表",
                landValuationTitle = "土地評価額および印紙税・登記費用見積",
                basePlotValue = "基本土地価格:",
                estStampDuty = "推定印紙税・登記料",
                totalLandCost = "土地取得総額:",
                saveToVault = "ローカル保存",
                exportPdf = "PDF出力",
                liveCadBlueprint = "リアルタイム 2D CAD 測量図面 (縮尺対応)",
                triangulatedArea = "ヘロン三角測量面積",
                perimeter = "外周長",
                contractorErrorDetector = "業者『平均法』誤差チェッカー",
                enterFourSides = "4辺の長さと対角線 (フィート) を入力",
                northSide = "北辺 (A) ft",
                southSide = "南辺 (C) ft",
                eastSide = "東辺 (B) ft",
                westSide = "西辺 (D) ft",
                diagonalSide = "対角線 (NW-SE) ft",
                autoCalibrateDiagonal = "対角線を自動補正",
                selectCivilCategory = "1. 建築・左官工事カテゴリを選択",
                enterRoomDimensions = "2. 部屋・壁の寸法と仕様を入力",
                lengthFeet = "長さ (ft)",
                widthOrHeightFeet = "幅 / 高さ (ft)",
                billOfQuantities = "建築資材数量内訳書 (BOQ)",
                totalMaterialCost = "推定資材総コスト",
                savedVaultTab = "保存済みデータ (オフラインDB)",
                polyglotTab = "多言語エンジン (7言語実装)",
                legalTab = "法務・プライバシー規約 (13項目)",
                languagePickerTitle = "表示言語を選択 (Google対応115+言語)",
                searchLanguageHint = "言語を検索 (例: 日本語, ગુજરાતી, हिन्दी)..."
            )
            "ko" -> LocalizedUiStrings(
                appTitle = "Khetrova (토지 측량 및 건축 적산 엔진)",
                appSubtitle = "지역 토지 단위 변환, 2D CAD 부정형 플롯 도면 및 건축 자재 BOQ 계산기",
                zeroPermissionBadge = "100% 오프라인 • 권한 불필요 • 클라우드 추적 없음",
                navLandUnits = "토지 단위",
                navPlotCad = "플롯 CAD",
                navMaterials = "건축 자재",
                navVaultCode = "보관함 & 코드",
                selectStateStandard = "1. 주/지역 토지 측량 표준 선택",
                chooseUnitAndArea = "2. 입력 단위 선택 및 면적 입력",
                areaLabel = "면적",
                rateLabel = "단가 ₹ /",
                instantEquivalents = "실시간 지역 및 미터법 단위 변환",
                landValuationTitle = "토지 가치 및 인지세 견적",
                basePlotValue = "기본 토지 가액:",
                estStampDuty = "예상 인지세 및 등록비",
                totalLandCost = "총 토지 취득 비용:",
                saveToVault = "보관함에 저장",
                exportPdf = "PDF 내보내기",
                liveCadBlueprint = "실시간 2D CAD 측량 도면 (축척 적용)",
                triangulatedArea = "삼각측량 정밀 면적",
                perimeter = "둘레",
                contractorErrorDetector = "시공사 '평균 공식' 오차 감지기",
                enterFourSides = "4면의 길이와 대각선 (피트) 입력",
                northSide = "북쪽 변 (A) ft",
                southSide = "남쪽 변 (C) ft",
                eastSide = "동쪽 변 (B) ft",
                westSide = "서쪽 변 (D) ft",
                diagonalSide = "대각선 (NW-SE) ft",
                autoCalibrateDiagonal = "대각선 자동 보정",
                selectCivilCategory = "1. 건축 / 시공 작업 유형 선택",
                enterRoomDimensions = "2. 방 / 벽 치수 입력",
                lengthFeet = "길이 (ft)",
                widthOrHeightFeet = "너비 / 높이 (ft)",
                billOfQuantities = "건축 자재 내역서 (BOQ)",
                totalMaterialCost = "총 예상 자재 비용",
                savedVaultTab = "저장된 보관함 (오프라인 DB)",
                polyglotTab = "폴리글랏 엔진 (7개 언어)",
                legalTab = "법률 및 개인정보 보호정책 (13개)",
                languagePickerTitle = "앱 언어 선택 (115개 이상 언어)",
                searchLanguageHint = "언어 검색..."
            )
            "zh-CN", "zh-TW" -> LocalizedUiStrings(
                appTitle = "Khetrova (土地测量与建筑工程量计算)",
                appSubtitle = "区域土地单位换算、2D CAD不规则地块绘图与建筑材料BOQ引擎",
                zeroPermissionBadge = "100% 离线运行 • 零权限要求 • 零云端追踪",
                navLandUnits = "土地单位",
                navPlotCad = "地块 CAD",
                navMaterials = "建筑材料",
                navVaultCode = "保险库与代码",
                selectStateStandard = "1. 选择州/区域土地测量标准",
                chooseUnitAndArea = "2. 选择输入单位并输入面积",
                areaLabel = "面积",
                rateLabel = "单价 ₹ /",
                instantEquivalents = "即时区域与公制单位换算",
                landValuationTitle = "土地估值与印花税估算",
                basePlotValue = "基础土地价值:",
                estStampDuty = "预计印花税与登记费",
                totalLandCost = "土地购置总成本:",
                saveToVault = "保存到本地库",
                exportPdf = "导出 PDF 报告",
                liveCadBlueprint = "实时 2D CAD 测量蓝图 (按比例)",
                triangulatedArea = "海伦三角测量精确面积",
                perimeter = "周长",
                contractorErrorDetector = "承包商‘平均公式’误差检测器",
                enterFourSides = "输入四边长度与对角线 (英尺)",
                northSide = "北边 (A) ft",
                southSide = "南边 (C) ft",
                eastSide = "东边 (B) ft",
                westSide = "西边 (D) ft",
                diagonalSide = "对角线 (NW-SE) ft",
                autoCalibrateDiagonal = "自动校准对角线",
                selectCivilCategory = "1. 选择土木/泥瓦工程类别",
                enterRoomDimensions = "2. 输入房间/墙体尺寸与规格",
                lengthFeet = "长度 (英尺)",
                widthOrHeightFeet = "宽度 / 高度 (英尺)",
                billOfQuantities = "建筑材料工程量清单 (BOQ)",
                totalMaterialCost = "预计材料总成本",
                savedVaultTab = "已存报告 (离线数据库)",
                polyglotTab = "多语言引擎 (7种语言)",
                legalTab = "法律与隐私合规 (13项政策)",
                languagePickerTitle = "选择应用语言 (115+ 种语言)",
                searchLanguageHint = "搜索语言..."
            )
            "ar", "ur", "fa", "ps", "sd", "he", "ug", "yi" -> LocalizedUiStrings(
                appTitle = "Khetrova (حاسبة الأراضي والبناء)",
                appSubtitle = "تحويل وحدات الأراضي، مخطط 2D CAD وحساب كميات مواد البناء BOQ",
                zeroPermissionBadge = "100% بدون إنترنت • بدون أذونات • خصوصية تامة",
                navLandUnits = "وحدات الأرض",
                navPlotCad = "مخطط CAD",
                navMaterials = "مواد البناء",
                navVaultCode = "الخزنة والكود",
                selectStateStandard = "1. اختر معيار قياس الأراضي الإقليمي",
                chooseUnitAndArea = "2. اختر الوحدة وأدخل المساحة",
                areaLabel = "المساحة",
                rateLabel = "السعر ₹ /",
                instantEquivalents = "التحويل الفوري للوحدات الإقليمية والمترية",
                landValuationTitle = "تقييم الأرض وتقدير رسوم التسجيل",
                basePlotValue = "قيمة الأرض الأساسية:",
                estStampDuty = "رسوم الدمغة والتسجيل المقدرة",
                totalLandCost = "إجمالي تكلفة الأرض:",
                saveToVault = "حفظ في الخزنة",
                exportPdf = "تصدير PDF",
                liveCadBlueprint = "مخطط مسح هندسي 2D CAD مباشر",
                triangulatedArea = "المساحة الدقيقة بالمثلثات",
                perimeter = "المحيط",
                contractorErrorDetector = "كاشف خطأ صيغة المتوسط للمقاول",
                enterFourSides = "أدخل الأضلاع الأربعة والقطر (بالقدم)",
                northSide = "الضلع الشمالي (A)",
                southSide = "الضلع الجنوبي (C)",
                eastSide = "الضلع الشرقي (B)",
                westSide = "الضلع الغربي (D)",
                diagonalSide = "القطر (Diagonal)",
                autoCalibrateDiagonal = "معايرة القطر تلقائياً",
                selectCivilCategory = "1. اختر فئة أعمال البناء",
                enterRoomDimensions = "2. أدخل أبعاد الغرفة أو الجدار",
                lengthFeet = "الطول (قدم)",
                widthOrHeightFeet = "العرض / الارتفاع (قدم)",
                billOfQuantities = "جدول كميات مواد البناء (BOQ)",
                totalMaterialCost = "إجمالي تكلفة المواد المقدرة",
                savedVaultTab = "التقارير المحفوظة",
                polyglotTab = "محركات متعددة اللغات (7)",
                legalTab = "السياسات القانونية والخصوصية (13)",
                languagePickerTitle = "اختر لغة التطبيق (115+ لغة)",
                searchLanguageHint = "ابحث عن اللغة..."
            )
            "es", "pt", "ca", "gl" -> LocalizedUiStrings(
                appTitle = "Khetrova (Topografía y Construcción)",
                appSubtitle = "Conversor de Unidades de Tierra, Plano CAD 2D y Presupuesto BOQ de Obra",
                zeroPermissionBadge = "100% Sin Conexión • Cero Permisos • Privacidad Total",
                navLandUnits = "Unidades",
                navPlotCad = "Plano CAD",
                navMaterials = "Materiales",
                navVaultCode = "Bóveda y Código",
                selectStateStandard = "1. Seleccione el Estándar Regional de Medición",
                chooseUnitAndArea = "2. Elija la Unidad e Ingrese el Área",
                areaLabel = "Área en",
                rateLabel = "Tarifa ₹ /",
                instantEquivalents = "EQUIVALENTES REGIONALES Y MÉTRICOS INSTANTÁNEOS",
                landValuationTitle = "Tasación del Terreno e Impuestos de Timbre",
                basePlotValue = "Valor Base del Terreno:",
                estStampDuty = "Impuesto de Timbre y Registro Est.",
                totalLandCost = "Costo Total de Adquisición:",
                saveToVault = "Guardar en Bóveda",
                exportPdf = "Exportar PDF",
                liveCadBlueprint = "PLANO TOPOGRÁFICO CAD 2D EN VIVO (A ESCALA)",
                triangulatedArea = "Área Triangulada Exacta",
                perimeter = "Perímetro",
                contractorErrorDetector = "Detector de Error de 'Fórmula Promedio'",
                enterFourSides = "Ingrese los 4 Lados y la Diagonal (Pies)",
                northSide = "Lado Norte (A) ft",
                southSide = "Lado Sur (C) ft",
                eastSide = "Lado Este (B) ft",
                westSide = "Lado Oeste (D) ft",
                diagonalSide = "Diagonal (NO-SE) ft",
                autoCalibrateDiagonal = "Auto-Calibrar Diagonal",
                selectCivilCategory = "1. Seleccione Categoría de Obra Civil",
                enterRoomDimensions = "2. Ingrese Dimensiones de Habitación / Muro",
                lengthFeet = "Largo (Pies)",
                widthOrHeightFeet = "Ancho / Alto (Pies)",
                billOfQuantities = "LISTA DE CANTIDADES DE MATERIALES (BOQ)",
                totalMaterialCost = "Costo Total Est. de Materiales",
                savedVaultTab = "Bóveda Guardada (DB Local)",
                polyglotTab = "Motores Políglotas (7 Idiomas)",
                legalTab = "Legal y Privacidad (13 Políticas)",
                languagePickerTitle = "Seleccionar Idioma (115+ Idiomas de Google)",
                searchLanguageHint = "Buscar idioma..."
            )
            "fr", "ht", "co", "lb" -> LocalizedUiStrings(
                appTitle = "Khetrova (Arpentage & Construction)",
                appSubtitle = "Convertisseur Foncier Régional, Plan CAD 2D & Devis Matériaux BOQ",
                zeroPermissionBadge = "100% Hors-Ligne • Zéro Permission • Zéro Suivi Cloud",
                navLandUnits = "Unités Foncières",
                navPlotCad = "Plan CAD",
                navMaterials = "Matériaux",
                navVaultCode = "Coffre & Code",
                selectStateStandard = "1. Sélectionner la Norme Régionale d'Arpentage",
                chooseUnitAndArea = "2. Choisir l'Unité et Entrer la Superficie",
                areaLabel = "Superficie en",
                rateLabel = "Tarif ₹ /",
                instantEquivalents = "ÉQUIVALENTS RÉGIONAUX ET MÉTRIQUES INSTANTANÉS",
                landValuationTitle = "Évaluation Foncière et Droits d'Enregistrement",
                basePlotValue = "Valeur Foncière de Base:",
                estStampDuty = "Droits de Timbre Estimés",
                totalLandCost = "Coût Total d'Acquisition:",
                saveToVault = "Sauvegarder",
                exportPdf = "Exporter PDF",
                liveCadBlueprint = "PLAN D'ARPENTAGE CAD 2D EN DIRECT (À L'ÉCHELLE)",
                triangulatedArea = "Superficie Triangulée Exacte",
                perimeter = "Périmètre",
                contractorErrorDetector = "Détecteur d'Erreur de Formule Moyenne",
                enterFourSides = "Entrer les 4 Côtés et la Diagonale (Pieds)",
                northSide = "Côté Nord (A) ft",
                southSide = "Côté Sud (C) ft",
                eastSide = "Côté Est (B) ft",
                westSide = "Côté Ouest (D) ft",
                diagonalSide = "Diagonale (NO-SE) ft",
                autoCalibrateDiagonal = "Calibrer la Diagonale Auto",
                selectCivilCategory = "1. Choisir la Catégorie de Génie Civil",
                enterRoomDimensions = "2. Entrer les Dimensions de la Pièce / du Mur",
                lengthFeet = "Longueur (Pieds)",
                widthOrHeightFeet = "Largeur / Hauteur (Pieds)",
                billOfQuantities = "BORDEREAU DES QUANTITÉS (BOQ)",
                totalMaterialCost = "Coût Total Estimé des Matériaux",
                savedVaultTab = "Coffre Local (Base SQLite)",
                polyglotTab = "Moteurs Polyglottes (7 Langues)",
                legalTab = "Mentions Légales & Confidentialité (13)",
                languagePickerTitle = "Choisir la Langue (115+ Langues Google)",
                searchLanguageHint = "Rechercher une langue..."
            )
            "de", "nl", "af", "fy", "da", "sv", "no", "is" -> LocalizedUiStrings(
                appTitle = "Khetrova (Landvermessung & Bau-BOQ)",
                appSubtitle = "Regionale Flächeneinheiten, 2D-CAD-Grundstücksplan & Baumaterialrechner",
                zeroPermissionBadge = "100% Offline • Keine Berechtigungen • Kein Cloud-Tracking",
                navLandUnits = "Flächeneinheiten",
                navPlotCad = "CAD-Plan",
                navMaterials = "Baumaterial",
                navVaultCode = "Tresor & Code",
                selectStateStandard = "1. Regionalen Vermessungsstandard wählen",
                chooseUnitAndArea = "2. Einheit wählen & Grundstücksfläche eingeben",
                areaLabel = "Fläche in",
                rateLabel = "Preis ₹ /",
                instantEquivalents = "SOFORTIGE REGIONALE & METRISCHE UMRECHNUNG",
                landValuationTitle = "Grundstücksbewertung & Grunderwerbsteuer",
                basePlotValue = "Grundstückswert:",
                estStampDuty = "Geschätzte Stempelsteuer",
                totalLandCost = "Gesamterwerbskosten:",
                saveToVault = "Im Tresor speichern",
                exportPdf = "PDF exportieren",
                liveCadBlueprint = "LIVE 2D-CAD-VERMESSUNGSPLAN (MASSSTABSGETREU)",
                triangulatedArea = "Exakte Triangulationsfläche",
                perimeter = "Umfang",
                contractorErrorDetector = "Fehlerdetektor für Durchschnittsformel",
                enterFourSides = "4 Grundstücksseiten & Diagonale (Fuß) eingeben",
                northSide = "Nordseite (A) ft",
                southSide = "Südseite (C) ft",
                eastSide = "Ostseite (B) ft",
                westSide = "Westseite (D) ft",
                diagonalSide = "Diagonale (NW-SO) ft",
                autoCalibrateDiagonal = "Diagonale automatisch kalibrieren",
                selectCivilCategory = "1. Bauarbeiten-Kategorie auswählen",
                enterRoomDimensions = "2. Raum- / Wandmaße eingeben",
                lengthFeet = "Länge (Fuß)",
                widthOrHeightFeet = "Breite / Höhe (Fuß)",
                billOfQuantities = "BAUMATERIAL-LEISTUNGSVERZEICHNIS (BOQ)",
                totalMaterialCost = "Geschätzte Materialgesamtkosten",
                savedVaultTab = "Gespeicherte Berichte (Offline)",
                polyglotTab = "Polyglotte Engines (7 Sprachen)",
                legalTab = "Rechtliches & Datenschutz (13)",
                languagePickerTitle = "App-Sprache wählen (115+ Sprachen)",
                searchLanguageHint = "Sprache suchen..."
            )
            "ru", "uk", "be", "bg", "sr", "mk", "kk", "ky", "tg", "tt", "mn" -> LocalizedUiStrings(
                appTitle = "Khetrova (Геодезия и Расчет Стройматериалов)",
                appSubtitle = "Конвертер земельных единиц, 2D CAD план участка и смета материалов BOQ",
                zeroPermissionBadge = "100% Офлайн • Без разрешений • Без облачной слежки",
                navLandUnits = "Единицы земли",
                navPlotCad = "CAD План",
                navMaterials = "Материалы",
                navVaultCode = "Хранилище и Код",
                selectStateStandard = "1. Выберите региональный стандарт измерения",
                chooseUnitAndArea = "2. Выберите единицу и введите площадь",
                areaLabel = "Площадь в",
                rateLabel = "Цена ₹ /",
                instantEquivalents = "МГНОВЕННЫЙ ПЕРЕСЧЕТ РЕГИОНАЛЬНЫХ И МЕТРИЧЕСКИХ ЕДИНИЦ",
                landValuationTitle = "Оценка стоимости участка и госпошлины",
                basePlotValue = "Базовая стоимость участка:",
                estStampDuty = "Расчетная пошлина и регистрация",
                totalLandCost = "Итоговая стоимость участка:",
                saveToVault = "Сохранить в базу",
                exportPdf = "Экспорт PDF",
                liveCadBlueprint = "2D CAD ЧЕРТЕЖ УЧАСТКА В РЕАЛЬНОМ ВРЕМЕНИ",
                triangulatedArea = "Точная площадь триангуляции",
                perimeter = "Периметр",
                contractorErrorDetector = "Детектор ошибки «средней формулы»",
                enterFourSides = "Введите 4 стороны и диагональ (в футах)",
                northSide = "Север (A) фут",
                southSide = "Юг (C) фут",
                eastSide = "Восток (B) фут",
                westSide = "Запад (D) фут",
                diagonalSide = "Диагональ (СЗ-ЮВ) фут",
                autoCalibrateDiagonal = "Авто-калибровка диагонали",
                selectCivilCategory = "1. Выберите категорию строительных работ",
                enterRoomDimensions = "2. Введите размеры комнаты / стены",
                lengthFeet = "Длина (футы)",
                widthOrHeightFeet = "Ширина / Высота (футы)",
                billOfQuantities = "ВЕДОМОСТЬ СТРОИТЕЛЬНЫХ МАТЕРИАЛОВ (BOQ)",
                totalMaterialCost = "Общая стоимость материалов",
                savedVaultTab = "Сохраненные отчеты (SQLite)",
                polyglotTab = "7/12 и Фермерские инструменты",
                legalTab = "Правовая информация и защита данных (13)",
                languagePickerTitle = "Выберите язык (115+ языков Google)",
                searchLanguageHint = "Поиск языка...",
                navMap3D = "Карта и 3D",
                menuAllToolsBtn = "Меню • Все инструменты",
                allFeaturesRibbonBtn = "Все функции (24+)",
                dailyRojmelTab = "Ежедневный учет и Рынок",
                allFeaturesMenuTitle = "Меню всех функций и страниц",
                allFeaturesMenuSubtitle = "Нажмите на любой инструмент для быстрого открытия",
                allFeaturesSearchHint = "Поиск инструмента..."
            )
            else -> {
                val lang = allLanguages.find { it.code == langCode }
                val badgeSuffix = if (lang != null) " [${lang.nativeName} • ${lang.englishName}]" else ""
                englishDefault.copy(
                    appTitle = "Khetrova$badgeSuffix",
                    appSubtitle = "${lang?.nativeName ?: langCode} Edition • Global Land Survey, Daily Rojmel, 3D Map & BOQ",
                    languagePickerTitle = "Select App Language (${lang?.nativeName ?: "115+ Languages"})",
                    menuAllToolsBtn = "Menu • ${lang?.nativeName ?: "All Tools"}",
                    allFeaturesMenuTitle = "All Features & Pages (${lang?.nativeName ?: langCode})"
                )
            }
        }
    }

    /**
     * Generates localized "Continue as ___" confirmation button text for FirstLaunchLanguageOnboardingScreen.
     */
    fun getContinueButtonText(lang: AppLanguage): String {
        return when (lang.code) {
            "en" -> "Continue as English"
            "gu" -> "${lang.nativeName} (${lang.englishName}) તરીકે આગળ વધો • Continue as ${lang.nativeName}"
            "hi", "mai", "doi", "kok", "brx", "ne" -> "${lang.nativeName} (${lang.englishName}) में आगे बढ़ें • Continue as ${lang.nativeName}"
            "mr" -> "${lang.nativeName} (${lang.englishName}) मधून पुढे जा • Continue as ${lang.nativeName}"
            "ta" -> "${lang.nativeName} (${lang.englishName}) மொழியில் தொடரவும் • Continue as ${lang.nativeName}"
            "te" -> "${lang.nativeName} (${lang.englishName}) లో కొనసాగండి • Continue as ${lang.nativeName}"
            "kn" -> "${lang.nativeName} (${lang.englishName}) ನಲ್ಲಿ ಮುಂದುವರಿಯಿರಿ • Continue as ${lang.nativeName}"
            "ml" -> "${lang.nativeName} (${lang.englishName}) ഭാഷയിൽ തുടരുക • Continue as ${lang.nativeName}"
            "bn", "as", "mni" -> "${lang.nativeName} (${lang.englishName}) ভাষায় এগিয়ে যান • Continue as ${lang.nativeName}"
            "pa" -> "${lang.nativeName} (${lang.englishName}) ਵਿੱਚ ਅੱਗੇ ਵਧੋ • Continue as ${lang.nativeName}"
            "ja" -> "${lang.nativeName} (${lang.englishName}) で続行 • Continue as ${lang.nativeName}"
            "ko" -> "${lang.nativeName} (${lang.englishName})로 계속하기 • Continue as ${lang.nativeName}"
            "zh-CN", "zh-TW" -> "以 ${lang.nativeName} (${lang.englishName}) 继续 • Continue as ${lang.nativeName}"
            "ar", "ur", "fa", "ps", "sd", "he" -> "المتابعة باللغة ${lang.nativeName} • Continue as ${lang.englishName}"
            "es", "pt", "ca", "gl" -> "Continuar en ${lang.nativeName} (${lang.englishName})"
            "fr" -> "Continuer en ${lang.nativeName} (${lang.englishName})"
            "de" -> "Weiter auf ${lang.nativeName} (${lang.englishName})"
            "ru", "uk", "be", "kk" -> "Продолжить на ${lang.nativeName} (${lang.englishName})"
            else -> "Continue as ${lang.nativeName} (${lang.englishName})"
        }
    }

    /**
     * Returns fully localized Top Menu item (category, title, subtitle, badge) in the user's selected language.
     */
    fun getLocalizedMenuEntry(
        destName: String,
        langCode: String,
        defaultCategory: String,
        defaultTitle: String,
        defaultSubtitle: String,
        defaultBadge: String
    ): LocalizedMenuEntry {
        if (langCode == "en") {
            return LocalizedMenuEntry(defaultCategory, defaultTitle, defaultSubtitle, defaultBadge)
        }
        if (langCode == "gu") {
            return when (destName) {
                "LIVE_APMC_MANDI_PRICES" -> LocalizedMenuEntry(
                    category = "રોજિંદા ખેડૂત, જમીનદાર અને દલાલ સાધનો (Daily Use)",
                    title = "લાઈવ APMC માર્કેટ યાર્ડ (વિસ્તાર મુજબ) અને વિશ્વના દેશોના સરકારી પાક ભાવ",
                    subtitle = "ગોંડલ, રાજકોટ, ઊંઝા, ડીસા, મહુવા, અમરેલી સહિત વિસ્તાર મુજબ રોજના ઓટો-અપડેટ ભાવ અને વિદેશી સરકારી બજાર ભાવ",
                    badge = "લાઈવ • APMC"
                )
                "DAILY_MANDI_BILL" -> LocalizedMenuEntry(
                    category = "રોજિંદા ખેડૂત, જમીનદાર અને દલાલ સાધનો (Daily Use)",
                    title = "રોજિંદુ APMC માર્કેટ યાર્ડ (૨૦ કિલો મણ / ક્વિન્ટલ) અને પાક બિલ",
                    subtitle = "ગુણી/કટ્ટા, બારદાન ઘટ (કસર), ૨૦ કિલો મણ અને ક્વિન્ટલનો ભાવ તથા ચોખ્ખી રકમ ગણો",
                    badge = "રોજિંદુ • મંડી"
                )
                "DAILY_LABOUR_TRACTOR_KHATA" -> LocalizedMenuEntry(
                    category = "રોજિંદા ખેડૂત, જમીનદાર અને દલાલ સાધનો (Daily Use)",
                    title = "રોજિંદી ખેત-મજૂરી (દાડિયા હાજરી), ઉપાડ અને ટ્રેક્ટર ભાડા ખાતું",
                    subtitle = "મજૂરોના દિવસ, રોજિંદી દાડી, ટ્રેક્ટરના કલાકનું ભાડું અને આપેલ ઉપાડ બાદ કરીને બાકી હિસાબ",
                    badge = "રોજિંદુ • મજૂરી"
                )
                "DAILY_LAND_BROKER_DEAL" -> LocalizedMenuEntry(
                    category = "રોજિંદા ખેડૂત, જમીનદાર અને દલાલ સાધનો (Daily Use)",
                    title = "જમીન લે-વેચ દલાલી (Brokerage), ટોકન, બાનાખત અને દસ્તાવેજ હપ્તા",
                    subtitle = "જમીન દલાલો અને જમીનદારો માટે ખરીદ-વેચાણ દલાલી (1%-2%), ટોકન અને બાનાખત પેમેન્ટ શેડ્યૂલ",
                    badge = "રોજિંદુ • દલાલી"
                )
                "DAILY_FARM_DAIRY_ROJMEL" -> LocalizedMenuEntry(
                    category = "રોજિંદા ખેડૂત, જમીનદાર અને દલાલ સાધનો (Daily Use)",
                    title = "રોજિંદો ખેતી અને ડેરી (દૂધ) આવક-જાવક રોજમેળ (Daily Rojmel)",
                    subtitle = "સવાર-સાંજના દૂધના લીટર, શાકભાજી આવક સામે પશુ-દાણ, ડીઝલ અને પરચૂરણ ખર્ચનો રોજિંદો નફો",
                    badge = "રોજિંદુ • રોજમેળ"
                )
                "KHET_TALAVDI_WATER_TANK" -> LocalizedMenuEntry(
                    category = "ખેડૂત, પાણી અને ૭/૧૨ મહેસૂલ સાધનો",
                    title = "ખેત-તળાવડી / પાણીના ટાંકાની ક્ષમતા (લીટરમાં)",
                    subtitle = "લંબચોરસ ખેત-તળાવડી કે ટાંકામાં કેટલા લીટર પાણી સમાશે અને કેટલા કલાક પિયત ચાલશે",
                    badge = "ખેત-તળાવડી"
                )
                "CIRCULAR_WELL_BOREWELL_HP" -> LocalizedMenuEntry(
                    category = "ખેડૂત, પાણી અને ૭/૧૨ મહેસૂલ સાધનો",
                    title = "ગોળ કૂવાનું પાણી અને બોરવેલ સબમર્સિબલ મોટર HP",
                    subtitle = "ગોળ કૂવામાં પાણીનો જથ્થો (લીટર) અને બોરવેલની ઊંડાઈ મુજબ કેટલા HP ની મોટર જોશે",
                    badge = "કૂવો • મોટર HP"
                )
                "REVENUE_712_DECODER" -> LocalizedMenuEntry(
                    category = "ખેડૂત, પાણી અને ૭/૧૨ મહેસૂલ સાધનો",
                    title = "૭/૧૨ અને ૮-અ ઉતારા (હેક્ટર-આરે-ચો.મી.) ડિકોડર",
                    subtitle = "સરકારી ૭/૧૨ ના હેક્ટર-આરે-ચો.મી. નું સૌરાષ્ટ્ર/ઉત્તર ગુજરાત વીઘા, ગુંઠા, વાર અને એકરમાં રૂપાંતરણ",
                    badge = "૭/૧૨ ઉતારો"
                )
                "KHEDUT_LAND_PARTITION" -> LocalizedMenuEntry(
                    category = "ખેડૂત, પાણી અને ૭/૧૨ મહેસૂલ સાધનો",
                    title = "કૌટુંબિક જમીન વહેંચણી (વારસદારો / ભાઈઓના સરખા ભાગ)",
                    subtitle = "રસ્તા કપાત બાદ કરીને ૨ થી ૧૨ વારસદારો વચ્ચે જમીનના ચોક્કસ ભાગ અને રસ્તા પરનો પનો (Frontage)",
                    badge = "વારસાઈ ભાગ"
                )
                "KHEDUT_FARM_FENCING" -> LocalizedMenuEntry(
                    category = "ખેડૂત, પાણી અને ૭/૧૨ મહેસૂલ સાધનો",
                    title = "ખેતરની કાંટાળી તારની વાડ અને થાંભલાનો ખર્ચ",
                    subtitle = "સિમેન્ટ/લોખંડના થાંભલાની સંખ્યા, કાંટાળા તારનું વજન (કિલો) અને કુલ ખર્ચ",
                    badge = "તારની વાડ"
                )
                "KHEDUT_CROP_CALCULATOR" -> LocalizedMenuEntry(
                    category = "ખેડૂત, પાણી અને ૭/૧૨ મહેસૂલ સાધનો",
                    title = "પાકનું બિયારણ, યુરિયા, DAP, પોટાશ અને અંદાજિત ઉત્પાદન",
                    subtitle = "કપાસ, મગફળી, ઘઉં, જીરું, એરંડા વગેરે માટે બિયારણ, ખાતરની થેલી અને મણમાં ઉત્પાદન",
                    badge = "પાક • ખાતર"
                )
                "LAND_UNIT_CONVERTER" -> LocalizedMenuEntry(
                    category = "જમીન માપણી, નકશા કટઆઉટ અને 2D/3D પ્લોટ",
                    title = "ભારત અને વિશ્વના દેશો-રાજ્યો મુજબ જમીન માપણી કન્વર્ટર",
                    subtitle = "વીઘા, ગુંઠા, વાર, ચોરસ ફૂટ, મરલા, કનાલ, એકર, હેક્ટર અને વિદેશી રાજ્યોના માપ",
                    badge = "મુખ્ય પેજ"
                )
                "LAND_STAMP_DUTY_VALUATION" -> LocalizedMenuEntry(
                    category = "જમીન માપણી, નકશા કટઆઉટ અને 2D/3D પ્લોટ",
                    title = "જમીનનું મૂલ્યાંકન, જંત્રી અને સ્ટેમ્પ ડ્યુટી ગણતરી",
                    subtitle = "જમીનનો ભાવ, સ્ટેમ્પ ડ્યુટી અને રજીસ્ટ્રેશન ખર્ચ સાથે કુલ કિંમત",
                    badge = "સ્ટેમ્પ ડ્યુટી"
                )
                "MAP_SATELLITE_LINE_SURVEY" -> LocalizedMenuEntry(
                    category = "જમીન માપણી, નકશા કટઆઉટ અને 2D/3D પ્લોટ",
                    title = "લાઇવ નકશા પર લાઇન દોરીને જમીન માપણી અને GPS લોકેશન",
                    subtitle = "શેરબજારની લાઇનની જેમ નકશા પર બિંદુ મૂકીને દરેક બાજુનું સાચું માપ અને ક્ષેત્રફળ મેળવો",
                    badge = "લાઇવ મેપ"
                )
                "MAP_3D_PARCEL_VIEWER" -> LocalizedMenuEntry(
                    category = "જમીન માપણી, નકશા કટઆઉટ અને 2D/3D પ્લોટ",
                    title = "3D જમીન કટઆઉટ મોડેલ અને માટી ખોદકામ (Excavation)",
                    subtitle = "તમારી જમીનનો ટુકડો 3D માં ફેરવીને જુઓ અને પાયાના ખોદકામના ડમ્પર/ટ્રેક્ટર ફેરા ગણો",
                    badge = "3D કટઆઉટ"
                )
                "IRREGULAR_PLOT_CAD" -> LocalizedMenuEntry(
                    category = "જમીન માપણી, નકશા કટઆઉટ અને 2D/3D પ્લોટ",
                    title = "અનિયમિત ચાર ખૂણાના પ્લોટનો 2D CAD નકશો (ત્રિકોણમિતિ)",
                    subtitle = "જે પ્લોટના ચારેય ખૂણા સરખા ન હોય તેની ચારેય બાજુ અને કર્ણ પરથી ૧૦૦% સાચો નકશો",
                    badge = "2D CAD"
                )
                "CONTRACTOR_ERROR_DETECTOR" -> LocalizedMenuEntry(
                    category = "જમીન માપણી, નકશા કટઆઉટ અને 2D/3D પ્લોટ",
                    title = "કોન્ટ્રાક્ટરની ખોટી 'સરેરાશ માપણી' પકડનાર સાધન",
                    subtitle = "સામસામેની બાજુની સરેરાશ કાઢવાથી કેટલા ચોરસ ફૂટ અને કેટલા રૂપિયાનું નુકસાન થાય તે જુઓ",
                    badge = "છેતરપિંડી રોકો"
                )
                "CONSTRUCTION_TILES" -> LocalizedMenuEntry(
                    category = "બાંધકામ અને ઘર મટીરીયલ ગણતરી (Construction)",
                    title = "ભોંયતળિયાની ટાઇલ્સ, બોક્સ, સિમેન્ટ અને રેતી ગણતરી",
                    subtitle = "રૂમની લંબાઈ-પહોળાઈ પરથી કેટલી ટાઇલ્સ, કેટલા બોક્સ અને કેટલી સિમેન્ટ-રેતી જોશે",
                    badge = "ટાઇલ્સ"
                )
                "CONSTRUCTION_BRICKWORK" -> LocalizedMenuEntry(
                    category = "બાંધકામ અને ઘર મટીરીયલ ગણતરી (Construction)",
                    title = "દિવાલના ચણતરની ઈંટો, સિમેન્ટ અને રેતી ગણતરી",
                    subtitle = "૪.૫ ઇંચ કે ૯ ઇંચની દિવાલ માટે કેટલી ઈંટો, સિમેન્ટની થેલી અને રેતી જોશે",
                    badge = "ચણતર • ઈંટો"
                )
                "CONSTRUCTION_PLASTER" -> LocalizedMenuEntry(
                    category = "બાંધકામ અને ઘર મટીરીયલ ગણતરી (Construction)",
                    title = "દિવાલ અને છતના પ્લાસ્ટર માટે સિમેન્ટ અને રેતી ગણતરી",
                    subtitle = "૧૨mm, ૧૫mm કે ૨૦mm પ્લાસ્ટર માટે સિમેન્ટની થેલી અને રેતી (ઘન ફૂટ)",
                    badge = "પ્લાસ્ટર"
                )
                "CONSTRUCTION_PAINT" -> LocalizedMenuEntry(
                    category = "બાંધકામ અને ઘર મટીરીયલ ગણતરી (Construction)",
                    title = "દિવાલનો કલર (Paint), પ્રાઇમર અને પુટ્ટી (લીટર/કિલો) ગણતરી",
                    subtitle = "બારી-બારણાં બાદ કરીને કેટલા કિલો પુટ્ટી, કેટલું પ્રાઇમર અને કેટલા લીટર કલર જોશે",
                    badge = "કલર • પુટ્ટી"
                )
                "SAVED_SURVEY_VAULT" -> LocalizedMenuEntry(
                    category = "સેવ કરેલા રિપોર્ટ, ભાષા અને સેટિંગ્સ",
                    title = "સેવ કરેલા તમામ રિપોર્ટ અને PDF વોલ્ટ (ઓફલાઇન)",
                    subtitle = "તમે સેવ કરેલા જમીન, મંડી, મજૂરી અને બાંધકામના તમામ રિપોર્ટ જુઓ અને PDF કાઢો",
                    badge = "વોલ્ટ"
                )
                "REWARDED_1HR_AD_FREE" -> LocalizedMenuEntry(
                    category = "સેવ કરેલા રિપોર્ટ, ભાષા અને સેટિંગ્સ",
                    title = "૧ કલાક માટે પોપ-અપ એડ-ફ્રી પાસ (Rewarded Ad)",
                    subtitle = "૧ નાની એડ જોઈને ૬૦ મિનિટ માટે તમામ વચ્ચે આવતી Interstitial અને App Open એડ બંધ કરો",
                    badge = "૧-કલાક પાસ"
                )
                "LANGUAGE_SWITCHER_115" -> LocalizedMenuEntry(
                    category = "સેવ કરેલા રિપોર્ટ, ભાષા અને સેટિંગ્સ",
                    title = "એપની ભાષા બદલો (૧૧૫+ ગૂગલ ભાષાઓ)",
                    subtitle = "અંગ્રેજી, ગુજરાતી, હિન્દી, મરાઠી, તમિલ, જાપાનીઝ સહિત ૧૧૫+ ભાષાઓમાં એપ વાપરો",
                    badge = "૧૧૫+ ભાષા"
                )
                "LEGAL_AND_PRIVACY" -> LocalizedMenuEntry(
                    category = "સેવ કરેલા રિપોર્ટ, ભાષા અને સેટિંગ્સ",
                    title = "૧૩ કાયદાકીય, પ્રાઇવસી અને ઝીરો-પરમિશન પોલિસી",
                    subtitle = "પ્રાઇવસી પોલિસી, શરતો, ડિસ્ક્લેમર અને ૧૦૦% ઓફલાઇન સુરક્ષા માહિતી",
                    badge = "કાયદાકીય"
                )
                else -> LocalizedMenuEntry(defaultCategory, defaultTitle, defaultSubtitle, defaultBadge)
            }
        }
        if (langCode in setOf("hi", "mai", "doi", "kok", "brx", "ne", "mr")) {
            return when (destName) {
                "LIVE_APMC_MANDI_PRICES" -> LocalizedMenuEntry(
                    category = "दैनिक किसान, ज़मींदार व दलाल टूल्स (Daily Use)",
                    title = "लाइव क्षेत्रीय APMC मंडी भाव व विश्व के सरकारी कृषि बाज़ार भाव (ऑटो-अपडेट)",
                    subtitle = "गुजरात, महाराष्ट्र, मध्य प्रदेश, राजस्थान, पंजाब की मंडियां व अमेरिका/ब्राज़ील/यूरोप/जापान के सरकारी फसल भाव",
                    badge = "लाइव • मंडी"
                )
                "DAILY_MANDI_BILL" -> LocalizedMenuEntry(
                    category = "दैनिक किसान, ज़मींदार व दलाल टूल्स (Daily Use)",
                    title = "दैनिक मंडी तौल (20 किलो मण / क्विंटल), बारदान व फसल बिल",
                    subtitle = "बोरी/कट्टे, बारदान कटौती, मण व क्विंटल भाव तथा किसान को मिलने वाली शुद्ध राशि",
                    badge = "दैनिक • मंडी"
                )
                "DAILY_LABOUR_TRACTOR_KHATA" -> LocalizedMenuEntry(
                    category = "दैनिक किसान, ज़मींदार व दलाल टूल्स (Daily Use)",
                    title = "दैनिक खेत-मज़दूरी (हाज़िरी), उधार (उपाड़) व ट्रैक्टर भाड़ा खाता",
                    subtitle = "मज़दूरों के दिन, दिहाड़ी, ट्रैक्टर घंटे का किराया और एडवांस घटाकर शुद्ध हिसाब",
                    badge = "दैनिक • मज़दूरी"
                )
                "DAILY_LAND_BROKER_DEAL" -> LocalizedMenuEntry(
                    category = "दैनिक किसान, ज़मींदार व दलाल टूल्स (Daily Use)",
                    title = "ज़मीन खरीद-बिक्री दलाली (Brokerage), टोकन व बयाना/बानाखत किस्त",
                    subtitle = "प्रॉपर्टी डीलर व ज़मींदारों के लिए 1%-2% दलाली, टोकन, बयाना और रजिस्ट्री भुगतान शेड्यूल",
                    badge = "दैनिक • दलाली"
                )
                "DAILY_FARM_DAIRY_ROJMEL" -> LocalizedMenuEntry(
                    category = "दैनिक किसान, ज़मींदार व दलाल टूल्स (Daily Use)",
                    title = "दैनिक खेती व डेयरी (दूध) आय-व्यय रोज़मेल (Daily Rojmel)",
                    subtitle = "दूध लीटर व सब्ज़ी आय बनाम पशु चारा, डीज़ल और दैनिक खर्च का शुद्ध मुनाफ़ा",
                    badge = "दैनिक • रोज़मेल"
                )
                "KHET_TALAVDI_WATER_TANK" -> LocalizedMenuEntry(
                    category = "किसान, जल व 7/12 राजस्व टूल्स",
                    title = "खेत-तालाब (खेत-तलावड़ी) व पानी टंकी क्षमता (लीटर में)",
                    subtitle = "तालाब या टंकी में कितने लीटर पानी आएगा और कितने घंटे सिंचाई चलेगी",
                    badge = "खेत-तालाब"
                )
                "CIRCULAR_WELL_BOREWELL_HP" -> LocalizedMenuEntry(
                    category = "किसान, जल व 7/12 राजस्व टूल्स",
                    title = "गोल कुआं जल क्षमता और बोरवेल सबमर्सिबल मोटर HP",
                    subtitle = "कुएं में पानी (लीटर) और बोरवेल गहराई के अनुसार सही मोटर HP",
                    badge = "कुआं • मोटर HP"
                )
                "REVENUE_712_DECODER" -> LocalizedMenuEntry(
                    category = "किसान, जल व 7/12 राजस्व टूल्स",
                    title = "7/12 व 8-अ खतौनी (हेक्टेयर-आर-वर्ग मीटर) डिकोडर",
                    subtitle = "सरकारी 7/12 के हेक्टेयर-आर-वर्ग मीटर को बीघा, गुंठा, वार और एकड़ में बदलें",
                    badge = "7/12 उतारा"
                )
                "KHEDUT_LAND_PARTITION" -> LocalizedMenuEntry(
                    category = "किसान, जल व 7/12 राजस्व टूल्स",
                    title = "पारिवारिक भूमि बंटवारा (वारिस / हिस्सेदार विभाजक)",
                    subtitle = "रास्ता कटौती के बाद 2 से 12 हिस्सेदारों में बराबर ज़मीन व फ्रंटेज बांटें",
                    badge = "बंटवारा"
                )
                "KHEDUT_FARM_FENCING" -> LocalizedMenuEntry(
                    category = "किसान, जल व 7/12 राजस्व टूल्स",
                    title = "खेत की कंटीले तार की बाड़ (Fencing) व खंभे खर्च",
                    subtitle = "सीमेंट/लोहे के खंभे, कंटीले तार का वज़न (किलो) और कुल लागत",
                    badge = "तार बाड़"
                )
                "KHEDUT_CROP_CALCULATOR" -> LocalizedMenuEntry(
                    category = "किसान, जल व 7/12 राजस्व टूल्स",
                    title = "फसल बीज, यूरिया, DAP, पोटाश व अनुमानित उत्पादन",
                    subtitle = "गेहूं, कपास, मूंगफली, जीरा आदि के लिए बीज, खाद बोरी व मण/क्विंटल उपज",
                    badge = "फसल • खाद"
                )
                "LAND_UNIT_CONVERTER" -> LocalizedMenuEntry(
                    category = "भूमि मापन, मैप कटआउट व 2D/3D प्लॉट",
                    title = "भारत व विश्व के देशों-राज्यों अनुसार भूमि इकाई कन्वर्टर",
                    subtitle = "बीघा, गुंठा, गज, वर्ग फुट, मरला, कनाल, एकड़, हेक्टेयर व अंतरराष्ट्रीय इकाइयां",
                    badge = "मुख्य पेज"
                )
                "LAND_STAMP_DUTY_VALUATION" -> LocalizedMenuEntry(
                    category = "भूमि मापन, मैप कटआउट व 2D/3D प्लॉट",
                    title = "भूमि मूल्यांकन, सर्कल रेट व स्टाम्प ड्यूटी गणना",
                    subtitle = "ज़मीन की कीमत, स्टाम्प ड्यूटी व रजिस्ट्रेशन खर्च",
                    badge = "स्टाम्प ड्यूटी"
                )
                "MAP_SATELLITE_LINE_SURVEY" -> LocalizedMenuEntry(
                    category = "भूमि मापन, मैप कटआउट व 2D/3D प्लॉट",
                    title = "लाइव मैप लाइन सर्वेक्षण व GPS कोऑर्डिनेट मापन",
                    subtitle = "नक्शे पर लाइन खींचकर हर भुजा की लंबाई और सटीक क्षेत्रफल जानें",
                    badge = "लाइव मैप"
                )
                "MAP_3D_PARCEL_VIEWER" -> LocalizedMenuEntry(
                    category = "भूमि मापन, मैप कटआउट व 2D/3D प्लॉट",
                    title = "3D भूमि पार्सल कटआउट और मिट्टी खुदाई (Excavation)",
                    subtitle = "अपनी ज़मीन को 3D में घुमाकर देखें और नींव खुदाई के डंपर/ट्रैक्टर ट्रॉली गिनें",
                    badge = "3D कटआउट"
                )
                "IRREGULAR_PLOT_CAD" -> LocalizedMenuEntry(
                    category = "भूमि मापन, मैप कटआउट व 2D/3D प्लॉट",
                    title = "4-भुजाओं वाले अनियमित प्लॉट का 2D CAD नक्शा",
                    subtitle = "तिरछे प्लॉट की चारों भुजाओं और विकर्ण से सटीक क्षेत्रफल व नक्शा",
                    badge = "2D CAD"
                )
                "CONTRACTOR_ERROR_DETECTOR" -> LocalizedMenuEntry(
                    category = "भूमि मापन, मैप कटआउट व 2D/3D प्लॉट",
                    title = "ठेकेदार 'औसत फ़ॉर्मूला' त्रुटि जांचकर्ता",
                    subtitle = "गलत औसत फ़ॉर्मूले से होने वाले वर्ग फुट और पैसों के नुकसान से बचें",
                    badge = "धोखाधड़ी रोकें"
                )
                "CONSTRUCTION_TILES" -> LocalizedMenuEntry(
                    category = "निर्माण सामग्री गणना (Construction BOQ)",
                    title = "फर्श टाइल्स, बॉक्स, सीमेंट और रेत गणना",
                    subtitle = "कमरे की लंबाई-चौड़ाई से कुल टाइल्स, बॉक्स और मसाला सामग्री",
                    badge = "टाइल्स"
                )
                "CONSTRUCTION_BRICKWORK" -> LocalizedMenuEntry(
                    category = "निर्माण सामग्री गणना (Construction BOQ)",
                    title = "दीवार चिनाई की ईंटें, सीमेंट और रेत गणना",
                    subtitle = "4.5 इंच या 9 इंच दीवार के लिए ईंटों की संख्या और सीमेंट-रेत",
                    badge = "ईंट चिनाई"
                )
                "CONSTRUCTION_PLASTER" -> LocalizedMenuEntry(
                    category = "निर्माण सामग्री गणना (Construction BOQ)",
                    title = "दीवार व छत प्लास्टर सीमेंट और रेत गणना",
                    subtitle = "12mm/15mm/20mm प्लास्टर के लिए सीमेंट बैग और रेत CFT",
                    badge = "प्लास्टर"
                )
                "CONSTRUCTION_PAINT" -> LocalizedMenuEntry(
                    category = "निर्माण सामग्री गणना (Construction BOQ)",
                    title = "दीवार पेंट, प्राइमर और पुट्टी (लीटर/किलो) गणना",
                    subtitle = "खिड़की-दरवाजे घटाकर पुट्टी, प्राइमर और पेंट की सटीक मात्रा",
                    badge = "पेंट • पुट्टी"
                )
                "SAVED_SURVEY_VAULT" -> LocalizedMenuEntry(
                    category = "सेव रिपोर्ट, भाषा व सेटिंग्स",
                    title = "सेव किए गए सर्वे व रोज़मेल वॉल्ट (ऑफ़लाइन)",
                    subtitle = "सभी सेव किए गए भूमि, मंडी, मज़दूरी व निर्माण रिकॉर्ड देखें व PDF निकालें",
                    badge = "वॉल्ट"
                )
                "REWARDED_1HR_AD_FREE" -> LocalizedMenuEntry(
                    category = "सेव रिपोर्ट, भाषा व सेटिंग्स",
                    title = "1 घंटे के लिए पॉप-अप ऐड-फ़्री पास (Rewarded Ad)",
                    subtitle = "1 छोटा वीडियो देखकर 60 मिनट के लिए सभी Interstitial व App Open विज्ञापन बंद करें",
                    badge = "1-घंटा पास"
                )
                "LANGUAGE_SWITCHER_115" -> LocalizedMenuEntry(
                    category = "सेव रिपोर्ट, भाषा व सेटिंग्स",
                    title = "ऐप की भाषा बदलें (115+ गूगल भाषाएं)",
                    subtitle = "अंग्रेज़ी, गुजराती, हिंदी, मराठी, तमिल, जापानी सहित 115+ भाषाएं",
                    badge = "115+ भाषाएं"
                )
                "LEGAL_AND_PRIVACY" -> LocalizedMenuEntry(
                    category = "सेव रिपोर्ट, भाषा व सेटिंग्स",
                    title = "13 कानूनी, गोपनीयता व ज़ीरो-परमिशन नीतियां",
                    subtitle = "प्राइवेसी पॉलिसी, नियम व शर्तें और 100% ऑफ़लाइन डेटा सुरक्षा",
                    badge = "कानूनी"
                )
                else -> LocalizedMenuEntry(defaultCategory, defaultTitle, defaultSubtitle, defaultBadge)
            }
        }
        val lang = allLanguages.find { it.code == langCode }
        val prefix = if (lang != null) "[${lang.nativeName}] " else ""
        return LocalizedMenuEntry(
            category = "$prefix$defaultCategory",
            title = "$prefix$defaultTitle",
            subtitle = defaultSubtitle,
            badge = defaultBadge
        )
    }
}

