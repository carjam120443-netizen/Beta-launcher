package com.carson.betalauncher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.Holder> {
    private final Context context;
    private final List<AppInfo> source;
    private final List<AppInfo> shown = new ArrayList<>();

    public AppAdapter(Context context, List<AppInfo> source) {
        this.context = context;
        this.source = source;
        shown.addAll(source);
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int type) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(8, 12, 8, 12);
        return new Holder(box);
    }

    @Override public void onBindViewHolder(Holder h, int position) {
        AppInfo app = shown.get(position);
        h.box.removeAllViews();
        h.box.addView(makeIconView(app, 56));
        TextView label = new TextView(context);
        label.setText(app.label);
        label.setTextColor(Color.WHITE);
        label.setTextSize(12);
        label.setGravity(Gravity.CENTER);
        label.setMaxLines(1);
        h.box.addView(label, new LinearLayout.LayoutParams(-1, 28));
        h.box.setOnClickListener(v -> {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (intent != null) context.startActivity(intent);
        });
    }

    public LinearLayout makeIconView(AppInfo app, int size) {
        LinearLayout wrap = new LinearLayout(context);
        wrap.setGravity(Gravity.CENTER);
        ImageView icon = new ImageView(context);
        icon.setImageDrawable(app.icon);
        icon.setContentDescription(app.label);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0x22FFFFFF);
        bg.setCornerRadius(20);
        icon.setBackground(bg);
        int pad = Math.max(4, size / 12);
        icon.setPadding(pad, pad, pad, pad);
        wrap.addView(icon, new LinearLayout.LayoutParams(size, size));
        wrap.setOnClickListener(v -> {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (intent != null) context.startActivity(intent);
        });
        return wrap;
    }

    public void filter(String query) {
        shown.clear();
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        for (AppInfo app : source) if (q.isEmpty() || app.label.toLowerCase(Locale.ROOT).contains(q)) shown.add(app);
        notifyDataSetChanged();
    }

    @Override public int getItemCount() { return shown.size(); }
    static class Holder extends RecyclerView.ViewHolder { final LinearLayout box; Holder(LinearLayout v) { super(v); box = v; } }
}
