package cz.mendelu.pef.petstore.ui.screens.listofpets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.ui.screens.destinations.PetDetailScreenDestination
import cz.mendelu.pef.petstore.ui.theme.basicMargin
import cz.mendelu.pef.petstore.ui.theme.halfMargin

@Destination(start = true)
@Composable
fun ListOfPetsScreen(
    navigator: DestinationsNavigator,
    petDetailRecipient: ResultRecipient<PetDetailScreenDestination, Boolean>,
) {
    val viewModel = hiltViewModel<ListOfPetsViewModel>()
    val uiState: MutableState<UiState<List<Pet>,ListOfPetsErrors>>
        = rememberSaveable { mutableStateOf(UiState()) }

    viewModel.petsUIState.value.let {
        uiState.value = it
    }

    // refresh pet list after delete action on pet detail screen
    petDetailRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.refreshList()
                }
            }
        }
    }

    BaseScreen(
        topBarText = "List of pets",
        drawFullScreenContent = true,
        showLoading = uiState.value.loading,
        actions = {
            IconButton(
                enabled = !uiState.value.loading,
                onClick = {
                    viewModel.refreshList()
                },
            ) {
                Icon(Icons.Filled.Refresh, null)
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
        } else if (uiState.value.data?.isEmpty() == true) {
            PlaceHolderScreen(
                modifier = Modifier,
                content = PlaceholderScreenContent(
                    image = null,
                    text = stringResource(R.string.no_available_pets)
                )
            )
        } else {
            ListOfPetsScreenContent(
                paddingValues = it,
                uiState = uiState.value,
                navigator = navigator
            )
        }
    }
}


@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<List<Pet>, ListOfPetsErrors>,
    navigator: DestinationsNavigator
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(all = basicMargin()),
        verticalArrangement = Arrangement.spacedBy(halfMargin()),
    ) {
        if (uiState.data != null) {
            uiState.data!!.forEach {
                item {
                    PetCard(navigator = navigator, pet = it)
                }
            }
        }
    }
}

@Composable
fun PetCard(
    navigator: DestinationsNavigator,
    pet: Pet,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.navigate(PetDetailScreenDestination(id = pet.id!!))
            }
    ) {
        Column() {
            Text(
                text = if (pet.name.isNullOrEmpty()) "Pet" else pet.name!!,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "#${pet.id}",
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp
            )
        }
    }
}
