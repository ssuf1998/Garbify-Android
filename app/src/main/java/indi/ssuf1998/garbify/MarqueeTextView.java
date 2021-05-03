package indi.ssuf1998.garbify;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
  public MarqueeTextView(@NonNull Context context) {
    super(context);
  }

  public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    if (focused)
      super.onFocusChanged(focused, direction, previouslyFocusedRect);
  }

  @Override
  public void onWindowFocusChanged(boolean focused) {
    if (focused)
      super.onWindowFocusChanged(focused);
  }

  @Override
  public boolean isFocused() {
    return true;
  }
}
