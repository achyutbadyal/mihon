package eu.kanade.tachiyomi.ui.download.manager

import tachiyomi.domain.source.model.Source
import tachiyomi.domain.manga.model.Manga

data class DownloadManagerSourceItem(
    val source: Source,
    val mangaCount: Int,
)

data class SourceDownloadMangaItem(
    val manga: Manga,
    val chapterCount: Int,
)
