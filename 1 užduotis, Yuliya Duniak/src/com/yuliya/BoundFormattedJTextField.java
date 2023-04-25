package com.yuliya;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * tekstinis laukas priristas prie tam tikros reiksmes Data
 * @param <T> tipas su kurio dirbame
 */
public class BoundFormattedJTextField<T> extends JFormattedTextField implements DocumentListener {

    private final Consumer<T> setter;
    private final Function<String, T> caster;

    BoundFormattedJTextField(DefaultFormatter formatter, Function<Void, T> getter, Consumer<T> setter, Function<String, T> caster) {
        super(formatter);
        this.setter = setter;
        this.caster = caster;
        setValue(getter.apply(null));
        getDocument().addDocumentListener(this);
        setMaximumSize(new Dimension(250, 20));
    }

    /**
     * Atnaujina susieto lauko reiksme per setter
     */
    private void setBoundValue() {
        setter.accept(caster.apply(getValue().toString()));
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        setBoundValue();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setBoundValue();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setBoundValue();
    }
}

