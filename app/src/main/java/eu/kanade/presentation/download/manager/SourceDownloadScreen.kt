package eu.kanade.presentation.download.manager

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import eu.kanade.presentation.components.SearchToolbar
import eu.kanade.presentation.manga.components.BaseMangaListItem
import eu.kanade.tachiyomi.ui.download.manager.SourceDownloadMangaItem
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.ScrollbarLazyColumn
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.EmptyScreen

@Composable
fun SourceDownloadScreen(
    title: String,
    state: List<SourceDownloadMangaItem>,
    searchQuery: String?,
    onChangeSearchQuery: (String?) -> Unit,
    onBackClick: () -> Unit,
    onMangaClick: (Long) -> Unit,
    onDeleteManga: (tachiyomi.domain.manga.model.Manga) -> Unit,
) {
    var mangaToDelete by remember { mutableStateOf<tachiyomi.domain.manga.model.Manga?>(null) }

    if (mangaToDelete != null) {
        MangaDeleteDialog(
            onDismissRequest = { mangaToDelete = null },
            onConfirm = {
                onDeleteManga(mangaToDelete!!)
                mangaToDelete = null
            },
            title = mangaToDelete!!.title,
        )
    }

    Scaffold(
        topBar = {
            SearchToolbar(
                titleContent = { Text(stringResource(MR.strings.label_download_manager)) },
                navigateUp = onBackClick,
                searchQuery = searchQuery,
                onChangeSearchQuery = onChangeSearchQuery,
                onSearch = onChangeSearchQuery,
            )
        },
    ) { contentPadding ->
        if (state.isEmpty()) {
            val emptyMessage = if (!searchQuery.isNullOrEmpty()) {
                MR.strings.no_results_found
            } else {
                MR.strings.information_no_downloads
            }
            EmptyScreen(
                stringRes = emptyMessage,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            ScrollbarLazyColumn(
                contentPadding = contentPadding,
            ) {
                items(
                    items = state,
                    key = { it.manga.id },
                ) { item ->
                    BaseMangaListItem(
                        manga = item.manga,
                        onClickItem = { onMangaClick(item.manga.id) },
                        onLongClickItem = { mangaToDelete = item.manga },
                        actions = {
                            Text(
                                text = "${item.chapterCount}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = MaterialTheme.padding.medium),
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MangaDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(MR.strings.action_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(MR.strings.action_cancel))
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = stringResource(MR.strings.confirm_delete_all_chapters))
        },
    )
}
