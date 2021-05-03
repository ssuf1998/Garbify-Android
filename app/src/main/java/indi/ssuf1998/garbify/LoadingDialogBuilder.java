package indi.ssuf1998.garbify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import indi.ssuf1998.garbify.databinding.LoadingDialogBinding;

public class LoadingDialogBuilder {
  private Context context;
  private int screenWidth;
  private int dialogA;

  public LoadingDialogBuilder(Context context) {
    this.context = context;
  }

  public LoadingDialogBuilder() {
    this(null);
  }

  public LoadingDialogBuilder with(Context context) {
    this.context = context;
    DisplayMetrics metrics = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
    screenWidth = metrics.widthPixels;
    dialogA = (int) (screenWidth * 0.3);
    return this;
  }

  public Dialog build() {
    assert context != null;
    Dialog dialog = new Dialog(context, R.style.dialogTransparent);
    LoadingDialogBinding binding = LoadingDialogBinding.inflate(
      ((Activity) context).getLayoutInflater()
    );

    dialog.getWindow().setLayout(dialogA, dialogA);
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
    );
    params.setMargins(dialogA / 4, dialogA / 4, dialogA / 4, dialogA / 4);
    binding.prog.setLayoutParams(params);

    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    dialog.setContentView(binding.getRoot());
    return dialog;
  }
}
