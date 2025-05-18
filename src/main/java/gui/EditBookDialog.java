package gui;

import entities.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditBookDialog extends JDialog {

    private final JTextField titleField  = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField isbnField   = new JTextField();
    private final JCheckBox   readCheck  = new JCheckBox("Read");
    private boolean saved = false;

    public EditBookDialog(JFrame parent, Book book) {
        super(parent, "Edit Book", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        readCheck.setSelected(book.getIs_read());

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 10));
        grid.add(new JLabel("Title:"));   grid.add(titleField);
        grid.add(new JLabel("Author:"));  grid.add(authorField);
        grid.add(new JLabel("ISBN:"));    grid.add(isbnField);
        grid.add(new JLabel("Is Read:")); grid.add(readCheck);
        content.add(grid, BorderLayout.CENTER);

        JButton save   = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.add(save); buttons.add(cancel);
        content.add(buttons, BorderLayout.SOUTH);

        save.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());

        setContentPane(content);
        pack(); setLocationRelativeTo(parent);
    }

    private void onSave() {
        if (titleField.getText().isBlank() ||
                authorField.getText().isBlank() ||
                isbnField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Fill all fields", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        saved = true;
        dispose();
    }

    public boolean isSaved()   { return saved; }
    public String  getTitle()  { return titleField.getText().trim(); }
    public String  getAuthor() { return authorField.getText().trim(); }
    public String  getIsbn()   { return isbnField.getText().trim(); }
    public boolean isRead()    { return readCheck.isSelected(); }
}
