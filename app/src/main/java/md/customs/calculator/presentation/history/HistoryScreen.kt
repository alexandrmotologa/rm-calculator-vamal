package md.customs.calculator.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import md.customs.calculator.data.local.entity.CalculationHistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Link
import md.customs.calculator.presentation.util.AppStrings
import md.customs.calculator.presentation.util.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateBack: () -> Unit
) {
    val historyList by viewModel.historyEntries.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "history_title"),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AppStrings.get(LanguageManager.currentLanguage, "back")
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = AppStrings.get(LanguageManager.currentLanguage, "no_saved_calculations"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(historyList, key = { it.id }) { entry ->
                    HistoryItemCard(
                        entry = entry,
                        onDelete = { viewModel.deleteCalculation(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(entry: CalculationHistoryEntity, onDelete: (CalculationHistoryEntity) -> Unit) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(entry.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${entry.parcelValue} ${entry.currency}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Redesigned delete button container
                IconButton(
                    onClick = { onDelete(entry) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = AppStrings.get(LanguageManager.currentLanguage, "delete"),
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 4.dp))

            // Details list with custom leading icons
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (!entry.productName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Inventory, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Text(
                            text = "${AppStrings.get(LanguageManager.currentLanguage, "product_label")}: ${entry.productName}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Label, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Text(
                        text = "${AppStrings.get(LanguageManager.currentLanguage, "category_label")}: ${AppStrings.get(LanguageManager.currentLanguage, getCategoryKey(entry.category))}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!entry.deliveryCompany.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Text(
                            text = "${AppStrings.get(LanguageManager.currentLanguage, "delivery_label")}: ${entry.deliveryCompany}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (!entry.trackerId.isNullOrBlank()) {
                    val uriHandler = LocalUriHandler.current
                    val company = entry.deliveryCompany ?: ""
                    val url = TrackingResolver.resolveUrl(company, entry.trackerId)
                    val isClickable = url.isNotBlank()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isClickable) Icons.Default.Link else Icons.Default.Label,
                            contentDescription = null,
                            tint = if (isClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${AppStrings.get(LanguageManager.currentLanguage, "tracker")}: ${entry.trackerId} ${if (isClickable) "(${AppStrings.get(LanguageManager.currentLanguage, "follow")} 🔗)" else ""}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isClickable) FontWeight.Bold else FontWeight.Normal,
                                textDecoration = if (isClickable) TextDecoration.Underline else TextDecoration.None
                            ),
                            color = if (isClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = if (isClickable) Modifier.clickable {
                                try {
                                    uriHandler.openUri(url)
                                } catch (e: Exception) {
                                    // Ignore failed links
                                }
                            } else Modifier
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 4.dp))

            // Total Cost display & Law Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (entry.isJuly2026LawApplied) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = AppStrings.get(LanguageManager.currentLanguage, "applied_july_2026"),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = AppStrings.get(LanguageManager.currentLanguage, "current_law"),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "total_payment"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format(Locale.US, "%.2f MDL", entry.totalCost),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

object TrackingResolver {
    fun resolveUrl(company: String, trackerId: String): String {
        val normalized = company.trim().lowercase()
        return when {
            normalized.contains("dhl") -> "https://www.dhl.com/en/express/tracking.html?AWB=$trackerId"
            normalized.contains("fedex") -> "https://www.fedex.com/apps/fedextrack/?action=track&trackingnumbers=$trackerId"
            normalized.contains("posta") -> "https://posta.md/ro/tracking"
            normalized.contains("nova poshta") -> "https://novapost.com/ro-ro/tracking"
            normalized.contains("fan courier") -> "https://www.fancourier.md/"
            normalized.contains("pesoto") -> "https://pesoto.md/"
            normalized.contains("altele") -> ""
            else -> ""
        }
    }
}

fun getCategoryKey(category: String): String {
    return when (category) {
        "Telefoane Mobile (0%)" -> "cat_phones"
        "Laptopuri, PC (0%)" -> "cat_laptops"
        "Piese auto (10%)" -> "cat_auto"
        "Încălțăminte (10%)" -> "cat_shoes"
        "Haine (15%)" -> "cat_clothes"
        "Cosmetice (15%)" -> "cat_cosmetics"
        "Jucării (0%)" -> "cat_toys"
        "Suplimente alimentare (10%)" -> "cat_supplements"
        "Electrocasnice (15%)" -> "cat_appliances"
        "Altele (10%)" -> "cat_other"
        else -> category
    }
}
