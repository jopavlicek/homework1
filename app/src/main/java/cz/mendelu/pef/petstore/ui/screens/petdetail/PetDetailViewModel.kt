package cz.mendelu.pef.petstore.ui.screens.petdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.screens.listofpets.ListOfPetsErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val petsRemoteRepositoryImpl: PetsRemoteRepositoryImpl,
    ) : BaseViewModel()
{
    val petsUIState: MutableState<UiState<Pet, PetDetailErrors>>
        = mutableStateOf(UiState())

    val isInitialized: MutableState<Boolean> = mutableStateOf(false)
    val deleteResult: MutableState<Boolean?> = mutableStateOf(null)

    fun loadPet(id: Long) {
        launch {
            val result = withContext(Dispatchers.IO) {
                petsRemoteRepositoryImpl.findById(id)
            }

            when (result) {
                is CommunicationResult.CommunicationError ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = null,
                        errors = PetDetailErrors(
                            communicationError = R.string.no_internet_connection
                        )
                    )
                is CommunicationResult.Error ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = null,
                        errors = PetDetailErrors(
                            communicationError = R.string.failed_to_load_pet_details
                        )
                    )
                is CommunicationResult.Exception ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = null,
                        errors = PetDetailErrors(
                            communicationError = R.string.unknown_error
                        )
                    )
                is CommunicationResult.Success ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = result.data,
                        errors = null
                    )
            }
        }
    }

    fun deletePet(id: Long) {
        launch {
            petsUIState.value = UiState()

            val result = withContext(Dispatchers.IO) {
                petsRemoteRepositoryImpl.deletePet(id)
            }

            when (result) {
                is CommunicationResult.CommunicationError ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = null,
                        errors = PetDetailErrors(
                            communicationError = R.string.no_internet_connection
                        )
                    )
                is CommunicationResult.Error ->
                    deleteResult.value = false
//                    petsUIState.value = UiState(
//                        loading = false,
//                        data = null,
//                        errors = PetDetailErrors(
//                            communicationError = R.string.failed_to_delete_pet
//                        )
//                    )
                is CommunicationResult.Exception ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = null,
                        errors = PetDetailErrors(
                            communicationError = R.string.unknown_error
                        )
                    )
                is CommunicationResult.Success ->
                    deleteResult.value = true
            }
        }
    }
}