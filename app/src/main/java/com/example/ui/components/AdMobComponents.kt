package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

object AdMobTestUnitIds {
    const val APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val ADAPTIVE_BANNER_ID = "ca-app-pub-3940256099942544/9214589741"
    const val NATIVE_ADVANCED_ID = "ca-app-pub-3940256099942544/2247696110"
    const val INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
    const val REWARDED_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917"
    const val APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
}

data class NativeTestAdSpec(
    val headline: String,
    val body: String,
    val advertiser: String,
    val ctaText: String,
    val rating: String
)

private val nativeTestAdsCatalog = listOf(
    NativeTestAdSpec(
        headline = "Solar Agricultural Pump & Drip Kit (Test Ad)",
        body = "Save 90% electricity on farm irrigation with 3HP–10HP Solar Submersible Systems.",
        advertiser = "Google AdMob Test • Krushi Solar Tech",
        ctaText = "Learn More",
        rating = "4.9 ★"
    ),
    NativeTestAdSpec(
        headline = "Laser Land Leveler & GPS Survey Tool (Test Ad)",
        body = "Precision field leveling saves 30% irrigation water and boosts crop yield per Vigha.",
        advertiser = "Google AdMob Test • AgriSurvey Pro",
        ctaText = "View Details",
        rating = "4.8 ★"
    ),
    NativeTestAdSpec(
        headline = "IS-456 Waterproof Cement & Tile Adhesive (Test Ad)",
        body = "High-strength polymer tile mortar & damp-proof exterior plaster protection.",
        advertiser = "Google AdMob Test • BuildShield Civil",
        ctaText = "Check Price",
        rating = "4.7 ★"
    ),
    NativeTestAdSpec(
        headline = "GI Barbed Wire & Solar Zatka Fencing (Test Ad)",
        body = "Protect your farm boundary from wild animals with rust-proof 12×12 GI wire.",
        advertiser = "Google AdMob Test • Kisan Fence Guard",
        ctaText = "Get Quote",
        rating = "4.9 ★"
    ),
    NativeTestAdSpec(
        headline = "Kisan Credit Card & Tractor Finance (Test Ad)",
        body = "Instant 7/12 land record crop loan calculation with low 4% interest subsidy.",
        advertiser = "Google AdMob Test • Rural Agri Finance",
        ctaText = "Calculate",
        rating = "4.8 ★"
    )
)

/**
 * Non-intrusive Adaptive Banner Test Ad Bar displayed at the bottom of every page.
 * Remains active even when the 1-Hour Rewarded Ad-Free Pass is active (per user rules).
 */
@Composable
fun AdMobBannerBar(
    adFreeRemainingMinutes: Int,
    onWatchRewardedAdClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF0B241D),
        modifier = modifier
            .fillMaxWidth()
            .border(width = 0.8.dp, color = Color(0xFF1E4D3E))
            .testTag("admob_banner_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFF59E0B)
                ) {
                    Text(
                        text = "Test Ad",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF091D17),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Google AdMob Adaptive Banner • Nice Job! Test Ad Loaded",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Unit: ${AdMobTestUnitIds.ADAPTIVE_BANNER_ID}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFA7F3D0),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quick 1-Hour Popup-Free Rewarded Ad Chip right inside the banner strip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (adFreeRemainingMinutes > 0) Color(0xFF065F46) else Color(0xFF1E3A8A),
                modifier = Modifier
                    .clickable { onWatchRewardedAdClick() }
                    .testTag("btn_banner_rewarded_ad")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (adFreeRemainingMinutes > 0) Icons.Default.Verified else Icons.Default.PlayCircleFilled,
                        contentDescription = "Rewarded Ad 1 Hour Pass",
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (adFreeRemainingMinutes > 0)
                            "${adFreeRemainingMinutes}m No-Popup"
                        else
                            "1Hr Ad-Free",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Clean, UX-friendly Native Advanced Test Ad Card inserted after every 2–3–4 boxes across all lists.
 * More boxes on screen = more Native Ad cards automatically placed without hurting UI/UX.
 * Remains active even when the 1-Hour Rewarded Ad-Free Pass is active.
 */
@Composable
fun AdMobNativeAdCard(
    slotIndex: Int,
    modifier: Modifier = Modifier
) {
    val adSpec = remember(slotIndex) {
        nativeTestAdsCatalog[kotlin.math.abs(slotIndex) % nativeTestAdsCatalog.size]
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("admob_native_ad_card_$slotIndex"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF59E0B)
                    ) {
                        Text(
                            text = "Test Ad • Native #${slotIndex + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF091D17),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = adSpec.advertiser,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = adSpec.rating,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = adSpec.headline,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = adSpec.body,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "AdMob Native ID: ${AdMobTestUnitIds.NATIVE_ADVANCED_ID}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = adSpec.ctaText,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Full-Screen Test Interstitial Ad Modal shown after completing a task (with UX cooldown).
 * Completely suppressed when the 1-Hour Rewarded Ad-Free Pass is active!
 */
@Composable
fun AdMobInterstitialDialog(
    triggerReason: String,
    onDismiss: () -> Unit,
    onSwitchToRewardedAdPass: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .testTag("admob_interstitial_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A221B))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF59E0B)
                    ) {
                        Text(
                            text = "GOOGLE ADMOB • TEST INTERSTITIAL AD",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF091D17),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_interstitial_ad")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Interstitial Ad",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF134E4A), Color(0xFF0F2922))
                            )
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AdsClick,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Nice Job! Google AdMob Test Interstitial",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Shown after: $triggerReason",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA7F3D0)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Test Unit ID: ${AdMobTestUnitIds.INTERSTITIAL_ID}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFDE68A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Shortcut to watch 1 Rewarded Ad & remove Interstitial/AppOpen ads for 1 hour
                OutlinedButton(
                    onClick = onSwitchToRewardedAdPass,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Watch 1 Rewarded Ad → Remove Popup Ads for 1 Hour",
                        color = Color(0xFFFDE68A)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Ad & Continue (બંધ કરો)")
                }
            }
        }
    }
}

