import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class Main {


    static class Product {
        private String nazwa;
        private double cena;
        private int ilosc;

        public Product(String nazwa, double cena, int ilosc) {
            this.nazwa = nazwa;
            this.cena = cena;
            this.ilosc = ilosc;
        }

        public String getNazwa() { return nazwa; }
        public double getCena() { return cena; }
        public int getIlosc() { return ilosc; }
    }


    static class ProductTableModel extends AbstractTableModel {
        private List<Product> produkty;
        private String[] nazwyKolumn = {"Nazwa", "Cena", "Ilość"};

        public ProductTableModel(List<Product> produkty) {
            this.produkty = produkty;
        }

        @Override
        public int getRowCount() {
            return produkty.size();
        }

        @Override
        public int getColumnCount() {
            return nazwyKolumn.length;
        }

        @Override
        public String getColumnName(int column) {
            return nazwyKolumn[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product p = produkty.get(rowIndex);
            switch (columnIndex) {
                case 0: return p.getNazwa();
                case 1: return p.getCena();
                case 2: return p.getIlosc();
                default: return null;
            }
        }
    }


    public static void main(String[] args) {

        List<Product> lista = new ArrayList<>();
        lista.add(new Product("woda", 2, 1111));
        lista.add(new Product("sok", 4, 122));
        lista.add(new Product("cola", 5, 111));


        ProductTableModel model = new ProductTableModel(lista);

        JTable tabela = new JTable(model);


        JFrame frame = new JFrame("Tabela produktów");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(tabela));
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}
