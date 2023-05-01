package com.ogzkesk.core.mine

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

@Composable
fun keyboardAsState(): State<Boolean> {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    return remember(ime){
        derivedStateOf {
            ime.getBottom(density) > 0
        }
    }
}


@Composable
fun animateHorizontalBrushAsState(
    targetValue: Boolean,
    colorList1: List<Color>,
    colorList2: List<Color>
): Brush {

    if (colorList1.size != colorList2.size) throw Exception("Color List sizes should be same")

    val brushList = mutableListOf<Color>()
    colorList1.forEachIndexed { i, _ ->
        val brushItem = animateColorAsState(
            targetValue = if (targetValue) {
                colorList1[i]
            } else {
                colorList2[i]
            }
        ).value
        brushList.add(brushItem)
    }

    return Brush.horizontalGradient(brushList)
}

@Composable
fun animateLinearBrushAsState(
    targetValue: Boolean,
    colorList1: List<Color>,
    colorList2: List<Color>
): Brush {

    if (colorList1.size != colorList2.size) throw Exception("Color List sizes should be same")

    val brushList = mutableListOf<Color>()
    colorList1.forEachIndexed { i, _ ->
        val brushItem = animateColorAsState(
            targetValue = if (targetValue) {
                colorList1[i]
            } else {
                colorList2[i]
            }
        ).value
        brushList.add(brushItem)
    }

    return Brush.linearGradient(brushList)
}