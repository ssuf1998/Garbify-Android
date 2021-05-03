package indi.ssuf1998.garbify;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hjq.bar.OnTitleBarListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import es.dmoral.toasty.Toasty;
import indi.ssuf1998.garbify.databinding.ActivityHistoryBinding;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.HistoryDao;
import indi.ssuf1998.garbify.db.UserDao;
import indi.ssuf1998.garbify.db.def.History;
import indi.ssuf1998.garbify.db.def.User;
import indi.ssuf1998.garbify.multilistitem.Adapter;
import indi.ssuf1998.garbify.multilistitem.BaseItem;
import indi.ssuf1998.garbify.multilistitem.HistoryItem;
import indi.ssuf1998.garbify.multilistitem.ItemTouchCallback;
import indi.ssuf1998.garbify.predictor.ReadablePredict;

public class HistoryActivity extends InnerActivity {
  ActivityHistoryBinding binding;
  private final List<BaseItem<?>> items = new ArrayList<>();
  private Adapter adapter;
  private static final SimpleDateFormat dateFormat =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

  private YesNoBottomSheet clearHistorySheet;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityHistoryBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initDB();
    initUI();
    bindListeners();
  }

  @Override
  protected void initDB() {
    String loggedUserId = preferences.getString(
      Const.SP_LOGGED_USER_ID_KEY, Const.DEFAULT_USER_ID
    );

    DaoSession session = (DaoSession) sharedHelper.getData("db_session");
    HistoryDao historyDao = session.getHistoryDao();

    QueryBuilder<History> qb = historyDao.queryBuilder();
    qb.orderDesc(HistoryDao.Properties.CreateTime);
    qb.join(HistoryDao.Properties.UserId, User.class)
      .where(UserDao.Properties.Id.eq(loggedUserId));
    List<History> histories = qb.list();
    if (histories.size() != 0) {
      for (History h : histories) {
        ReadablePredict predict = new Gson().fromJson(
          h.getReadablePredictsJson(), ReadablePredict[].class
        )[0];
        items.add(
          new HistoryItem()
            .setId(h.getId())
            .setImgUri(Uri.parse(Optional.ofNullable(h.getPic()).orElse("")))
            .setItemText(predict.getClassName())
            .setItemSubText(
              String.format(
                "%s %s",
                predict.getTypeName(),
                dateFormat.format(new Date(h.getCreateTime()))
              )
            )
        );
      }
    }

  }

  @Override
  protected void initUI() {
    adapter = new Adapter(items);
    binding.historyRecycler.setLayoutManager(new LinearLayoutManager(this));
    binding.historyRecycler.setAdapter(adapter);

    ItemTouchHelper.Callback callback = new ItemTouchCallback(adapter);
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(binding.historyRecycler);

    clearHistorySheet = new YesNoBottomSheet(
      getString(R.string.clear_history_title),
      getString(R.string.clear_history_msg)
    );
  }

  @Override
  protected void bindListeners() {
    HistoryActivity that = this;

    adapter.setListener(new ItemTouchCallback.Callback() {
      @Override
      public void onDelete(int position) {
        HistoryItem item = (HistoryItem) items.get(position);
        DaoSession session = (DaoSession) sharedHelper.getData("db_session");
        session.getHistoryDao().deleteByKey(item.getId());
        new File(item.getImgUri().getPath()).delete();
      }
    });

    clearHistorySheet.setBtnClickListener((id, view) -> {
      if (id == YesNoBottomSheet.ButtonId.YES_BTN) {
        DaoSession session = (DaoSession) sharedHelper.getData("db_session");
        session.getHistoryDao().deleteAll();
        items.clear();
        adapter.notifyDataSetChanged();
        File historyImgCacheDir = Utils.getImgCacheFile(this, "history").getParentFile();
        for (File f : historyImgCacheDir.listFiles()) {
          f.delete();
        }
      }
      clearHistorySheet.dismiss();
    });

    binding.historyTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
      @Override
      public void onLeftClick(View view) {
        finish();
      }

      @Override
      public void onTitleClick(View view) {

      }

      @Override
      public void onRightClick(View view) {
        if (items.size() != 0) {
          clearHistorySheet.show(getSupportFragmentManager());
        } else {
          Toasty.info(
            that,
            getString(R.string.history_is_blank_msg),
            Toast.LENGTH_SHORT
          ).show();
        }
      }
    });
  }
}