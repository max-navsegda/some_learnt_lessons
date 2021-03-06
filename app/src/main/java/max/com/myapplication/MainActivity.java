package max.com.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.TimeUnit;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button button1;
    Button button3;
    TextView textView;
    EditText editText;
    private final static String FILE_NAME = "content.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        ViewPager pager=(ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(this, getSupportFragmentManager()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText(v);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openText(v);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, age INTEGER)");
                db.execSQL("INSERT INTO users VALUES ('Tom Smith', 23);");
                db.execSQL("INSERT INTO users VALUES ('John Dow', 31);");

                Cursor query = db.rawQuery("SELECT * FROM users;", null);
                TextView textView = (TextView) findViewById(R.id.textView3);
                if(query.moveToFirst()){
                    do{
                        String name = query.getString(0);
                        int age = query.getInt(1);
                        textView.append("Name: " + name + " Age: " + age + "\n");
                    }
                    while(query.moveToNext());
                }
                query.close();
                db.close();
            }
            }
        );}

    public void saveText(View view) {
        FileOutputStream fos = null;
        String text = editText.getText().toString();
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openText(View view) {
        FileInputStream fin = null;
        textView = (TextView) findViewById(R.id.textView);
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            textView.setText(text);
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public class SendMessage extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView = (TextView) findViewById(R.id.textView);
            textView.setText("Begin");
        }

        @Override
        protected Void doInBackground(Void... unused) {

            SystemClock.sleep(2000);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.setText("End");
        }
    }
}
