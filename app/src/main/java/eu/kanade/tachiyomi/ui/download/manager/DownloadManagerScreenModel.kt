package eu.kanade.tachiyomi.ui.download.manager

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import eu.kanade.tachiyomi.data.download.DownloadCache
import eu.kanade.tachiyomi.data.download.DownloadManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import logcat.LogPriority
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.core.common.util.system.logcat
import tachiyomi.domain.manga.repository.MangaRepository
import tachiyomi.domain.source.repository.SourceRepository
import tachiyomi.domain.source.service.SourceManager
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class DownloadManagerScreenModel(
    private val downloadManager: DownloadManager = Injekt.get(),
    private val sourceRepository: SourceRepository = Injekt.get(),
    private val mangaRepository: MangaRepository = Injekt.get(),
    private val sourceManager: SourceManager = Injekt.get(),
    private val downloadCache: DownloadCache = Injekt.get(),
) : ScreenModel {

    private val _state = MutableStateFlow<List<DownloadManagerSourceItem>>(emptyList())
    val state: StateFlow<List<DownloadManagerSourceItem>> = _state.asStateFlow()
    
    val query = MutableStateFlow<String?>(null)

    init {
        screenModelScope.launch {
            try {
                combine(
                    sourceRepository.getSources(),
                    downloadCache.changes,
                    query,
                ) { sources, _, query ->
                    val counts = downloadManager.getSourceDownloadCounts()
                    sources
                        .mapNotNull { source ->
                            val count = counts[source.id] ?: 0
                            if (count > 0 && (query.isNullOrBlank() || source.name.contains(query, ignoreCase = true))) {
                                DownloadManagerSourceItem(source, count)
                            } else {
                                null
                            }
                        }
                        .sortedBy { it.source.name }
                }
                .collectLatest { _state.value = it }
            } catch (e: Exception) {
                logcat(LogPriority.ERROR, e) { "Failed to load download manager sources" }
            }
        }
    }
    
    fun search(query: String?) {
        this.query.value = query
    }

    fun deleteSource(source: tachiyomi.domain.source.model.Source) {
        screenModelScope.launchIO {
            val mangaList = mangaRepository.getMangaBySourceId(source.id).firstOrNull() ?: emptyList()
            val legacySource = sourceManager.get(source.id) ?: return@launchIO
            mangaList.forEach { manga ->
                downloadManager.deleteManga(manga, legacySource)
            }
        }
    }
}


