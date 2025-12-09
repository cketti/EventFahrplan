package nerd.tuxmobil.fahrplan.congress.search

import com.google.common.truth.Truth.assertThat
import nerd.tuxmobil.fahrplan.congress.models.Session
import nerd.tuxmobil.fahrplan.congress.search.filters.HasAlarmSearchFilter
import nerd.tuxmobil.fahrplan.congress.search.filters.IsFavoriteSearchFilter
import nerd.tuxmobil.fahrplan.congress.search.filters.NotRecordedSearchFilter
import nerd.tuxmobil.fahrplan.congress.search.filters.WithinSpeakerNamesSearchFilter
import org.junit.jupiter.api.Test

class SearchQueryFilterTest {

    private val filter = SearchQueryFilter()

    @Test
    fun `filterAll returns empty list when sessions is empty and query is empty`() {
        val result = filter.filterAll(emptyList(), "")
        assertThat(result).isEqualTo(emptyList<String>())
    }

    @Test
    fun `filterAll returns empty list when sessions is empty and query is not empty`() {
        val result = filter.filterAll(emptyList(), "test")
        assertThat(result).isEqualTo(emptyList<String>())
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches sessionId`() {
        val result = filter.filterAll(listOf(Session("1056")), "56")
        assertThat(result).isEqualTo(listOf(Session("1056")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches title`() {
        val result = filter.filterAll(listOf(Session("1", title = "some title")), "title")
        assertThat(result).isEqualTo(listOf(Session("1", title = "some title")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches subtitle`() {
        val result = filter.filterAll(listOf(Session("1", subtitle = "some subtitle")), "subtitle")
        assertThat(result).isEqualTo(listOf(Session("1", subtitle = "some subtitle")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches abstractt`() {
        val result = filter.filterAll(listOf(Session("1", abstractt = "some abstract")), "abstract")
        assertThat(result).isEqualTo(listOf(Session("1", abstractt = "some abstract")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches description`() {
        val result = filter.filterAll(listOf(Session("1", description = "some description")), "description")
        assertThat(result).isEqualTo(listOf(Session("1", description = "some description")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches track`() {
        val result = filter.filterAll(listOf(Session("1", track = "some track")), "track")
        assertThat(result).isEqualTo(listOf(Session("1", track = "some track")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches roomName`() {
        val result = filter.filterAll(listOf(Session("1", roomName = "some room name")), "room name")
        assertThat(result).isEqualTo(listOf(Session("1", roomName = "some room name")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches links`() {
        val result = filter.filterAll(listOf(Session("1", links = "https:/example.com")), "example")
        assertThat(result).isEqualTo(listOf(Session("1", links = "https:/example.com")))
    }

    @Test
    fun `filterAll returns list with session when query at least partially matches speakers`() {
        val result = filter.filterAll(listOf(Session("1", speakers = listOf("Jane Doe", "John Doe"))), "Jane")
        assertThat(result).isEqualTo(listOf(Session("1", speakers = listOf("Jane Doe", "John Doe"))))
    }

    @Test
    fun `IsFavoriteSearchFilter only returns starred sessions`() {
        val session1 = Session("1", title = "Session 1", isHighlight = false)
        val session2 = Session("2", title = "Session 2", isHighlight = true)
        val session3 = Session("3", title = "no match", isHighlight = true)
        val sessions = listOf(session1, session2, session3)
        val query = "session"
        val filters = listOf(IsFavoriteSearchFilter())

        val result = filter.filterAll(sessions, query, filters)

        assertThat(result).containsExactly(session2)
    }

    @Test
    fun `HasAlarmSearchFilter only returns sessions with alarm`() {
        val session1 = Session("1", title = "Session 1", hasAlarm = false)
        val session2 = Session("2", title = "Session 2", hasAlarm = true)
        val session3 = Session("3", title = "no match", hasAlarm = true)
        val sessions = listOf(session1, session2, session3)
        val query = "session"
        val filters = listOf(HasAlarmSearchFilter())

        val result = filter.filterAll(sessions, query, filters)

        assertThat(result).containsExactly(session2)
    }

    @Test
    fun `NotRecordedSearchFilter only returns sessions with alarm`() {
        val session1 = Session("1", title = "Session 1", recordingOptOut = false)
        val session2 = Session("2", title = "Session 2", recordingOptOut = true)
        val session3 = Session("3", title = "no match", recordingOptOut = true)
        val sessions = listOf(session1, session2, session3)
        val query = "session"
        val filters = listOf(NotRecordedSearchFilter())

        val result = filter.filterAll(sessions, query, filters)

        assertThat(result).containsExactly(session2)
    }

    @Test
    fun `WithinSpeakerNamesSearchFilter only returns sessions where at least one speaker name matches the query`() {
        val session1 = Session("1", title = "Query 1", speakers = listOf("Jane Doe"))
        val session2 = Session("2", title = "Query 2", speakers = listOf("Jane Doe", "Peter Query"))
        val session3 = Session("3", title = "no title match", speakers = listOf("QUERY", "Jane Doe"))
        val sessions = listOf(session1, session2, session3)
        val query = "query"
        val filters = listOf(WithinSpeakerNamesSearchFilter())

        val result = filter.filterAll(sessions, query, filters)

        assertThat(result).containsExactly(session2, session3)
    }

    @Test
    fun `all provided SearchFilters must match`() {
        val session1 = Session("1", title = "Session 1", isHighlight = true, hasAlarm = false)
        val session2 = Session("2", title = "Session 2", isHighlight = true, hasAlarm = true)
        val session3 = Session("3", title = "Session 3", isHighlight = false, hasAlarm = true)
        val session4 = Session("4", title = "Session 4", isHighlight = false, hasAlarm = false)
        val sessions = listOf(session1, session2, session3, session4)
        val query = "session"
        val filters = listOf(IsFavoriteSearchFilter(), HasAlarmSearchFilter())

        val result = filter.filterAll(sessions, query, filters)

        assertThat(result).containsExactly(session2)
    }

}
