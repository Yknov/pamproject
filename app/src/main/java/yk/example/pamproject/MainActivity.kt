package yk.example.pamproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import yk.example.pamproject.ui.canteen_pay.balance.BalanceRoute
import yk.example.pamproject.ui.theme.PamprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PamprojectTheme {
                // Sementara langsung menampilkan layar saldo CanteenPay.
                // Ganti dengan NavHost terpusat di navigation/ saat routing sudah tersedia.
                BalanceRoute()
            }
        }
    }
}
