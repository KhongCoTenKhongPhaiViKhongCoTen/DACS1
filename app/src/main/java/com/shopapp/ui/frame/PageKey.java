package com.shopapp.ui.frame;

public class PageKey {

    private PageKey() {
    }

    public static final String HOME = "home";
    public static final String SETTINGS = "settings";

    public static final String KHACH_HANG = "kh";
    public static final String DON_HANG = "dh";

    public static class KhoHang {
        public static final String DANH_MUC = "dm";
        public static final String NHA_CUNG_CAP = "ncc";
        public static final String SAN_PHAM = "sp";
        public static final String TON_KHO = "tk";

        public static String[] getListkey() {
            return new String[] {
                    KhoHang.DANH_MUC,
                    KhoHang.NHA_CUNG_CAP,
                    KhoHang.SAN_PHAM,
                    KhoHang.TON_KHO
            };
        }

        public static String[] getItemName() {
            return new String[] {
                    "Quản lý danh mục",
                    "Quản lý nhà cung cấp",
                    "Quản lý sản phẩm",
                    "Quản lý tồn kho"
            };
        }
    }

    public static class AccountManagement {
        public static final String NGUOI_DUNG = "nd";
        public static final String VAI_TRO = "vt";
        public static final String NHOM_QUYEN = "nq";

        public static String[] getListkey() {
            return new String[] {
                    AccountManagement.NGUOI_DUNG,
                    AccountManagement.VAI_TRO,
                    AccountManagement.NHOM_QUYEN
            };
        }

        public static String[] getItemName() {
            return new String[] {
                    "Quản lý người dùng",
                    "Quản lý vai trò",
                    "Quản lý nhóm quyền"
            };
        }
    }

}
