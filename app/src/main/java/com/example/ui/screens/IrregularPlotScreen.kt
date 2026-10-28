package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.IrregularPlotResult
import com.example.domain.LandAndConstructionEngine
import com.example.ui.MainViewModel
import com.example.ui.components.AdMobNativeAdCard
import com.example.ui.theme.CadBlueprintBg
import com.example.ui.theme.CadDiagonalStroke
import com.example.ui.theme.CadGridLine
import com.example.ui.theme.CadPolygonFill
import com.example.ui.theme.CadPolygonStroke
import com.example.ui.theme.CadVertexPoint
import kotlin.math.abs

@Composable
fun IrregularPlotScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiStrings by viewModel.uiStrings.collectAsStateWithLifecycle()
    val northText by viewModel.northSideText.collectAsStateWithLifecycle()
    val eastText by viewModel.eastSideText.collectAsStateWithLifecycle()
    val southText by viewModel.southSideText.collectAsStateWithLifecycle()
    val westText by viewModel.westSideText.collectAsStateWithLifecycle()
    val diagonalText by viewModel.diagonalSideText.collectAsStateWithLifecycle()
    val rateSqFtText by viewModel.plotRatePerSqFtText.collectAsStateWithLifecycle()
    val plotResult by viewModel.irregularPlotResult.collectAsStateWithLifecycle()
    val hasAllMeasurements = listOf(northText, eastText, southText, westText).all { it.toDoubleOrNull()?.let { value -> value > 0.0 } == true }
    val hasValidPlotRate = rateSqFtText.toDoubleOrNull()?.let { it > 0.0 } == true

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
                    colors = CardDefaults.cardColors(containerColor = CadBlueprintBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        if (hasAllMeasurements) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = uiStrings.liveCadBlueprint,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = CadPolygonStroke
                                    )
                                    Text(
                                        text = "${uiStrings.triangulatedArea}: ${LandAndConstructionEngine.formatDec(plotResult.exactAreaSqFt)} Sq.Ft  •  ${LandAndConstructionEngine.formatDec(plotResult.exactAreaSqFt / 9.0)} Vaar",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF1E3A31)
                                ) {
                                    Text(
                                        text = "${uiStrings.perimeter}: ${LandAndConstructionEngine.formatDec(plotResult.perimeterFt, 1)} ft",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = CadVertexPoint,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            IrregularPlotCadCanvas(
                                plotResult = plotResult,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(235.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .testTag("irregular_plot_cad_canvas")
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = plotResult.validationMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFBAE6FD)
                            )
                        } else {
                            Text(
                                text = "No sample plot is preloaded. Enter the real North, South, East and West boundary lengths below to see area and perimeter.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFD1FAE5)
                            )
                        }
                    }
                }
            }

            if (hasAllMeasurements) {
            item {
                val absError = abs(plotResult.contractorErrorSqFt)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = "Contractor formula comparison",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiStrings.contractorErrorDetector,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Naive Average ((N+S)/2 × (E+W)/2) = ${LandAndConstructionEngine.formatDec(plotResult.naiveAvgAreaSqFt)} Sq.Ft  |  Exact Heron Triangulation = ${LandAndConstructionEngine.formatDec(plotResult.exactAreaSqFt)} Sq.Ft",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (hasValidPlotRate) {
                                "Discrepancy Prevented: ${LandAndConstructionEngine.formatDec(absError)} Sq.Ft (${LandAndConstructionEngine.formatCurrency(abs(plotResult.contractorErrorInr))} at ₹$rateSqFtText/sq.ft)"
                            } else {
                                "Area discrepancy: ${LandAndConstructionEngine.formatDec(absError)} Sq.Ft • Enter an actual rate to estimate the money difference."
                            },
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            }

            // Native Ad after Box 2 (CAD Blueprint + Contractor Error Detector)
            item {
                AdMobNativeAdCard(slotIndex = 1)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SquareFoot,
                                contentDescription = "Plot dimensions",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiStrings.enterFourSides,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Use measurements from your actual plot. No demo plot presets are loaded.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = northText,
                                onValueChange = { viewModel.onPlotDimensionChange(north = it) },
                                label = { Text(uiStrings.northSide) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_north_ft")
                            )
                            OutlinedTextField(
                                value = southText,
                                onValueChange = { viewModel.onPlotDimensionChange(south = it) },
                                label = { Text(uiStrings.southSide) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_south_ft")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = eastText,
                                onValueChange = { viewModel.onPlotDimensionChange(east = it) },
                                label = { Text(uiStrings.eastSide) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_east_ft")
                            )
                            OutlinedTextField(
                                value = westText,
                                onValueChange = { viewModel.onPlotDimensionChange(west = it) },
                                label = { Text(uiStrings.westSide) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_west_ft")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = diagonalText,
                                onValueChange = { viewModel.onPlotDimensionChange(diagonal = it) },
                                label = { Text(uiStrings.diagonalSide) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_diagonal_ft")
                            )
                            OutlinedTextField(
                                value = rateSqFtText,
                                onValueChange = { viewModel.onPlotDimensionChange(rateSqFt = it) },
                                label = { Text("${uiStrings.rateLabel} Sq.Ft") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_plot_rate")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { viewModel.autoCalibrateDiagonal() },
                            enabled = hasAllMeasurements,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_auto_calibrate_diagonal")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = uiStrings.autoCalibrateDiagonal
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (hasAllMeasurements) {
                                    "${uiStrings.autoCalibrateDiagonal} (${LandAndConstructionEngine.formatDec(plotResult.minValidDiagonal, 1)}–${LandAndConstructionEngine.formatDec(plotResult.maxValidDiagonal, 1)} ft)"
                                } else {
                                    "${uiStrings.autoCalibrateDiagonal} (enter four sides first)"
                                }
                            )
                        }
                    }
                }
            }

            if (hasAllMeasurements) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiStrings.instantEquivalents,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Triangle 1 (N-E-Diag): ${LandAndConstructionEngine.formatDec(plotResult.triangle1AreaSqFt)} Sq.Ft  +  Triangle 2 (S-W-Diag): ${LandAndConstructionEngine.formatDec(plotResult.triangle2AreaSqFt)} Sq.Ft",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        plotResult.regionalConversions.entries.forEach { (unitLabel, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = unitLabel,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = LandAndConstructionEngine.formatDec(
                                        value,
                                        if (value < 10.0) 4 else 2
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                        if (hasValidPlotRate) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = uiStrings.basePlotValue,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = LandAndConstructionEngine.formatCurrency(plotResult.estimatedPlotValueInr),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Text(
                                text = "Enter your actual land rate per square foot to estimate the plot value.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            }

            // Native Ad after Regional Conversions Box
            item {
                AdMobNativeAdCard(slotIndex = 2)
            }

            if (hasAllMeasurements) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.saveCurrentIrregularPlot() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_save_irregular_plot")
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = uiStrings.saveToVault)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.saveToVault)
                    }

                    Button(
                        onClick = { viewModel.exportCurrentIrregularPlotPdf(context) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_export_plot_pdf")
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
}

@Composable
fun IrregularPlotCadCanvas(
    plotResult: IrregularPlotResult,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.background(CadBlueprintBg)) {
        val w = size.width
        val h = size.height

        val step = 28.dp.toPx()
        var gx = 0f
        while (gx <= w) {
            drawLine(
                color = CadGridLine,
                start = Offset(gx, 0f),
                end = Offset(gx, h),
                strokeWidth = 1f
            )
            gx += step
        }
        var gy = 0f
        while (gy <= h) {
            drawLine(
                color = CadGridLine,
                start = Offset(0f, gy),
                end = Offset(w, gy),
                strokeWidth = 1f
            )
            gy += step
        }

        val verts = plotResult.vertices
        if (verts.size < 4) return@Canvas

        val xs = verts.map { it.x }
        val ys = verts.map { it.y }
        val minX = xs.minOrNull() ?: 0f
        val maxX = (xs.maxOrNull() ?: 10f).coerceAtLeast(minX + 1f)
        val minY = ys.minOrNull() ?: 0f
        val maxY = (ys.maxOrNull() ?: 10f).coerceAtLeast(minY + 1f)

        val padX = 48.dp.toPx()
        val padY = 34.dp.toPx()
        val availW = (w - padX * 2).coerceAtLeast(20f)
        val availH = (h - padY * 2).coerceAtLeast(20f)
        val scale = minOf(availW / (maxX - minX), availH / (maxY - minY))

        val offsetX = padX + (availW - (maxX - minX) * scale) / 2f
        val offsetY = padY + (availH - (maxY - minY) * scale) / 2f

        val p = verts.map { v ->
            Offset(
                x = offsetX + (v.x - minX) * scale,
                y = offsetY + (v.y - minY) * scale
            )
        }

        val polyPath = Path().apply {
            moveTo(p[0].x, p[0].y)
            lineTo(p[1].x, p[1].y)
            lineTo(p[2].x, p[2].y)
            lineTo(p[3].x, p[3].y)
            close()
        }

        drawPath(path = polyPath, color = CadPolygonFill)
        drawPath(
            path = polyPath,
            color = CadPolygonStroke,
            style = Stroke(width = 3.dp.toPx())
        )

        drawLine(
            color = CadDiagonalStroke,
            start = p[0],
            end = p[2],
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        )

        val labelPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 26f
            isAntiAlias = true
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        val dimPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#FDE68A")
            textSize = 24f
            isAntiAlias = true
        }

        p.forEachIndexed { i, pt ->
            drawCircle(
                color = CadVertexPoint,
                radius = 5.dp.toPx(),
                center = pt
            )
            val v = verts[i]
            drawContext.canvas.nativeCanvas.drawText(
                "${v.label} (${v.angleDegrees.toInt()}°)",
                pt.x - 24f,
                if (i < 2) pt.y - 14f else pt.y + 28f,
                labelPaint
            )
        }

        val midNorth = Offset((p[0].x + p[1].x) / 2f, (p[0].y + p[1].y) / 2f - 10f)
        val midEast = Offset((p[1].x + p[2].x) / 2f + 10f, (p[1].y + p[2].y) / 2f)
        val midSouth = Offset((p[2].x + p[3].x) / 2f, (p[2].y + p[3].y) / 2f + 22f)
        val midWest = Offset((p[3].x + p[0].x) / 2f - 68f, (p[3].y + p[0].y) / 2f)
        val midDiag = Offset((p[0].x + p[2].x) / 2f - 28f, (p[0].y + p[2].y) / 2f - 6f)

        drawContext.canvas.nativeCanvas.apply {
            drawText("N: ${plotResult.northFt} ft", midNorth.x - 30f, midNorth.y, dimPaint)
            drawText("E: ${plotResult.eastFt} ft", midEast.x, midEast.y, dimPaint)
            drawText("S: ${plotResult.southFt} ft", midSouth.x - 30f, midSouth.y, dimPaint)
            drawText("W: ${plotResult.westFt} ft", midWest.x, midWest.y, dimPaint)
            drawText("Diag: ${LandAndConstructionEngine.formatDec(plotResult.usedDiagonal, 1)} ft", midDiag.x, midDiag.y, dimPaint)
        }
    }
}
