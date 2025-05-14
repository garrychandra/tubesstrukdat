import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    static Scanner input = new Scanner(System.in);
    static User loginUser = new User();
    static Double payLaterFee = 0.02;
    static HashMap<String, User> user = new HashMap<>();
    static HashMap<String, Produk> produks = new HashMap<>();
    static HashMap<String, Makanan> makanan = new HashMap<>();
    static HashMap<String, Produk> tagihan = new HashMap<>();
    static HashMap<String, Toko> toko = new HashMap<>();
    static HashMap<String, Stack<Riwayat>> riwayat = new HashMap<>();
    static HashMap<String, PayLater> paylater = new HashMap<>();
    static HashMap<String, ArrayDeque<SPinjam>> pinjam = new HashMap<>();
    public static void main(String[] args) {
        addDummy();
        if (login()) {
            menu();
        }
        input.close();
        System.exit(0);
    }

    public static Boolean login() {
        while (true) {
            System.out.println("Login");
            System.out.print("Masukkan username: ");
            String username = input.nextLine();
            System.out.print("Masukkan password: ");
            String password = input.nextLine();

            // Check if username exists
            if (user.containsKey(username)) {
                User u = user.get(username);
                if (u.password.equals(password)) {
                    System.out.println("Login berhasil. Selamat datang, " + u.name + "!");
                    loginUser = u;
                    return true;
                } else {
                    System.out.println("Password salah. Silakan coba lagi.\n");
                }
            } else {
                System.out.println("Username tidak ditemukan. Silakan coba lagi.\n");
            }
        }
    }


    public static void menu() {
        while (true) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Produk");
            System.out.println("2. Makanan");
            System.out.println("3. Utilities");
            System.out.println("4. SPinjam");
            System.out.println("5. Riwayat");
            System.out.println("6. Top up Shopee pay");
            System.out.println("7. Tagihan PayLater");
            System.out.println("8. Exit");
            System.out.print("Pilih menu: ");

            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1":
                    tampilkanProduk();
                    System.out.print("Masukkan kode produk yang ingin dibeli: ");
                    String kode = input.nextLine();
                    System.out.print("Masukkan jumlah yang ingin dibeli: ");
                    int jumlah = input.nextInt();
                    input.nextLine(); // Consume newline
                    beliProduk(kode, jumlah);
                    break;
                case "2":
                    tampilkanMakanan();
                    break;
                case "3":
                    tampilkanTagihan();
                    break;
                case "4":
                    tampilkanPinjaman();
                    break;
                case "5":
                    tampilkanRiwayat();
                    break;
                case "6":
                // Top up Shopee pay
                    break;
                case "7":
                    tampilkanPayLater();
                    break;
                case "8":
                    System.out.println("Keluar dari program. Terima kasih!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }

    public static void addUser(String username, String password, String phone, String address, double saldo,
            String name) {
        User u = new User();
        u.username = username;
        u.password = password;
        u.phoneNumber = phone;
        u.address = address;
        u.saldo = saldo;
        u.name = name;
        user.put(username, u);
    }

    public static void addToko(String kode, String nama, String alamat, String kodePos, String status) {
        Toko t = new Toko();
        t.kode = kode;
        t.nama = nama;
        t.alamat = alamat;
        t.kodePos = kodePos;
        t.status = status;
        toko.put(kode, t);
    }

    public static void addProduk(String kode, String nama, Double harga, int stok, int terjual, String tipe,
            String kodePenjual, Double ongkir) {
        Produk p = new Produk();
        p.kode = kode;
        p.nama = nama;
        p.harga = harga;
        p.stok = stok;
        p.banyakTerjual = terjual;
        p.tipe = tipe;
        p.kodePenjual = kodePenjual;
        p.ongkir = ongkir;
        produks.put(kode, p);
    }

    public static void addMakanan(String kode, String nama, Double harga, int stok, int terjual, String tipe,
            String kodePenjual, Double ongkir, String kurir, Date estimasi) {
        Makanan m = new Makanan();
        m.kode = kode;
        m.nama = nama;
        m.harga = harga;
        m.stok = stok;
        m.banyakTerjual = terjual;
        m.tipe = tipe;
        m.kodePenjual = kodePenjual;
        m.ongkir = ongkir;
        m.namaKurir = kurir;
        m.estimasiKedatangan = estimasi;
        makanan.put(kode, m);
    }

    public static void addPayLater(String userid, List<Double> tagihanList) {
        PayLater p = new PayLater();
        p.userid = userid;
        p.tagihan.addAll(tagihanList);
        paylater.put(userid, p);
    }

    public static void addPinjaman(String userid, SPinjam pinjaman) {
        if(!pinjam.containsKey(userid)) {
            pinjam.put(userid, new ArrayDeque<>());
        }
        pinjam.get(userid).add(pinjaman);
    }

    public static void addRiwayat(String userid, Riwayat entries) {
        if(!riwayat.containsKey(userid)) {
            riwayat.put(userid, new Stack<>());
        }
        riwayat.get(userid).push(entries);
    }

    public static void addTagihan(String kode, String nama, Double harga, int stok, int terjual,
            String tipe, String kodePenjual, Double ongkir) {
        Produk t = new Produk();
        t.kode = kode;
        t.nama = nama;
        t.harga = harga;
        t.stok = stok;
        t.banyakTerjual = terjual;
        t.tipe = tipe;
        t.kodePenjual = kodePenjual;
        t.ongkir = ongkir;
        tagihan.put(kode, t);
    }

    public static void addDummy(){
        // Add Users
        addUser("alice", "pass123", "081234567890", "Jl. Mawar 12", 100000.0, "Alice");
        addUser("bob", "bobpass", "082233445566", "Jl. Melati 21", 75000.0, "Bob");
        addUser("carol", "carol123", "083311122233", "Jl. Dahlia 7", 120000.0, "Carol");
        addUser("dave", "davepass", "085522334455", "Jl. Anggrek 9", 50000.0, "Dave");
        addUser("eve", "eve123", "087733344455", "Jl. Teratai 19", 95000.0, "Eve");

        // Add Stores
        addToko("T001", "Toko Sukses", "Jl. Kenanga 1", "40123", "Aktif");
        addToko("T002", "Toko Makmur", "Jl. Sakura 99", "40234", "Aktif");
        addToko("T003", "Mart Hebat", "Jl. Jaya 77", "40345", "Aktif");
        addToko("T004", "PLN", "", "", "Aktif");
        addToko("T005", "PAM", "", "", "Aktif");
        // Add Produk
        addProduk("P001", "Sabun Mandi", 10000.0, 50, 5, "Non-Makanan", "T001", 5000.0);
        addProduk("P002", "Shampoo", 15000.0, 40, 3, "Non-Makanan", "T002", 4000.0);
        addProduk("P003", "Pasta Gigi", 12000.0, 60, 7, "Non-Makanan", "T003", 3000.0);
        addProduk("P004", "Tissue", 8000.0, 70, 6, "Non-Makanan", "T001", 2500.0);

        // Add Makanan
        addMakanan("M001", "Nasi Goreng", 20000.0, 30, 10, "Makanan", "T001", 7000.0, "Kurir A", new Date());
        addMakanan("M002", "Bakso", 18000.0, 25, 8, "Makanan", "T002", 6000.0, "Kurir B", new Date());
        addMakanan("M003", "Ayam Geprek", 22000.0, 20, 12, "Makanan", "T003", 6500.0, "Kurir C", new Date());
        addMakanan("M004", "Sate Ayam", 25000.0, 15, 9, "Makanan", "T002", 8000.0, "Kurir D", new Date());

        // Add PayLater (Tagihan)
        addPayLater("alice", List.of(25000.0, 12000.0));
        addPayLater("bob", List.of(18000.0, 8000.0));
        addPayLater("carol", List.of(5000.0));
        addPayLater("dave", List.of(10000.0, 20000.0));
        addPayLater("eve", List.of(15000.0, 15000.0, 5000.0));

        // Add Tagihan
        addTagihan("TAG001", "Tagihan Listrik PLN", 100000.0, 100000, 0, "PLN", "T004", 0.0);
        addTagihan("TAG002", "Tagihan Air PAM", 100000.0, 100000, 0, "PAM", "T005", 0.0);
        addTagihan("TAG003", "Tagihan Listrik PLN",250000.0, 100000, 0, "PLN", "T004", 0.0);
        addTagihan("TAG004", "Tagihan Air PAM", 250000.0, 100000, 0, "PAM", "T005", 0.0);

        // Add SPinjam (Pinjaman)
        addPinjaman("alice", new SPinjam() {{
                        hutang = 50000.0;
                        bunga = 0.10;
                        jangka = 3;
                    }});
        addPinjaman("alice", new SPinjam() {{
                        hutang = 30000.0;
                        bunga = 0.08;
                        jangka = 2;
                    }});

        addPinjaman("bob", new SPinjam() {{
                        hutang = 20000.0;
                        bunga = 0.05;
                        jangka = 1;
                    }});
        addPinjaman("dave", new SPinjam() {{
                        hutang = 75000.0;
                        bunga = 0.12;
                        jangka = 4;
                    }});

        // Add Riwayat
        addRiwayat("alice",
                new Riwayat() {{
                        kodePembeli = "alice";
                        produk = produks.get("P001");
                        jumlah = 2;
                        tanggal = new Date();
                        status = "Selesai";
                    }});

        addRiwayat("alice", new Riwayat() {{
                        kodePembeli = "alice";
                        produk = makanan.get("M001");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                    }});
        addRiwayat("bob", new Riwayat() {{
                        kodePembeli = "bob";
                        produk = produks.get("P002");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Selesai";
                    }});
        addRiwayat("bob", new Riwayat() {{
                        kodePembeli = "bob";
                        produk = makanan.get("M002");
                        jumlah = 2;
                        tanggal = new Date();
                        status = "Diproses";
                    }});
        addRiwayat("carol", new Riwayat() {{
                        kodePembeli = "carol";
                        produk = produks.get("P004");
                        jumlah = 3;
                        tanggal = new Date();
                        status = "Selesai";
                    }});
        addRiwayat("dave", new Riwayat() {{
                        kodePembeli = "dave";
                        produk = makanan.get("M004");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                    }});
        addRiwayat("alice", new RiwayatTagihan() {{
                        kodePembeli = "alice";
                        produk = tagihan.get("TAG001");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                        token = "PLN-88F3-BC27";
                    }});
        addRiwayat("alice", new RiwayatTagihan() {{
                        kodePembeli = "alice";
                        produk = tagihan.get("TAG002");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                        token = "PAM-96F3-BC27";
                    }});
    }

    public static void beliProduk(String kode, int jumlah) {
        Produk p = produks.get(kode);
        if (p != null && p.stok >= jumlah) {
            Double totalBayar = p.harga * jumlah;
            if(pilihBayar(totalBayar)){
                p.stok -= jumlah;
                Riwayat r = new Riwayat();
                r.kodePembeli = loginUser.username;
                r.produk = p;
                r.jumlah = jumlah;
                r.tanggal = new Date();
                r.status = "Selesai";
                addRiwayat(loginUser.username, r);
                System.out.println("Pembelian berhasil!");
            }
        } else {
            System.out.println("Stok tidak cukup atau produk tidak ditemukan.");
        }
    }

    public static Boolean pilihBayar(Double totalBayar){
        Boolean pilihanValid = false;
        while(!pilihanValid){
            System.out.println("Pilih metode pembayaran:");
            System.out.println("1. PayLater");
            System.out.println("2. Shopee Pay");
            System.out.println("3. Kembali ke Menu Utama");
            String metode = input.nextLine();

            switch (metode) {
                case "1":
                    pilihanValid = bayarDenganPayLater(totalBayar);
                    break;
                case "2":
                    if (loginUser.saldo >= totalBayar) {
                        loginUser.saldo -= totalBayar;
                        pilihanValid = true;
                        System.out.println("Pembayaran berhasil! Saldo tersisa: " + loginUser.saldo);
                    } else {
                        System.out.println("Saldo tidak cukup.");
                    }   
                    break;
                case "3":
                    return pilihanValid;
                default:
                    System.out.println("Metode pembayaran tidak valid.");
                    break;
            }
        }
        return pilihanValid;
    }

    public static Boolean bayarDenganPayLater(double jumlah){
        System.out.println("jangka cicilan paylater: ");
        System.out.println("1. 1 bulan");
        System.out.println("2. 3 bulan");
        System.out.println("3. 6 bulan");
        System.out.println("4. 12 bulan");
        System.out.println("5. Kembali ke Pemilihan Pembayaran ");
        String jangka = input.nextLine();
        int jangkaBulan = 0;
        while(jangkaBulan == 0){
            switch (jangka) {
                case "1":
                    jangkaBulan = 1;
                    break;
                case "2":
                    jangkaBulan = 3;
                    break;
                case "3":
                    jangkaBulan = 6;
                    break;
                case "4":
                    jangkaBulan = 12;
                    break;
                case "5":
                    return false;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
        double totalBayar = jumlah*(1+payLaterFee);
        double cicilan = totalBayar / jangkaBulan;
        for(int i = 0; i < jangkaBulan; i++){
            if(paylater.get(loginUser.username) == null){
                paylater.put(loginUser.username, new PayLater());
            } 
            if(paylater.get(loginUser.username).tagihan == null){
                paylater.get(loginUser.username).tagihan = new ArrayList<>();
            } 
            if(paylater.get(loginUser.username).tagihan.size() < i+1){
                paylater.get(loginUser.username).tagihan.add(0.0);
            }

            Double val = paylater.get(loginUser.username).tagihan.get(i);
            val += cicilan;
            paylater.get(loginUser.username).tagihan.set(i, val);
        }
        System.out.println("Pembayaran berhasil! Total tagihan: " + totalBayar);
        System.out.println("Cicilan per bulan: " + cicilan);
        System.out.println("Jangka waktu: " + jangkaBulan + " bulan");
        return true;
    }

    public static void tampilkanProduk() {
        System.out.println("\n--- Daftar Produk ---");
        for (Produk p : produks.values()) {
            System.out.println(p.kode + " - " + p.nama + ", Harga: " + p.harga + ", Stok: " + p.stok);
        }
    }

    public static void tampilkanMakanan() {
        System.out.println("\n--- Daftar Makanan ---");
        for (Makanan m : makanan.values()) {
            System.out.println(m.kode + " - " + m.nama + ", Harga: " + m.harga + ", Kurir: " + m.namaKurir);
        }
    }

    public static void tampilkanTagihan() {
        PayLater pl = paylater.get(loginUser.username);
        if (pl == null || pl.tagihan.isEmpty()) {
            System.out.println("Tidak ada tagihan.");
            return;
        }
        System.out.println("--- Tagihan PayLater ---");
        for (Double t : pl.tagihan) {
            System.out.println("Rp" + t);
        }
    }

    public static void tampilkanPinjaman() {
        ArrayDeque<SPinjam> queue = pinjam.get(loginUser.username);
        if (queue == null || queue.isEmpty()) {
            System.out.println("Tidak ada data pinjaman.");
            return;
        }
        System.out.println("--- Data SPinjam ---");
        for (SPinjam s : queue) {
            System.out.println(
                    "Hutang: Rp" + s.hutang + ", Bunga: " + (s.bunga * 100) + "%, Jangka: " + s.jangka + " bulan");
        }
    }

    public static void tampilkanPayLater() {
        PayLater pl = paylater.get(loginUser.username);
        if (pl == null || pl.tagihan.isEmpty()) {
            System.out.println("Tidak ada tagihan.");
            return;
        }
        System.out.println("--- Tagihan PayLater ---");
        for (Double t : pl.tagihan) {
            System.out.println("Rp" + t);
        }
    }

    public static void tampilkanRiwayat() {
        Stack<Riwayat> stack = riwayat.get(loginUser.username);
        if (stack == null || stack.isEmpty()) {
            System.out.println("Tidak ada riwayat ditemukan.");
            return;
        }
        System.out.println("--- Riwayat Pembelian ---");
        for (Riwayat r : stack) {
            String tambahan = "";
            if(r instanceof RiwayatTagihan) {
                tambahan += ", Token: " + ((RiwayatTagihan) r).token;
            }
            if(r.produk instanceof Makanan) {
                tambahan += ", Estimasi Kedatangan: " + ((Makanan) r.produk).estimasiKedatangan + ", Nama Kurir: " + ((Makanan) r.produk).namaKurir;
            } 
            System.out.println("Produk: " + r.produk.nama + ", Jumlah: " + r.jumlah + ", Tanggal: " + r.tanggal
                    + ", Status: " + r.status + tambahan);
        }
    }

    public static void tampilkanTagihanProduk() {
        System.out.println("\n--- Daftar Tagihan Listrik dan Air ---");
        for (Produk t : tagihan.values()) {
            System.out.println(t.kode + " - " + t.nama + ", Harga: " + t.harga + ", Stok: " + t.stok);
        }
    }
}
