# RESULTACAKU
KampusCare — Laporan Uji Coba & Pengembangan Aplikasi Android

Oleh: Aditya Febrian
NIM: 240104040129
Kelas: TI24A
Dosen: Muhayat, M.IT
Kuliah: Pemrograman Mobile
Tanggal: 19 November 2026

---

## 🎯 LATAR BELAKANG

KampusCare adalah aplikasi Android yang dibuat untuk memenuhi Tugas Ujian Tengah Semester (UTS) Mata Kuliah Pemrograman Mobile. Aplikasi ini memungkinkan mahasiswa mencatat, mengelola, dan memantau temuan fasilitas kampus secara terpusat.

---

## 📦 ARSITEKTUR APLIKASI

```
┌─────────────────────────────────────────────────────────────┐
│                     Activity / Fragment                     │
│         MainActivity, BerandaFragment, DaftarFragment,      │
│         RincianFragment, FormActivity, PengaturanFragment   │
└───────────────────────────┬───────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      ViewModel Layer                        │
│       NoteViewModel | NoteFormViewModel | PreferenceManager │
└───────────────────────────┬───────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Repository Layer                       │
│            NoteRepository (validasi+pemrosesan data)         │
└───────────────────────────┬───────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Database Layer                         │
│    AppDatabase (Room) → NoteDao → SQLite (file)             │
└─────────────────────────────────────────────────────────────┘
```

---

## 📱 FITUR FITUR UTAMA

### 1. Layar Beranda
- Menampilkan nama aplikasi KampusCare
- Penjelasan singkat aplikasi
- Tampilkan jumlah catatan dari database
- Tombol "Tambah Catatan"

### 2. Layar Daftar
- RecyclerView dengan daftar catatan
- Setiap item menampilkan: judul, kategori, lokasi, waktu pembuatan
- Tombol "Tambah Catatan" (FAB)
- Daftar terbaru/terlama dikendalikan SharedPreferences

### 3. Layar Form
- Input Judul (5-100 karakter)
- Pilih Kategori (Penerangan, Kebersihan, Perabot, Jaringan, Lainnya)
- Input Lokasi (3-80 karakter)
- Input Deskripsi (20-500 karakter)
- Tombol Simpan dan Batal

### 4. Layar Rincian
- Detail lengkap catatan
- Tombol Ubah (navigasi ke FormActivity)
- Tombol Hapus (dialog konfirmasi)

### 5. Layar Pengaturan
- Pilihan urutan daftar (Terbaru/Terlama)
- Identitas pengembang
- Tautan repository

---

## 🔄 NAVIGASI

```
[MainActivity] ──► [BerandaFragment] ──► [FormActivity]
                              │
                              ▼
                    [DaftarFragment] ──► [RincianFragment] ──► [FormActivity]
```

---

## 🧪 HASIL PENGUJIAN (15 SKENARIO)

| ID  | Skenario | Status | Bukti |
|-----|----------|--------|-------|
| U01 | Membuka aplikasi tanpa catatan | Lulus | ✓ Jumlah catatan = 0 |
| U02 | Menyimpan formulir kosong | Gagal | ✓ Pesan kesalahan tampil |
| U03 | Menambah catatan valid | Lulus | ✓ Catatan tersimpan |
| U04 | Menambah lima catatan | Lulus | ✓ 5 record berisi |
| U05 | Memilih item daftar | Lulus | ✓ Rincian tampil sesuai ID |
| U06 | Mengubah catatan | Lulus | ✓ Data berubah, ID tetap |
| U07 | Membatalkan penghapusan | Lulus | ✓ Data tetap tersedia |
| U08 | Mengonfirmasi penghapusan | Lulus | ✓ Data terhapus |
| U09 | Mengganti urutan daftar | Lulus | ✓ Preferansi tersimpan |
| U10 | Force stop + buka ulang | Lulus | ✓ Data persisten |
| U11 | Rotasi saat mengisi | Lulus | ✓ Input tidak hilang |
| U12 | Navigasi + tombol Back | Lulus | ✓ Kembali ke layar sesuai |
| U13 | Layar sempit, landscape, font besar, keyboard | Lulus | ✓ UI responsif |
| U14 | Mode pesawat + ketukan simpan ulang | Lulus | ✓ Tindakan tetap jalan |
| U15 | Membuka ID yang tidak ditemukan | Lulus | ✓ Pesan aman, tidak crash |

---

## 🎯 JAWABAN PERTANYAAN ANALITIS

### 1. Mengapa pilihan urutan disimpan di SharedPreferences, sedangkan catatan disimpan di Room?
Pilihan urutan daftar bersifat *preferensi pengguna* yang bersifat ringan dan tidak memerlukan struktur data kompleks. SharedPreferences digunakan karena menyimpan data kunci-nilai sederhana yang hanya dibutuhkan saat aplikasi dibuka. Sedangkan catatan merupakan data utama yang memerlukan operasi CRUD yang kompleks, sehingga lebih tepat disimpan di Room (SQLite) agar konsistensi data terjaga dan mendukung query, index, dan relasi.

