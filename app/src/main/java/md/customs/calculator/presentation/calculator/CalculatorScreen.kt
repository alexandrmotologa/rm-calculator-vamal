package md.customs.calculator.presentation.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import md.customs.calculator.presentation.util.AppLanguage
import md.customs.calculator.presentation.util.AppStrings
import md.customs.calculator.presentation.util.LanguageManager
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val currencies = listOf("MDL", "EUR", "USD", "RON", "GBP")
    val categories = listOf(
        "cat_phones" to 0.0,
        "cat_laptops" to 0.0,
        "cat_auto" to 0.10,
        "cat_shoes" to 0.10,
        "cat_clothes" to 0.15,
        "cat_cosmetics" to 0.15,
        "cat_toys" to 0.0,
        "cat_supplements" to 0.10,
        "cat_appliances" to 0.15,
        "cat_other" to 0.10
    )
    
    val courierCompanies = listOf(
        "Poșta Moldovei",
        "Nova Poshta",
        "DHL",
        "FedEx",
        "Fan Courier",
        "Pesoto",
        "Altele"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.get(LanguageManager.currentLanguage, "app_title")) },
                actions = {
                    var langExpanded by remember { mutableStateOf(false) }
                    
                    Box {
                        TextButton(onClick = { langExpanded = true }) {
                            Text(
                                text = LanguageManager.currentLanguage.name,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        DropdownMenu(
                            expanded = langExpanded,
                            onDismissRequest = { langExpanded = false }
                        ) {
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.name) },
                                    onClick = {
                                        LanguageManager.currentLanguage = lang
                                        langExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    IconButton(onClick = onNavigateToHistory) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Deschide Istoric"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. "Nume Produs"
            OutlinedTextField(
                value = uiState.productName,
                onValueChange = viewModel::updateProductName,
                label = { Text(AppStrings.get(LanguageManager.currentLanguage, "product_name")) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 2. Currencies Dropdown
            var currencyExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(AppStrings.get(LanguageManager.currentLanguage, "currency")) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = {
                                viewModel.updateCurrency(currency)
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }

            // 3. "Valoare Colet"
            OutlinedTextField(
                value = uiState.parcelValue,
                onValueChange = viewModel::updateParcelValue,
                label = { Text(AppStrings.get(LanguageManager.currentLanguage, "parcel_value")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text(uiState.selectedCurrency) }
            )

            // 4. "Cost Livrare"
            OutlinedTextField(
                value = uiState.shippingCost,
                onValueChange = viewModel::updateShippingCost,
                label = { Text(AppStrings.get(LanguageManager.currentLanguage, "shipping_cost")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text(uiState.selectedCurrency) }
            )

            // 5. "Companie Livrare" Dropdown
            var courierExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = courierExpanded,
                onExpandedChange = { courierExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.deliveryCompany,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(AppStrings.get(LanguageManager.currentLanguage, "delivery_company")) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courierExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = courierExpanded,
                    onDismissRequest = { courierExpanded = false }
                ) {
                    courierCompanies.forEach { company ->
                        DropdownMenuItem(
                            text = { Text(company) },
                            onClick = {
                                viewModel.updateDeliveryCompany(company)
                                courierExpanded = false
                            }
                        )
                    }
                }
            }

            // 6. "Tracker ID"
            OutlinedTextField(
                value = uiState.trackerId,
                onValueChange = viewModel::updateTrackerId,
                label = { Text(AppStrings.get(LanguageManager.currentLanguage, "tracker_id")) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 7. Categories Dropdown
            var categoryExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = AppStrings.get(LanguageManager.currentLanguage, uiState.selectedCategory),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(AppStrings.get(LanguageManager.currentLanguage, "category")) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { (catKey, pct) ->
                        DropdownMenuItem(
                            text = { Text(AppStrings.get(LanguageManager.currentLanguage, catKey)) },
                            onClick = {
                                viewModel.updateCategory(catKey, pct)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            // July 2026 Switch
            val uriHandler = LocalUriHandler.current
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "july_2026_rules"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = uiState.isJuly2026LawEnabled,
                        onCheckedChange = viewModel::toggleJuly2026Law
                    )
                }
                Text(
                    text = AppStrings.get(LanguageManager.currentLanguage, "law_description"),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = AppStrings.get(LanguageManager.currentLanguage, "read_details"),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://customs.gov.md/ro/articles/bunuri-expediate-prin-intermediul-trimiterilor-postale-internationale")
                    }
                )
            }
            
            // Error Message
            if (uiState.errorMessage != null) {
                Text(
                    text = AppStrings.get(LanguageManager.currentLanguage, uiState.errorMessage!!),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val hasInputs = uiState.parcelValue.isNotBlank() || 
                            uiState.shippingCost.isNotBlank() || 
                            uiState.deliveryCompany.isNotBlank() || 
                            uiState.trackerId.isNotBlank() || 
                            uiState.productName.isNotBlank()

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (hasInputs) {
                    IconButton(
                        onClick = viewModel::resetInputs,
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = AppStrings.get(LanguageManager.currentLanguage, "reset")
                        )
                    }
                }

                Button(
                    onClick = viewModel::calculateTaxes,
                    modifier = Modifier.weight(1f).height(56.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(AppStrings.get(LanguageManager.currentLanguage, "calculate"), style = MaterialTheme.typography.labelLarge)
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
