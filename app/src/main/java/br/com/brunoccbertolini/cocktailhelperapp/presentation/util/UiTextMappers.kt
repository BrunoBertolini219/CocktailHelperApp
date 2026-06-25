package br.com.brunoccbertolini.cocktailhelperapp.presentation.util

import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.util.DataError

/** Maps a typed [DataError] to localized [UiText]. Lives in the presentation layer only. */
fun DataError.toUiText(): UiText = when (this) {
    DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
    DataError.Network.TIMEOUT -> UiText.StringResource(R.string.error_timeout)
    DataError.Network.SERVER -> UiText.StringResource(R.string.error_server)
    DataError.Network.SERIALIZATION -> UiText.StringResource(R.string.error_serialization)
    DataError.Network.NOT_FOUND -> UiText.StringResource(R.string.error_not_found)
    DataError.Network.UNKNOWN -> UiText.StringResource(R.string.error_unknown)
}
