package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;

public class DetailFragmentViJp extends Fragment {

    private String value = "";
    private TextView tvWord;
    private ImageButton btnBookmark, btnVolume;
    private WebView tvWordTranslate;
    private ViJpHelper vietEngHelper;
    //private  int mDicType;
    private TextToSpeech mTTS;          //am thanh

    public DetailFragmentViJp() {
        // Required empty public constructor
    }

    public static DetailFragmentViJp getNewInstance(String value, ViJpHelper vietEngHelper) {
        DetailFragmentViJp fragment = new DetailFragmentViJp();
        fragment.value = value;
        fragment.vietEngHelper = vietEngHelper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvWord = view.findViewById(R.id.tvWord);
        tvWordTranslate = view.findViewById(R.id.tvWordTranslate);
        btnBookmark = view.findViewById(R.id.btnBookmark);
        btnVolume = view.findViewById(R.id.btnVolume);

        final Word word = vietEngHelper.getWord(value);
        tvWord.setText(word.key);
        tvWordTranslate.loadDataWithBaseURL(null, word.value, "text/html", "utf-8", null);

        Word bookmarkWord = vietEngHelper.getWordFromBookMark(value);
        int isMark = bookmarkWord == null ? 0 : 1;

        btnBookmark.setTag(isMark);

        int icon = bookmarkWord == null ? R.drawable.ic_bookmark_border : R.drawable.ic_bookmark;
        btnBookmark.setImageResource(icon);

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) btnBookmark.getTag();
                if (i == 0) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark);
                    btnBookmark.setTag(1);
                    vietEngHelper.addBookMark(word);
                } else if (i == 1) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
                    btnBookmark.setTag(0);
                    vietEngHelper.removeBookMark(word);
                }
            }
        });

        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        btnVolume.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        //Speak btn click
        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wordSpeak = tvWord.getText().toString();
                mTTS.speak(wordSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
