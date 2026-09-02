package md.customs.calculator.presentation.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import md.customs.calculator.presentation.calculator.components.DisclaimerCard
import md.customs.calculator.presentation.calculator.components.FinancialsSectionCard
import md.customs.calculator.presentation.calculator.components.LegislationCard
import md.customs.calculator.presentation.calculator.components.ProductSectionCard
import md.customs.calculator.presentation.util.AppLanguage
import md.customs.calculator.presentation.util.AppStrings
import md.customs.calculator.presentation.util.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "app_title"),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                },
                actions = {
                    var langExpanded by remember { mutableStateOf(false) }

                    Box {
                        TextButton(
                            onClick = { langExpanded = true },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = LanguageManager.currentLanguage.name,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = langExpanded,
                            onDismissRequest = { langExpanded = false }
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.name) },
                                    onClick = {
                                        viewModel.updateLanguage(lang)
                                        langExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onNavigateToHistory,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Deschide Istoric"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Card 1: Product Information
            ProductSectionCard(
                productName = uiState.productName,
                onProductNameChange = viewModel::updateProductName,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::updateCategory,
                currentLanguage = LanguageManager.currentLanguage
            )

            // Card 2: Financials & Logistics
            FinancialsSectionCard(
                parcelValue = uiState.parcelValue,
                onParcelValueChange = viewModel::updateParcelValue,
                shippingCost = uiState.shippingCost,
                onShippingCostChange = viewModel::updateShippingCost,
                selectedCurrency = uiState.selectedCurrency,
                onCurrencySelected = viewModel::updateCurrency,
                deliveryCompany = uiState.deliveryCompany,
                onDeliveryCompanySelected = viewModel::updateDeliveryCompany,
                trackerId = uiState.trackerId,
                onTrackerIdChange = viewModel::updateTrackerId,
                currentLanguage = LanguageManager.currentLanguage
            )

            // Card 3: Legislation Details
            LegislationCard(
                isJuly2026LawEnabled = uiState.isJuly2026LawEnabled,
                onToggleJuly2026Law = viewModel::toggleJuly2026Law,
                currentLanguage = LanguageManager.currentLanguage
            )

            // Card 4: Disclaimer & Government Sources
            DisclaimerCard(
                currentLanguage = LanguageManager.currentLanguage
            )

            // Error Banner
            if (uiState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = AppStrings.get(LanguageManager.currentLanguage, uiState.errorMessage!!),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            val hasInputs = uiState.parcelValue.isNotBlank() ||
                    uiState.shippingCost.isNotBlank() ||
                    uiState.deliveryCompany.isNotBlank() ||
                    uiState.trackerId.isNotBlank() ||
                    uiState.productName.isNotBlank()

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (hasInputs) {
                    IconButton(
                        onClick = viewModel::resetInputs,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.errorContainer),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = AppStrings.get(LanguageManager.currentLanguage, "reset")
                        )
                    }
                }

                val primaryColor = MaterialTheme.colorScheme.primary
                val secondaryColor = MaterialTheme.colorScheme.secondary

                Button(
                    onClick = viewModel::calculateTaxes,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(primaryColor, secondaryColor)
                            )
                        )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = AppStrings.get(LanguageManager.currentLanguage, "calculate"),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }

    if (uiState.calculationResult != null) {
        ResultBottomSheet(
            result = uiState.calculationResult!!,
            currentLanguage = LanguageManager.currentLanguage,
            onDismiss = viewModel::clearResult,
            onSaveToHistory = {
                viewModel.saveCalculationToHistory()
                viewModel.resetInputs()
                onNavigateToHistory()
            }
        )
    }
}
