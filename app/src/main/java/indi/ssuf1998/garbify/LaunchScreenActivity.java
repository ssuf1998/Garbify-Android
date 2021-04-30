package indi.ssuf1998.garbify;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import indi.ssuf1998.garbify.databinding.ActivityLaunchScreenBinding;
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
    getWindow().getDecorView().setSystemUiVisibility(
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
  }

  @Override
  protected void bindListeners() {

  }
}
