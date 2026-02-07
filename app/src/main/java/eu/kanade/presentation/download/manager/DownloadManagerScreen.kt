package eu.kanade.presentation.download.manager

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import eu.kanade.presentation.browse.components.SourceIcon
import eu.kanade.presentation.components.SearchToolbar
import eu.kanade.tachiyomi.ui.download.manager.DownloadManagerSourceItem
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.ScrollbarLazyColumn
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.EmptyScreen

@Composable
fun DownloadManagerScreen(
    state: List<DownloadManagerSourceItem>,
    searchQuery: String?,
    onChangeSearchQuery: (String?) -> Unit,
    onBackClick: () -> Unit,
    onSourceClick: (Long) -> Unit,
    onDeleteSource: (tachiyomi.domain.source.model.Source) -> Unit,
) {
    var sourceToDelete by remember { mutableStateOf<tachiyomi.domain.source.model.Source?>(null) }

    if (sourceToDelete != null) {
        DownloadManagerDeleteDialog(
            onDismissRequest = { sourceToDelete = null },
            onConfirm = {
                onDeleteSource(sourceToDelete!!)
                sourceToDelete = null
            },
            title = sourceToDelete!!.name,
        )
    }

    Scaffold(
        topBar = {
            SearchToolbar(
                titleContent = { Text(stringResource(MR.strings.label_download_manager)) },
                navigateUp = onBackClick,
                searchQuery = searchQuery,
                onChangeSearchQuery = onChangeSearchQuery,
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
                    key = { it.source.id },
                ) { item ->
                    DownloadManagerSourceItem(
                        item = item,
                        onClick = { onSourceClick(item.source.id) },
                        onLongClick = { sourceToDelete = item.source },
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadManagerSourceItem(
    item: DownloadManagerSourceItem,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SourceIcon(
            source = item.source,
            modifier = Modifier.padding(end = 16.dp),
        )

        Text(
            text = item.source.name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "${item.mangaCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DownloadManagerDeleteDialog(
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
