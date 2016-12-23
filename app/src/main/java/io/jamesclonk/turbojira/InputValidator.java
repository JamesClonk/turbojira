package io.jamesclonk.turbojira;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class  InputValidator implements TextWatcher {
    private final TextView view;

    public InputValidator(TextView view) {
        this.view = view;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = view.getText().toString();
        validate(view, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing
    }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing
    }
}
