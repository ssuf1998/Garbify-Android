package indi.ssuf1998.garbify.multilistitem;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchCallback extends ItemTouchHelper.Callback {
  private final Callback callback;
//  private Paint paint;
//  private final Drawable ico = ContextCompat.getDrawable(
//    MainActivity.getWeakRef().get(),
//    R.drawable.ic_trash
//  );
//  private final float icoPadding = Utils.dp2px(8);

  public ItemTouchCallback(Callback callback) {
    this.callback = callback;
//    ico.setTint(Color.WHITE);
  }

  @Override
  public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
    return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
  }

  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
    return false;
  }

  @Override
  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    callback.onDelete(viewHolder.getAdapterPosition());
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//    View itemView = viewHolder.itemView;
//
//    paint = new Paint();
//    paint.setColor(Color.RED);
//    paint.setStyle(Paint.Style.FILL);
//
//
//
//    int left = dX > 0 ? itemView.getLeft() : (int) (itemView.getWidth() + dX);
//    int right = dX > 0 ? (int) dX : itemView.getWidth();
//
//    RectF bgRect = new RectF(
//      left,
//      itemView.getTop(),
//      right,
//      itemView.getBottom()
//    );
//    c.drawRect(bgRect, paint);
//
//    ico.setBounds();
//    ico.draw(c);
  }

  public interface Callback {
    void onDelete(int position);
  }
}
