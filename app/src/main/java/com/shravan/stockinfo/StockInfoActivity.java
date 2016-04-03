package com.shravan.stockinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;



/**
 * Created by Archu on 05-03-2016.
 */
public class StockInfoActivity extends Activity {
    private static final String TAG = "StockQoute";

    static final String KEY_ITEM = "quote";
    static final String KEY_YEAR_LOW = "YearLow";
    static final String KEY_YEAR_HIGH = "YearHigh";
    static final String KEY_DAYS_LOW = "DaysLow";
    static final String KEY_DAYS_HIGH = "DaysHigh";
    static final String KEY_CHANGE = "Change";
    static final String KEY_LAST_TRADE = "LastTradePriceOnly";
    static final String KEY_DAYS_CHANGE = "DaysRange";

    TextView companyNameView;
    TextView yearLowView;
    TextView yearHighView;
    TextView daysLowView;
    TextView daysHighView;
    TextView LastPriceView;
    TextView changeView;
    TextView dailyPriceRangeView;

    String name = "";
    String yearLow = "";
    String yearHigh = "";
    String daysLow = "";
    String daysHigh = "";
    String lastTradePrice = "";
    String change = "";
    String daysRange = "";

    String yahooURLFirst = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
    String yahooURLSecond = "%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    String[][] xmlPullParserArray = {{"AverageDailyVolume", "0"}, {"Change", "0"}, {"DaysLow", "0"},
            {"DaysHigh", "0"}, {"YearLow", "0"}, {"YearHigh", "0"},
            {"MarketCapitalization", "0"}, {"LastTradePriceOnly", "0"}, {"DaysRange", "0"},
            {"Name", "0"}, {"Symbol", "0"}, {"Volume", "0"}, {"StockExchange", "0"}};

    int parserArrayIncrement = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_stock_qoute);
        Intent intent = getIntent();
        String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);

        companyNameView = (TextView) findViewById(R.id.companyNameView);
        yearLowView = (TextView) findViewById(R.id.yearLow);
        yearHighView = (TextView) findViewById(R.id.yearHigh);
        daysLowView = (TextView) findViewById(R.id.daysLow);
        daysHighView = (TextView) findViewById(R.id.daysHigh);
        LastPriceView = (TextView) findViewById(R.id.lastPrice);
        changeView = (TextView) findViewById(R.id.change);
        dailyPriceRangeView = (TextView) findViewById(R.id.dailyPriceRange);
        //Log.d(TAG, "Before URL creation" + stockSymbol);
        final String yqlURL = yahooURLFirst + stockSymbol + yahooURLSecond;
        Log.d(TAG, "Before URL creation" + yqlURL);
        //Log.d("Stockinfo", "start");
        new MyAsyncTask().execute(yqlURL);
        //Log.d("Stockinfo", "asyc task end");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "StockInfo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.shravan.stockinfo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "StockInfo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.shravan.stockinfo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            try {
                //Log.d("Stockinfo", "1");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                //Log.d("Stockinfo", "2");
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(getUrlData(args[0])));
                //Log.d(TAG, "do in background" + 1);
                beginDocument(parser, "query");
                int eventType = parser.getEventType();
                //Log.d("Stockinfo", "3");
                do {
                    nextElement(parser);
                    //Log.d("Stockinfo", "3a");
                    parser.next();
                    //Log.d("Stockinfo", "3b");
                    eventType = parser.getEventType();
                    Log.d("Stockinfo", "3c");
                    if (eventType == XmlPullParser.TEXT) {
                        Log.d("Stockinfo", "3d");
                        String valueFromXML = parser.getText();
                        Log.d("Stockinfo", valueFromXML);
                        xmlPullParserArray[parserArrayIncrement++][1] = valueFromXML;
                        //Log.d("Stockinfo", valueFromXML);
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }


        public InputStream getUrlData(String url) throws URISyntaxException,ClientProtocolException, IOException {
            //Log.d("Stockinfo", "4");
            DefaultHttpClient client =new DefaultHttpClient();
            HttpGet method = new HttpGet(new URI(url));
            HttpResponse res = client.execute(method);
            //Log.d("Stockinfo", "5");
            return res.getEntity().getContent();
        }

        public final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
            int type;
            Log.d("Stockinfo", "6");
            while ((type = parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT) {
                ;
            }
            Log.d("Stockinfo", "6b");
            //if(type!=parser.START_TAG) {
            //    throw new XmlPullParserException("No Start tag found");
            //}
            //if(parser.getName().equals(firstElementName)){
            //    throw new XmlPullParserException("Unexpected Start Tag found"+ parser.getName());
            //}

        }

        protected void onPostExecute(String result) {

            Log.d("Stockinfo", "7");
            companyNameView.setText(xmlPullParserArray[9][1]);
            yearHighView.setText("Year High: " + xmlPullParserArray[4][1]);
            yearLowView.setText("Year Low: " + xmlPullParserArray[5][1]);
            daysHighView.setText("Days High: " + xmlPullParserArray[2][1]);
            daysLowView.setText("Days Low: " + xmlPullParserArray[3][1]);
            LastPriceView.setText("Last Price: " + xmlPullParserArray[7][1]);
            changeView.setText("Change: " + xmlPullParserArray[1][1]);
            dailyPriceRangeView.setText("Daily Price Range: " + xmlPullParserArray[8][1]);
        }

        private final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException {
            int type;
            Log.d("Stockinfo", "8");
            while ((type = parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT) {
                        ;
            }
            if (type != parser.START_TAG) {
                Log.d("Stockinfo", "9");
                throw new XmlPullParserException("No Start tag found");
            }
            if (!parser.getName().equals(firstElementName)) {
                Log.d("Stockinfo", "10");
                throw new XmlPullParserException("Unexpected Start Tag found" + parser.getName());
            }
            Log.d("Stockinfo", "11");
        }


    }


}
