package md.customs.calculator.presentation.util

/**
 * Utility for resolving courier tracking URLs based on company and tracking number.
 */
object TrackingResolver {
    fun resolveUrl(company: String, trackerId: String): String {
        val normalized = company.trim().lowercase()
        val trimmedId = trackerId.trim()
        if (trimmedId.isBlank()) return ""

        return when {
            normalized.contains("dhl") -> "https://www.dhl.com/en/express/tracking.html?AWB=$trimmedId"
            normalized.contains("fedex") -> "https://www.fedex.com/apps/fedextrack/?action=track&trackingnumbers=$trimmedId"
            normalized.contains("posta") -> "https://posta.md/ro/tracking"
            normalized.contains("nova poshta") || normalized.contains("novaposhta") -> "https://novapost.com/ro-ro/tracking"
            normalized.contains("fan courier") || normalized.contains("fancourier") -> "https://www.fancourier.md/"
            normalized.contains("pesoto") -> "https://pesoto.md/"
            normalized.contains("altele") -> ""
            else -> ""
        }
    }
}
