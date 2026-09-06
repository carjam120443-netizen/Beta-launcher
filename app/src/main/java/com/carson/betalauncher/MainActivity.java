package com.carson.betalauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private final Handler handler = new Handler();
    private final List<AppInfo> allApps = new ArrayList<>();
    private AppAdapter adapter;
    private RecyclerView recycler;
    private EditText search;
    private TextView clock;
    private TextView date;
    private LinearLayout dock;
    private boolean drawerOpen = false;

    private final Runnable clockTick = new Runnable() {
        @Override public void run() {
            Calendar now = Calendar.getInstance();
            clock.setText(String.format(Locale.getDefault(), "%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)));
            date.setText(DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(now.getTime()));
            handler.postDelayed(this, 1000);
        }
    };

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        clock = findViewById(R.id.clock);
        date = findViewById(R.id.date);
        search = findViewById(R.id.search);
        recycler = findViewById(R.id.apps);
        dock = findViewById(R.id.dock);

        loadApps();
        setupDock();
        setupGestures();
        setupSearch();
        handler.post(clockTick);
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent launcher = new Intent(Intent.ACTION_MAIN);
        launcher.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ApplicationInfo> infos = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo info : infos) {
            if (pm.getLaunchIntentForPackage(info.packageName) != null && !info.packageName.equals(getPackageName())) {
                allApps.add(new AppInfo(info.loadLabel(pm).toString(), info.packageName, info.loadIcon(pm)));
            }
        }
        Collections.sort(allApps, Comparator.comparing(a -> a.label.toLowerCase(Locale.ROOT)));
        adapter = new AppAdapter(this, allApps);
        recycler.setLayoutManager(new GridLayoutManager(this, 4));
        recycler.setAdapter(adapter);
    }

    private void setupDock() {
        dock.removeAllViews();
        int count = Math.min(5, allApps.size());
        for (int i = 0; i < count; i++) dock.addView(adapter.makeIconView(allApps.get(i), 64));
    }

    private void setupSearch() {
        search.setOnFocusChangeListener((v, focused) -> {
            if (focused) openDrawer();
        });
        search.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int before, int count) {
                if (!drawerOpen) openDrawer();
                adapter.filter(s.toString());
            }
            public void afterTextChanged(android.text.Editable e) {}
        });
    }

    private void setupGestures() {
        GestureDetector detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
                if (e1 == null || e2 == null) return false;
                float dy = e2.getY() - e1.getY();
                if (Math.abs(dy) > 100) {
                    if (dy < 0) openDrawer(); else closeDrawer();
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.root).setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }

    private void openDrawer() {
        drawerOpen = true;
        recycler.setVisibility(View.VISIBLE);
        search.setHint("Search apps");
    }

    private void closeDrawer() {
        drawerOpen = false;
        recycler.setVisibility(View.GONE);
        search.clearFocus();
    }

    @Override protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(clockTick);
        super.onDestroy();
    }
}
