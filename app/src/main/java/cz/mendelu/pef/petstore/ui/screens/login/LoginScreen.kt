package cz.mendelu.pef.petstore.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.RoundButton
import cz.mendelu.pef.petstore.ui.elements.TextInputField
import cz.mendelu.pef.petstore.ui.theme.basicMargin


@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
) {

    BaseScreen(
        topBarText = null,
        showLoading = true,
        placeholderScreenContent = null
    ) {
        LoginScreenContent(
            paddingValues = it,
            )
    }
}

@Composable
fun LoginScreenContent(
    paddingValues: PaddingValues,
    ){

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(all = basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        TextInputField(value = username, hint = stringResource(R.string.email), onValueChange = {
            username = it
        }, errorMessage = null)

        TextInputField(value = password, hint = stringResource(R.string.password), onValueChange = {
            password = it
        }, errorMessage = null,
            keyboardType = KeyboardType.Password)

        RoundButton(
            text = stringResource(R.string.sign_in), onClick = {

            })

    }
}