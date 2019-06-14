package cn.appoa.aframework.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 显示隐藏刪除
 */
public class EditTextDeleteWatcher implements TextWatcher {

    private EditText et;
    private ImageView iv;

    public EditTextDeleteWatcher(EditText et, ImageView iv) {
        super();
        this.et = et;
        this.iv = iv;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDeleteWatcher.this.et.setText(null);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            iv.setVisibility(View.GONE);
        } else {
            iv.setVisibility(View.VISIBLE);
        }
    }

}
