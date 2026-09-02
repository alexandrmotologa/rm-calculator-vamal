package md.customs.calculator.presentation.calculator.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import md.customs.calculator.domain.model.Currency
import md.customs.calculator.presentation.util.AppLanguage
import md.customs.calculator.presentation.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialsSectionCard(
    parcelValue: String,
    onParcelValueChange: (String) -> Unit,
    shippingCost: String,
    onShippingCostChange: (String) -> Unit,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    deliveryCompany: String,
    onDeliveryCompanySelected: (String) -> Unit,
    trackerId: String,
    onTrackerIdChange: (String) -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val courierCompanies = listOf(
        "Poșta Moldovei",
        "Nova Poshta",
        "DHL",
        "FedEx",
        "Fan Courier",
        "Pesoto",
        "Altele"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = AppStrings.get(currentLanguage, "section_financials"),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            // Side-by-side Valoare Colet & Valută
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = parcelValue,
                    onValueChange = onParcelValueChange,
                    label = { Text(AppStrings.get(currentLanguage, "parcel_value")) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
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
                        value = selectedCurrency.code,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(AppStrings.get(currentLanguage, "currency")) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = currencyExpanded,
                        onDismissRequest = { currencyExpanded = false }
                    ) {
                        Currency.entries.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency.code) },
                                onClick = {
                                    onCurrencySelected(currency)
                                    currencyExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = shippingCost,
                onValueChange = onShippingCostChange,
                label = { Text(AppStrings.get(currentLanguage, "shipping_cost")) },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                suffix = { Text(selectedCurrency.code) }
            )

            var courierExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = courierExpanded,
                onExpandedChange = { courierExpanded = it }
            ) {
                OutlinedTextField(
                    value = deliveryCompany,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(AppStrings.get(currentLanguage, "delivery_company")) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Business,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courierExpanded) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = courierExpanded,
                    onDismissRequest = { courierExpanded = false }
                ) {
                    courierCompanies.forEach { company ->
                        DropdownMenuItem(
                            text = { Text(company) },
                            onClick = {
                                onDeliveryCompanySelected(company)
                                courierExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = trackerId,
                onValueChange = onTrackerIdChange,
                label = { Text(AppStrings.get(currentLanguage, "tracker_id")) },
                leadingIcon = {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
