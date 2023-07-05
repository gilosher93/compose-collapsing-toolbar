package com.example.collapsingtoolbarapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp

private const val curveXFactor = 1.25f
private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f

private val headerHeight = 375.dp
private val toolbarHeight = 56.dp
private val paddingMedium = 16.dp
private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp

@Composable
fun CollapsingToolbar() {

    val scrollState = rememberScrollState()
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }
    val toolbarBottom = headerHeightPx - toolbarHeightPx
    val showToolbar by remember {
        derivedStateOf { scrollState.value >= toolbarBottom }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Header(
            scroll = scrollState,
            headerHeightPx = headerHeightPx
        )
        Body(scroll = scrollState)
        Toolbar(visible = showToolbar)
        Title(
            scroll = scrollState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx
        )
    }
}

@Composable
private fun Header(
    scroll: ScrollState,
    headerHeightPx: Float
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(headerHeight)
        .graphicsLayer {
            translationY = -scroll.value.toFloat() / 2f // Parallax effect
            alpha = (-1f / headerHeightPx) * scroll.value + 1
        }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.messi),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = headerHeightPx * 0.75f // to wrap the title only
                    )
                )
        )
    }
}

@Composable
private fun Body(
    scroll: ScrollState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scroll)
    ) {
        Spacer(Modifier.height(headerHeight))

        repeat(5) {
            Text(
                text = stringResource(R.string.lorem_ipsum),
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .background(Color(0XFF161616))
                    .padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xff026586), Color(0xff032C45))
                )
            ),
            navigationIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            title = {},
            colors = TopAppBarDefaults.smallTopAppBarColors(
                scrolledContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun Title(
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    var titleWidthPx by remember { mutableStateOf(0f) }
    var titleHeightPx by remember { mutableStateOf(0f) }
    val titleWidthDp = with(LocalDensity.current) { titleWidthPx.toDp() }
    val titleHeightDp = with(LocalDensity.current) { titleHeightPx.toDp() }

    val collapseRange: Float = (headerHeightPx - toolbarHeightPx)
    val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

    val scaleXY = lerp(
        titleFontScaleStart.dp,
        titleFontScaleEnd.dp,
        collapseFraction
    )

    val titleExtraStartPadding = titleWidthDp * (1 - scaleXY.value) / 2

    Text(
        text = stringResource(R.string.lionel_messi),
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .graphicsLayer {
                val titleYFirstInterpolatedPoint = lerp(
                    headerHeight - titleHeightDp - paddingMedium,
                    headerHeight / 2,
                    collapseFraction
                )
                val titleYSecondInterpolatedPoint = lerp(
                    headerHeight / 2,
                    toolbarHeight / 2 - titleHeightDp / 2 + (paddingMedium / 2),
                    collapseFraction
                )

                val titleY = lerp(
                    titleYFirstInterpolatedPoint,
                    titleYSecondInterpolatedPoint,
                    collapseFraction
                )

                val titleXFirstInterpolatedPoint = lerp(
                    titlePaddingStart,
                    (titlePaddingEnd - titleExtraStartPadding) * curveXFactor,
                    collapseFraction
                )
                val titleXSecondInterpolatedPoint = lerp(
                    (titlePaddingEnd - titleExtraStartPadding) * curveXFactor,
                    titlePaddingEnd - titleExtraStartPadding,
                    collapseFraction
                )
                val titleX = lerp(
                    titleXFirstInterpolatedPoint, // start X
                    titleXSecondInterpolatedPoint, // end X
                    collapseFraction
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()

                // if scaleXY equals 0.8.dp then scaleXY.value equals 0.8
                scaleX = scaleXY.value
                scaleY = scaleXY.value
            }
            .onGloballyPositioned {
                // We don't know title height in advance to calculate the lerp
                // so we wait for initial composition
                titleWidthPx = it.size.width.toFloat()
                titleHeightPx = it.size.height.toFloat()
            }
    )
}