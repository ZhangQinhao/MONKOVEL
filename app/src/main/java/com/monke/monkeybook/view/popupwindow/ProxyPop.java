package com.monke.monkeybook.view.popupwindow;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kyleduo.switchbutton.SwitchButton;
import com.monke.monkeybook.ProxyManager;
import com.monke.monkeybook.R;

public class ProxyPop extends PopupWindow {
    private Context mContext;
    private View view;

    private FrameLayout flProxyContent;
    private EditText edtProxyHttp;
    private SwitchButton sbProxyStart;

    public ProxyPop(Context context) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContext = context;
        view = LayoutInflater.from(mContext).inflate(R.layout.view_pop_proxy, null);
        this.setContentView(view);
        bindView();
        bindEvent();

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_checkaddshelf);
    }

    private void bindEvent() {
        edtProxyHttp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    finishInputProxyHttp(true);
                }
            }
        });
        edtProxyHttp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    finishInputProxyHttp(true);
                }
                return false;
            }
        });
        flProxyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishInputProxyHttp(true);
            }
        });
        edtProxyHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtProxyHttp.setCursorVisible(true);
            }
        });

        sbProxyStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                finishInputProxyHttp(false);
                ProxyManager.saveProxyState(isChecked);
                if (isChecked) {
                    if (!ProxyManager.hasProxy()) {
                        YoYo.with(Techniques.Shake).playOn(edtProxyHttp);
                        sbProxyStart.setCheckedImmediatelyNoEvent(false);
                    }
                }
            }
        });
    }

    private void bindView() {
        edtProxyHttp = view.findViewById(R.id.edt_proxy_http);
        sbProxyStart = view.findViewById(R.id.sb_proxy_start);
        flProxyContent = view.findViewById(R.id.fl_proxy_content);
    }

    @Override
    public void showAsDropDown(View anchor) {
        edtProxyHttp.setText(ProxyManager.proxyHttp);
        sbProxyStart.setCheckedImmediatelyNoEvent(ProxyManager.proxyState);
        super.showAsDropDown(anchor);
    }

    private void finishInputProxyHttp(boolean needUpdateState) {
        ProxyManager.saveProxyHttp(edtProxyHttp.getText().toString().trim());
        if (needUpdateState) {
            sbProxyStart.setCheckedImmediatelyNoEvent(ProxyManager.hasProxy());
        }
        edtProxyHttp.setCursorVisible(false);
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtProxyHttp.getWindowToken(), 0);
    }
}
