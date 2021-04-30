package indi.ssuf1998.garbify.predictor;

import java.io.Serializable;

public class ReadablePredict implements Serializable {
  private int type;
  private String className;
  private int classIdx;
  private String typeName;
  private float confidence;

  public ReadablePredict() {
    this(0, null, 0, null, 0);
  }

  public ReadablePredict(int type, String typeName, int classIdx, String className, float confidence) {
    this.type = type;
    this.className = className;
    this.classIdx = classIdx;
    this.typeName = typeName;
    this.confidence = confidence;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public int getClassIdx() {
    return classIdx;
  }

  public void setClassIdx(int classIdx) {
    this.classIdx = classIdx;
  }

  public float getConfidence() {
    return confidence;
  }

  public void setConfidence(float confidence) {
    this.confidence = confidence;
  }

  @Override
  public String toString() {
    return "ReadablePredict{" +
      "type=" + type +
      ", className='" + className + '\'' +
      ", classIdx=" + classIdx +
      ", typeName='" + typeName + '\'' +
      ", confidence=" + confidence +
      '}';
  }
}
