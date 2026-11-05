# CanteenKu

**Nama aplikasi (rencana):** CanteenKu  
**Kategori:** Marketplace katalog menu & ulasan makanan kantin multi-stand

CanteenKu adalah platform informasi katalog makanan dan minuman di area kantin kampus untuk membantu pengguna menjelajahi menu dari berbagai stand serta melihat maupun membagikan rating dan ulasan makanan.

Aplikasi dirancang untuk dua role, yaitu **user** dan **admin**. User menggunakan aplikasi untuk menjelajahi katalog menu dan memberikan ulasan, sedangkan admin mengelola katalog menu dan memantau ulasan melalui dashboard sesuai hak aksesnya.

## Kontribusi Anggota

Tabel berikut mencatat pembagian fitur utama yang menjadi tanggung jawab anggota.

| No. | NIM | Nama Lengkap | Fitur Utama yang Ditangani |
|---:|---|---|---|
| 1 | 245150401111010 | Cantika Putri Permatasari | Modul Katalog Menu Makanan & Upload Gambar |
| 2 | 245150407111012 | Achmad Hasbil Wafi Rahmawan | Modul Rating & Ulasan Makanan |

## Spesifikasi Project

| Komponen | Versi / konfigurasi |
|---|---|
| Bahasa | Kotlin 2.2.10 |
| UI | Jetpack Compose, Material 3 |
| Android Gradle Plugin | 9.4.1 |
| Gradle Wrapper | 9.6.0 |
| Compile SDK | Android API 37 |
| Target SDK | Android API 37 |
| Minimum SDK | Android API 24 |
| Java source/target compatibility | Java 11 |
| JVM untuk menjalankan Gradle | JDK 17 atau lebih baru |
| Compose BOM | 2026.02.01 |
| Application ID | `yk.example.pamproject` |

Versi dependency dikelola melalui `gradle/libs.versions.toml`. Backend dan integrasi Supabase belum dikonfigurasi.

## Struktur Project

```text
pamproject/
├── AGENTS.md                                      # Panduan kontribusi dan aturan struktur
├── README.md                                      # Deskripsi, spesifikasi, struktur, dan cara menjalankan project
├── app/
│   ├── build.gradle.kts                           # Konfigurasi module Android app
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml                # Deklarasi aplikasi dan Activity
│       │   ├── java/yk/example/pamproject/
│       │   │   ├── MainActivity.kt                # Activity utama; entry point UI Compose
│       │   │   ├── navigation/                    # Route/NavHost aplikasi
│       │   │   ├── data/                          # Sumber data dan implementasi repository (kerangka)
│       │   │   │   ├── remote/supabase/           # Lokasi integrasi Supabase mendatang
│       │   │   │   ├── auth/                      # Data autentikasi
│       │   │   │   ├── profile/                   # Data profil
│       │   │   │   ├── catalog/                   # Data katalog/menu
│       │   │   │   └── reviews/                   # Data rating/ulasan
│       │   │   ├── domain/                        # Model dan kontrak bisnis (kerangka)
│       │   │   │   ├── auth/{model,repository,usecase}/
│       │   │   │   ├── profile/{model,repository,usecase}/
│       │   │   │   ├── catalog/{model,repository,usecase}/
│       │   │   │   └── reviews/{model,repository,usecase}/
│       │   │   └── ui/                            # Tampilan dikelompokkan per fitur
│       │   │       ├── theme/                     # Warna, typography, dan tema Compose
│       │   │       ├── components/                # Komponen UI lintas fitur
│       │   │       ├── auth/                      # Login dan register
│       │   │       ├── profile/                   # Profil dan edit profil
│       │   │       ├── home/{user,admin}/         # Beranda dan dashboard berdasarkan role
│       │   │       ├── catalog/{list,detail,form,components}/
│       │   │       └── reviews/{list,detail,form,components}/
│       │   └── res/                               # Resource Android
│       │       ├── drawable/                      # Gambar statis dan drawable
│       │       ├── mipmap-*/                      # Ikon launcher berbagai ukuran
│       │       ├── values/                        # String, warna, tema XML
│       │       └── xml/                           # Backup/data extraction rules
│       ├── test/                                  # Unit test
│       └── androidTest/                           # Instrumented test Android
├── gradle/
│   ├── libs.versions.toml                         # Versi plugin dan dependency
│   └── wrapper/                                   # Gradle Wrapper
├── build.gradle.kts                               # Konfigurasi Gradle root
└── settings.gradle.kts                            # Daftar module project
```

Folder `data`, `domain`, dan subfolder yang belum memiliki file berisi `.gitkeep`. Saat logic mulai dibuat, repository implementation berada di `data`, kontrak repository/model berada di `domain`, sedangkan ViewModel dan state UI diletakkan dekat tampilan fitur terkait.

## Asset

Letakkan gambar statis di `app/src/main/res/drawable/`. Resource Android tidak mendukung pengelompokan subfolder bebas di dalam `drawable`, jadi gunakan nama berawalan fitur, contohnya `catalog_sample_menu.webp` atau `profile_default_avatar.webp`.

## Menjalankan project

Buka folder project ini di Android Studio, lakukan Gradle Sync, lalu jalankan konfigurasi `app` pada perangkat Android yang terhubung. Dari terminal:

```bash
./gradlew assembleDebug
```

Untuk install, perangkat Android harus terdeteksi oleh `adb devices`, kemudian jalankan:

```bash
./gradlew installDebug
```
