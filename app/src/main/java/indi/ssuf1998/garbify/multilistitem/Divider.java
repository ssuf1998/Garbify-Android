package indi.ssuf1998.garbify.multilistitem;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import indi.ssuf1998.garbify.databinding.DividerBinding;

public class Divider extends BaseItem<Void> {
  private int color;
  private int thickness;
  private int vPadding;
  private int hPadding;

  public Divider(int color, int thickness, int hPadding, int vPadding) {
    super("", "", 0, null, null);
    this.color = color;
    this.thickness = thickness;
    this.hPadding = hPadding;
    this.vPadding = vPadding;
  }

  public Divider(int color) {
    this(color, 2, 0, 12);
  }

  public Divider() {
    this(-1);
  }

  public int getColor() {
    return color;
  }

  public Divider setColor(int color) {
    this.color = color;
    return this;
  }

  public int getThickness() {
    return thickness;
  }

  public Divider setThickness(int thickness) {
    this.thickness = thickness;
    return this;
  }

  public int getHPadding() {
    return hPadding;
  }

  public Divider setHPadding(int val) {
    this.hPadding = val;
    return this;
  }

  public int getVPadding() {
    return vPadding;
  }

  public Divider setVPadding(int val) {
    this.vPadding = val;
    return this;
  }

  @Override
  protected BaseItemVH<?> createViewHolder(@NonNull ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    DividerBinding binding = DividerBinding.inflate(
      inflater, parent, false
    );
    return new BaseItemVH<>(binding);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void bindViewHolder(@NonNull BaseItemVH<?> holder) {
    final BaseItemVH<DividerBinding> mHolder =
      (BaseItemVH<DividerBinding>) holder;

    mHolder.getBinding().dividerLayout.setPadding(
      getHPadding(), getVPadding(),
      getHPadding(), getVPadding()
    );
    if (getColor() != -1) mHolder.getBinding().divider.setBackgroundColor(getColor());
    ViewGroup.LayoutParams params = mHolder.getBinding().divider.getLayoutParams();
    params.height = getThickness();
    mHolder.getBinding().divider.setLayoutParams(params);
  }
}
