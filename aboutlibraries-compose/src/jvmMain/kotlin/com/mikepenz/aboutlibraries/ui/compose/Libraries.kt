package com.mikepenz.aboutlibraries.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library

/**
 * Displays all provided libraries in a simple list.
 */
@Composable
fun LibrariesContainer(
    aboutLibsJson: String,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: LibraryColors = LibraryDefaults.libraryColors(),
    padding: LibraryPadding = LibraryDefaults.libraryPadding(),
    itemContentPadding: PaddingValues = LibraryDefaults.ContentPadding,
    header: (LazyListScope.() -> Unit)? = null,
    onLibraryClick: ((Library) -> Unit)? = null,
) {
    LibrariesContainer(
        { Libs.Builder().withJson(aboutLibsJson).build() },
        modifier,
        lazyListState,
        contentPadding,
        showAuthor,
        showVersion,
        showLicenseBadges,
        colors,
        padding,
        itemContentPadding,
        header,
        onLibraryClick
    )
}

/**
 * Displays all provided libraries in a simple list.
 */
@Composable
fun LibrariesContainer(
    librariesBlock: () -> Libs,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: LibraryColors = LibraryDefaults.libraryColors(),
    padding: LibraryPadding = LibraryDefaults.libraryPadding(),
    itemContentPadding: PaddingValues = LibraryDefaults.ContentPadding,
    header: (LazyListScope.() -> Unit)? = null,
    onLibraryClick: ((Library) -> Unit)? = null,
) {
    val libraries = remember { mutableStateOf<Libs?>(null) }
    LaunchedEffect(libraries) {
        libraries.value = librariesBlock.invoke()
    }

    val libs = libraries.value?.libraries
    if (libs != null) {
        Libraries(
            libraries = libs,
            modifier,
            lazyListState,
            contentPadding,
            showAuthor,
            showVersion,
            showLicenseBadges,
            colors,
            padding,
            itemContentPadding,
            header,
            onLibraryClick
        )
    }
}

/**
 * Displays all provided libraries in a simple list.
 */
@Composable
fun Libraries(
    libraries: List<Library>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: LibraryColors = LibraryDefaults.libraryColors(),
    padding: LibraryPadding = LibraryDefaults.libraryPadding(),
    itemContentPadding: PaddingValues = LibraryDefaults.ContentPadding,
    header: (LazyListScope.() -> Unit)? = null,
    onLibraryClick: ((Library) -> Unit)? = null,
) {
    LazyColumn(modifier, state = lazyListState, contentPadding = contentPadding) {
        header?.invoke(this)
        libraryItems(
            libraries,
            showAuthor,
            showVersion,
            showLicenseBadges,
            colors,
            padding,
            itemContentPadding) {
            onLibraryClick?.invoke(it)
        }
    }
}