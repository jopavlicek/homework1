package cz.mendelu.pef.petstore.ui.screens.newpet

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.RoundButton
import cz.mendelu.pef.petstore.ui.elements.TextInputField
import cz.mendelu.pef.petstore.ui.theme.basicMargin


@Destination
@Composable
fun NewPetScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val viewModel = hiltViewModel<NewPetViewModel>()
    val uiState: MutableState<UiState<Nothing, NewPetErrors>> =
        rememberSaveable { mutableStateOf(UiState(loading = false)) }

    viewModel.petsUIState.value.let {
        uiState.value = it
    }

    viewModel.createResult.value.let {
        if (it == true) {
            resultNavigator.navigateBack(result = true)
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.pet_create_ok), Toast.LENGTH_SHORT
            ).show()
        } else if (it == false) {
            resultNavigator.navigateBack(result = true)
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.pet_create_fail), Toast.LENGTH_LONG
            ).show()
        }
    }

    BaseScreen(
        topBarText = "Add Pet",
        drawFullScreenContent = true,
        onBackClick = {
            navigator.popBackStack()
        },
        showLoading = uiState.value.loading
    ) {
        NewPetScreenContent(paddingValues = it, viewModel = viewModel)
    }
}

@Composable
fun NewPetScreenContent(
    paddingValues: PaddingValues,
    viewModel: NewPetViewModel
) {

    var name by rememberSaveable { mutableStateOf("") }
    var photoUrl by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(all = basicMargin()),
    ) {
        TextInputField(
            value = name,
            hint = stringResource(R.string.pet_name),
            onValueChange = { name = it },
            errorMessage = null
        )

        TextInputField(
            value = photoUrl,
            hint = stringResource(R.string.photo_url),
            onValueChange = { photoUrl = it },
            errorMessage = null
        )

        RoundButton(
            text = stringResource(R.string.save_pet),
            onClick = { viewModel.createPet(name, photoUrl) },
            backgroundColor = MaterialTheme.colorScheme.primary,
            enabled = name.isNotBlank()
        )
    }

}