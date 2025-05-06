package gui;

import entities.Book;
import service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibraryApp extends JFrame {

    private final BookService bookService;

    private DefaultListModel<Book> bookListModel;
    private JList<Book> bookList;

    private JButton addButton;
    private JButton deleteButton;
    private JButton markReadButton;

    public LibraryApp(BookService bookService) {
        this.bookService = bookService;

        // Устанавливаем современный Nimbus L&F
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Не удалось установить Nimbus L&F: " + e);
        }

        setTitle("My Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initListeners();

        loadBooksFromDb();
    }

    private void initComponents() {
        // Модель и список
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setBorder(new EmptyBorder(5, 5, 5, 5));
        bookList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookList.setCellRenderer(new BookCellRenderer());

        // Кнопки
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        markReadButton = new JButton("Mark Read");
        for (JButton btn : new JButton[]{addButton, deleteButton, markReadButton}) {
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(120,     30));
        }
    }

    private void initLayout() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(markReadButton);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(null, "Books",
                0, 0, new Font("Segoe UI", Font.BOLD, 16), Color.DARK_GRAY));
        listPanel.setBackground(Color.WHITE);
        listPanel.add(new JScrollPane(bookList), BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(listPanel, BorderLayout.WEST);
    }

    private void initListeners() {
        addButton.addActionListener(e -> {
            AddBookDialog dlg = new AddBookDialog(this);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                bookService.addBook(dlg.getTitle(), dlg.getAuthor(), dlg.getIsbn());
                loadBooksFromDb();
            }
        });


    }

    private void loadBooksFromDb() {
        bookListModel.clear();
        List<Book> books = bookService.listBooks();
        for (Book b : books) {
            bookListModel.addElement(b);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookService bookService = new BookService();
            LibraryApp app = new LibraryApp(bookService);
            app.setVisible(true);
        });
    }

    // Кастомный рендерер для списка книг
    private static class BookCellRenderer extends DefaultListCellRenderer {
        private static final Color ALT_BG = new Color(245, 245, 245);
        private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 14);

        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus
        ) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
            );
            if (value instanceof Book) {
                Book b = (Book) value;
                String txt = String.format("%d: %s - %s | ISBN: %s",
                        b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn());
                lbl.setText(txt);
            }
            lbl.setFont(LIST_FONT);
            lbl.setBorder(new EmptyBorder(5, 5, 5, 5));
            if (!isSelected) {
                lbl.setBackground(index % 2 == 0 ? ALT_BG : Color.WHITE);
            }
            return lbl;
        }
    }
}

// Диалог без изменений, но можно задать прозрачный фон и отступы
class AddBookDialog extends JDialog {
    private final JTextField titleField   = new JTextField();
    private final JTextField authorField  = new JTextField();
    private final JTextField isbnField    = new JTextField();
    private final JButton    saveButton   = new JButton("Save");
    private final JButton    cancelButton = new JButton("Cancel");
    private boolean saved = false;

    public AddBookDialog(JFrame parent) {
        super(parent, "Add New Book", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        content.setBackground(Color.WHITE);

        JPanel input = new JPanel(new GridLayout(3, 2, 10, 10));
        input.setOpaque(false);
        input.add(new JLabel("Title:"));
        input.add(titleField);
        input.add(new JLabel("Author:"));
        input.add(authorField);
        input.add(new JLabel("ISBN:"));
        input.add(isbnField);
        content.add(input, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        saveButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setPreferredSize(new Dimension(80, 30));
        buttons.add(saveButton);
        buttons.add(cancelButton);
        content.add(buttons, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> onCancel());

        setContentPane(content);
        pack();
        setLocationRelativeTo(parent);
    }

    private void onSave() {
        if (titleField.getText().isBlank() || authorField.getText().isBlank() || isbnField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        saved = true;
        dispose();
    }

    private void onCancel() {
        saved = false;
        dispose();
    }

    public boolean isSaved() { return saved; }
    public String getTitle() { return titleField.getText().trim(); }
    public String getAuthor() { return authorField.getText().trim(); }
    public String getIsbn() { return isbnField.getText().trim(); }
}
