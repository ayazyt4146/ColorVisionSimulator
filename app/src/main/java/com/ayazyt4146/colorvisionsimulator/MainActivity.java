package com.ayazyt4146.colorvisionsimulator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.content.ClipData;
import android.content.ClipboardManager;

public class MainActivity extends Activity {
    private final String[] modes = {
        "Normal (X)", "Protanopi", "Protanomali", "Deuteranopi",
        "Deuteranomali", "Tritanopi", "Tritanomali", "Akromatopsi",
        "Akromatomali", "Yeşil", "Kırmızı", "Mavi", "Daha yeşil", "Daha mavi"
    };
    private int selected = 0;
    private Spinner modeSpinner;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showMenu();
    }

    private int dp(float value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    private Button button(String label, int color) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(16);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackground(shape(color, 16));
        b.setMinHeight(dp(62));
        return b;
    }

    private void showMenu() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(24), dp(22), dp(24));
        page.setBackgroundColor(Color.rgb(13, 19, 31));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        TextView title = text("RENK KÖRÜ\nSİMÜLATÖRÜ", 30, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, dp(10), 0, dp(12));
        content.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView subtitle = text("Bir görünüm seç. BAŞLAT'a basınca sistem filtresini etkinleştirmeyi dener.", 16, Color.LTGRAY, false);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(dp(8), 0, dp(8), dp(24));
        content.addView(subtitle);

        TextView section = text("SİMÜLASYON TÜRÜ", 17, Color.rgb(116, 210, 170), true);
        section.setPadding(dp(4), dp(8), 0, dp(10));
        content.addView(section);

        modeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes) {
            @Override public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor(Color.WHITE);
                v.setTextSize(18);
                v.setPadding(dp(12), dp(8), dp(8), dp(8));
                return v;
            }
            @Override public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getDropDownView(position, convertView, parent);
                v.setTextSize(17);
                v.setPadding(dp(14), dp(12), dp(14), dp(12));
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);
        modeSpinner.setSelection(selected);
        modeSpinner.setBackground(shape(Color.rgb(39, 51, 70), 14));
        modeSpinner.setPadding(dp(8), dp(6), dp(8), dp(6));
        content.addView(modeSpinner, new LinearLayout.LayoutParams(-1, dp(64)));

        TextView info = text("Protanopi • Protanomali • Deuteranopi • Deuteranomali • Tritanopi • Tritanomali • Akromatopsi ve renk ayarları", 14, Color.LTGRAY, false);
        info.setPadding(dp(6), dp(14), dp(6), dp(20));
        content.addView(info);

        LinearLayout sample = new LinearLayout(this);
        sample.setOrientation(LinearLayout.VERTICAL);
        sample.setPadding(dp(16), dp(16), dp(16), dp(16));
        sample.setBackground(shape(Color.rgb(27, 37, 54), 18));
        TextView sampleTitle = text("ÖNİZLEME ALANI", 17, Color.WHITE, true);
        sample.addView(sampleTitle);
        TextView sampleDesc = text("Sistem filtresi desteklenirse telefonun diğer uygulamalarında da görünür.", 14, Color.LTGRAY, false);
        sampleDesc.setPadding(0, dp(6), 0, dp(12));
        sample.addView(sampleDesc);
        LinearLayout swatches = new LinearLayout(this);
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        for (int c : colors) {
            View chip = new View(this);
            chip.setBackground(shape(c, 9));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(38), 1);
            cp.setMargins(dp(3), 0, dp(3), 0);
            swatches.addView(chip, cp);
        }
        sample.addView(swatches);
        content.addView(sample);

        Button start = button("BAŞLAT  ▶", Color.rgb(25, 150, 103));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(66));
        bp.setMargins(0, dp(22), 0, dp(10));
        content.addView(start, bp);
        start.setOnClickListener(v -> {
            selected = modeSpinner.getSelectedItemPosition();
            startGlobalSimulation();
        });

        if (getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean("active", false)) {
            Button restore = button("SİSTEM FİLTRESİNİ KAPAT", Color.rgb(180, 65, 72));
            LinearLayout.LayoutParams restoreParams = new LinearLayout.LayoutParams(-1, dp(60));
            restoreParams.setMargins(0, dp(8), 0, dp(8));
            content.addView(restore, restoreParams);
            restore.setOnClickListener(v -> {
                restoreGlobalSettings();
                showMenu();
            });
        }

        TextView limitation = text("Sistem genelinde çalışma, Android'in renk düzeltme desteğine ve bir defalık sistem iznine bağlıdır. Her cihazda destek garantisi yoktur.", 12, Color.rgb(190, 199, 211), false);
        limitation.setPadding(dp(6), dp(8), dp(6), dp(12));
        content.addView(limitation);

        scroll.addView(content);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(page);
    }

    private static final String DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final String DALTONIZER_MODE = "accessibility_display_daltonizer";
    private static final String PREFS = "global_filter_backup";

    private int systemModeForSelection(int selection) {
        switch (selection) {
            case 1: case 2: return 11;
            case 3: case 4: return 12;
            case 5: case 6: return 13;
            case 7: return 0;
            default: return -999;
        }
    }

    private void startGlobalSimulation() {
        if (selected == 0) {
            Toast.makeText(this, "Önce bir simülasyon türü seç.", Toast.LENGTH_LONG).show();
            return;
        }

        if (checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS")
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            showAdbPermissionDialog();
            return;
        }

        int systemMode = systemModeForSelection(selected);
        if (systemMode == -999) {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Bu mod sistem genelinde desteklenmiyor")
                .setMessage("Seçtiğin mod Android'in standart sistem filtresinde ayrı olarak bulunmuyor. Uygulama içi önizlemeyi açabilirsin.")
                .setPositiveButton("Önizlemeyi aç", (d, w) -> showSimulation())
                .setNegativeButton("Geri", null)
                .show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (!prefs.getBoolean("active", false)) {
            int oldEnabled = Settings.Secure.getInt(getContentResolver(), DALTONIZER_ENABLED, 0);
            int oldMode = Settings.Secure.getInt(getContentResolver(), DALTONIZER_MODE, -1);
            prefs.edit()
                .putInt("old_enabled", oldEnabled)
                .putInt("old_mode", oldMode)
                .putBoolean("active", true)
                .apply();
        }

        boolean modeWritten = Settings.Secure.putInt(getContentResolver(), DALTONIZER_MODE, systemMode);
        boolean enabledWritten = Settings.Secure.putInt(getContentResolver(), DALTONIZER_ENABLED, 1);
        if (!modeWritten || !enabledWritten) {
            restoreGlobalSettings();
            new android.app.AlertDialog.Builder(this)
                .setTitle("Cihaz sistem filtresini kabul etmedi")
                .setMessage("İzin verilmiş olsa bile telefonun yazılımı bu Android ayarını desteklemiyor olabilir.")
                .setPositiveButton("Önizlemeyi aç", (d, w) -> showSimulation())
                .setNegativeButton("Geri", null)
                .show();
            return;
        }

        Toast.makeText(this, "Sistem filtresi etkinleştirildi (cihaz desteğine bağlı).", Toast.LENGTH_LONG).show();
        showSimulation();
    }

    private void showAdbPermissionDialog() {
        String command = "pm grant com.ayazyt4146.colorvisionsimulator android.permission.WRITE_SECURE_SETTINGS";
        new android.app.AlertDialog.Builder(this)
            .setTitle("Tek telefonla bir defalık izin")
            .setMessage("İkinci telefon veya bilgisayar gerekmiyor. Aynı telefonda çalışan yerel bir ADB kabuğu (ör. LADB) kullanabilirsin. Geliştirici seçeneklerinden Kablosuz hata ayıklamayı açıp yerel ADB uygulamasında eşleştirme yap; ardından aşağıdaki komutu o uygulamaya yapıştır. Bu izin Android tarafından normal uygulama penceresiyle verilemez.\n\nNot: Yerel ADB uygulaması ayrıca kurulmalıdır; bu uygulama tek başına izni veremez.")
            .setPositiveButton("Komutu kopyala", (d, w) -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Yerel ADB komutu", command));
                Toast.makeText(this, "Yerel ADB komutu kopyalandı.", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Sonra", null)
            .show();
    }

    private void restoreGlobalSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (!prefs.getBoolean("active", false)) return;
        int oldEnabled = prefs.getInt("old_enabled", 0);
        int oldMode = prefs.getInt("old_mode", -1);
        if (checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS")
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Sistem izni yok; filtre ayarı geri yüklenemedi.", Toast.LENGTH_LONG).show();
            return;
        }
        Settings.Secure.putInt(getContentResolver(), DALTONIZER_MODE, oldMode);
        Settings.Secure.putInt(getContentResolver(), DALTONIZER_ENABLED, oldEnabled);
        prefs.edit().putBoolean("active", false).apply();
        Toast.makeText(this, "Önceki ekran ayarları geri yüklendi.", Toast.LENGTH_SHORT).show();
    }

    private void showSimulation() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(18), dp(20), dp(18), dp(18));
        page.setBackgroundColor(Color.rgb(10, 15, 25));

        TextView heading = text("SİMÜLASYON AKTİF", 24, Color.WHITE, true);
        heading.setGravity(Gravity.CENTER);
        page.addView(heading);

        TextView selectedMode = text("Seçilen görünüm: " + modes[selected], 16, Color.rgb(116, 210, 170), true);
        selectedMode.setGravity(Gravity.CENTER);
        selectedMode.setPadding(0, dp(8), 0, dp(18));
        page.addView(selectedMode);

        SimulationView simulation = new SimulationView();
        page.addView(simulation, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView explanation = text("Sistem filtresi destekleniyorsa diğer uygulamalara da uygulanır. Bazı modlar yalnızca bu uygulamanın önizlemesinde çalışır. İPTAL önceki sistem ayarlarını geri yükler.", 13, Color.LTGRAY, false);
        explanation.setGravity(Gravity.CENTER);
        explanation.setPadding(dp(8), dp(12), dp(8), dp(12));
        page.addView(explanation);

        Button cancel = button("İPTAL  ·  MENÜYE DÖN", Color.rgb(180, 65, 72));
        page.addView(cancel, new LinearLayout.LayoutParams(-1, dp(64)));
        cancel.setOnClickListener(v -> {
            restoreGlobalSettings();
            selected = 0;
            showMenu();
        });
        setContentView(page);
    }

    @Override public void onBackPressed() {
        restoreGlobalSettings();
        showMenu();
    }

    private class SimulationView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final String[] labels = {"KIRMIZI", "YEŞİL", "MAVİ", "SARI", "CYAN", "MAGENTA", "TURUNCU", "BEYAZ"};
        private final int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.rgb(255,128,0), Color.WHITE};
        SimulationView() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.rgb(19, 28, 43));
            float gap = dp(12);
            float cellW = (getWidth() - gap * 3) / 2f;
            float cellH = Math.min(dp(112), (getHeight() - gap * 5) / 4f);
            paint.setTextSize(dp(14));
            paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
            for (int i = 0; i < colors.length; i++) {
                int col = i % 2, row = i / 2;
                float left = gap + col * (cellW + gap);
                float top = gap + row * (cellH + gap);
                paint.setColor(colors[i]);
                paint.setColorFilter(filterFor(selected));
                canvas.drawRoundRect(left, top, left + cellW, top + cellH, dp(14), dp(14), paint);
                paint.setColorFilter(null);
                paint.setColor(Color.BLACK);
                canvas.drawText(labels[i], left + dp(12), top + cellH / 2f + dp(5), paint);
            }
        }

        private ColorFilter filterFor(int mode) {
            float[] gray = {.299f,.587f,.114f,0,0, .299f,.587f,.114f,0,0, .299f,.587f,.114f,0,0, 0,0,0,1,0};
            float[] matrix;
            switch (mode) {
                case 7: matrix = gray; break;
                case 8: matrix = new float[]{.65f,.25f,.10f,0,0, .25f,.65f,.10f,0,0, .10f,.25f,.65f,0,0, 0,0,0,1,0}; break;
                case 1: case 2: matrix = new float[]{.567f,.433f,0,0,0, .558f,.442f,0,0,0, 0,.242f,.758f,0,0, 0,0,0,1,0}; break;
                case 3: case 4: matrix = new float[]{.625f,.375f,0,0,0, .7f,.3f,0,0,0, 0,.3f,.7f,0,0, 0,0,0,1,0}; break;
                case 5: case 6: matrix = new float[]{.95f,.05f,0,0,0, 0,.433f,.567f,0,0, 0,.475f,.525f,0,0, 0,0,0,1,0}; break;
                case 9: matrix = new float[]{.8f,.1f,.1f,0,0, .1f,1.1f,.1f,0,0, .1f,.1f,.8f,0,0, 0,0,0,1,0}; break;
                case 10: matrix = new float[]{1.15f,-.1f,-.05f,0,0, -.05f,.9f,.05f,0,0, -.05f,-.1f,1.15f,0,0, 0,0,0,1,0}; break;
                case 11: matrix = new float[]{.85f,.05f,.1f,0,0, .05f,.85f,.1f,0,0, .1f,.05f,1.15f,0,0, 0,0,0,1,0}; break;
                case 12: matrix = new float[]{.9f,.05f,.05f,0,0, .05f,1.2f,.05f,0,0, .05f,.05f,.9f,0,0, 0,0,0,1,0}; break;
                case 13: matrix = new float[]{.9f,.05f,.05f,0,0, .05f,.9f,.05f,0,0, .05f,.05f,1.2f,0,0, 0,0,0,1,0}; break;
                default: return null;
            }
            return new ColorMatrixColorFilter(matrix);
        }
    }
}
