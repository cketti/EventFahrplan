package nerd.tuxmobil.fahrplan.congress.search

import kotlinx.collections.immutable.ImmutableList

sealed interface SearchResultState {
    data object Loading : SearchResultState
    data class SearchHistory(val searchTerms: ImmutableList<String>) : SearchResultState
    data class SearchResults(val parameters: ImmutableList<SearchResultParameter>) : SearchResultState
}
