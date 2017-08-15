package android.mso.com.uploadimagesapp;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final File mFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "upload_investigate");

        findViewById(R.id.send_by_multipart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INICIO multipart", new Date().toString());
                for(File file : mFolder.listFiles()){
                    Service.sendImageByMultipart(file);
                }
            }
        });


        findViewById(R.id.send_by_base64).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INICIO base64", new Date().toString());
                for(File file : mFolder.listFiles()){
                    Service.sendImageByBase64(file);
                }
            }
        });
    }
}
