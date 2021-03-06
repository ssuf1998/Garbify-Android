package indi.ssuf1998.garbify.predictor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import indi.ssuf1998.garbify.SharedHelper;
import indi.ssuf1998.garbify.Utils;

public class Predictor {
  private final static String MODEL_FILE_NAME = "garbify_mobilenetv3_model.tflite";

  private Interpreter interpreter;
  private Labels labels;
  private final Context context;

  public Predictor(Context context) {
    this.context = context;

    if (context == null) {
      SharedHelper sharedHelper = SharedHelper.getInstance();

      if (sharedHelper.contains("interpreter")) {
        this.interpreter = (Interpreter) sharedHelper.getData("interpreter");
      }
      if (sharedHelper.contains("labels")) {
        this.labels = (Labels) sharedHelper.getData("labels");
      }
      return;
    }

    loadLabels(context, (errMsg, labels) -> {
      if (errMsg == null) this.labels = labels;
    });
    loadModel(context, (errMsg, interpreter) -> {
      if (errMsg == null) this.interpreter = interpreter;
    });
  }

  public Predictor() {
    this(null);
  }

  public static void loadModel(Context context) {
    loadModel(context, (errMsg, interpreter1) -> {
    });
  }

  public static void loadModel(Context context, ModelLoadedCallback callback) {
    SharedHelper sharedHelper = SharedHelper.getInstance();

    if (sharedHelper.contains("interpreter")) {
      callback.loaded(null, (Interpreter) sharedHelper.getData("interpreter"));
      return;
    }

    try {
      MappedByteBuffer modelFile = FileUtil.loadMappedFile(
        context,
        MODEL_FILE_NAME
      );
      Interpreter interpreter = new Interpreter((ByteBuffer) modelFile);
      sharedHelper.putData("interpreter", interpreter);
      callback.loaded(null, interpreter);
    } catch (IOException e) {
      callback.loaded(e.getMessage(), null);
      e.printStackTrace();
    }
  }

  public static void loadLabels(Context context) {
    loadLabels(context, (errMsg, interpreter1) -> {
    });
  }

  public static void loadLabels(Context context, LabelsLoadedCallback callback) {
    SharedHelper sharedHelper = SharedHelper.getInstance();

    if (sharedHelper.contains("labels")) {
      callback.loaded(null, (Labels) sharedHelper.getData("labels"));
      return;
    }

    try {
      InputStream is = context.getAssets().open("garbage.json");
      Labels labels = new Gson().fromJson(
        IOUtils.toString(is, StandardCharsets.UTF_8),
        Labels.class
      );

      sharedHelper.putData("labels", labels);
      callback.loaded(null, labels);
    } catch (Exception e) {
      callback.loaded(e.getMessage(), null);
      e.printStackTrace();
    }
  }

  public float[] run(@NonNull Bitmap bmp) {
    ImageProcessor processor =
      new ImageProcessor.Builder()
        .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
        .add(new ZeroOneNormalizationOp())
        .build();
    TensorImage tImg = new TensorImage(DataType.FLOAT32);
    tImg.load(bmp);
    tImg = processor.process(tImg);

    TensorBuffer predict =
      TensorBuffer.createFixedSize(new int[]{1, 41}, DataType.FLOAT32);

    interpreter.run(tImg.getBuffer(), predict.getBuffer());
    return predict.getFloatArray();
  }

  public float[] run(@NonNull Uri imgUri) throws IOException {
    assert interpreter != null;
    Bitmap bmp = MediaStore.Images.Media.getBitmap(
      context.getContentResolver(), imgUri);

    return run(bmp);
  }

  public ReadablePredict[] getReadablePredicts(float[] predicts) {
    return getReadablePredicts(predicts, 3);
  }

  public ReadablePredict[] getReadablePredicts(float[] predicts, int count) {
    ArrayList<Float> predictList = new ArrayList<>();
    ArrayList<Float> sortedPredictList = new ArrayList<>();
    for (float v : predicts) {
      predictList.add(v);
      sortedPredictList.add(v);
    }

    sortedPredictList.sort((o1, o2) -> o2.compareTo(o1));
    ReadablePredict[] result = new ReadablePredict[Math.min(count, predicts.length)];

    for (int i = 0; i < result.length; i++) {
      int classIdx = predictList.indexOf(sortedPredictList.get(i));
      Labels.Clazz clazz = labels.getClasses()[classIdx];
      int type = clazz.getType();
      String typeName = labels.getTypes()[type];

      result[i] = new ReadablePredict(
        type, typeName, classIdx, clazz.getName(), sortedPredictList.get(i)
      );
    }
    return result;
  }


  @Nullable
  public Interpreter getInterpreter() {
    return interpreter;
  }

  @Nullable
  public Labels getLabels() {
    return labels;
  }

  public interface ModelLoadedCallback {
    void loaded(@Nullable String errMsg, @Nullable Interpreter interpreter);
  }

  public interface LabelsLoadedCallback {
    void loaded(@Nullable String errMsg, @Nullable Labels labels);
  }
}