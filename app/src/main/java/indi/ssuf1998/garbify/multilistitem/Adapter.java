package indi.ssuf1998.garbify.multilistitem;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<BaseItemVH<?>> implements ItemTouchCallback.Callback {
  private final List<BaseItem<?>> items;
  private ItemTouchCallback.Callback listener;

  public Adapter(List<BaseItem<?>> items, ItemTouchCallback.Callback listener) {
    this.items = items;
    this.listener = listener;
  }

  public Adapter(List<BaseItem<?>> items) {
    this(items, position -> {
    });
  }

  @NonNull
  @Override
  public BaseItemVH<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    BaseItemVH<?> vh = null;
    for (BaseItem<?> item : items) {
      if (item.hashCode() == viewType) {
        vh = item.createViewHolder(parent);
      }
    }
    return Objects.requireNonNull(vh);
  }

  @Override
  public void onBindViewHolder(@NonNull BaseItemVH<?> holder, int position) {
    items.get(position).bindViewHolder(holder);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @Override
  public int getItemViewType(int position) {
    return items.get(position).hashCode();
  }

  public ItemTouchCallback.Callback getListener() {
    return listener;
  }

  public void setListener(ItemTouchCallback.Callback listener) {
    this.listener = listener;
  }

  @Override
  public void onDelete(int position) {
    listener.onDelete(position);
    items.remove(position);
    notifyItemRemoved(position);
  }
}
