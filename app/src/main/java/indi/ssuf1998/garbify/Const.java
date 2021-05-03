package indi.ssuf1998.garbify;

public class Const {
  public static final int PICK_IMG_REQUEST = 100;
  public static final int ASK_FOR_CAMERA = 101;
  public static final int ASK_FOR_READ_EX = 102;
  public static final int LOG_IN_SUCCEED = 103;

  public static final int[] TYPE_DRAWABLE_ARRAY = new int[]{
    R.drawable.ic_text_dry,
    R.drawable.ic_text_wet,
    R.drawable.ic_text_recyclable,
    R.drawable.ic_text_hazardous,
  };

  public final static String SP_NAME = "garbify_preferences";
  public final static String SP_LOGGED_USER_ID_KEY = "logged_user_account";

  public final static String DEFAULT_USER_ID = "00000";
}
