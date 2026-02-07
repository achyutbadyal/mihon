package eu.kanade.tachiyomi.ui.download.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.download.manager.SourceDownloadScreen as SourceDownloadScreenComposable
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.ui.manga.MangaScreen

data class SourceDownloadScreen(
    val sourceId: Long,
    val sourceName: String,
) : Screen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { SourceDownloadScreenModel(sourceId) }
        val state by screenModel.state.collectAsState()
        val query by screenModel.query.collectAsState()

        SourceDownloadScreenComposable(
            title = sourceName,
            state = state,
            searchQuery = query,
            onChangeSearchQuery = screenModel::search,
            onBackClick = { navigator.pop() },
            onMangaClick = { mangaId ->
                navigator.push(MangaScreen(mangaId))
            },
            onDeleteManga = screenModel::deleteManga,
        )
    }
}
