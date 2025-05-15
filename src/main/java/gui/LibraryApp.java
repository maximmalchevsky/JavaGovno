package gui;

import entities.Book;
import gui.AddBookDialog;
import gui.EditBookDialog;
import service.Service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Чистый слой представления: получает готовый Service через конструктор.
 */
public class LibraryApp extends JFrame {

    private final Service service;

    private final DefaultListModel<Book> model = new DefaultListModel<>();
    private final JList<Book>            list  = new JList<>(model);
    private final JButton btnAdd    = new JButton("Add");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnEdit   = new JButton("Edit");

    public LibraryApp(Service service) {
        this.service = service;

        /* Nimbus L&F */
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
        catch (Exception ignored) {}

        setTitle("My Library");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initListeners();
        reloadBooks();
    }

    /* ----------------------- UI helpers --------------------------------- */

    private void initComponents() {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBorder(new EmptyBorder(5, 5, 5, 5));
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setCellRenderer(new BookCellRenderer());

        for (JButton b : new JButton[]{btnAdd, btnDelete, btnEdit}) {
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setPreferredSize(new Dimension(120, 30));
            b.setFocusPainted(false);
        }
    }

    private void initLayout() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons.setBackground(Color.WHITE);
        buttons.add(btnAdd);
        buttons.add(btnDelete);
        buttons.add(btnEdit);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Books", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16), Color.DARK_GRAY));
        listPanel.setBackground(Color.WHITE);
        listPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttons, BorderLayout.NORTH);
        getContentPane().add(listPanel, BorderLayout.WEST);
    }

    private void initListeners() {
        btnAdd.addActionListener(e -> {
            AddBookDialog dlg = new AddBookDialog(this);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                service.addBook(dlg.getTitle(), dlg.getAuthor(), dlg.getIsbn());
                reloadBooks();
            }
        });

        btnDelete.addActionListener(e -> {
            Book sel = list.getSelectedValue();
            if (sel != null && JOptionPane.showConfirmDialog(
                    this, "Delete \"" + sel.getTitle() + "\"?",
                    "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                service.deleteBook(sel.getId());
                reloadBooks();
            }
        });

        btnEdit.addActionListener(e -> {
            Book sel = list.getSelectedValue();
            if (sel != null) {
                EditBookDialog dlg = new EditBookDialog(this, sel);
                dlg.setVisible(true);
                if (dlg.isSaved()) {
                    sel.setTitle(dlg.getTitle());
                    sel.setAuthor(dlg.getAuthor());
                    sel.setIsbn(dlg.getIsbn());
                    service.updateBook(sel);
                    reloadBooks();
                }
            }
        });
    }

    private void reloadBooks() {
        model.clear();
        List<Book> books = service.listBooks();
        books.forEach(model::addElement);
    }

    /* ---------- renderer ---------- */
    private static class BookCellRenderer extends DefaultListCellRenderer {
        private static final Color ALT_BG = new Color(245, 245, 245);
        @Override
        public Component getListCellRendererComponent(
                JList<?> l, Object v, int i, boolean s, boolean f) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
            if (v instanceof Book b) {
                lbl.setText("%d: %s - %s | ISBN: %s"
                        .formatted(b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn()));
            }
            if (!s) lbl.setBackground(i % 2 == 0 ? ALT_BG : Color.WHITE);
            return lbl;
        }
    }
}
