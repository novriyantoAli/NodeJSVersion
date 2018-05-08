package net.ali.rhein.mvpbase.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.ali.rhein.mvpbase.base.ui.BaseActivity;
import net.ali.rhein.mvpbase.base.ui.BasePresenter;

/**
 * Created by rhein on 3/27/18.
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {

    protected P presenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
        }
    }
}
