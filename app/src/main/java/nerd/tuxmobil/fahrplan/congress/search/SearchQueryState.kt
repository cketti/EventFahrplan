package nerd.tuxmobil.fahrplan.congress.search

import kotlinx.collections.immutable.ImmutableMap

data class SearchQueryState(
    val query: String,
    val filters: ImmutableMap<Int, Boolean>,
)
