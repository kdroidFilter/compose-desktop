package utils

import data.manager.PreferencesManager
import data.model.Language
import org.w3c.dom.Element
import org.w3c.dom.Node
import utils.Localization.localizedStrings
import java.io.FileNotFoundException
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

object Localization {
    private val preferenceRepository = PreferencesManager
    private var _currentLocale = if (preferenceRepository.isLanguageSet()) preferenceRepository.getCurrentLanguage()!! else Locale.getDefault().language


    var localizedStrings: Map<String, String> = mapOf()

    fun availableLanguages() = listOf(
        Language("en", "English"),
        Language("he", "עברית", true),
        Language("fr", "Français"),
    )

    fun isCurrentLanguageRtl(): Boolean {
        return availableLanguages().find { it.code == _currentLocale }?.isRtl ?: false
    }

    fun setLanguage(languageCode: String) {
        _currentLocale = languageCode
        preferenceRepository.setLanguage(languageCode)
        updateLocalizedStrings()
    }

    fun currentLanguageCode() = _currentLocale

    fun currentLanguage() = availableLanguages().find { it.code == _currentLocale }

    private fun updateLocalizedStrings() {
        try {
            localizedStrings = loadLocalizedStrings(_currentLocale)
        } catch (e: Exception) {
            e.printStackTrace()
            _currentLocale = "en"
            localizedStrings = loadLocalizedStrings(_currentLocale)
        }
    }


    init {
        updateLocalizedStrings()
    }
}

fun loadLocalizedStrings(locale: String): Map<String, String> {
    val localizedStrings = mutableMapOf<String, String>()
    val inputStream =
        Localization::class.java.classLoader.getResourceAsStream("strings/strings_$locale.xml")

    inputStream?.let {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(it)

        // Gestion des chaînes simples
        val stringList = doc.getElementsByTagName("string")
        for (i in 0 until stringList.length) {
            val node = stringList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val name = element.getAttribute("name")
                val value = element.textContent
                localizedStrings[name] = value
            }
        }

        // Gestion des pluriels
        val pluralsList = doc.getElementsByTagName("plurals")
        for (i in 0 until pluralsList.length) {
            val node = pluralsList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val name = element.getAttribute("name")
                val quantities = element.getElementsByTagName("item")
                for (j in 0 until quantities.length) {
                    val quantityElement = quantities.item(j) as Element
                    val quantityName = quantityElement.getAttribute("quantity")
                    val value = quantityElement.textContent
                    localizedStrings["$name:$quantityName"] = value
                }
            }
        }

    } ?: throw FileNotFoundException("File strings/strings_$locale.xml not found")

    return localizedStrings
}

fun stringResource(key: String, vararg formatArgs: Any): String {
    val currentStrings = localizedStrings
    var formatString = currentStrings.getOrDefault(key, key)

    // Remplacer les \' par des '
    formatString = formatString.replace("\\'", "'")

    return String.format(formatString, *formatArgs)
}

fun pluralResource(key: String, quantity: Int, vararg formatArgs: Any): String {
    val currentStrings = localizedStrings
    val formatKey = when (quantity) {
        1 -> "$key:one"
        else -> "$key:other"
    }

    var formatString = currentStrings.getOrDefault(formatKey, key)
    // Remplacer les \' par des '
    formatString = formatString.replace("\\'", "'")

    return String.format(formatString, *formatArgs)
}
