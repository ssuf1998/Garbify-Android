package indi.ssuf1998.garbify.predictor;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat;

public class ZeroOneNormalizationOp implements TensorOperator {
  @Override
  public TensorBuffer apply(TensorBuffer input) {
    int[] shape = input.getShape();
    float[] values = input.getFloatArray();

    for (int i = 0; i < values.length; ++i) {
      values[i] = values[i] / 255;
    }

    TensorBuffer output;
    if (input.isDynamic()) {
      output = TensorBufferFloat.createDynamic(DataType.FLOAT32);
    } else {
      output = TensorBufferFloat.createFixedSize(shape, DataType.FLOAT32);
    }

    output.loadArray(values, shape);
    return output;
  }
}