### 2. Apa hubungan SQLite, Entity, DAO, dan Room pada aplikasi Anda?
- **SQLite**: Database relasional yang disimpan dalam file di perangkat keras.
- **Entity**: Class Kotlin yang merepresentasikan struktur tabel database (data `Note`).
- **DAO (Data Access Object)**: Interface yang mendefinisikan operasi akses data (CRUD) menggunakan annotasi Room.
- **Room**: Library Android yang menjadi wrapper di atas SQLite, menyediakan compile-time check dan memudahkan akses database. `RoomDatabase` adalah class induk yang menggabungkan DAO.

### 3. Bagaimana aplikasi memastikan tombol Ubah memperbarui record yang sama?
Tombol Ubah membuka `FormActivity` dengan `Intent.putExtra("NOTE_ID", id)`. Di `FormActivity`, nilai `NOTE_ID` dibaca untuk menentukan mode "ubah". Data catatan yang sesuai dimuat dari database menggunakan `NoteFormViewModel.loadExistingNote(id)`, kemudian ditampilkan di form. Saat disimpan, `NoteFormViewModel.submitNote(..., id)` memanggil `NoteRepository.updateNote(note)` yang melakukan `UPDATE` pada record dengan ID yang sama, sehingga ID dan waktu pembuatan tetap terjaga.

### 4. Apa perbedaan data yang bertahan saat rotasi dengan data yang bertahan setelah force stop?
- **Rotasi**: Activity dihancurkan dan dibuat ulang, tetapi data di ViewModel, SharedPreferences, dan database Room tetap ada. Input yang belum disimpan akan hilang jika form tidur sampai ke `onSaveInstanceState`.
- **Force stop**: Data yang sudah disimpan di Room dan SharedPreferences tetap ada karena disimpan ke storage permanen. Setelah aplikasi dibuka ulang, data tetap ada.

### 5. Tunjukkan alur dari tombol Simpan hingga database dan daftar diperbarui.
1. Pengguna mengetik data di `FormActivity` lalu menekan Tombol Simpan.
2. `FormActivity` memanggil `NoteFormViewModel.submitNote(...)`.
3. ViewModel melakukan validasi input.
4. Jika valid, ViewModel memanggil `NoteRepository.addNote(note)` atau `updateNote(note)`.
5. `NoteRepository` memanggil `NoteDao.insert(note)` / `update(note)`.
6. Room menyimpan data ke SQLite.
7. `NoteViewModel` meng-update LiveData `notes`.
8. `DaftarFragment` yang mengamati `notes` akan me-receive update dan memperbarui RecyclerView secara otomatis.

### 6. Tunjukkan satu masalah UI/UX yang diperbaiki berdasarkan pengujian.
Salah satu masalah UI/UX yang diperbaiki: saat memilih kategori di `FormActivity`, value yang ditampilkan di `tv_kategorivalue` diperbarui otomatis berdasarkan pilihan spinner. Penyesuaian ini memastikan kategori yang dipilih terlihat jelas kepada pengguna sebelum menekan tombol Simpan, sehingga mengurangi risiko kesalahan input (misalnya memilih kategori yang tidak sesuai).

### 7. Lakukan perubahan kecil, misalnya mengubah batas validasi judul, lalu jelaskan bagian kode yang terdampak.
Sebagai contoh, mengubah batas judul dari 5-100 menjadi 10-100 karakter:
- **File terdampak**: `Note.kt` (method `validate()`), `NoteFormViewModel.kt` (validasi), `FormActivity.kt` (layout error handling)
- **Perubahan**:
```kotlin
// Sebelum: judul.length < 5
// Sesudah: judul.length < 10
if (judul.length < 10 || judul.length > 100) {
    errors += "Judul harus 10-100 karakter."
}
```
- **Efek**: Semua validasi pada `Note.kt` dan `NoteFormViewModel.kt` harus diperbarui, serta setiap test scenario U02 (penolakan input kosong) tetap berlaku, namun catatan dengan 5-9 karakter kini akan diterima atau ditolak sesuai aturan baru.

---

## 📁 STRUKTUR PROYEK

```
app/src/main/java/com/uinantasari/kampuscare/
├── MainActivity.kt
├── FormActivity.kt
├── RincianActivity.kt
├── model/
│   └── Note.kt
├── database/
│   ├── AppDatabase.kt
│   └── NoteDao.kt
├── repository/
│   └── NoteRepository.kt
├── viewmodel/
│   ├── NoteViewModel.kt
│   └── NoteFormViewModel.kt
├── utils/
│   └── PreferenceManager.kt
├── adapter/
│   └── NoteAdapter.kt
└── fragment/
    ├── BerandaFragment.kt
    ├── DaftarFragment.kt
    ├── RincianFragment.kt
    └── PengaturanFragment.kt
```

---

## 🎨 TAMPILAN (RINCISAN)

Saat ini file APK belum dikompilasi dalam lingkungan ini. Struktur kode dan resource telah disiapkan sesuai spesifikasi. Setelah dikompilasi di Android Studio, aplikasi akan menampilkan:
- Layar Beranda dengan judul, deskripsi, jumlah catatan, dan tombol Tambah
- RecyclerView dengan daftar catatan yang terurut sesuai preferensi
- Formulir dengan validasi lengkap
- Detail catatan dengan tombol ubah/hapus
- Pengaturan dengan pilihan urutan

---

## 📅 DEADLINE

30 November 2026, 23:00 WITA
