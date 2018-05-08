package net.ali.rhein.mvpbase.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.ali.rhein.mvpbase.base.ui.BaseFragment;
import net.ali.rhein.mvpbase.base.ui.BasePresenter;

/**
 * Created by rhein on 3/27/18.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {

    protected P presenter;

    protected abstract P createPresenter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
        }
    }
}
