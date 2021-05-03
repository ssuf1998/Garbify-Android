package indi.ssuf1998.garbify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;
import java.util.Random;

import indi.ssuf1998.garbify.databinding.ActivitySignUpBinding;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.UserDao;
import indi.ssuf1998.garbify.db.def.User;

public class SignUpActivity extends InnerActivity implements Validator.ValidationListener {
  private ActivitySignUpBinding binding;

  @NotEmpty(trim = true, message = "此项必填")
  @Length(trim = true, max = 16, message = "请选用长度小于16位的昵称")
  @Pattern(regex = "(?=.*[a-zA-z\\u4E00-\\u9FFF]).+", message = "包括特殊字符")
  @Order(1)
  private EditText nicknameInput;

  @NotEmpty(trim = true, message = "此项必填")
  @Length(trim = true, max = 12, min = 6, message = "请选用长度6-12位的密码")
  @Pattern(regex = "(?=.*[a-zA-z])(?=.*\\d).+", message = "密码过于简单，或包含特殊字符")
  @Password(min = 0, message = "")
  @Order(2)
  private EditText passwordInput;

  @ConfirmPassword(message = "密码不一致")
  @Order(3)
  private EditText passwordAgainInput;

  private Validator validator;
  private UserDao userDao;
  private String userId;
  private YesNoBottomSheet showIdSheet;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivitySignUpBinding.inflate(getLayoutInflater());
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
    nicknameInput = binding.nicknameInput;
    passwordInput = binding.passwordInput;
    passwordAgainInput = binding.passwordAgainInput;

    showIdSheet = new YesNoBottomSheet(
      getString(R.string.show_id_title), ""
    )
      .setOnlyYes(true);
    showIdSheet.setCancelable(false);
  }

  @Override
  protected void bindListeners() {
    validator = new Validator(this);
    validator.setValidationListener(this);

    binding.signUpBtn.setOnClickListener(view -> validator.validate());

    showIdSheet.setBtnClickListener((id, view) -> finish());
    binding.go2LogInBtn.setOnClickListener(view -> finish());
  }


  @Override
  public void onValidationSucceeded() {
    insertUser();
    showIdSheet
      .setMsgText(String.format(getString(R.string.show_id_msg), userId))
      .show(getSupportFragmentManager());
  }

  private void insertUser() {
    userId = Utils.getRandomDigitStr(6);
    User queryUsers = userDao.load(userId);

    if (queryUsers != null) {
      insertUser();
      return;
    }

    User user = new User();
    user.setId(userId);
    user.setExp(0);
    user.setNickname(binding.nicknameInput.getText().toString());
    user.setPassword(binding.passwordInput.getText().toString());
    user.setAvatar("");
    userDao.insert(user);
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