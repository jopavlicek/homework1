package cz.mendelu.pef.petstore.ui.screens.petdetail

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.ui.theme.basicMargin
import cz.mendelu.pef.petstore.ui.theme.basicTextColor
import cz.mendelu.pef.petstore.ui.theme.smallMargin

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
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.pet_delete_ok), Toast.LENGTH_SHORT
            ).show()
        } else if (it == false) {
            resultNavigator.navigateBack(result = true)
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.pet_delete_fail), Toast.LENGTH_LONG
            ).show()
        }
    }

    BaseScreen(
        topBarText = "Pet Detail",
        drawFullScreenContent = true,
        showLoading = uiState.value.loading,
        onBackClick = {
            navigator.popBackStack()
        },
        actions = {
            val builder: AlertDialog.Builder = AlertDialog.Builder(LocalContext.current)
            builder
                .setMessage("Are you sure you want to delete this pet?")
                .setTitle("Delete?")
                .setPositiveButton("Yes") { dialog, which ->
                    viewModel.deletePet(id = id)
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }

            IconButton(
                enabled = !uiState.value.loading && uiState.value.errors == null,
                onClick = {
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
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
                pet = uiState.value.data
            )
        }
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    pet: Pet?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(all = basicMargin())
    ) {
        if (pet != null) {
            Text(
                text = pet.status?.replaceFirstChar(Char::titlecase) ?: "Unknown status",
                color = basicTextColor(),
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = if (pet.name.isNullOrEmpty()) "Anonymous Pet"
                    else pet.name!!.replaceFirstChar(Char::titlecase),
                color = basicTextColor(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = smallMargin()),
                 maxLines = 3,
                 overflow = TextOverflow.Ellipsis
            )

            val photoAvailable: Boolean

            if (pet.photoUrls == null) {
                photoAvailable = false
            } else {
                photoAvailable = pet.photoUrls!!.isNotEmpty()
            }

            if (photoAvailable) {
                pet.photoUrls!!.forEach {
                    SubcomposeAsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = basicMargin())
                            .clip(RoundedCornerShape(2))
                            .fillMaxWidth()
                    ) {
                        val state = painter.state
                        if (state is AsyncImagePainter.State.Success) {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        }
    }
}