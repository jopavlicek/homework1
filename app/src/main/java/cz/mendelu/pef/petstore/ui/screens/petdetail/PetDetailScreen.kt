package cz.mendelu.pef.petstore.ui.screens.petdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.ui.screens.listofpets.ListOfPetsErrors
import cz.mendelu.pef.petstore.ui.screens.listofpets.ListOfPetsScreenContent
import cz.mendelu.pef.petstore.ui.screens.listofpets.ListOfPetsViewModel
import cz.mendelu.pef.petstore.ui.theme.basicTextColor

@Destination
@Composable
fun PetDetailScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    id: Long
) {
    val viewModel = hiltViewModel<PetDetailViewModel>()
    val uiState: MutableState<UiState<Pet, PetDetailErrors>>
        = rememberSaveable { mutableStateOf(UiState()) }

    viewModel.petsUIState.value.let {
        uiState.value = it
    }

    if (!viewModel.isInitialized.value) {
        viewModel.loadPet(id)
        viewModel.isInitialized.value = true
    }

    viewModel.deleteResult.value.let {
        if (it == true) {
            resultNavigator.navigateBack(result = true)
            // navigator.popBackStack()
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.pet_delete_ok), Toast.LENGTH_LONG
            ).show()
        } else if (it == false) {
            resultNavigator.navigateBack(result = true)
            // navigator.popBackStack()
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.pet_delete_fail), Toast.LENGTH_LONG
            ).show()
        }
    }

    BaseScreen(
        topBarText = "Pet detail",
        drawFullScreenContent = true,
        showLoading = uiState.value.loading,
        onBackClick = {
            navigator.popBackStack()
        },
        actions = {
            IconButton(
                enabled = !uiState.value.loading && uiState.value.errors == null,
                onClick = {
                    viewModel.deletePet(id = id)
                },
            ) {
                Icon(Icons.Filled.DeleteOutline, null)
            }
        }
    ) {
        if (uiState.value.errors != null) {
            PlaceHolderScreen(
                modifier = Modifier,
                content = PlaceholderScreenContent(
                    image = null,
                    text = stringResource(id = uiState.value.errors!!.communicationError)
                )
            )
        } else {
            PetDetailScreenContent(
                paddingValues = it,
                uiState = uiState.value
            )
        }
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<Pet, PetDetailErrors>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (uiState.data != null) {
            Text(uiState.data!!.id.toString(), color = basicTextColor())
            Text(uiState.data!!.name ?: "Anonymous Pet", color = basicTextColor())
        }
    }
}