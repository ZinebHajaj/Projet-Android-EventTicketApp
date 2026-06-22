package com.dcc.eventticketapp.ui.organizer.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.ui.organizer.OrganizerIntent
import com.dcc.eventticketapp.ui.organizer.OrganizerViewModel
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    event: EventModel,
    onBack: () -> Unit,
    viewModel: OrganizerViewModel = hiltViewModel()
) {
    // Charger l'événement dans le formulaire
    LaunchedEffect(Unit) {
        viewModel.handleIntent(OrganizerIntent.SelectEvent(event))
    }

    val state by viewModel.state.collectAsState()

    // Launcher pour sélectionner une image
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.handleIntent(OrganizerIntent.ImageUrlChanged(it.toString()))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modifier l'événement") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.handleIntent(OrganizerIntent.ClearSelection)
                        onBack()
                    }) {
                        Icon(Icons.Outlined.ArrowBack, "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ===== SÉLECTION D'IMAGE =====
            ImagePickerCard(
                selectedImageUrl = state.formImageUrl,
                onPickImage = { imagePicker.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===== TITRE =====
            FormTextField(
                value = state.formTitle,
                onValueChange = { viewModel.handleIntent(OrganizerIntent.TitleChanged(it)) },
                label = "Titre de l'événement",
                icon = Icons.Outlined.Event,
                placeholder = "Ex: Concert Jazz Night"
            )
            Spacer(modifier = Modifier.height(12.dp))

            // ===== VILLE =====
            FormTextField(
                value = state.formCity,
                onValueChange = { viewModel.handleIntent(OrganizerIntent.CityChanged(it)) },
                label = "Ville",
                icon = Icons.Outlined.LocationOn,
                placeholder = "Ex: Casablanca"
            )
            Spacer(modifier = Modifier.height(12.dp))

            // ===== DATE =====
            FormTextField(
                value = state.formDate,
                onValueChange = { viewModel.handleIntent(OrganizerIntent.DateChanged(it)) },
                label = "Date",
                icon = Icons.Outlined.CalendarToday,
                placeholder = "Ex: 15 Juin 2025"
            )
            Spacer(modifier = Modifier.height(12.dp))

            // ===== PRIX =====
            FormTextField(
                value = state.formPrice,
                onValueChange = { viewModel.handleIntent(OrganizerIntent.PriceChanged(it)) },
                label = "Prix standard (MAD)",
                icon = Icons.Outlined.AttachMoney,
                placeholder = "Ex: 150",
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(20.dp))

            // ===== CATÉGORIE =====
            CategorySelector(
                selectedCategory = state.formCategory,
                onCategorySelected = { viewModel.handleIntent(OrganizerIntent.CategoryChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ===== MESSAGES =====
            if (state.error != null) {
                ErrorCard(message = state.error!!)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.successMessage != null) {
                SuccessCard(message = state.successMessage!!)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ===== BOUTONS ACTIONS =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bouton Annuler
                OutlinedButton(
                    onClick = {
                        viewModel.handleIntent(OrganizerIntent.ClearSelection)
                        onBack()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Annuler")
                }

                // Bouton Mettre à jour
                Button(
                    onClick = {
                        val updatedEvent = viewModel.buildEventFromForm()
                        viewModel.handleIntent(OrganizerIntent.UpdateEvent(updatedEvent))
                    },
                    enabled = state.isFormValid && !state.isLoading,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Outlined.Save, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mettre à jour")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ===== COMPOSANTS RÉUTILISÉS (mêmes que CreateEventScreen) =====

@Composable
private fun ImagePickerCard(
    selectedImageUrl: String,
    onPickImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onPickImage() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUrl),
                    contentDescription = "Image événement",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Edit, null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Text("Changer l'image", color = Color.White, fontSize = 14.sp)
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.AddPhotoAlternate, null, tint = TextGrayMode, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ajouter une image", color = TextGrayMode, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    placeholder: String = "",
    keyboardType: androidx.compose.ui.text.input.KeyboardType = androidx.compose.ui.text.input.KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = TextGrayMode) },
        leadingIcon = { Icon(icon, null, tint = OrangeMain) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrangeMain,
            focusedLabelColor = OrangeMain,
            cursorColor = OrangeMain
        ),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
private fun CategorySelector(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Text("Catégorie", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

    val categories = listOf(
        "Concerts" to Icons.Outlined.MusicNote,
        "Sports" to Icons.Outlined.Sports,
        "Théâtre" to Icons.Outlined.TheaterComedy,
        "Ateliers" to Icons.Outlined.School,
        "Autres" to Icons.Outlined.MoreHoriz
    )

    categories.forEach { (cat, icon) ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onCategorySelected(cat) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedCategory == cat) OrangeMain.copy(alpha = 0.1f) else Color(0xFFF5F5F5)
            ),
            border = if (selectedCategory == cat) {
                CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(OrangeMain)
                )
            } else null
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, null, tint = if (selectedCategory == cat) OrangeMain else TextGrayMode, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    cat,
                    fontSize = 16.sp,
                    color = if (selectedCategory == cat) OrangeMain else Color.Black,
                    fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.weight(1f))
                if (selectedCategory == cat) {
                    Icon(Icons.Outlined.CheckCircle, null, tint = OrangeMain)
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(12.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SuccessCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = message, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
        }
    }
}