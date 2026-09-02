package md.customs.calculator.domain.model

/**
 * Domain model representing supported product categories and their standard customs duty rates.
 */
enum class ProductCategory(val key: String, val defaultDutyRate: Double) {
    PHONES("cat_phones", 0.0),
    LAPTOPS("cat_laptops", 0.0),
    AUTO_PARTS("cat_auto", 0.10),
    SHOES("cat_shoes", 0.10),
    CLOTHES("cat_clothes", 0.15),
    COSMETICS("cat_cosmetics", 0.15),
    TOYS("cat_toys", 0.0),
    SUPPLEMENTS("cat_supplements", 0.10),
    APPLIANCES("cat_appliances", 0.15),
    OTHER("cat_other", 0.10);

    companion object {
        fun fromKey(key: String): ProductCategory {
            return entries.find { it.key == key } ?: when (key) {
                "Telefoane Mobile (0%)" -> PHONES
                "Laptopuri, PC (0%)" -> LAPTOPS
                "Piese auto (10%)" -> AUTO_PARTS
                "Încălțăminte (10%)" -> SHOES
                "Haine (15%)" -> CLOTHES
                "Cosmetice (15%)" -> COSMETICS
                "Jucării (0%)" -> TOYS
                "Suplimente alimentare (10%)" -> SUPPLEMENTS
                "Electrocasnice (15%)" -> APPLIANCES
                "Altele (10%)" -> OTHER
                else -> OTHER
            }
        }
    }
}
