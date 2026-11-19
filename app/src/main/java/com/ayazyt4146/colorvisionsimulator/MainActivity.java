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
import android.content.Intent;

public class MainActivity extends Activity {
    private final String[] modes = {
        "Normal", "Protanopi (kırmızı algısı yok)", "Protanomali (kırmızı algısı zayıf)",
        "Deuteranopi (yeşil algısı yok)", "Deuteranomali (yeşil algısı zayıf)",
        "Tritanopi (mavi algısı yok)", "Tritanomali (mavi algısı zayıf)",
        "Akromatopsi (tam renk körlüğü)", "Akromatomali (renk algısı çok az)"
    };
    private final String[] modeDescriptions = {
        "Normal renk görünümü. Sistem filtresi kapalıdır.",
        "Protanopi: Kırmızıya duyarlı koni hücreleri çalışmaz. Kırmızı-yeşil tonlarını ayırt etmek zorlaşabilir.",
        "Protanomali: Kırmızıya duyarlılık azalmıştır. Kırmızı ve yeşil tonları farklı görünebilir.",
        "Deuteranopi: Yeşile duyarlı koni hücreleri çalışmaz. Kırmızı-yeşil ayrımı zorlaşabilir.",
        "Deuteranomali: Yeşile duyarlılık azalmıştır. En yaygın renk görme farklılıklarından biridir.",
        "Tritanopi: Maviye duyarlı koni hücreleri çalışmaz. Mavi-sarı ayrımı etkilenebilir.",
        "Tritanomali: Maviye duyarlılık azalmıştır. Mavi ve sarı tonları farklı algılanabilir.",
        "Akromatopsi: Çok ileri durumda renkler algılanmaz veya çok sınırlı algılanır; siyah-beyaz görünüm yaklaşık simülasyondur.",
        "Akromatomali: Renk algısı belirgin biçimde azalmıştır; bu uygulamadaki görünüm yaklaşık önizlemedir."
    };
    private int selected = 0;
    private Spinner modeSpinner;
    private boolean english = false;
    private final String[] modesEn = {"Normal","Protanopia (red deficiency)","Protanomaly (reduced red sensitivity)","Deuteranopia (green deficiency)","Deuteranomaly (reduced green sensitivity)","Tritanopia (blue deficiency)","Tritanomaly (reduced blue sensitivity)","Achromatopsia (complete color blindness)","Achromatomaly (limited color vision)"};
    private final String[] descriptionsEn = {"Normal color appearance. System filter is off.","Protanopia: red-sensitive cone cells are absent; red-green distinction may be difficult.","Protanomaly: reduced sensitivity to red.","Deuteranopia: green-sensitive cone cells are absent; red-green distinction may be difficult.","Deuteranomaly: reduced sensitivity to green.","Tritanopia: blue-sensitive cone cells are absent; blue-yellow distinction may be affected.","Tritanomaly: reduced sensitivity to blue.","Achromatopsia: color perception is absent or extremely limited; grayscale is an approximation.","Achromatomaly: color perception is significantly reduced; this is an approximate preview."};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        english = getSharedPreferences("app_settings", MODE_PRIVATE).getBoolean("english", false);
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


