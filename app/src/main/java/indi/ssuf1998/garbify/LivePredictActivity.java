package indi.ssuf1998.garbify;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import indi.ssuf1998.garbify.databinding.ActivityLivePredictBinding;
import indi.ssuf1998.garbify.predictor.PredictAnalyzer;

public class LivePredictActivity extends InnerActivity {
  private ActivityLivePredictBinding binding;

  private ListenableFuture<ProcessCameraProvider> providerFuture;
  private ProcessCameraProvider provider;
  private int deg;
  private Camera camera;
  private final PredictAnalyzer analyzer = new PredictAnalyzer();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLivePredictBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initUI();
    bindListeners();
  }

  @Override
  protected void initDB() {

  }

  @Override
  protected void onPause() {
    super.onPause();
    analyzer.pause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    analyzer.resume();
  }

  @Override
  protected void initUI() {
    providerFuture = ProcessCameraProvider.getInstance(this);
  }

  @Override
  protected void bindListeners() {
    providerFuture.addListener(() -> {
      try {
        provider = providerFuture.get();
        bindCamera();
      } catch (ExecutionException | InterruptedException ignore) {
      }
    }, ContextCompat.getMainExecutor(this));

    analyzer.setPredictListener((predict) -> {
      binding.livePredictProg.setProgress(Math.round(predict.getConfidence() * 100));
      binding.livePredictClassNameText.setText(
        predict.getClassName()
      );
      binding.livePredictTypeNameImg.setImageResource(
        Const.TYPE_DRAWABLE_ARRAY[predict.getType()]
      );
    });

    binding.backImgBtn.setOnClickListener(view -> onBackPressed());

    binding.lockImgBtn.setOnClickListener(view -> {
      if (analyzer.isPause()) {
        binding.lockImgBtn.setChecked(false);
        binding.freezePreviewImg.setImageBitmap(null);
        binding.freezePreviewImg.setVisibility(View.GONE);
        analyzer.resume();
      } else {
        binding.lockImgBtn.setChecked(true);
        binding.freezePreviewImg.setImageBitmap(binding.livePredictView.getBitmap());
        binding.freezePreviewImg.setVisibility(View.VISIBLE);
        analyzer.pause();
      }
    });
  }

  private void bindCamera() {
    final CameraSelector selector = new CameraSelector.Builder()
      .requireLensFacing(CameraSelector.LENS_FACING_BACK)
      .build();

    final Preview preview = new Preview.Builder()
      .build();

    final ImageAnalysis analysis = new ImageAnalysis.Builder()
      .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
      .build();

    analysis.setAnalyzer(ContextCompat.getMainExecutor(this), analyzer);

    final OrientationEventListener mOrientationEventListener =
      new OrientationEventListener(this) {
        @Override
        public void onOrientationChanged(int orientation) {
          if (orientation >= 45 && orientation < 135) {
            deg = Surface.ROTATION_270;
          } else if (orientation >= 135 && orientation < 225) {
            deg = Surface.ROTATION_180;
          } else if (orientation >= 225 && orientation < 315) {
            deg = Surface.ROTATION_90;
          } else {
            deg = Surface.ROTATION_0;
          }

          analysis.setTargetRotation(deg);
        }
      };
    mOrientationEventListener.enable();

    preview.setSurfaceProvider(binding.livePredictView.getSurfaceProvider());
    camera = provider.bindToLifecycle(
      this, selector, preview, analysis);
  }
}