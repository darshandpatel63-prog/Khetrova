package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SurveyRecordEntity
import com.example.data.repository.SurveyRepository
import com.example.domain.ApmcRegionZone
import com.example.domain.ApmcYardProfile
import com.example.domain.AppLanguage
import com.example.domain.AppLocalizationEngine
import com.example.domain.ConstructionBoqResult
import com.example.domain.ConstructionModuleType
import com.example.domain.DailyFarmDairyRojmelResult
import com.example.domain.DailyLabourTractorKhataResult
import com.example.domain.DailyMandiBillResult
import com.example.domain.FarmerAndLandToolsEngine
import com.example.domain.GeodesicMapAnd3DEngine
import com.example.domain.GeodesicPolygonSurveyResult
import com.example.domain.GpsLatLng
import com.example.domain.IrregularPlotResult
import com.example.domain.LandAndConstructionEngine
import com.example.domain.LandConversionResult
import com.example.domain.LandDealBrokerageResult
import com.example.domain.LandUnitDefinition
import com.example.domain.LiveApmcMarketEngine
import com.example.domain.LiveApmcSyncSnapshot
import com.example.domain.LiveCropPriceQuote
import com.example.domain.LocalizedUiStrings
import com.example.domain.PdfReportGenerator
import com.example.domain.Revenue712DecodeResult
import com.example.domain.StateLandProfile
import com.example.domain.WaterTankAndBorewellResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val route: String, val label: String) {
    LAND_CONVERTER("land_converter", "Land Units"),
    MAP_3D_CUT("map_3d_cut", "Map & 3D"),
    IRREGULAR_PLOT("irregular_plot", "Plot CAD"),
    CONSTRUCTION("construction_boq", "Materials"),
    VAULT_SYSTEM("vault_system", "Daily & Vault")
}

enum class VaultSubSection {
    DAILY_ROJMEL_MANDI_BROKER,
    FARM_712_AND_WATER_TOOLS,
    SAVED_VAULT,
    LEGAL_COMPLIANCE
}

enum class QuickFeatureDestination {
    LIVE_APMC_MANDI_PRICES,
    DAILY_MANDI_BILL,
    DAILY_LABOUR_TRACTOR_KHATA,
    DAILY_LAND_BROKER_DEAL,
    DAILY_FARM_DAIRY_ROJMEL,
    KHET_TALAVDI_WATER_TANK,
    CIRCULAR_WELL_BOREWELL_HP,
    REVENUE_712_DECODER,
    KHEDUT_LAND_PARTITION,
    KHEDUT_FARM_FENCING,
    KHEDUT_CROP_CALCULATOR,
    LAND_UNIT_CONVERTER,
    LAND_STAMP_DUTY_VALUATION,
    MAP_SATELLITE_LINE_SURVEY,
    MAP_3D_PARCEL_VIEWER,
    IRREGULAR_PLOT_CAD,
    CONTRACTOR_ERROR_DETECTOR,
    CONSTRUCTION_TILES,
    CONSTRUCTION_BRICKWORK,
    CONSTRUCTION_PLASTER,
    CONSTRUCTION_PAINT,
    SAVED_SURVEY_VAULT,
    REWARDED_1HR_AD_FREE,
    LANGUAGE_SWITCHER_115,
    LEGAL_AND_PRIVACY,
    HOW_TO_USE_APP_MANUAL
}

