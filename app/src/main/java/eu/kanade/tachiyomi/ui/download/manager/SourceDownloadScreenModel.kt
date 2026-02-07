package eu.kanade.tachiyomi.ui.download.manager

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import eu.kanade.tachiyomi.data.download.DownloadCache
import eu.kanade.tachiyomi.data.download.DownloadManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.domain.manga.model.Manga
import tachiyomi.domain.manga.repository.MangaRepository
import tachiyomi.domain.source.service.SourceManager
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class SourceDownloadScreenModel(
    private val sourceId: Long,
    private val mangaRepository: MangaRepository = Injekt.get(),
    private val sourceManager: SourceManager = Injekt.get(),
    private val downloadManager: DownloadManager = Injekt.get(),
    private val downloadCache: DownloadCache = Injekt.get(),
) : ScreenModel {

    private val _state = MutableStateFlow<List<SourceDownloadMangaItem>>(emptyList())
    val state = _state.asStateFlow()

    val query = MutableStateFlow<String?>(null)

    init {
        screenModelScope.launch {
            combine(
                mangaRepository.getMangaBySourceId(sourceId),
                downloadCache.changes,
                query,
            ) { mangas, _, query ->
                mangas
                    .map { manga ->
                        val count = downloadManager.getDownloadCount(manga)
                        manga to count
                    }
                    .filter { it.second > 0 }
                    .filter { (manga, _) -> query.isNullOrBlank() || manga.title.contains(query, ignoreCase = true) }
                    .map { (manga, count) -> SourceDownloadMangaItem(manga, count) }
                    .sortedBy { it.manga.title }
            }
                .collectLatest { _state.value = it }
        }
    }

    fun search(query: String?) {
        this.query.value = query
    }

    fun deleteManga(manga: Manga) {
        screenModelScope.launchIO {
            val source = sourceManager.get(sourceId)
            if (source != null) {
                downloadManager.deleteManga(manga, source)
            }
        }
    }
}


