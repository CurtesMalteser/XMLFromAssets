package com.curtesmalteser.xmlfromassets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

        parseXML();

    }

    private void parseXML() {

        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("data.xml");

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            executeParsing(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeParsing(XmlPullParser parser) throws IOException, XmlPullParserException {

        ArrayList<Poet> poets = new ArrayList<>();
        int eventType = parser.getEventType();
        Poet currentPoet = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String poetName = null; // Set the string to null is redundant, so this is just for visual help

            switch (eventType) {
                case XmlPullParser.START_TAG :
                    poetName = parser.getName();
                    if ("poet".equals(poetName)) {
                        currentPoet = new Poet();
                        poets.add(currentPoet);
                    } else if (currentPoet != null) {
                        if("name".equals(poetName)) {
                            currentPoet.name = parser.nextText();
                        } else if ("life".equals(poetName)){
                            currentPoet.life = parser.nextText();
                        } else if ("poem".equals(poetName)){
                            currentPoet.poem = parser.nextText();
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
        printPoets(poets);
    }

    private void printPoets(ArrayList<Poet> poets) {
        StringBuilder sb = new StringBuilder();

        for (Poet poet : poets) {
            sb.append(poet.name)
                    .append("\n")
                    .append(poet.life)
                    .append("\n")
                    .append(poet.poem)
                    .append("\n\n");
        }
        tv.setText(sb.toString());
    }
}
