package com.rkm.tasky.feature.loginscreen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rkm.tasky.R
import com.rkm.tasky.feature.loginscreen.viewmodel.LoginViewModel
import com.rkm.tasky.ui.component.PasswordTextField
import com.rkm.tasky.ui.component.ValidationTextField
import com.rkm.tasky.ui.theme.LoginSignUpTextColor

@Composable
fun LoginScreenRoot(
    navHostController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val showPassword by viewModel.showPassword.collectAsStateWithLifecycle()
    val isValidEmail by viewModel.isValidEmail.collectAsStateWithLifecycle()

    LoginScreen(
        email = viewModel.email,
        password = viewModel.password,
        isValidEmail = isValidEmail,
        showPassword = showPassword,
        onShowPasswordClick = viewModel::onShowPasswordClicked,
        onLoginButtonClick = { /*TODO*/ },
        modifier = Modifier
    )

}

@Composable
fun LoginScreen(
    email: TextFieldState,
    password: TextFieldState,
    isValidEmail: Boolean,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.login_screen_greeting),
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginBodyScreen(
                modifier = modifier,
                email = email,
                password = password,
                isValidEmail = isValidEmail,
                showPassword = showPassword,
                onShowPasswordClick = onShowPasswordClick,
                onLoginButtonClick = onLoginButtonClick
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
            )

            SignUpText(
                modifier = modifier,
                onSignUpClick = {}
            )
        }

    }

}

@Composable
private fun LoginBodyScreen(
    modifier: Modifier,
    email: TextFieldState,
    password: TextFieldState,
    isValidEmail: Boolean,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    onLoginButtonClick: () -> Unit
) {
   Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ValidationTextField(
            state = email,
            hint = R.string.text_field_email_hint,
            isValidEmail = isValidEmail,
            modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        PasswordTextField(
            password = password,
            onShowPasswordClick = onShowPasswordClick,
            showPassword = showPassword,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

       Button(
           modifier = modifier
               .fillMaxWidth()
               .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
           onClick = onLoginButtonClick,
           colors = ButtonDefaults.buttonColors(
               containerColor = Color.Black,
           )
       ) {
           Text(text = stringResource(R.string.login_button_text))
       }
    }

}

@Composable
private fun SignUpText(
    modifier: Modifier,
    onSignUpClick: () -> Unit
){
    Text(
        modifier = modifier.fillMaxWidth()
            .padding(bottom = 32.dp)
            .clickable {
                onSignUpClick()
            },
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black, fontSize = MaterialTheme.typography.bodyLarge.fontSize)){
                append(stringResource(R.string.login_text_sign_up_question) + " ")
            }
            withStyle(style = SpanStyle(color = LoginSignUpTextColor, fontSize = MaterialTheme.typography.bodyLarge.fontSize)){
                append(stringResource(R.string.login_text_sign_up))
            }
        },
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun SignUpTextPreview() {
    SignUpText(
        modifier = Modifier,
        onSignUpClick = {}
    )
}
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        TextFieldState("email@email.com"),
        TextFieldState("password"),
        true,
        false,
        {},
        {},
        Modifier
    )
}