package indi.ssuf1998.garbify.predictor;

class Labels {
  private Clazz[] classes;
  private String[] types;

  public Clazz[] getClasses() {
    return classes;
  }

  public void setClasses(Clazz[] classes) {
    this.classes = classes;
  }

  public String[] getTypes() {
    return types;
  }

  public void setTypes(String[] types) {
    this.types = types;
  }

  static class Clazz {
    private int type;
    private String name;

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
