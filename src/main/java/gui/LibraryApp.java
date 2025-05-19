package gui;

import entities.Book;
import gui.AddBookDialog;
import gui.EditBookDialog;
import service.Service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//получает готовый Service через конструктор.

public class LibraryApp extends JFrame {

    private final Service service;

    private final DefaultListModel<Book> model = new DefaultListModel<>();
    private final JList<Book>            list  = new JList<>(model);
    private final JButton btnAdd    = new JButton("Add");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnEdit   = new JButton("Edit");
    private final JTextField authorSearchField = new JTextField(20);
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnReset = new JButton("Reset");
    private final JCheckBox readCheck = new JCheckBox("Read");

    public LibraryApp(Service service) {
        this.service = service;

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

        authorSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setPreferredSize(new Dimension(100, 30));
        btnSearch.setFocusPainted(false);

        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReset.setPreferredSize(new Dimension(100, 30));
        btnReset.setFocusPainted(false);

        readCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void initLayout() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons.setBackground(Color.WHITE);
        buttons.add(btnAdd);
        buttons.add(btnDelete);
        buttons.add(btnEdit);

        buttons.add(new JLabel("Search by author:"));
        buttons.add(authorSearchField);
        buttons.add(btnSearch);
        buttons.add(btnReset);
        buttons.add(readCheck);

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
                service.addBook(
                        dlg.getTitle(),
                        dlg.getAuthor(),
                        dlg.getIsbn(),
                        dlg.isRead()
                );
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
                    sel.setIsRead(dlg.isRead());
                    service.updateBook(sel);
                    reloadBooks();
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index < 0) return;

                Rectangle bounds = list.getCellBounds(index, index);
                int xInCell = e.getX() - bounds.x;

                if (xInCell < BookCellRenderer.BOX_WIDTH) {
                    Book book = model.getElementAt(index);
                    boolean newRead = !Boolean.TRUE.equals(book.getIsRead());

                    service.setRead(book.getId(), newRead);
                    reloadBooks();

                    book.setIsRead(newRead);

                    model.setElementAt(book, index);

                    list.repaint(bounds);
                }
            }
        });


        btnSearch.addActionListener(e -> applyFilter());
        readCheck.addActionListener(e -> applyFilter());

        btnReset.addActionListener(e -> {
            authorSearchField.setText("");
            readCheck.setSelected(false);
            reloadBooks();
        });
    }

    private void reloadBooks() {
        model.clear();
        List<Book> books = service.listBooks();
        books.forEach(model::addElement);
    }

    private void applyFilter() {
        String author = authorSearchField.getText().trim();
        boolean onlyRead = readCheck.isSelected();

        List<Book> books = author.isEmpty()
                ? service.listBooks()
                : service.findBooksByAuthor(author);

        if (onlyRead) {
            books.removeIf(b -> !Boolean.TRUE.equals(b.getIsRead()));
        }

        model.clear();
        books.forEach(model::addElement);
    }

    private static class BookCellRenderer extends JCheckBox implements ListCellRenderer<Book> {
        private static final Color ALT_BG = new Color(245, 245, 245);

        public static final int BOX_WIDTH;
        static {
            JCheckBox tmp = new JCheckBox();
            Insets ins = tmp.getInsets();
            Icon icon = UIManager.getIcon("CheckBox.icon");
            BOX_WIDTH = ins.left + icon.getIconWidth() + tmp.getIconTextGap();
        }

        public BookCellRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Book> list,
                                                      Book book,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            setText(String.format("%d: %s — %s (ISBN: %s)",
                    book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn()));
            setSelected(Boolean.TRUE.equals(book.getIsRead()));

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(index % 2 == 0 ? ALT_BG : Color.WHITE);
                setForeground(list.getForeground());
            }
            return this;
        }
    }



}
