package indi.ssuf1998.garbify.multilistitem;

import android.net.Uri;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import indi.ssuf1998.garbify.databinding.HistoryItemBinding;

public class HistoryItem extends BaseItem<Void> {
  private Uri imgUri;
  private long id;

  public HistoryItem(String itemText, String itemSubText, ItemClickListener listener, Uri imgUri) {
    super(itemText, itemSubText, 0, listener, null);
    this.imgUri = imgUri;
  }

  public HistoryItem() {
    this("", "", null, null);
  }

  public Uri getImgUri() {
    return imgUri;
  }

  public HistoryItem setImgUri(Uri imgUri) {
    this.imgUri = imgUri;
    return this;
  }

  public long getId() {
    return id;
  }

  public HistoryItem setId(long id) {
    this.id = id;
    return this;
  }

  @Override
  protected BaseItemVH<?> createViewHolder(@NonNull ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    HistoryItemBinding binding = HistoryItemBinding.inflate(
      inflater, parent, false
    );
    return new BaseItemVH<>(binding);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void bindViewHolder(@NonNull BaseItemVH<?> holder) {
    final BaseItemVH<HistoryItemBinding> mHolder =
      (BaseItemVH<HistoryItemBinding>) holder;

    mHolder.getBinding().itemText.setText(getItemText());
    if (getItemSubText().isEmpty()) {
      mHolder.getBinding().itemSubText.setVisibility(View.GONE);
    } else {
      mHolder.getBinding().itemSubText.setText(getItemSubText());
    }

    mHolder.getBinding().historyImg.setImageURI(getImgUri());
    if (mHolder.getBinding().historyImg.getDrawable() == null)
      mHolder.getBinding().historyImg.setVisibility(View.GONE);
  }
}
