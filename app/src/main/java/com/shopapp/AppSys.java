package com.shopapp;

import com.shopapp.entity.NguoiDung;
import com.shopapp.util.QuyenManager;

public class AppSys {
    private AppSys() {
    }

    private static final AppSys Self = new AppSys();
    private static final QuyenManager quyenManager = new QuyenManager();

    private NguoiDung nguoiDung = null;

    public static void setNguoiDung(NguoiDung nguoiDung) {
        if (nguoiDung == null) {
            Self.nguoiDung = null;
            quyenManager.update(null);
            return;
        }
        Self.nguoiDung = nguoiDung;
        quyenManager.update(nguoiDung.getPermissions());
    }

    public static NguoiDung getNguoiDung() {
        return Self.nguoiDung;
    }

    public static QuyenManager quyen() {
        return quyenManager;
    }
}