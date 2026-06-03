package com.shopapp;

import com.shopapp.entity.NguoiDung;
import com.shopapp.ui.themes.AppFont;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.util.QuyenManager;

public class AppSys {
    private static final AppSys Self = new AppSys();
    
    private static final QuyenManager quyenManager = new QuyenManager();
    private NguoiDung nguoiDung = null;

    // Kết nối trực tiếp tới gói themes thông qua Singleton
    public static final ThemeManager themes = ThemeManager.getInstance();
    public static final AppFont font = AppFont.getInstance();

    private AppSys() {
    }

    
    public static void setNguoiDung(NguoiDung nguoiDung) {
        Self.nguoiDung = nguoiDung;
        quyenManager.update(nguoiDung == null ? null : nguoiDung.getPermissions());
    }
    
    public static NguoiDung getNguoiDung() {
        return Self.nguoiDung;
    }
    
    public static QuyenManager quyen() {
        return quyenManager;
    }
    
}