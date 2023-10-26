package cz.mendelu.pef.petstore.ui.screens.login

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.user.UsersRemoteRepositoryImpl
import cz.mendelu.pef.petstore.datastore.DataStoreRepositoryImpl
import cz.mendelu.pef.petstore.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val usersRemoteRepositoryImpl: UsersRemoteRepositoryImpl,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
) : BaseViewModel(), LoginScreenActions {

    init {
        launch {
            loginSuccessful.value = dataStoreRepositoryImpl.getLoginSuccessful()
        }
    }

    val loginUIState: MutableState<UiState<Nothing, LoginErrors>>
            = mutableStateOf(UiState(loading = false))

    val loginSuccessful: MutableState<Boolean?> = mutableStateOf(null)

    override fun login(username: String, password: String) {
        if (!isUserNameValid(username) || !isPasswordValid(password)) {
            loginUIState.value = UiState(
                loading = false,
                data = null,
                errors = LoginErrors(
                    communicationError = null,
                    usernameError = if (!isUserNameValid(username)) "Invalid email format" else null,
                    passwordError = if (!isPasswordValid(password)) "Password is less than 7 characters" else null
                )
            )
        } else {
            launch {
                loginUIState.value = UiState()

                val result = withContext(Dispatchers.IO) {
                    usersRemoteRepositoryImpl.loginUser(username, password)
                }

                when (result) {
                    is CommunicationResult.CommunicationError ->
                        loginUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = LoginErrors(
                                communicationError = R.string.no_internet_connection
                            )
                        )
                    is CommunicationResult.Error ->
                        loginUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = LoginErrors(
                                communicationError = R.string.login_failed
                            )
                        )
                    is CommunicationResult.Exception ->
                        loginUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = LoginErrors(
                                communicationError = R.string.unknown_error
                            )
                        )
                    is CommunicationResult.Success -> {
                        dataStoreRepositoryImpl.setLoginSuccessful(true)
                        loginSuccessful.value = true
                    }
                }
            }
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }
}