package com.ogzkesk.core.ui.content

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 3.dp,
    enabled: Boolean = true,
    iconEnabled: Boolean = true,
    expandable: Boolean = false,
    onSearchClicked: ((String) -> Unit)? = null,
    icon: ImageVector = Icons.Default.Edit,
    shapeDp: Dp = 16.dp,
    maxChar: Int? = null,
    @StringRes placeHolder: Int? = null,
    @StringRes label: Int? = null,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
) {

    var focused by remember { mutableStateOf(value = false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val maxCharColor by animateColorAsState(
        targetValue = maxChar?.toDouble()?.let { char ->
            when (value.length.toFloat()) {
                in (char / 2)..(char / 1.4) -> Color(0xFFFFAB2D)
                in (char / 1.4)..(char / 1) -> Color.Red
                else -> Color.Gray
            }
        } ?: Color.Gray
    )

    val height by animateDpAsState(
        targetValue = if (expandable) {
            if (focused) 150.dp else 48.dp
        } else {
            48.dp
        }
    )

    val defaultIcon = remember {
        onSearchClicked?.let { Icons.Default.Search } ?: icon
    }

    val clearFocusAndValue = {
        focusManager.clearFocus()
        if (value.isNotEmpty()) onValueChange("")
    }.also {
        BackHandler(enabled = focused, onBack = it)
    }



    Column(modifier = modifier) {
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            ) {
                Text(
                    text = stringResource(id = label),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = labelColor,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(shapeDp))
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(tonalElevation))
                .fillMaxWidth()
                .height(height)
        ) {
            if (iconEnabled) {
                IconButton(
                    enabled = enabled,
                    onClick = {
                        if (focused) clearFocusAndValue() else focusRequester.requestFocus()
                    }
                ) {
                    Crossfade(targetState = focused) { targetState ->

                        Icon(
                            imageVector = if (targetState) Icons.Default.ArrowBack else defaultIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        bottom = 8.dp,
                        start = if (iconEnabled) 0.dp else (shapeDp.value / 1.2).dp,
                        end = (shapeDp.value / 1.2).dp,
                    )
            ) {

                if (value.isEmpty() && placeHolder != null) {

                    Text(
                        text = stringResource(id = placeHolder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (focused) 4 else 1,
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = { value ->
                        maxChar?.let { if (value.length <= it) onValueChange(value) }
                            ?: onValueChange(value)
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged { focusState -> focused = focusState.isFocused },
                    maxLines = if (expandable) 4 else 1,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    singleLine = !expandable,
                    enabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (onSearchClicked != null) ImeAction.Search else ImeAction.Default
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (onSearchClicked != null) {
                                onSearchClicked(value)
                            }
                        }
                    )
                )

                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    visible = maxChar != null && expandable && focused
                ) {
                    Text(
                        text = "${value.length}/$maxChar",
                        style = MaterialTheme.typography.bodySmall,
                        color = maxCharColor
                    )
                }
            }
        }
    }
}