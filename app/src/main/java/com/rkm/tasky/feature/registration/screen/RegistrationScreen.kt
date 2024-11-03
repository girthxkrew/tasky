package com.rkm.tasky.feature.registration.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
import com.rkm.tasky.R
import com.rkm.tasky.feature.registration.viewmodel.RegistrationScreenEvent
import com.rkm.tasky.feature.registration.viewmodel.RegistrationViewModel
import com.rkm.tasky.feature.snackbar.SnackBarController
import com.rkm.tasky.feature.snackbar.SnackBarEvent
import com.rkm.tasky.ui.component.PasswordTextField
import com.rkm.tasky.ui.component.ValidationTextField
import com.rkm.tasky.ui.event.ObserveAsEvents
import com.rkm.tasky.ui.theme.LoginSignUpTextColor
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreenRoot(
    onNavigateBack:() -> Unit,
    modifier: Modifier,
    viewModel: RegistrationViewModel = hiltViewModel()
) {

    val showPassword by viewModel.showPassword.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.registrationScreenEventChannel) { event ->
        when(event) {
            is RegistrationScreenEvent.RegistrationSuccessEvent -> {
                onNavigateBack()
            }
            is RegistrationScreenEvent.RegistrationFailedEvent -> {
                val message = event.message.asString(context)
                scope.launch {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = message
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        RegistrationScreen(
            email = viewModel.email,
            password = viewModel.password,
            fullName = viewModel.fullName,
            isValidEmail = viewModel.isValidEmail,
            isValidName = viewModel.isValidName,
            showPassword = showPassword,
            onShowPasswordClick = viewModel::onShowPasswordClicked,
            onRegistrationButtonClick = viewModel::onRegistrationClicked,
            onLoginClick = onNavigateBack,
            modifier = modifier
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier.wrapContentSize(),
                color = colorResource(R.color.black)
            )
        }
    }

}

@Composable
private fun RegistrationScreen(
    email: TextFieldState,
    password: TextFieldState,
    fullName: TextFieldState,
    isValidEmail: Boolean,
    isValidName: Boolean,
    showPassword: Boolean,
    onShowPasswordClick:() -> Unit,
    onRegistrationButtonClick:() -> Unit,
    onLoginClick:() -> Unit,
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
                text = stringResource(R.string.registration_screen_greeting),
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
            RegistrationBodyScreen(
                modifier = modifier,
                email = email,
                password = password,
                fullName = fullName,
                isValidEmail = isValidEmail,
                isValidName = isValidName,
                showPassword = showPassword,
                onShowPasswordClick = onShowPasswordClick,
                onRegistrationButtonClick = onRegistrationButtonClick,
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )

            SignInText(
                modifier = modifier,
                onLoginClick = onLoginClick
            )
        }

    }
}

@Composable
private fun RegistrationBodyScreen(
    modifier: Modifier,
    email: TextFieldState,
    password: TextFieldState,
    fullName: TextFieldState,
    isValidEmail: Boolean,
    isValidName: Boolean,
    showPassword: Boolean,
    onShowPasswordClick:() -> Unit,
    onRegistrationButtonClick:() -> Unit,
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ValidationTextField(
            state = fullName,
            hint = R.string.text_field_name_hint,
            isValid = isValidName,
            modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        ValidationTextField(
            state = email,
            hint = R.string.text_field_email_hint,
            isValid = isValidEmail,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
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
            onClick = onRegistrationButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            )
        ) {
            Text(text = stringResource(R.string.registration_button_text))
        }
    }
}

@Composable
private fun SignInText(
    modifier: Modifier,
    onLoginClick: () -> Unit
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .clickable {
                onLoginClick()
            },
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            ) {
                append(stringResource(R.string.registration_text_sign_up_question) + " ")
            }
            withStyle(
                style = SpanStyle(
                    color = LoginSignUpTextColor,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            ) {
                append(stringResource(R.string.registration_text_sign_in))
            }
        },
        textAlign = TextAlign.Center
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RegistrationScreenPreview() {
    RegistrationScreen(
        email = TextFieldState("email@email.com"),
        password = TextFieldState("password"),
        fullName = TextFieldState("Bob Smith"),
        isValidEmail = true,
        isValidName = true,
        showPassword = false,
        onShowPasswordClick = {},
        onRegistrationButtonClick = {},
        onLoginClick = {},
        modifier = Modifier
    )
}