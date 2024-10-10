package com.rkm.tasky.ui.component

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rkm.tasky.R
import com.rkm.tasky.ui.theme.LightGreyTextFieldBackground
import com.rkm.tasky.ui.theme.TextFieldCheckColor
import com.rkm.tasky.ui.theme.TextFieldFontColor
import com.rkm.tasky.ui.theme.TextFieldHintColor

@Composable
fun EmailValidationTextField(
    email: String,
    onEmailChange: (String) -> Unit,
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
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier
                .weight(5f)
                .padding(16.dp),
            textStyle = TextStyle(
                color = TextFieldFontColor,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            ),
            decorationBox = { innerTextField ->
                if (email.isEmpty()) {
                    Text(
                        text = stringResource(R.string.text_field_email_hint),
                        style = TextStyle(
                            color = TextFieldHintColor,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                    )
                }
                innerTextField()
            }
        )

        if(isValidEmail) {
            Icon(
                imageVector = Icons.Rounded.Check,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                contentDescription = null,
                tint = TextFieldCheckColor
            )
        }
    }

}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
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
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .weight(5f)
                .padding(16.dp),
            textStyle = TextStyle(
                color = TextFieldFontColor,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            ),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) {
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

        if(showPassword) {
            Icon(
                painter = painterResource(id = R.drawable.round_visibility_24),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                contentDescription = null,
                tint = TextFieldHintColor
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.round_visibility_off_24),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
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
fun EmailValidationTextFieldPreview() {
    EmailValidationTextField(
        email = "someemail@email.com",
        onEmailChange = {},
        isValidEmail = true,
        modifier = Modifier
    )
}

@Preview(
    showBackground = false,
)
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(
        password = "password",
        onPasswordChange = {},
        showPassword = false,
        modifier = Modifier
    )
}

