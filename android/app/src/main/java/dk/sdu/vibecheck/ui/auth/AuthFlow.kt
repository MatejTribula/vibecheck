package dk.sdu.vibecheck.ui.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.sdu.vibecheck.ui.theme.VibeTextStyles
import dk.sdu.vibecheck.ui.theme.VibeTokens

@Composable
fun AuthFlow(viewModel: AuthViewModel, content: @Composable (AuthUiState) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoggedIn) {
        content(state)

        return
    }

    val colors = MaterialTheme.colorScheme
    Surface(modifier = Modifier.fillMaxSize(), color = colors.background, contentColor = colors.onBackground) {
        Column(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = VibeTokens.Spacing.ScreenHorizontal, vertical = VibeTokens.Spacing.ExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!state.isSessionChecked) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(state.errorMessage ?: "Could not check your session.")
                    Button(onClick = viewModel::checkSession, shape = RoundedCornerShape(VibeTokens.Shapes.Button)) {
                        Text("Retry")
                    }
                }
            } else {
                AuthForm(viewModel, state)
            }
        }
    }
}

private enum class AuthMode { SignIn, Register, ForgotPassword }

@Composable
private fun AuthForm(viewModel: AuthViewModel, state: AuthUiState) {
    var mode by rememberSaveable { mutableStateOf(AuthMode.SignIn) }
    var email by rememberSaveable { mutableStateOf("") }
    var displayName by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var accountCreated by rememberSaveable { mutableStateOf(false) }
    val registering = mode == AuthMode.Register
    val resetting = mode == AuthMode.ForgotPassword

    LaunchedEffect(state.registrationSuccessful) {
        if (state.registrationSuccessful) {
            mode = AuthMode.SignIn
            password = ""
            passwordVisible = false
            accountCreated = true
            viewModel.clearMessages()
        }
    }

    fun showForm(nextMode: AuthMode) {
        mode = nextMode
        password = ""
        passwordVisible = false
        accountCreated = false
        viewModel.clearMessages()
    }

    BackHandler(enabled = mode != AuthMode.SignIn && !state.isLoading) {
        showForm(AuthMode.SignIn)
    }

    Column(
        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(VibeTokens.Spacing.Medium)
    ) {
        AuthHeader(mode, Modifier.padding(bottom = VibeTokens.Spacing.Medium))

        if (registering) {
            AuthField(displayName, { displayName = it }, "Display name", "Your name", enabled = !state.isLoading)
        }

        AuthField(email, { email = it }, "Email", "you@example.com", enabled = !state.isLoading, keyboardType = KeyboardType.Email)

        if (!resetting) {
            AuthField(
                password, { password = it }, "Password",
                if (registering) "At least 8 characters" else "Enter your password",
                enabled = !state.isLoading,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            ) {
                TextButton(onClick = { passwordVisible = !passwordVisible }, enabled = !state.isLoading) {
                    Text(if (passwordVisible) "Hide" else "Show")
                }
            }
        }

        if (mode == AuthMode.SignIn) {
            TextButton(onClick = { showForm(AuthMode.ForgotPassword) }, modifier = Modifier.align(Alignment.End), enabled = !state.isLoading) {
                Text("Forgot password?")
            }
        }

        if (accountCreated) {
            Text("Account created. You can now sign in.")
        }

        state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (resetting) {
            state.passwordResetMessage?.let {
                Text(it)
                Text("For this demo, check the backend console for the reset link.")
            }
        }

        val actionLabel = when (mode) {
            AuthMode.SignIn -> "Sign in"
            AuthMode.Register -> "Create account"
            AuthMode.ForgotPassword -> "Request reset link"
        }
        val canSubmit = email.isNotBlank() && (resetting || password.isNotEmpty()) && (!registering || displayName.isNotBlank())

        Button(
            onClick = {
                accountCreated = false
                when (mode) {
                    AuthMode.SignIn -> viewModel.login(email, password)
                    AuthMode.Register -> viewModel.register(displayName, email, password)
                    AuthMode.ForgotPassword -> viewModel.requestPasswordReset(email)
                }
            },
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            shape = RoundedCornerShape(VibeTokens.Shapes.Button),
            enabled = !state.isLoading && canSubmit
        ) {
            Text(if (state.isLoading) "Please wait…" else actionLabel, style = VibeTextStyles.ButtonLabel)
        }

        val onSignIn = mode == AuthMode.SignIn
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (!resetting) {
                Text(if (onSignIn) "New to Vibe Check?" else "Already have an account?", style = VibeTextStyles.Subtitle)
            }

            TextButton(onClick = { showForm(if (onSignIn) AuthMode.Register else AuthMode.SignIn) }, enabled = !state.isLoading) {
                Text(if (onSignIn) "Create an account" else if (resetting) "Back to sign in" else "Sign in")
            }
        }
    }
}

@Composable
private fun AuthHeader(mode: AuthMode, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val title = when (mode) {
        AuthMode.SignIn -> "Welcome back"
        AuthMode.Register -> "Create your account"
        AuthMode.ForgotPassword -> "Reset password"
    }
    val subtitle = when (mode) {
        AuthMode.SignIn -> "Sign in to find your group’s vibe."
        AuthMode.Register -> "Make a playlist with your friends."
        AuthMode.ForgotPassword -> "Enter your account email to request a reset link."
    }

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(VibeTokens.Spacing.Small)) {
        Box(
            modifier = Modifier.size(48.dp).background(colors.primaryContainer, RoundedCornerShape(VibeTokens.Shapes.Card)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.size(28.dp, 18.dp)) {
                val wave = Path().apply {
                    moveTo(0f, center.y)
                    lineTo(size.width * 0.18f, center.y)
                    lineTo(size.width * 0.33f, size.height * 0.12f)
                    lineTo(size.width * 0.50f, size.height * 0.88f)
                    lineTo(size.width * 0.67f, size.height * 0.22f)
                    lineTo(size.width * 0.82f, center.y)
                    lineTo(size.width, center.y)
                }
                drawPath(wave, colors.onPrimaryContainer, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
        Text("Vibe Check", style = VibeTextStyles.Header, textAlign = TextAlign.Center)
        Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text(subtitle, style = VibeTextStyles.Subtitle, textAlign = TextAlign.Center)
    }
}

@Composable
private fun AuthField(value: String, onValueChange: (String) -> Unit, label: String, hint: String, enabled: Boolean, keyboardType: KeyboardType = KeyboardType.Text, visualTransformation: VisualTransformation = VisualTransformation.None, trailingIcon: (@Composable () -> Unit)? = null) {
    val colors = MaterialTheme.colorScheme
    val fieldBackground = if (isSystemInDarkTheme()) colors.surfaceContainerHigh else VibeTokens.Colors.JoinInput

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(hint) },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(VibeTokens.Shapes.Field),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldBackground,
            unfocusedContainerColor = fieldBackground,
            disabledContainerColor = fieldBackground,
            unfocusedBorderColor = colors.outlineVariant
        )
    )
}
