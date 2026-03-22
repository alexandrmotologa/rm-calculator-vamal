package md.customs.calculator.presentation.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage { RO, EN, RU }

object LanguageManager {
    var currentLanguage by mutableStateOf(AppLanguage.RO)
}

object AppStrings {
    private val ro = mapOf(
        "app_title" to "Calculator Vamal",
        "product_name" to "Nume Produs (opțional)",
        "parcel_value" to "Valoare Colet",
        "shipping_cost" to "Cost Livrare",
        "currency" to "Valuta",
        "delivery_company" to "Companie Livrare (opțional)",
        "tracker_id" to "Tracker ID (opțional)",
        "category" to "Categoria Produsului",
        "reset" to "Resetează",
        "calculate" to "Calculează",
        "delete" to "Șterge",
        "save_history" to "Salvează în Istoric",
        "july_2026_rules" to "Aplică noile reguli vamale (Iulie 2026)",
        "law_description" to "Legea nr. Tax Code modificat — intră în vigoare din 1 iulie 2026",
        "read_details" to "Citește detalii despre lege →",
        "error_parcel_value" to "Valoarea coletului trebuie să fie mai mare ca 0.",
        "scutire_title" to "Scutire aplicată",
        "total_payment" to "Total de plată:",
        "applied_july_2026" to "Aplicat legea Iulie 2026",
        "tracker" to "Tracker",
        "follow" to "Urmărește",
        "result_title" to "Rezultat Calcul",
        "base_calc" to "Baza de calcul (MDL)",
        "duty_tax" to "Taxă Vamală",
        "vat_tax" to "TVA (20%)",
        "proc_fee" to "Taxă proceduri vamale",
        "read_law_detailed" to "Citește legea detaliat",
        "history_title" to "Istoric Calcule",
        "back" to "Înapoi",
        "no_saved_calculations" to "Nu există calcule salvate.",
        "product_label" to "Produs",
        "category_label" to "Categorie",
        "delivery_label" to "Livrare",
        "cat_phones" to "Telefoane Mobile (0%)",
        "cat_laptops" to "Laptopuri, PC (0%)",
        "cat_auto" to "Piese auto (10%)",
        "cat_shoes" to "Încălțăminte (10%)",
        "cat_clothes" to "Haine (15%)",
        "cat_cosmetics" to "Cosmetice (15%)",
        "cat_toys" to "Jucării (0%)",
        "cat_supplements" to "Suplimente alimentare (10%)",
        "cat_appliances" to "Electrocasnice (15%)",
        "cat_other" to "Altele (10%)"
    )

    private val en = mapOf(
        "app_title" to "Customs Calculator",
        "product_name" to "Product Name (optional)",
        "parcel_value" to "Parcel Value",
        "shipping_cost" to "Shipping Cost",
        "currency" to "Currency",
        "delivery_company" to "Delivery Company (optional)",
        "tracker_id" to "Tracker ID (optional)",
        "category" to "Product Category",
        "reset" to "Reset",
        "calculate" to "Calculate",
        "delete" to "Delete",
        "save_history" to "Save to History",
        "july_2026_rules" to "Apply new customs rules (July 2026)",
        "law_description" to "Law No. Tax Code modified — effective July 1, 2026",
        "read_details" to "Read details about the law →",
        "error_parcel_value" to "Parcel value must be greater than 0.",
        "scutire_title" to "Exemption applied",
        "total_payment" to "Total payable:",
        "applied_july_2026" to "Applied July 2026 law",
        "tracker" to "Tracker",
        "follow" to "Track",
        "result_title" to "Calculation Result",
        "base_calc" to "Calculation Base (MDL)",
        "duty_tax" to "Customs Duty",
        "vat_tax" to "VAT (20%)",
        "proc_fee" to "Customs Procedure Fee",
        "read_law_detailed" to "Read the law in detail",
        "history_title" to "Calculation History",
        "back" to "Back",
        "no_saved_calculations" to "No saved calculations.",
        "product_label" to "Product",
        "category_label" to "Category",
        "delivery_label" to "Delivery",
        "cat_phones" to "Mobile Phones (0%)",
        "cat_laptops" to "Laptops, PC (0%)",
        "cat_auto" to "Auto Parts (10%)",
        "cat_shoes" to "Shoes (10%)",
        "cat_clothes" to "Clothes (15%)",
        "cat_cosmetics" to "Cosmetics (15%)",
        "cat_toys" to "Toys (0%)",
        "cat_supplements" to "Supplements (10%)",
        "cat_appliances" to "Appliances (15%)",
        "cat_other" to "Other (10%)"
    )

    private val ru = mapOf(
        "app_title" to "Таможенный Калькулятор",
        "product_name" to "Название товара (опционально)",
        "parcel_value" to "Стоимость посылки",
        "shipping_cost" to "Стоимость доставки",
        "currency" to "Валюта",
        "delivery_company" to "Служба доставки (опционально)",
        "tracker_id" to "Трекер ID (опционально)",
        "category" to "Категория товара",
        "reset" to "Сбросить",
        "calculate" to "Рассчитать",
        "delete" to "Удалить",
        "save_history" to "Сохранить в историю",
        "july_2026_rules" to "Применить новые правила (Июль 2026)",
        "law_description" to "Закон № Изменен Налоговый кодекс — вступает в силу с 1 июля 2026",
        "read_details" to "Подробнее о законе →",
        "error_parcel_value" to "Стоимость посылки должна быть больше 0.",
        "scutire_title" to "Освобождение применено",
        "total_payment" to "Итого к оплате:",
        "applied_july_2026" to "Применен закон Июль 2026",
        "tracker" to "Трекер",
        "follow" to "Отследить",
        "result_title" to "Результат расчета",
        "base_calc" to "База расчета (MDL)",
        "duty_tax" to "Таможенная пошлина",
        "vat_tax" to "НДС (20%)",
        "proc_fee" to "Таможенная пошлина за процедуры",
        "read_law_detailed" to "Подробнее о законе",
        "history_title" to "История Расчетов",
        "back" to "Назад",
        "no_saved_calculations" to "Нет сохраненных расчетов.",
        "product_label" to "Продукт",
        "category_label" to "Категория",
        "delivery_label" to "Доставка",
        "cat_phones" to "Мобильные телефоны (0%)",
        "cat_laptops" to "Ноутбуки, ПК (0%)",
        "cat_auto" to "Автозапчасти (10%)",
        "cat_shoes" to "Обувь (10%)",
        "cat_clothes" to "Одежда (15%)",
        "cat_cosmetics" to "Косметика (15%)",
        "cat_toys" to "Игрушки (0%)",
        "cat_supplements" to "Пищевые добавки (10%)",
        "cat_appliances" to "Бытовая техника (15%)",
        "cat_other" to "Другое (10%)"
    )

    private val translations = mapOf(
        AppLanguage.RO to ro,
        AppLanguage.EN to en,
        AppLanguage.RU to ru
    )

    fun get(lang: AppLanguage, key: String): String {
        return translations[lang]?.get(key) ?: key
    }
}
