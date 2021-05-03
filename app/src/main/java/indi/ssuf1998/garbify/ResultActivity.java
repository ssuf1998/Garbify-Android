package indi.ssuf1998.garbify;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import indi.ssuf1998.garbify.databinding.ActivityResultBinding;
import indi.ssuf1998.garbify.predictor.ReadablePredict;

import static indi.ssuf1998.garbify.Utils.dp2px;

public class ResultActivity extends InnerActivity {
  private ActivityResultBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityResultBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initUI();
    bindListeners();
  }

  @Override
  protected void initDB() {

  }

  @Override
  @SuppressWarnings("unchecked")
  protected void initUI() {
    ObjectAnimator shadowAnimX = ObjectAnimator.ofFloat(
      binding.previewImgShadow,
      "translationX",
      0, -dp2px(this, 14));
    ObjectAnimator shadowAnimY = ObjectAnimator.ofFloat(
      binding.previewImgShadow,
      "translationY",
      0, -dp2px(this, 14));

    ObjectAnimator previewAnimX = ObjectAnimator.ofFloat(
      binding.previewImg,
      "translationX",
      0, dp2px(this, 14));
    ObjectAnimator previewAnimY = ObjectAnimator.ofFloat(
      binding.previewImg,
      "translationY",
      0, dp2px(this, 14));

    AnimatorSet shadowAnimSet = new AnimatorSet();
    shadowAnimSet.playTogether(shadowAnimX, shadowAnimY,
      previewAnimX, previewAnimY);
    shadowAnimSet.setInterpolator(new DecelerateInterpolator());
    shadowAnimSet.setDuration(500);
    shadowAnimSet.start();

    binding.previewImg.setImageURI(Uri.parse(
      getIntent().getStringExtra("imgPath")
    ));

    List<ReadablePredict> predicts = (List<ReadablePredict>)
      getIntent().getSerializableExtra("predicts");

    binding.classNameText.setText(predicts.get(0).getClassName());
    binding.typeNameImg.setImageResource(
      Const.TYPE_DRAWABLE_ARRAY[predicts.get(0).getType()]
    );

    TextView[] moreClassesTexts = new TextView[]{
      binding.moreClassesOneText,
      binding.moreClassesTwoText,
      binding.moreClassesThreeText
    };
    ProgressBar[] moreClassesProgs = new ProgressBar[]{
      binding.moreClassesOneProg,
      binding.moreClassesTwoProg,
      binding.moreClassesThreeProg
    };
    ObjectAnimator[] progAnims = new ObjectAnimator[3];

    for (int i = 0; i < 3; i++) {
      ReadablePredict predict = predicts.get(i);
      moreClassesTexts[i].setText(predict.getClassName());
      ObjectAnimator progAnim = ObjectAnimator.ofInt(
        moreClassesProgs[i],
        "progress",
        0, Math.round(predict.getConfidence() * 100));
      progAnims[i] = progAnim;
    }

    AnimatorSet progAnimSet = new AnimatorSet();
    progAnimSet.playTogether(progAnims);
    progAnimSet.setDuration(1000);
    progAnimSet.setInterpolator(new DecelerateInterpolator());
    progAnimSet.start();
  }

  @Override
  protected void bindListeners() {

  }
}