package cz.mendelu.pef.petstore.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.RoundButton
import cz.mendelu.pef.petstore.ui.elements.TextInputField
import cz.mendelu.pef.petstore.ui.screens.destinations.ListOfPetsScreenDestination
import cz.mendelu.pef.petstore.ui.theme.basicMargin

@Destination(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    val uiState: MutableState<UiState<Nothing, LoginErrors>>
            = rememberSaveable { mutableStateOf(UiState(loading = false)) }

    viewModel.loginUIState.value.let {
        uiState.value = it
    }

    viewModel.loginSuccessful.value.let {
        if (it == true) {
            navigator.popBackStack()
            navigator.navigate(ListOfPetsScreenDestination)
        }
    }

    BaseScreen(
        topBarText = if (viewModel.loginSuccessful.value == false) "Pet Store" else null,
        showLoading = uiState.value.loading || viewModel.loginSuccessful.value != false,
        placeholderScreenContent = null
    ) {
        if (viewModel.loginSuccessful.value == false) {
            LoginScreenContent(viewModel = viewModel, uiState = uiState.value)
        }
    }
}

@Composable
fun LoginScreenContent(
    viewModel: LoginScreenViewModel,
    uiState: UiState<Nothing, LoginErrors>
){

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(all = basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        TextInputField(
            value = username, hint = stringResource(R.string.email), onValueChange = {
            username = it
        }, errorMessage = uiState.errors?.usernameError)

        TextInputField(value = password, hint = stringResource(R.string.password), onValueChange = {
            password = it
        }, errorMessage = uiState.errors?.passwordError,
            keyboardType = KeyboardType.Password)

        RoundButton(
            text = stringResource(R.string.sign_in),
            onClick = {
                viewModel.login(username, password)
            })

        if (uiState.errors != null) {
            if (uiState.errors!!.communicationError != null) {
                Text(stringResource(id = uiState.errors!!.communicationError!!))
            }
        }

    }
}