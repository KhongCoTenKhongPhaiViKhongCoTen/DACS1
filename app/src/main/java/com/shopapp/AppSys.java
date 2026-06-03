package com.shopapp;

import com.shopapp.entity.NguoiDung;
import com.shopapp.ui.themes.AppFont;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.util.QuyenManager;

public class AppSys {
    private static final AppSys instance = new AppSys();
    private static final QuyenManager quyenManager = new QuyenManager();

    private NguoiDung nguoiDung = null;

    private AppSys() {
    }

    
    public static void setNguoiDung(NguoiDung nguoiDung) {
        instance.nguoiDung = nguoiDung;
        quyenManager.update(nguoiDung == null ? null : nguoiDung.getPermissions());
    }
    
    public static NguoiDung getNguoiDung() {
        return instance.nguoiDung;
    }
    
    public static QuyenManager quyen() {
        return quyenManager;
    }
    
    // Kết nối trực tiếp tới gói themes thông qua Singleton
    public static final ThemeManager themes = ThemeManager.getInstance();
    public static final AppFont font = AppFont.getInstance();
}