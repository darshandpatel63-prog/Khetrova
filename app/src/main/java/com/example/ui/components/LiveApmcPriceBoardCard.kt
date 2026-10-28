package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.ApmcRegionZone
import com.example.domain.AppLocalizationEngine
import com.example.domain.LandAndConstructionEngine
import com.example.domain.LiveApmcMarketEngine
import com.example.domain.LiveCropPriceQuote
import com.example.ui.MainViewModel

@Composable
fun LiveApmcPriceBoardCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedZone by viewModel.selectedApmcRegionZone.collectAsStateWithLifecycle()
    val selectedYard by viewModel.selectedApmcYard.collectAsStateWithLifecycle()
    val searchQuery by viewModel.apmcCropSearchQuery.collectAsStateWithLifecycle()
    val showQuintalMode by viewModel.apmcShowQuintalMode.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncingLiveApmc.collectAsStateWithLifecycle()
    val snapshot by viewModel.liveApmcSyncSnapshot.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    val yardsInSelectedZone = remember(selectedZone) {
        LiveApmcMarketEngine.getYardsByZone(selectedZone)
    }

    val yardQuotes = remember(snapshot, selectedYard, searchQuery) {
        val raw = snapshot.quotes.filter { it.yardId == selectedYard.yardId }
        if (searchQuery.isBlank()) {
            raw
        } else {
            val q = searchQuery.trim().lowercase()
            raw.filter {
                it.cropNameEn.lowercase().contains(q) ||
                    it.cropNameLocal.lowercase().contains(q) ||
                    it.varietyOrGrade.lowercase().contains(q) ||
                    it.yardName.lowercase().contains(q)
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_apmc_price_board_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Header Row
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
                        imageVector = if (selectedZone.isInternational) Icons.Default.Public else Icons.Default.Agriculture,
                        contentDescription = "Live APMC & Global Govt Market",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = AppLocalizationEngine.getMarketPricesTitle(selectedLanguage.code),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Official daily India Agmarknet records are shown when available. Other country-specific prices appear only after a verified source is connected.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Auto-Sync Status Banner & Refresh Action
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (snapshot.isOnlineSynced) Color(0xFF065F46) else MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "Auto Sync Status",
                            tint = if (snapshot.isOnlineSynced) Color(0xFF6EE7B7) else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = snapshot.syncStatusBanner,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (snapshot.isOnlineSynced) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Last feed check: ${snapshot.lastUpdatedTimeFormatted} • Check each quote’s market date for price freshness.",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (snapshot.isOnlineSynced) Color(0xFFD1FAE5) else MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.syncLiveApmcMarketPrices(isSilentStartup = false) },
                        enabled = !isSyncing,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (snapshot.isOnlineSynced) Color.White else MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("btn_sync_live_apmc")
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Syncing...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Live APMC Rates",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sync Live", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step 1: Select Region / Country Zone (Vistar Vise Alag Alag Zone)
            Text(
                text = "1. Select Region / Country Market Zone (વિસ્તાર અથવા દેશ પસંદ કરો):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ApmcRegionZone.entries.forEach { zone ->
                    val selected = zone == selectedZone
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.selectApmcRegionZone(zone) },
                        label = {
                            Text(
                                text = zone.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.testTag("apmc_zone_${zone.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Step 2: Select Specific APMC Market Yard / International Govt Board
            Text(
                text = "2. Select APMC Market Yard / Govt Board (માર્કેટ યાર્ડ પસંદ કરો - દરેક યાર્ડના અલગ ભાવ):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                yardsInSelectedZone.forEach { yard ->
                    val selected = yard.yardId == selectedYard.yardId
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.selectApmcYard(yard) },
                        label = {
                            Text(
                                text = yard.yardName,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        modifier = Modifier.testTag("apmc_yard_${yard.yardId}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Selected Yard Info & Unit Mode Toggle (20 Kg Man vs 100 Kg Quintal)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedYard.yardName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${selectedYard.districtOrState} • Source: ${selectedYard.governmentPortalName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Specialty: ${selectedYard.specialtyCropsSummary}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = !showQuintalMode,
                            onClick = { viewModel.toggleApmcPriceUnitMode(false) },
                            label = {
                                Text(
                                    text = "Show per 20 Kg Man (₹ / ૨૦ કિલો મણ)",
                                    fontWeight = if (!showQuintalMode) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("chip_unit_20kg_man")
                        )
                        FilterChip(
                            selected = showQuintalMode,
                            onClick = { viewModel.toggleApmcPriceUnitMode(true) },
                            label = {
                                Text(
                                    text = "Show per 100 Kg Quintal (₹ / ૧૦૦ કિલો ક્વિન્ટલ)",
                                    fontWeight = if (showQuintalMode) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("chip_unit_100kg_quintal")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Crop Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onApmcSearchQueryChange(it) },
                label = { Text("Search Crop / Commodity (પાક શોધો: કપાસ, મગફળી, જીરું, ડુંગળી, ઘઉં, Soybeans...)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Crop") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_apmc_crop_search")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Live Crop Quotes List for Selected Yard
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (yardQuotes.isEmpty()) {
                    val emptyMessage = when {
                        selectedZone.isInternational ->
                            "No verified live price feed is connected for this country/market yet. Sample prices are intentionally hidden."
                        snapshot.quotes.isEmpty() ->
                            "No official Agmarknet prices were received. Check internet and tap Sync Live. Sample prices are intentionally hidden."
                        else ->
                            "No official daily record matched this market. Try another market or sync again."
                    }
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    yardQuotes.forEach { quote ->
                        LiveCropQuoteRowCard(
                            quote = quote,
                            showQuintalMode = showQuintalMode,
                            onApplyToMandiBill = { viewModel.applyLiveQuoteToMandiCalculator(quote) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Save Yard Daily Sheet to Vault Button
            OutlinedButton(
                onClick = { viewModel.saveLiveApmcYardSheetToVault() },
                enabled = yardQuotes.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save_live_apmc_sheet")
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save ${selectedYard.yardName} Live Price List to Vault")
            }
        }
    }
}

@Composable
private fun LiveCropQuoteRowCard(
    quote: LiveCropPriceQuote,
    showQuintalMode: Boolean,
    onApplyToMandiBill: () -> Unit
) {
    val isPositive = quote.dailyChangePerManInr >= 0.0
    val unitSuffix = if (showQuintalMode) "/ Quintal (100Kg)" else "/ Man (20Kg મણ)"
    val minDisplay = if (showQuintalMode) quote.minPricePerQuintalInr else quote.minPricePer20KgManInr
    val maxDisplay = if (showQuintalMode) quote.maxPricePerQuintalInr else quote.maxPricePer20KgManInr
    val modalDisplay = if (showQuintalMode) quote.modalPricePerQuintalInr else quote.modalPricePer20KgManInr
    val changeDisplay = if (showQuintalMode) quote.dailyChangePerManInr * 5.0 else quote.dailyChangePerManInr

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("apmc_quote_${quote.quoteId}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = quote.cropNameEn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${quote.cropNameLocal} • Grade: ${quote.varietyOrGrade}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = if (quote.hasVerifiedDailyChange) {
                        val sign = if (isPositive) "+" else ""
                        "${sign}₹${LandAndConstructionEngine.formatDec(changeDisplay, 0)}"
                    } else {
                        "Daily change: not provided by source"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Min / Max / Modal Price Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "નીચો ભાવ (Min)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹${LandAndConstructionEngine.formatDec(minDisplay, 0)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "સરેરાશ ભાવ (Modal $unitSuffix)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹${LandAndConstructionEngine.formatDec(modalDisplay, 0)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "ઊંચો ભાવ (Max)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹${LandAndConstructionEngine.formatDec(maxDisplay, 0)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF047857)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(6.dp))

            // Secondary Row: Both Man & Quintal + International Native Price + 1-Tap Use in Bill Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "20Kg મણ: ₹${LandAndConstructionEngine.formatDec(quote.modalPricePer20KgManInr, 0)}  |  100Kg ક્વિન્ટલ: ₹${LandAndConstructionEngine.formatDec(quote.modalPricePerQuintalInr, 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    val intlInfo = if (quote.regionZone.isInternational) {
                        "Local Govt Price: ${quote.nativeCurrencySymbol}${LandAndConstructionEngine.formatDec(quote.nativeModalPrice, 2)} (${quote.nativeUnitLabel}) • "
                    } else {
                        ""
                    }
                    val arrivalInfo = if (quote.hasVerifiedArrivalData) {
                        "Arrival: ${quote.arrivalBagsCount} Bags"
                    } else {
                        "Arrival: not provided by this source"
                    }
                    Text(
                        text = "${intlInfo}${arrivalInfo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Source: ${quote.governmentSourceLabel} • Market date: ${quote.updatedTimestampText} • " +
                            (if (quote.isLiveNetworkSynced) "Recent official record" else "Older official record"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onApplyToMandiBill,
                    enabled = quote.isLiveNetworkSynced,
                    modifier = Modifier.testTag("btn_use_apmc_rate_${quote.quoteId}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Use verified recent rate in Mandi Bill",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (quote.isLiveNetworkSynced) "Use in Bill" else "Dated Data",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
