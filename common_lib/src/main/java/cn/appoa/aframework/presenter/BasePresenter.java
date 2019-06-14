package cn.appoa.aframework.presenter;

import android.os.Bundle;

import cn.appoa.aframework.view.IBaseView;

public abstract class BasePresenter {

    public void onCreate(Bundle savedState) {
    }

    public abstract void onAttach(IBaseView view);

    public void onSaveInstanceState(Bundle outState) {
    }

    public abstract void onDetach();

    public void onDestroy() {
    }

}
