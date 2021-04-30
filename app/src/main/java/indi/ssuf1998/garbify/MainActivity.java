package indi.ssuf1998.garbify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import es.dmoral.toasty.Toasty;
import indi.ssuf1998.garbify.databinding.ActivityMainBinding;
import indi.ssuf1998.garbify.predictor.Predictor;
import indi.ssuf1998.garbify.predictor.ReadablePredict;

public class MainActivity extends InnerActivity {
  private ActivityMainBinding binding;
  private Predictor predictor;

  private boolean cameraGranted = false;
  private YesNoBottomSheet cameraGrantSheet;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initUI();
    bindListeners();

    predictor = new Predictor(this);
  }

  @Override
  protected void initUI() {
    cameraGrantSheet = new YesNoBottomSheet(
      getString(R.string.grant_title),
      getString(R.string.grant_camera_msg)
    );
  }

  @Override
  protected void bindListeners() {
    binding.pickAPicBtn.setOnClickListener(view -> {
      final Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, null),
        Const.PICK_IMG_REQUEST);
    });

    cameraGrantSheet.setBtnClickListener((id, view) -> {
      if (id == YesNoBottomSheet.ButtonId.YES_BTN) {
        cameraGranted = askForPermissions(false);
      }
      cameraGrantSheet.dismiss();
    });

    binding.livePredictBtn.setOnClickListener(view -> {
      cameraGranted = askForPermissions(true);
      if (!cameraGranted) {
        cameraGrantSheet.show(getSupportFragmentManager());
        return;
      }
      Intent intent = new Intent(MainActivity.this, LivePredictActivity.class);
      startActivity(intent);
    });
  }

  private boolean askForPermissions(boolean justAsk) {
    int hasCameraPermission = ContextCompat.checkSelfPermission(
      getApplication(),
      Manifest.permission.CAMERA
    );
    if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
      if (!justAsk) {
        ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.CAMERA},
          Const.ASK_FOR_CAMERA);
      }
      return false;
    }
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode == Const.ASK_FOR_CAMERA) {
      if (grantResults.length > 0 &&
        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        cameraGranted = true;
        binding.livePredictBtn.performClick();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      if (requestCode == Const.PICK_IMG_REQUEST) {
        try {
          assert data != null;
          final Uri uri = data.getData();
          List<ReadablePredict> predicts = Arrays.asList(
            predictor.getReadablePredicts(predictor.run(uri))
          );
          Intent intent = new Intent(MainActivity.this, ResultActivity.class);
          intent.putExtra("predicts", (Serializable) predicts);
          intent.putExtra("imgUri", uri);
          startActivity(intent);
        } catch (Exception e) {
          Toasty.error(
            MainActivity.this,
            Optional.ofNullable(e.getMessage()).orElse("Unknown error."),
            Toast.LENGTH_SHORT
          ).show();
        }
      }
    }
  }

}