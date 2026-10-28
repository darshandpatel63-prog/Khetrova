package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.LandAndConstructionEngine
import com.example.domain.PolyglotAndLegalData
import com.example.ui.MainViewModel
import com.example.ui.VaultSubSection
import com.example.ui.components.AdMobNativeAdCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VaultAndSystemScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiStrings by viewModel.uiStrings.collectAsStateWithLifecycle()
    val savedRecords by viewModel.savedRecords.collectAsStateWithLifecycle()
    val adFreeMinutes by viewModel.adFreeRemainingMinutes.collectAsStateWithLifecycle()
    val appLaunchCount by viewModel.appLaunchCount.collectAsStateWithLifecycle()

    // 7/12 Revenue Extract & Farm Water Tank states
    val hectares712 by viewModel.hectares712Text.collectAsStateWithLifecycle()
    val are712 by viewModel.are712Text.collectAsStateWithLifecycle()
    val sqMeters712 by viewModel.sqMeters712Text.collectAsStateWithLifecycle()
    val revenue712Result by viewModel.revenue712Result.collectAsStateWithLifecycle()

    val isCircularWell by viewModel.isCircularWell.collectAsStateWithLifecycle()
    val tankLengthOrDiam by viewModel.tankLengthOrDiameterFtText.collectAsStateWithLifecycle()
    val tankWidth by viewModel.tankWidthFtText.collectAsStateWithLifecycle()
    val tankDepth by viewModel.tankDepthFtText.collectAsStateWithLifecycle()
    val borewellDepth by viewModel.borewellDepthFtText.collectAsStateWithLifecycle()
    val waterTankResult by viewModel.waterTankResult.collectAsStateWithLifecycle()

    val activeSubSection by viewModel.selectedVaultSubSection.collectAsStateWithLifecycle()
    val scrollTargetIndex by viewModel.vaultScrollTargetIndex.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var expandedLegalId by remember { mutableStateOf<String?>("privacy_policy") }

    LaunchedEffect(scrollTargetIndex, activeSubSection) {
        val target = scrollTargetIndex
        if (target != null) {
            listState.animateScrollToItem(target.coerceAtLeast(0))
            viewModel.clearVaultScrollTarget()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 760.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VaultSubSection.entries.forEach { sec ->
                        val selected = sec == activeSubSection
                        val label = when (sec) {
                            VaultSubSection.SAVED_VAULT -> uiStrings.savedVaultTab
                            VaultSubSection.DAILY_ROJMEL_MANDI_BROKER -> uiStrings.dailyRojmelTab
                            VaultSubSection.FARM_712_AND_WATER_TOOLS -> uiStrings.polyglotTab
                            VaultSubSection.LEGAL_COMPLIANCE -> uiStrings.legalTab
                        }
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.selectVaultSubSection(sec) },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = {
                                val icon = when (sec) {
                                    VaultSubSection.SAVED_VAULT -> Icons.Default.FolderSpecial
                                    VaultSubSection.DAILY_ROJMEL_MANDI_BROKER -> Icons.Default.WorkspacePremium
                                    VaultSubSection.FARM_712_AND_WATER_TOOLS -> Icons.Default.Agriculture
                                    VaultSubSection.LEGAL_COMPLIANCE -> Icons.Default.Gavel
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("vault_subtab_${sec.name}")
                        )
                    }
                }
            }

            // Rewarded Ad 1-Hour Pass & AdMob Test Controls Card
            item {
                AdMobRewardedPassAndTestControlCard(
                    adFreeRemainingMinutes = adFreeMinutes,
                    appLaunchCount = appLaunchCount,
                    onWatchRewardedAd = { viewModel.openRewardedAdDialog() },
                    onTestInterstitialAd = {
                        viewModel.triggerInterstitialAfterTaskCompletion(
                            taskReason = "Manual Test Interstitial Trigger",
                            forceForTest = true
                        )
                    },
                    onSimulate6thAppOpenAd = {
                        viewModel.simulateSixthAppOpenWithTwoSecondDelay(context)
                    },
                    onResetAdFreePass = {
                        viewModel.clearOneHourAdFreePassForTesting(context)
                    }
                )
            }

            when (activeSubSection) {
                VaultSubSection.SAVED_VAULT -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = "Offline database",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${uiStrings.savedVaultTab} (${savedRecords.size})",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = uiStrings.zeroPermissionBadge,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    if (savedRecords.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FolderSpecial,
                                        contentDescription = "Empty vault",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "No Saved Surveys Yet",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    } else {
                        itemsIndexed(savedRecords, key = { _, it -> it.id }) { index, record ->
                            val dateStr = remember(record.timestamp) {
                                SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(Date(record.timestamp))
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("saved_record_${record.id}"),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {
                                            Text(
                                                text = record.category,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Text(
                                            text = dateStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = record.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = record.summaryMetrics,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = record.detailedBreakdown,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = LandAndConstructionEngine.formatCurrency(record.estimatedCostInr),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Row {
                                            IconButton(
                                                onClick = { viewModel.shareSavedRecordPdf(context, record) },
                                                modifier = Modifier.testTag("btn_pdf_record_${record.id}")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PictureAsPdf,
                                                    contentDescription = uiStrings.exportPdf,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteRecord(record.id) },
                                                modifier = Modifier.testTag("btn_delete_record_${record.id}")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.DeleteOutline,
                                                    contentDescription = "Delete record",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Insert Native Ad Card after every 3 saved boxes (more boxes = more native ads)
                            if ((index + 1) % 3 == 0) {
                                Spacer(modifier = Modifier.height(12.dp))
                                AdMobNativeAdCard(slotIndex = index / 3 + 10)
                            }
                        }
                    }
                }

                VaultSubSection.FARM_712_AND_WATER_TOOLS -> {
                    // Box 1: 7/12 & 8A Revenue Extract Hectare-Are-Sq.M Decoder
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_712_decoder"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Agriculture,
                                        contentDescription = "7/12 Extract Decoder",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "1. 7/12 & 8-A REVENUE EXTRACT DECODER (૭/૧૨ હેક્ટર-આર-ચો.મી. ગણતરીકાર)",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Enter Hectare - Are - Sq.Meter from your 7/12 document to get exact Vigha, Guntha, Vaar & KCC Loan",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = hectares712,
                                        onValueChange = { viewModel.on712InputsChange(hectares = it) },
                                        label = { Text("Hectare (હેક્ટર)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_712_hectare")
                                    )
                                    OutlinedTextField(
                                        value = are712,
                                        onValueChange = { viewModel.on712InputsChange(are = it) },
                                        label = { Text("Are (આર / ગુંઠા)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_712_are")
                                    )
                                    OutlinedTextField(
                                        value = sqMeters712,
                                        onValueChange = { viewModel.on712InputsChange(sqMeters = it) },
                                        label = { Text("Sq.M (ચો.મી.)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_712_sqm")
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "7/12 EXTRACT INSTANT BREAKDOWN (${revenue712Result.hectares.toInt()}-${revenue712Result.ares.toInt()}-${revenue712Result.sqMeters.toInt()})",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "• Saurashtra 16-Guntha Vigha: ${LandAndConstructionEngine.formatDec(revenue712Result.saurashtraVigha16Guntha, 3)} Vigha (વીઘા)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "• North/Central Gujarat 23.5-Guntha Pucca Vigha: ${LandAndConstructionEngine.formatDec(revenue712Result.northGujaratVigha23_5Guntha, 3)} Vigha",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "• Kachha Vigha: ${LandAndConstructionEngine.formatDec(revenue712Result.centralGujaratKachhaVigha, 3)} Vigha  |  Guntha: ${LandAndConstructionEngine.formatDec(revenue712Result.totalGuntha, 2)} Guntha",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "• Acres: ${LandAndConstructionEngine.formatDec(revenue712Result.totalAcres, 3)} Acre  |  Vaar: ${LandAndConstructionEngine.formatDec(revenue712Result.totalVaarSqYd, 1)} Vaar",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "• Total Area: ${LandAndConstructionEngine.formatDec(revenue712Result.totalSqMeters, 2)} Sq.M  =  ${LandAndConstructionEngine.formatDec(revenue712Result.totalSqFeet, 1)} Sq.Ft",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                        Text(
                                            text = "Est. Kisan Credit Card (KCC) Crop Loan Limit: ${LandAndConstructionEngine.formatCurrency(revenue712Result.kccCropLoanEligibilityInr)}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = { viewModel.saveCurrent712DecodeRecord() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_save_712_record")
                                ) {
                                    Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save 7/12 Calculation to Vault")
                                }
                            }
                        }
                    }

                    // Native Ad between Box 1 and Box 2
                    item {
                        AdMobNativeAdCard(slotIndex = 20)
                    }

                    // Box 2: Farm Water Tank / Circular Well Capacity & Borewell Submersible Pump HP Calculator
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_water_tank_calculator"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "Water Tank & Borewell Calculator",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "2. FARM WATER TANK, CIRCULAR WELL & BOREWELL MOTOR HP (ખેત-તલાવડી / કૂવો / બોરવેલ મોટર)",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Calculate exact water storage in Liters and required Submersible Pump HP",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = !isCircularWell,
                                        onClick = { viewModel.onWaterTankInputsChange(circularWell = false) },
                                        label = { Text("Rectangular Tank (લંબચોરસ ટાંકો)") },
                                        modifier = Modifier.testTag("chip_rect_tank")
                                    )
                                    FilterChip(
                                        selected = isCircularWell,
                                        onClick = { viewModel.onWaterTankInputsChange(circularWell = true) },
                                        label = { Text("Circular Well (ગોળ કૂવો)") },
                                        modifier = Modifier.testTag("chip_circular_well")
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = tankLengthOrDiam,
                                        onValueChange = { viewModel.onWaterTankInputsChange(lengthOrDiamFt = it) },
                                        label = {
                                            Text(if (isCircularWell) "Well Diameter (વ્યાસ ft)" else "Tank Length (લંબાઈ ft)")
                                        },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (!isCircularWell) {
                                        OutlinedTextField(
                                            value = tankWidth,
                                            onValueChange = { viewModel.onWaterTankInputsChange(widthFt = it) },
                                            label = { Text("Tank Width (પહોળાઈ ft)") },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    OutlinedTextField(
                                        value = tankDepth,
                                        onValueChange = { viewModel.onWaterTankInputsChange(depthFt = it) },
                                        label = { Text("Water Depth (ઊંડાઈ ft)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = borewellDepth,
                                    onValueChange = { viewModel.onWaterTankInputsChange(borewellDepthFt = it) },
                                    label = { Text("Borewell Total Depth in Feet (બોરવેલની ઊંડાઈ ફૂટમાં - e.g. 320 ft)") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "Total Water Capacity: ${LandAndConstructionEngine.formatDec(waterTankResult.totalCapacityLiters, 0)} Liters (લીટર)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "• Volume: ${LandAndConstructionEngine.formatDec(waterTankResult.volumeCubicFeet, 1)} Cu.Ft (${LandAndConstructionEngine.formatDec(waterTankResult.volumeCubicMeters, 2)} m³)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Text(
                                            text = "• Equivalent Tractor Tankers (4,000L): ${LandAndConstructionEngine.formatDec(waterTankResult.totalCapacityLiters / 4000.0, 1)} Tankers",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Text(
                                            text = "• Irrigation Runtime (@250 LPM): ${LandAndConstructionEngine.formatDec(waterTankResult.irrigationHoursAt250Lpm, 1)} Hours",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                        Text(
                                            text = "Recommended Borewell Submersible Motor: ${waterTankResult.recommendedSubmersibleHp} HP (${LandAndConstructionEngine.formatDec(waterTankResult.powerConsumptionKwhPerHour, 2)} kW/hr)",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = { viewModel.saveCurrentWaterTankRecord() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_save_water_tank")
                                ) {
                                    Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save Water Tank & Borewell Spec to Vault")
                                }
                            }
                        }
                    }
                }

                VaultSubSection.DAILY_ROJMEL_MANDI_BROKER -> {
                    // Item index 1: Live Regional APMC & Global Govt Mandi Price Board (Auto-Sync)
                    item {
                        com.example.ui.components.LiveApmcPriceBoardCard(viewModel = viewModel)
                    }

                    // Item index 2: Daily APMC Mandi Weight (20 Kg Man / Quintal), Bardaan & Crop Sale Bill Calculator
                    item {
                        DailyMandiBillCalculatorCard(viewModel = viewModel)
                    }

                    // Item index 3: Native Ad after box 1-2
                    item {
                        AdMobNativeAdCard(slotIndex = 41)
                    }

                    // Item index 4: Daily Farm Labour (Dadiya), Tractor Hours & Upaad (Advance) Khata Calculator
                    item {
                        DailyLabourAndTractorKhataCard(viewModel = viewModel)
                    }

                    // Item index 5: Native Ad after box 4
                    item {
                        AdMobNativeAdCard(slotIndex = 42)
                    }

                    // Item index 6: Daily Land Broker Deal Commission, Token & Banakhat Installment Schedule
                    item {
                        DailyLandBrokerCommissionCard(viewModel = viewModel)
                    }

                    // Item index 7: Native Ad after box 6
                    item {
                        AdMobNativeAdCard(slotIndex = 43)
                    }

                    // Item index 8: Daily Farm, Dairy (Milk) & Landowner Income-Expense Rojmel Calculator
                    item {
                        DailyFarmAndDairyRojmelCard(viewModel = viewModel)
                    }
                }

                VaultSubSection.LEGAL_COMPLIANCE -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = uiStrings.legalTab,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    itemsIndexed(PolyglotAndLegalData.legalDocuments, key = { _, it -> it.id }) { index, doc ->
                        val isExpanded = expandedLegalId == doc.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedLegalId = if (isExpanded) null else doc.id
                                }
                                .testTag("legal_card_${doc.id}"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = doc.title,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Text(
                                            text = doc.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "Expand legal policy"
                                    )
                                }
                                AnimatedVisibility(visible = isExpanded) {
                                    Column {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                                        Text(
                                            text = doc.content,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // Native Ad after every 3 legal policy boxes (more boxes = more native ads)
                        if ((index + 1) % 3 == 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            AdMobNativeAdCard(slotIndex = index / 3 + 30)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AdMobRewardedPassAndTestControlCard(
    adFreeRemainingMinutes: Int,
    appLaunchCount: Int,
    onWatchRewardedAd: () -> Unit,
    onTestInterstitialAd: () -> Unit,
    onSimulate6thAppOpenAd: () -> Unit,
    onResetAdFreePass: () -> Unit
) {
    val isAdFreeActive = adFreeRemainingMinutes > 0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admob_rewarded_control_card"),
        colors = CardDefaults.cardColors(
            containerColor = if (isAdFreeActive)
                Color(0xFF064E3B)
            else MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Rewarded 1-Hour Ad-Free Pass",
                        tint = if (isAdFreeActive) Color(0xFFFBBF24) else MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isAdFreeActive)
                                "1-Hour Ad-Free Pass ACTIVE ($adFreeRemainingMinutes mins left)"
                            else "Watch 1 Rewarded Ad → Get 1 Hour Ad-Free!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isAdFreeActive) Color.White else MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Removes Interstitial & App Open Ads for 60 mins • Banner & Native Ads stay active • App Open Count: #$appLaunchCount (Every 6th open @ 2s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isAdFreeActive) Color(0xFFD1FAE5) else MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onWatchRewardedAd,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD97706),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("btn_watch_rewarded_ad")
                ) {
                    Icon(Icons.Default.PlayCircleFilled, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAdFreeActive) "Extend 1-Hour Pass ($adFreeRemainingMinutes m)" else "Watch Rewarded Ad (1 Hr Ad-Free)",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onSimulate6thAppOpenAd,
                    modifier = Modifier.testTag("btn_simulate_6th_app_open_ad")
                ) {
                    Text("Test 6th App Open Ad (2s)")
                }

                OutlinedButton(
                    onClick = onTestInterstitialAd,
                    modifier = Modifier.testTag("btn_test_interstitial_ad")
                ) {
                    Text("Test Interstitial Ad")
                }

                if (isAdFreeActive) {
                    OutlinedButton(
                        onClick = onResetAdFreePass,
                        modifier = Modifier.testTag("btn_reset_ad_free_pass")
                    ) {
                        Text("Reset Pass")
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyMandiBillCalculatorCard(viewModel: MainViewModel) {
    val bags by viewModel.mandiBagsText.collectAsStateWithLifecycle()
    val bagWeightKg by viewModel.mandiBagWeightKgText.collectAsStateWithLifecycle()
    val bardaanKg by viewModel.mandiBardaanKgText.collectAsStateWithLifecycle()
    val ratePerMan by viewModel.mandiRatePerManText.collectAsStateWithLifecycle()
    val commissionPct by viewModel.mandiCommissionPctText.collectAsStateWithLifecycle()
    val hamaliPerBag by viewModel.mandiHamaliPerBagText.collectAsStateWithLifecycle()
    val res by viewModel.dailyMandiBillResult.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_mandi_bill_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "APMC Mandi Bill",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "1. Daily APMC Mandi Weight (20 Kg મણ / ક્વિન્ટલ), Bardaan & Crop Sale Bill",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "રોજેરોજ માર્કેટ યાર્ડ (APMC) માં ગુણી/કટ્ટાનું વજન, બારદાન કપાત, મણ (20 Kg) / ક્વિન્ટલનો ભાવ અને ચોખ્ખો હિસાબ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = bags,
                    onValueChange = { viewModel.onMandiInputsChange(bags = it) },
                    label = { Text("Total Bags (ગુણી/કટ્ટા)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_mandi_bags")
                )
                OutlinedTextField(
                    value = bagWeightKg,
                    onValueChange = { viewModel.onMandiInputsChange(bagWeightKg = it) },
                    label = { Text("Avg Kg/Bag (કિલો/ગુણી)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_mandi_bag_kg")
                )
                OutlinedTextField(
                    value = bardaanKg,
                    onValueChange = { viewModel.onMandiInputsChange(bardaanKg = it) },
                    label = { Text("Bardaan Kg (બારદાન)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_mandi_bardaan_kg")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = ratePerMan,
                    onValueChange = { viewModel.onMandiInputsChange(ratePerMan = it) },
                    label = { Text("Rate / 20Kg Man (₹/મણ)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_mandi_rate_man")
                )
                OutlinedTextField(
                    value = commissionPct,
                    onValueChange = { viewModel.onMandiInputsChange(commissionPct = it) },
                    label = { Text("Yard Comm % (દલાલી%)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = hamaliPerBag,
                    onValueChange = { viewModel.onMandiInputsChange(hamaliPerBag = it) },
                    label = { Text("Hamali ₹/Bag (હમાલી)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Net Payable to Farmer (ખેડૂતને મળતી ચોખ્ખી રકમ): ₹${LandAndConstructionEngine.formatDec(res.netPayableToFarmerInr, 0)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Gross Weight: ${LandAndConstructionEngine.formatDec(res.grossWeightKg, 1)} Kg | Less Bardaan Tare: -${LandAndConstructionEngine.formatDec(res.totalBardaanDeductionKg, 2)} Kg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "• Net Weight: ${LandAndConstructionEngine.formatDec(res.netWeightKg, 2)} Kg = ${LandAndConstructionEngine.formatDec(res.netWeightMan20Kg, 2)} Man (20 Kg મણ) = ${LandAndConstructionEngine.formatDec(res.netWeightQuintal, 2)} Quintal (ક્વિન્ટલ) = ${LandAndConstructionEngine.formatDec(res.netWeightKhandi, 2)} Khandi",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "• Equivalent Rate: ₹${LandAndConstructionEngine.formatDec(res.ratePer20KgManInr, 0)} / Man = ₹${LandAndConstructionEngine.formatDec(res.equivalentRatePerQuintalInr, 0)} / Quintal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "• Gross Bill: ₹${LandAndConstructionEngine.formatDec(res.grossAmountInr, 0)} | Less Comm + Hamali: -₹${LandAndConstructionEngine.formatDec(res.commissionAmountInr + res.hamaliAndTolaiInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.saveCurrentMandiBillRecord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save_mandi_bill")
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save APMC Mandi Crop Bill to Vault")
            }
        }
    }
}

@Composable
private fun DailyLabourAndTractorKhataCard(viewModel: MainViewModel) {
    val workers by viewModel.labourWorkersCountText.collectAsStateWithLifecycle()
    val days by viewModel.labourDaysWorkedText.collectAsStateWithLifecycle()
    val dailyWage by viewModel.labourDailyWageText.collectAsStateWithLifecycle()
    val tractorHours by viewModel.tractorHoursText.collectAsStateWithLifecycle()
    val tractorRate by viewModel.tractorRatePerHourText.collectAsStateWithLifecycle()
    val upaadAdvance by viewModel.upaadAdvanceText.collectAsStateWithLifecycle()
    val res by viewModel.dailyLabourKhataResult.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_labour_tractor_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "Daily Farm Labour & Tractor Khata",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "2. Daily Farm Labour (દાડિયા/મજૂર હાજરી), Upaad & Tractor Rent Khata",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "રોજેરોજ ખેત-મજૂરના દાડિયા, ઉપાડ (Advance) કપાત અને ટ્રેક્ટરના કલાકના ભાડાનો ચોખ્ખો હિસાબ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = workers,
                    onValueChange = { viewModel.onLabourKhataInputsChange(workers = it) },
                    label = { Text("Workers (મજૂર સંખ્યા)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_labour_workers")
                )
                OutlinedTextField(
                    value = days,
                    onValueChange = { viewModel.onLabourKhataInputsChange(days = it) },
                    label = { Text("Days (દિવસ/હાજરી)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_labour_days")
                )
                OutlinedTextField(
                    value = dailyWage,
                    onValueChange = { viewModel.onLabourKhataInputsChange(dailyWage = it) },
                    label = { Text("Daily Wage ₹ (રોજ)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_labour_wage")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tractorHours,
                    onValueChange = { viewModel.onLabourKhataInputsChange(tractorHours = it) },
                    label = { Text("Tractor Hrs (કલાક)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = tractorRate,
                    onValueChange = { viewModel.onLabourKhataInputsChange(tractorRate = it) },
                    label = { Text("Rate ₹/Hr (ભાડું/કલાક)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = upaadAdvance,
                    onValueChange = { viewModel.onLabourKhataInputsChange(upaadAdvance = it) },
                    label = { Text("Upaad Advance ₹ (ઉપાડ)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Net Balance Payable (ઉપાડ કાપીને બાકી ચૂકવવાપાત્ર): ₹${LandAndConstructionEngine.formatDec(res.netBalancePayableInr, 0)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Total Person-Days (કુલ હાજરી): ${LandAndConstructionEngine.formatDec(res.totalPersonDays, 1)} Hazri → Labour Bill: ₹${LandAndConstructionEngine.formatDec(res.totalLabourWageInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "• Tractor Work (${LandAndConstructionEngine.formatDec(res.tractorHours, 1)} Hrs): ₹${LandAndConstructionEngine.formatDec(res.totalTractorRentInr, 0)} (Est. Diesel: ${LandAndConstructionEngine.formatDec(res.estimatedDieselLiters, 1)} L)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "• Gross Combined Bill: ₹${LandAndConstructionEngine.formatDec(res.grossCombinedBillInr, 0)} | Less Upaad Paid: -₹${LandAndConstructionEngine.formatDec(res.upaadAdvanceGivenInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.saveCurrentLabourKhataRecord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save_labour_khata")
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Labour & Tractor Khata to Vault")
            }
        }
    }
}

@Composable
private fun DailyLandBrokerCommissionCard(viewModel: MainViewModel) {
    val totalDealAmount by viewModel.dealTotalAmountText.collectAsStateWithLifecycle()
    val tokenAmount by viewModel.dealTokenAmountText.collectAsStateWithLifecycle()
    val banakhatPct by viewModel.dealBanakhatPctText.collectAsStateWithLifecycle()
    val buyerBrokeragePct by viewModel.dealBuyerBrokeragePctText.collectAsStateWithLifecycle()
    val sellerBrokeragePct by viewModel.dealSellerBrokeragePctText.collectAsStateWithLifecycle()
    val res by viewModel.landDealBrokerageResult.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_land_broker_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Land Broker & Banakhat Deal Calculator",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "3. Daily Land Broker Commission (દલાલી), Token & Banakhat Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "જમીન લે-વેચ કરનાર દલાલ મિત્રો અને જમીનદારો માટે: ટોકન, બાનાખત, દસ્તાવેજ હપ્તા અને દલાલી (1%–2%) નો હિસાબ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = totalDealAmount,
                    onValueChange = { viewModel.onLandDealInputsChange(totalDealAmount = it) },
                    label = { Text("Total Deal Value ₹ (કુલ સોદો)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("input_deal_total_amount")
                )
                OutlinedTextField(
                    value = tokenAmount,
                    onValueChange = { viewModel.onLandDealInputsChange(tokenAmount = it) },
                    label = { Text("Token Paid ₹ (ટોકન)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_deal_token_amount")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = banakhatPct,
                    onValueChange = { viewModel.onLandDealInputsChange(banakhatPct = it) },
                    label = { Text("Banakhat % (બાનાખત%)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = buyerBrokeragePct,
                    onValueChange = { viewModel.onLandDealInputsChange(buyerBrokeragePct = it) },
                    label = { Text("Buyer Comm % (લેનાર%)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = sellerBrokeragePct,
                    onValueChange = { viewModel.onLandDealInputsChange(sellerBrokeragePct = it) },
                    label = { Text("Seller Comm % (વેચનાર%)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Total Broker Commission Pool (કુલ દલાલી): ₹${LandAndConstructionEngine.formatDec(res.totalBrokeragePoolInr, 0)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Buyer Brokerage: ₹${LandAndConstructionEngine.formatDec(res.buyerBrokerageInr, 0)} | Seller Brokerage: ₹${LandAndConstructionEngine.formatDec(res.sellerBrokerageInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "• Step 1 Token (ટોકન): ₹${LandAndConstructionEngine.formatDec(res.tokenAmountInr, 0)} | Step 2 Banakhat (${LandAndConstructionEngine.formatDec(res.banakhatPercent, 0)}%): ₹${LandAndConstructionEngine.formatDec(res.banakhatAmountInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "• Step 3 Final Dastavej Balance (દસ્તાવેજ વખતે બાકી રકમ): ₹${LandAndConstructionEngine.formatDec(res.remainingOnDastavejInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "• Total Buyer Outflow: ₹${LandAndConstructionEngine.formatDec(res.totalBuyerOutflowInr, 0)} | Net Seller Receipt: ₹${LandAndConstructionEngine.formatDec(res.netSellerHandoverInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.saveCurrentLandDealRecord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save_land_deal")
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Land Sauda & Brokerage Schedule to Vault")
            }
        }
    }
}

@Composable
private fun DailyFarmAndDairyRojmelCard(viewModel: MainViewModel) {
    val milkLiters by viewModel.rojmelMilkLitersText.collectAsStateWithLifecycle()
    val milkRate by viewModel.rojmelMilkRateText.collectAsStateWithLifecycle()
    val otherIncome by viewModel.rojmelOtherIncomeText.collectAsStateWithLifecycle()
    val cattleFeedExp by viewModel.rojmelCattleFeedExpText.collectAsStateWithLifecycle()
    val dieselExp by viewModel.rojmelDieselExpText.collectAsStateWithLifecycle()
    val miscExp by viewModel.rojmelMiscExpText.collectAsStateWithLifecycle()
    val res by viewModel.dailyRojmelResult.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_farm_dairy_rojmel_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = "Daily Farm & Dairy Rojmel",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "4. Daily Farm, Dairy (દૂધ ડેરી) & Landowner Income-Expense Rojmel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "રોજનું સવાર-સાંજનું દૂધ (લીટર × ભાવ), શાકભાજી/ખેતી આવક અને ખાણ-દાણ/ડીઝલ ખર્ચનો દૈનિક રોજમેળ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = milkLiters,
                    onValueChange = { viewModel.onDailyRojmelInputsChange(milkLiters = it) },
                    label = { Text("Milk Liters (દૂધ લીટર)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_rojmel_milk_liters")
                )
                OutlinedTextField(
                    value = milkRate,
                    onValueChange = { viewModel.onDailyRojmelInputsChange(milkRate = it) },
                    label = { Text("Rate ₹/L (ભાવ/લીટર)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_rojmel_milk_rate")
                )
                OutlinedTextField(
                    value = otherIncome,
                    onValueChange = { viewModel.onDailyRojmelInputsChange(otherIncome = it) },
                    label = { Text("Other Income ₹ (આવક)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cattleFeedExp,
                    onValueChange = { viewModel.onDailyRojmelInputsChange(cattleFeedExp = it) },
                    label = { Text("Feed/Chara ₹ (ખાણ-ચારો)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = dieselExp,
                    onValueChange = { viewModel.onDailyRojmelInputsChange(dieselExp = it) },
                    label = { Text("Diesel/Ferti ₹ (ડીઝલ)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = miscExp,
                    onValueChange = { viewModel.onDailyRojmelInputsChange(miscExp = it) },
                    label = { Text("Misc Exp ₹ (પરચૂરણ)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Net Daily Cash Profit (રોજની ચોખ્ખી બચત): ₹${LandAndConstructionEngine.formatDec(res.netDailyProfitInr, 0)} / day",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Daily Milk Income: ₹${LandAndConstructionEngine.formatDec(res.dailyMilkIncomeInr, 0)} + Other Farm Income: ₹${LandAndConstructionEngine.formatDec(res.otherDailyFarmIncomeInr, 0)} = Total ₹${LandAndConstructionEngine.formatDec(res.totalDailyIncomeInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "• Total Daily Expense (કુલ ખર્ચ): -₹${LandAndConstructionEngine.formatDec(res.totalDailyExpenseInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "• Projected 30-Day Monthly Net Savings (માસિક અંદાજિત ચોખ્ખી બચત): ₹${LandAndConstructionEngine.formatDec(res.projectedMonthlyNetProfitInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.saveCurrentDailyRojmelRecord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save_daily_rojmel")
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Daily Farm & Dairy Rojmel Entry to Vault")
            }
        }
    }
}

