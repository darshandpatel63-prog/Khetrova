package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fence
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.SurveyRepository
import com.example.domain.AppLanguage
import com.example.domain.AppLocalizationEngine
import com.example.domain.LocalizedUiStrings
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.QuickFeatureDestination
import com.example.ui.components.AdMobAppOpenAdDialog
import com.example.ui.components.AdMobBannerBar
import com.example.ui.components.AdMobInterstitialDialog
import com.example.ui.components.AdMobRewardedAdDialog
import com.example.ui.screens.ConstructionEstimatorScreen
import com.example.ui.screens.IrregularPlotScreen
import com.example.ui.screens.LandConverterScreen
import com.example.ui.screens.MapAnd3DCutoutScreen
import com.example.ui.screens.VaultAndSystemScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = SurveyRepository(database.surveyDao())

        setContent {
            MyApplicationTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModel.provideFactory(repository, applicationContext)
                )
                DailyUtilityLabApp(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun DailyUtilityLabApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val uiStrings by viewModel.uiStrings.collectAsStateWithLifecycle()
    val isFirstLaunchLanguageGateOpen by viewModel.isFirstLaunchLanguageGateOpen.collectAsStateWithLifecycle()
    val isLanguageDialogOpen by viewModel.isLanguageDialogOpen.collectAsStateWithLifecycle()
    val isAllFeaturesMenuOpen by viewModel.isAllFeaturesMenuOpen.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusBannerMessage.collectAsStateWithLifecycle()
    val adFreeRemainingMinutes by viewModel.adFreeRemainingMinutes.collectAsStateWithLifecycle()
    val appLaunchCount by viewModel.appLaunchCount.collectAsStateWithLifecycle()
    val isInterstitialAdVisible by viewModel.isInterstitialAdVisible.collectAsStateWithLifecycle()
    val interstitialReason by viewModel.interstitialTriggerReason.collectAsStateWithLifecycle()
    val isRewardedAdVisible by viewModel.isRewardedAdVisible.collectAsStateWithLifecycle()
    val isAppOpenAdVisible by viewModel.isAppOpenAdVisible.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = selectedTab != AppTab.LAND_CONVERTER) {
        viewModel.selectTab(AppTab.LAND_CONVERTER)
    }

    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusBanner()
        }
    }

    val layoutDirection = if (selectedLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        if (isFirstLaunchLanguageGateOpen) {
            FirstLaunchLanguageOnboardingScreen(
                currentLanguage = selectedLanguage,
                onConfirmLanguage = { chosenLanguage ->
                    viewModel.completeFirstLaunchLanguageSelection(chosenLanguage, context)
                }
            )
            return@CompositionLocalProvider
        }

        if (isAllFeaturesMenuOpen) {
            AllFeaturesQuickMenuDialog(
                selectedLanguage = selectedLanguage,
                uiStrings = uiStrings,
                onSelectFeature = { destination ->
                    viewModel.navigateToQuickFeature(destination)
                },
                onDismiss = { viewModel.setAllFeaturesMenuOpen(false) }
            )
        }

        if (isLanguageDialogOpen) {
            LanguagePickerDialog(
                currentLanguage = selectedLanguage,
                uiStrings = uiStrings,
                onSelectLanguage = { viewModel.selectLanguage(it, context) },
                onDismiss = { viewModel.setLanguageDialogOpen(false) }
            )
        }

        if (isAppOpenAdVisible) {
            AdMobAppOpenAdDialog(
                launchNumber = appLaunchCount,
                onContinueToApp = { viewModel.dismissAppOpenAd() }
            )
        }

        if (isInterstitialAdVisible) {
            AdMobInterstitialDialog(
                triggerReason = interstitialReason,
                onDismiss = { viewModel.dismissInterstitialAd() },
                onSwitchToRewardedAdPass = {
                    viewModel.dismissInterstitialAd()
                    viewModel.openRewardedAdDialog()
                }
            )
        }

        if (isRewardedAdVisible) {
            AdMobRewardedAdDialog(
                onRewardEarnedAndClose = { viewModel.grantOneHourAdFreeReward(context) },
                onCancel = { viewModel.dismissRewardedAdDialog() }
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isExpandedScreen = maxWidth >= 640.dp

            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // AdMob Adaptive Banner Ad stays active on every screen (even during 1-Hr Ad-Free Pass)
                        AdMobBannerBar(
                            adFreeRemainingMinutes = adFreeRemainingMinutes,
                            onWatchRewardedAdClick = { viewModel.openRewardedAdDialog() }
                        )
                        if (!isExpandedScreen) {
                            NavigationBar(
                                modifier = Modifier.testTag("bottom_navigation_bar")
                            ) {
                                AppTab.entries.forEach { tab ->
                                    val isSelected = selectedTab == tab
                                    val tabLabel = getLocalizedTabLabel(tab, uiStrings)
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { viewModel.selectTab(tab) },
                                        icon = {
                                            Icon(
                                                imageVector = getTabIcon(tab, isSelected),
                                                contentDescription = tabLabel
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = tabLabel,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_tab_${tab.route}")
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (isExpandedScreen) {
                        NavigationRail(
                            modifier = Modifier
                                .fillMaxHeight()
                                .testTag("side_navigation_rail")
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            AppTab.entries.forEach { tab ->
                                val isSelected = selectedTab == tab
                                val tabLabel = getLocalizedTabLabel(tab, uiStrings)
                                NavigationRailItem(
                                    selected = isSelected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        Icon(
                                            imageVector = getTabIcon(tab, isSelected),
                                            contentDescription = tabLabel
                                        )
                                    },
                                    label = { Text(tabLabel, maxLines = 1) },
                                    modifier = Modifier
                                        .padding(vertical = 6.dp)
                                        .testTag("rail_tab_${tab.route}")
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        HeroBlueprintBanner(
                            uiStrings = uiStrings,
                            selectedLanguage = selectedLanguage,
                            onOpenAllFeaturesMenu = { viewModel.setAllFeaturesMenuOpen(true) },
                            onOpenLanguagePicker = { viewModel.setLanguageDialogOpen(true) }
                        )

                        QuickLanguageRibbon(
                            uiStrings = uiStrings,
                            selectedLanguage = selectedLanguage,
                            onOpenAllFeaturesMenu = { viewModel.setAllFeaturesMenuOpen(true) },
                            onSelectLanguage = { viewModel.selectLanguage(it, context) },
                            onOpenAllLanguages = { viewModel.setLanguageDialogOpen(true) }
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            when (selectedTab) {
                                AppTab.LAND_CONVERTER -> LandConverterScreen(viewModel = viewModel)
                                AppTab.MAP_3D_CUT -> MapAnd3DCutoutScreen(viewModel = viewModel)
                                AppTab.IRREGULAR_PLOT -> IrregularPlotScreen(viewModel = viewModel)
                                AppTab.CONSTRUCTION -> ConstructionEstimatorScreen(viewModel = viewModel)
                                AppTab.VAULT_SYSTEM -> VaultAndSystemScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Full-Screen First-Launch Language Selection Gate.
 * Defaults to English ("en") as requested, and saves the chosen language in SharedPreferences
 * so every future app launch opens directly in that saved language.
 */
@Composable
private fun FirstLaunchLanguageOnboardingScreen(
    currentLanguage: AppLanguage,
    onConfirmLanguage: (AppLanguage) -> Unit
) {
    var selectedLang by remember {
        mutableStateOf(
            AppLocalizationEngine.allLanguages.find { it.code == "en" } ?: currentLanguage
        )
    }
    var searchQuery by remember { mutableStateOf("") }

    val featuredCodes = remember {
        listOf("en", "gu", "hi", "mr", "ta", "te", "kn", "ml", "bn", "pa", "ja", "ko", "ar", "es", "fr", "de")
    }
    val featuredLanguages = remember {
        featuredCodes.mapNotNull { code -> AppLocalizationEngine.allLanguages.find { it.code == code } }
    }

    val filteredLanguages = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            AppLocalizationEngine.allLanguages
        } else {
            val q = searchQuery.trim().lowercase()
            AppLocalizationEngine.allLanguages.filter {
                it.englishName.lowercase().contains(q) ||
                    it.nativeName.lowercase().contains(q) ||
                    it.code.lowercase().contains(q) ||
                    it.regionGroup.lowercase().contains(q)
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier
            .fillMaxSize()
            .testTag("first_launch_language_gate")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp)
                    .padding(16.dp)
            ) {
                // Welcome Header Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF0B3B2D),
                                    Color(0xFF134E4A),
                                    Color(0xFF0F2922)
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Choose Language",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(26.dp)
                            )
                            Column {
                                Text(
                                    text = "Choose Your Language (Default: English)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Your selection is saved automatically for every future app launch (You can change it anytime later)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFD1FAE5)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Quick Select Popular Languages (English Default):",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Language Pills Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    featuredLanguages.forEach { lang ->
                        val isChosen = selectedLang.code == lang.code
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isChosen)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .border(
                                    width = if (isChosen) 2.dp else 0.dp,
                                    color = if (isChosen) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    // Step 1: Only select language (do not auto-close), so accidental taps can be changed before confirming below
                                    selectedLang = lang
                                }
                                .testTag("onboard_quick_lang_${lang.code}")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = lang.nativeName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = lang.englishName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search all 115+ Google Languages (English, ગુજરાતી, Hindi, 日本語...)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search language") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("onboard_language_search")
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredLanguages, key = { it.code }) { lang ->
                        val isSelected = lang.code == selectedLang.code
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Step 1: Only select language (do not auto-close), user confirms via bottom "Continue as ___" button
                                    selectedLang = lang
                                }
                                .testTag("onboard_list_lang_${lang.code}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${lang.nativeName}  (${lang.englishName})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = "${lang.regionGroup} • ISO: ${lang.code}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onConfirmLanguage(selectedLang) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("btn_confirm_initial_language")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppLocalizationEngine.getContinueButtonText(selectedLang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroBlueprintBanner(
    uiStrings: LocalizedUiStrings,
    selectedLanguage: AppLanguage,
    onOpenAllFeaturesMenu: () -> Unit,
    onOpenLanguagePicker: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF0B3B2D),
                        Color(0xFF134E4A),
                        Color(0xFF0F2922)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("hero_blueprint_banner")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.4f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prominent Top Menu Button ("☰ Menu • All Tools") on the Upper Side
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF59E0B),
                        modifier = Modifier
                            .clickable { onOpenAllFeaturesMenu() }
                            .testTag("btn_top_all_features_menu")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open All Features Menu",
                                tint = Color(0xFF0F2922),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = uiStrings.menuAllToolsBtn,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF0F2922),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0x33F59E0B)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Zero permission offline security",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = uiStrings.zeroPermissionBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFFDE68A),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = uiStrings.appTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiStrings.appSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD1FAE5),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1E4D3E),
                    modifier = Modifier
                        .clickable { onOpenLanguagePicker() }
                        .testTag("btn_open_language_dialog")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Select Language",
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${selectedLanguage.nativeName} • 115+",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_hero_blueprint),
                    contentDescription = "Architectural land survey and tile CAD vector illustration",
                    modifier = Modifier
                        .width(88.dp)
                        .height(40.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun QuickLanguageRibbon(
    uiStrings: LocalizedUiStrings,
    selectedLanguage: AppLanguage,
    onOpenAllFeaturesMenu: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onOpenAllLanguages: () -> Unit
) {
    val quickCodes = remember {
        listOf("en", "gu", "hi", "mr", "ta", "te", "kn", "ml", "bn", "pa", "ja", "ko", "zh-CN", "ar", "es", "fr", "de", "ru")
    }
    val quickLanguages = remember {
        quickCodes.mapNotNull { code -> AppLocalizationEngine.allLanguages.find { it.code == code } }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Quick 1-Tap All Features Launcher Pill in the Ribbon too
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onOpenAllFeaturesMenu() }
                .testTag("btn_ribbon_all_features_menu")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = "All Features Directory",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = uiStrings.allFeaturesRibbonBtn,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .clickable { onOpenAllLanguages() }
                .testTag("btn_all_115_languages")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "All 115+ Languages",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "115+ Languages",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        quickLanguages.forEach { lang ->
            val isSelected = lang.code == selectedLanguage.code
            FilterChip(
                selected = isSelected,
                onClick = { onSelectLanguage(lang) },
                label = {
                    Text(
                        text = lang.nativeName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("quick_lang_${lang.code}")
            )
        }
    }
}

private data class QuickMenuFeatureItem(
    val destination: QuickFeatureDestination,
    val category: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val accentColor: Color
)

/**
 * Upper Menu Modal Dialog ("All Features & Direct Page Launcher").
 * Lets the user jump directly to ANY page or sub-calculator in 1 tap (e.g. Khet-Talavdi Water Tank,
 * 7/12 Revenue Extract Decoder, Land Partition, Farm Fencing, Crop Seed & NPK, Tiles/Bricks/Plaster/Paint,
 * 3D Map Cutout, Irregular Plot CAD, Land Converter, Saved Vault, or 1-Hour Ad-Free Pass).
 */
@Composable
private fun AllFeaturesQuickMenuDialog(
    selectedLanguage: AppLanguage,
    uiStrings: LocalizedUiStrings,
    onSelectFeature: (QuickFeatureDestination) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val baseFeatures = remember {
        listOf(
            // Category 0: High-Frequency DAILY-USE Tools for Farmers, Landowner & Land Brokers
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.LIVE_APMC_MANDI_PRICES,
                category = "Daily Use: Farmer, Landowner & Broker Tools",
                title = "Live Regional APMC Market Yard & Global Govt Crop Prices (Auto-Updated)",
                subtitle = "Region-wise Saurashtra (Gondal/Rajkot/Mahuva), North/South Gujarat (Unjha/Deesa), All-India e-NAM & USA/Brazil/Europe/Japan Govt Rates",
                badge = "LIVE APMC",
                icon = Icons.Default.Agriculture,
                accentColor = Color(0xFF047857)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.DAILY_MANDI_BILL,
                category = "Daily Use: Farmer, Landowner & Broker Tools",
                title = "APMC Mandi Weight (20Kg Man / Quintal), Bardaan & Crop Sale Bill",
                subtitle = "Calculate daily mandi bags, bardaan tare deduction, 20Kg Man / Quintal price & net farmer payout",
                badge = "Daily Mandi",
                icon = Icons.Default.Agriculture,
                accentColor = Color(0xFFD97706)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.DAILY_LABOUR_TRACTOR_KHATA,
                category = "Daily Use: Farmer, Landowner & Broker Tools",
                title = "Daily Farm Labour (Dadiya Attendance), Upaad & Tractor Rent Khata",
                subtitle = "Track daily worker hazri, wage rate, tractor hours rent, diesel & advance (upaad) deduction",
                badge = "Daily Khata",
                icon = Icons.Default.Groups,
                accentColor = Color(0xFF059669)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.DAILY_LAND_BROKER_DEAL,
                category = "Daily Use: Farmer, Landowner & Broker Tools",
                title = "Land Broker Deal Commission (1%-2%), Token & Banakhat Schedule",
                subtitle = "Calculate buyer/seller brokerage, token money, banakhat % & final dastavej balance",
                badge = "Land Broker",
                icon = Icons.Default.Calculate,
                accentColor = Color(0xFF7C3AED)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.DAILY_FARM_DAIRY_ROJMEL,
                category = "Daily Use: Farmer, Landowner & Broker Tools",
                title = "Daily Farm, Dairy (Morning/Evening Milk) & Income-Expense Rojmel",
                subtitle = "Daily milk liters × fat rate, crop/vegetable income, cattle feed & diesel cashbook",
                badge = "Daily Rojmel",
                icon = Icons.Default.WaterDrop,
                accentColor = Color(0xFF0284C7)
            ),

            // Category 1: Khedut, Water Tank (Khet-Talavdi) & 7/12 Revenue Tools
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.KHET_TALAVDI_WATER_TANK,
                category = "Farmer (Khedut), Water & 7/12 Revenue Tools",
                title = "Khet-Talavdi / Farm Water Tank Capacity",
                subtitle = "Calculate rectangular farm pond & tank volume in Liters + irrigation hours",
                badge = "Direct Open",
                icon = Icons.Default.WaterDrop,
                accentColor = Color(0xFF0284C7)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.CIRCULAR_WELL_BOREWELL_HP,
                category = "Farmer (Khedut), Water & 7/12 Revenue Tools",
                title = "Circular Well Volume & Borewell Motor HP",
                subtitle = "Well storage in Liters + recommended submersible pump motor HP",
                badge = "Pump HP",
                icon = Icons.Default.Speed,
                accentColor = Color(0xFF0369A1)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.REVENUE_712_DECODER,
                category = "Farmer (Khedut), Water & 7/12 Revenue Tools",
                title = "7/12 & 8A Revenue Extract Hectare-Are-Sq.m Decoder",
                subtitle = "Convert government 7/12 H-Are-Sq.m into Vigha, Guntha, Acre, Sq.Ft & co-owner share",
                badge = "7/12 Utaro",
                icon = Icons.Default.Description,
                accentColor = Color(0xFF0D9488)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.KHEDUT_LAND_PARTITION,
                category = "Farmer (Khedut), Water & 7/12 Revenue Tools",
                title = "Family Land Partition (Heirs / Hissa Splitter)",
                subtitle = "Divide any land parcel among 2 to 20 family heirs with exact frontage & area",
                badge = "Heirs Split",
                icon = Icons.Default.Groups,
                accentColor = Color(0xFF15803D)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.KHEDUT_FARM_FENCING,
                category = "Farmer (Khedut), Water & 7/12 Revenue Tools",
                title = "Farm Boundary Fencing, Poles & Barbed Wire",
                subtitle = "Calculate cement/iron poles, barbed wire rows, weight (kg) & total fencing cost",
                badge = "Fencing",
                icon = Icons.Default.Fence,
                accentColor = Color(0xFF166534)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.KHEDUT_CROP_CALCULATOR,
                category = "Farmer (Khedut), Water & 7/12 Revenue Tools",
                title = "Crop Seed, Urea, DAP, MOP & Expected Yield",
                subtitle = "Plant population, seed bags, fertilizer bags & yield for Wheat, Cotton, Groundnut, etc.",
                badge = "Crop & NPK",
                icon = Icons.Default.Agriculture,
                accentColor = Color(0xFF65A30D)
            ),

            // Category 2: Land Measurement, Satellite Map & 2D/3D Plot CAD
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.LAND_UNIT_CONVERTER,
                category = "Land Measurement, Map Cutout & Plot CAD",
                title = "Global & State/Province Land Unit Converter",
                subtitle = "Convert Vigha, Guntha, Vaar, Sq.Ft, Marla, Kanal, Tsubo, Pyeong, Mu, Rai, Acre & Hectare",
                badge = "Main Page",
                icon = Icons.Default.Calculate,
                accentColor = Color(0xFF0F766E)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.LAND_STAMP_DUTY_VALUATION,
                category = "Land Measurement, Map Cutout & Plot CAD",
                title = "Jantri / Circle Rate, Stamp Duty & Registration Valuation",
                subtitle = "Calculate market land value, stamp duty & registration charges",
                badge = "Valuation",
                icon = Icons.Default.Calculate,
                accentColor = Color(0xFF047857)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.MAP_SATELLITE_LINE_SURVEY,
                category = "Land Measurement, Map Cutout & Plot CAD",
                title = "Interactive Map Line-Chart Survey & GPS Coordinate Pinning",
                subtitle = "Tap points or enter GPS coordinates on map to measure every side, angle & area",
                badge = "Live Map",
                icon = Icons.Default.Public,
                accentColor = Color(0xFF1D4ED8)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.MAP_3D_PARCEL_VIEWER,
                category = "Land Measurement, Map Cutout & Plot CAD",
                title = "3D Isometric Land Parcel Cutout & Soil Excavation",
                subtitle = "Rotate & tilt your land cutout in 3D + calculate foundation earthwork & truckloads",
                badge = "3D Cutout",
                icon = Icons.Default.Layers,
                accentColor = Color(0xFF2563EB)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.IRREGULAR_PLOT_CAD,
                category = "Land Measurement, Map Cutout & Plot CAD",
                title = "4-Sided Irregular Plot CAD Blueprint (Bretschneider Formula)",
                subtitle = "Exact triangulation area for non-rectangular plots with diagonal & compass bearings",
                badge = "2D CAD",
                icon = Icons.Default.Architecture,
                accentColor = Color(0xFF7C3AED)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.CONTRACTOR_ERROR_DETECTOR,
                category = "Land Measurement, Map Cutout & Plot CAD",
                title = "Anti-Fraud Contractor Measurement Error Detector",
                subtitle = "Compare average-side shortcut vs true triangulation to see how much money you save",
                badge = "Anti-Fraud",
                icon = Icons.Default.Shield,
                accentColor = Color(0xFFB45309)
            ),

            // Category 3: Construction Materials (Tiles, Bricks, Plaster, Paint)
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.CONSTRUCTION_TILES,
                category = "Construction & Home Material Estimators",
                title = "Flooring Tiles, Boxes & Cement-Sand Bedding Calculator",
                subtitle = "Exact tile count, boxes, wastage, cement bags & sand for any room dimension",
                badge = "Tiles",
                icon = Icons.Default.Construction,
                accentColor = Color(0xFFD97706)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.CONSTRUCTION_BRICKWORK,
                category = "Construction & Home Material Estimators",
                title = "Brickwork Wall, Bricks Count & Mortar Calculator",
                subtitle = "Calculate red/fly-ash bricks, cement bags & sand for 4.5-inch or 9-inch walls",
                badge = "Brickwork",
                icon = Icons.Default.Construction,
                accentColor = Color(0xFFC2410C)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.CONSTRUCTION_PLASTER,
                category = "Construction & Home Material Estimators",
                title = "Wall & Ceiling Plaster Cement + Sand Estimator",
                subtitle = "12mm/15mm/20mm plaster dry mortar volume, cement bags & sand CFT",
                badge = "Plaster",
                icon = Icons.Default.Construction,
                accentColor = Color(0xFFB45309)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.CONSTRUCTION_PAINT,
                category = "Construction & Home Material Estimators",
                title = "Wall Paint, Primer & Putty Liters Calculator",
                subtitle = "Net paintable area after door/window deduction + putty, primer & emulsion liters",
                badge = "Paint",
                icon = Icons.Default.Construction,
                accentColor = Color(0xFF9333EA)
            ),

            // Category 4: Saved Vault, Ad-Free Pass, Languages & Legal
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.SAVED_SURVEY_VAULT,
                category = "Saved Records, Language & App Controls",
                title = "Offline Survey Vault & JSON Backup",
                subtitle = "View, verify SHA-256 checksums, or export all saved land & material records",
                badge = "Vault",
                icon = Icons.Default.FolderSpecial,
                accentColor = Color(0xFF334155)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.REWARDED_1HR_AD_FREE,
                category = "Saved Records, Language & App Controls",
                title = "Activate 1-Hour Popup Ad-Free Pass (Rewarded Ad)",
                subtitle = "Watch 1 short test video to disable all Interstitial & App Open ads for 60 minutes",
                badge = "1-Hr Pass",
                icon = Icons.Default.PlayCircleFilled,
                accentColor = Color(0xFF059669)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.LANGUAGE_SWITCHER_115,
                category = "Saved Records, Language & App Controls",
                title = "Change App Language (115+ Google Languages)",
                subtitle = "Switch between English (Default), Gujarati, Hindi, Marathi, Tamil, Japanese & 115+ more",
                badge = "115+ Langs",
                icon = Icons.Default.Language,
                accentColor = Color(0xFF4F46E5)
            ),
            QuickMenuFeatureItem(
                destination = QuickFeatureDestination.LEGAL_AND_PRIVACY,
                category = "Saved Records, Language & App Controls",
                title = "13 Legal, Privacy & Offline Zero-Permission Policies",
                subtitle = "Inspect Privacy Policy, Terms of Service, Land Disclaimer & Data Security",
                badge = "Legal",
                icon = Icons.Default.Gavel,
                accentColor = Color(0xFF475569)
            )
        )
    }

    // Localize every menu option into the user's selected language (Gujarati, Hindi, Marathi, or any of 115+ languages)
    val allFeatures = remember(selectedLanguage.code, baseFeatures) {
        baseFeatures.map { raw ->
            val loc = AppLocalizationEngine.getLocalizedMenuEntry(
                destName = raw.destination.name,
                langCode = selectedLanguage.code,
                defaultCategory = raw.category,
                defaultTitle = raw.title,
                defaultSubtitle = raw.subtitle,
                defaultBadge = raw.badge
            )
            raw.copy(
                category = loc.category,
                title = loc.title,
                subtitle = loc.subtitle,
                badge = loc.badge
            )
        }
    }

    val filteredFeatures = remember(searchQuery, allFeatures) {
        if (searchQuery.isBlank()) {
            allFeatures
        } else {
            val q = searchQuery.trim().lowercase()
            allFeatures.filter {
                it.title.lowercase().contains(q) ||
                    it.subtitle.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.badge.lowercase().contains(q)
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(620.dp)
                .testTag("all_features_quick_menu_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Column {
                            Text(
                                text = uiStrings.allFeaturesMenuTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = uiStrings.allFeaturesMenuSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_all_features_menu")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close menu")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(uiStrings.allFeaturesSearchHint) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search tools") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("all_features_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredFeatures, key = { it.destination.name }) { item ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectFeature(item.destination) }
                                .testTag("menu_feature_${item.destination.name}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = item.accentColor.copy(alpha = 0.15f),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = item.accentColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = item.accentColor.copy(alpha = 0.16f)
                                        ) {
                                            Text(
                                                text = item.badge,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = item.accentColor,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = item.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = item.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open ${item.title}",
                                    tint = item.accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguagePickerDialog(
    currentLanguage: AppLanguage,
    uiStrings: LocalizedUiStrings,
    onSelectLanguage: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredLanguages = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            AppLocalizationEngine.allLanguages
        } else {
            val q = searchQuery.trim().lowercase()
            AppLocalizationEngine.allLanguages.filter {
                it.englishName.lowercase().contains(q) ||
                    it.nativeName.lowercase().contains(q) ||
                    it.code.lowercase().contains(q) ||
                    it.regionGroup.lowercase().contains(q)
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(540.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uiStrings.languagePickerTitle,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${AppLocalizationEngine.allLanguages.size} Languages • Saved Automatically (Default: English)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close language picker")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(uiStrings.searchLanguageHint) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("language_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredLanguages, key = { it.code }) { lang ->
                        val isSelected = lang.code == currentLanguage.code
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectLanguage(lang) }
                                .testTag("dialog_lang_${lang.code}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${lang.nativeName}  (${lang.englishName})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = "${lang.regionGroup} • ISO: ${lang.code}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getLocalizedTabLabel(tab: AppTab, uiStrings: LocalizedUiStrings): String = when (tab) {
    AppTab.LAND_CONVERTER -> uiStrings.navLandUnits
    AppTab.MAP_3D_CUT -> uiStrings.navMap3D
    AppTab.IRREGULAR_PLOT -> uiStrings.navPlotCad
    AppTab.CONSTRUCTION -> uiStrings.navMaterials
    AppTab.VAULT_SYSTEM -> uiStrings.navVaultCode
}

private fun getTabIcon(tab: AppTab, isSelected: Boolean) = when (tab) {
    AppTab.LAND_CONVERTER -> if (isSelected) Icons.Filled.Calculate else Icons.Outlined.Calculate
    AppTab.MAP_3D_CUT -> if (isSelected) Icons.Filled.Public else Icons.Outlined.Public
    AppTab.IRREGULAR_PLOT -> if (isSelected) Icons.Filled.Architecture else Icons.Outlined.Architecture
    AppTab.CONSTRUCTION -> if (isSelected) Icons.Filled.Construction else Icons.Outlined.Construction
    AppTab.VAULT_SYSTEM -> if (isSelected) Icons.Filled.FolderSpecial else Icons.Outlined.FolderSpecial
}
