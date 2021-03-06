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
  private String msgText;
  private boolean onlyYes;
  private BtnClickListener mBtnClickListener = (id, view) -> {
  };

  private String yesText;
  private String noText;

  public YesNoBottomSheet(String titleText, String subTitleText, String msgText, boolean onlyYes) {
    super(titleText, subTitleText);
    this.msgText = msgText;
  }

  public YesNoBottomSheet(String titleText, String msgText) {
    this(titleText, "", msgText, false);
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
    binding.noBtn.setVisibility(onlyYes ? View.GONE : View.VISIBLE);
  }


  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void bindListeners() {
    super.bindListeners();

    if (yesText != null) binding.yesBtn.setText(yesText);
    if (noText != null) binding.noBtn.setText(noText);

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

  public String getMsgText() {
    return msgText;
  }

  public YesNoBottomSheet setMsgText(String msgText) {
    this.msgText = msgText;
    return this;
  }

  public boolean isOnlyYes() {
    return onlyYes;
  }

  public YesNoBottomSheet setOnlyYes(boolean onlyYes) {
    this.onlyYes = onlyYes;
    return this;
  }

  public String getYesText() {
    return yesText;
  }

  public YesNoBottomSheet setYesText(String yesText) {
    this.yesText = yesText;
    return this;
  }

  public String getNoText() {
    return noText;
  }

  public YesNoBottomSheet setNoText(String noText) {
    this.noText = noText;
    return this;
  }
}
