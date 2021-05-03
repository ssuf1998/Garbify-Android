package indi.ssuf1998.garbify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class InnerFragment extends Fragment {
  protected final SharedHelper sharedHelper = SharedHelper.getInstance();
  protected SharedPreferences preferences;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    preferences = getActivity().getSharedPreferences(
      Const.SP_NAME, Context.MODE_PRIVATE
    );
  }

  protected abstract void initDB();

  protected abstract void initUI();

  protected abstract void bindListeners();
}
