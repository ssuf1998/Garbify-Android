package indi.ssuf1998.garbify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

import es.dmoral.toasty.Toasty;
import indi.ssuf1998.garbify.databinding.ActivityLogInBinding;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.UserDao;
import indi.ssuf1998.garbify.db.def.User;

public class LogInActivity extends InnerActivity implements Validator.ValidationListener {
  private ActivityLogInBinding binding;

  @NotEmpty(trim = true, message = "此项必填")
  @Length(trim = true, max = 6, min = 6, message = "应为6位")
  @Order(1)
  private EditText idInput;

  @NotEmpty(trim = true, message = "此项必填")
  @Length(trim = true, max = 12, min = 6, message = "应为6-12位")
  @Pattern(regex = "(?=.*[a-zA-z])(?=.*\\d).+", message = "不应包含特殊字符")
  @Order(2)
  private EditText passwordInput;

  private Validator validator;

  private UserDao userDao;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLogInBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initDB();
    initUI();
    bindListeners();
  }

  @Override
  protected void initDB() {
    DaoSession session = (DaoSession) sharedHelper.getData("db_session");
    userDao = session.getUserDao();
  }

  @Override
  protected void initUI() {
    idInput = binding.idInput;
    passwordInput = binding.passwordInput;
  }

  @Override
  protected void bindListeners() {
    validator = new Validator(this);
    validator.setValidationListener(this);

    binding.go2SignUpBtn.setOnClickListener(v -> {
      Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
      startActivity(intent);
    });

    binding.logInBtn.setOnClickListener(v -> validator.validate());
  }

  @Override
  public void onValidationSucceeded() {
    List<User> queryUsers = userDao.queryBuilder()
      .where(
        UserDao.Properties.Id.eq(
          binding.idInput.getText().toString()
        ),
        UserDao.Properties.Password.eq(
          binding.passwordInput.getText().toString()
        )
      )
      .limit(1)
      .list();

    if (queryUsers.size() == 0) {
      Toasty.error(
        this,
        getString(R.string.log_in_no_user_err_msg),
        Toast.LENGTH_LONG
      ).show();
      binding.idInput.setText("");
      binding.passwordInput.setText("");
      binding.idInput.requestFocus();
      return;
    }

    Toasty.success(
      this,
      getString(R.string.log_in_suc_msg),
      Toast.LENGTH_SHORT
    ).show();
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(Const.SP_LOGGED_USER_ID_KEY, queryUsers.get(0).getId());
    editor.apply();

    setResult(Const.LOG_IN_SUCCEED);
    finish();
  }

  @Override
  public void onValidationFailed(List<ValidationError> errors) {
    for (ValidationError error : errors) {
      String msg = error.getCollatedErrorMessage(this);
      if (error.getView() instanceof EditText) {
        ((EditText) error.getView()).setError(msg);
      }
    }
  }
}