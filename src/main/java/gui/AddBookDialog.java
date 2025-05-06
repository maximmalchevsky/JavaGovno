package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddBookDialog extends JDialog {
    private final JTextField titleField  = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField isbnField   = new JTextField();
    private final JButton saveButton     = new JButton("Save");
    private final JButton cancelButton   = new JButton("Cancel");
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
        if (titleField.getText().isBlank() ||
                authorField.getText().isBlank() ||
                isbnField.getText().isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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
    public String getTitle()  { return titleField.getText().trim(); }
    public String getAuthor() { return authorField.getText().trim(); }
    public String getIsbn()   { return isbnField.getText().trim(); }
}
