package com.rkm.tasky.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rkm.tasky.R
import com.rkm.tasky.ui.theme.LightGreyTextFieldBackground
import com.rkm.tasky.ui.theme.TextFieldCheckColor
import com.rkm.tasky.ui.theme.TextFieldFontColor
import com.rkm.tasky.ui.theme.TextFieldHintColor

@Composable
fun ValidationTextField(
    state: TextFieldState,
    hint: Int,
    isValidEmail: Boolean,
    modifier: Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(color = LightGreyTextFieldBackground, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        BasicTextField(
            state = state,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            textStyle = TextStyle(
                color = TextFieldFontColor,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            decorator = { innerTextField ->
                if (state.text.isEmpty()) {
                    Text(
                        text = stringResource(hint),
                        style = TextStyle(
                            color = TextFieldHintColor,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                    )
                }
                innerTextField()
            }
        )

        if (isValidEmail) {
            Icon(
                imageVector = Icons.Rounded.Check,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
                contentDescription = null,
                tint = TextFieldCheckColor
            )
        }
    }

}

@Composable
fun PasswordTextField(
    password: TextFieldState,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    modifier: Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(color = LightGreyTextFieldBackground, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        BasicSecureTextField(
            state = password,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f),
            textStyle = TextStyle(
                color = TextFieldFontColor,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            ),
            textObfuscationMode = if (showPassword) TextObfuscationMode.Visible else TextObfuscationMode.Hidden,
            decorator = { innerTextField ->
                if (password.text.isEmpty()) {
                    Text(
                        text = stringResource(R.string.text_field_password_hint),
                        style = TextStyle(
                            color = TextFieldHintColor,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                    )
                }
                innerTextField()
            }
        )

        if (showPassword) {
            Icon(
                painter = painterResource(id = R.drawable.round_visibility_24),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .clickable { onShowPasswordClick() },
                contentDescription = null,
                tint = TextFieldHintColor
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.round_visibility_off_24),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .clickable { onShowPasswordClick() },
                contentDescription = null,
                tint = TextFieldHintColor
            )
        }
    }

}

@Preview(
    showBackground = false,
)
@Composable
private fun ValidationTextFieldNoEmailPreview() {
    ValidationTextField(
        hint = R.string.text_field_email_hint,
        state = TextFieldState(),
        isValidEmail = false,
        modifier = Modifier
    )
}

@Preview(
    showBackground = false,
)
@Composable
private fun ValidationTextFieldNotValidEmailPreview() {
    ValidationTextField(
        hint = R.string.text_field_email_hint,
        state = TextFieldState("someemail"),
        isValidEmail = false,
        modifier = Modifier
    )
}

@Preview(
    showBackground = false,
)
@Composable
private fun ValidationTextFieldValidEmailPreview() {
    ValidationTextField(
        hint = R.string.text_field_email_hint,
        state = TextFieldState("someemail@email.com"),
        isValidEmail = true,
        modifier = Modifier
    )
}

@Preview(
    showBackground = false,
)
@Composable
private fun PasswordTextFieldNoPasswordPreview() {
    PasswordTextField(
        password = TextFieldState(),
        onShowPasswordClick = {},
        showPassword = false,
        modifier = Modifier
    )
}

@Preview(
    showBackground = false,
)
@Composable
private fun PasswordTextFieldPasswordHiddenPreview() {
    PasswordTextField(
        password = TextFieldState("password"),
        onShowPasswordClick = {},
        showPassword = false,
        modifier = Modifier
    )
}
@Preview(
    showBackground = false,
)
@Composable
private fun PasswordTextFieldPasswordVisiblePreview() {
    PasswordTextField(
        password = TextFieldState("password"),
        onShowPasswordClick = {},
        showPassword = true,
        modifier = Modifier
    )
}

