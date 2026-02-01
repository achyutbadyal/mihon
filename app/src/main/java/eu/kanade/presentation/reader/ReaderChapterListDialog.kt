package eu.kanade.presentation.reader

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import eu.kanade.presentation.components.AdaptiveSheet
import eu.kanade.presentation.components.TabbedDialogPaddings
import eu.kanade.tachiyomi.ui.reader.model.ReaderChapter
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource
import java.text.DateFormat

@Composable
fun ReaderChapterListDialog(
    onDismissRequest: () -> Unit,
    chapters: List<ReaderChapter>,
    currentChapter: ReaderChapter?,
    onChapterSelected: (ReaderChapter) -> Unit,
) {
    BoxWithConstraints {
        AdaptiveSheet(
            modifier = Modifier.heightIn(max = maxHeight * 0.75f),
            onDismissRequest = onDismissRequest,
        ) {
            val listState = rememberLazyListState()

            LaunchedEffect(currentChapter) {
                currentChapter?.let {
                    val index = chapters.indexOf(it)
                    if (index != -1) {
                        listState.scrollToItem(index)
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(MR.strings.chapters),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = TabbedDialogPaddings.Horizontal, vertical = TabbedDialogPaddings.Vertical)
                )

                HorizontalDivider()

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(vertical = MaterialTheme.padding.small),
                ) {
                    items(chapters) { chapter ->
                        val isCurrent = chapter.chapter.id == currentChapter?.chapter?.id
                        val itemColor = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        val alpha = if (chapter.chapter.read && !isCurrent) 0.38f else 1f

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onChapterSelected(chapter) }
                                .padding(horizontal = TabbedDialogPaddings.Horizontal, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            if (isCurrent) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = itemColor,
                                    modifier = Modifier.size(16.dp),
                                )
                            }

                            // Bookmark icon if bookmarked
                            if (chapter.chapter.bookmark) {
                                Icon(
                                    imageVector = Icons.Outlined.Bookmark,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp),
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = chapter.chapter.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = itemColor.copy(alpha = alpha),
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                chapter.chapter.date_upload.takeIf { it > 0 }?.let { date ->
                                    Text(
                                        text = DateFormat.getDateInstance(DateFormat.SHORT).format(date),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (chapter.chapter.read) 0.38f else 1f),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
