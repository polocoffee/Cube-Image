@file:OptIn(ExperimentalFoundationApi::class)

package com.banklannister.cubeimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.banklannister.cubeimage.ui.theme.CubeImageTheme
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CubeImageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val pictures = listOf(
                        R.drawable.pic_1,
                        R.drawable.pic_2,
                        R.drawable.pic_3,
                        R.drawable.pic_4,
                        R.drawable.pic_5,
                        R.drawable.pic_1,
                        R.drawable.pic_2,
                        R.drawable.pic_3,
                        R.drawable.pic_4,
                        R.drawable.pic_5
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val horizontalPagerState = rememberPagerState(
                            pageCount = {
                                pictures.count()
                            }
                        )

                        val verticalPagerState = rememberPagerState(
                            pageCount = {
                                pictures.count()
                            }
                        )
                        HorizontalPager(
                            state = horizontalPagerState,
                            modifier = Modifier.size(250.dp)
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .applyCube(horizontalPagerState, page, horizontal = true)


                            ) {
                                Image(
                                    painter = painterResource(id = pictures[page]),
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = null
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        VerticalPager(
                            state = verticalPagerState,
                            modifier = Modifier.size(250.dp)
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .applyCube(verticalPagerState, page, horizontal = false)


                            ) {
                                Image(
                                    painter = painterResource(id = pictures[page]),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = null
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.applyCube(state: PagerState, page: Int, horizontal: Boolean): Modifier {
    return graphicsLayer {
        val pageOffset = state.offSetForPage(page)
        val offScreenRight = pageOffset < 0f
        val deg = if (horizontal) 105f else -90f
        val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
        val rotation = (interpolated * if (offScreenRight) deg else -deg).coerceAtMost(90f)
        if (horizontal) {
            rotationY = rotation
        } else {
            rotationX = rotation
        }

        transformOrigin = if (horizontal) {
            TransformOrigin(
                pivotFractionX = if (offScreenRight) 0f else 1f,
                pivotFractionY = .5f
            )
        } else {
            TransformOrigin(
                pivotFractionX = if (offScreenRight) 0f else 1f,
                pivotFractionY = .5f
            )
        }
    }
}


fun PagerState.offSetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

fun PagerState.startOffsetForPage(page: Int) = offSetForPage(page).coerceAtLeast(0f)

fun PagerState.endOffsetForPage(page: Int) = offSetForPage(page).coerceAtMost(0f)


