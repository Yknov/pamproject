package yk.example.pamproject.ui.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction
import yk.example.pamproject.ui.components.StatusLabel

data class DemoReview(val dish: String, val author: String, val stars: Int, val comment: String)
val demoReviews = listOf(
    DemoReview("Nasi ayam sambal matah", "Pengguna CanteenKu", 5, "Sambalnya segar dan porsinya pas."),
    DemoReview("Mie ayam jamur", "Mahasiswa", 4, "Rasanya enak, antreannya cukup cepat."),
)

fun reviewsForMenu(menuName: String): List<DemoReview> = demoReviews.filter { it.dish == menuName }

@Composable
fun ReviewListScreen(
    menuName: String,
    navItems: List<CanteenNavItem>,
    selectedDestination: yk.example.pamproject.navigation.AppDestination?,
    onNavigate: (yk.example.pamproject.navigation.AppDestination) -> Unit,
    onBack: () -> Unit,
    onOpenReview: (String) -> Unit,
    onWriteReview: () -> Unit,
) {
    CanteenScaffold(
        "Rating & ulasan",
        navItems = navItems,
        selectedDestination = selectedDestination,
        onNavigate = onNavigate,
        onBack = onBack,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro("Ulasan menu", "$menuName · contoh tampilan") }
            item { PrimaryAction("Tulis ulasan", onWriteReview) }
            items(reviewsForMenu(menuName)) { review ->
                Surface(
                    onClick = { onOpenReview(review.dish) },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 1.dp,
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(review.dish, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            StatusLabel("★ ${review.stars}/5")
                        }
                        Text(review.author, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(review.comment, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            if (reviewsForMenu(menuName).isEmpty()) {
                item { InfoPanel("Belum ada ulasan", "Jadilah yang pertama berbagi pengalaman untuk menu ini.") }
            }
            item { InfoPanel("Bagikan pengalaman", "Ulasan hanya dapat dikirim untuk pesanan yang sudah selesai.") }
        }
    }
}

@Composable
fun ReviewDetailScreen(dish: String, onBack: () -> Unit, onEdit: () -> Unit) {
    val review = demoReviews.firstOrNull { it.dish == dish } ?: demoReviews.first()
    CanteenScaffold("Detail ulasan", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PageIntro(review.dish, "Ulasan contoh dari ${review.author}.")
            Text("★".repeat(review.stars), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(review.comment, style = MaterialTheme.typography.bodyLarge)
            InfoPanel("Tentang ulasan", "Ulasan membantu pengguna lain memilih menu kantin.")
            PrimaryAction("Edit ulasan", onEdit)
        }
    }
}

@Composable
fun ReviewFormScreen(dish: String, onBack: () -> Unit, onSave: () -> Unit) {
    var rating by rememberSaveable { mutableStateOf(5) }
    var comment by rememberSaveable { mutableStateOf("") }
    CanteenScaffold("Tulis ulasan", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PageIntro("Bagaimana rasanya?", dish)
            Text("Rating", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { value ->
                    FilterChip(selected = rating == value, onClick = { rating = value }, label = { Text("★ $value") })
                }
            }
            OutlinedTextField(comment, { comment = it }, Modifier.fillMaxWidth(), label = { Text("Tulis ulasan") }, minLines = 4)
            InfoPanel("Ulasan yang baik", "Jelaskan rasa, porsi, dan pengalaman mengambil pesanan dengan sopan.")
            PrimaryAction("Kirim ulasan", onSave)
        }
    }
}
