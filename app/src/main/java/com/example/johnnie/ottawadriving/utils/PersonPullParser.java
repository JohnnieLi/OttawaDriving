package com.example.johnnie.ottawadriving.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnnie on 2016-09-21.
 */
public class PersonPullParser {
    private static final String LOGTAG = "XMLPULLPARSER";
    private static final String PERSON_ADDRESS = "address";
    private static final String PERSON_PHONE = "phone";
    private static final String PERSON_NAME = "name";
    private static final String PERSON_INFO = "information";
    private static final String PERSON_EMAIL = "email";
    private static final String PERSON_IMG = "imageUri";

    private PersonModel currentPerson = null;
    private String currentTag = null;
    List<PersonModel> people = new ArrayList<>();

    public List<PersonModel> parseXML(Context context){


        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            InputStream stream = context.getResources().openRawResource(R.raw.samplepeople);
            xpp.setInput(stream, null);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                if (eventType == XmlPullParser.START_TAG){
                    Log.d(LOGTAG,"when evenType is start tag "+xpp.getName());
                    handleStartTag(xpp.getName());
                } else if (eventType == XmlPullParser.END_TAG){
                    currentTag = null;
                } else if(eventType == XmlPullParser.TEXT){
                    handleText(xpp.getText());
                }
                eventType = xpp.next();
            }


        } catch (Resources.NotFoundException e){
            Log.d(LOGTAG, e.getMessage());
        } catch (XmlPullParserException e) {
            Log.d(LOGTAG, e.getMessage());
        } catch (IOException e) {
            Log.d(LOGTAG, e.getMessage());
        }

        return people;
    }

    private  void handleText(String text){
        String xmlText = text;
        if ( currentPerson != null && currentTag != null){
            if(currentTag.equals(PERSON_ADDRESS)){
                currentPerson.setAddress(xmlText);
            }else if(currentTag.equals(PERSON_NAME)){
                currentPerson.setName(xmlText);
            }else if(currentTag.equals(PERSON_PHONE)){
                currentPerson.setPhoneNumber(xmlText);
            }else if(currentTag.equals(PERSON_INFO)){
                currentPerson.setInformation(xmlText);
            }else if(currentTag.equals(PERSON_EMAIL)){
                currentPerson.setEmail(xmlText);
            }else if(currentTag.equals(PERSON_IMG)){
                currentPerson.setImageUri(xmlText);
            }
        }
    }


    private void handleStartTag(String name){
        if (name.equals("person")){
            Log.d(LOGTAG,"get person");
            currentPerson = new PersonModel();
            people.add(currentPerson);
        }else {
            currentTag = name;
        }
    }
}
