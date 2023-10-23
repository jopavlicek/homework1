package cz.mendelu.pef.petstore.ui.screens.listofpets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen

@Destination(start = true)
@Composable
fun ListOfPetsScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<ListOfPetsViewModel>()
    val uiState: MutableState<UiState<List<Pet>,ListOfPetsErrors>>
            = rememberSaveable { mutableStateOf(UiState()) }

    viewModel.petsUIState.value.let {
        uiState.value = it
    }

    BaseScreen(
        topBarText = "List of pets",
        drawFullScreenContent = true,
        showLoading = uiState.value.loading,
        placeholderScreenContent = null // TODO
    ) {
        ListOfPetsScreenContent(paddingValues = it, uiState = uiState.value)
    }
}


@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<List<Pet>, ListOfPetsErrors>
){

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        if (uiState.data != null){
            uiState.data!!.forEach {
                item {
                    Text(text = it.name!!, color = Color.Red)
                }
            }
        }
    }

}