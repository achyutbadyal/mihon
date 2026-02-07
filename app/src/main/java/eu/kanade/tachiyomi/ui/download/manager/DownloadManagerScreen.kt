package eu.kanade.tachiyomi.ui.download.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.download.manager.DownloadManagerScreen as DownloadManagerScreenComposable
import eu.kanade.presentation.util.Screen

class DownloadManagerScreen : Screen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { DownloadManagerScreenModel() }
        val state by screenModel.state.collectAsState()
        val query by screenModel.query.collectAsState()

        DownloadManagerScreenComposable(
            state = state,
            searchQuery = query,
            onChangeSearchQuery = screenModel::search,
            onBackClick = { navigator.pop() },
            onSourceClick = { sourceId ->
                // Find source name from state to pass it
                val sourceName = state.find { it.source.id == sourceId }?.source?.name ?: ""
                navigator.push(SourceDownloadScreen(sourceId, sourceName))
            },
            onDeleteSource = screenModel::deleteSource,
        )
    }
}
