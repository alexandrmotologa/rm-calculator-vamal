package md.customs.calculator.data.remote.dto

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Root element of the BNM XML response representing the daily exchange rates.
 * Example XML from: https://www.bnm.md/ro/official_exchange_rates?get_xml=1
 */
@Serializable
@XmlSerialName("ValCurs")
data class BnmExchangeRateResponse(
    @XmlSerialName("Date", "", "")
    val date: String,
    
    @XmlSerialName("name", "", "")
    val name: String,
    
    @XmlElement(true)
    @XmlSerialName("Valute", "", "")
    val valutes: List<BnmValuteDto>
)

/**
 * Individual currency element.
 */
@Serializable
@XmlSerialName("Valute")
data class BnmValuteDto(
    @XmlSerialName("ID", "", "")
    val id: String,
    
    @XmlElement(true)
    @XmlSerialName("NumCode", "", "")
    val numCode: String,
    
    @XmlElement(true)
    @XmlSerialName("CharCode", "", "")
    val charCode: String,
    
    @XmlElement(true)
    @XmlSerialName("Nominal", "", "")
    val nominal: Int,
    
    @XmlElement(true)
    @XmlSerialName("Name", "", "")
    val name: String,
    
    @XmlElement(true)
    @XmlSerialName("Value", "", "")
    val value: String
)
