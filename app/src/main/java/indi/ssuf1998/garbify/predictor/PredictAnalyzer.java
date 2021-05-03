package indi.ssuf1998.garbify.predictor;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.util.Arrays;
import java.util.List;

import indi.ssuf1998.garbify.MainActivity;

public class PredictAnalyzer implements ImageAnalysis.Analyzer {
  private final Predictor predictor = new Predictor();
  private static final List<Integer> SupportYuvFormats = Arrays.asList(
    ImageFormat.YUV_420_888,
    ImageFormat.YUV_422_888,
    ImageFormat.YUV_444_888
  );

  private static final int analyzeDelay = 1000;
  private long lastTimestamp;

  private boolean pause;

  private PredictListener mPredictListener;
  YuvToRgbConverter converter = new YuvToRgbConverter(MainActivity.getWeakRef().get());


  @SuppressLint("UnsafeOptInUsageError")
  @Override
  public void analyze(@NonNull ImageProxy image) {
    if (!SupportYuvFormats.contains(image.getFormat())) {
      return;
    }

    final long cur = System.currentTimeMillis();
    if (pause || cur - lastTimestamp <= analyzeDelay) {
      image.close();
      return;
    }
    Bitmap bmp = Bitmap.createBitmap(
      image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

    Image realImg = image.getImage();
    if (realImg != null) {
      Matrix matrix = new Matrix();
      matrix.postRotate(image.getImageInfo().getRotationDegrees());
      converter.yuvToRgb(image.getImage(), bmp);
      bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
      mPredictListener.predict(predictor.getReadablePredicts(predictor.run(bmp), 1)[0]);
    }

    image.close();
    lastTimestamp = cur;
  }


  public interface PredictListener {
    void predict(ReadablePredict predict);
  }

  public PredictListener getPredictListener() {
    return mPredictListener;
  }

  public void setPredictListener(PredictListener listener) {
    this.mPredictListener = listener;
  }

  public void pause() {
    pause = true;
  }

  public void resume() {
    pause = false;
  }

  public boolean isPause() {
    return pause;
  }
}
