package indi.ssuf1998.garbify.multilistitem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BaseItemVH<T extends ViewBinding> extends RecyclerView.ViewHolder {
  private final T binding;

  public BaseItemVH(@NonNull T binding) {
    super(binding.getRoot());
    this.binding = binding;
  }

  public T getBinding() {
    return binding;
  }
}

