package br.com.brunoccbertolini.cocktailhelperapp.util

/**
 * Typed, layer-agnostic errors. The data layer returns these instead of hardcoded
 * English strings, so the presentation layer can map them to localized [UiText].
 */
sealed interface DataError {
    enum class Network : DataError {
        NO_INTERNET,
        TIMEOUT,
        SERVER,
        SERIALIZATION,
        NOT_FOUND,
        UNKNOWN
    }
}
