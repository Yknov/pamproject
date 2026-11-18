package yk.example.pamproject.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    CanteenScaffold("Profil", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = uiState.initial,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            PageIntro("Akun CanteenKu", uiState.email)
            Spacer(Modifier.height(24.dp))
            InfoPanel(
                "Profil pengguna",
                "Informasi akun dan foto profil dapat dikelola dari halaman ini.",
            )
            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                InfoPanel("Gagal memuat profil", uiState.errorMessage)
            }
            Spacer(Modifier.height(20.dp))
            PrimaryAction("Edit profil", onEditProfile)
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text("Keluar")
            }
        }
    }
}

@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    CanteenScaffold("Edit profil", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            PageIntro("Informasi pribadi", "Perbarui informasi akun CanteenKu.")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "Foto profil",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        "Pemilihan foto tersedia setelah upload diintegrasikan.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nama lengkap") },
                singleLine = true,
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true,
                readOnly = true,
            )
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = onPhoneChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nomor telepon") },
                singleLine = true,
            )
            if (uiState.errorMessage != null) {
                InfoPanel("Gagal menyimpan", uiState.errorMessage)
            }
            Spacer(Modifier.weight(1f))
            PrimaryAction(
                if (uiState.isSaving) "Menyimpan..." else "Simpan perubahan",
                onSave,
            )
        }
    }
}

