package com.example.dictionary;

public class Word {
    public String key = "";             //tên cột từ mới trong db
    public String value = "";           //tên nghĩa tiếng việt tương ứng

    public Word() {

    }

    public Word(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
