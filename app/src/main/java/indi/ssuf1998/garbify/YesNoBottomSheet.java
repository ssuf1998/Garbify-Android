package indi.ssuf1998.garbify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import indi.ssuf1998.bhbottomsheet.BaseBottomSheet;
import indi.ssuf1998.garbify.databinding.YesNoBottomSheetBinding;

public class YesNoBottomSheet extends BaseBottomSheet {
  public static class ButtonId {
    public static int YES_BTN = 0;
    public static int NO_BTN = 1;
  }

  private YesNoBottomSheetBinding binding;
  private final String msgText;
  private BtnClickListener mBtnClickListener = (id, view) -> {
  };

  public YesNoBottomSheet(String titleText, String subTitleText, String msgText) {
    super(titleText, subTitleText);
    this.msgText = msgText;
  }

  public YesNoBottomSheet(String titleText, String msgText) {
    this(titleText, "", msgText);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    final View fatherView = super.onCreateView(inflater, container, savedInstanceState);
    assert fatherView != null;
    final LinearLayout slotView = fatherView.findViewById(R.id.slotView);
    binding = YesNoBottomSheetBinding.inflate(inflater, slotView, true);

    return fatherView;
  }

  @Override
  public void onStart() {
    super.onStart();
    initUI();
    bindListeners();
  }


  @Override
  protected void initUI() {
    super.initUI();
    binding.msgText.setText(msgText);
  }


  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void bindListeners() {
    super.bindListeners();

    binding.yesBtn.setOnClickListener(view -> mBtnClickListener.click(ButtonId.YES_BTN, view));
    binding.noBtn.setOnClickListener(view -> mBtnClickListener.click(ButtonId.NO_BTN, view));

    binding.msgText.setMovementMethod(ScrollingMovementMethod.getInstance());
    binding.msgText.setOnTouchListener((v, event) -> {
      binding.msgText.getParent().requestDisallowInterceptTouchEvent(
        binding.msgText.canScrollVertically(-1));
      return false;
    });
  }


  public interface BtnClickListener {
    void click(int id, View view);
  }

  public BtnClickListener getBtnClickListener() {
    return mBtnClickListener;
  }

  public void setBtnClickListener(BtnClickListener listener) {
    this.mBtnClickListener = listener;
  }
}
