package control.user.com.usercontrol.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.user.com.usercontrol.R;
import control.user.com.usercontrol.database.DbConstants;
import control.user.com.usercontrol.database.DbEntryService;

public class UserListActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ListView userView = (ListView) findViewById(R.id.userList);

        ArrayList<HashMap<String, String>> allAccounts = DbEntryService.getAllAccounts();
        List<String> names = new ArrayList<>();
        for (HashMap<String, String> account : allAccounts) {
            names.add(account.get(DbConstants.ACCOUNT_NAME) + "");
        }
    }
}
