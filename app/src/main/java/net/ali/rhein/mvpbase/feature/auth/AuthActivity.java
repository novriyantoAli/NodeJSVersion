package net.ali.rhein.mvpbase.feature.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.base.mvp.MvpActivity;
import net.ali.rhein.mvpbase.feature.homes.HomesActivity;
import net.ali.rhein.mvpbase.models.LoginResponse;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;

public class AuthActivity extends MvpActivity<AuthPresenter> implements AuthView, View.OnClickListener {

    private SharedPreferences sharedPreferences;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @BindView(R.id.edit_text_username)
    EditText editTextUsername;

    @BindView(R.id.edit_text_password)
    EditText editTextPassword;

    @BindView(R.id.text_view_password_show)
    TextView textViewPasswordShow;

    @BindView(R.id.text_input_layout_password)
    TextInputLayout textInputLayoutPassword;

    @BindView(R.id.button_loading)
    CircularProgressButton circularProgressButton;

    @Override
    protected AuthPresenter createPresenter() {
        return new AuthPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        initialize();

        initializeOnClickListener();

        if (sharedPreferences.getInt("login", 0) > 0){
            Intent intent = new Intent(this, HomesActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onClick(View view) {}

    @Override
    public void showLoading() {
        circularProgressButton.startAnimation();
    }

    @Override
    public void hideLoading() {
        circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_error));
        circularProgressButton.revertAnimation();
    }

    @Override
    public void showLoginError(String message) {
        showSnackBars(message);
    }

    @Override
    public void showLoginSuccess(LoginResponse model) {
        if(model.getStatus() == 200){
            presenter.bindData(model, sharedPreferences, this);
        }
        else{
            showSnackBars(model.getMessage());
        }
    }

    @Override
    public void showValidationUsernameError(String message) {
        editTextUsername.setError(message);
    }

    @Override
    public void showValidationPasswordError(String message) {
        editTextPassword.setError(message);
    }

    @Override
    public void showValidationSuccess(String username, String password) {
        presenter.login(username, password);
    }

    @Override
    public void moveToActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    private void initialize(){
        this.sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    private void initializeOnClickListener() {
        textViewPasswordShow.setVisibility(View.INVISIBLE);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTextPassword.getText().length() > 0){
                    textViewPasswordShow.setVisibility(View.VISIBLE);
                    if(editTextPassword.getText().length() < 5)
                        textInputLayoutPassword.setError("Password must be at least 5 characters");
                    else {
                        textInputLayoutPassword.setError(null);
                        circularProgressButton.setEnabled(true);
                    }
                }
                else{
                    textViewPasswordShow.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textViewPasswordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textViewPasswordShow.getText().toString() == "SHOW"){
                    textViewPasswordShow.setText("HIDE");
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editTextPassword.setSelection(editTextPassword.length());
                }
                else{
                    textViewPasswordShow.setText("SHOW");
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextPassword.setSelection(editTextPassword.length());
                }
            }
        });
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                presenter.validate(username, password);
            }
        });
    }

    private void showSnackBars(String message){
        TSnackbar snackbar = TSnackbar.make(relativeLayout, message, TSnackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setIconLeft(R.drawable.ic_warning, 24);
        snackbar.setIconPadding(8);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#d10202"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
