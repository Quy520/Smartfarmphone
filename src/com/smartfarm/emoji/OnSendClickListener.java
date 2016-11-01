package com.smartfarm.emoji;

import android.text.Editable;

public interface OnSendClickListener {
    void onClickSendButton(Editable str);

    void onClickFlagButton();
}
