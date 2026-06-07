package md.customs.calculator.presentation.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.name) },
                                    onClick = {
                                        LanguageManager.currentLanguage = lang
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
                            imageVector = Icons.Default.List,
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
            
            // Welcome Section or Ticker space
            Spacer(modifier = Modifier.height(4.dp))

            // Card 1: Informații Produs (Product Info Section)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "section_product"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    OutlinedTextField(
                        value = uiState.productName,
                        onValueChange = viewModel::updateProductName,
                        label = { Text(AppStrings.get(LanguageManager.currentLanguage, "product_name")) },
                        leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

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
                            leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            shape = RoundedCornerShape(12.dp),
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
                }
            }

            // Card 2: Valoare & Transport (Financials & Logistics)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "section_financials"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Side-by-side Valoare Colet & Valută
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.parcelValue,
                            onValueChange = viewModel::updateParcelValue,
                            label = { Text(AppStrings.get(LanguageManager.currentLanguage, "parcel_value")) },
                            leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1.4f),
                            singleLine = true
                        )

                        var currencyExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = currencyExpanded,
                            onExpandedChange = { currencyExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = uiState.selectedCurrency,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(AppStrings.get(LanguageManager.currentLanguage, "currency")) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                                shape = RoundedCornerShape(12.dp),
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
                    }

                    OutlinedTextField(
                        value = uiState.shippingCost,
                        onValueChange = viewModel::updateShippingCost,
                        label = { Text(AppStrings.get(LanguageManager.currentLanguage, "shipping_cost")) },
                        leadingIcon = { Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        suffix = { Text(uiState.selectedCurrency) }
                    )

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
                            leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courierExpanded) },
                            shape = RoundedCornerShape(12.dp),
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

                    OutlinedTextField(
                        value = uiState.trackerId,
                        onValueChange = viewModel::updateTrackerId,
                        label = { Text(AppStrings.get(LanguageManager.currentLanguage, "tracker_id")) },
                        leadingIcon = { Icon(Icons.Default.QrCode, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Card 3: Cadrul Legislativ (Legislation Details)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val uriHandler = LocalUriHandler.current
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = AppStrings.get(LanguageManager.currentLanguage, "july_2026_rules"),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Switch(
                            checked = uiState.isJuly2026LawEnabled,
                            onCheckedChange = viewModel::toggleJuly2026Law,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                    
                    Text(
                        text = AppStrings.get(LanguageManager.currentLanguage, "read_details") + " 🔗",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clickable {
                                uriHandler.openUri("https://moldova1.md/p/75240/noi-reguli-pentru-cumparaturile-online-coletele-taxate-cu-tva-de-20--incepand-cu-1-octombrie")
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
            
            // Error Message
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
