package android.reposearchapp.composables

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalTextApi::class)
@Composable
fun LinkText(
    text: String,
    urlText: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    textDecoration: TextDecoration = TextDecoration.Underline
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = textDecoration, color = textColor, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
            append(text)
        }
        addUrlAnnotation(
            UrlAnnotation(urlText),
            start = 0,
            end = text.length-1
        )
    }

    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotatedText,
        onClick = {
            annotatedText
                .getUrlAnnotations(it, it)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item.url)
                }
        }
    )
}