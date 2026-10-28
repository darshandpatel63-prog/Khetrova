package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.AppLanguage
import com.example.ui.components.KhetrovaManualNativeAdSlot
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private data class ManualParagraph(
    val heading: String,
    val body: String
)

private sealed class ManualListRow {
    abstract val key: String
    data class Content(val index: Int, val paragraph: ManualParagraph) : ManualListRow() {
        override val key: String = "manual-content-$index"
    }
    data class Ad(val index: Int) : ManualListRow() {
        override val key: String = "manual-ad-$index"
    }
}

private val khetrovaManualEnglish = listOf(
    ManualParagraph("1. Getting started and navigation", "Choose your preferred language when the app opens. Use the bottom tabs to move between Land Units, Map & 3D, Plot CAD, Materials, and Daily & Vault. Open Menu • All Tools to jump directly to a specific feature. The app does not load a sample land parcel for you."),
    ManualParagraph("2. Land units and regional standards", "Open Land Units, choose a country or state profile, select the starting measurement unit, and enter the area you actually know. Khetrova converts that value into the units available for the selected region. Regional units such as Vigha can differ from place to place, so confirm the selected local standard before relying on a conversion."),
    ManualParagraph("3. Land value and stamp-duty estimates", "In Land Units, enter your real land area and the applicable rate to see an estimated land value and the configured stamp-duty estimate. This is a calculator result, not a government valuation certificate or a legal quote. Confirm current local rates, registration fees, and rules with the relevant authority."),
    ManualParagraph("4. Start a real map survey", "Open Map & 3D. Enter the real latitude/longitude of your plot or paste a Google Maps link, then tap Jump to Location. The map starts without an example boundary. Tap the actual corners of the plot to add points, or drag a point to refine its location. Add at least three real boundary points before interpreting the computed area or perimeter."),
    ManualParagraph("5. Edit, clear and transfer map measurements", "Use Undo Line to remove the last point. Clear Boundary removes the measured points; it does not create a replacement box. Once you have traced the real boundary, review the side lengths and compass directions. Send Lines to 2D CAD transfers the calculated sides only when a usable survey exists. Satellite imagery, GPS accuracy, zoom level, and how accurately you choose corners affect results."),
    ManualParagraph("6. 3D land cutout and regional conversions", "After entering a real boundary, review the 3D cutout and rotate or tilt the view to inspect its shape. Check the area, perimeter, side-by-side boundary table, and regional-unit conversions. These outputs depend on the points you selected; they are not a certified cadastral survey. Save or export a report only after checking that every boundary point belongs to your actual land."),
    ManualParagraph("7. 2D Plot CAD and four-side calculation", "Open Plot CAD and enter the measured North, South, East, and West sides. Add the measured diagonal when available. Auto-calibrate diagonal estimates a diagonal from the four sides; it is not a substitute for measuring a diagonal on site. The calculator can compare triangulated area with a simple average formula, but the result is only as good as the measurements entered."),
    ManualParagraph("8. Construction material estimates (BOQ)", "Open Materials, select the relevant estimator—tiles, brickwork, plaster, or paint—and enter the actual room or wall dimensions. Review the calculated quantities, wastage assumptions, material rates, and estimated total. Prices vary by location, supplier, date, specifications, and workmanship, so replace default rates with current local quotes."),
    ManualParagraph("9. India APMC and mandi price information", "Open Daily & Vault and choose the market-price board. Tap Refresh Feed to request official daily India Agmarknet records when the government feed is available. Read the market date and source shown with every quote; a recent sync time does not mean the market price changed at that moment. If a verified record is unavailable, the app should show no quote rather than invent a live price. Global market profiles are not live unless a verified feed has been connected."),
    ManualParagraph("10. Mandi weight and crop-sale bill", "In the daily mandi bill tool, enter the number of bags, average bag weight, bag/tare deduction, rate per 20 kg man, commission and labour/handling charges. Check the net payable result before saving it. Use a rate from the correct commodity, variety, grade, market and date; do not use an unrelated or stale market quote."),
    ManualParagraph("11. Farm labour, tractor, brokerage and daily rojmel", "Use the daily tools to record worker attendance and wages, tractor hours and rent, advances, land-deal token and brokerage amounts, or farm/dairy income and expenses. Replace every input with your real transaction figures. These features calculate the amounts you enter; they do not verify a person's attendance, a property transaction, or a bank payment."),
    ManualParagraph("12. Farm water tank, borewell and crop tools", "Enter the actual tank or well dimensions to estimate storage volume, and provide the borewell depth for the pump estimate. The crop tool uses the selected crop and entered field details to estimate seed, fertilizer, and yield. These are planning estimates: confirm engineering and irrigation requirements with a qualified local professional."),
    ManualParagraph("13. 7/12 revenue conversion and land partition", "Use the 7/12 tool to enter hectare–are–square-metre values from the relevant record and view conversions. Use the land-partition tool with the actual parent-plot area, number of heirs, and road/canal deduction, and review the resulting shares. Khetrova does not fetch or authenticate government land records; verify ownership, legal shares, road rights, and official records with the appropriate authority."),
    ManualParagraph("14. Fencing and farm planning", "The fencing calculator estimates poles and wire from the perimeter, pole spacing, and wire rows. The crop and other farm planning tools calculate from the values you provide. Inspect the assumptions before buying materials, and allow for gates, terrain, corner braces, wastage, and local building or agricultural rules."),
    ManualParagraph("15. Save reports, JSON backup and export PDFs", "Use Save to Vault to store a calculation locally in the app, then open Daily & Vault to review or delete saved items. The vault tools can show saved survey records and support JSON backup where offered. Use Export PDF to create or share a report. Check units, date, inputs, and output before sharing; a saved report is not government-certified. Keep your own backup of important information."),
    ManualParagraph("16. Change language and read translated help", "Open Menu • All Tools or the language control to select another language. The help manual attempts on-device translation for languages supported by the translation model; the first use may download a language model. If a language is not supported or translation fails, the manual remains in English instead of showing a misleading translation. Machine translation can make mistakes, especially with legal and regional land terms."),
    ManualParagraph("17. Ads, the rewarded pass, and privacy", "Manual-page native ads currently use Google's official test ad unit and do not generate revenue. Other banner, interstitial, app-open, and rewarded-ad surfaces in this codebase may still be demo UI. The Menu • All Tools entry for the 1-Hour Popup Ad-Free Pass describes a rewarded-ad flow, but only trust it as live after real rewarded ads and production IDs are configured and tested. Ads must remain clearly labelled. Read Legal & Privacy, and avoid sharing private land documents or personal information unnecessarily."),
    ManualParagraph("18. Final measurement and legal reminder", "Always use field-verified dimensions and check units before acting on a result. Map-derived distances are affected by coordinate accuracy and the points chosen; land-unit standards and fees can vary by jurisdiction; and material prices change. For purchase, construction, boundary dispute, ownership, tax, or registration decisions, confirm the result with a licensed surveyor, engineer, accountant, or competent local authority as appropriate.")
)

