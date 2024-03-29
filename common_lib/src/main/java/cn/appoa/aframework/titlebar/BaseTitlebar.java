package cn.appoa.aframework.titlebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public abstract class BaseTitlebar extends FrameLayout {

    /**
     * Context
     */
    protected Context context;

    public BaseTitlebar(Context context) {
        super(context);
        this.context = context;
        initTitle();
    }

    /**
     * 设置标题
     */
    public void setTitle(int layoutResID) {
        View view = LayoutInflater.from(context).inflate(layoutResID, null);
        setTitle(view);
    }

    /**
     * 设置标题
     */
    public void setTitle(View view) {
        if (view == null)
            return;
        if (view.getLayoutParams() == null) {
            this.addView(view);
        } else {
            this.addView(view, view.getLayoutParams());
        }
        initView(view);
    }

    /**
     * 初始化标题
     */
    public abstract void initTitle();

    /**
     * 初始化控件
     */
    public abstract void initView(View view);

    /**
     * 返回监听
     */
    protected OnClickBackListener onClickBackListener;

    /**
     * 设置返回监听
     */
    public void setOnClickBackListener(OnClickBackListener onClickBackListener) {
        this.onClickBackListener = onClickBackListener;
    }

    /**
     * 返回监听回调接口
     */
    public interface OnClickBackListener {
        void onClickBack(View view);
    }

    /**
     * 菜单监听
     */
    protected OnClickMenuListener onClickMenuListener;

    /**
     * 设置菜单监听
     */
    public void setOnClickMenuListener(OnClickMenuListener onClickMenuListener) {
        this.onClickMenuListener = onClickMenuListener;
    }

    /**
     * 菜单监听回调接口
     */
    public interface OnClickMenuListener {
        void onClickMenu(View view);
    }
}
