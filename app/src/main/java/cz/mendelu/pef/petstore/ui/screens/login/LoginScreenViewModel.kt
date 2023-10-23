package cz.mendelu.pef.petstore.ui.screens.login

import android.util.Patterns
import cz.mendelu.pef.petstore.architecture.BaseViewModel

class LoginScreenViewModel : BaseViewModel(), LoginScreenActions {


    override fun login(username: String, password: String) {
        if(!isUserNameValid(username) || !isPasswordValid(password)){

        } else {

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