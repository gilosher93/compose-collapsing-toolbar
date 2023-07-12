package com.gilosher.collapsing_toolbar.toolbar.scrollflags

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import com.gilosher.collapsing_toolbar.toolbar.ScrollFlagState

class ScrollState(
    heightRange: IntRange,
    scrollOffset: Float = 0f
) : ScrollFlagState(heightRange) {

    override var _scrollOffset by mutableStateOf(scrollOffset.coerceIn(0f, maxHeight.toFloat()))

    override val offset: Float
        get() = -(scrollOffset - rangeDifference).coerceIn(0f, minHeight.toFloat())

    override var scrollOffset: Float
        get() = _scrollOffset
        set(value) {
            if (scrollTopLimitReached) {
                val oldOffset = _scrollOffset
                Log.d("GIL_TEST", "oldOffset: $oldOffset")
                _scrollOffset = value.coerceIn(0f, maxHeight.toFloat())
                Log.d("GIL_TEST", "_scrollOffset: $_scrollOffset")
                _consumed = oldOffset - _scrollOffset
                Log.d("GIL_TEST", "_consumed: $_consumed")
            } else {
                _consumed = 0f
                Log.d("GIL_TEST", "_consumed: $_consumed")
            }
        }

    companion object {
        val Saver = run {

            val minHeightKey = "MinHeight"
            val maxHeightKey = "MaxHeight"
            val scrollOffsetKey = "ScrollOffset"

            mapSaver(
                save = {
                    mapOf(
                        minHeightKey to it.minHeight,
                        maxHeightKey to it.maxHeight,
                        scrollOffsetKey to it.scrollOffset
                    )
                },
                restore = {
                    ScrollState(
                        heightRange = (it[minHeightKey] as Int)..(it[maxHeightKey] as Int),
                        scrollOffset = it[scrollOffsetKey] as Float,
                    )
                }
            )
        }
    }
}