/**
 * Interactive Test Rewarded Video Ad Modal.
 * Watching this ad activates the 1-Hour Popup-Free Pass (disables Interstitial & App Open ads for 60 mins,
 * while Banner & Native ads continue to display normally).
 */
@Composable
fun AdMobRewardedAdDialog(
    onRewardEarnedAndClose: () -> Unit,
    onCancel: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(3) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .testTag("admob_rewarded_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1D2E))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF59E0B)
                    ) {
                        Text(
                            text = "GOOGLE ADMOB • TEST REWARDED VIDEO AD",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF091D17),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF112A45))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (secondsLeft > 0)
                                "Rewarded Test Ad Playing... (${secondsLeft}s)"
                            else
                                "Reward Unlocked! 1 Hour No-Popup Pass Ready!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Removes all Interstitial & App Open ads for 60 minutes!\n(Banner & Native ads remain active as configured)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFBAE6FD)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Test Unit ID: ${AdMobTestUnitIds.REWARDED_VIDEO_ID}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFDE68A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { (3 - secondsLeft) / 3f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onRewardEarnedAndClose,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (secondsLeft == 0) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_claim_reward_1hr")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (secondsLeft == 0)
                            "Activate 1 Hour Ad-Free Pass Now (૧ કલાક એડ-ફ્રી ચાલુ કરો)"
                        else
                            "Claim 1 Hour Pass Early (${secondsLeft}s)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Full-Screen Google AdMob Test App Open Ad Modal.
 * Triggered automatically on every 6th app launch (after 5 opens) within 2 seconds of app startup.
 * Suppressed if the 1-Hour Rewarded Ad-Free Pass is active.
 */
@Composable
fun AdMobAppOpenAdDialog(
    launchNumber: Int,
    onContinueToApp: () -> Unit
) {
    Dialog(
        onDismissRequest = onContinueToApp,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .testTag("admob_app_open_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF091E18))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF59E0B)
                    ) {
                        Text(
                            text = "GOOGLE ADMOB • TEST APP OPEN AD (6TH LAUNCH)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF091D17),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Button(
                        onClick = onContinueToApp,
                        modifier = Modifier.testTag("btn_continue_app_open_ad")
                    ) {
                        Text("Continue to App →")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF12382D))
                        .padding(22.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBBF24))
                            Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFF38BDF8))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "App Open Test Ad • Launch #$launchNumber",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Triggered 2.0 seconds after the 6th app open (after 5 opens completed)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD1FAE5)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Test Unit ID: ${AdMobTestUnitIds.APP_OPEN_ID}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFDE68A)
                        )
                    }
                }
            }
        }
    }
}
