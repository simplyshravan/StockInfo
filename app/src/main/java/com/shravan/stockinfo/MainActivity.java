package com.shravan.stockinfo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends ActionBarActivity {

    public final static String STOCK_SYMBOL = "com.example.stockqoute.STOCK";

    private SharedPreferences stockSymbolEntered;
    private TableLayout stockTableScrollView ;
    private EditText stockSymbolEditText;
    Button enterStockSymbol;
    Button deleteStockButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stockSymbolEntered = getSharedPreferences("stocklist", MODE_PRIVATE);
        stockSymbolEditText = (EditText) findViewById(R.id.stockSymbolEditText);
        enterStockSymbol = (Button) findViewById(R.id.enterStockSymbol);
        deleteStockButton = (Button) findViewById(R.id.deleteStockButton);
        enterStockSymbol.setOnClickListener(enterStockSymbolListener);
        deleteStockButton.setOnClickListener(deleteStockButtonListener);
        stockTableScrollView= (TableLayout) findViewById(R.id.stockTableScrollView);
        //Log.d("1", "enter");
        updateStockSaveList(null);
        //Log.d("1b", "enter");


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updateStockSaveList(String newStockSymbol) {
        //Log.d("3", "enter");
        String[] stock = stockSymbolEntered.getAll().keySet().toArray(new String[0]);
        Arrays.sort(stock, String.CASE_INSENSITIVE_ORDER);
        if (newStockSymbol != null) {
            //Log.d("3", "mid");
            insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stock, newStockSymbol));


        } else {
            //Log.d("3", "end");
            for (int i = 0; i < stock.length; i++)
            {   //Log.d("3", stock[i]+" "+i);
                insertStockInScrollView(stock[i], i);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void saveStockSymbol(String newStock) {
        //Log.d("2", newStock);

        String isTheNewStock = stockSymbolEntered.getString(newStock, null);
        SharedPreferences.Editor preferenceEditor = stockSymbolEntered.edit();
        preferenceEditor.putString(newStock, newStock);
        preferenceEditor.apply();
        //Log.d("2", isTheNewStock);
        if (newStock != null) {
            //Log.d("2", "mid");
            updateStockSaveList(newStock);
            //Log.d("2", "end");
        }

    }

    private void insertStockInScrollView(String stock, int arrayIndex) {
        //Log.d("4", "start");
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newStockRow = inflator.inflate(R.layout.stock_qoute_row, null);
        TextView stocksymboltextView = (TextView) newStockRow.findViewById(R.id.stockSymbolTextView);
        stocksymboltextView.setText(stock);
        Button stockQouteButton = (Button) newStockRow.findViewById(R.id.stockQouteButton);
        Button qouteFromWebButton = (Button) newStockRow.findViewById(R.id.qouteFromWebButton);
        stockQouteButton.setOnClickListener(getStockActivityListener);
        qouteFromWebButton.setOnClickListener(getStockFromWebListener);
        //Log.d("4", stock+" "+arrayIndex);
        stockTableScrollView.addView(newStockRow, arrayIndex);
        //Log.d("4", "end");
    }

    public View.OnClickListener enterStockSymbolListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if (stockSymbolEditText.getText().length() > 0) {

                //Log.d("1", stockSymbolEditText.getText().toString());
                saveStockSymbol(stockSymbolEditText.getText().toString());
                stockSymbolEditText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stockSymbolEditText.getWindowToken(), 0);
                //Log.d("1", "exit");
            } else {
                AlertDialog.Builder bldr = new AlertDialog.Builder(MainActivity.this);
                bldr.setTitle(R.string.invalid_stock);//stockSymbolEditText.getText().length()
                bldr.setPositiveButton(R.string.ok, null);
                bldr.setMessage(R.string.missing_stock);
                AlertDialog thealertdialog = bldr.create();
                thealertdialog.show();

            }

        }
    };

    public void deleteAllStocks() {
        stockTableScrollView.removeAllViews();

    }

    public View.OnClickListener deleteStockButtonListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onClick(View v) {
            deleteAllStocks();
            SharedPreferences.Editor preferencesEditor = stockSymbolEntered.edit();
            preferencesEditor.clear();
            preferencesEditor.apply();
        }
    };

    public View.OnClickListener getStockActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow tableRow = (TableRow) v.getParent();
            TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
            String stockSymbol = stockTextView.getText().toString();
            Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
            intent.putExtra(STOCK_SYMBOL, stockSymbol);
            startActivity(intent);
        }
    };

    public View.OnClickListener getStockFromWebListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow tableRow = (TableRow) v.getParent();
            TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
            String stockSymbol = stockTextView.getText().toString();
            String stockUrl = getString(R.string.yahoo_stock_url) + stockSymbol;
            Intent getStockWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(stockUrl));
            startActivity(getStockWeb);


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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
}
