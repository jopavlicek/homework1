package cz.mendelu.pef.petstore.ui.screens.newpet

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.Category
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.screens.petdetail.PetDetailErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewPetViewModel @Inject constructor(
    private val petsRemoteRepositoryImpl: PetsRemoteRepositoryImpl
) : BaseViewModel()
{

    val petsUIState: MutableState<UiState<Nothing, NewPetErrors>> = mutableStateOf(UiState(loading = false))

    val createResult: MutableState<Boolean?> = mutableStateOf(null)

    fun createPet(name: String) {
        launch {
            petsUIState.value = UiState()

            val newPet = Pet(
                id = null,
                name = name,
                status = "available",
                category = null,
                photoUrls = null,
                tags = null,
            )

            val result = withContext(Dispatchers.IO) {
                petsRemoteRepositoryImpl.createPet(newPet)
            }

            when (result) {
                is CommunicationResult.CommunicationError ->
                    petsUIState.value = UiState(
                        loading = false,
                        data = null,
                        errors = NewPetErrors(
                            communicationError = R.string.no_internet_connection
                        )
                    )
                is CommunicationResult.Error ->
                    createResult.value = false
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
                        errors = NewPetErrors(
                            communicationError = R.string.unknown_error
                        )
                    )
                is CommunicationResult.Success ->
                    createResult.value = true
            }
        }
    }

}