package indi.ssuf1998.garbify.predictor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.nio.ByteBuffer;
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

    Bitmap bmp = ImageProxy2Bmp(image);
    mPredictListener.predict(predictor.getReadablePredicts(predictor.run(bmp), 1)[0]);

    image.close();
    lastTimestamp = cur;
  }

  // https://stackoverflow.com/questions/56772967/converting-imageproxy-to-bitmap
  // https://stackoverflow.com/questions/28430024/convert-android-media-image-yuv-420-888-to-bitmap
  private Bitmap ImageProxy2Bmp(ImageProxy image) {
    final ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
    final ByteBuffer vBuffer = image.getPlanes()[1].getBuffer();
    final ByteBuffer uBuffer = image.getPlanes()[2].getBuffer();

    final int ySize = yBuffer.remaining();
    final int vSize = vBuffer.remaining();
    final int uSize = uBuffer.remaining();

    byte[] data = new byte[ySize + vSize + uSize];

    yBuffer.get(data, 0, ySize);
    vBuffer.get(data, ySize, vSize);
    uBuffer.get(data, ySize + vSize, uSize);

    final Context c = MainActivity.getWeakRef().get();

    final RenderScript rs = RenderScript.create(c);
    final Bitmap bmp = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
    final Allocation allocationRgb = Allocation.createFromBitmap(rs, bmp);

    final Allocation allocationYuv = Allocation.createSized(rs, Element.U8(rs), data.length);
    allocationYuv.copyFrom(data);

    ScriptIntrinsicYuvToRGB scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    scriptYuvToRgb.setInput(allocationYuv);
    scriptYuvToRgb.forEach(allocationRgb);

    allocationRgb.copyTo(bmp);

    allocationYuv.destroy();
    allocationRgb.destroy();
    rs.destroy();

    Matrix matrix = new Matrix();
    matrix.postRotate(image.getImageInfo().getRotationDegrees());
    return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
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
