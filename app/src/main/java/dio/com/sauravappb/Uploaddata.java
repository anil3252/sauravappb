package dio.com.sauravappb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import dio.com.sauravappb.library.UserFunctions;



public class Uploaddata extends AppCompatActivity {

    TextView uploadreview ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        uploadreview = (TextView) findViewById(R.id.textupload);
        SQLiteDatabase db1;
        db1 = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        db1.execSQL("CREATE TABLE IF NOT EXISTS SBM(PSName VARCHAR,GPName VARCHAR,Village  VARCHAR,InspectDept VARCHAR,Mclean  VARCHAR,LoginMobile VARCHAR,SBMDesc VARCHAR,FilePath VARCHAR,lat VARCHAR,longi VARCHAR,accure VARCHAR);");
        db1.execSQL("CREATE TABLE IF NOT EXISTS MDM(PSname VARCHAR,GPName VARCHAR,Village  VARCHAR,InsepectionDept VARCHAR,Oname  VARCHAR,LoginMobile VARCHAR,Wtype VARCHAR,WSituation VARCHAR,Mfood VARCHAR,Mclean VARCHAR,Mkitchen VARCHAR,Mgrade VARCHAR,FilePath VARCHAR,lati VARCHAR,longi VARCHAR,accur VARCHAR);");
        Cursor c = db1.rawQuery("SELECT * FROM SBM", null);
        Cursor d = db1.rawQuery("SELECT * FROM MDM ", null);
        if (c.getCount() == 0 && d.getCount() == 0) {
          uploadreview.setText("You have no any data in Local System");
        } else if(c.getCount()> 0 && d.getCount()==0) {
            uploadreview.setText("Now You have "+c.getCount()+" entry of SBM in Local System");
        }else if (d.getCount()> 0 && c.getCount()==0){
            uploadreview.setText("Now You have "+d.getCount()+" entry of MDM in Local System");
        }else if (c.getCount()> 0 && d.getCount()>0){
            uploadreview.setText("Now you have "+c.getCount()+" entry of SBM and "+d.getCount()+" entry of MDM in local system");
        }
        db1.close();
    }

    public void UploadClick(View view){
        SQLiteDatabase db;
        db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS SBM(PSName VARCHAR,GPName VARCHAR,Village  VARCHAR,InspectDept VARCHAR,Mclean  VARCHAR,LoginMobile VARCHAR,SBMDesc VARCHAR,FilePath VARCHAR,lat VARCHAR,longi VARCHAR,accure VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS MDM(PSname VARCHAR,GPName VARCHAR,Village  VARCHAR,InsepectionDept VARCHAR,Oname  VARCHAR,LoginMobile VARCHAR,Wtype VARCHAR,WSituation VARCHAR,Mfood VARCHAR,Mclean VARCHAR,Mkitchen VARCHAR,Mgrade VARCHAR,FilePath VARCHAR,lati VARCHAR,longi VARCHAR,accur VARCHAR);");
        Cursor c = db.rawQuery("SELECT * FROM SBM", null);
        Cursor d = db.rawQuery("SELECT * FROM MDM ", null);

        if (c.getCount() == 0 && d.getCount() ==0 ) {
            showMessage("Details","No Data Abilable");
        } else if(c.getCount()>0) {
            while (c.moveToNext()) {
                insertToDatabase(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10));
                db.execSQL("DELETE FROM SBM where FilePath = '"+c.getString(7)+"'");
            }
        }else if (d.getCount()>0){
            while (d.moveToNext()) {
                MdmLocal mdmLocal = new MdmLocal();
                mdmLocal.insertServerDB(d.getString(0),d.getString(1),d.getString(2),d.getString(3),d.getString(4),d.getString(5),d.getString(6),d.getString(7),d.getString(8),d.getString(9),d.getString(10),d.getString(11),d.getString(12),d.getString(13),d.getString(14),d.getString(15));
                db.execSQL("DELETE FROM MDM where FilePath = '"+d.getString(12)+"'");
                showAlertStatus("Upload successfully");
            }
        }

        db.close();
    }
    private void showAlertStatus(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Uploaddata.this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent upanel = new Intent(Uploaddata.this, Uploaddata.class);
                        startActivity(upanel);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    @Override
    public void onBackPressed(){
        UserFunctions logout = new UserFunctions();
        logout.logoutUser(getApplicationContext());
        Intent login = new Intent(getApplicationContext(), MainActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish() ;
        // System.exit(0);
    }
    private void insertToDatabase(String name, String add,String village ,String inspectdep ,String ofname,String mob,String typeW ,String SituW ,String lat ,String longi, String accurecy){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramPSName = params[0];
                String paramGPName = params[1];
                String paramVillage = params[2];
                String paramInsepectionDept = params[3];
                String paramOname = params[4];
                String paramLoginMobile = params[5];
                String paramType = params[6];
                String paramSituation = params[7];
                String paramLat = params[8];
                String paramLong = params[9];
                String paramAccure = params[10];

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", paramPSName));
                nameValuePairs.add(new BasicNameValuePair("address", paramGPName));
                nameValuePairs.add(new BasicNameValuePair("village", paramVillage));
                nameValuePairs.add(new BasicNameValuePair("inspect", paramInsepectionDept));
                nameValuePairs.add(new BasicNameValuePair("offname", paramOname));
                nameValuePairs.add(new BasicNameValuePair("logmob", paramLoginMobile));
                nameValuePairs.add(new BasicNameValuePair("type", paramType));
                nameValuePairs.add(new BasicNameValuePair("situation", paramSituation));
                nameValuePairs.add(new BasicNameValuePair("lat", paramLat));
                nameValuePairs.add(new BasicNameValuePair("longi", paramLong));
                nameValuePairs.add(new BasicNameValuePair("accure", paramAccure));

                UserFunctions userFunction = new UserFunctions();
                String json = userFunction.registerSbmdata(paramPSName, paramGPName, paramVillage, paramInsepectionDept, paramOname,paramLoginMobile,paramType,paramSituation,paramLat,paramLong,paramAccure);

                return json;
            }

            @Override
            protected void onPostExecute(String result) {
                showAlert(result);
                super.onPostExecute(result);
            }
            private void showAlert(String message) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Uploaddata.this);
                builder.setMessage(message).setTitle("Response from Servers")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent upanel = new Intent(Uploaddata.this, Uploaddata.class);
                                startActivity(upanel);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, add ,village,inspectdep,ofname,mob,typeW,SituW,lat,longi,accurecy);
    }
}
