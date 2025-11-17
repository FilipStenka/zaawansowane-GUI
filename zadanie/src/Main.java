import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("To-Do Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        TaskTableModel model = new TaskTableModel();
        JTable table = new JTable(model);


        table.getColumnModel().getColumn(1).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));


        table.getColumnModel().getColumn(2).setCellRenderer(new PriorityRenderer(model));


        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton addBtn = new JButton("Dodaj Zadanie");
        JButton removeBtn = new JButton("Usuń Zadanie");

        leftPanel.add(addBtn);
        leftPanel.add(removeBtn);


        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Podaj nazwę zadania:");
            if (name == null || name.trim().isEmpty()) return;

            String[] options = {"Niski", "Średni", "Wysoki"};
            String priority = (String) JOptionPane.showInputDialog(
                    frame, "Wybierz priorytet:", "Priorytet",
                    JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]
            );

            model.addTask(new Task(name, false, priority));
        });


        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) model.removeTask(row);
        });


        JScrollPane tableScroll = new JScrollPane(table);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tableScroll);
        splitPane.setDividerLocation(200);

        frame.add(splitPane);
        frame.setVisible(true);
    }
}



class Task {
    String name;
    boolean completed;
    String priority;

    public Task(String name, boolean completed, String priority) {
        this.name = name;
        this.completed = completed;
        this.priority = priority;
    }
}

class TaskTableModel extends AbstractTableModel {

    private final String[] columns = {"Nazwa", "Status", "Priorytet"};
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task t) {
        tasks.add(t);
        fireTableRowsInserted(tasks.size() - 1, tasks.size() - 1);
    }

    public void removeTask(int index) {
        tasks.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public Task getTask(int row) {
        return tasks.get(row);
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Task t = tasks.get(row);
        switch (col) {
            case 0: return t.name;
            case 1: return t.completed;
            case 2: return t.priority;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1; // checkbox tylko
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Task t = tasks.get(row);
        if (col == 1) {
            t.completed = (boolean) value;
            fireTableCellUpdated(row, col);
        }
    }
}




class StatusRenderer extends JCheckBox implements TableCellRenderer {
    public StatusRenderer() {
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        setSelected(value != null && (boolean) value);

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        return this;
    }
}


class PriorityRenderer extends DefaultTableCellRenderer {
    private final TaskTableModel model;

    public PriorityRenderer(TaskTableModel model) {
        this.model = model;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        Task t = model.getTask(row);

        if ("Wysoki".equalsIgnoreCase(t.priority)) {
            c.setBackground(Color.RED);
            c.setForeground(Color.WHITE);
        } else {
            c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
}