    private void showHelpDialog() {
        String title = english ? "EASY GUIDE" : "KOLAY KULLANIM REHBERİ";
        String message = english
            ? "1. CHOOSE A MODE\\nTap the simulation list and select the color-vision mode you want to preview. The text below explains it.\\n\\n2. PRESS START\\nTap START. If Android asks for a special permission, follow the on-screen instructions. This permission is a one-time setup on most devices.\\n\\n3. USE THE FLOATING MENU (OPTIONAL)\\nTap ENABLE FLOATING MENU and allow “Display over other apps”. You can then change modes from the small floating panel.\\n\\n4. RETURN TO NORMAL\\nTap CANCEL / RESTORE ORIGINAL to turn off the filter and restore the previous system setting.\\n\\nIMPORTANT\\nSystem-wide filtering depends on Android and device support. Some modes are approximate or preview-only. This app is a simulator, not a medical test."
            : "1. MOD SEÇ\\nSimülasyon listesinden denemek istediğin renk görme moduna dokun. Altındaki yazı ne anlama geldiğini açıklar.\\n\\n2. BAŞLAT'A BAS\\nBAŞLAT düğmesine dokun. Android özel bir izin isterse ekrandaki adımları izle. Bu izin çoğu cihazda bir kez verilir.\\n\\n3. YÜZEN MENÜ (İSTEĞE BAĞLI)\\nYÜZEN MENÜYÜ ETKİNLEŞTİR'e bas ve “Diğer uygulamaların üzerinde göster” iznini ver. Sonra küçük menüden mod değiştirebilirsin.\\n\\n4. NORMAL GÖRÜNÜME DÖN\\nFiltreyi kapatıp önceki sistem ayarını geri yüklemek için İPTAL / ORİJİNAL RENKLERE DÖN düğmesine bas.\\n\\nÖNEMLİ\\nSistem genelinde filtreleme Android'e ve cihaz desteğine bağlıdır. Bazı modlar yaklaşık simülasyondur veya yalnızca önizlemede çalışır. Bu uygulama tıbbi test değildir.";
        new android.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(english ? "GOT IT" : "ANLADIM", null)
            .show();
    }

    private boolean isTabletBuild() {
        return "tablet".equals(getString(R.string.app_layout));
    }

