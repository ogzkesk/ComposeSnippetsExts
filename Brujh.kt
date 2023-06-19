package com.ogzkesk.animtest.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class TargetBrush {
    HORIZONTAL,
    VERTICAL,
    RADIAL,
    LINEAR,
    SWEEP
}

class Brujh private constructor(
    var brush: Brush,
    var reversed: Brush,
) {

    companion object {

        val defaultAnimationSpec: AnimationSpec<Color> = spring()

        @Composable
        fun animateAsState(
            colorList1: List<Color>,
            colorList2: List<Color>,
            targetValue: Boolean,
            targetBrush: TargetBrush = TargetBrush.LINEAR,
            animationSpec: AnimationSpec<Color> = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ): Brujh {

            return Brujh(
                brush = animateBrushAsState(
                    targetValue = targetValue,
                    colorList1 = colorList1,
                    colorList2 = colorList2,
                    animationSpec = animationSpec,
                    targetBrush = targetBrush
                ),
                reversed = animateBrushAsState(
                    targetValue = targetValue,
                    colorList1 = colorList2,
                    colorList2 = colorList1,
                    animationSpec = animationSpec,
                    targetBrush = targetBrush
                )
            )
        }


        @Composable
        private fun animateBrushAsState(
            targetValue: Boolean,
            colorList1: List<Color>,
            colorList2: List<Color>,
            animationSpec: AnimationSpec<Color>,
            targetBrush: TargetBrush
        ): Brush {

            if (colorList1.size != colorList2.size) throw Exception("Colorlist sizes should be same")

            val brushList = mutableListOf<Color>()
            colorList1.forEachIndexed { i, _ ->
                val brushItem = animateColorAsState(
                    animationSpec = animationSpec,
                    targetValue = if (targetValue) {
                        colorList1[i]
                    } else {
                        colorList2[i]
                    }
                ).value
                brushList.add(brushItem)
            }

            return when (targetBrush) {
                TargetBrush.LINEAR -> Brush.linearGradient(brushList)
                TargetBrush.HORIZONTAL -> Brush.horizontalGradient(brushList)
                TargetBrush.VERTICAL -> Brush.verticalGradient(brushList)
                TargetBrush.RADIAL -> Brush.radialGradient(brushList)
                TargetBrush.SWEEP -> Brush.sweepGradient(brushList)
            }
        }
    }
}


private val Blue = Color(0xFF29B6F6)
private val DarkBlue = Color(0xFF3F51B5)
private val DarkBlue2 = Color(0xFF1D2D83)
private val Orange = Color(0xFFFFAB2D)
private val Pink = Color(0xFFFA1E61)
private val Purple = Color(0xFFAA32B8)

@Preview
@Composable
fun PrevBrujh() {

    var selected by remember { mutableStateOf(false) }

    val brujh = Brujh.animateAsState(
        colorList1 = listOf(Orange, Pink, Purple),
        colorList2 = listOf(Blue, DarkBlue, DarkBlue2),
        targetValue = selected,
        animationSpec = tween(700),
        targetBrush = TargetBrush.LINEAR
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 32.dp)
                .background(brujh.brush, RoundedCornerShape(16.dp))
                .border(3.dp, brujh.reversed, RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { selected = !selected }) {
            Text(text = "CHANGE")
        }
    }

}