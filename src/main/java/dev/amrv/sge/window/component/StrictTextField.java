package dev.amrv.sge.window.component;

import java.awt.Color;
import java.util.function.Predicate;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class StrictTextField extends JTextField {

    private Predicate<String> validator;
    private Color normalColor;
    private Color errorColor;
    private boolean validInput;

    public StrictTextField() {
        configureValidator();
    }

    public StrictTextField(String string) {
        super(string);
        configureValidator();
    }

    public StrictTextField(String string, Predicate<String> validator) {
        super(string);
        this.validator = validator;
        configureValidator();
    }

    public StrictTextField(String string, String regex) {
        this(string, (t) -> t.matches(regex));
    }

    private void configureValidator() {
        normalColor = super.getForeground();
        errorColor = new Color(245, 97, 81);
        super.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    public boolean isValidInput() {
        return validInput;
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        validateInput();
    }

    protected synchronized void validateInput() {
        if (validator == null || validator.test(super.getText())) {
            // Valid input
            validInput = true;
            super.setForeground(normalColor);
        } else {
            // Invalid input
            validInput = false;
            super.setForeground(errorColor);
        }
    }

    @Override
    public void setForeground(Color fg) {
        normalColor = fg;
    }

    public void setForegroundError(Color fge) {
        errorColor = fge;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        normalColor = super.getForeground();
    }

}
