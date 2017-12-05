package control.user.com.usercontrol.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import control.user.com.usercontrol.R;
import control.user.com.usercontrol.database.Database;
import control.user.com.usercontrol.database.DbTableService;

public class MainActivity extends AppCompatActivity{

    public static OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Database database = new Database(this);
        DbTableService.createAccountTable();
        client = new OkHttpClient();
        client.setConnectTimeout(45, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(45, TimeUnit.SECONDS);
        List<Protocol> protocols = client.getProtocols();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));

        Button button = (Button) findViewById(R.id.btnLogInOut);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
