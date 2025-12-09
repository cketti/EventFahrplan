package nerd.tuxmobil.fahrplan.congress.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nerd.tuxmobil.fahrplan.congress.repositories.AppRepository
import nerd.tuxmobil.fahrplan.congress.search.SearchEffect.NavigateBack
import nerd.tuxmobil.fahrplan.congress.search.SearchEffect.NavigateToSession
import nerd.tuxmobil.fahrplan.congress.search.SearchResultState.Loading
import nerd.tuxmobil.fahrplan.congress.search.SearchResultState.SearchHistory
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnBackIconClick
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnBackPress
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnSearchHistoryClear
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnSearchHistoryItemClick
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnSearchQueryChange
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnSearchQueryClear
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnSearchResultItemClick
import nerd.tuxmobil.fahrplan.congress.search.SearchViewEvent.OnSearchSubScreenBackPress
import nerd.tuxmobil.fahrplan.congress.search.filters.HasAlarmSearchFilter
import nerd.tuxmobil.fahrplan.congress.search.filters.IsFavoriteSearchFilter
import nerd.tuxmobil.fahrplan.congress.search.filters.NotRecordedSearchFilter
import nerd.tuxmobil.fahrplan.congress.search.filters.WithinSpeakerNamesSearchFilter

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: AppRepository,
    private val searchQueryFilter: SearchQueryFilter,
    private val searchHistoryManager: SearchHistoryManager,
    private val searchResultParameterFactory: SearchResultParameterFactory,
) : ViewModel() {

    private companion object {
        const val FINISH_TYPING_SEARCH_QUERY_DELAY = 1_000L

        private val SUPPORTED_SEARCH_FILTERS = listOf(
            IsFavoriteSearchFilter(),
            HasAlarmSearchFilter(),
            NotRecordedSearchFilter(),
            WithinSpeakerNamesSearchFilter(),
        )
    }

    private val mutableEffects = Channel<SearchEffect>()
    val effects = mutableEffects.receiveAsFlow()

    private var searchQuery by mutableStateOf("")

    private val useDeviceTimeZone: Boolean
        get() = repository.readUseDeviceTimeZoneEnabled()

    private val initialFilterState = SUPPORTED_SEARCH_FILTERS.associateWith { false }
    private val searchFilters = MutableStateFlow(initialFilterState)
    val uiState: StateFlow<SearchUiState> =
        combine(
            snapshotFlow { searchQuery },
            searchFilters,
            repository.sessions,
            searchHistoryManager.searchHistory,
        ) { query, filters, sessions, searchHistory ->
            val resultState = if (query.isEmpty()) {
                SearchHistory(searchHistory.toImmutableList())
            } else {
                val activeFilters = filters.filterValues { enabled -> enabled }.keys.toList() //FIXME: change SearchQueryFilter to use Set
                val matchingSessions = searchQueryFilter.filterAll(sessions, query, activeFilters)
                val searchResults = searchResultParameterFactory.createSearchResults(
                    matchingSessions,
                    useDeviceTimeZone,
                )
                SearchResultState.SearchResults(searchResults.toImmutableList())
            }

            SearchUiState(
                queryState = SearchQueryState(query, filters.mapKeys { 1 /*FIXME*/ }.toImmutableMap()),
                resultsState = resultState,
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = SearchUiState(
                queryState = SearchQueryState(
                    query = "",
                    filters = initialFilterState.mapKeys { 1 /* FIXME */ }.toImmutableMap(),
                ),
                resultsState = Loading,
            ),
            started = WhileSubscribed(5_000)
        )

    init {
        snapshotFlow { searchQuery }
            .debounce(FINISH_TYPING_SEARCH_QUERY_DELAY)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .onEach { searchHistoryManager.append(viewModelScope, it) }
            .launchIn(viewModelScope)
    }

    fun onViewEvent(viewEvent: SearchViewEvent) {
        when (viewEvent) {
            OnBackPress -> sendEffect(NavigateBack)
            OnBackIconClick -> searchQuery = ""
            OnSearchSubScreenBackPress -> searchQuery = ""
            is OnSearchResultItemClick -> sendEffect(NavigateToSession(viewEvent.sessionId))
            is OnSearchHistoryItemClick -> searchQuery = viewEvent.searchQuery
            OnSearchHistoryClear -> searchHistoryManager.clear(viewModelScope)
            is OnSearchQueryChange -> searchQuery = viewEvent.updatedQuery
            OnSearchQueryClear -> searchQuery = ""
        }
    }

    private fun sendEffect(effect: SearchEffect) {
        viewModelScope.launch {
            mutableEffects.send(effect)
        }
    }
}
