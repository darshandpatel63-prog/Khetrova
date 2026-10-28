package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.ConstructionModuleType
import com.example.domain.LandAndConstructionEngine
import com.example.ui.MainViewModel
import com.example.ui.components.AdMobNativeAdCard

@Composable
fun ConstructionEstimatorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiStrings by viewModel.uiStrings.collectAsStateWithLifecycle()
    val selectedModule by viewModel.selectedConstructionModule.collectAsStateWithLifecycle()
    val lengthText by viewModel.roomLengthText.collectAsStateWithLifecycle()
    val widthText by viewModel.roomWidthOrHeightText.collectAsStateWithLifecycle()
    val deductionText by viewModel.deductionSqFtText.collectAsStateWithLifecycle()
    val tileLengthFt by viewModel.tileLengthFt.collectAsStateWithLifecycle()
    val tileWidthFt by viewModel.tileWidthFt.collectAsStateWithLifecycle()
    val wallThicknessInches by viewModel.wallThicknessInches.collectAsStateWithLifecycle()
    val plasterThicknessMm by viewModel.plasterThicknessMm.collectAsStateWithLifecycle()
    val cementRateText by viewModel.cementRateText.collectAsStateWithLifecycle()
    val sandRateText by viewModel.sandRateText.collectAsStateWithLifecycle()
    val tileRateText by viewModel.tileRateText.collectAsStateWithLifecycle()
    val brickRateText by viewModel.brickRateText.collectAsStateWithLifecycle()
    val paintRateText by viewModel.paintRateText.collectAsStateWithLifecycle()
    val boqResult by viewModel.constructionBoqResult.collectAsStateWithLifecycle()

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
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Construction,
                                contentDescription = "Construction estimator",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiStrings.selectCivilCategory,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = selectedModule.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ConstructionModuleType.entries.forEach { mod ->
                                val isSelected = mod == selectedModule
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onSelectConstructionModule(mod) },
                                    label = {
                                        Text(
                                            text = mod.title,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = {
                                        val icon = when (mod) {
                                            ConstructionModuleType.TILES_FLOORING -> Icons.Default.GridView
                                            ConstructionModuleType.BRICKWORK_MASONRY -> Icons.Default.Layers
                                            ConstructionModuleType.WALL_PLASTERING -> Icons.Default.Architecture
                                            ConstructionModuleType.PAINT_PUTTY -> Icons.Default.FormatPaint
                                        }
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.CheckCircle else icon,
                                            contentDescription = mod.title,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    modifier = Modifier.testTag("boq_module_${mod.name}")
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiStrings.enterRoomDimensions,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = lengthText,
                                onValueChange = { viewModel.onConstructionInputsChange(lengthText = it) },
                                label = { Text(uiStrings.lengthFeet) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("boq_length_input")
                            )
                            OutlinedTextField(
                                value = widthText,
                                onValueChange = { viewModel.onConstructionInputsChange(widthText = it) },
                                label = { Text(uiStrings.widthOrHeightFeet) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("boq_width_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        when (selectedModule) {
                            ConstructionModuleType.TILES_FLOORING -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        Triple("2×2 ft (600×600mm)", 2.0, 2.0),
                                        Triple("4×2 ft (1200×600mm)", 4.0, 2.0),
                                        Triple("2.66×2.66 ft (800×800mm)", 2.66, 2.66),
                                        Triple("1×1 ft (Parking/Bath)", 1.0, 1.0)
                                    ).forEachIndexed { idx, (label, tl, tw) ->
                                        val active = tileLengthFt == tl && tileWidthFt == tw
                                        FilterChip(
                                            selected = active,
                                            onClick = {
                                                viewModel.onConstructionInputsChange(tileL = tl, tileW = tw)
                                            },
                                            label = { Text(label) },
                                            modifier = Modifier.testTag("tile_size_chip_$idx")
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = tileRateText,
                                        onValueChange = { viewModel.onConstructionInputsChange(tileRate = it) },
                                        label = { Text("${uiStrings.rateLabel} Sq.Ft") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = cementRateText,
                                        onValueChange = { viewModel.onConstructionInputsChange(cementRate = it) },
                                        label = { Text("${uiStrings.rateLabel} 50kg Bag") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            ConstructionModuleType.BRICKWORK_MASONRY -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        Pair("4.5\" Half-Brick Wall", 4.5),
                                        Pair("9.0\" Full-Brick Wall", 9.0),
                                        Pair("13.5\" Heavy Outer Wall", 13.5)
                                    ).forEachIndexed { idx, (label, inches) ->
                                        FilterChip(
                                            selected = wallThicknessInches == inches,
                                            onClick = { viewModel.onConstructionInputsChange(wallInches = inches) },
                                            label = { Text(label) },
                                            modifier = Modifier.testTag("wall_thickness_chip_$idx")
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = deductionText,
                                        onValueChange = { viewModel.onConstructionInputsChange(deductionText = it) },
                                        label = { Text("Door/Window Deduction (Sq.Ft)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = brickRateText,
                                        onValueChange = { viewModel.onConstructionInputsChange(brickRate = it) },
                                        label = { Text("${uiStrings.rateLabel} Brick") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            ConstructionModuleType.WALL_PLASTERING -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        Pair("12 mm (Internal Smooth)", 12.0),
                                        Pair("15 mm (Rough Brick Face)", 15.0),
                                        Pair("20 mm (External Double Coat)", 20.0)
                                    ).forEachIndexed { idx, (label, mm) ->
                                        FilterChip(
                                            selected = plasterThicknessMm == mm,
                                            onClick = { viewModel.onConstructionInputsChange(plasterMm = mm) },
                                            label = { Text(label) },
                                            modifier = Modifier.testTag("plaster_mm_chip_$idx")
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = cementRateText,
                                        onValueChange = { viewModel.onConstructionInputsChange(cementRate = it) },
                                        label = { Text("${uiStrings.rateLabel} 50kg Bag") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = sandRateText,
                                        onValueChange = { viewModel.onConstructionInputsChange(sandRate = it) },
                                        label = { Text("${uiStrings.rateLabel} Cu.Ft Sand") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            ConstructionModuleType.PAINT_PUTTY -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = deductionText,
                                        onValueChange = { viewModel.onConstructionInputsChange(deductionText = it) },
                                        label = { Text("Opening Deduction (Sq.Ft)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = paintRateText,
                                        onValueChange = { viewModel.onConstructionInputsChange(paintRate = it) },
                                        label = { Text("${uiStrings.rateLabel} Liter") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${uiStrings.billOfQuantities} • ${boqResult.moduleType.title.uppercase()}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiStrings.totalMaterialCost}: ${LandAndConstructionEngine.formatCurrency(boqResult.totalEstimatedCostInr)}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = boqResult.technicalFormulaNote,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Native Ad after top 3 construction estimator cards
            item {
                AdMobNativeAdCard(slotIndex = 3)
            }

            itemsIndexed(boqResult.items, key = { _, it -> it.materialName }) { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.materialName,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.specification,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = item.quantityFormatted,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = LandAndConstructionEngine.formatCurrency(item.totalCostInr),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                // Native Ad after every 3 BOQ items (more boxes = more native ads)
                if ((index + 1) % 3 == 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AdMobNativeAdCard(slotIndex = index / 3 + 4)
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
                        onClick = { viewModel.saveCurrentConstructionBoq() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_save_boq")
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = uiStrings.saveToVault)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.saveToVault)
                    }

                    Button(
                        onClick = { viewModel.exportCurrentConstructionBoqPdf(context) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_export_boq_pdf")
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = uiStrings.exportPdf)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.exportPdf)
                    }
                }
            }
        }
    }
}
