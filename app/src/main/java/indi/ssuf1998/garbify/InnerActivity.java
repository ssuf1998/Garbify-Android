package indi.ssuf1998.garbify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public abstract class InnerActivity extends AppCompatActivity {
  protected final SharedHelper sharedHelper = SharedHelper.getInstance();
  protected SharedPreferences preferences;

  private static WeakReference<Context> weakRef;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    weakRef = new WeakReference<>(this);
    preferences = getSharedPreferences(Const.SP_NAME, MODE_PRIVATE);
  }

  protected abstract void initDB();

  protected abstract void initUI();

  protected abstract void bindListeners();

  public static WeakReference<Context> getWeakRef() {
    return weakRef;
  }

}
