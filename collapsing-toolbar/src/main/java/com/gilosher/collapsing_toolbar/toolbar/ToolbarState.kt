package com.gilosher.collapsing_toolbar.toolbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import com.gilosher.collapsing_toolbar.toolbar.scrollflags.EnterAlwaysCollapsedState
import com.gilosher.collapsing_toolbar.toolbar.scrollflags.EnterAlwaysState
import com.gilosher.collapsing_toolbar.toolbar.scrollflags.ExitUntilCollapsedState
import com.gilosher.collapsing_toolbar.toolbar.scrollflags.ScrollState

@Stable
interface ToolbarState {
    fun update(
        scrollTopLimitReached: Boolean,
        scrollOffset: Float
    ): Offset

    val offset: Float
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
}

@Composable
fun rememberToolbarScrollState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}

@Composable
fun rememberToolbarEnterAlwaysCollapsedState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = EnterAlwaysCollapsedState.Saver) {
        EnterAlwaysCollapsedState(toolbarHeightRange)
    }
}

@Composable
fun rememberToolbarEnterAlwaysState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = EnterAlwaysState.Saver) {
        EnterAlwaysState(toolbarHeightRange)
    }
}

@Composable
fun rememberToolbarExitUntilCollapsedState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}