private fun mlKitTargetLanguage(appCode: String): String {
    return when (appCode) {
        "en", "en-IN" -> TranslateLanguage.ENGLISH
        "zh-CN", "zh-TW" -> TranslateLanguage.CHINESE
        "hmv" -> "hmn"
        else -> appCode.substringBefore('-')
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToUseKhetrovaScreen(
    selectedLanguage: AppLanguage,
    onBack: () -> Unit
) {
    var displayedParagraphs by remember { mutableStateOf(khetrovaManualEnglish) }
    var pageTitle by remember { mutableStateOf("How to Use Khetrova") }
    var introHeading by remember { mutableStateOf("Complete feature manual") }
    var introBody by remember {
        mutableStateOf("Read the guide in order, or scroll to the feature you need. Use real measurements and verify important results before making decisions.")
    }
    var footerNote by remember {
        mutableStateOf("Translation powered by Google ML Kit where supported. This guide is for product guidance only, not legal, survey, engineering, tax, or financial advice. Verify critical terms and measurements independently.")
    }
    var translationStatus by remember { mutableStateOf("English manual ready.") }
    var isTranslating by remember { mutableStateOf(false) }
    val rows = remember(displayedParagraphs) {
        buildList<ManualListRow> {
            displayedParagraphs.forEachIndexed { index, paragraph ->
                add(ManualListRow.Content(index, paragraph))
                add(ManualListRow.Ad(index))
            }
        }
    }

    BackHandler(enabled = true, onBack = onBack)

    LaunchedEffect(selectedLanguage.code) {
        displayedParagraphs = khetrovaManualEnglish
        pageTitle = "How to Use Khetrova"
        introHeading = "Complete feature manual"
        introBody = "Read the guide in order, or scroll to the feature you need. Use real measurements and verify important results before making decisions."
        footerNote = "Translation powered by Google ML Kit where supported. This guide is for product guidance only, not legal, survey, engineering, tax, or financial advice. Verify critical terms and measurements independently."
        val targetLanguage = mlKitTargetLanguage(selectedLanguage.code)
        if (targetLanguage == TranslateLanguage.ENGLISH) {
            translationStatus = if (selectedLanguage.code == "en-IN") "English (India) manual selected." else "English manual selected."
            isTranslating = false
            return@LaunchedEffect
        }

        if (targetLanguage !in TranslateLanguage.getAllLanguages()) {
            translationStatus = "On-device translation is not available for this language. The manual is shown in English."
            isTranslating = false
            return@LaunchedEffect
        }

        val translator = Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLanguage)
                .build()
        )
        isTranslating = true
        translationStatus = "Preparing the on-device model for ${selectedLanguage.nativeName}… The first download may use mobile data."
        try {
            awaitVoidTask(translator.downloadModelIfNeeded(DownloadConditions.Builder().build()))
            translationStatus = "Translating the manual on this device…"
            pageTitle = awaitStringTask(translator.translate("How to Use Khetrova"))
            introHeading = awaitStringTask(translator.translate("Complete feature manual"))
            introBody = awaitStringTask(translator.translate("Read the guide in order, or scroll to the feature you need. Use real measurements and verify important results before making decisions."))
            footerNote = awaitStringTask(translator.translate("Translation powered by Google ML Kit where supported. This guide is for product guidance only, not legal, survey, engineering, tax, or financial advice. Verify critical terms and measurements independently."))
            displayedParagraphs = khetrovaManualEnglish.map { paragraph ->
                ManualParagraph(
                    heading = awaitStringTask(translator.translate(paragraph.heading)),
                    body = awaitStringTask(translator.translate(paragraph.body))
                )
            }
            translationStatus = "Manual translated on this device to ${selectedLanguage.nativeName}. Machine translation may be imperfect."
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            displayedParagraphs = khetrovaManualEnglish
            pageTitle = "How to Use Khetrova"
            introHeading = "Complete feature manual"
            introBody = "Read the guide in order, or scroll to the feature you need. Use real measurements and verify important results before making decisions."
            footerNote = "Translation powered by Google ML Kit where supported. This guide is for product guidance only, not legal, survey, engineering, tax, or financial advice. Verify critical terms and measurements independently."
            translationStatus = "Translation could not be downloaded or completed. The manual is shown in English."
        } finally {
            translator.close()
            isTranslating = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(pageTitle)
                        Text(selectedLanguage.nativeName, style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { insets ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
                .testTag("khetrova_manual_page"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null)
                            Text(introHeading, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            introBody,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isTranslating) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Language, contentDescription = null)
                            }
                            Text(translationStatus, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }

            item {
                KhetrovaManualNativeAdSlot()
            }

            itemsIndexed(rows, key = { _, row -> row.key }) { _, row ->
                when (row) {
                    is ManualListRow.Content -> Card(
                        modifier = Modifier.fillMaxWidth().testTag("manual_section_${row.index}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                row.paragraph.heading,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(row.paragraph.body, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    is ManualListRow.Ad -> KhetrovaManualNativeAdSlot()
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        footerNote,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            item {
                KhetrovaManualNativeAdSlot()
            }
        }
    }
}

private suspend fun awaitVoidTask(task: Task<Void>) {
    suspendCancellableCoroutine<Unit> { continuation ->
        task.addOnSuccessListener {
            if (continuation.isActive) continuation.resume(Unit)
        }
        task.addOnFailureListener { error ->
            if (continuation.isActive) continuation.resumeWithException(error)
        }
        task.addOnCanceledListener {
            if (continuation.isActive) continuation.cancel()
        }
    }
}

private suspend fun awaitStringTask(task: Task<String>): String {
    return suspendCancellableCoroutine { continuation ->
        task.addOnSuccessListener { value ->
            if (continuation.isActive) continuation.resume(value)
        }
        task.addOnFailureListener { error ->
            if (continuation.isActive) continuation.resumeWithException(error)
        }
        task.addOnCanceledListener {
            if (continuation.isActive) continuation.cancel()
        }
    }
}
