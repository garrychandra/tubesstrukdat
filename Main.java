import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class Main {
    static Scanner input = new Scanner(System.in);
    static User loginUser = new User();
    static Random rand = new Random();
    static Double payLaterFee = 0.02;
    static Double[] bunga = {0.15,0.145,0.14,0.135,0.13,0.125,0.12,0.115,0.11,0.1,0.09,0.08};
    static HashMap<String, User> user = new HashMap<>();
    static HashMap<String, Produk> produks = new HashMap<>();
    static HashMap<String, Produk> makanan = new HashMap<>();
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
                    System.out.print("Masukkan kode makanan yang ingin dibeli: ");
                    kode = input.nextLine();
                    System.out.print("Masukkan jumlah yang ingin dibeli: ");
                    jumlah = input.nextInt();
                    input.nextLine(); // Consume newline
                    beliMakanan(kode, jumlah, pilihKurir());
                    break;
                case "3":
                    tampilkanTagihan();
                    System.out.print("Masukkan kode Tagihan yang ingin dibeli: ");
                    kode = input.nextLine();
                    beliTagihan(kode);
                    break;
                case "4":
                    tampilkanPinjaman();
                    System.out.println("1. Pinjam");
                    System.out.println("2. Bayar");
                    System.out.println("Pilihan: ");
                    int choice = input.nextInt();
                    input.nextLine(); // Consume newline
                    if(choice == 1){
                        newPinjam();
                    } else if (choice == 2){
                        System.out.println("Nominal Bayar: ");
                        bayarPinjam(input.nextDouble());
                    } else {
                        System.out.println("invalid input, returning to menu");
                    }
                    break;
                case "5":
                    tampilkanRiwayat();
                    break;
                case "6":
                    topUp();
                    System.out.println("Top Up ShopeePay Berhasil");
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

    public static void addToko(String kode, String nama, String alamat, String kodePos, String status, Double standardOngkir, Double expressOngkir, Double vipOngkir) {
        Toko t = new Toko();
        t.kode = kode;
        t.nama = nama;
        t.alamat = alamat;
        t.kodePos = kodePos;
        t.status = status;
        t.ongkir[0] = standardOngkir;
        t.ongkir[1] = expressOngkir;
        t.ongkir[2] = vipOngkir;
        toko.put(kode, t);
    }

    public static void addProduk(String kode, String nama, Double harga, int stok, int terjual, String tipe,
            String kodePenjual) {
        Produk p = new Produk();
        p.kode = kode;
        p.nama = nama;
        p.harga = harga;
        p.stok = stok;
        p.banyakTerjual = terjual;
        p.tipe = tipe;
        p.kodePenjual = kodePenjual;
        produks.put(kode, p);
    }

    public static void addMakanan(String kode, String nama, Double harga, int stok, int terjual, String tipe,
            String kodePenjual) {
        Produk m = new Produk();
        m.kode = kode;
        m.nama = nama;
        m.harga = harga;
        m.stok = stok;
        m.banyakTerjual = terjual;
        m.tipe = tipe;
        m.kodePenjual = kodePenjual;
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
            String tipe, String kodePenjual) {
        Produk t = new Produk();
        t.kode = kode;
        t.nama = nama;
        t.harga = harga;
        t.stok = stok;
        t.banyakTerjual = terjual;
        t.tipe = tipe;
        t.kodePenjual = kodePenjual;
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
        addToko("T001", "Toko Sukses", "Jl. Kenanga 1", "40123", "Aktif", 5000.0, 8000.0, 15000.0);
        addToko("T002", "Toko Makmur", "Jl. Sakura 99", "40234", "Aktif", 6000.0, 9000.0, 16000.0);
        addToko("T003", "Mart Hebat", "Jl. Jaya 77", "40345", "Aktif", 5500.0, 8500.0, 15500.0);
        addToko("T004", "PLN", "", "", "Aktif", 0.0, 0.0, 0.0);
        addToko("T005", "PAM", "", "", "Aktif", 0.0, 0.0, 0.0);
        // Add Produk
        addProduk("P001", "Sabun Mandi", 10000.0, 50, 5, "Non-Makanan", "T001");
        addProduk("P002", "Shampoo", 15000.0, 40, 3, "Non-Makanan", "T002");
        addProduk("P003", "Pasta Gigi", 12000.0, 60, 7, "Non-Makanan", "T003");
        addProduk("P004", "Tissue", 8000.0, 70, 6, "Non-Makanan", "T001");

        // Add Makanan
        addMakanan("M001", "Nasi Goreng", 20000.0, 30, 10, "Makanan", "T001");
        addMakanan("M002", "Bakso", 18000.0, 25, 8, "Makanan", "T002");
        addMakanan("M003", "Ayam Geprek", 22000.0, 20, 12, "Makanan", "T003");
        addMakanan("M004", "Sate Ayam", 25000.0, 15, 9, "Makanan", "T002");

        // Add PayLater (Tagihan)
        addPayLater("alice", List.of(25000.0, 12000.0));
        addPayLater("bob", List.of(18000.0, 8000.0));
        addPayLater("carol", List.of(5000.0));
        addPayLater("dave", List.of(10000.0, 20000.0));
        addPayLater("eve", List.of(15000.0, 15000.0, 5000.0));

        // Add Tagihan
        addTagihan("TAG001", "Tagihan Listrik PLN", 100000.0, 100000, 0, "PLN", "T004");
        addTagihan("TAG002", "Tagihan Air PAM", 100000.0, 100000, 0, "PAM", "T005");
        addTagihan("TAG003", "Tagihan Listrik PLN",250000.0, 100000, 0, "PLN", "T004");
        addTagihan("TAG004", "Tagihan Air PAM", 250000.0, 100000, 0, "PAM", "T005");

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
                        namaKurir = "REGULER";
                    }});

        addRiwayat("alice", new RiwayatMakanan() {{
                        kodePembeli = "alice";
                        produk = makanan.get("M001");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                        estimasiKedatangan = estimasiPengiriman();
                        namaKurir = "VIP";
                    }});
        addRiwayat("bob", new Riwayat() {{
                        kodePembeli = "bob";
                        produk = produks.get("P002");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Selesai";
                        namaKurir = "EXPRESS";
                    }});
        addRiwayat("bob", new RiwayatMakanan() {{
                        kodePembeli = "bob";
                        produk = makanan.get("M002");
                        jumlah = 2;
                        tanggal = new Date();
                        status = "Diproses";
                        estimasiKedatangan = estimasiPengiriman();
                        namaKurir = "REGULER";
                    }});
        addRiwayat("carol", new Riwayat() {{
                        kodePembeli = "carol";
                        produk = produks.get("P004");
                        jumlah = 3;
                        tanggal = new Date();
                        status = "Selesai";
                        namaKurir = "REGULER";
                    }});
        addRiwayat("dave", new RiwayatMakanan() {{
                        kodePembeli = "dave";
                        produk = makanan.get("M004");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                        estimasiKedatangan = estimasiPengiriman();
                        namaKurir = "REGULER";
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

    public static Date estimasiPengiriman(){
        return new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5+rand.nextLong(26)));
    }

    public static void beliProduk(String kode, int jumlah) {
        Produk p = produks.get(kode);
        if (p != null && p.stok >= jumlah) {
            Toko t = toko.get(p.kodePenjual);
            Double ongkir = 0.0;
            String kurir = pilihKurir();
            switch(kurir) {
                case "REGULER":
                    ongkir = t.ongkir[0];
                    break;
                case "EXPRESS":
                    ongkir = t.ongkir[1];
                    break;
                case "VIP":
                    ongkir = t.ongkir[2];
                    break;
            }
            Double totalBayar = (p.harga * jumlah) + ongkir;
            if(pilihBayar(totalBayar)){
                p.stok -= jumlah;
                RiwayatMakanan r = new RiwayatMakanan();
                r.kodePembeli = loginUser.username;
                r.produk = p;
                r.jumlah = jumlah;
                r.tanggal = new Date();
                r.status = "Disiapkan";
                r.estimasiKedatangan = estimasiPengiriman();
                r.namaKurir = kurir;
                addRiwayat(loginUser.username, r);
                System.out.println("Pembelian berhasil!");
                printRiwayat(r);
                System.out.print("\n---Tekan enter untuk lanjut---");
                input.nextLine();
            }
        } else {
            System.out.println("Stok tidak cukup atau produk tidak ditemukan.");
        }
    }

    public static void beliMakanan(String kode, int jumlah, String kurir) {
        Produk p = makanan.get(kode);
        if (p != null && p.stok >= jumlah) {
            Toko t = toko.get(p.kodePenjual);
            Double ongkir = 0.0;
            switch(kurir) {
                case "REGULER":
                    ongkir = t.ongkir[0];
                    break;
                case "EXPRESS":
                    ongkir = t.ongkir[1];
                    break;
                case "VIP":
                    ongkir = t.ongkir[2];
                    break;
            }
            Double totalBayar = (p.harga * jumlah) + ongkir;
            if(pilihBayar(totalBayar)){
                p.stok -= jumlah;
                RiwayatMakanan r = new RiwayatMakanan();
                r.kodePembeli = loginUser.username;
                r.produk = p;
                r.jumlah = jumlah;
                r.tanggal = new Date();
                r.status = "Disiapkan";
                r.estimasiKedatangan = estimasiPengiriman();
                r.namaKurir = kurir;
                addRiwayat(loginUser.username, r);
                System.out.println("Pembelian berhasil!");
                printRiwayat(r);
                System.out.print("\n---Tekan enter untuk lanjut---");
                input.nextLine();
            }
        } else {
            System.out.println("Stok tidak cukup atau Makanan tidak ditemukan.");
        }
    }

    public static void beliTagihan(String kode) {
        Produk p = tagihan.get(kode);
        if (p != null) {
            Double totalBayar = p.harga;
            if(pilihBayar(totalBayar)){
                RiwayatTagihan r = new RiwayatTagihan();
                r.kodePembeli = loginUser.username;
                r.produk = p;
                r.jumlah = 1;
                r.tanggal = new Date();
                r.token = getToken();
                r.status = "Selesai";
                addRiwayat(loginUser.username, r);
                System.out.println("Pembelian berhasil!");
                printRiwayat(r);
                System.out.print("\n---Tekan enter untuk lanjut---");
                input.nextLine();
            }
        } else {
            System.out.println("Tagihan tidak ditemukan.");
        }
    }

    public static String getToken(){
        String token = "";
        token += String.format("%05d",rand.nextInt(100000));
        token += " - ";
        token += String.format("%05d",rand.nextInt(100000));
        token += " - ";
        token += String.format("%05d",rand.nextInt(100000));
        token += " - ";
        token += String.format("%05d",rand.nextInt(100000));
        token += " - ";
        token += String.format("%05d",rand.nextInt(100000));
        return token;
    }

    public static String pilihKurir(){
        Boolean pilihanValid = false;
        String kurir = "";
        while(!pilihanValid){
            System.out.println("1. Reguler");
            System.out.println("2. Express");
            System.out.println("3. VIP");
            System.out.print("Pilih Jasa Kurir:");
            String metode = input.nextLine();

            switch (metode) {
                case "1":
                    pilihanValid = true;
                    kurir = "REGULER";
                    break;
                case "2":
                    pilihanValid = true;
                    kurir = "EXPRESS";
                    break;
                case "3":
                    pilihanValid = true;
                    kurir = "VIP";
                    break;
                default:
                    System.out.println("Kurir tidak valid.");
                    break;
            }
        }
        return kurir;
    }

    public static void topUp(){
        System.out.println("Jumlah Topup: ");
        loginUser.saldo += input.nextDouble();
        input.nextLine();
    }

    public static void newPinjam(){
        SPinjam p = new SPinjam();
        System.out.println("Jumlah Pinjam: ");
        p.hutang = input.nextDouble();
        input.nextLine();
        System.out.println("Jangka Pinjam(1-12): ");
        p.jangka = input.nextInt();
        input.nextLine();
        p.bunga = bunga[p.jangka-1];

        pinjam.get(loginUser.username).add(p);

        System.out.println("Hutang: Rp" + p.hutang);
        System.out.println("Bunga: Rp" + p.bunga);
        System.out.println("Jangka: Rp" + p.jangka);
        System.out.println("Total: Rp" + p.hutang * p.bunga / 12 * p.jangka);
    }

    public static void bayarPinjam(Double nominal){
        // Adding null check
        if (!pinjam.containsKey(loginUser.username) || pinjam.get(loginUser.username).isEmpty()) {
            System.out.println("No active loans found.");
            return;
        }
        input.nextLine();
        while(nominal > 0){
            SPinjam p = pinjam.get(loginUser.username).getFirst();
            Double bunga = (p.hutang * p.bunga / 12 * p.jangka);
            Double hutang = p.hutang;
            if(hutang + bunga - nominal <= 0){
                nominal -= hutang;
                nominal -= bunga;
                pinjam.get(loginUser.username).remove();
            } else {
                if(bunga < nominal){
                    nominal -= bunga;
                    hutang -= nominal;
                } else {
                    bunga -= nominal;
                    hutang += bunga;
                }
                p.bunga = 0.0;
                p.hutang = hutang;
                nominal = 0.0;
            }
        }
    }

    public static Boolean pilihBayar(Double totalBayar){
        Boolean pilihanValid = false;
        while(!pilihanValid){
            System.out.println("1. PayLater");
            System.out.println("2. Shopee Pay");
            System.out.println("3. Kembali ke Menu Utama");
            System.out.print("Pilih metode pembayaran:");
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
        System.out.println("1. 1 bulan");
        System.out.println("2. 3 bulan");
        System.out.println("3. 6 bulan");
        System.out.println("4. 12 bulan");
        System.out.println("5. Kembali ke Pemilihan Pembayaran ");
        System.out.print("jangka paylater: ");
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
            if(paylater.get(loginUser.username).tagihan.size() <= i){
                while(paylater.get(loginUser.username).tagihan.size() <= i) {
                    paylater.get(loginUser.username).tagihan.add(0.0);
                }
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
        for (Produk m : makanan.values()) {
            System.out.println(m.kode + " - " + m.nama + ", Harga: " + m.harga);
        }
    }

    public static void tampilkanTagihan() {
        System.out.println("--- Tagihan Utilities ---");
        for (Produk p : tagihan.values()) {
             System.out.println(p.kode + " - " + p.nama + ", Harga: " + p.harga);
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
                    "Hutang: Rp" + (s.hutang + (s.hutang * s.bunga / 12 * s.jangka)));
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
        System.out.print("\n---Tekan enter untuk lanjut---");
        input.nextLine();
    }

    public static void printRiwayat(Riwayat r){
        String tambahan = "";
        Double ongkir = 0.0;
        
        if(r.namaKurir != null) {
            Toko t = toko.get(r.produk.kodePenjual);
            switch(r.namaKurir) {
                case "REGULER":
                    ongkir = t.ongkir[0];
                    break;
                case "EXPRESS":  // Changed from REGULEREXPRESS to EXPRESS
                    ongkir = t.ongkir[1];
                    break;
                case "VIP":
                    ongkir = t.ongkir[2];
                    break;
            }
            tambahan += ", Kurir: " + r.namaKurir + 
                       ", Ongkir: Rp" + ongkir;
        }

        if(r instanceof RiwayatTagihan) {
            tambahan += ", Token: " + ((RiwayatTagihan) r).token;
        }
        if(r instanceof RiwayatMakanan) {
            RiwayatMakanan rm = (RiwayatMakanan) r;
            tambahan += ", Estimasi Kedatangan: " + rm.estimasiKedatangan;
        } 

        System.out.println("Produk: " + r.produk.nama + 
                          ", Jumlah: " + r.jumlah + 
                          ", Harga: Rp" + r.produk.harga * r.jumlah +
                          (ongkir > 0 ? ", Total + Ongkir: Rp" + (r.produk.harga * r.jumlah + ongkir) : "") +
                          ", Tanggal: " + r.tanggal +
                          ", Status: " + r.status + tambahan);
    }

    public static void tampilkanRiwayat() {
        Stack<Riwayat> stack = riwayat.get(loginUser.username);
        if (stack == null || stack.isEmpty()) {
            System.out.println("Tidak ada riwayat ditemukan.");
            return;
        }
        System.out.println("--- Riwayat Pembelian ---");
        for (Riwayat r : stack) {
            printRiwayat(r);
        }
        System.out.print("\n---Tekan enter untuk lanjut---");
        input.nextLine();
    }

    public static void tampilkanTagihanProduk() {
        System.out.println("\n--- Daftar Tagihan Listrik dan Air ---");
        for (Produk t : tagihan.values()) {
            System.out.println(t.kode + " - " + t.nama + ", Harga: " + t.harga + ", Stok: " + t.stok);
        }
    }
}
