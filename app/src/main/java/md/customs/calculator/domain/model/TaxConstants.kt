package md.customs.calculator.domain.model

/**
 * Constants and thresholds related to Customs Duty, VAT, and Processing Fees in Republic of Moldova.
 */
object TaxConstants {
    const val EXEMPTION_THRESHOLD_EUR = 150.0
    const val STANDARD_VAT_RATE = 0.20 // 20%
    const val STANDARD_PROCEDURE_FEE_MDL = 50.0 // 50 MDL standard customs procedure
    const val REDUCED_PROCEDURE_FEE_MDL = 12.0 // 12 MDL fixed fee under the new fiscal policy for parcels <= 150 EUR

    const val DEFAULT_EUR_RATE_FALLBACK = 20.0
    const val DEFAULT_MDL_RATE = 1.0

    const val LEGISLATION_URL = "https://moldova1.md/p/75240/noi-reguli-pentru-cumparaturile-online-coletele-taxate-cu-tva-de-20--incepand-cu-1-octombrie"
    const val CUSTOMS_INFO_URL = "https://customs.gov.md/ro/articles/trimiterile-postale-internationale"
    const val LEGIS_MD_URL = "https://www.legis.md/cautare/getResults?doc_id=137957&lang=ro"
    const val BNM_URL = "https://www.bnm.md/"
}
