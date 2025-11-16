package com.plcoding.core.designsystem.components.chat

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

enum class ChatBubblePosition {
    LEFT,
    RIGHT,
}


class ChatBubbleShape(
    private val trianglePosition: ChatBubblePosition,
    private val triangleSize: Dp = 16.dp,
    private val cornerRadius: Dp = 8.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val triangleSizePixel = with(density) { triangleSize.toPx() }
        val cornerRadiusPixel = with(density) { cornerRadius.toPx() }

        val path = when (trianglePosition) {
            ChatBubblePosition.LEFT -> {
                val bodPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = triangleSizePixel,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            cornerRadius = CornerRadius(
                                x = cornerRadiusPixel,
                                y = cornerRadiusPixel,
                            )
                        )
                    )
                }
                val trianglePath = Path().apply {
                    moveTo(
                        x = 0f,
                        y = size.height
                    )
                    lineTo(triangleSizePixel, size.height - cornerRadiusPixel)
                    lineTo(triangleSizePixel + cornerRadiusPixel, size.height)
                    close()
                }
                Path.combine(PathOperation.Union, bodPath, trianglePath)
            }

            ChatBubblePosition.RIGHT -> {
                val bodPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width - triangleSizePixel,
                            bottom = size.height,
                            cornerRadius = CornerRadius(
                                x = cornerRadiusPixel,
                                y = cornerRadiusPixel,
                            )
                        )
                    )
                }
                val trianglePath = Path().apply {
                    moveTo(
                        x = size.width,
                        y = size.height
                    )
                    lineTo(size.width - triangleSizePixel, size.height - cornerRadiusPixel)
                    lineTo(size.width - triangleSizePixel - cornerRadiusPixel, size.height)
                    close()
                }
                Path.combine(PathOperation.Union, bodPath, trianglePath)
            }
        }
        return Outline.Generic(path)
    }

}