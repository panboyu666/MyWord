package pan.bo.yu.myword;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {


    AlertDialog dialog;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();

    private TextView textView1,textView2 ;
    int i =1;
    SoundPool soundPool = new SoundPool.Builder().build();
    int soundID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         soundID =soundPool.load(this,R.raw.sound,1);  //最後參數沒啥用 取1
        textView1 =findViewById(R.id.text1);
        textView2 =findViewById(R.id.text2);

        //授權成功就開始
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
                init();
        }

    }

        //授權成功就開始
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions , @NonNull int[]  grantResult){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);
        if(grantResult[0]==PackageManager.PERMISSION_GRANTED &&
                grantResult[1]==PackageManager.PERMISSION_GRANTED){
                init();
            textView1.setText(arrayList.get(0));
            textView2.setText(arrayList2.get(0));
        }else{
            finish();
        }}



    private void init() {
        //如果找不到檔案就新增一個
        try {
            FileInputStream fis = openFileInput("data.txt");
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream fos = openFileOutput("data.txt",MODE_PRIVATE);
                fos.write("word 單詞\n".getBytes());
                fos.flush();
                fos.close();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } }

        //讀取檔案 分割 傳入arrayList
        try {
            FileInputStream  fis2 = openFileInput("data.txt");
            BufferedReader br =new BufferedReader(new InputStreamReader(fis2));
            String line;
            while(true){
                try {
                    if (!((line =br.readLine())!=null)) break;


                    String s =line;
                    String []x = s.split(" ");
                    arrayList.add(x[0]);
                    arrayList2.add(x[1]);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }



    private void brush() {

        soundPool.play(soundID,1,1,0,0,1);
        if(i==arrayList.size()){
            i=0;
        }

        textView1.setText(arrayList.get(i));
        textView2.setText(arrayList2.get(i));

        i++;

    }

    public void add(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        dialog = builder.create();
        View dialogView = View.inflate(MainActivity.this, R.layout.mydialog, null);
        dialog.setView(dialogView);
        dialog.show();


        }

    public void delete(View view) {

        if (arrayList.size() > 1) {


            arrayList.remove(i - 1);
            arrayList2.remove(i - 1);


            //壓進去檔案
            try {
                FileOutputStream fos = openFileOutput("data.txt", MODE_PRIVATE);

                for (int i = 0; i < arrayList.size(); i++) {
                    String s = arrayList.get(i) + " " + arrayList2.get(i) + "\n";
                    ;

                    fos.write(s.getBytes());

                }

                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            textView1.setText(arrayList.get(0));
            textView2.setText(arrayList2.get(0));

            arrayList.removeAll(arrayList);
            arrayList2.removeAll(arrayList2);
            i=0;
            init();


        } //if
        else {
            Toast.makeText(this, "至少要留一個唷", Toast.LENGTH_SHORT).show();
        }
    }

    public void next(View view) {

    brush();

    }

    public void addWord(View view)  {



    }



    public void closeDialog(View view) {
        dialog.cancel();
    }
    public void addDialog(View view) {


        EditText editText= (EditText)dialog.findViewById(R.id.EditText1);
        EditText editText2= (EditText)dialog.findViewById(R.id.EditText2);

        String s1 =editText.getText().toString();
        String s2 =editText2.getText().toString();

      if(s1.equals("")||s2.equals("")){
          Toast.makeText(this,  "不能為空", Toast.LENGTH_SHORT).show();
      } else if(!s1.equals("")&&!s2.equals("")){
          Toast.makeText(this,  "成功輸入", Toast.LENGTH_SHORT).show();


          try {
          FileOutputStream fos = openFileOutput("data.txt",MODE_APPEND);
          String s = s1+" "+s2+"\n";
          fos.write(s.getBytes());
          fos.flush();
          fos.close();
          }

                catch (IOException e) {
              e.printStackTrace();
          }

          arrayList.removeAll(arrayList);
          arrayList2.removeAll(arrayList2);
          init();
          textView1.setText(s1);
          textView2.setText(s2);
          dialog.cancel();

        }

    }
}