class MainViewModel(
    private val repository: SurveyRepository,
    initialLanguageCode: String? = "en",
    hasCompletedLanguageOnboarding: Boolean = false,
    initialLaunchCount: Int = 1,
    initialAdFreeUntilEpochMs: Long = 0L
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(AppTab.LAND_CONVERTER)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    // --- All-Features Top Menu & Deep-Link Sub-Screen Navigation State ---
    private val _isAllFeaturesMenuOpen = MutableStateFlow(false)
    val isAllFeaturesMenuOpen: StateFlow<Boolean> = _isAllFeaturesMenuOpen.asStateFlow()

    private val _isHowToUseManualOpen = MutableStateFlow(false)
    val isHowToUseManualOpen: StateFlow<Boolean> = _isHowToUseManualOpen.asStateFlow()

    private val _selectedVaultSubSection = MutableStateFlow(VaultSubSection.DAILY_ROJMEL_MANDI_BROKER)
    val selectedVaultSubSection: StateFlow<VaultSubSection> = _selectedVaultSubSection.asStateFlow()

    private val _selectedKhedutTab = MutableStateFlow(0)
    val selectedKhedutTab: StateFlow<Int> = _selectedKhedutTab.asStateFlow()

    private val _vaultScrollTargetIndex = MutableStateFlow<Int?>(null)
    val vaultScrollTargetIndex: StateFlow<Int?> = _vaultScrollTargetIndex.asStateFlow()

    private val _mapScrollTargetIndex = MutableStateFlow<Int?>(null)
    val mapScrollTargetIndex: StateFlow<Int?> = _mapScrollTargetIndex.asStateFlow()

    // --- Global 115+ Language Localization State + First-Launch Persistent Gate (Default = English "en") ---
    private val initialLangObj = AppLocalizationEngine.allLanguages.find { it.code == (initialLanguageCode ?: "en") }
        ?: AppLocalizationEngine.allLanguages.first()

    private val _selectedLanguage = MutableStateFlow(initialLangObj)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _uiStrings = MutableStateFlow(AppLocalizationEngine.getStringsForLanguage(initialLangObj.code))
    val uiStrings: StateFlow<LocalizedUiStrings> = _uiStrings.asStateFlow()

    private val _isFirstLaunchLanguageGateOpen = MutableStateFlow(!hasCompletedLanguageOnboarding)
    val isFirstLaunchLanguageGateOpen: StateFlow<Boolean> = _isFirstLaunchLanguageGateOpen.asStateFlow()

    private val _isLanguageDialogOpen = MutableStateFlow(false)
    val isLanguageDialogOpen: StateFlow<Boolean> = _isLanguageDialogOpen.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    // --- Google AdMob Monetization & UX-Safe State ---
    // Banner & Native Ads are ALWAYS shown on screens (even during the 1-Hour Rewarded Ad-Free pass).
    // Interstitial Ads & App Open Ads are suppressed for 1 Hour (60 mins) after watching a Rewarded Ad.
    private val _appLaunchCount = MutableStateFlow(initialLaunchCount)
    val appLaunchCount: StateFlow<Int> = _appLaunchCount.asStateFlow()

    private val _adFreeUntilEpochMs = MutableStateFlow(initialAdFreeUntilEpochMs)
    val adFreeUntilEpochMs: StateFlow<Long> = _adFreeUntilEpochMs.asStateFlow()

    private val _adFreeRemainingMinutes = MutableStateFlow(computeRemainingAdFreeMinutes(initialAdFreeUntilEpochMs))
    val adFreeRemainingMinutes: StateFlow<Int> = _adFreeRemainingMinutes.asStateFlow()

    private val _isInterstitialAdVisible = MutableStateFlow(false)
    val isInterstitialAdVisible: StateFlow<Boolean> = _isInterstitialAdVisible.asStateFlow()

    private val _interstitialTriggerReason = MutableStateFlow("Survey Task Completed")
    val interstitialTriggerReason: StateFlow<String> = _interstitialTriggerReason.asStateFlow()

    private val _isRewardedAdVisible = MutableStateFlow(false)
    val isRewardedAdVisible: StateFlow<Boolean> = _isRewardedAdVisible.asStateFlow()

    private val _isAppOpenAdVisible = MutableStateFlow(false)
    val isAppOpenAdVisible: StateFlow<Boolean> = _isAppOpenAdVisible.asStateFlow()

    private var lastInterstitialShownEpochMs: Long = 0L

    // --- Tab 1: Regional & Global Country Land Unit Converter State ---
    private val _selectedStateProfile = MutableStateFlow(LandAndConstructionEngine.stateProfiles.first())
    val selectedStateProfile: StateFlow<StateLandProfile> = _selectedStateProfile.asStateFlow()

    private val _selectedInputUnit = MutableStateFlow(LandAndConstructionEngine.stateProfiles.first().units.first())
    val selectedInputUnit: StateFlow<LandUnitDefinition> = _selectedInputUnit.asStateFlow()

    private val _landInputValueText = MutableStateFlow("2.5")
    val landInputValueText: StateFlow<String> = _landInputValueText.asStateFlow()

    private val _landRatePerUnitText = MutableStateFlow("1250000")
    val landRatePerUnitText: StateFlow<String> = _landRatePerUnitText.asStateFlow()

    private val _landConversionResult = MutableStateFlow(computeCurrentLandConversion())
    val landConversionResult: StateFlow<LandConversionResult> = _landConversionResult.asStateFlow()

    // --- Tab 2: User-entered location and user-drawn land survey (no preloaded demo parcel) ---
    private val _mapDisplayCenter = MutableStateFlow<GpsLatLng?>(null)
    val mapDisplayCenter: StateFlow<GpsLatLng?> = _mapDisplayCenter.asStateFlow()

    private val _mapPolygonPoints = MutableStateFlow<List<GpsLatLng>>(emptyList())
    val mapPolygonPoints: StateFlow<List<GpsLatLng>> = _mapPolygonPoints.asStateFlow()

    private val _googleMapsInputText = MutableStateFlow("")
    val googleMapsInputText: StateFlow<String> = _googleMapsInputText.asStateFlow()

    private val _mapPolygonSurveyResult = MutableStateFlow(computeCurrentMapSurvey())
    val mapPolygonSurveyResult: StateFlow<GeodesicPolygonSurveyResult> = _mapPolygonSurveyResult.asStateFlow()

    // --- Tab 3: Irregular 4-Sided Plot CAD State ---
    private val _northSideText = MutableStateFlow("")
    val northSideText: StateFlow<String> = _northSideText.asStateFlow()

    private val _eastSideText = MutableStateFlow("")
    val eastSideText: StateFlow<String> = _eastSideText.asStateFlow()

    private val _southSideText = MutableStateFlow("")
    val southSideText: StateFlow<String> = _southSideText.asStateFlow()

    private val _westSideText = MutableStateFlow("")
    val westSideText: StateFlow<String> = _westSideText.asStateFlow()

    private val _diagonalSideText = MutableStateFlow("")
    val diagonalSideText: StateFlow<String> = _diagonalSideText.asStateFlow()

    private val _plotRatePerSqFtText = MutableStateFlow("")
    val plotRatePerSqFtText: StateFlow<String> = _plotRatePerSqFtText.asStateFlow()

    private val _irregularPlotResult = MutableStateFlow(computeCurrentIrregularPlot())
    val irregularPlotResult: StateFlow<IrregularPlotResult> = _irregularPlotResult.asStateFlow()

    // --- Tab 4: Construction Material Estimator State ---
    private val _selectedConstructionModule = MutableStateFlow(ConstructionModuleType.TILES_FLOORING)
    val selectedConstructionModule: StateFlow<ConstructionModuleType> = _selectedConstructionModule.asStateFlow()

    private val _roomLengthText = MutableStateFlow("24")
    val roomLengthText: StateFlow<String> = _roomLengthText.asStateFlow()

    private val _roomWidthOrHeightText = MutableStateFlow("15")
    val roomWidthOrHeightText: StateFlow<String> = _roomWidthOrHeightText.asStateFlow()

    private val _tileLengthFt = MutableStateFlow(2.0)
    val tileLengthFt: StateFlow<Double> = _tileLengthFt.asStateFlow()

    private val _tileWidthFt = MutableStateFlow(2.0)
    val tileWidthFt: StateFlow<Double> = _tileWidthFt.asStateFlow()

    private val _wastagePercent = MutableStateFlow(7.0)
    val wastagePercent: StateFlow<Double> = _wastagePercent.asStateFlow()

    private val _wallThicknessInches = MutableStateFlow(9.0)
    val wallThicknessInches: StateFlow<Double> = _wallThicknessInches.asStateFlow()

    private val _plasterThicknessMm = MutableStateFlow(12.0)
    val plasterThicknessMm: StateFlow<Double> = _plasterThicknessMm.asStateFlow()

    private val _deductionSqFtText = MutableStateFlow("0")
    val deductionSqFtText: StateFlow<String> = _deductionSqFtText.asStateFlow()

    private val _cementRateText = MutableStateFlow("390")
    val cementRateText: StateFlow<String> = _cementRateText.asStateFlow()

    private val _sandRateText = MutableStateFlow("55")
    val sandRateText: StateFlow<String> = _sandRateText.asStateFlow()

    private val _tileRateText = MutableStateFlow("48")
    val tileRateText: StateFlow<String> = _tileRateText.asStateFlow()

    private val _brickRateText = MutableStateFlow("8.5")
    val brickRateText: StateFlow<String> = _brickRateText.asStateFlow()

    private val _paintRateText = MutableStateFlow("320")
    val paintRateText: StateFlow<String> = _paintRateText.asStateFlow()

    private val _constructionBoqResult = MutableStateFlow(computeCurrentConstructionBoq())
    val constructionBoqResult: StateFlow<ConstructionBoqResult> = _constructionBoqResult.asStateFlow()

    // --- Tab 5: New 7/12 Revenue Extract Decoder & Farm Water Tank / Borewell Pump Calculator ---
    private val _hectares712Text = MutableStateFlow("1")
    val hectares712Text: StateFlow<String> = _hectares712Text.asStateFlow()

    private val _are712Text = MutableStateFlow("45")
    val are712Text: StateFlow<String> = _are712Text.asStateFlow()

    private val _sqMeters712Text = MutableStateFlow("68")
    val sqMeters712Text: StateFlow<String> = _sqMeters712Text.asStateFlow()

    private val _revenue712Result = MutableStateFlow(computeCurrent712Decode())
    val revenue712Result: StateFlow<Revenue712DecodeResult> = _revenue712Result.asStateFlow()

    private val _isCircularWell = MutableStateFlow(false)
    val isCircularWell: StateFlow<Boolean> = _isCircularWell.asStateFlow()

    private val _tankLengthOrDiameterFtText = MutableStateFlow("20")
    val tankLengthOrDiameterFtText: StateFlow<String> = _tankLengthOrDiameterFtText.asStateFlow()

    private val _tankWidthFtText = MutableStateFlow("15")
    val tankWidthFtText: StateFlow<String> = _tankWidthFtText.asStateFlow()

    private val _tankDepthFtText = MutableStateFlow("10")
    val tankDepthFtText: StateFlow<String> = _tankDepthFtText.asStateFlow()

    private val _borewellDepthFtText = MutableStateFlow("320")
    val borewellDepthFtText: StateFlow<String> = _borewellDepthFtText.asStateFlow()

    private val _waterTankResult = MutableStateFlow(computeCurrentWaterTank())
    val waterTankResult: StateFlow<WaterTankAndBorewellResult> = _waterTankResult.asStateFlow()

    // --- Daily-Use Suite 1: APMC Mandi Weight (20 Kg Man / Quintal), Bardaan & Crop Bill ---
    private val _mandiBagsText = MutableStateFlow("45")
    val mandiBagsText: StateFlow<String> = _mandiBagsText.asStateFlow()

    private val _mandiBagWeightKgText = MutableStateFlow("50")
    val mandiBagWeightKgText: StateFlow<String> = _mandiBagWeightKgText.asStateFlow()

    private val _mandiBardaanKgText = MutableStateFlow("0.6")
    val mandiBardaanKgText: StateFlow<String> = _mandiBardaanKgText.asStateFlow()

    private val _mandiRatePerManText = MutableStateFlow("1520")
    val mandiRatePerManText: StateFlow<String> = _mandiRatePerManText.asStateFlow()

    private val _mandiCommissionPctText = MutableStateFlow("1.0")
    val mandiCommissionPctText: StateFlow<String> = _mandiCommissionPctText.asStateFlow()

    private val _mandiHamaliPerBagText = MutableStateFlow("8")
    val mandiHamaliPerBagText: StateFlow<String> = _mandiHamaliPerBagText.asStateFlow()

    private val _dailyMandiBillResult = MutableStateFlow(computeCurrentMandiBill())
    val dailyMandiBillResult: StateFlow<DailyMandiBillResult> = _dailyMandiBillResult.asStateFlow()

    // --- Daily-Use Suite 2: Daily Farm Labour (Dadiya), Tractor Hours & Upaad (Advance) Khata ---
    private val _labourWorkersCountText = MutableStateFlow("6")
    val labourWorkersCountText: StateFlow<String> = _labourWorkersCountText.asStateFlow()

    private val _labourDaysWorkedText = MutableStateFlow("5")
    val labourDaysWorkedText: StateFlow<String> = _labourDaysWorkedText.asStateFlow()

    private val _labourDailyWageText = MutableStateFlow("400")
    val labourDailyWageText: StateFlow<String> = _labourDailyWageText.asStateFlow()

    private val _tractorHoursText = MutableStateFlow("4.5")
    val tractorHoursText: StateFlow<String> = _tractorHoursText.asStateFlow()

    private val _tractorRatePerHourText = MutableStateFlow("900")
    val tractorRatePerHourText: StateFlow<String> = _tractorRatePerHourText.asStateFlow()

    private val _upaadAdvanceText = MutableStateFlow("3500")
    val upaadAdvanceText: StateFlow<String> = _upaadAdvanceText.asStateFlow()

    private val _dailyLabourKhataResult = MutableStateFlow(computeCurrentLabourKhata())
    val dailyLabourKhataResult: StateFlow<DailyLabourTractorKhataResult> = _dailyLabourKhataResult.asStateFlow()

    // --- Daily-Use Suite 3: Land Broker / Real Estate Deal Commission, Token & Banakhat Schedule ---
    private val _dealTotalAmountText = MutableStateFlow("4500000")
    val dealTotalAmountText: StateFlow<String> = _dealTotalAmountText.asStateFlow()

    private val _dealTokenAmountText = MutableStateFlow("100000")
    val dealTokenAmountText: StateFlow<String> = _dealTokenAmountText.asStateFlow()

    private val _dealBanakhatPctText = MutableStateFlow("25")
    val dealBanakhatPctText: StateFlow<String> = _dealBanakhatPctText.asStateFlow()

    private val _dealBuyerBrokeragePctText = MutableStateFlow("1.0")
    val dealBuyerBrokeragePctText: StateFlow<String> = _dealBuyerBrokeragePctText.asStateFlow()

    private val _dealSellerBrokeragePctText = MutableStateFlow("1.0")
    val dealSellerBrokeragePctText: StateFlow<String> = _dealSellerBrokeragePctText.asStateFlow()

    private val _landDealBrokerageResult = MutableStateFlow(computeCurrentLandDealBrokerage())
    val landDealBrokerageResult: StateFlow<LandDealBrokerageResult> = _landDealBrokerageResult.asStateFlow()

    // --- Daily-Use Suite 4: Daily Farm, Dairy (Milk) & Landowner Rojmel (Income vs Expense) ---
    private val _rojmelMilkLitersText = MutableStateFlow("28")
    val rojmelMilkLitersText: StateFlow<String> = _rojmelMilkLitersText.asStateFlow()

    private val _rojmelMilkRateText = MutableStateFlow("62")
    val rojmelMilkRateText: StateFlow<String> = _rojmelMilkRateText.asStateFlow()

    private val _rojmelOtherIncomeText = MutableStateFlow("850")
    val rojmelOtherIncomeText: StateFlow<String> = _rojmelOtherIncomeText.asStateFlow()

    private val _rojmelCattleFeedExpText = MutableStateFlow("520")
    val rojmelCattleFeedExpText: StateFlow<String> = _rojmelCattleFeedExpText.asStateFlow()

    private val _rojmelDieselExpText = MutableStateFlow("400")
    val rojmelDieselExpText: StateFlow<String> = _rojmelDieselExpText.asStateFlow()

    private val _rojmelMiscExpText = MutableStateFlow("250")
    val rojmelMiscExpText: StateFlow<String> = _rojmelMiscExpText.asStateFlow()

    private val _dailyRojmelResult = MutableStateFlow(computeCurrentDailyRojmel())
    val dailyRojmelResult: StateFlow<DailyFarmDairyRojmelResult> = _dailyRojmelResult.asStateFlow()

    // --- Daily-Use Suite 0: Live Regional APMC & Global Government Market Prices (Auto-Sync) ---
    private val _selectedApmcRegionZone = MutableStateFlow(ApmcRegionZone.SAURASHTRA_GUJARAT)
    val selectedApmcRegionZone: StateFlow<ApmcRegionZone> = _selectedApmcRegionZone.asStateFlow()

    private val _selectedApmcYard = MutableStateFlow(LiveApmcMarketEngine.getYardsByZone(ApmcRegionZone.SAURASHTRA_GUJARAT).first())
    val selectedApmcYard: StateFlow<ApmcYardProfile> = _selectedApmcYard.asStateFlow()

    private val _apmcCropSearchQuery = MutableStateFlow("")
    val apmcCropSearchQuery: StateFlow<String> = _apmcCropSearchQuery.asStateFlow()

    private val _apmcShowQuintalMode = MutableStateFlow(false)
    val apmcShowQuintalMode: StateFlow<Boolean> = _apmcShowQuintalMode.asStateFlow()

    private val _isSyncingLiveApmc = MutableStateFlow(false)
    val isSyncingLiveApmc: StateFlow<Boolean> = _isSyncingLiveApmc.asStateFlow()

    private val _liveApmcSyncSnapshot = MutableStateFlow(LiveApmcMarketEngine.getInstantCalibratedSnapshot())
    val liveApmcSyncSnapshot: StateFlow<LiveApmcSyncSnapshot> = _liveApmcSyncSnapshot.asStateFlow()

    // --- Saved Vault Records ---
    val savedRecords: StateFlow<List<SurveyRecordEntity>> = repository.allRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.seedInitialRecordsIfEmpty()
        }
        // Automatically fetch official daily Indian APMC records on app open
        syncLiveApmcMarketPrices(isSilentStartup = true)
        // Periodic ticker to keep the 1-Hour Rewarded Ad-Free countdown accurate
        viewModelScope.launch {
            while (true) {
                _adFreeRemainingMinutes.value = computeRemainingAdFreeMinutes(_adFreeUntilEpochMs.value)
                delay(15_000L)
            }
        }
        // Check if this is the 6th app open (after 5 opens -> 6th open triggers App Open Ad in 2 seconds)
        scheduleAppOpenAdIfEligible(initialLaunchCount)
    }

    private fun computeRemainingAdFreeMinutes(expiryMs: Long): Int {
        val diff = expiryMs - System.currentTimeMillis()
        return if (diff <= 0L) 0 else ((diff + 59_999L) / 60_000L).toInt()
    }

    fun isInterstitialAndAppOpenAdFreeActive(): Boolean {
        return System.currentTimeMillis() < _adFreeUntilEpochMs.value
    }

    /**
     * Rule requested by user:
     * After 5 times app open, on the 6th time when the app opens, trigger App Open Ad within 2 seconds (2000ms),
     * unless the 1-Hour Rewarded Ad-Free Pass is active.
     */
    private fun scheduleAppOpenAdIfEligible(launchCount: Int) {
        if (launchCount >= 6 && launchCount % 6 == 0 && !isInterstitialAndAppOpenAdFreeActive()) {
            viewModelScope.launch {
                delay(2000L) // Exactly 2 seconds after app opens
                if (!isInterstitialAndAppOpenAdFreeActive() && !_isFirstLaunchLanguageGateOpen.value) {
                    _isAppOpenAdVisible.value = true
                }
            }
        }
    }

    /**
     * Allows testing the 6th App Open Ad rule (2-second delay) directly from the UI or lifecycle.
     */
    fun simulateSixthAppOpenWithTwoSecondDelay(context: Context? = null) {
        val nextSixth = ((_appLaunchCount.value / 6) + 1) * 6
        _appLaunchCount.value = nextSixth
        if (context != null) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putInt(KEY_APP_LAUNCH_COUNT, nextSixth).apply()
        }
        if (isInterstitialAndAppOpenAdFreeActive()) {
            _statusBannerMessage.value = "1-Hour Rewarded Ad-Free Pass is active! App Open Ad is suppressed."
            return
        }
        _statusBannerMessage.value = "App Open #${nextSixth} simulated — App Open Test Ad will appear in 2 seconds..."
        viewModelScope.launch {
            delay(2000L)
            if (!isInterstitialAndAppOpenAdFreeActive()) {
                _isAppOpenAdVisible.value = true
            }
        }
    }

    fun dismissAppOpenAd() {
        _isAppOpenAdVisible.value = false
    }

    /**
     * Triggers an AdMob Test Interstitial Ad after completing a task (Save to Vault, Export PDF, Send to CAD),
     * respecting:
     * 1) 1-Hour Rewarded Ad-Free Pass (if active, Interstitial is skipped)
     * 2) UX-Friendly Cooldown (minimum 45 seconds between natural Interstitials so UI/UX is never ruined)
     */
    fun triggerInterstitialAfterTaskCompletion(taskReason: String, forceForTest: Boolean = false) {
        if (isInterstitialAndAppOpenAdFreeActive() && !forceForTest) {
            return
        }
        val now = System.currentTimeMillis()
        if (!forceForTest && (now - lastInterstitialShownEpochMs) < 45_000L) {
            return
        }
        lastInterstitialShownEpochMs = now
        _interstitialTriggerReason.value = taskReason
        _isInterstitialAdVisible.value = true
    }

    fun dismissInterstitialAd() {
        _isInterstitialAdVisible.value = false
    }

    fun openRewardedAdDialog() {
        _isRewardedAdVisible.value = true
    }

    fun dismissRewardedAdDialog() {
        _isRewardedAdVisible.value = false
    }

    /**
     * Grants 1 Hour (60 minutes) of Ad-Free experience from Interstitial & App Open ads
     * while keeping Banner Ads and Native Ads active as requested.
     */
    fun grantOneHourAdFreeReward(context: Context? = null) {
        val oneHourFromNow = System.currentTimeMillis() + 3_600_000L
        _adFreeUntilEpochMs.value = oneHourFromNow
        _adFreeRemainingMinutes.value = 60
        _isRewardedAdVisible.value = false
        _isInterstitialAdVisible.value = false
        _isAppOpenAdVisible.value = false
        if (context != null) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putLong(KEY_AD_FREE_UNTIL_MS, oneHourFromNow).apply()
        }
        _statusBannerMessage.value = "1-Hour Ad-Free Pass Activated! Interstitial & App Open Ads disabled for 60 mins (Banner & Native Ads stay active)."
    }

    fun clearOneHourAdFreePassForTesting(context: Context? = null) {
        _adFreeUntilEpochMs.value = 0L
        _adFreeRemainingMinutes.value = 0
        if (context != null) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putLong(KEY_AD_FREE_UNTIL_MS, 0L).apply()
        }
        _statusBannerMessage.value = "1-Hour Ad-Free Pass reset. Interstitial & App Open Test Ads re-enabled."
    }

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun setAllFeaturesMenuOpen(open: Boolean) {
        _isAllFeaturesMenuOpen.value = open
    }

    fun closeHowToUseManual() {
        _isHowToUseManualOpen.value = false
    }

    fun selectVaultSubSection(subSection: VaultSubSection) {
        _selectedVaultSubSection.value = subSection
    }

    fun selectKhedutTab(tabIndex: Int) {
        _selectedKhedutTab.value = tabIndex
    }

    fun clearVaultScrollTarget() {
        _vaultScrollTargetIndex.value = null
    }

    fun clearMapScrollTarget() {
        _mapScrollTargetIndex.value = null
    }

    fun navigateToQuickFeature(destination: QuickFeatureDestination) {
        _isAllFeaturesMenuOpen.value = false
        when (destination) {
            QuickFeatureDestination.HOW_TO_USE_APP_MANUAL -> {
                _isHowToUseManualOpen.value = true
            }
            QuickFeatureDestination.KHET_TALAVDI_WATER_TANK -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.FARM_712_AND_WATER_TOOLS
                _isCircularWell.value = false
                _waterTankResult.value = computeCurrentWaterTank()
                _vaultScrollTargetIndex.value = 4 // Direct scroll to Farm Water Tank / Khet-Talavdi Card
                _statusBannerMessage.value = "Opened Farm Water Tank (ખેત-તલાવડી / પાણીનો ટાંકો) & Borewell Motor Calculator"
            }
            QuickFeatureDestination.CIRCULAR_WELL_BOREWELL_HP -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.FARM_712_AND_WATER_TOOLS
                _isCircularWell.value = true
                _waterTankResult.value = computeCurrentWaterTank()
                _vaultScrollTargetIndex.value = 4 // Direct scroll to Circular Well & Borewell Motor Card
                _statusBannerMessage.value = "Opened Circular Well (ગોળ કૂવો) & Borewell Submersible HP Calculator"
            }
            QuickFeatureDestination.REVENUE_712_DECODER -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.FARM_712_AND_WATER_TOOLS
                _vaultScrollTargetIndex.value = 2 // Direct scroll to 7/12 Revenue Extract Decoder Card
                _statusBannerMessage.value = "Opened 7/12 & 8-A Revenue Extract (હેક્ટર-આર-ચો.મી.) Decoder"
            }
            QuickFeatureDestination.KHEDUT_LAND_PARTITION -> {
                _selectedTab.value = AppTab.MAP_3D_CUT
                _selectedKhedutTab.value = 0
                _mapScrollTargetIndex.value = 6 // Direct scroll to Khedut Pro Suite Card
                _statusBannerMessage.value = "Opened Land Partition & Family Heirs Splitter (જમીનના ભાગલા / વારસાઈ)"
            }
            QuickFeatureDestination.KHEDUT_FARM_FENCING -> {
                _selectedTab.value = AppTab.MAP_3D_CUT
                _selectedKhedutTab.value = 1
                _mapScrollTargetIndex.value = 6 // Direct scroll to Khedut Pro Suite Card
                _statusBannerMessage.value = "Opened Farm Boundary Fencing & Barbed Wire Calculator (તારની વાડ)"
            }
            QuickFeatureDestination.KHEDUT_CROP_CALCULATOR -> {
                _selectedTab.value = AppTab.MAP_3D_CUT
                _selectedKhedutTab.value = 2
                _mapScrollTargetIndex.value = 6 // Direct scroll to Khedut Pro Suite Card
                _statusBannerMessage.value = "Opened Crop Seed, Fertilizer & Expected Yield Calculator (પાક-ખાતર)"
            }
            QuickFeatureDestination.LAND_UNIT_CONVERTER -> {
                _selectedTab.value = AppTab.LAND_CONVERTER
                _statusBannerMessage.value = "Opened Regional & Global Land Unit Converter"
            }
            QuickFeatureDestination.LAND_STAMP_DUTY_VALUATION -> {
                _selectedTab.value = AppTab.LAND_CONVERTER
                _statusBannerMessage.value = "Opened Land Valuation & Stamp Duty Calculator"
            }
            QuickFeatureDestination.MAP_SATELLITE_LINE_SURVEY -> {
                _selectedTab.value = AppTab.MAP_3D_CUT
                _mapScrollTargetIndex.value = 1
                _statusBannerMessage.value = "Opened In-App Satellite Map & Multi-Line Land Survey"
            }
            QuickFeatureDestination.MAP_3D_PARCEL_VIEWER -> {
                _selectedTab.value = AppTab.MAP_3D_CUT
                _mapScrollTargetIndex.value = 3
                _statusBannerMessage.value = "Opened 3D Land Cutout & 360° Boundary Model"
            }
            QuickFeatureDestination.IRREGULAR_PLOT_CAD -> {
                _selectedTab.value = AppTab.IRREGULAR_PLOT
                _statusBannerMessage.value = "Opened 4-Sided Irregular Plot 2D CAD Blueprint"
            }
            QuickFeatureDestination.CONTRACTOR_ERROR_DETECTOR -> {
                _selectedTab.value = AppTab.IRREGULAR_PLOT
                _statusBannerMessage.value = "Opened Contractor 'Average Formula' Error Detector"
            }
            QuickFeatureDestination.CONSTRUCTION_TILES -> {
                _selectedTab.value = AppTab.CONSTRUCTION
                onSelectConstructionModule(ConstructionModuleType.TILES_FLOORING)
                _statusBannerMessage.value = "Opened Floor & Wall Tiles Material Estimator"
            }
            QuickFeatureDestination.CONSTRUCTION_BRICKWORK -> {
                _selectedTab.value = AppTab.CONSTRUCTION
                onSelectConstructionModule(ConstructionModuleType.BRICKWORK_MASONRY)
                _statusBannerMessage.value = "Opened Brickwork & Masonry Material Estimator"
            }
            QuickFeatureDestination.CONSTRUCTION_PLASTER -> {
                _selectedTab.value = AppTab.CONSTRUCTION
                onSelectConstructionModule(ConstructionModuleType.WALL_PLASTERING)
                _statusBannerMessage.value = "Opened Wall Plastering Cement & Sand Estimator"
            }
            QuickFeatureDestination.CONSTRUCTION_PAINT -> {
                _selectedTab.value = AppTab.CONSTRUCTION
                onSelectConstructionModule(ConstructionModuleType.PAINT_PUTTY)
                _statusBannerMessage.value = "Opened Wall Paint, Primer & Putty Estimator"
            }
            QuickFeatureDestination.SAVED_SURVEY_VAULT -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.SAVED_VAULT
                _vaultScrollTargetIndex.value = 0
                _statusBannerMessage.value = "Opened Saved Survey Vault & PDF Reports"
            }
            QuickFeatureDestination.REWARDED_1HR_AD_FREE -> {
                _isRewardedAdVisible.value = true
            }
            QuickFeatureDestination.LANGUAGE_SWITCHER_115 -> {
                _isLanguageDialogOpen.value = true
            }
            QuickFeatureDestination.LEGAL_AND_PRIVACY -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.LEGAL_COMPLIANCE
                _vaultScrollTargetIndex.value = 0
                _statusBannerMessage.value = "Opened Legal, Privacy & Security Policies"
            }
            QuickFeatureDestination.LIVE_APMC_MANDI_PRICES -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.DAILY_ROJMEL_MANDI_BROKER
                _vaultScrollTargetIndex.value = 1
                syncLiveApmcMarketPrices(isSilentStartup = true)
                _statusBannerMessage.value = "Opened Live Regional APMC & Global Govt Mandi Price Board (Auto-Updated)"
            }
            QuickFeatureDestination.DAILY_MANDI_BILL -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.DAILY_ROJMEL_MANDI_BROKER
                _vaultScrollTargetIndex.value = 2
                _statusBannerMessage.value = "Opened Daily APMC Mandi Weight (20 Kg Man / Quintal) & Crop Bill Calculator"
            }
            QuickFeatureDestination.DAILY_LABOUR_TRACTOR_KHATA -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.DAILY_ROJMEL_MANDI_BROKER
                _vaultScrollTargetIndex.value = 4
                _statusBannerMessage.value = "Opened Daily Farm Labour (Dadiya), Tractor Hours & Upaad Khata"
            }
            QuickFeatureDestination.DAILY_LAND_BROKER_DEAL -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.DAILY_ROJMEL_MANDI_BROKER
                _vaultScrollTargetIndex.value = 6
                _statusBannerMessage.value = "Opened Daily Land Broker Commission, Token & Banakhat Schedule Calculator"
            }
            QuickFeatureDestination.DAILY_FARM_DAIRY_ROJMEL -> {
                _selectedTab.value = AppTab.VAULT_SYSTEM
                _selectedVaultSubSection.value = VaultSubSection.DAILY_ROJMEL_MANDI_BROKER
                _vaultScrollTargetIndex.value = 8
                _statusBannerMessage.value = "Opened Daily Farm, Dairy (Milk) & Landowner Income-Expense Rojmel"
            }
        }
    }

    fun selectLanguage(language: AppLanguage, context: Context? = null) {
        _selectedLanguage.value = language
        _uiStrings.value = AppLocalizationEngine.getStringsForLanguage(language.code)
        _isLanguageDialogOpen.value = false
        _isFirstLaunchLanguageGateOpen.value = false
        if (context != null) {
            saveLanguagePreference(context, language.code)
        }
        _statusBannerMessage.value = "Language set to ${language.nativeName} (${language.englishName})"
    }

    fun completeFirstLaunchLanguageSelection(language: AppLanguage, context: Context) {
        _selectedLanguage.value = language
        _uiStrings.value = AppLocalizationEngine.getStringsForLanguage(language.code)
        _isFirstLaunchLanguageGateOpen.value = false
        _isLanguageDialogOpen.value = false
        saveLanguagePreference(context, language.code)
        _statusBannerMessage.value = "Welcome! Language saved as ${language.nativeName} (${language.englishName})"
        scheduleAppOpenAdIfEligible(_appLaunchCount.value)
    }

    fun setLanguageDialogOpen(open: Boolean) {
        _isLanguageDialogOpen.value = open
    }

    fun clearStatusBanner() {
        _statusBannerMessage.value = null
    }

    fun postStatusMessage(msg: String) {
        _statusBannerMessage.value = msg
    }

    // --- Tab 1 Actions (Indian & Global Country Units) ---
    fun onSelectStateProfile(profile: StateLandProfile) {
        _selectedStateProfile.value = profile
        _selectedInputUnit.value = profile.units.first()
        _landConversionResult.value = computeCurrentLandConversion()
        _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
        _irregularPlotResult.value = computeCurrentIrregularPlot()
        _revenue712Result.value = computeCurrent712Decode()
    }

    fun onSelectInputUnit(unit: LandUnitDefinition) {
        _selectedInputUnit.value = unit
        _landConversionResult.value = computeCurrentLandConversion()
    }

    fun onLandInputValueChange(text: String) {
        _landInputValueText.value = text
        _landConversionResult.value = computeCurrentLandConversion()
    }

    fun onLandRateChange(text: String) {
        _landRatePerUnitText.value = text
        _landConversionResult.value = computeCurrentLandConversion()
    }

    private fun computeCurrentLandConversion(): LandConversionResult {
        val value = _landInputValueText.value.toDoubleOrNull() ?: 0.0
        val rate = _landRatePerUnitText.value.toDoubleOrNull() ?: 0.0
        return LandAndConstructionEngine.convertLandUnits(
            stateProfile = _selectedStateProfile.value,
            inputUnit = _selectedInputUnit.value,
            inputValue = value,
            ratePerInputUnitInr = rate
        )
    }

    fun buildLandConversionRecord(customTitle: String = ""): SurveyRecordEntity {
        val res = _landConversionResult.value
        val gunthaVal = res.totalSqFeet / 1089.0
        val vaarVal = res.totalSqFeet / 9.0
        val vighaVal = res.totalSqFeet / res.stateProfile.vighaSqFt
        val title = customTitle.ifBlank {
            "${LandAndConstructionEngine.formatDec(res.inputValue)} ${res.inputUnit.name} (${res.stateProfile.stateName.substringBefore("(").trim()})"
        }
        val details = buildString {
            appendLine("Country / State Standard: ${res.stateProfile.stateName}")
            appendLine("Input Measurement: ${res.inputValue} ${res.inputUnit.name} (${res.inputUnit.localName})")
            appendLine("Total Square Feet: ${LandAndConstructionEngine.formatDec(res.totalSqFeet)} Sq.Ft")
            appendLine("Total Square Meters: ${LandAndConstructionEngine.formatDec(res.totalSqMeters)} Sq.M")
            res.convertedItems.forEach { item ->
                appendLine("• ${item.unit.name}: ${LandAndConstructionEngine.formatDec(item.convertedValue, 4)}")
            }
            appendLine("Base Land Valuation: ${LandAndConstructionEngine.formatCurrency(res.baseLandValueInr)}")
            appendLine("Stamp Duty (${res.stateProfile.stampDutyPercent}%): ${LandAndConstructionEngine.formatCurrency(res.stampDutyInr)}")
            appendLine("Total All-Inclusive Cost: ${LandAndConstructionEngine.formatCurrency(res.totalLandCostInr)}")
        }
        return SurveyRecordEntity(
            title = title,
            category = "LAND_CONVERSION",
            stateRegion = res.stateProfile.stateName,
            primaryAreaSqFt = res.totalSqFeet,
            summaryMetrics = "${LandAndConstructionEngine.formatDec(vighaVal, 3)} Primary Unit • ${LandAndConstructionEngine.formatDec(gunthaVal, 2)} Guntha • ${LandAndConstructionEngine.formatDec(vaarVal, 1)} Vaar/Sq.Yd",
            detailedBreakdown = details.trim(),
            estimatedCostInr = res.totalLandCostInr
        )
    }

    fun saveCurrentLandConversion() {
        viewModelScope.launch {
            val record = buildLandConversionRecord()
            repository.insert(record)
            _statusBannerMessage.value = "Saved '${record.title}' to Offline Vault"
            triggerInterstitialAfterTaskCompletion("Land Conversion Saved to Vault")
        }
    }

    fun exportCurrentLandConversionPdf(context: Context) {
        val record = buildLandConversionRecord()
        PdfReportGenerator.generateAndSharePdf(context, record) { msg ->
            _statusBannerMessage.value = msg
            triggerInterstitialAfterTaskCompletion("Land Conversion PDF Exported")
        }
    }

    // --- Tab 2 Actions (In-App Satellite Map + Trading-Style Multi-Line Cutout + 3D Parcel Viewer) ---
    private fun computeCurrentMapSurvey(): GeodesicPolygonSurveyResult {
        return GeodesicMapAnd3DEngine.evaluateGeodesicPolygon(
            points = _mapPolygonPoints.value,
            stateProfile = _selectedStateProfile.value
        )
    }

    fun onGoogleMapsInputChange(text: String) {
        _googleMapsInputText.value = text
    }

    fun jumpToGoogleMapsCoordinates() {
        val parsed = GeodesicMapAnd3DEngine.parseGoogleMapsInput(_googleMapsInputText.value)
        if (parsed != null) {
            // Center the map only. Do not generate a pretend boundary or area around this point.
            _mapDisplayCenter.value = parsed.copy(label = "Map center")
            _mapPolygonPoints.value = emptyList()
            _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
            _statusBannerMessage.value = "Map centered at entered coordinates. Tap actual boundary corners to begin measuring."
        } else {
            _statusBannerMessage.value = "Enter valid latitude, longitude or a Google Maps link before opening the map."
        }
    }

    fun addMapPolygonPoint(lat: Double, lng: Double) {
        val current = _mapPolygonPoints.value
        if (current.isEmpty() && _mapDisplayCenter.value == null) {
            _mapDisplayCenter.value = GpsLatLng(lat, lng, "Map center")
        }
        val nextIndex = current.size + 1
        val label = "P$nextIndex"
        _mapPolygonPoints.value = current + GpsLatLng(
            lat = lat,
            lng = lng,
            label = label
        )
        _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
    }

    fun updateMapPolygonPoint(index: Int, lat: Double, lng: Double) {
        val current = _mapPolygonPoints.value.toMutableList()
        if (index in current.indices) {
            val existing = current[index]
            current[index] = existing.copy(lat = lat, lng = lng)
            _mapPolygonPoints.value = current
            _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
        }
    }

    fun startSinglePointLineDrawingMode() {
        val point = GeodesicMapAnd3DEngine.parseGoogleMapsInput(_googleMapsInputText.value) ?: _mapDisplayCenter.value
        if (point == null) {
            _statusBannerMessage.value = "Enter coordinates and open the map first, or tap a location on the map to begin."
            return
        }
        _mapDisplayCenter.value = point
        _mapPolygonPoints.value = listOf(point.copy(label = "P1"))
        _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
        _statusBannerMessage.value = "Started at the chosen first point. Tap the real boundary corners to continue measuring."
    }

    fun undoLastMapPoint() {
        val current = _mapPolygonPoints.value
        if (current.isNotEmpty()) {
            _mapPolygonPoints.value = current.dropLast(1)
            _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
        }
    }

    fun resetDefaultFourCornerMapCut() {
        val parsed = GeodesicMapAnd3DEngine.parseGoogleMapsInput(_googleMapsInputText.value)
        if (parsed != null) _mapDisplayCenter.value = parsed.copy(label = "Map center")
        _mapPolygonPoints.value = emptyList()
        _mapPolygonSurveyResult.value = computeCurrentMapSurvey()
        _statusBannerMessage.value = "Boundary points cleared. Enter/select a location and draw the actual plot boundary; no sample box was created."
    }

    fun transferMapCutToIrregularCadTab() {
        val survey = _mapPolygonSurveyResult.value
        if (survey.equivalentFourSidesFt.size >= 5 && survey.areaSqFeet > 0) {
            val n = survey.equivalentFourSidesFt[0]
            val e = survey.equivalentFourSidesFt[1]
            val s = survey.equivalentFourSidesFt[2]
            val w = survey.equivalentFourSidesFt[3]
            val diag = survey.equivalentFourSidesFt[4]
            _northSideText.value = LandAndConstructionEngine.formatDec(n, 2)
            _eastSideText.value = LandAndConstructionEngine.formatDec(e, 2)
            _southSideText.value = LandAndConstructionEngine.formatDec(s, 2)
            _westSideText.value = LandAndConstructionEngine.formatDec(w, 2)
            _diagonalSideText.value = LandAndConstructionEngine.formatDec(diag, 2)
            _irregularPlotResult.value = computeCurrentIrregularPlot()
            _selectedTab.value = AppTab.IRREGULAR_PLOT
            _statusBannerMessage.value = "Transferred Map Cutout sides (${LandAndConstructionEngine.formatDec(n, 1)}', ${LandAndConstructionEngine.formatDec(e, 1)}', ${LandAndConstructionEngine.formatDec(s, 1)}', ${LandAndConstructionEngine.formatDec(w, 1)}') to 2D CAD Blueprint"
            triggerInterstitialAfterTaskCompletion("Map Cutout Transferred to 2D CAD")
        } else {
            _statusBannerMessage.value = "Draw at least 3 boundary corners on the Map first"
        }
    }

    private fun buildMapCutSurveyRecord(): SurveyRecordEntity {
        val survey = _mapPolygonSurveyResult.value
        val profile = _selectedStateProfile.value
        val ratePerSqFt = _plotRatePerSqFtText.value.toDoubleOrNull() ?: 3500.0
        val estimatedCost = survey.areaSqFeet * ratePerSqFt

        val topUnitsSummary = survey.countryConversions.take(3).joinToString(" • ") {
            "${LandAndConstructionEngine.formatDec(it.convertedValue, 3)} ${it.unit.name.substringBefore("(").trim()}"
        }

        val sidesCsv = if (survey.equivalentFourSidesFt.size >= 5) {
            survey.equivalentFourSidesFt.joinToString(",")
        } else ""

        val details = buildString {
            appendLine("GPS Geodesic 3D Parcel Cutout (${survey.points.size} Boundary Corners)")
            appendLine("Center GPS: ${String.format("%.6f", survey.centroid.lat)}, ${String.format("%.6f", survey.centroid.lng)}")
            appendLine("Country / Regional Standard: ${profile.stateName}")
            appendLine("Exact Geodesic Area: ${LandAndConstructionEngine.formatDec(survey.areaSqFeet, 2)} Sq.Ft (${LandAndConstructionEngine.formatDec(survey.areaSqMeters, 2)} Sq.M)")
            appendLine("Total Boundary Perimeter: ${LandAndConstructionEngine.formatDec(survey.perimeterFeet, 2)} ft (${LandAndConstructionEngine.formatDec(survey.perimeterMeters, 2)} m)")
            appendLine("--- Measured Boundary Sides ---")
            survey.edges.forEach { edge ->
                appendLine("• ${edge.label}: ${LandAndConstructionEngine.formatDec(edge.lengthFeet, 2)} ft | ${LandAndConstructionEngine.formatDec(edge.lengthMeters, 2)} m | ${LandAndConstructionEngine.formatDec(edge.lengthVaar, 2)} yd/Vaar | Bearing ${LandAndConstructionEngine.formatDec(edge.bearingDegrees, 1)}° (${edge.compassDirection})")
            }
            appendLine("--- Country & Regional Unit Conversions ---")
            survey.countryConversions.forEach { item ->
                appendLine("• ${item.unit.name} (${item.unit.localName}): ${LandAndConstructionEngine.formatDec(item.convertedValue, 4)}")
            }
        }

        return SurveyRecordEntity(
            title = "3D Map Cutout (${LandAndConstructionEngine.formatDec(survey.areaSqFeet, 0)} Sq.Ft • ${profile.stateName.substringBefore("(").trim()})",
            category = "MAP_3D_CUTOUT",
            stateRegion = profile.stateName,
            primaryAreaSqFt = survey.areaSqFeet,
            summaryMetrics = "${LandAndConstructionEngine.formatDec(survey.areaSqFeet, 1)} Sq.Ft • $topUnitsSummary",
            detailedBreakdown = details.trim(),
            estimatedCostInr = estimatedCost,
            plotSidesCsv = sidesCsv
        )
    }

    fun saveCurrentMapCutSurvey() {
        viewModelScope.launch {
            val record = buildMapCutSurveyRecord()
            repository.insert(record)
            _statusBannerMessage.value = "Saved 3D Map Cutout '${record.title}' to Vault"
            triggerInterstitialAfterTaskCompletion("3D Map Cutout Saved to Vault")
        }
    }

    fun exportCurrentMapCutPdf(context: Context) {
        val record = buildMapCutSurveyRecord()
        PdfReportGenerator.generateAndSharePdf(context, record) { msg ->
            _statusBannerMessage.value = msg
            triggerInterstitialAfterTaskCompletion("3D Map Cutout PDF Exported")
        }
    }

    // --- Tab 3 Actions (Irregular 4-Sided Plot CAD) ---
    fun onPlotDimensionChange(
        north: String = _northSideText.value,
        east: String = _eastSideText.value,
        south: String = _southSideText.value,
        west: String = _westSideText.value,
        diagonal: String = _diagonalSideText.value,
        rateSqFt: String = _plotRatePerSqFtText.value
    ) {
        _northSideText.value = north
        _eastSideText.value = east
        _southSideText.value = south
        _westSideText.value = west
        _diagonalSideText.value = diagonal
        _plotRatePerSqFtText.value = rateSqFt
        _irregularPlotResult.value = computeCurrentIrregularPlot()
    }

    fun autoCalibrateDiagonal() {
        if (!hasValidIrregularPlotInputs()) {
            _statusBannerMessage.value = "Enter all four actual boundary sides before calibrating a diagonal."
            return
        }
        val n = _northSideText.value.toDoubleOrNull() ?: 0.0
        val e = _eastSideText.value.toDoubleOrNull() ?: 0.0
        val s = _southSideText.value.toDoubleOrNull() ?: 0.0
        val w = _westSideText.value.toDoubleOrNull() ?: 0.0
        val opt = LandAndConstructionEngine.calculateOptimalDiagonal(n, e, s, w)
        _diagonalSideText.value = LandAndConstructionEngine.formatDec(opt, 1)
        _irregularPlotResult.value = computeCurrentIrregularPlot()
        _statusBannerMessage.value = "Calibrated diagonal from the four entered boundary sides to ${LandAndConstructionEngine.formatDec(opt, 1)} ft"
    }

    fun applyPlotPreset(north: Double, east: Double, south: Double, west: Double, diag: Double) {
        _northSideText.value = north.toString()
        _eastSideText.value = east.toString()
        _southSideText.value = south.toString()
        _westSideText.value = west.toString()
        _diagonalSideText.value = diag.toString()
        _irregularPlotResult.value = computeCurrentIrregularPlot()
    }

    private fun hasValidIrregularPlotInputs(): Boolean =
        listOf(_northSideText.value, _eastSideText.value, _southSideText.value, _westSideText.value)
            .all { it.toDoubleOrNull()?.let { value -> value > 0.0 } == true }

    private fun computeCurrentIrregularPlot(): IrregularPlotResult {
        val n = _northSideText.value.toDoubleOrNull() ?: 0.0
        val e = _eastSideText.value.toDoubleOrNull() ?: 0.0
        val s = _southSideText.value.toDoubleOrNull() ?: 0.0
        val w = _westSideText.value.toDoubleOrNull() ?: 0.0
        val d = _diagonalSideText.value.toDoubleOrNull() ?: 0.0
        val rate = _plotRatePerSqFtText.value.toDoubleOrNull() ?: 0.0
        return LandAndConstructionEngine.evaluateIrregularPlot(
            northFt = n,
            eastFt = e,
            southFt = s,
            westFt = w,
            diagonalInputFt = d,
            vighaSqFt = _selectedStateProfile.value.vighaSqFt,
            ratePerSqFtInr = rate
        )
    }

    fun buildIrregularPlotRecord(): SurveyRecordEntity {
        val res = _irregularPlotResult.value
        val hasRate = _plotRatePerSqFtText.value.toDoubleOrNull()?.let { it > 0.0 } == true
        val vaar = res.exactAreaSqFt / 9.0
        val guntha = res.exactAreaSqFt / 1089.0
        val vigha = res.exactAreaSqFt / _selectedStateProfile.value.vighaSqFt
        val moneyDifferenceText = if (hasRate) {
            LandAndConstructionEngine.formatCurrency(res.contractorErrorInr)
        } else {
            "Money difference not calculated; no rate was entered"
        }
        val estimatedLandValueText = if (hasRate) {
            LandAndConstructionEngine.formatCurrency(res.estimatedPlotValueInr)
        } else {
            "Not calculated; no rate per square foot was entered"
        }
        val details = buildString {
            appendLine("Boundary Sides: North=${res.northFt} ft, East=${res.eastFt} ft, South=${res.southFt} ft, West=${res.westFt} ft")
            appendLine("Diagonal (NW to SE): ${LandAndConstructionEngine.formatDec(res.usedDiagonal)} ft")
            appendLine("Sub-Triangle 1 (N-E-Diag): ${LandAndConstructionEngine.formatDec(res.triangle1AreaSqFt)} Sq.Ft")
            appendLine("Sub-Triangle 2 (S-W-Diag): ${LandAndConstructionEngine.formatDec(res.triangle2AreaSqFt)} Sq.Ft")
            appendLine("Exact Triangulated Area: ${LandAndConstructionEngine.formatDec(res.exactAreaSqFt)} Sq.Ft")
            appendLine("Naive Contractor Avg Area: ${LandAndConstructionEngine.formatDec(res.naiveAvgAreaSqFt)} Sq.Ft")
            appendLine("Measurement Discrepancy: ${LandAndConstructionEngine.formatDec(res.contractorErrorSqFt)} Sq.Ft (${moneyDifferenceText})")
            appendLine("Estimated Land Value: $estimatedLandValueText")
            appendLine("Vaar (Gaj): ${LandAndConstructionEngine.formatDec(vaar)} | Guntha: ${LandAndConstructionEngine.formatDec(guntha, 4)} | Vigha: ${LandAndConstructionEngine.formatDec(vigha, 4)}")
        }
        return SurveyRecordEntity(
            title = "4-Sided Plot (${res.northFt.toInt()}×${res.eastFt.toInt()}×${res.southFt.toInt()}×${res.westFt.toInt()} ft)",
            category = "IRREGULAR_PLOT",
            stateRegion = _selectedStateProfile.value.stateName,
            primaryAreaSqFt = res.exactAreaSqFt,
            summaryMetrics = "${LandAndConstructionEngine.formatDec(res.exactAreaSqFt)} Sq.Ft • ${LandAndConstructionEngine.formatDec(vaar)} Vaar • ${LandAndConstructionEngine.formatDec(guntha, 3)} Guntha",
            detailedBreakdown = details.trim(),
            estimatedCostInr = if (hasRate) res.estimatedPlotValueInr else 0.0,
            plotSidesCsv = "${res.northFt},${res.eastFt},${res.southFt},${res.westFt},${res.usedDiagonal}"
        )
    }

    fun saveCurrentIrregularPlot() {
        viewModelScope.launch {
            val record = buildIrregularPlotRecord()
            repository.insert(record)
            _statusBannerMessage.value = "Saved CAD Plot Survey '${record.title}' to Vault"
            triggerInterstitialAfterTaskCompletion("2D CAD Plot Saved to Vault")
        }
    }

    fun exportCurrentIrregularPlotPdf(context: Context) {
        val record = buildIrregularPlotRecord()
        PdfReportGenerator.generateAndSharePdf(context, record) { msg ->
            _statusBannerMessage.value = msg
            triggerInterstitialAfterTaskCompletion("2D CAD Plot PDF Exported")
        }
    }

    // --- Tab 4 Actions (Construction Material BOQ) ---
    fun onSelectConstructionModule(module: ConstructionModuleType) {
        _selectedConstructionModule.value = module
        _constructionBoqResult.value = computeCurrentConstructionBoq()
    }

    fun onConstructionInputsChange(
        lengthText: String = _roomLengthText.value,
        widthText: String = _roomWidthOrHeightText.value,
        deductionText: String = _deductionSqFtText.value,
        tileL: Double = _tileLengthFt.value,
        tileW: Double = _tileWidthFt.value,
        wastage: Double = _wastagePercent.value,
        wallInches: Double = _wallThicknessInches.value,
        plasterMm: Double = _plasterThicknessMm.value,
        cementRate: String = _cementRateText.value,
        sandRate: String = _sandRateText.value,
        tileRate: String = _tileRateText.value,
        brickRate: String = _brickRateText.value,
        paintRate: String = _paintRateText.value
    ) {
        _roomLengthText.value = lengthText
        _roomWidthOrHeightText.value = widthText
        _deductionSqFtText.value = deductionText
        _tileLengthFt.value = tileL
        _tileWidthFt.value = tileW
        _wastagePercent.value = wastage
        _wallThicknessInches.value = wallInches
        _plasterThicknessMm.value = plasterMm
        _cementRateText.value = cementRate
        _sandRateText.value = sandRate
        _tileRateText.value = tileRate
        _brickRateText.value = brickRate
        _paintRateText.value = paintRate
        _constructionBoqResult.value = computeCurrentConstructionBoq()
    }

    private fun computeCurrentConstructionBoq(): ConstructionBoqResult {
        return LandAndConstructionEngine.estimateConstructionMaterials(
            moduleType = _selectedConstructionModule.value,
            lengthFt = _roomLengthText.value.toDoubleOrNull() ?: 20.0,
            widthOrHeightFt = _roomWidthOrHeightText.value.toDoubleOrNull() ?: 12.0,
            tileLengthFt = _tileLengthFt.value,
            tileWidthFt = _tileWidthFt.value,
            wastagePercent = _wastagePercent.value,
            wallThicknessInches = _wallThicknessInches.value,
            plasterThicknessMm = _plasterThicknessMm.value,
            deductionSqFt = _deductionSqFtText.value.toDoubleOrNull() ?: 0.0,
            cementBagRateInr = _cementRateText.value.toDoubleOrNull() ?: 390.0,
            sandPerCuFtRateInr = _sandRateText.value.toDoubleOrNull() ?: 55.0,
            tilePerSqFtRateInr = _tileRateText.value.toDoubleOrNull() ?: 48.0,
            brickUnitRateInr = _brickRateText.value.toDoubleOrNull() ?: 8.5,
            paintPerLiterRateInr = _paintRateText.value.toDoubleOrNull() ?: 320.0
        )
    }

    fun buildConstructionBoqRecord(): SurveyRecordEntity {
        val res = _constructionBoqResult.value
        val summary = res.items.joinToString(" • ") { "${it.materialName.substringBefore("(").trim()}: ${it.quantityFormatted}" }
        val details = buildString {
            appendLine("Estimator Module: ${res.moduleType.title}")
            appendLine("Net Work Area: ${LandAndConstructionEngine.formatDec(res.netWorkAreaSqFt)} Sq.Ft")
            appendLine("Engineering Formula: ${res.technicalFormulaNote}")
            appendLine("--- Itemized Bill of Quantities (BOQ) ---")
            res.items.forEach { item ->
                appendLine("• ${item.materialName}: ${item.quantityFormatted}  |  Cost: ${LandAndConstructionEngine.formatCurrency(item.totalCostInr)}")
                appendLine("  Spec: ${item.specification}")
            }
            appendLine("Total Material Cost: ${LandAndConstructionEngine.formatCurrency(res.totalEstimatedCostInr)}")
        }
        return SurveyRecordEntity(
            title = "${res.moduleType.title} (${LandAndConstructionEngine.formatDec(res.netWorkAreaSqFt, 0)} Sq.Ft)",
            category = "CONSTRUCTION_BOQ",
            stateRegion = "IS 456 / CPWD Civil Standard",
            primaryAreaSqFt = res.netWorkAreaSqFt,
            summaryMetrics = summary.take(110),
            detailedBreakdown = details.trim(),
            estimatedCostInr = res.totalEstimatedCostInr
        )
    }

    fun saveCurrentConstructionBoq() {
        viewModelScope.launch {
            val record = buildConstructionBoqRecord()
            repository.insert(record)
            _statusBannerMessage.value = "Saved BOQ '${record.title}' to Vault"
            triggerInterstitialAfterTaskCompletion("Construction Material BOQ Saved")
        }
    }

    fun exportCurrentConstructionBoqPdf(context: Context) {
        val record = buildConstructionBoqRecord()
        PdfReportGenerator.generateAndSharePdf(context, record) { msg ->
            _statusBannerMessage.value = msg
            triggerInterstitialAfterTaskCompletion("Construction Material BOQ PDF Exported")
        }
    }

    // --- Tab 5 Actions: 7/12 Revenue Extract Decoder & Farm Water Tank / Borewell Pump ---
    fun on712InputsChange(
        hectares: String = _hectares712Text.value,
        are: String = _are712Text.value,
        sqMeters: String = _sqMeters712Text.value
    ) {
        _hectares712Text.value = hectares
        _are712Text.value = are
        _sqMeters712Text.value = sqMeters
        _revenue712Result.value = computeCurrent712Decode()
    }

    private fun computeCurrent712Decode(): Revenue712DecodeResult {
        val h = _hectares712Text.value.toDoubleOrNull() ?: 0.0
        val a = _are712Text.value.toDoubleOrNull() ?: 0.0
        val sqm = _sqMeters712Text.value.toDoubleOrNull() ?: 0.0
        return FarmerAndLandToolsEngine.calculate712HectareAreSqm(
            hectares = h,
            ares = a,
            sqMeters = sqm
        )
    }

    fun saveCurrent712DecodeRecord() {
        viewModelScope.launch {
            val res = _revenue712Result.value
            val details = buildString {
                appendLine("7/12 & 8A Government Revenue Extract: ${res.hectares.toInt()}-${res.ares.toInt()}-${LandAndConstructionEngine.formatDec(res.sqMeters, 2)} (H-Are-Sq.M)")
                appendLine("Total Square Meters: ${LandAndConstructionEngine.formatDec(res.totalSqMeters, 2)} m²")
                appendLine("Total Square Feet: ${LandAndConstructionEngine.formatDec(res.totalSqFeet, 2)} Sq.Ft")
                appendLine("Saurashtra 16-Guntha Vigha: ${LandAndConstructionEngine.formatDec(res.saurashtraVigha16Guntha, 3)} Vigha")
                appendLine("North/Central Gujarat 23.5-Guntha Vigha: ${LandAndConstructionEngine.formatDec(res.northGujaratVigha23_5Guntha, 3)} Vigha")
                appendLine("Kachha Vigha: ${LandAndConstructionEngine.formatDec(res.centralGujaratKachhaVigha, 3)} Vigha")
                appendLine("Guntha (40 per Acre): ${LandAndConstructionEngine.formatDec(res.totalGuntha, 2)} Guntha")
                appendLine("Vaar / Sq.Yards: ${LandAndConstructionEngine.formatDec(res.totalVaarSqYd, 1)} Vaar")
                appendLine("Estimated KCC Crop Loan Eligibility: ${LandAndConstructionEngine.formatCurrency(res.kccCropLoanEligibilityInr)}")
            }
            val record = SurveyRecordEntity(
                title = "7/12 Extract (${res.hectares.toInt()}-${res.ares.toInt()}-${res.sqMeters.toInt()} H-Are-m²)",
                category = "7_12_REVENUE_EXTRACT",
                stateRegion = _selectedStateProfile.value.stateName,
                primaryAreaSqFt = res.totalSqFeet,
                summaryMetrics = "${LandAndConstructionEngine.formatDec(res.saurashtraVigha16Guntha, 2)} Vigha (16G) • ${LandAndConstructionEngine.formatDec(res.totalGuntha, 2)} Guntha • ${LandAndConstructionEngine.formatDec(res.totalAcres, 2)} Acre",
                detailedBreakdown = details.trim(),
                estimatedCostInr = res.kccCropLoanEligibilityInr
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved 7/12 Extract '${record.title}' to Vault"
            triggerInterstitialAfterTaskCompletion("7/12 Revenue Extract Saved to Vault")
        }
    }

    fun onWaterTankInputsChange(
        circularWell: Boolean = _isCircularWell.value,
        lengthOrDiamFt: String = _tankLengthOrDiameterFtText.value,
        widthFt: String = _tankWidthFtText.value,
        depthFt: String = _tankDepthFtText.value,
        borewellDepthFt: String = _borewellDepthFtText.value
    ) {
        _isCircularWell.value = circularWell
        _tankLengthOrDiameterFtText.value = lengthOrDiamFt
        _tankWidthFtText.value = widthFt
        _tankDepthFtText.value = depthFt
        _borewellDepthFtText.value = borewellDepthFt
        _waterTankResult.value = computeCurrentWaterTank()
    }

    private fun computeCurrentWaterTank(): WaterTankAndBorewellResult {
        return FarmerAndLandToolsEngine.calculateWaterTankAndBorewellPump(
            isCircularWell = _isCircularWell.value,
            lengthOrDiameterFt = _tankLengthOrDiameterFtText.value.toDoubleOrNull() ?: 20.0,
            widthFt = _tankWidthFtText.value.toDoubleOrNull() ?: 15.0,
            depthFt = _tankDepthFtText.value.toDoubleOrNull() ?: 10.0,
            borewellDepthFt = _borewellDepthFtText.value.toDoubleOrNull() ?: 300.0
        )
    }

    fun saveCurrentWaterTankRecord() {
        viewModelScope.launch {
            val res = _waterTankResult.value
            val shapeLabel = if (res.isCircularWell) "Circular Well (ગોળ કૂવો)" else "Rectangular Farm Tank (ખેત-તલાવડી/ટાંકો)"
            val details = buildString {
                appendLine("Structure Type: $shapeLabel")
                appendLine("Volume: ${LandAndConstructionEngine.formatDec(res.volumeCubicFeet, 1)} Cu.Ft (${LandAndConstructionEngine.formatDec(res.volumeCubicMeters, 2)} m³)")
                appendLine("Total Water Storage Capacity: ${LandAndConstructionEngine.formatDec(res.totalCapacityLiters, 0)} Liters")
                appendLine("Tractor Tanker Loads (4,000L): ${LandAndConstructionEngine.formatDec(res.totalCapacityLiters / 4000.0, 1)} Tankers")
                appendLine("Irrigation Runtime (@250 LPM): ${LandAndConstructionEngine.formatDec(res.irrigationHoursAt250Lpm, 1)} Hours")
                appendLine("Recommended Borewell Submersible Pump: ${res.recommendedSubmersibleHp} HP (${LandAndConstructionEngine.formatDec(res.powerConsumptionKwhPerHour, 2)} kW/hr)")
            }
            val record = SurveyRecordEntity(
                title = "$shapeLabel (${LandAndConstructionEngine.formatDec(res.totalCapacityLiters, 0)} Liters)",
                category = "FARM_WATER_TANK",
                stateRegion = "Agricultural Irrigation Standard",
                primaryAreaSqFt = res.volumeCubicFeet,
                summaryMetrics = "${LandAndConstructionEngine.formatDec(res.totalCapacityLiters, 0)} Liters • ${res.recommendedSubmersibleHp} HP Borewell Motor",
                detailedBreakdown = details.trim(),
                estimatedCostInr = res.totalCapacityLiters * 0.65
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved Water Tank & Borewell Calculation to Vault"
            triggerInterstitialAfterTaskCompletion("Water Tank & Borewell Spec Saved")
        }
    }

    // --- Daily-Use Suite 1: APMC Mandi Weight & Crop Bill ---
    fun onMandiInputsChange(
        bags: String = _mandiBagsText.value,
        bagWeightKg: String = _mandiBagWeightKgText.value,
        bardaanKg: String = _mandiBardaanKgText.value,
        ratePerMan: String = _mandiRatePerManText.value,
        commissionPct: String = _mandiCommissionPctText.value,
        hamaliPerBag: String = _mandiHamaliPerBagText.value
    ) {
        _mandiBagsText.value = bags
        _mandiBagWeightKgText.value = bagWeightKg
        _mandiBardaanKgText.value = bardaanKg
        _mandiRatePerManText.value = ratePerMan
        _mandiCommissionPctText.value = commissionPct
        _mandiHamaliPerBagText.value = hamaliPerBag
        _dailyMandiBillResult.value = computeCurrentMandiBill()
    }

    private fun computeCurrentMandiBill(): DailyMandiBillResult {
        return FarmerAndLandToolsEngine.calculateDailyMandiBill(
            totalBags = _mandiBagsText.value.toIntOrNull() ?: 45,
            avgBagWeightKg = _mandiBagWeightKgText.value.toDoubleOrNull() ?: 50.0,
            bardaanWeightPerBagKg = _mandiBardaanKgText.value.toDoubleOrNull() ?: 0.6,
            ratePer20KgManInr = _mandiRatePerManText.value.toDoubleOrNull() ?: 1520.0,
            commissionPercent = _mandiCommissionPctText.value.toDoubleOrNull() ?: 1.0,
            hamaliPerBagInr = _mandiHamaliPerBagText.value.toDoubleOrNull() ?: 8.0
        )
    }

    fun saveCurrentMandiBillRecord() {
        viewModelScope.launch {
            val res = _dailyMandiBillResult.value
            val details = buildString {
                appendLine("Total Bags (ગુણી/કટ્ટા): ${res.totalBags} Bags")
                appendLine("Gross Weight: ${LandAndConstructionEngine.formatDec(res.grossWeightKg, 1)} Kg")
                appendLine("Bardaan (Tare) Deduction: ${LandAndConstructionEngine.formatDec(res.totalBardaanDeductionKg, 2)} Kg")
                appendLine("Net Payable Weight: ${LandAndConstructionEngine.formatDec(res.netWeightKg, 2)} Kg")
                appendLine("Net Weight in Man (20 Kg મણ): ${LandAndConstructionEngine.formatDec(res.netWeightMan20Kg, 2)} Man")
                appendLine("Net Weight in Quintal (100 Kg ક્વિન્ટલ): ${LandAndConstructionEngine.formatDec(res.netWeightQuintal, 2)} Quintal")
                appendLine("Net Weight in Khandi (ખાંડી): ${LandAndConstructionEngine.formatDec(res.netWeightKhandi, 2)} Khandi")
                appendLine("Rate: ₹${LandAndConstructionEngine.formatDec(res.ratePer20KgManInr, 0)} / Man (₹${LandAndConstructionEngine.formatDec(res.equivalentRatePerQuintalInr, 0)} / Quintal)")
                appendLine("Gross Crop Value: ₹${LandAndConstructionEngine.formatDec(res.grossAmountInr, 0)}")
                appendLine("Commission + Hamali/Tolai Deduction: ₹${LandAndConstructionEngine.formatDec(res.commissionAmountInr + res.hamaliAndTolaiInr, 0)}")
                appendLine("Net Farmer Payout (ચોખ્ખી રકમ): ₹${LandAndConstructionEngine.formatDec(res.netPayableToFarmerInr, 0)}")
            }
            val record = SurveyRecordEntity(
                title = "APMC Mandi Bill (${LandAndConstructionEngine.formatDec(res.netWeightMan20Kg, 1)} Man • ${res.totalBags} Bags)",
                category = "DAILY_MANDI_BILL",
                stateRegion = "APMC Yard Standard (20 Kg Man / Quintal)",
                primaryAreaSqFt = res.netWeightKg,
                summaryMetrics = "${LandAndConstructionEngine.formatDec(res.netWeightMan20Kg, 2)} Man (${LandAndConstructionEngine.formatDec(res.netWeightQuintal, 2)} Qtl) • Net ₹${LandAndConstructionEngine.formatDec(res.netPayableToFarmerInr, 0)}",
                detailedBreakdown = details.trim(),
                estimatedCostInr = res.netPayableToFarmerInr
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved Daily APMC Mandi Bill to Vault"
            triggerInterstitialAfterTaskCompletion("Daily APMC Mandi Bill Saved")
        }
    }

    // --- Daily-Use Suite 2: Daily Farm Labour (Dadiya) & Tractor Khata ---
    fun onLabourKhataInputsChange(
        workers: String = _labourWorkersCountText.value,
        days: String = _labourDaysWorkedText.value,
        dailyWage: String = _labourDailyWageText.value,
        tractorHours: String = _tractorHoursText.value,
        tractorRate: String = _tractorRatePerHourText.value,
        upaadAdvance: String = _upaadAdvanceText.value
    ) {
        _labourWorkersCountText.value = workers
        _labourDaysWorkedText.value = days
        _labourDailyWageText.value = dailyWage
        _tractorHoursText.value = tractorHours
        _tractorRatePerHourText.value = tractorRate
        _upaadAdvanceText.value = upaadAdvance
        _dailyLabourKhataResult.value = computeCurrentLabourKhata()
    }

    private fun computeCurrentLabourKhata(): DailyLabourTractorKhataResult {
        return FarmerAndLandToolsEngine.calculateDailyLabourAndTractorKhata(
            maleOrGeneralWorkers = _labourWorkersCountText.value.toIntOrNull() ?: 6,
            daysWorked = _labourDaysWorkedText.value.toDoubleOrNull() ?: 5.0,
            dailyWagePerWorkerInr = _labourDailyWageText.value.toDoubleOrNull() ?: 400.0,
            tractorHours = _tractorHoursText.value.toDoubleOrNull() ?: 4.5,
            tractorRatePerHourInr = _tractorRatePerHourText.value.toDoubleOrNull() ?: 900.0,
            upaadAdvanceGivenInr = _upaadAdvanceText.value.toDoubleOrNull() ?: 3500.0
        )
    }

    fun saveCurrentLabourKhataRecord() {
        viewModelScope.launch {
            val res = _dailyLabourKhataResult.value
            val details = buildString {
                appendLine("Workers Count (મજૂર સંખ્યા): ${res.workersCount} × ${LandAndConstructionEngine.formatDec(res.daysWorked, 1)} Days (${LandAndConstructionEngine.formatDec(res.totalPersonDays, 1)} Hazri)")
                appendLine("Total Labour Wage (દાડિયા રકમ): ₹${LandAndConstructionEngine.formatDec(res.totalLabourWageInr, 0)}")
                appendLine("Tractor Work (${LandAndConstructionEngine.formatDec(res.tractorHours, 1)} Hrs @ ₹${LandAndConstructionEngine.formatDec(res.tractorRatePerHourInr, 0)}/hr): ₹${LandAndConstructionEngine.formatDec(res.totalTractorRentInr, 0)}")
                appendLine("Estimated Tractor Diesel: ${LandAndConstructionEngine.formatDec(res.estimatedDieselLiters, 1)} Liters")
                appendLine("Combined Gross Bill: ₹${LandAndConstructionEngine.formatDec(res.grossCombinedBillInr, 0)}")
                appendLine("Less Upaad / Advance Paid (ઉપાડ): ₹${LandAndConstructionEngine.formatDec(res.upaadAdvanceGivenInr, 0)}")
                appendLine("Net Balance Payable (બાકી ચૂકવવાપાત્ર): ₹${LandAndConstructionEngine.formatDec(res.netBalancePayableInr, 0)}")
            }
            val record = SurveyRecordEntity(
                title = "Labour & Tractor Khata (${LandAndConstructionEngine.formatDec(res.totalPersonDays, 0)} Hazri + ${LandAndConstructionEngine.formatDec(res.tractorHours, 1)}h)",
                category = "DAILY_LABOUR_KHATA",
                stateRegion = "Daily Farm Hazri & Tractor Ledger",
                primaryAreaSqFt = res.totalPersonDays,
                summaryMetrics = "Gross ₹${LandAndConstructionEngine.formatDec(res.grossCombinedBillInr, 0)} • Upaad ₹${LandAndConstructionEngine.formatDec(res.upaadAdvanceGivenInr, 0)} • Net ₹${LandAndConstructionEngine.formatDec(res.netBalancePayableInr, 0)}",
                detailedBreakdown = details.trim(),
                estimatedCostInr = res.netBalancePayableInr
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved Daily Farm Labour & Tractor Khata to Vault"
            triggerInterstitialAfterTaskCompletion("Daily Farm Labour & Tractor Khata Saved")
        }
    }

    // --- Daily-Use Suite 3: Land Broker Deal Commission, Token & Banakhat Schedule ---
    fun onLandDealInputsChange(
        totalDealAmount: String = _dealTotalAmountText.value,
        tokenAmount: String = _dealTokenAmountText.value,
        banakhatPct: String = _dealBanakhatPctText.value,
        buyerBrokeragePct: String = _dealBuyerBrokeragePctText.value,
        sellerBrokeragePct: String = _dealSellerBrokeragePctText.value
    ) {
        _dealTotalAmountText.value = totalDealAmount
        _dealTokenAmountText.value = tokenAmount
        _dealBanakhatPctText.value = banakhatPct
        _dealBuyerBrokeragePctText.value = buyerBrokeragePct
        _dealSellerBrokeragePctText.value = sellerBrokeragePct
        _landDealBrokerageResult.value = computeCurrentLandDealBrokerage()
    }

    private fun computeCurrentLandDealBrokerage(): LandDealBrokerageResult {
        return FarmerAndLandToolsEngine.calculateLandDealBrokerage(
            totalDealAmountInr = _dealTotalAmountText.value.toDoubleOrNull() ?: 4500000.0,
            tokenAmountInr = _dealTokenAmountText.value.toDoubleOrNull() ?: 100000.0,
            banakhatPercent = _dealBanakhatPctText.value.toDoubleOrNull() ?: 25.0,
            buyerBrokeragePercent = _dealBuyerBrokeragePctText.value.toDoubleOrNull() ?: 1.0,
            sellerBrokeragePercent = _dealSellerBrokeragePctText.value.toDoubleOrNull() ?: 1.0
        )
    }

    fun saveCurrentLandDealRecord() {
        viewModelScope.launch {
            val res = _landDealBrokerageResult.value
            val details = buildString {
                appendLine("Total Land Deal Value (કુલ સોદો): ₹${LandAndConstructionEngine.formatDec(res.totalDealAmountInr, 0)}")
                appendLine("Token Paid (બાના પેટે ટોકન): ₹${LandAndConstructionEngine.formatDec(res.tokenAmountInr, 0)}")
                appendLine("Banakhat Agreement (${LandAndConstructionEngine.formatDec(res.banakhatPercent, 1)}%): ₹${LandAndConstructionEngine.formatDec(res.banakhatAmountInr, 0)}")
                appendLine("Balance on Final Sale Deed (દસ્તાવેજ વખતે બાકી): ₹${LandAndConstructionEngine.formatDec(res.remainingOnDastavejInr, 0)}")
                appendLine("Buyer Brokerage (ખરીદનાર દલાલી): ₹${LandAndConstructionEngine.formatDec(res.buyerBrokerageInr, 0)}")
                appendLine("Seller Brokerage (વેચનાર દલાલી): ₹${LandAndConstructionEngine.formatDec(res.sellerBrokerageInr, 0)}")
                appendLine("Total Broker Commission (કુલ દલાલી): ₹${LandAndConstructionEngine.formatDec(res.totalBrokeragePoolInr, 0)}")
                appendLine("Total Buyer Outflow: ₹${LandAndConstructionEngine.formatDec(res.totalBuyerOutflowInr, 0)} | Net Seller Handover: ₹${LandAndConstructionEngine.formatDec(res.netSellerHandoverInr, 0)}")
            }
            val record = SurveyRecordEntity(
                title = "Land Deal Sauda (₹${LandAndConstructionEngine.formatDec(res.totalDealAmountInr, 0)})",
                category = "DAILY_LAND_DEAL_BROKER",
                stateRegion = "Land Sauda, Banakhat & Brokerage Ledger",
                primaryAreaSqFt = res.totalDealAmountInr,
                summaryMetrics = "Banakhat ₹${LandAndConstructionEngine.formatDec(res.banakhatAmountInr, 0)} • Dastavej ₹${LandAndConstructionEngine.formatDec(res.remainingOnDastavejInr, 0)} • Brokerage ₹${LandAndConstructionEngine.formatDec(res.totalBrokeragePoolInr, 0)}",
                detailedBreakdown = details.trim(),
                estimatedCostInr = res.totalBrokeragePoolInr
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved Land Deal & Brokerage Schedule to Vault"
            triggerInterstitialAfterTaskCompletion("Land Deal & Brokerage Schedule Saved")
        }
    }

    // --- Daily-Use Suite 4: Daily Farm, Dairy (Milk) & Landowner Rojmel ---
    fun onDailyRojmelInputsChange(
        milkLiters: String = _rojmelMilkLitersText.value,
        milkRate: String = _rojmelMilkRateText.value,
        otherIncome: String = _rojmelOtherIncomeText.value,
        cattleFeedExp: String = _rojmelCattleFeedExpText.value,
        dieselExp: String = _rojmelDieselExpText.value,
        miscExp: String = _rojmelMiscExpText.value
    ) {
        _rojmelMilkLitersText.value = milkLiters
        _rojmelMilkRateText.value = milkRate
        _rojmelOtherIncomeText.value = otherIncome
        _rojmelCattleFeedExpText.value = cattleFeedExp
        _rojmelDieselExpText.value = dieselExp
        _rojmelMiscExpText.value = miscExp
        _dailyRojmelResult.value = computeCurrentDailyRojmel()
    }

    private fun computeCurrentDailyRojmel(): DailyFarmDairyRojmelResult {
        return FarmerAndLandToolsEngine.calculateDailyFarmDairyRojmel(
            dailyMilkLiters = _rojmelMilkLitersText.value.toDoubleOrNull() ?: 28.0,
            milkRatePerLiterInr = _rojmelMilkRateText.value.toDoubleOrNull() ?: 62.0,
            otherDailyFarmIncomeInr = _rojmelOtherIncomeText.value.toDoubleOrNull() ?: 850.0,
            cattleFeedAndCharaExpenseInr = _rojmelCattleFeedExpText.value.toDoubleOrNull() ?: 520.0,
            dieselAndFertilizerExpenseInr = _rojmelDieselExpText.value.toDoubleOrNull() ?: 400.0,
            miscDailyExpenseInr = _rojmelMiscExpText.value.toDoubleOrNull() ?: 250.0
        )
    }

    fun saveCurrentDailyRojmelRecord() {
        viewModelScope.launch {
            val res = _dailyRojmelResult.value
            val details = buildString {
                appendLine("Daily Dairy Milk Income: ₹${LandAndConstructionEngine.formatDec(res.dailyMilkIncomeInr, 0)}")
                appendLine("Other Daily Farm/Vegetable Income: ₹${LandAndConstructionEngine.formatDec(res.otherDailyFarmIncomeInr, 0)}")
                appendLine("Total Daily Income (કુલ દૈનિક આવક): ₹${LandAndConstructionEngine.formatDec(res.totalDailyIncomeInr, 0)}")
                appendLine("Total Daily Expense (કુલ દૈનિક ખર્ચ): ₹${LandAndConstructionEngine.formatDec(res.totalDailyExpenseInr, 0)}")
                appendLine("Net Daily Cash Profit (રોજની ચોખ્ખી બચત): ₹${LandAndConstructionEngine.formatDec(res.netDailyProfitInr, 0)}")
                appendLine("Projected 30-Day Monthly Net Savings (માસિક અંદાજિત બચત): ₹${LandAndConstructionEngine.formatDec(res.projectedMonthlyNetProfitInr, 0)}")
            }
            val record = SurveyRecordEntity(
                title = "Daily Farm & Dairy Rojmel (Net ₹${LandAndConstructionEngine.formatDec(res.netDailyProfitInr, 0)}/day)",
                category = "DAILY_FARM_ROJMEL",
                stateRegion = "Daily Khedut & Dairy Cashbook",
                primaryAreaSqFt = res.totalDailyIncomeInr,
                summaryMetrics = "Income ₹${LandAndConstructionEngine.formatDec(res.totalDailyIncomeInr, 0)} • Expense ₹${LandAndConstructionEngine.formatDec(res.totalDailyExpenseInr, 0)} • Monthly ₹${LandAndConstructionEngine.formatDec(res.projectedMonthlyNetProfitInr, 0)}",
                detailedBreakdown = details.trim(),
                estimatedCostInr = res.projectedMonthlyNetProfitInr
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved Daily Farm & Dairy Rojmel Entry to Vault"
            triggerInterstitialAfterTaskCompletion("Daily Farm & Dairy Rojmel Saved")
        }
    }

    // --- Daily-Use Suite 0: Live Regional APMC & Global Govt Mandi Price Engine Actions ---
    fun selectApmcRegionZone(zone: ApmcRegionZone) {
        _selectedApmcRegionZone.value = zone
        val yardsInZone = LiveApmcMarketEngine.getYardsByZone(zone)
        if (yardsInZone.isNotEmpty()) {
            _selectedApmcYard.value = yardsInZone.first()
        }
    }

    fun selectApmcYard(yard: ApmcYardProfile) {
        _selectedApmcYard.value = yard
        _selectedApmcRegionZone.value = yard.regionZone
    }

    fun onApmcSearchQueryChange(query: String) {
        _apmcCropSearchQuery.value = query
    }

    fun toggleApmcPriceUnitMode(showQuintal: Boolean) {
        _apmcShowQuintalMode.value = showQuintal
    }

    fun syncLiveApmcMarketPrices(isSilentStartup: Boolean = false) {
        if (_isSyncingLiveApmc.value) return
        viewModelScope.launch {
            _isSyncingLiveApmc.value = true
            if (!isSilentStartup) {
                _statusBannerMessage.value = "Fetching official daily India Agmarknet market prices..."
            }
            val syncedSnapshot = LiveApmcMarketEngine.fetchAndSyncLiveApmcRates()
            _liveApmcSyncSnapshot.value = syncedSnapshot
            _isSyncingLiveApmc.value = false
            if (!isSilentStartup) {
                _statusBannerMessage.value = syncedSnapshot.syncStatusBanner
            }
        }
    }

    fun applyLiveQuoteToMandiCalculator(quote: LiveCropPriceQuote) {
        val formattedRate = LandAndConstructionEngine.formatDec(quote.modalPricePer20KgManInr, 0).replace(",", "")
        _mandiRatePerManText.value = formattedRate
        _dailyMandiBillResult.value = computeCurrentMandiBill()
        _selectedTab.value = AppTab.VAULT_SYSTEM
        _selectedVaultSubSection.value = VaultSubSection.DAILY_ROJMEL_MANDI_BROKER
        _vaultScrollTargetIndex.value = 2
        _statusBannerMessage.value = "Applied ${quote.cropNameLocal} (${quote.yardName}) Live Rate ₹${LandAndConstructionEngine.formatDec(quote.modalPricePer20KgManInr, 0)}/Man to Mandi Bill Calculator!"
    }

    fun saveLiveApmcYardSheetToVault() {
        viewModelScope.launch {
            val yard = _selectedApmcYard.value
            val snapshot = _liveApmcSyncSnapshot.value
            val yardQuotes = snapshot.quotes.filter { it.yardId == yard.yardId }
            if (yardQuotes.isEmpty()) return@launch

            val avgModalMan = yardQuotes.map { it.modalPricePer20KgManInr }.average()
            val details = buildString {
                appendLine("Market Yard / Board: ${yard.yardName}")
                appendLine("Region / District: ${yard.districtOrState} (${yard.countryName})")
                appendLine("Official Portal Source: ${yard.governmentPortalName}")
                appendLine("Updated Timestamp: ${snapshot.lastUpdatedTimeFormatted}")
                appendLine("--------------------------------------------------")
                yardQuotes.forEach { q ->
                    appendLine(
                        "• ${q.cropNameEn} (${q.varietyOrGrade}): " +
                            "20Kg Man ₹${LandAndConstructionEngine.formatDec(q.minPricePer20KgManInr, 0)} - ₹${LandAndConstructionEngine.formatDec(q.maxPricePer20KgManInr, 0)} " +
                            "(Modal ₹${LandAndConstructionEngine.formatDec(q.modalPricePer20KgManInr, 0)}/Man | ₹${LandAndConstructionEngine.formatDec(q.modalPricePerQuintalInr, 0)}/Quintal | " +
                            "Arrivals: ${q.arrivalBagsCount} Bags)"
                    )
                }
            }
            val record = SurveyRecordEntity(
                title = "Live APMC Bhav: ${yard.yardName}",
                category = "LIVE_APMC_MANDI_RATES",
                stateRegion = "${yard.districtOrState} (${yard.governmentPortalName})",
                primaryAreaSqFt = yardQuotes.size.toDouble(),
                summaryMetrics = "${yardQuotes.size} Crops • ${yardQuotes.first().cropNameLocal} ₹${LandAndConstructionEngine.formatDec(yardQuotes.first().modalPricePer20KgManInr, 0)}/Man • Updated ${snapshot.lastUpdatedTimeFormatted}",
                detailedBreakdown = details.trim(),
                estimatedCostInr = avgModalMan
            )
            repository.insert(record)
            _statusBannerMessage.value = "Saved ${yard.yardName} Daily Price Sheet (${yardQuotes.size} Crops) to Vault!"
            triggerInterstitialAfterTaskCompletion("Live APMC Yard Price Sheet Saved")
        }
    }

    // --- Saved Vault Actions ---
    fun deleteRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
            _statusBannerMessage.value = "Record removed from local vault"
        }
    }

    fun shareSavedRecordPdf(context: Context, record: SurveyRecordEntity) {
        PdfReportGenerator.generateAndSharePdf(context, record) { msg ->
            _statusBannerMessage.value = msg
            triggerInterstitialAfterTaskCompletion("Saved Vault Record PDF Exported")
        }
    }

    companion object {
        private const val PREFS_NAME = "daily_utility_lab_prefs"
        private const val KEY_SAVED_LANGUAGE = "saved_language_code"
        private const val KEY_HAS_ONBOARDED_LANGUAGE = "has_completed_language_onboarding"
        private const val KEY_APP_LAUNCH_COUNT = "app_launch_count"
        private const val KEY_AD_FREE_UNTIL_MS = "ad_free_until_epoch_ms"

        fun saveLanguagePreference(context: Context, languageCode: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putString(KEY_SAVED_LANGUAGE, languageCode)
                .putBoolean(KEY_HAS_ONBOARDED_LANGUAGE, true)
                .apply()
        }

        fun provideFactory(repository: SurveyRepository, context: Context? = null): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    val savedLang = prefs?.getString(KEY_SAVED_LANGUAGE, "en") ?: "en"
                    val hasOnboarded = prefs?.getBoolean(KEY_HAS_ONBOARDED_LANGUAGE, false) ?: false
                    val previousLaunches = prefs?.getInt(KEY_APP_LAUNCH_COUNT, 0) ?: 0
                    val newLaunchCount = previousLaunches + 1
                    val adFreeUntil = prefs?.getLong(KEY_AD_FREE_UNTIL_MS, 0L) ?: 0L
                    prefs?.edit()?.putInt(KEY_APP_LAUNCH_COUNT, newLaunchCount)?.apply()

                    return MainViewModel(
                        repository = repository,
                        initialLanguageCode = savedLang,
                        hasCompletedLanguageOnboarding = hasOnboarded,
                        initialLaunchCount = newLaunchCount,
                        initialAdFreeUntilEpochMs = adFreeUntil
                    ) as T
                }
            }
    }
}
