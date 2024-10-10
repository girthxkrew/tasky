package com.rkm.tasky.feature.loginscreen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rkm.tasky.R
import com.rkm.tasky.feature.loginscreen.viewmodel.LoginViewModel
import com.rkm.tasky.ui.component.EmailValidationTextField
import com.rkm.tasky.ui.component.PasswordTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier
) {

    // TODO: Change to collectAsStateWithLifeCycle 

    val email by viewModel.emailState.collectAsState()
    val isValidEmail by viewModel.isValidEmailState.collectAsState()
    val password by viewModel.passwordState.collectAsState()
    val showPassword by viewModel.showPasswordState.collectAsState()

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val (greetingText, loginBodyLayout) = createRefs()
        val textGuideLine = createGuidelineFromTop(0.15f)
        val constraintLayoutGuideline = createGuidelineFromTop(0.3f)

        Text(
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(greetingText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(textGuideLine)
                },
            text = stringResource(R.string.login_screen_greeting),
            style = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .constrainAs(loginBodyLayout) {
                    top.linkTo(constraintLayoutGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {

            val (emailTextField, passwordTextField, loginButton, signUpText) = createRefs()
            val loginBodyBeginningGuideline = createGuidelineFromTop(0.05f)
            val loginBodyEndGuideline = createGuidelineFromTop(0.4f)

            EmailValidationTextField(
                email = email,
                onEmailChange = viewModel::updateEmail,
                isValidEmail = isValidEmail,
                modifier = Modifier
                    .constrainAs(emailTextField) {
                        top.linkTo(loginBodyBeginningGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(passwordTextField.top)
                    }
                    .padding(start = 16.dp, end = 16.dp)
            )

            PasswordTextField(
                password = password,
                onPasswordChange = viewModel::updatePassword,
                showPassword = showPassword,
                modifier = Modifier
                    .constrainAs(passwordTextField) {
                        top.linkTo(emailTextField.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(loginButton.top)
                    }
                    .padding(start = 16.dp, end = 16.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .constrainAs(loginButton) {
                        top.linkTo(passwordTextField.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(loginBodyEndGuideline)
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(R.string.login_button_text),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

        }

    }

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        viewModel = LoginViewModel(),
        modifier = Modifier
    )
}