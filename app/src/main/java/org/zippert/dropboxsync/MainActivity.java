package org.zippert.dropboxsync;

import com.dropbox.sync.android.DbxAccountManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private DbxAccountManager mDbxAccountManager;
    private static final int REQUESTCODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbxAccountManager = DbxAccountManager
                .getInstance(this.getApplicationContext(), SyncValues.APP_KEY, SyncValues.APP_SECRET);
        Button connectButton = (Button)findViewById(R.id.connect_button);
        View contentView = findViewById(R.id.content);
        if (mDbxAccountManager.hasLinkedAccount()) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content, MainFragment.newInstance(), MainFragment.TAG).commit();
            connectButton.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        } else {
            connectButton.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            connectButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });
        }

    }

    DbxAccountManager getDropboxAccountManager(){
        return mDbxAccountManager;
    }

    private void login() {
        mDbxAccountManager.startLink(this, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                getFragmentManager().beginTransaction().replace(R.id.content,
                        MainFragment.newInstance(), MainFragment.TAG).commit();
            } else {
                Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

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
}
