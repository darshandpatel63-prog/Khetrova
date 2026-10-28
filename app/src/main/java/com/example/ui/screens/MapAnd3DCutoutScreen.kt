package com.example.ui.screens

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fence
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.FarmerAndLandToolsEngine
import com.example.domain.FarmerCropSpec
import com.example.domain.GeodesicMapAnd3DEngine
import com.example.domain.GeodesicPolygonSurveyResult
import com.example.domain.GpsLatLng
import com.example.domain.LandAndConstructionEngine
import com.example.ui.MainViewModel
import com.example.ui.components.AdMobNativeAdCard
import com.example.ui.theme.CadBlueprintBg
import com.example.ui.theme.CadGridLine
import com.example.ui.theme.CadPolygonFill
import com.example.ui.theme.CadPolygonStroke
import com.example.ui.theme.CadVertexPoint
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun MapAnd3DCutoutScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val selectedStateProfile by viewModel.selectedStateProfile.collectAsStateWithLifecycle()
    val mapPoints by viewModel.mapPolygonPoints.collectAsStateWithLifecycle()
    val mapSurveyResult by viewModel.mapPolygonSurveyResult.collectAsStateWithLifecycle()
    val mapDisplayCenter by viewModel.mapDisplayCenter.collectAsStateWithLifecycle()
    val googleMapsQuery by viewModel.googleMapsInputText.collectAsStateWithLifecycle()
    val uiStrings by viewModel.uiStrings.collectAsStateWithLifecycle()

    // 3D Rotation & Extrusion interactive controls
    var yawDegrees by remember { mutableFloatStateOf(32f) }
    var pitchDegrees by remember { mutableFloatStateOf(52f) }
    var extrusionHeightRatio by remember { mutableFloatStateOf(0.28f) }
    var useSatelliteWebView by remember { mutableStateOf(true) }

    // Farmer & Landowner Special Toolkit state
    val selectedKhedutTab by viewModel.selectedKhedutTab.collectAsStateWithLifecycle() // 0 = Partition/Heirs, 1 = Fencing, 2 = Crop Inputs
    val mapScrollTargetIndex by viewModel.mapScrollTargetIndex.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var numberOfHeirs by remember { mutableIntStateOf(3) }
    var roadCutPercent by remember { mutableFloatStateOf(5f) }
    var fencingPoleSpacingFt by remember { mutableFloatStateOf(8f) }
    var fencingWireRows by remember { mutableIntStateOf(5) }
    var selectedFarmerCrop by remember { mutableStateOf(FarmerCropSpec.COTTON) }

    LaunchedEffect(mapScrollTargetIndex) {
        val target = mapScrollTargetIndex
        if (target != null) {
            listState.animateScrollToItem(target.coerceAtLeast(0))
            viewModel.clearMapScrollTarget()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                // Card 1: Geo-Location Input + Global Country Selector
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = "Global Map & 3D Cutout",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "1. GEO-LOCATION INPUT & COUNTRY MEASUREMENT SYSTEM",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Enter 1 Geo-Location (Lat, Lng or Google Maps link) to open that location, then draw/drag lines on the map like a stock-market chart!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // No preloaded demo locations; enter a real location below.

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = googleMapsQuery,
                                onValueChange = { viewModel.onGoogleMapsInputChange(it) },
                                label = { Text("Geo-Location (Lat, Lng) or Google Maps Link (e.g. 22.2916, 70.7932)") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_google_maps_coords")
                            )
                            Button(
                                onClick = { viewModel.jumpToGoogleMapsCoordinates() },
                                modifier = Modifier
                                    .height(54.dp)
                                    .testTag("btn_jump_coords")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Jump to location")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Country Measurement Profile Chips (20 Global & Indian Profiles)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LandAndConstructionEngine.stateProfiles.forEachIndexed { idx, profile ->
                                val isSelected = profile.stateName == selectedStateProfile.stateName
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onSelectStateProfile(profile) },
                                    label = {
                                        Text(
                                            text = profile.stateName.substringBefore("(").trim(),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected country",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    modifier = Modifier.testTag("map_country_chip_$idx")
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Card 2: Stock-Market / TradingView-Style Multi-Line Drawing on Satellite Map & Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CadBlueprintBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "2. IN-APP MAP & STOCK-CHART MULTI-LINE SURVEY",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CadPolygonStroke
                                )
                                Text(
                                    text = "Tap to draw connected lines (P1→P2→P3→P4) or drag any corner node to measure land live!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFD1FAE5)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E4D3E),
                                modifier = Modifier
                                    .clickable { useSatelliteWebView = !useSatelliteWebView }
                                    .testTag("btn_toggle_map_mode")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (useSatelliteWebView) Icons.Default.Layers else Icons.Default.ShowChart,
                                        contentDescription = "Switch Map Mode",
                                        tint = CadPolygonStroke,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (useSatelliteWebView) "Satellite + Lines" else "Stock-Line Chart",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        if (mapPoints.isEmpty() && mapDisplayCenter == null) {
                            Text(
                                text = "No demo map or sample measurements are loaded. Enter a real latitude/longitude or Google Maps link above, tap Jump to Location, then add the actual boundary corners.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFD1FAE5),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        } else {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Stock-Market Style Live Telemetry Ticker Strip
                        val primaryUnitItem = mapSurveyResult.countryConversions.firstOrNull()
                        val gunthaVal = mapSurveyResult.areaSqFeet / 1089.0
                        val vaarVal = mapSurveyResult.areaSqFeet / 9.0
                        if (mapPoints.size >= 3) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF071712),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LiveTickerMetric("LINES", "${mapSurveyResult.edges.size}", Color(0xFF38BDF8))
                                LiveTickerMetric("AREA (SQ.FT)", LandAndConstructionEngine.formatDec(mapSurveyResult.areaSqFeet, 1), Color(0xFFFBBF24))
                                if (primaryUnitItem != null) {
                                    LiveTickerMetric(
                                        primaryUnitItem.unit.name.substringBefore("(").trim().uppercase(),
                                        LandAndConstructionEngine.formatDec(primaryUnitItem.convertedValue, 3),
                                        Color(0xFF34D399)
                                    )
                                }
                                LiveTickerMetric("GUNTHA", LandAndConstructionEngine.formatDec(gunthaVal, 2), Color(0xFFA7F3D0))
                                LiveTickerMetric("VAAR / YD²", LandAndConstructionEngine.formatDec(vaarVal, 1), Color(0xFFFDE68A))
                                LiveTickerMetric("PERIMETER", "${LandAndConstructionEngine.formatDec(mapSurveyResult.perimeterFeet, 1)} ft", Color.White)
                            }
                        }
                        } else {
                            Text(
                                text = "${mapPoints.size} actual boundary point(s) selected. Add at least 3 points to calculate area and perimeter.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFD1FAE5),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (useSatelliteWebView) {
                            InteractiveSatelliteLeafletMap(
                                points = mapPoints,
                                centroid = if (mapPoints.isNotEmpty()) mapSurveyResult.centroid else mapDisplayCenter ?: mapSurveyResult.centroid,
                                onMapTapAddPoint = { lat, lng ->
                                    viewModel.addMapPolygonPoint(lat, lng)
                                },
                                onMarkerDragged = { index, lat, lng ->
                                    viewModel.updateMapPolygonPoint(index, lat, lng)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .testTag("satellite_map_webview")
                            )
                        } else {
                            InteractiveTradingLineMapCanvas(
                                surveyResult = mapSurveyResult,
                                onTapAddRelativePoint = { dLat, dLng ->
                                    val c = if (mapPoints.isNotEmpty()) mapSurveyResult.centroid else mapDisplayCenter ?: mapSurveyResult.centroid
                                    viewModel.addMapPolygonPoint(c.lat + dLat, c.lng + dLng)
                                },
                                onDragVertexRelative = { vertexIdx, newLat, newLng ->
                                    viewModel.updateMapPolygonPoint(vertexIdx, newLat, newLng)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .testTag("offline_geodesic_cut_canvas")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Line-Drawing & Polygon Cut Action Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.startSinglePointLineDrawingMode() },
                                modifier = Modifier.testTag("btn_map_start_1point")
                            ) {
                                Icon(Icons.Default.ShowChart, contentDescription = "Start from 1 point", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Start From 1 Point (Draw Lines)", color = Color(0xFFFDE68A))
                            }

                            OutlinedButton(
                                onClick = { viewModel.undoLastMapPoint() },
                                modifier = Modifier.testTag("btn_map_undo_point")
                            ) {
                                Icon(Icons.Default.Undo, contentDescription = "Undo line", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Undo Line", color = Color.White)
                            }

                            OutlinedButton(
                                onClick = { viewModel.resetDefaultFourCornerMapCut() },
                                modifier = Modifier.testTag("btn_map_reset_4corner")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Clear Boundary Points", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear Boundary", color = Color.White)
                            }

                            Button(
                                onClick = { viewModel.transferMapCutToIrregularCadTab() },
                                modifier = Modifier.testTag("btn_transfer_to_cad")
                            ) {
                                Icon(Icons.Default.ContentCut, contentDescription = "Cut & Open in 2D CAD", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Send Lines to 2D CAD")
                            }
                        }
                        } // End of the map/measurement area when a real location is selected.
                    }
                }
            }

            if (mapPoints.size >= 3) {
            item {
                // Card 3: 3D Extruded Cutout Parcel Visualizer Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF091B16)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ViewInAr,
                                    contentDescription = "3D Cutout View",
                                    tint = CadPolygonStroke
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "3. EXTRACTED 3D LAND CUTOUT & BOUNDARY MODEL",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = CadPolygonStroke
                                    )
                                    Text(
                                        text = "Drag on 3D view or use sliders to rotate 360° & inspect every side",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFA7F3D0)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Plot3DExtrudedCutoutCanvas(
                            surveyResult = mapSurveyResult,
                            yawDegrees = yawDegrees,
                            pitchDegrees = pitchDegrees,
                            extrusionHeightRatio = extrusionHeightRatio,
                            onDragRotate = { dx, dy ->
                                yawDegrees = (yawDegrees + dx * 0.45f) % 360f
                                pitchDegrees = (pitchDegrees - dy * 0.25f).coerceIn(20f, 80f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .testTag("plot_3d_cutout_canvas")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 3D Rotation & Extrusion Sliders
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "3D Rotate (${yawDegrees.toInt()}°)",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = yawDegrees,
                                onValueChange = { yawDegrees = it },
                                valueRange = 0f..360f,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("slider_3d_yaw")
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "3D Tilt (${pitchDegrees.toInt()}°)",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = pitchDegrees,
                                onValueChange = { pitchDegrees = it },
                                valueRange = 20f..80f,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("slider_3d_pitch")
                            )
                        }

                        // Primary Area Summary Banner inside 3D Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF12352B),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "CUT PARCEL GEODESIC AREA",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFA7F3D0)
                                    )
                                    Text(
                                        text = "${LandAndConstructionEngine.formatDec(mapSurveyResult.areaSqFeet, 1)} Sq.Ft",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = CadPolygonStroke,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${LandAndConstructionEngine.formatDec(mapSurveyResult.areaSqMeters, 2)} Sq.Meters (m²)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "TOTAL PERIMETER",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFA7F3D0)
                                    )
                                    Text(
                                        text = "${LandAndConstructionEngine.formatDec(mapSurveyResult.perimeterFeet, 1)} ft",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${LandAndConstructionEngine.formatDec(mapSurveyResult.perimeterMeters, 2)} m",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFD1FAE5)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Native Ad after Card 3 (Geo-Location + Map Line Survey + 3D Cutout)
            item {
                AdMobNativeAdCard(slotIndex = 5)
            }

            item {
                // Card 4: Exact Side-by-Side Geodesic Boundary Measurements Table
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "4. EXACT BOUNDARY LINE MEASUREMENTS (ALL SIDES)",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "True geodesic distance & compass direction of every line drawn on the map",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        mapSurveyResult.edges.forEachIndexed { idx, edge ->
                            if (idx > 0) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${edge.label}  •  Compass ${LandAndConstructionEngine.formatDec(edge.bearingDegrees, 1)}° (${edge.compassDirection})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${LandAndConstructionEngine.formatDec(edge.lengthMeters, 2)} Meters  |  ${LandAndConstructionEngine.formatDec(edge.lengthVaar, 2)} Vaar (Yards)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "${LandAndConstructionEngine.formatDec(edge.lengthFeet, 2)} ft",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Card 5: KHEDUT & LANDOWNER SPECIAL TOOLKIT (Partition, Fencing, Crop Calculator)
                KhedutAndLandownerSpecialSuiteCard(
                    areaSqFeet = mapSurveyResult.areaSqFeet,
                    perimeterFeet = mapSurveyResult.perimeterFeet,
                    selectedStateProfile = selectedStateProfile,
                    selectedKhedutTab = selectedKhedutTab,
                    onSelectKhedutTab = { viewModel.selectKhedutTab(it) },
                    numberOfHeirs = numberOfHeirs,
                    onNumberOfHeirsChange = { numberOfHeirs = it },
                    roadCutPercent = roadCutPercent,
                    onRoadCutPercentChange = { roadCutPercent = it },
                    fencingPoleSpacingFt = fencingPoleSpacingFt,
                    onFencingPoleSpacingChange = { fencingPoleSpacingFt = it },
                    fencingWireRows = fencingWireRows,
                    onFencingWireRowsChange = { fencingWireRows = it },
                    selectedFarmerCrop = selectedFarmerCrop,
                    onSelectFarmerCrop = { selectedFarmerCrop = it }
                )
            }

            item {
                // Country-Specific Converted Units Header
                Text(
                    text = "ALL REGIONAL & COUNTRY MEASUREMENTS (${selectedStateProfile.stateName})",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Native Ad after Card 5 (Boundary Table + Khedut Pro Suite)
            item {
                AdMobNativeAdCard(slotIndex = 6)
            }

            itemsIndexed(mapSurveyResult.countryConversions, key = { _, it -> it.unit.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("map_converted_unit_${item.unit.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                text = "${item.unit.name} (${item.unit.localName})",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = item.unit.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = LandAndConstructionEngine.formatDec(
                                item.convertedValue,
                                if (item.convertedValue < 10.0) 4 else 2
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Native Ad after every 3 country conversion boxes (more boxes = more native ads)
                if ((index + 1) % 3 == 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AdMobNativeAdCard(slotIndex = index / 3 + 7)
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
                        onClick = { viewModel.saveCurrentMapCutSurvey() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_save_map_cut")
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = uiStrings.saveToVault)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.saveToVault)
                    }

                    Button(
                        onClick = { viewModel.exportCurrentMapCutPdf(context) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_export_map_cut_pdf")
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = uiStrings.exportPdf)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uiStrings.exportPdf)
                    }
                }
            }
            } // Show calculated CAD, boundary tables, conversions and export only after real points exist.
        }
    }
}

@Composable
private fun LiveTickerMetric(
    label: String,
    value: String,
    valueColor: Color
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Farmer & Landowner Special Toolkit Card:
 * 1) Land Partition / Family Heirs Splitter with Live Sub-Plot Blueprint
 * 2) Farm Boundary Fencing, Poles & Barbed Wire Estimator
 * 3) Khedut Crop Seed, Fertilizer, Tractor & Expected Yield (Man 20Kg) Calculator
 */
