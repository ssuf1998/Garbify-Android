package indi.ssuf1998.garbify.multilistitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import indi.ssuf1998.garbify.databinding.SimpleItemBinding;

public abstract class BaseItem<T> {
  protected String itemText;
  protected String itemSubText;
  protected int itemIcon;
  protected ItemClickListener mItemClickListener;

  protected T value;

  public BaseItem(String itemText, String itemSubText, int itemIcon,
                  ItemClickListener listener, T value) {
    this.itemText = itemText;
    this.itemSubText = itemSubText;
    this.itemIcon = itemIcon;
    this.mItemClickListener = listener;
    this.value = value;
  }

  public BaseItem(String itemText, T value) {
    this(itemText, "", 0, null, value);
  }

  public BaseItem(String itemText, ItemClickListener listener, T value) {
    this(itemText, "", 0, listener, value);
  }

  public interface ItemClickListener {
    void click(View view);
  }

  public String getItemText() {
    return itemText;
  }

  public BaseItem<T> setItemText(String itemText) {
    this.itemText = itemText;
    return this;
  }

  public String getItemSubText() {
    return itemSubText;
  }

  public BaseItem<T> setItemSubText(String itemSubText) {
    this.itemSubText = itemSubText;
    return this;
  }

  public int getItemIcon() {
    return itemIcon;
  }

  public BaseItem<T> setItemIcon(int itemIcon) {
    this.itemIcon = itemIcon;
    return this;
  }

  public ItemClickListener getOnItemClickListener() {
    return mItemClickListener;
  }

  public BaseItem<T> setOnItemClickListener(ItemClickListener listener) {
    this.mItemClickListener = listener;
    return this;
  }

  public T getValue() {
    return value;
  }

  public BaseItem<T> setValue(T value) {
    this.value = value;
    return this;
  }

  protected abstract BaseItemVH<?> createViewHolder(@NonNull ViewGroup parent);

  protected abstract void bindViewHolder(@NonNull BaseItemVH<?> holder);

  public static class SimpleItem extends BaseItem<Void> {
    public SimpleItem(String itemText, String itemSubText, int itemIcon,
                      ItemClickListener listener, Void value) {
      super(itemText, itemSubText, itemIcon, listener, value);
    }

    public SimpleItem(String itemText, Void value) {
      this(itemText, "", 0, null, value);
    }

    public SimpleItem(String itemText, ItemClickListener listener, Void value) {
      this(itemText, "", 0, listener, value);
    }

    @Override
    protected BaseItemVH<?> createViewHolder(@NonNull ViewGroup parent) {
      final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      SimpleItemBinding binding = SimpleItemBinding.inflate(
        inflater, parent, false
      );
      return new BaseItemVH<>(binding);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void bindViewHolder(@NonNull BaseItemVH<?> holder) {
      final BaseItemVH<SimpleItemBinding> mHolder =
        (BaseItemVH<SimpleItemBinding>) holder;

      if (getItemIcon() != 0) {
        mHolder.getBinding().itemIcon.setImageResource(getItemIcon());
      }

      mHolder.getBinding().itemText.setText(getItemText());
      if (getItemSubText().isEmpty()) {
        mHolder.getBinding().itemSubText.setVisibility(View.GONE);
      } else {
        mHolder.getBinding().itemSubText.setText(getItemSubText());
      }

      holder.itemView.setOnClickListener(v -> {
        if (getOnItemClickListener() != null) {
          getOnItemClickListener().click(v);
        }
      });
    }
  }
}
