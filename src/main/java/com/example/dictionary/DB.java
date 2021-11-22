package com.example.dictionary;

public class DB {
    public static String[] getData(int id) {
        if (id == R.id.action_eng_viet) {
            return getEngViet();
        } else if (id == R.id.action_viet_eng) {
            return getVietAnh();
        }
        return new String[0];
    }

    public static String[] getEngViet() {
        String[] source = new String[] {
                "A",
                "BB",
                "CC",
                "DD",
        };
        return source;
    }

    public static String[] getVietAnh() {
        String[] source = new String[] {
                "Ă",
                "Â",
                "Đ",
                "Ê",
        };
        return source;
    }
}
