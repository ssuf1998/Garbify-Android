package indi.ssuf1998.garbify.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

public class DBOpenHelper extends DaoMaster.OpenHelper {
  public DBOpenHelper(Context context, String name) {
    super(context, name);
  }

  public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
    super(context, name, factory);
  }

  @Override
  public void onCreate(Database db) {
    super.onCreate(db);
  }
}
