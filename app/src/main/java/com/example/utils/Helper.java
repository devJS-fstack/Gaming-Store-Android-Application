package com.example.utils;

import android.util.Log;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.Locale;
import java.util.Random;

public class Helper {
    public void parseXML(InputStream is){
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            Log.d("Parser: ", parser.toString());
        } catch (Exception e){
            Log.e("Error while parse XML", e.toString());
        }
    }

    public String generateRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        alphabet = alphabet.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 6;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    static public String generateRandomSixNumber() {
        String alphabet = "0123456789";
        alphabet = alphabet.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 6;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
