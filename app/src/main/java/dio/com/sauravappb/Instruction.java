package dio.com.sauravappb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dio.com.sauravappb.library.UserFunctions;

/**
 * Created by ss1 on 8/14/2017.
 */

public class Instruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruct);
    }

    @Override
    public void onBackPressed(){
        UserFunctions logout = new UserFunctions();
        logout.logoutUser(getApplicationContext());
        Intent login = new Intent(getApplicationContext(), MainActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish() ;
    }
}
