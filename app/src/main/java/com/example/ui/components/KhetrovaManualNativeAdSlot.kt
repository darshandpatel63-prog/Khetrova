package com.example.ui.components

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

private const val KhetrovaNativeTestAdUnitId = "ca-app-pub-3940256099942544/2247696110"

@Composable
fun KhetrovaManualNativeAdSlot(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadFailed by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        var disposed = false
        var loadedAd: NativeAd? = null
        val loader = AdLoader.Builder(context, KhetrovaNativeTestAdUnitId)
            .forNativeAd { ad ->
                if (disposed) {
                    ad.destroy()
                } else {
                    loadedAd?.destroy()
                    loadedAd = ad
                    nativeAd = ad
                    isLoading = false
                    loadFailed = false
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    if (!disposed) {
                        isLoading = false
                        loadFailed = true
                    }
                }
            })
            .build()
        loader.loadAd(AdRequest.Builder().build())

        onDispose {
            disposed = true
            loadedAd?.destroy()
        }
    }

    when {
        nativeAd != null -> Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            AndroidView(
                factory = { viewContext -> createNativeAdView(viewContext) },
                update = { adView -> populateNativeAdView(adView, nativeAd!!) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        isLoading -> Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Text("Loading test ad…", style = MaterialTheme.typography.labelSmall)
            }
        }
        loadFailed -> Spacer(modifier = modifier.height(2.dp))
    }
}

private fun createNativeAdView(context: Context): NativeAdView {
    fun dp(value: Int): Int = (value * context.resources.displayMetrics.density).toInt()

    val nativeAdView = NativeAdView(context)
    nativeAdView.setPadding(dp(12), dp(10), dp(12), dp(12))

    val column = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(AndroidColor.TRANSPARENT)
    }

    val attribution = TextView(context).apply {
        tag = "khetrova-ad-attribution"
        text = "Ad"
        textSize = 12f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(AndroidColor.rgb(4, 120, 87))
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }
    column.addView(attribution, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    ))

    val headingRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }
    val icon = ImageView(context).apply {
        tag = "khetrova-ad-icon"
        scaleType = ImageView.ScaleType.CENTER_CROP
    }
    headingRow.addView(icon, LinearLayout.LayoutParams(dp(48), dp(48)).apply {
        rightMargin = dp(10)
    })

    val textColumn = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
    }
    val advertiser = TextView(context).apply {
        tag = "khetrova-ad-advertiser"
        textSize = 12f
        setTextColor(AndroidColor.DKGRAY)
    }
    val headline = TextView(context).apply {
        tag = "khetrova-ad-headline"
        textSize = 16f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(AndroidColor.BLACK)
        maxLines = 2
    }
    textColumn.addView(advertiser, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    ))
    textColumn.addView(headline, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    ))
    headingRow.addView(textColumn, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
    column.addView(headingRow, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply { topMargin = dp(8) })

    val body = TextView(context).apply {
        tag = "khetrova-ad-body"
        textSize = 14f
        setTextColor(AndroidColor.DKGRAY)
        maxLines = 3
    }
    column.addView(body, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply { topMargin = dp(8) })

    val callToAction = Button(context).apply {
        tag = "khetrova-ad-cta"
        isAllCaps = false
    }
    column.addView(callToAction, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply { topMargin = dp(8) })

    nativeAdView.addView(column, FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    ))

    nativeAdView.iconView = icon
    nativeAdView.advertiserView = advertiser
    nativeAdView.headlineView = headline
    nativeAdView.bodyView = body
    nativeAdView.callToActionView = callToAction
    return nativeAdView
}

private fun populateNativeAdView(adView: NativeAdView, ad: NativeAd) {
    val headline = adView.findViewWithTag<TextView>("khetrova-ad-headline")
    val advertiser = adView.findViewWithTag<TextView>("khetrova-ad-advertiser")
    val body = adView.findViewWithTag<TextView>("khetrova-ad-body")
    val callToAction = adView.findViewWithTag<Button>("khetrova-ad-cta")
    val icon = adView.findViewWithTag<ImageView>("khetrova-ad-icon")

    headline.text = ad.headline ?: ""
    advertiser.text = ad.advertiser.orEmpty()
    advertiser.visibility = if (ad.advertiser.isNullOrBlank()) View.GONE else View.VISIBLE
    body.text = ad.body.orEmpty()
    body.visibility = if (ad.body.isNullOrBlank()) View.GONE else View.VISIBLE
    callToAction.text = ad.callToAction.orEmpty()
    callToAction.visibility = if (ad.callToAction.isNullOrBlank()) View.GONE else View.VISIBLE
    icon.setImageDrawable(ad.icon?.drawable)
    icon.visibility = if (ad.icon?.drawable == null) View.GONE else View.VISIBLE

    adView.setNativeAd(ad)
}
