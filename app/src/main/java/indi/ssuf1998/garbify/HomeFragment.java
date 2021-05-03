package indi.ssuf1998.garbify;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import indi.ssuf1998.garbify.databinding.FragmentHomeBinding;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.HistoryDao;
import indi.ssuf1998.garbify.db.def.History;
import indi.ssuf1998.garbify.predictor.Predictor;
import indi.ssuf1998.garbify.predictor.ReadablePredict;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends InnerFragment {
  private FragmentHomeBinding binding;
  private Predictor predictor;

  private boolean cameraGranted = false;
  private boolean readExGranted = false;
  private YesNoBottomSheet cameraGrantSheet;
  private YesNoBottomSheet readExGrantSheet;

  private Dialog loadingDialog;

  private HistoryDao historyDao;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    predictor = new Predictor(getActivity());
    initDB();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    binding = FragmentHomeBinding.inflate(inflater, container, false);

    initUI();
    bindListeners();

    return binding.getRoot();
  }


  @Override
  protected void initDB() {
    DaoSession session = (DaoSession) sharedHelper.getData("db_session");
    historyDao = session.getHistoryDao();
  }

  @Override
  protected void initUI() {
    cameraGrantSheet = new YesNoBottomSheet(
      getString(R.string.grant_title),
      getString(R.string.grant_camera_msg)
    );
    cameraGrantSheet.setCancelable(false);

    readExGrantSheet = new YesNoBottomSheet(
      getString(R.string.grant_title),
      getString(R.string.grant_read_ex_msg)
    );
    readExGrantSheet.setCancelable(false);

    loadingDialog = new LoadingDialogBuilder()
      .with(getContext())
      .build();
    loadingDialog.setCancelable(false);
  }

  @Override
  protected void bindListeners() {
    binding.pickAPicBtn.setOnClickListener(view -> {
      readExGranted = askForReadEx(true);
      if (!readExGranted) {
        readExGrantSheet.show(getActivity().getSupportFragmentManager());
        return;
      }

      final Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, null),
        Const.PICK_IMG_REQUEST);
    });

    cameraGrantSheet.setBtnClickListener((id, view) -> {
      if (id == YesNoBottomSheet.ButtonId.YES_BTN) {
        cameraGranted = askForCamera(false);
      }
      cameraGrantSheet.dismiss();
    });

    readExGrantSheet.setBtnClickListener((id, view) -> {
      if (id == YesNoBottomSheet.ButtonId.YES_BTN) {
        readExGranted = askForReadEx(false);
      }
      readExGrantSheet.dismiss();
    });

    binding.livePredictBtn.setOnClickListener(view -> {
      cameraGranted = askForCamera(true);
      if (!cameraGranted) {
        cameraGrantSheet.show(getActivity().getSupportFragmentManager());
        return;
      }
      Intent intent = new Intent(getActivity(), LivePredictActivity.class);
      startActivity(intent);
    });
  }

  private boolean askForReadEx(boolean justAsk) {
    return askForPermission(
      justAsk,
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Const.ASK_FOR_READ_EX
    );
  }

  private boolean askForCamera(boolean justAsk) {
    return askForPermission(
      justAsk,
      Manifest.permission.CAMERA,
      Const.ASK_FOR_CAMERA
    );
  }

  private boolean askForPermission(boolean justAsk, String what2Granted, int code) {
    int already = ContextCompat.checkSelfPermission(
      getActivity().getApplication(),
      what2Granted
    );
    if (already != PackageManager.PERMISSION_GRANTED) {
      if (!justAsk) {
        ActivityCompat.requestPermissions(
          getActivity(),
          new String[]{what2Granted},
          code);
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
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == Const.PICK_IMG_REQUEST) {
        final Uri uri = data.getData();

        loadingDialog.show();

        Thread thread = new Thread() {
          @Override
          public void run() {
            try {
              String realPath = ImageFilePath.getPath(getContext(), uri);
              File imgCacheFile = Utils.getImgCacheFile(getContext(), "history");

              Bitmap img = new Compressor(getContext())
                .setMaxWidth(128)
                .setQuality(50)
                .compressToBitmap(new File(realPath));
              FileOutputStream out = new FileOutputStream(imgCacheFile.getAbsolutePath());
              img.compress(Bitmap.CompressFormat.JPEG, 50, out);

              List<ReadablePredict> predicts = Arrays.asList(
                predictor.getReadablePredicts(predictor.run(uri))
              );
              Intent intent = new Intent(getActivity(), ResultActivity.class);
              intent.putExtra("predicts", (Serializable) predicts);
              intent.putExtra("imgPath", imgCacheFile.getAbsolutePath());

              insertHistory(imgCacheFile.getAbsolutePath(), (ReadablePredict[]) predicts.toArray());
              binding.getRoot().post(loadingDialog::dismiss);
              startActivity(intent);
            } catch (Exception e) {
              binding.getRoot().post(()-> Toasty.error(
                getActivity(),
                Optional.ofNullable(e.getMessage()).orElse("Unknown error."),
                Toast.LENGTH_LONG
              ).show());
            }
          }
        };

        thread.start();
      }

    }
  }

  private void insertHistory(String imgPath, ReadablePredict[] predicts) throws IOException {
    History history = new History();
    history.setReadablePredictsJson(new Gson().toJson(predicts));
    history.setPic(imgPath);
    history.setUserId(
      preferences.getString(Const.SP_LOGGED_USER_ID_KEY, Const.DEFAULT_USER_ID)
    );
    history.setCreateTime(System.currentTimeMillis());

    historyDao.insert(history);
  }

}