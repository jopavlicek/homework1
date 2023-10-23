package cz.mendelu.pef.petstore.ui.screens.listofpets

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListOfPetsViewModel @Inject constructor(
    private val petsRemoteRepositoryImpl: PetsRemoteRepositoryImpl) : BaseViewModel()
{
    init {
        loadPets()
    }

    val petsUIState: MutableState<UiState<List<Pet>, ListOfPetsErrors>>
            = mutableStateOf(UiState())

    private fun loadPets(){
        launch {
            val result = withContext(Dispatchers.IO) {
                petsRemoteRepositoryImpl.findByStatus("available")
            }
            when (result){
                is CommunicationResult.CommunicationError -> petsUIState.value = UiState(loading = false, null,
                    ListOfPetsErrors(R.string.no_internet_connection))
                is CommunicationResult.Error -> petsUIState.value = UiState(loading = false, null,
                    ListOfPetsErrors(R.string.failed_to_load_the_list))
                is CommunicationResult.Exception ->
                    petsUIState.value = UiState(loading = false, null,
                        ListOfPetsErrors(R.string.unknown_error)
                    )
                is CommunicationResult.Success ->
                    petsUIState.value = UiState(loading = false, result.data, null)
            }
        }
    }
}