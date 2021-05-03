package indi.ssuf1998.garbify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import indi.ssuf1998.garbify.databinding.ActivityMainBinding;
import indi.ssuf1998.garbify.db.DaoMaster;
import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.HistoryDao;
import indi.ssuf1998.garbify.db.def.History;

public class MainActivity extends InnerActivity {
  private ActivityMainBinding binding;
  private static final int NUM_PAGES = 2;
  private Adapter adapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    initUI();
    bindListeners();
  }

  @Override
  protected void initDB() {

  }

  @Override
  protected void initUI() {
    adapter = new Adapter(this);
    binding.wrapper.setAdapter(adapter);
    binding.wrapper.setOffscreenPageLimit(2);
  }

  @Override
  protected void bindListeners() {
    binding.navBar.setOnNavigationItemSelectedListener(item -> {
      if (item.getItemId() == R.id.navHome) binding.wrapper.setCurrentItem(0, true);
      if (item.getItemId() == R.id.navUser) binding.wrapper.setCurrentItem(1, true);
      return true;
    });

    binding.wrapper.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
        binding.navBar.getMenu().getItem(position).setChecked(true);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  private static class Adapter extends FragmentStateAdapter {
    public Adapter(AppCompatActivity activity) {
      super(activity);
    }

    @Override
    public Fragment createFragment(int position) {
      if (position == 0) return new HomeFragment();
      if (position == 1) return new UserFragment();
      return null;
    }

    @Override
    public int getItemCount() {
      return NUM_PAGES;
    }
  }
}