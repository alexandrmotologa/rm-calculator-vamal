package md.customs.calculator.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import md.customs.calculator.data.local.entity.CalculationHistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
            TopAppBar(
                title = { Text(AppStrings.get(LanguageManager.currentLanguage, "history_title")) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AppStrings.get(LanguageManager.currentLanguage, "back")
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onDelete(entry) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Șterge",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (!entry.productName.isNullOrBlank()) {
                Text(
                    text = "${AppStrings.get(LanguageManager.currentLanguage, "product_label")}: ${entry.productName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "${AppStrings.get(LanguageManager.currentLanguage, "category_label")}: ${AppStrings.get(LanguageManager.currentLanguage, getCategoryKey(entry.category))}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!entry.deliveryCompany.isNullOrBlank()) {
                Text(
                    text = "${AppStrings.get(LanguageManager.currentLanguage, "delivery_label")}: ${entry.deliveryCompany}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!entry.trackerId.isNullOrBlank()) {
                val uriHandler = LocalUriHandler.current
                val company = entry.deliveryCompany ?: ""
                val url = TrackingResolver.resolveUrl(company, entry.trackerId)
                val isClickable = url.isNotBlank()

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
                            // Handler handles absolute URLs properly
                        }
                    } else Modifier
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = AppStrings.get(LanguageManager.currentLanguage, "total_payment"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = String.format(Locale.US, "%.2f MDL", entry.totalCost),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (entry.isJuly2026LawApplied) {
                Text(
                    text = "Aplicat legea Iulie 2026",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
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
            normalized.contains("altele") -> "" // Not clickable, just shows text
            else -> "" // Default to empty to avoid broken aggregators
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