@Composable
private fun KhedutAndLandownerSpecialSuiteCard(
    areaSqFeet: Double,
    perimeterFeet: Double,
    selectedStateProfile: com.example.domain.StateLandProfile,
    selectedKhedutTab: Int,
    onSelectKhedutTab: (Int) -> Unit,
    numberOfHeirs: Int,
    onNumberOfHeirsChange: (Int) -> Unit,
    roadCutPercent: Float,
    onRoadCutPercentChange: (Float) -> Unit,
    fencingPoleSpacingFt: Float,
    onFencingPoleSpacingChange: (Float) -> Unit,
    fencingWireRows: Int,
    onFencingWireRowsChange: (Int) -> Unit,
    selectedFarmerCrop: FarmerCropSpec,
    onSelectFarmerCrop: (FarmerCropSpec) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("khedut_special_suite_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "Khedut & Landowner Toolkit",
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "5. KHEDUT & LANDOWNER PRO SUITE (ખેડૂત અને જમીન માલિકના ખાસ સાધનો)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Automatically calculated from your Map Line Cutout (${LandAndConstructionEngine.formatDec(areaSqFeet, 0)} Sq.Ft)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedKhedutTab == 0,
                    onClick = { onSelectKhedutTab(0) },
                    label = { Text("1. Land Partition (ભાગલા/વારસાઈ)") },
                    leadingIcon = { Icon(Icons.Default.GridOn, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("khedut_tab_partition")
                )
                FilterChip(
                    selected = selectedKhedutTab == 1,
                    onClick = { onSelectKhedutTab(1) },
                    label = { Text("2. Fencing & Poles (તારની વાડ)") },
                    leadingIcon = { Icon(Icons.Default.Fence, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("khedut_tab_fencing")
                )
                FilterChip(
                    selected = selectedKhedutTab == 2,
                    onClick = { onSelectKhedutTab(2) },
                    label = { Text("3. Crop Seed & Fertilizer (પાક-ખાતર)") },
                    leadingIcon = { Icon(Icons.Default.Agriculture, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("khedut_tab_crop")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedKhedutTab) {
                0 -> {
                    val partition = remember(areaSqFeet, roadCutPercent, numberOfHeirs, selectedStateProfile) {
                        FarmerAndLandToolsEngine.calculateLandPartition(
                            grossAreaSqFt = areaSqFeet,
                            roadCutPercent = roadCutPercent.toDouble(),
                            numberOfHeirs = numberOfHeirs,
                            stateProfile = selectedStateProfile
                        )
                    }

                    Text(
                        text = "Split Measured Land Among Family Heirs / Co-Owners (વારસદારો કે ભાગીદારો વચ્ચે જમીનના સરખા ભાગ)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Number of Heirs selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Shares (ભાગ):", style = MaterialTheme.typography.labelMedium)
                        listOf(2, 3, 4, 5, 6, 8).forEach { count ->
                            FilterChip(
                                selected = numberOfHeirs == count,
                                onClick = { onNumberOfHeirsChange(count) },
                                label = { Text("$count Shares") }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Road/Canal Cut (${roadCutPercent.toInt()}%)",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.width(120.dp)
                        )
                        Slider(
                            value = roadCutPercent,
                            onValueChange = onRoadCutPercentChange,
                            valueRange = 0f..30f,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Visual Partition Blueprint Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0A221B))
                    ) {
                        val w = size.width
                        val h = size.height
                        val pad = 16.dp.toPx()
                        val drawW = w - pad * 2
                        val drawH = h - pad * 2

                        // Road cut strip at top if roadCutPercent > 0
                        val roadRatio = (roadCutPercent / 100f).coerceIn(0f, 0.35f)
                        val roadH = drawH * roadRatio
                        if (roadH > 2f) {
                            drawRect(
                                color = Color(0xFF334155),
                                topLeft = Offset(pad, pad),
                                size = Size(drawW, roadH)
                            )
                        }

                        val netTop = pad + roadH
                        val netH = drawH - roadH
                        val shareW = drawW / numberOfHeirs.coerceAtLeast(1)

                        val textPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 24f
                            isAntiAlias = true
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }

                        for (i in 0 until numberOfHeirs) {
                            val sx = pad + i * shareW
                            drawRect(
                                color = if (i % 2 == 0) Color(0x5510B981) else Color(0x55F59E0B),
                                topLeft = Offset(sx, netTop),
                                size = Size(shareW, netH)
                            )
                            drawRect(
                                color = CadPolygonStroke,
                                topLeft = Offset(sx, netTop),
                                size = Size(shareW, netH),
                                style = Stroke(width = 2.dp.toPx())
                            )
                            drawContext.canvas.nativeCanvas.drawText(
                                "Share #${i + 1}",
                                sx + 10f,
                                netTop + netH * 0.45f,
                                textPaint
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val sampleShare = partition.shares.firstOrNull()
                    if (sampleShare != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Each Share Gets (${LandAndConstructionEngine.formatDec(sampleShare.sharePercent, 1)}% after ${LandAndConstructionEngine.formatDec(partition.roadOrCanalCutSqFt, 0)} Sq.Ft Road Cut):",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• ${LandAndConstructionEngine.formatDec(sampleShare.primaryRegionalUnitValue, 3)} ${partition.primaryUnitName}  |  ${LandAndConstructionEngine.formatDec(sampleShare.gunthaValue, 2)} Guntha  |  ${LandAndConstructionEngine.formatDec(sampleShare.vaarValue, 1)} Vaar",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "• Exact Area: ${LandAndConstructionEngine.formatDec(sampleShare.areaSqFt, 1)} Sq.Ft (${LandAndConstructionEngine.formatDec(sampleShare.areaSqMeters, 2)} m²)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "• Frontage Width per Brother/Partner: ${LandAndConstructionEngine.formatDec(sampleShare.frontageWidthFt, 1)} ft × Depth ${LandAndConstructionEngine.formatDec(sampleShare.depthFt, 1)} ft",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                1 -> {
                    val fencing = remember(perimeterFeet, fencingPoleSpacingFt, fencingWireRows) {
                        FarmerAndLandToolsEngine.calculateFarmFencing(
                            perimeterFeet = perimeterFeet,
                            poleSpacingFeet = fencingPoleSpacingFt.toDouble(),
                            barbedWireRows = fencingWireRows
                        )
                    }

                    Text(
                        text = "Farm Boundary Fencing, Cement Poles & Barbed Wire (તારની વાડ અને થાંભલાનો ખર્ચ)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Wire Rows (તારની લાઈન):", style = MaterialTheme.typography.labelMedium)
                        listOf(3, 4, 5, 6, 7).forEach { rows ->
                            FilterChip(
                                selected = fencingWireRows == rows,
                                onClick = { onFencingWireRowsChange(rows) },
                                label = { Text("$rows Rows") }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pole Spacing (${fencingPoleSpacingFt.toInt()} ft)",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.width(120.dp)
                        )
                        Slider(
                            value = fencingPoleSpacingFt,
                            onValueChange = onFencingPoleSpacingChange,
                            valueRange = 5f..15f,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Boundary Perimeter: ${LandAndConstructionEngine.formatDec(fencing.perimeterFeet, 1)} ft (${LandAndConstructionEngine.formatDec(fencing.perimeterMeters, 1)} m)",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "• Total Cement/Iron Poles Needed: ${fencing.totalPolesCount} Poles (${fencing.mainPolesCount} Line + ${fencing.cornerStrutPolesCount} Corner Struts)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• Barbed Wire Required (${fencing.barbedWireRows} Rows): ${LandAndConstructionEngine.formatDec(fencing.totalBarbedWireWeightKg, 1)} Kg (Total ${LandAndConstructionEngine.formatDec(fencing.totalWireLengthMeters, 0)} m wire)",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Foundation Cement for Pole Grouting: ${fencing.cementBagsForPoles} Bags (50 Kg)",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Estimated Total Fencing Material Cost: ${LandAndConstructionEngine.formatCurrency(fencing.totalEstimatedFencingCostInr)}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                2 -> {
                    val cropEstimate = remember(selectedFarmerCrop, areaSqFeet, selectedStateProfile) {
                        FarmerAndLandToolsEngine.calculateFarmerCropInputs(
                            crop = selectedFarmerCrop,
                            areaSqFt = areaSqFeet,
                            vighaSqFt = selectedStateProfile.vighaSqFt
                        )
                    }

                    Text(
                        text = "Khedut Crop Seed, Fertilizer, Water & Yield (પાક બિયારણ, ખાતર અને ઉત્પાદન અંદાજ)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FarmerCropSpec.entries.forEach { crop ->
                            FilterChip(
                                selected = selectedFarmerCrop == crop,
                                onClick = { onSelectFarmerCrop(crop) },
                                label = { Text(crop.gujaratiName) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${selectedFarmerCrop.gujaratiName} • For ${LandAndConstructionEngine.formatDec(cropEstimate.areaVigha, 2)} Vigha (${LandAndConstructionEngine.formatDec(cropEstimate.areaAcres, 2)} Acres)",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "• Seed Required (બિયારણ): ${LandAndConstructionEngine.formatDec(cropEstimate.totalSeedKg, 2)} Kg",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• Fertilizer Bags (ખાતર થેલી): DAP: ${LandAndConstructionEngine.formatDec(cropEstimate.dapBags50Kg, 1)} Bags | Urea: ${LandAndConstructionEngine.formatDec(cropEstimate.ureaBags45Kg, 1)} Bags | Potash: ${LandAndConstructionEngine.formatDec(cropEstimate.potashBags50Kg, 1)} Bags",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Tractor Ploughing & Rotavator: ${LandAndConstructionEngine.formatDec(cropEstimate.tractorHours, 1)} Hours (~${LandAndConstructionEngine.formatDec(cropEstimate.tractorDieselLiters, 1)} L Diesel)",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Irrigation Water / Cycle: ${LandAndConstructionEngine.formatDec(cropEstimate.irrigationWaterLitersPerCycle, 0)} Liters",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Expected Harvest Yield: ${LandAndConstructionEngine.formatDec(cropEstimate.expectedYieldMan20Kg, 1)} Man (મણ - 20 Kg)  |  ${LandAndConstructionEngine.formatDec(cropEstimate.expectedYieldQuintal, 1)} Quintals",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• Est. Gross Crop Value: ${LandAndConstructionEngine.formatCurrency(cropEstimate.estimatedGrossIncomeInr)} (Net Profit ~${LandAndConstructionEngine.formatCurrency(cropEstimate.estimatedNetProfitInr)})",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive 3D Isometric/Perspective Extruded Land Parcel Cutout Canvas.
 */
@Composable
fun Plot3DExtrudedCutoutCanvas(
    surveyResult: GeodesicPolygonSurveyResult,
    yawDegrees: Float,
    pitchDegrees: Float,
    extrusionHeightRatio: Float,
    onDragRotate: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .background(Color(0xFF071510))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDragRotate(dragAmount.x, dragAmount.y)
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val localPts = surveyResult.localPoints
        if (localPts.size < 3) return@Canvas

        val xs = localPts.map { it.eastFeet.toFloat() }
        val ys = localPts.map { it.northFeet.toFloat() }
        val minX = xs.minOrNull() ?: 0f
        val maxX = (xs.maxOrNull() ?: 1f).coerceAtLeast(minX + 1f)
        val minY = ys.minOrNull() ?: 0f
        val maxY = (ys.maxOrNull() ?: 1f).coerceAtLeast(minY + 1f)
        val centerX = (minX + maxX) / 2f
        val centerY = (minY + maxY) / 2f
        val maxSpan = maxOf(maxX - minX, maxY - minY, 1f)

        val yawRad = (yawDegrees * PI / 180.0).toFloat()
        val pitchRad = (pitchDegrees * PI / 180.0).toFloat()
        val cosYaw = cos(yawRad)
        val sinYaw = sin(yawRad)
        val sinPitch = sin(pitchRad)
        val cosPitch = cos(pitchRad)

        val scaleRadius = minOf(w, h) * 0.34f
        val extrudePx = minOf(w, h) * extrusionHeightRatio * 0.42f * cosPitch
        val screenCenterX = w / 2f
        val screenCenterY = h * 0.56f

        fun projectPoint(normX: Float, normY: Float, zHeightPx: Float): Offset {
            val rx = normX * cosYaw - normY * sinYaw
            val ry = normX * sinYaw + normY * cosYaw
            val sx = screenCenterX + rx * scaleRadius
            val sy = screenCenterY - ry * scaleRadius * sinPitch - zHeightPx
            return Offset(sx, sy)
        }

        // Draw subtle 3D ground reference grid
        for (g in -3..3) {
            val gf = g / 3f
            val gStartX = projectPoint(-1.15f, gf * 1.15f, 0f)
            val gEndX = projectPoint(1.15f, gf * 1.15f, 0f)
            drawLine(Color(0xFF14352B), gStartX, gEndX, strokeWidth = 1f)

            val gStartY = projectPoint(gf * 1.15f, -1.15f, 0f)
            val gEndY = projectPoint(gf * 1.15f, 1.15f, 0f)
            drawLine(Color(0xFF14352B), gStartY, gEndY, strokeWidth = 1f)
        }

        val baseProjected = localPts.map { pt ->
            val nx = ((pt.eastFeet.toFloat() - centerX) / (maxSpan * 0.5f))
            val ny = ((pt.northFeet.toFloat() - centerY) / (maxSpan * 0.5f))
            projectPoint(nx, ny, 0f)
        }

        val topProjected = localPts.map { pt ->
            val nx = ((pt.eastFeet.toFloat() - centerX) / (maxSpan * 0.5f))
            val ny = ((pt.northFeet.toFloat() - centerY) / (maxSpan * 0.5f))
            projectPoint(nx, ny, extrudePx)
        }

        val shadowPath = Path().apply {
            moveTo(baseProjected[0].x, baseProjected[0].y)
            for (i in 1 until baseProjected.size) {
                lineTo(baseProjected[i].x, baseProjected[i].y)
            }
            close()
        }
        drawPath(shadowPath, color = Color(0x55000000))
        drawPath(
            shadowPath,
            color = Color(0xFF38BDF8),
            style = Stroke(
                width = 1.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
            )
        )

        for (i in localPts.indices) {
            val j = (i + 1) % localPts.size
            val wallPath = Path().apply {
                moveTo(baseProjected[i].x, baseProjected[i].y)
                lineTo(baseProjected[j].x, baseProjected[j].y)
                lineTo(topProjected[j].x, topProjected[j].y)
                lineTo(topProjected[i].x, topProjected[i].y)
                close()
            }
            val wallShade = if (i % 2 == 0) Color(0xAA0D9488) else Color(0xAA0F766E)
            drawPath(wallPath, color = wallShade)
            drawPath(wallPath, color = Color(0xFF5EEAD4), style = Stroke(width = 1.5f))
        }

        val topPath = Path().apply {
            moveTo(topProjected[0].x, topProjected[0].y)
            for (i in 1 until topProjected.size) {
                lineTo(topProjected[i].x, topProjected[i].y)
            }
            close()
        }
        drawPath(topPath, color = Color(0x66F59E0B))
        drawPath(topPath, color = CadPolygonStroke, style = Stroke(width = 3.dp.toPx()))

        val labelPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 24f
            isAntiAlias = true
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        val edgePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#FEF08A")
            textSize = 23f
            isAntiAlias = true
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        for (i in topProjected.indices) {
            val topPt = topProjected[i]
            val basePt = baseProjected[i]
            drawLine(
                color = CadPolygonStroke,
                start = basePt,
                end = topPt,
                strokeWidth = 2.dp.toPx()
            )
            drawCircle(color = CadVertexPoint, radius = 5.dp.toPx(), center = topPt)
            drawContext.canvas.nativeCanvas.drawText(
                "P${i + 1}",
                topPt.x - 14f,
                topPt.y - 12f,
                labelPaint
            )

            val j = (i + 1) % topProjected.size
            val midX = (topPt.x + topProjected[j].x) / 2f
            val midY = (topPt.y + topProjected[j].y) / 2f
            val edgeInfo = surveyResult.edges.getOrNull(i)
            if (edgeInfo != null) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${LandAndConstructionEngine.formatDec(edgeInfo.lengthFeet, 1)} ft",
                    midX - 28f,
                    midY - 6f,
                    edgePaint
                )
            }
        }
    }
}

/**
 * Stock-Market / TradingView-Style Interactive Multi-Line & Crosshair Survey Canvas.
 * Supports:
 * - Touch-dragging any existing vertex (P1, P2, P3...) to adjust boundary lines in real-time
 * - Tapping anywhere to draw a new connected trendline segment
 * - Live Stock-Market Crosshair (+ dashed axes) showing live feet & GPS coordinates
 */
@Composable
fun InteractiveTradingLineMapCanvas(
    surveyResult: GeodesicPolygonSurveyResult,
    onTapAddRelativePoint: (Double, Double) -> Unit,
    onDragVertexRelative: (Int, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var crosshairPos by remember { mutableStateOf<Offset?>(null) }
    var draggedVertexIndex by remember { mutableStateOf<Int?>(null) }

    Canvas(
        modifier = modifier
            .background(Color(0xFF071A14))
            .pointerInput(surveyResult.points) {
                detectTapGestures { offset ->
                    crosshairPos = offset
                    val normX = (offset.x / size.width - 0.5) * 0.0014
                    val normY = (0.5 - offset.y / size.height) * 0.0014
                    onTapAddRelativePoint(normY, normX)
                }
            }
            .pointerInput(surveyResult.points) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        crosshairPos = startOffset
                        val pts = surveyResult.localPoints
                        if (pts.isNotEmpty()) {
                            val xs = pts.map { it.eastFeet.toFloat() }
                            val ys = pts.map { it.northFeet.toFloat() }
                            val minX = xs.minOrNull() ?: 0f
                            val maxX = (xs.maxOrNull() ?: 10f).coerceAtLeast(minX + 1f)
                            val minY = ys.minOrNull() ?: 0f
                            val maxY = (ys.maxOrNull() ?: 10f).coerceAtLeast(minY + 1f)
                            val pad = 42.dp.toPx()
                            val availW = (size.width - pad * 2).coerceAtLeast(20f)
                            val availH = (size.height - pad * 2).coerceAtLeast(20f)
                            val scale = minOf(availW / (maxX - minX), availH / (maxY - minY))
                            val offsetX = pad + (availW - (maxX - minX) * scale) / 2f
                            val offsetY = pad + (availH - (maxY - minY) * scale) / 2f

                            var closestIdx = 0
                            var minDist = Float.MAX_VALUE
                            pts.forEachIndexed { idx, p ->
                                val sx = offsetX + (p.eastFeet.toFloat() - minX) * scale
                                val sy = size.height - (offsetY + (p.northFeet.toFloat() - minY) * scale)
                                val dist = hypot(startOffset.x - sx, startOffset.y - sy)
                                if (dist < minDist) {
                                    minDist = dist
                                    closestIdx = idx
                                }
                            }
                            draggedVertexIndex = closestIdx
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        crosshairPos = change.position
                        val idx = draggedVertexIndex
                        if (idx != null && idx in surveyResult.points.indices) {
                            val pt = surveyResult.points[idx]
                            val dLng = (dragAmount.x / size.width.coerceAtLeast(1)) * 0.0014
                            val dLat = (-dragAmount.y / size.height.coerceAtLeast(1)) * 0.0014
                            onDragVertexRelative(idx, pt.lat + dLat, pt.lng + dLng)
                        }
                    },
                    onDragEnd = {
                        draggedVertexIndex = null
                    }
                )
            }
    ) {
        val w = size.width
        val h = size.height
        val step = 28.dp.toPx()
        var gx = 0f
        while (gx <= w) {
            drawLine(CadGridLine, Offset(gx, 0f), Offset(gx, h), 1f)
            gx += step
        }
        var gy = 0f
        while (gy <= h) {
            drawLine(CadGridLine, Offset(0f, gy), Offset(w, gy), 1f)
            gy += step
        }

        // Draw Stock-Market Crosshair if active
        crosshairPos?.let { ch ->
            val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
            drawLine(
                color = Color(0x8838BDF8),
                start = Offset(0f, ch.y),
                end = Offset(w, ch.y),
                strokeWidth = 1.5f,
                pathEffect = dash
            )
            drawLine(
                color = Color(0x8838BDF8),
                start = Offset(ch.x, 0f),
                end = Offset(ch.x, h),
                strokeWidth = 1.5f,
                pathEffect = dash
            )
        }

        val pts = surveyResult.localPoints
        if (pts.isEmpty()) return@Canvas

        if (pts.size < 3) {
            // Draw single point or 2-point line during initial line drawing mode
            val centerOffset = Offset(w / 2f, h / 2f)
            drawCircle(CadVertexPoint, 7.dp.toPx(), centerOffset)
            val hintPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#FDE68A")
                textSize = 25f
                isAntiAlias = true
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            drawContext.canvas.nativeCanvas.drawText(
                "P1 (Geo-Center) • Tap anywhere to draw Line 1, Line 2, Line 3...",
                24f,
                h - 24f,
                hintPaint
            )
            return@Canvas
        }

        val xs = pts.map { it.eastFeet.toFloat() }
        val ys = pts.map { it.northFeet.toFloat() }
        val minX = xs.minOrNull() ?: 0f
        val maxX = (xs.maxOrNull() ?: 10f).coerceAtLeast(minX + 1f)
        val minY = ys.minOrNull() ?: 0f
        val maxY = (ys.maxOrNull() ?: 10f).coerceAtLeast(minY + 1f)

        val pad = 42.dp.toPx()
        val availW = (w - pad * 2).coerceAtLeast(20f)
        val availH = (h - pad * 2).coerceAtLeast(20f)
        val scale = minOf(availW / (maxX - minX), availH / (maxY - minY))
        val offsetX = pad + (availW - (maxX - minX) * scale) / 2f
        val offsetY = pad + (availH - (maxY - minY) * scale) / 2f

        val mapped = pts.map { p ->
            Offset(
                x = offsetX + (p.eastFeet.toFloat() - minX) * scale,
                y = h - (offsetY + (p.northFeet.toFloat() - minY) * scale)
            )
        }

        val path = Path().apply {
            moveTo(mapped[0].x, mapped[0].y)
            for (i in 1 until mapped.size) {
                lineTo(mapped[i].x, mapped[i].y)
            }
            close()
        }
        drawPath(path, CadPolygonFill)
        drawPath(path, CadPolygonStroke, style = Stroke(width = 3.dp.toPx()))

        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 23f
            isAntiAlias = true
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        mapped.forEachIndexed { i, pt ->
            drawCircle(Color.White, 8.dp.toPx(), pt)
            drawCircle(CadVertexPoint, 6.dp.toPx(), pt)
            val j = (i + 1) % mapped.size
            val mid = Offset((pt.x + mapped[j].x) / 2f, (pt.y + mapped[j].y) / 2f)
            val edge = surveyResult.edges.getOrNull(i)
            val lenFt = edge?.lengthFeet ?: 0.0
            val dir = edge?.compassDirection ?: ""
            drawContext.canvas.nativeCanvas.drawText(
                "L${i + 1}: ${LandAndConstructionEngine.formatDec(lenFt, 1)}' ($dir)",
                mid.x - 42f,
                mid.y - 8f,
                paint
            )
        }
    }
}

/**
 * Embedded Interactive Satellite & Hybrid Map powered by Leaflet + Esri World Imagery + OpenStreetMap
 * with Stock-Chart Style Draggable Corner Handles & Live Line-Length Pills on every drawn line.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InteractiveSatelliteLeafletMap(
    points: List<GpsLatLng>,
    centroid: GpsLatLng,
    onMapTapAddPoint: (Double, Double) -> Unit,
    onMarkerDragged: (Int, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val pointsJson = remember(points) {
        points.joinToString(prefix = "[", postfix = "]") { "[${it.lat},${it.lng}]" }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun onMapClicked(lat: Double, lng: Double) {
                            post { onMapTapAddPoint(lat, lng) }
                        }

                        @JavascriptInterface
                        fun onPointDragged(index: Int, lat: Double, lng: Double) {
                            post { onMarkerDragged(index, lat, lng) }
                        }
                    },
                    "AndroidSurveyBridge"
                )
                loadDataWithBaseURL(
                    "https://unpkg.com/",
                    buildLeafletSatelliteHtml(centroid.lat, centroid.lng, pointsJson),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        update = { webView ->
            webView.evaluateJavascript("if(window.updateSurveyPolygon){window.updateSurveyPolygon($pointsJson, ${centroid.lat}, ${centroid.lng});}", null)
        }
    )
}

private fun buildLeafletSatelliteHtml(centerLat: Double, centerLng: Double, pointsJson: String): String {
    return """
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<style>
  html, body, #map { margin: 0; padding: 0; width: 100%; height: 100%; background: #0B221C; font-family: sans-serif; }
  .cut-badge {
    position: absolute; bottom: 8px; left: 8px; z-index: 999;
    background: rgba(7,23,18,0.92); color: #FDE68A; padding: 5px 10px;
    border-radius: 6px; font-size: 11px; border: 1px solid #F59E0B;
  }
  .edge-label {
    background: rgba(9, 27, 22, 0.90);
    color: #38BDF8;
    border: 1px solid #FBBF24;
    border-radius: 4px;
    padding: 1px 5px;
    font-size: 10px;
    font-weight: bold;
    white-space: nowrap;
  }
</style>
</head>
<body>
<div id="map"></div>
<div class="cut-badge">Tap map to draw lines (L1, L2, L3...) or drag green corners to resize</div>
<script>
  var map = L.map('map', { zoomControl: true }).setView([$centerLat, $centerLng], 18);
  var satLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
    maxZoom: 20, attribution: 'Satellite'
  }).addTo(map);
  var osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19, opacity: 0.35
  }).addTo(map);

  var polyLayer = null;
  var markers = [];
  var edgeLabels = [];

  function distFeet(lat1, lon1, lat2, lon2) {
    var R = 6378137.0;
    var dLat = (lat2 - lat1) * Math.PI / 180.0;
    var dLon = (lon2 - lon1) * Math.PI / 180.0;
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(lat1 * Math.PI / 180.0) * Math.cos(lat2 * Math.PI / 180.0) *
            Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return (R * c) * 3.280839895;
  }

  window.updateSurveyPolygon = function(pts, cLat, cLng) {
    if (polyLayer) { map.removeLayer(polyLayer); }
    markers.forEach(function(m) { map.removeLayer(m); });
    edgeLabels.forEach(function(l) { map.removeLayer(l); });
    markers = [];
    edgeLabels = [];

    if (pts && pts.length > 0) {
      polyLayer = L.polygon(pts, {
        color: '#FBBF24', weight: 3, fillColor: '#10B981', fillOpacity: 0.28
      }).addTo(map);

      pts.forEach(function(pt, i) {
        var icon = L.divIcon({
          className: '',
          html: '<div style="width:14px;height:14px;border-radius:7px;background:#10B981;border:2px solid #FFFFFF;box-shadow:0 0 4px #000;"></div>',
          iconSize: [14, 14],
          iconAnchor: [7, 7]
        });
        var marker = L.marker(pt, { draggable: true, icon: icon })
          .addTo(map)
          .bindTooltip("P" + (i + 1), {permanent: true, direction: 'top', offset: [0, -6]});

        marker.on('dragend', function(ev) {
          var pos = ev.target.getLatLng();
          if (window.AndroidSurveyBridge && window.AndroidSurveyBridge.onPointDragged) {
            window.AndroidSurveyBridge.onPointDragged(i, pos.lat, pos.lng);
          }
        });
        markers.push(marker);

        if (pts.length >= 2) {
          var nextIdx = (i + 1) % pts.length;
          if (pts.length > 2 || i === 0) {
            var p2 = pts[nextIdx];
            var midLat = (pt[0] + p2[0]) / 2.0;
            var midLng = (pt[1] + p2[1]) / 2.0;
            var ft = distFeet(pt[0], pt[1], p2[0], p2[1]);
            var lbl = L.marker([midLat, midLng], {
              interactive: false,
              icon: L.divIcon({
                className: 'edge-label',
                html: 'L' + (i + 1) + ': ' + ft.toFixed(1) + ' ft',
                iconSize: null
              })
            }).addTo(map);
            edgeLabels.push(lbl);
          }
        }
      });
    }
  };

  window.updateSurveyPolygon($pointsJson, $centerLat, $centerLng);

  map.on('click', function(e) {
    if (window.AndroidSurveyBridge && window.AndroidSurveyBridge.onMapClicked) {
      window.AndroidSurveyBridge.onMapClicked(e.latlng.lat, e.latlng.lng);
    }
  });
</script>
</body>
</html>
    """.trimIndent()
}
