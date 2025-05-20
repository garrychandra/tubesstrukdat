import java.util.Scanner;

public class SPinjam {
    Double hutang;
    Double bunga;
    int jangka;

    public SPinjam(Double hutang, Double bunga, int jangka) {
        this.hutang = hutang;
        this.bunga = bunga;
        this.jangka = jangka;
    }

    public double hitungTotalHutang() {
        return hutang + (hutang * bunga / 100.0 * jangka);
    }

    public double hitungCicilanPerBulan() {
        return hitungTotalHutang() / jangka;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Shopee Pinjam ===");
        System.out.print("Masukkan jumlah pinjaman (Rp): ");
        double hutang = scanner.nextDouble();

        System.out.print("Masukkan bunga per bulan (%): ");
        double bunga = scanner.nextDouble();

        System.out.print("Masukkan jangka waktu (bulan): ");
        int jangka = scanner.nextInt();

        SPinjam pinjaman = new SPinjam(hutang, bunga, jangka);

        double totalHutang = pinjaman.hitungTotalHutang();
        double cicilan = pinjaman.hitungCicilanPerBulan();

        System.out.println("\n=== Rincian Pinjaman ===");
        System.out.println("Total hutang setelah bunga: Rp " + totalHutang);
        System.out.println("Cicilan per bulan: Rp " + cicilan);

        scanner.close();
    }
}