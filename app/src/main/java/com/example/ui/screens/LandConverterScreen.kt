package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.LandAndConstructionEngine
import com.example.ui.MainViewModel
import com.example.ui.QuickFeatureDestination
import com.example.ui.components.AdMobNativeAdCard

@Composable
fun LandConverterScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiStrings by viewModel.uiStrings.collectAsStateWithLifecycle()
    val selectedProfile by viewModel.selectedStateProfile.collectAsStateWithLifecycle()
    val selectedUnit by viewModel.selectedInputUnit.collectAsStateWithLifecycle()
    val inputValueText by viewModel.landInputValueText.collectAsStateWithLifecycle()
    val ratePerUnitText by viewModel.landRatePerUnitText.collectAsStateWithLifecycle()
    val result by viewModel.landConversionResult.collectAsStateWithLifecycle()

    val countryGroups = remember {
        listOf("All Worldwide") + LandAndConstructionEngine.stateProfiles.map { it.countryGroup }.distinct()
    }
    var selectedCountryGroup by remember { mutableStateOf("All Worldwide") }
    val filteredProfiles = remember(selectedCountryGroup) {
        if (selectedCountryGroup == "All Worldwide") {
            LandAndConstructionEngine.stateProfiles
        } else {
            LandAndConstructionEngine.stateProfiles.filter { it.countryGroup == selectedCountryGroup }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 760.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Quick Daily-Use Khedut, Landowner & Broker Bar
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_tools_quick_banner"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = uiStrings.dailyRojmelTab,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.navigateToQuickFeature(QuickFeatureDestination.LIVE_APMC_MANDI_PRICES) },
                                modifier = Modifier.testTag("quick_btn_live_apmc_prices")
                            ) {
                                Text("★ Live APMC & Global Bhav (લાઈવ યાર્ડ ભાવ)")
                            }
                            OutlinedButton(
                                onClick = { viewModel.navigateToQuickFeature(QuickFeatureDestination.DAILY_MANDI_BILL) },
                                modifier = Modifier.testTag("quick_btn_mandi_bill")
                            ) {
                                Text("1. APMC Mandi Bill (20Kg મણ)")
                            }
                            OutlinedButton(
                                onClick = { viewModel.navigateToQuickFeature(QuickFeatureDestination.DAILY_LABOUR_TRACTOR_KHATA) },
                                modifier = Modifier.testTag("quick_btn_labour_khata")
                            ) {
                                Text("2. Labour & Tractor Khata (દાડિયા)")
                            }
                            OutlinedButton(
                                onClick = { viewModel.navigateToQuickFeature(QuickFeatureDestination.DAILY_LAND_BROKER_DEAL) },
                                modifier = Modifier.testTag("quick_btn_land_broker")
                            ) {
                                Text("3. Land Broker & Banakhat (દલાલી)")
                            }
                            OutlinedButton(
                                onClick = { viewModel.navigateToQuickFeature(QuickFeatureDestination.DAILY_FARM_DAIRY_ROJMEL) },
                                modifier = Modifier.testTag("quick_btn_daily_rojmel")
                            ) {
                                Text("4. Daily Dairy & Farm Rojmel (રોજમેળ)")
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "State standard icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiStrings.selectStateStandard,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${selectedProfile.countryGroup} • ${selectedProfile.regionNote}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Country / Global Region Group Filter Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            countryGroups.forEachIndexed { gIdx, groupName ->
                                val isGroupSelected = groupName == selectedCountryGroup
                                FilterChip(
                                    selected = isGroupSelected,
                                    onClick = {
                                        selectedCountryGroup = groupName
                                        val firstInGroup = if (groupName == "All Worldwide") {
                                            LandAndConstructionEngine.stateProfiles.firstOrNull()
                                        } else {
                                            LandAndConstructionEngine.stateProfiles.firstOrNull { it.countryGroup == groupName }
                                        }
                                        if (firstInGroup != null && (groupName != "All Worldwide" && selectedProfile.countryGroup != groupName)) {
                                            viewModel.onSelectStateProfile(firstInGroup)
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = groupName,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isGroupSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                                    ),
                                    modifier = Modifier.testTag("country_group_chip_$gIdx")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            filteredProfiles.forEachIndexed { idx, profile ->
                                val isSelected = profile.stateName == selectedProfile.stateName
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onSelectStateProfile(profile) },
                                    label = {
                                        Text(
                                            text = profile.stateName.substringBefore("(").trim(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected state",
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    modifier = Modifier.testTag("state_chip_$idx")
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Input unit selector",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiStrings.chooseUnitAndArea,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedProfile.units.forEach { unit ->
                                val isSelected = unit.id == selectedUnit.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onSelectInputUnit(unit) },
                                    label = {
                                        Text("${unit.name} (${unit.localName})")
                                    },
                                    modifier = Modifier.testTag("unit_chip_${unit.id}")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = inputValueText,
                                onValueChange = { viewModel.onLandInputValueChange(it) },
                                label = { Text("${uiStrings.areaLabel} ${selectedUnit.name}") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("land_area_input")
                            )

                            OutlinedTextField(
                                value = ratePerUnitText,
                                onValueChange = { viewModel.onLandRateChange(it) },
                                label = { Text("${uiStrings.rateLabel} ${selectedUnit.name.substringBefore(" ")}") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("land_rate_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("1", "1.5", "2.5", "5", "10", "25", "100").forEach { preset ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clickable { viewModel.onLandInputValueChange(preset) }
                                        .testTag("preset_area_$preset")
                                ) {
                                    Text(
                                        text = "$preset ${selectedUnit.name.substringBefore(" ")}",
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiStrings.instantEquivalents,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${LandAndConstructionEngine.formatDec(result.totalSqFeet)} Sq.Ft  •  ${LandAndConstructionEngine.formatDec(result.totalSqMeters)} Sq.Meters",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "1 ${selectedUnit.name} = ${LandAndConstructionEngine.formatDec(selectedUnit.sqFeetPerUnit)} Sq.Ft (${selectedUnit.description})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Native Ad after the top 3 boxes
            item {
                AdMobNativeAdCard(slotIndex = 0)
            }

            itemsIndexed(result.convertedItems, key = { _, it -> it.unit.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onSelectInputUnit(item.unit) }
                        .testTag("converted_row_${item.unit.id}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.unit.id == selectedUnit.id)
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
                        else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${item.unit.name}  •  ${item.unit.localName}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.unit.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = LandAndConstructionEngine.formatDec(
                                    item.convertedValue,
                                    if (item.convertedValue < 10.0) 4 else 2
                                ),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${LandAndConstructionEngine.formatDec(item.unit.sqFeetPerUnit)} sq.ft/unit",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Native Ad after every 3 unit conversion boxes (more boxes = more native ads)
                if ((index + 1) % 3 == 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AdMobNativeAdCard(slotIndex = index / 3 + 1)
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyRupee,
                                contentDescription = "Land valuation",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = uiStrings.landValuationTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(uiStrings.basePlotValue, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = LandAndConstructionEngine.formatCurrency(result.baseLandValueInr),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${uiStrings.estStampDuty} (${result.stateProfile.stampDutyPercent}%):",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = LandAndConstructionEngine.formatCurrency(result.stampDutyInr),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiStrings.totalLandCost,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = LandAndConstructionEngine.formatCurrency(result.totalLandCostInr),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.saveCurrentLandConversion() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_save_land_conversion")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = uiStrings.saveToVault
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.saveToVault)
                    }

                    Button(
                        onClick = { viewModel.exportCurrentLandConversionPdf(context) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_export_land_pdf")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = uiStrings.exportPdf
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.exportPdf)
                    }
                }
            }
        }
    }
}
