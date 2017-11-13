package control.user.com.usercontrol.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import control.user.com.usercontrol.R;
import control.user.com.usercontrol.database.Database;
import control.user.com.usercontrol.database.DbTableService;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Database database = new Database(this);
        DbTableService.createAccountTable();

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
