package indi.ssuf1998.garbify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import indi.ssuf1998.garbify.databinding.FragmentUserBinding;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.UserDao;
import indi.ssuf1998.garbify.db.def.User;
import indi.ssuf1998.garbify.multilistitem.Adapter;
import indi.ssuf1998.garbify.multilistitem.BaseItem;
import indi.ssuf1998.garbify.multilistitem.Divider;

public class UserFragment extends InnerFragment {
  private FragmentUserBinding binding;
  private final List<BaseItem<?>> items = new ArrayList<>();

  private YesNoBottomSheet aboutMeSheet;
  private YesNoBottomSheet signOutSheet;

  private UserDao userDao;
  private String userId;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDB();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentUserBinding.inflate(inflater, container, false);

    initUI();
    bindListeners();

    return binding.getRoot();
  }

  @Override
  protected void initDB() {
    DaoSession session = (DaoSession) sharedHelper.getData("db_session");
    userDao = session.getUserDao();
  }

  @Override
  protected void initUI() {
    items.add(
      new BaseItem.SimpleItem(getString(R.string.predict_history_text), null)
        .setItemIcon(R.drawable.ic_history)
        .setOnItemClickListener(view -> {
          Intent intent = new Intent(getActivity(), HistoryActivity.class);
          startActivity(intent);
        })
    );
    items.add(
      new BaseItem.SimpleItem(getString(R.string.garbage_knowledge_text), null)
        .setItemIcon(R.drawable.ic_book)
    );
//    items.add(
//      new BaseItem.SimpleItem(getString(R.string.user_profile_text), null)
//        .setItemIcon(R.drawable.ic_file_user)
//    );
    items.add(
      new Divider()
    );
    items.add(
      new BaseItem.SimpleItem(getString(R.string.about_me_text), null)
        .setItemIcon(R.drawable.ic_info_circle)
        .setOnItemClickListener(view ->
          aboutMeSheet.show(getActivity().getSupportFragmentManager())
        )
    );
    items.add(
      new BaseItem.SimpleItem(getString(R.string.exit_text), null)
        .setItemIcon(R.drawable.ic_door_open)
        .setOnItemClickListener(view ->
          getActivity().finishAndRemoveTask()
        )
    );

    Adapter adapter = new Adapter(items);
    binding.userRecycler.setAdapter(adapter);
    binding.userRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    String verName = "";
    try {
      verName = getActivity().getPackageManager().getPackageInfo(
        getActivity().getPackageName(), 0
      ).versionName;
    } catch (
      PackageManager.NameNotFoundException ignore) {
    }

    aboutMeSheet = new YesNoBottomSheet(getString(R.string.about_me_text),
      String.format(getString(R.string.about_me_msg), verName)
    ).setOnlyYes(true);

    signOutSheet = new YesNoBottomSheet(getString(R.string.sign_out_text),
      getString(R.string.sign_out_msg)
    );
    signOutSheet.setCancelable(false);

    refreshUI();
  }

  @Override
  protected void bindListeners() {
    aboutMeSheet.setBtnClickListener((id, view) -> aboutMeSheet.dismiss());
    signOutSheet.setBtnClickListener((id, view) -> {
      if (id == YesNoBottomSheet.ButtonId.YES_BTN) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Const.SP_LOGGED_USER_ID_KEY);
        editor.apply();
        refreshUI();
      }
      signOutSheet.dismiss();
    });
    binding.userCard.setOnClickListener(view -> {
      if (!userId.equals(Const.DEFAULT_USER_ID)) {
        signOutSheet.show(getActivity().getSupportFragmentManager());
        return;
      }
      Intent intent = new Intent(getContext(), LogInActivity.class);
      startActivityForResult(intent, Const.LOG_IN_SUCCEED);
    });
  }

  private void refreshUI() {
    userId = preferences.getString(
      Const.SP_LOGGED_USER_ID_KEY, Const.DEFAULT_USER_ID
    );
    User queryUser = userDao.load(userId);
    if (userId.equals(Const.DEFAULT_USER_ID) || queryUser == null) {
//      binding.levelImg.setImageResource(R.drawable.ic_lv1);
//      binding.levelImg.setVisibility(View.GONE);

      binding.nicknameText.setText(R.string.logout_msg);
      binding.avatarImg.setImageResource(R.drawable.default_avatar);
      return;
    }


//    int levelId = new int[]{
//      R.drawable.ic_lv1,
//      R.drawable.ic_lv2,
//      R.drawable.ic_lv3
//    }[(int) Math.max(0, Math.min(3, Math.ceil(queryUser.getExp() / 100.0)) - 1)];
//    binding.levelImg.setImageResource(levelId);
//    binding.levelImg.setVisibility(View.VISIBLE);
    binding.levelImg.setVisibility(View.GONE);
    binding.nicknameText.setText(queryUser.getNickname());
    Uri avatarUri = Uri.parse(queryUser.getAvatar());
    if (avatarUri != null && !queryUser.getAvatar().isEmpty())
      binding.avatarImg.setImageURI(avatarUri);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Const.LOG_IN_SUCCEED) refreshUI();
  }
}