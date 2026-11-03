# Panduan kontribusi

## Tahap pengerjaan saat ini

Project ini khusus untuk **CanteenKu**, marketplace pra-pesan makanan dan minuman di kantin kampus multi-stand. Fokus tahap saat ini adalah struktur tampilan dan routing. Jangan menambahkan koneksi backend, Supabase, CRUD, atau logika bisnis sebelum diminta secara eksplisit.

## Batas domain dan scope

- Kerjakan hanya kebutuhan yang mendukung domain CanteenKu: melihat menu lintas stand, pemesanan takeaway/dine-in, status pembuatan pesanan, rating/ulasan, favorit/langganan, pengelolaan stand, serta saldo dan top-up kantin.
- Login/register dan profil merupakan fondasi bersama; keduanya bukan fitur utama pembagian anggota.
- Jangan menambahkan domain, role, alur, atau fitur produk lain yang tidak tercantum di README atau tidak diminta pengguna.
- Jika permintaan ambigu atau tampak di luar domain CanteenKu, jangan mengasumsikan kebutuhan baru. Tanyakan konfirmasi sebelum mengubah struktur atau kode.
- Implementasikan hanya fitur yang ditugaskan/diminta. Jangan mengerjakan atau mengubah folder fitur anggota lain tanpa instruksi dan koordinasi.
- Hindari refactor, dependency, backend, pembayaran sungguhan, atau integrasi layanan eksternal yang tidak diperlukan oleh permintaan aktif.
- Pertahankan nama aplikasi **CanteenKu**. Jangan mengganti application ID atau namespace tanpa permintaan eksplisit.

## Aturan struktur

- Letakkan UI berdasarkan fitur di `app/src/main/java/yk/example/pamproject/ui/`.
- Layar fondasi login/register berada di `ui/auth/`; layar profil berada di `ui/profile/`.
- Fitur bisnis masing-masing memiliki folder sendiri. Kelompokkan halaman dengan subfolder `list/`, `detail/`, dan `form/` bila alurnya sesuai.
- Komponen yang hanya dipakai satu halaman tetap dekat dengan halaman tersebut.
- Komponen yang dipakai beberapa halaman dalam satu fitur berada di `components/` fitur itu.
- Komponen lintas fitur berada di `ui/components/`.
- Routing aplikasi dikelola terpusat di `navigation/`; hindari membuat NavHost terpisah tanpa kebutuhan yang jelas.
- Jika mulai mengimplementasikan logic, pisahkan per fitur: `domain/<fitur>/` untuk model, kontrak repository, dan use case; `data/<fitur>/` untuk sumber data, mapper, dan implementasi repository.
- ViewModel dan state UI diletakkan dekat halaman/fitur di `ui/<fitur>/`; jangan meletakkan query Supabase langsung di Screen atau ViewModel.
- Konfigurasi client dan sumber data Supabase bersama berada di `data/remote/supabase/`. Operasi CRUD tetap dikelompokkan berdasarkan fitur.
- Buat use case hanya jika ada aturan bisnis yang perlu dipisah; jangan menambah lapisan tanpa kebutuhan.
- Folder resource Android berada di `app/src/main/res/`. Jangan membuat subfolder bebas dalam `res/drawable/`; gunakan prefix nama file seperti `catalog_` atau `profile_`.
- Folder kerangka kosong dapat memakai `.gitkeep` agar tercatat di Git. Hapus `.gitkeep` saat folder mulai memiliki file.

## Aturan kerja tim

- Satu anggota bertanggung jawab atas satu fitur utama yang disepakati kelompok.
- Jumlah fitur utama yang dikerjakan harus sama dengan jumlah anggota. Auth dan profile adalah fondasi bersama, bukan fitur utama pembagian.
- Sebelum mengubah route bersama atau file tema, koordinasikan dengan anggota lain untuk menghindari konflik merge.
- Gunakan nama file dan folder Kotlin yang konsisten, berbahasa Inggris, dan deskriptif.
- Jangan mengubah atau menghapus pekerjaan anggota lain tanpa koordinasi.
- Sebelum mulai mengerjakan fitur, pastikan scope dan folder fitur yang diminta; jaga perubahan tetap terbatas pada scope tersebut.