    private void showMenu() {
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) { showTabletMenu(); return; }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(24), dp(22), dp(24));
        page.setBackgroundColor(Color.rgb(13, 19, 31));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        Button language = button(english ? "LANGUAGE: ENGLISH · TÜRKÇE" : "DİL: TÜRKÇE · ENGLISH", Color.rgb(75,83,112));
        content.addView(language, new LinearLayout.LayoutParams(-1, dp(48)));
        language.setOnClickListener(v -> { english = !english; getSharedPreferences("app_settings", MODE_PRIVATE).edit().putBoolean("english", english).apply(); showMenu(); });
        Button help = button(english ? "HELP / GUIDE  ?" : "YARDIM / NASIL KULLANILIR  ?", Color.rgb(48, 116, 105));
        LinearLayout.LayoutParams helpParams = new LinearLayout.LayoutParams(-1, dp(48));
        helpParams.setMargins(0, dp(8), 0, 0);
        content.addView(help, helpParams);
        help.setOnClickListener(v -> showHelpDialog());
        TextView title = text(english ? "COLOR VISION\nSIMULATOR" : "RENK GÖRME\nSİMÜLATÖRÜ", 30, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, dp(10), 0, dp(12));
        content.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView subtitle = text(english ? "Choose a mode. START will try to enable the system-wide filter." : "Bir görünüm seç. BAŞLAT'a basınca sistem filtresini etkinleştirmeyi dener.", 16, Color.LTGRAY, false);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(dp(8), 0, dp(8), dp(24));
        content.addView(subtitle);

        TextView section = text(english ? "SIMULATION MODE" : "SİMÜLASYON TÜRÜ", 17, Color.rgb(116, 210, 170), true);
        section.setPadding(dp(4), dp(8), 0, dp(10));
        content.addView(section);

        modeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, english ? modesEn : modes) {
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

        TextView info = text((english ? descriptionsEn : modeDescriptions)[0], 14, Color.LTGRAY, false);
        info.setPadding(dp(8), dp(14), dp(8), dp(20));
        content.addView(info);
        modeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                info.setText((english ? descriptionsEn : modeDescriptions)[position]);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        TextView overlayTitle = text(english ? "FLOATING MODE MENU" : "YÜZEN MOD MENÜSÜ", 17, Color.rgb(116, 210, 170), true);
        overlayTitle.setPadding(dp(4), dp(12), 0, dp(8));
        content.addView(overlayTitle);
        TextView overlayInfo = text(english ? "Open a compact control panel over other apps. Choose a simulation mode and apply it without returning here." : "Diğer uygulamaların üzerinde küçük bir kontrol paneli aç. Simülasyon türünü seçip buraya dönmeden uygula.", 14, Color.LTGRAY, false);
        overlayInfo.setPadding(dp(4), 0, dp(4), dp(12));
        content.addView(overlayInfo);
        Button overlayButton = button(english ? "ENABLE FLOATING MENU" : "YÜZEN MENÜYÜ ETKİNLEŞTİR", Color.rgb(74, 105, 190));
        content.addView(overlayButton, new LinearLayout.LayoutParams(-1, dp(62)));
        overlayButton.setOnClickListener(v -> {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                Intent permissionIntent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    android.net.Uri.parse("package:" + getPackageName()));
                startActivity(permissionIntent);
                Toast.makeText(this, english ? "Allow display over other apps, then return and tap again." : "Diğer uygulamaların üzerinde gösterme iznini ver, geri dönüp tekrar dokun.", Toast.LENGTH_LONG).show();
                return;
            }
            startService(new Intent(this, FloatingMenuService.class));
            Toast.makeText(this, english ? "Floating menu enabled." : "Yüzen menü etkinleştirildi.", Toast.LENGTH_SHORT).show();
        });
        Button stopOverlay = button(english ? "CLOSE FLOATING MENU" : "YÜZEN MENÜYÜ KAPAT", Color.rgb(92, 100, 118));
        LinearLayout.LayoutParams stopOverlayParams = new LinearLayout.LayoutParams(-1, dp(54));
        stopOverlayParams.setMargins(0, dp(8), 0, 0);
        content.addView(stopOverlay, stopOverlayParams);
        stopOverlay.setOnClickListener(v -> stopService(new Intent(this, FloatingMenuService.class)));



        LinearLayout sample = new LinearLayout(this);
        sample.setOrientation(LinearLayout.VERTICAL);
        sample.setPadding(dp(16), dp(16), dp(16), dp(16));
        sample.setBackground(shape(Color.rgb(27, 37, 54), 18));
        TextView sampleTitle = text(english ? "COLOR PREVIEW" : "ÖNİZLEME ALANI", 17, Color.WHITE, true);
        sample.addView(sampleTitle);
        TextView sampleDesc = text(english ? "If supported, the system filter also affects other apps." : "Sistem filtresi desteklenirse telefonun diğer uygulamalarında da görünür.", 14, Color.LTGRAY, false);
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

        Button start = button(english ? "START  ▶" : "BAŞLAT  ▶", Color.rgb(25, 150, 103));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(66));
        bp.setMargins(0, dp(22), 0, dp(10));
        content.addView(start, bp);
        start.setOnClickListener(v -> {
            selected = modeSpinner.getSelectedItemPosition();
            startGlobalSimulation();
        });

        if (getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean("active", false)) {
            Button restore = button(english ? "TURN OFF SYSTEM FILTER" : "SİSTEM FİLTRESİNİ KAPAT", Color.rgb(180, 65, 72));
            LinearLayout.LayoutParams restoreParams = new LinearLayout.LayoutParams(-1, dp(60));
            restoreParams.setMargins(0, dp(8), 0, dp(8));
            content.addView(restore, restoreParams);
            restore.setOnClickListener(v -> {
                restoreGlobalSettings();
                showMenu();
            });
        }

        TextView limitation = text(english ? "System-wide operation depends on Android color-correction support and a one-time permission. Device support is not guaranteed." : "Sistem genelinde çalışma, Android'in renk düzeltme desteğine ve bir defalık sistem iznine bağlıdır. Her cihazda destek garantisi yoktur.", 12, Color.rgb(190, 199, 211), false);
        limitation.setPadding(dp(6), dp(8), dp(6), dp(12));
        content.addView(limitation);

        scroll.addView(content);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(page);
    }


    // Wide-screen tablet layout: two-column control dashboard.
    private void showTabletMenu() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(26), dp(20), dp(26), dp(20));
        root.setBackgroundColor(Color.rgb(13, 19, 31));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);
        TextView brand = text("COLOR VISION SIMULATOR", 26, Color.WHITE, true);
        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
        Button language = button(english ? "ENGLISH / TÜRKÇE" : "TÜRKÇE / ENGLISH", Color.rgb(75,83,112));
        header.addView(language, new LinearLayout.LayoutParams(dp(210), dp(52)));
        language.setOnClickListener(v -> {
            english = !english;
            getSharedPreferences("app_settings", MODE_PRIVATE).edit().putBoolean("english", english).apply();
            showTabletMenu();
        });
        root.addView(header);

        TextView subtitle = text(english ? "A wider workspace for color-vision simulations" : "Renk görme simülasyonları için geniş ekran kontrol merkezi", 15, Color.LTGRAY, false);
        subtitle.setPadding(0, dp(5), 0, dp(18));
        root.addView(subtitle);

        LinearLayout columns = new LinearLayout(this);
        columns.setOrientation(LinearLayout.HORIZONTAL);
        columns.setGravity(Gravity.TOP);

        LinearLayout left = new LinearLayout(this);
        left.setOrientation(LinearLayout.VERTICAL);
        left.setPadding(dp(18), dp(18), dp(18), dp(18));
        left.setBackground(shape(Color.rgb(27, 37, 54), 20));
        TextView choose = text(english ? "SIMULATION MODE" : "SİMÜLASYON TÜRÜ", 19, Color.rgb(116,210,170), true);
        left.addView(choose);
        TextView hint = text(english ? "Select a mode from the list." : "Listeden bir görünüm seç.", 14, Color.LTGRAY, false);
        hint.setPadding(0, dp(6), 0, dp(14));
        left.addView(hint);

        modeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, english ? modesEn : modes) {
            @Override public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor(Color.WHITE); v.setTextSize(17); v.setPadding(dp(12), dp(8), dp(8), dp(8));
                return v;
            }
            @Override public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getDropDownView(position, convertView, parent);
                v.setTextSize(16); v.setPadding(dp(14), dp(14), dp(14), dp(14));
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);
        modeSpinner.setSelection(selected);
        modeSpinner.setBackground(shape(Color.rgb(39,51,70), 14));
        left.addView(modeSpinner, new LinearLayout.LayoutParams(-1, dp(64)));

        TextView description = text((english ? descriptionsEn : modeDescriptions)[selected], 15, Color.WHITE, false);
        description.setPadding(dp(4), dp(18), dp(4), dp(10));
        left.addView(description);
        modeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                description.setText((english ? descriptionsEn : modeDescriptions)[position]);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        LinearLayout right = new LinearLayout(this);
        right.setOrientation(LinearLayout.VERTICAL);
        right.setPadding(dp(20), dp(18), dp(20), dp(18));
        right.setBackground(shape(Color.rgb(27, 37, 54), 20));
        TextView previewTitle = text(english ? "COLOR PREVIEW" : "RENK ÖNİZLEMESİ", 19, Color.WHITE, true);
        right.addView(previewTitle);
        TextView previewText = text(english ? "Compare common colors in the selected simulation." : "Seçilen simülasyonda temel renkleri karşılaştır.", 14, Color.LTGRAY, false);
        previewText.setPadding(0, dp(6), 0, dp(14));
        right.addView(previewText);
        LinearLayout swatches = new LinearLayout(this);
        swatches.setOrientation(LinearLayout.HORIZONTAL);
        int[] previewColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        for (int c : previewColors) {
            View chip = new View(this);
            chip.setBackground(shape(c, 10));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(56), 1);
            cp.setMargins(dp(3), 0, dp(3), 0);
            swatches.addView(chip, cp);
        }
        right.addView(swatches);

        TextView floatingTitle = text(english ? "FLOATING MENU" : "YÜZEN MOD MENÜSÜ", 18, Color.rgb(116,210,170), true);
        floatingTitle.setPadding(0, dp(24), 0, dp(7));
        right.addView(floatingTitle);
        TextView floatingInfo = text(english ? "Keep quick mode controls above other apps." : "Diğer uygulamaların üzerinde hızlı mod kontrollerini kullan.", 14, Color.LTGRAY, false);
        floatingInfo.setPadding(0, 0, 0, dp(12));
        right.addView(floatingInfo);
        Button overlay = button(english ? "ENABLE FLOATING MENU" : "YÜZEN MENÜYÜ ETKİNLEŞTİR", Color.rgb(74,105,190));
        right.addView(overlay, new LinearLayout.LayoutParams(-1, dp(58)));
        overlay.setOnClickListener(v -> {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, android.net.Uri.parse("package:" + getPackageName()));
                startActivity(i);
                Toast.makeText(this, english ? "Allow display over other apps, then return." : "Diğer uygulamaların üzerinde gösterme iznini verip geri dön.", Toast.LENGTH_LONG).show();
                return;
            }
            startService(new Intent(this, FloatingMenuService.class));
            Toast.makeText(this, english ? "Floating menu enabled." : "Yüzen menü etkinleştirildi.", Toast.LENGTH_SHORT).show();
        });
        Button closeOverlay = button(english ? "CLOSE FLOATING MENU" : "YÜZEN MENÜYÜ KAPAT", Color.rgb(92,100,118));
        LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(-1, dp(52));
        closeParams.setMargins(0, dp(8), 0, 0);
        right.addView(closeOverlay, closeParams);
        closeOverlay.setOnClickListener(v -> stopService(new Intent(this, FloatingMenuService.class)));

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, -1, 1f);
        leftParams.setMargins(0, 0, dp(12), 0);
        columns.addView(left, leftParams);
        columns.addView(right, new LinearLayout.LayoutParams(0, -1, 1.25f));
        root.addView(columns, new LinearLayout.LayoutParams(-1, 0, 1f));

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, dp(14), 0, 0);
        Button start = button(english ? "START SIMULATION  ▶" : "SİMÜLASYONU BAŞLAT  ▶", Color.rgb(25,150,103));
        actions.addView(start, new LinearLayout.LayoutParams(0, dp(64), 1f));
        start.setOnClickListener(v -> { selected = modeSpinner.getSelectedItemPosition(); startGlobalSimulation(); });
        if (getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean("active", false)) {
            Button restore = button(english ? "RESTORE ORIGINAL" : "ORİJİNAL RENKLERE DÖN", Color.rgb(180,65,72));
            LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, dp(64), 1f);
            rp.setMargins(dp(12), 0, 0, 0);
            actions.addView(restore, rp);
            restore.setOnClickListener(v -> { restoreGlobalSettings(); showTabletMenu(); });
        }
        root.addView(actions);
        setContentView(root);
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

        TextView heading = text(english ? "SIMULATION ACTIVE" : "SİMÜLASYON AKTİF", 24, Color.WHITE, true);
        heading.setGravity(Gravity.CENTER);
        page.addView(heading);

        TextView selectedMode = text((english ? "Selected mode: " : "Seçilen görünüm: ") + (english ? modesEn[selected] : modes[selected]), 16, Color.rgb(116, 210, 170), true);
        selectedMode.setGravity(Gravity.CENTER);
        selectedMode.setPadding(0, dp(8), 0, dp(8));
        page.addView(selectedMode);
        TextView selectedDescription = text((english ? descriptionsEn : modeDescriptions)[selected], 14, Color.LTGRAY, false);
        selectedDescription.setGravity(Gravity.CENTER);
        selectedDescription.setPadding(dp(8), 0, dp(8), dp(14));
        page.addView(selectedDescription);

        SimulationView simulation = new SimulationView();
        page.addView(simulation, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView explanation = text("Sistem filtresi destekleniyorsa diğer uygulamalara da uygulanır. Bazı modlar yalnızca bu uygulamanın önizlemesinde çalışır. İPTAL önceki sistem ayarlarını geri yükler.", 13, Color.LTGRAY, false);
        explanation.setGravity(Gravity.CENTER);
        explanation.setPadding(dp(8), dp(12), dp(8), dp(12));
        page.addView(explanation);

        Button cancel = button(english ? "CANCEL · BACK TO MENU" : "İPTAL  ·  MENÜYE DÖN", Color.rgb(180, 65, 72));
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

                default: return null;
            }
            return new ColorMatrixColorFilter(matrix);
        }
    }
}
