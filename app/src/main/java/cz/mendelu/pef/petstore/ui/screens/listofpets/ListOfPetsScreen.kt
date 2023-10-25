package cz.mendelu.pef.petstore.ui.screens.listofpets

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
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
import cz.mendelu.pef.petstore.ui.screens.destinations.ListOfPetsScreenDestination
import cz.mendelu.pef.petstore.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.petstore.ui.screens.destinations.NewPetScreenDestination
import cz.mendelu.pef.petstore.ui.screens.destinations.PetDetailScreenDestination
import cz.mendelu.pef.petstore.ui.theme.basicMargin
import cz.mendelu.pef.petstore.ui.theme.halfMargin
import cz.mendelu.pef.petstore.ui.theme.smallMargin

@Destination
@Composable
fun ListOfPetsScreen(
    navigator: DestinationsNavigator,
    petDetailRecipient: ResultRecipient<PetDetailScreenDestination, Boolean>,
    newPetRecipient: ResultRecipient<NewPetScreenDestination, Boolean>,
) {
    val viewModel = hiltViewModel<ListOfPetsViewModel>()
    val uiState: MutableState<UiState<List<Pet>,ListOfPetsErrors>>
        = rememberSaveable { mutableStateOf(UiState()) }

    viewModel.petsUIState.value.let {
        uiState.value = it
    }

    viewModel.logoutSuccess.value.let {
        if (it) {
            navigator.popBackStack()
            navigator.navigate(LoginScreenDestination)
        }
    }

    // refresh list after delete action on pet detail screen
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

    // refresh list after create action on new pet screen
    newPetRecipient.onNavResult { result ->
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
        topBarText = "Pets",
        drawFullScreenContent = true,
        showLoading = uiState.value.loading,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigator.navigate(NewPetScreenDestination)
            }) {
                Icon(Icons.Filled.Add, null)
            }
        },
        actions = {
            IconButton(
                enabled = !uiState.value.loading,
                onClick = {
                    viewModel.refreshList()
                },
            ) {
                Icon(Icons.Filled.Refresh, null)
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(LocalContext.current)
            builder
                .setMessage("Are you sure you want to sign out?")
                .setTitle("Sign out?")
                .setPositiveButton("Yes") { dialog, which ->
                    viewModel.logout()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }

            IconButton(
                onClick = {
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                },
            ) {
                Icon(Icons.Filled.Logout, null)
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = halfMargin())
        ) {
            val photoAvailable: Boolean

            if (pet.photoUrls == null) {
                photoAvailable = false
            } else {
                photoAvailable = pet.photoUrls!!.isNotEmpty()
            }

            SubcomposeAsyncImage(
                model = if (photoAvailable) pet.photoUrls!!.first() else null,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(halfMargin())
                    .size(50.dp)
                    .clip(CircleShape)
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Success) {
                    SubcomposeAsyncImageContent()
                } else {
                    PetImagePlaceholder()
                }
            }

            Column(
                modifier = Modifier
                    .height(65.dp)
                    .fillMaxWidth()
                    .padding(horizontal = smallMargin()),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (pet.name.isNullOrEmpty()) "Anonymous Pet"
                        else pet.name!!.replaceFirstChar(Char::titlecase),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PetImagePlaceholder() {
    Box(
        modifier = Modifier
            .size(size = 50.dp)
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.background, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Filled.Pets,
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )
    }
}
