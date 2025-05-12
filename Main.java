import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    static Scanner input = new Scanner(System.in);
    static HashMap<String, User> user = new HashMap<>();
    static HashMap<String, Produk> produks = new HashMap<>();
    static HashMap<String, Makanan> makanan = new HashMap<>();
    static HashMap<String, Toko> toko = new HashMap<>();
    static HashMap<String, Stack<Riwayat>> riwayat = new HashMap<>();
    static HashMap<String, PayLater> paylater = new HashMap<>();
    static HashMap<String, PriorityQueue<SPinjam>> pinjam = new HashMap<>();

    public static void main(String[] args) {
        // Add Users
        addUser("alice", "pass123", "081234567890", "Jl. Mawar 12", 100000.0, "Alice");
        addUser("bob", "bobpass", "082233445566", "Jl. Melati 21", 75000.0, "Bob");
        addUser("carol", "carol123", "083311122233", "Jl. Dahlia 7", 120000.0, "Carol");

        // Add Toko
        addToko("T001", "Toko Sukses", "Jl. Kenanga 1", "40123", "Aktif");
        addToko("T002", "Toko Makmur", "Jl. Sakura 99", "40234", "Aktif");

        // Add Produk
        addProduk("P001", "Sabun Mandi", "10000", "50", "5", "Non-Makanan", "T001", "tok123", "5000");
        addProduk("P002", "Shampoo", "15000", "40", "3", "Non-Makanan", "T002", "tok456", "4000");

        // Add Makanan
        addMakanan("M001", "Nasi Goreng", "20000", "30", "10", "Makanan", "T001", "tok789", "7000", "Kurir A",
                new Date());
        addMakanan("M002", "Bakso", "18000", "25", "8", "Makanan", "T002", "tok987", "6000", "Kurir B", new Date());

        // Add PayLater
        addPayLater("alice", List.of(25000.0, 12000.0));
        addPayLater("bob", List.of(18000.0));

        // Add SPinjam
        addPinjaman("alice", List.of(
                new SPinjam() {
                    {
                        hutang = 50000.0;
                        bunga = 0.10;
                        jangka = 3;
                    }
                },
                new SPinjam() {
                    {
                        hutang = 30000.0;
                        bunga = 0.08;
                        jangka = 2;
                    }
                }));

        // Add Riwayat
        addRiwayat("alice", List.of(
                new Riwayat() {
                    {
                        kodePembeli = "alice";
                        produk = produks.get("P001");
                        jumlah = 2;
                        tanggal = new Date();
                        status = "Selesai";
                    }
                },
                new Riwayat() {
                    {
                        kodePembeli = "alice";
                        produk = makanan.get("M001");
                        jumlah = 1;
                        tanggal = new Date();
                        status = "Dikirim";
                    }
                }));

        if (login()) {
            menu();
        }
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
                    return true;
                } else {
                    System.out.println("Password salah. Silakan coba lagi.\n");
                }
            } else if (username.equals("admin") && password.equals("admin")) {
                System.out.println("Login sebagai admin berhasil!");
                return true;
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
            System.out.println("3. Tagihan");
            System.out.println("4. SPinjam");
            System.out.println("5. Riwayat");
            System.out.println("6. Exit");
            System.out.print("Pilih menu: ");

            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1":
                    tampilkanProduk();
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

    public static void addProduk(String kode, String nama, String harga, String stok, String terjual, String tipe,
            String kodePenjual, String token, String ongkir) {
        Produk p = new Produk();
        p.kode = kode;
        p.nama = nama;
        p.harga = harga;
        p.stok = stok;
        p.banyakTerjual = terjual;
        p.tipe = tipe;
        p.kodePenjual = kodePenjual;
        p.token = token;
        p.ongkir = ongkir;
        produks.put(kode, p);
    }

    public static void addMakanan(String kode, String nama, String harga, String stok, String terjual, String tipe,
            String kodePenjual, String token, String ongkir, String kurir, Date estimasi) {
        Makanan m = new Makanan();
        m.kode = kode;
        m.nama = nama;
        m.harga = harga;
        m.stok = stok;
        m.banyakTerjual = terjual;
        m.tipe = tipe;
        m.kodePenjual = kodePenjual;
        m.token = token;
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

    public static void addPinjaman(String userid, List<SPinjam> pinjamList) {
        PriorityQueue<SPinjam> queue = new PriorityQueue<>((a, b) -> b.jangka - a.jangka);
        queue.addAll(pinjamList);
        pinjam.put(userid, queue);
    }

    public static void addRiwayat(String userid, List<Riwayat> entries) {
        Stack<Riwayat> stack = new Stack<>();
        for (Riwayat r : entries) {
            stack.push(r);
        }
        riwayat.put(userid, stack);
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
        System.out.print("\nMasukkan username untuk melihat tagihan: ");
        String username = input.nextLine();
        PayLater pl = paylater.get(username);
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
        System.out.print("\nMasukkan username untuk melihat pinjaman: ");
        String username = input.nextLine();
        PriorityQueue<SPinjam> queue = pinjam.get(username);
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

    public static void tampilkanRiwayat() {
        System.out.print("\nMasukkan username untuk melihat riwayat: ");
        String username = input.nextLine();
        Stack<Riwayat> stack = riwayat.get(username);
        if (stack == null || stack.isEmpty()) {
            System.out.println("Tidak ada riwayat ditemukan.");
            return;
        }
        System.out.println("--- Riwayat Pembelian ---");
        for (Riwayat r : stack) {
            System.out.println("Produk: " + r.produk.nama + ", Jumlah: " + r.jumlah + ", Tanggal: " + r.tanggal
                    + ", Status: " + r.status);
        }
    }

}
