package indi.ssuf1998.garbify;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import indi.ssuf1998.garbify.databinding.ActivityLaunchScreenBinding;
import indi.ssuf1998.garbify.db.DaoMaster;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.UserDao;
import indi.ssuf1998.garbify.db.def.User;
import indi.ssuf1998.garbify.predictor.Predictor;

public class LaunchScreenActivity extends InnerActivity {
  private ActivityLaunchScreenBinding binding;
  private final boolean isFirst = (Boolean) sharedHelper.getData("isFirst", true);

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (isFirst) sharedHelper.putData("isFirst", false, true);
    else {
      goToMainActivity();
      return;
    }

    binding = ActivityLaunchScreenBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initUI();
    initDB();

    ObjectAnimator progressAnim = ObjectAnimator.ofInt(
      binding.loadingProgressBar, "progress", 0, 20
    );
    progressAnim.setDuration(300);
    progressAnim.setInterpolator(new DecelerateInterpolator());
    progressAnim.start();

    Predictor.loadLabels(this);
    Predictor.loadModel(this, (errMsg, interpreter) -> {
      if (errMsg == null) {
        Log.d("Garbify", "model loaded.");

        new Handler().postDelayed(() -> {
          progressAnim.setIntValues(20, 100);
          progressAnim.start();
        }, 500);

        new Handler().postDelayed(this::goToMainActivity, 1000);

      } else Log.d("Garbify", "model err." + errMsg);
    });
  }

  private void goToMainActivity() {
    Intent intent = new Intent(LaunchScreenActivity.this, MainActivity.class);
    startActivity(intent);
    this.finish();
  }

  @Override
  protected void initUI() {

  }

  @Override
  protected void bindListeners() {

  }

  protected void initDB() {
    if (sharedHelper.contains("db_session")) return;

    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "garbify-db");
    Database db = helper.getWritableDb();
    DaoSession session = new DaoMaster(db).newSession();
    sharedHelper.putData("db_session", session);

    UserDao userDao = session.getUserDao();
    User queryUsers = userDao.load(Const.DEFAULT_USER_ID);

    if (queryUsers == null) {
      User defaultUser = new User();
      defaultUser.setId(Const.DEFAULT_USER_ID);
      userDao.insert(defaultUser);
    }

//    queryUsers = userDao.load("000000");
//
//    if (queryUsers == null) {
//      User testUser = new User();
//      testUser.setId("000000");
//      testUser.setPassword("abc000");
//      testUser.setNickname("test");
//      testUser.setExp(270);
//      testUser.setAvatar("");
//
//      userDao.insert(testUser);
//    }

  }
}
