package br.com.brunoccbertolini.cocktailhelperapp.util

/**
 * Result of a data-layer operation. Carries a typed [DataError] (never a user-facing
 * string) so the presentation layer stays responsible for localizing messages.
 *
 * [Error.data] optionally carries a stale/cached value to keep showing while surfacing
 * the error (used by the offline-first list flow).
 */
sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error<out T>(val error: DataError, val data: T? = null) : Resource<T>
    data object Loading : Resource<Nothing>
}
