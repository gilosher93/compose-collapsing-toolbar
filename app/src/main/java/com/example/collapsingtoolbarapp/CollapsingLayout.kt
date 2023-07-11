package com.example.collapsingtoolbarapp

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import com.example.collapsingtoolbarapp.toolbar.ToolbarState
import com.example.collapsingtoolbarapp.toolbar.rememberToolbarScrollState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@Composable
fun CollapsingToolbarLayout(
    minToolbarHeight: Dp,
    maxToolbarHeight: Dp,
    toolbarState: ToolbarState = rememberToolbarScrollState(toolbarHeightRange = with(LocalDensity.current) {
        minToolbarHeight.roundToPx()..maxToolbarHeight.roundToPx()
    }),
    lazyListScope: LazyListScope.() -> Unit,
    toolbar: @Composable BoxScope.() -> Unit,

    ) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()
    val connection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            return toolbarState.update(
                scrollTopLimitReached = lazyState.firstVisibleItemIndex == 0 && lazyState.firstVisibleItemScrollOffset == 0,
                scrollOffset = toolbarState.scrollOffset - available.y
            )
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            if (available.y > 0) {
                scope.launch {
                    animateDecay(
                        initialValue = toolbarState.height + toolbarState.offset,
                        initialVelocity = available.y,
                        animationSpec = FloatExponentialDecaySpec()
                    ) { value: Float, velocity: Float ->
                        toolbarState.update(
                            scrollTopLimitReached = lazyState.firstVisibleItemIndex == 0 && lazyState.firstVisibleItemScrollOffset == 0,
                            scrollOffset = toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                        )
                        if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                    }
                }
            }
            return super.onPostFling(consumed, available)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = lazyState,
            modifier = Modifier
                .nestedScroll(connection)
                .graphicsLayer {
                    translationY = toolbarState.height + toolbarState.offset
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { scope.coroutineContext.cancelChildren() }
                    )
                },
        ) {
            lazyListScope()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .background(Color.Red)
                .graphicsLayer { translationY = toolbarState.offset }
        ) {
            toolbar()
        }
    }
}