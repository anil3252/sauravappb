package dio.com.sauravappb;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import dio.com.sauravappb.library.DatabaseHandler;
import dio.com.sauravappb.library.UserFunctions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Sbm extends AppCompatActivity
{
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";
    private static final int CAMERA_REQUEST = 1;
    String FilePath ="" ;
    String LoginMobile = "000";
    Spinner inputPanchayatSamiti;
    Spinner inputGPName;
    Spinner inputVisitDept;
    Spinner inputProcess;
    Spinner inputPosition;
    EditText inputVillageName;
    Button btnRegister,btnlocalsub;
    TextView registerErrorMsg , textViewWork;
    ImageView mimageView;
    LinearLayout container ,MDMcontainer;
    Spinner inputFood ,inputPlace,inputKitchen;
    EditText inputdesc;
    int prevpos=8;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;

    ArrayList<String> pslist = new ArrayList<String>();
    ArrayList<String> select = new ArrayList<String>();
    ArrayList<String> kar = new ArrayList<String>();
    ArrayList<String> hind = new ArrayList<String>();
    ArrayList<String> spt = new ArrayList<String>();
    ArrayList<String> mnd = new ArrayList<String>();
    ArrayList<String> ndt = new ArrayList<String>();
    ArrayList<String> tdm = new ArrayList<String>();
    ArrayList<String> vibhag = new ArrayList<String>();
    ArrayList<String> process = new ArrayList<String>();
    ArrayList<String> postionwork = new ArrayList<String>();
    ArrayList<String> afood = new ArrayList<String>();
    ArrayList<String> aplace = new ArrayList<String>();
    ArrayList<String> akitchen = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbm);



        registerErrorMsg = (TextView) findViewById(R.id.register_error);

        mimageView = (ImageView) this.findViewById(R.id.image_from_camera);
        Button button = (Button) this.findViewById(R.id.take_image_from_camera);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            // button.setEnabled(false);
//            ActivityCompat.requestPermissions(this, new String[] {
//                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, 0);
//        }
        if(checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    private  boolean checkAndRequestPermissions() {
        int permissioncam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int statepermissionntwrk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (statepermissionntwrk != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (permissioncam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 0) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                //button.setEnabled(true);
//            }
//        }
    }
    public void takeImageFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra ("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Bitmap dest = Bitmap.createBitmap(mphoto.getWidth(), mphoto.getHeight(), Bitmap.Config.ARGB_8888);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
            String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
            Canvas cs = new Canvas(dest);
            Paint tPaint = new Paint();
            tPaint.setTextSize(10);
            tPaint.setColor(Color.RED);
            tPaint.setStyle(Paint.Style.FILL);
            cs.drawBitmap(mphoto, 5f, 5f, null);
            cs.drawText(dateTime, 10,25, tPaint);
            String Village = inputVillageName.getText().toString();
            cs.drawText(Village, 10,35, tPaint);
            try {
                dest.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/timestamped")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mimageView.setImageBitmap(mphoto);
            }
            mimageView.setImageBitmap(dest);
            Uri tempUri = getImageUri(getApplicationContext(), dest);
            FilePath =    getRealPathFromURI(tempUri);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
       // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public void addItemsOnSpinner2() {

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
    public void addListenerOnSpinnerItemSelection() {
        inputPanchayatSamiti = (Spinner) findViewById(R.id.psname);
        inputGPName = (Spinner) findViewById(R.id.gpname);
        inputVisitDept = (Spinner) findViewById(R.id.visitdeptsbm);
        inputVillageName = (EditText) findViewById(R.id.villagename);
        inputVisitDept = (Spinner) findViewById(R.id.visitdeptsbm);
        inputPosition = (Spinner) findViewById(R.id.activity);
        inputPlace = (Spinner)findViewById(R.id.vcleaning);
        container = (LinearLayout)findViewById(R.id.containersbm);
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.sbmrow, null);
       // container.addView(addView);
        inputdesc = (EditText) addView.findViewById(R.id.descr);

        btnRegister = (Button) findViewById(R.id.btnSubmit);
        btnlocalsub = (Button) findViewById(R.id.btnlocalsub);
        fillarray();
        inputPanchayatSamiti.setAdapter(new ArrayAdapter<String>(Sbm.this,
                android.R.layout.simple_dropdown_item_1line, pslist));
        inputVisitDept.setAdapter(new ArrayAdapter<String>(Sbm.this,
                android.R.layout.simple_dropdown_item_1line, vibhag));
        inputPosition.setAdapter(new ArrayAdapter<String>(Sbm.this,
                android.R.layout.simple_dropdown_item_1line, postionwork));
        inputPlace.setAdapter(new ArrayAdapter<String>(Sbm.this,
                android.R.layout.simple_dropdown_item_1line, aplace));

        inputPanchayatSamiti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                // TODO Auto-generated method stub
                if (pos == 0) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, select));
                }
                if (pos == 1) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, kar));
                }
                if(pos == 2){
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, hind));
                }
                if (pos == 3) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, spt));
                }
                if(pos == 4){
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, tdm));
                }
                if (pos == 5) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, mnd));
                }
                if(pos == 6){
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Sbm.this,
                            android.R.layout.simple_dropdown_item_1line, ndt));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        inputPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {

                if(pos==3){
                    container.addView(addView);
                    prevpos = pos;

                }else
                    if(prevpos ==3 && pos!=3){
                    ((LinearLayout)addView.getParent()).removeView(addView);
                        prevpos =pos ;
                }
              }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (( !inputVillageName.getText().toString().equals(""))&&( !inputPanchayatSamiti.getSelectedItem().toString().equals("---चयन करे---")) &&( !inputGPName.getSelectedItem().toString().equals("---चयन करे---")) && (!FilePath.equals("")) )
                {
                    if ( !inputVillageName.getText().toString().equals("") ){
                        String PSname,GPName,Village,InsepectionDept,Mclean,SBMDesc ,MACAdd;
                        btnRegister.setEnabled(false);
                        PSname = inputPanchayatSamiti.getSelectedItem().toString();
                        GPName = inputGPName.getSelectedItem().toString();
                        Village = inputVillageName.getText().toString();
                        InsepectionDept = inputPosition.getSelectedItem().toString();
                        Mclean = inputPlace.getSelectedItem().toString();
                        //MACAdd = getMacaddress();
                        if ( inputPosition.getSelectedItem().toString().equals("अन्य") ){
                            SBMDesc = inputdesc.getText().toString();
                        }else {
                            SBMDesc= "NA";
                        }
                        //LoginMobile = getIntent().getStringExtra("One");
                        SQLiteDatabase db;
                        String lati="", longi="",accur="" ;
                        db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS details(name VARCHAR,number VARCHAR);");
                        db.execSQL("CREATE TABLE IF NOT EXISTS Locations (latitude VARCHAR,longitude VARCHAR,accurecy VARCHAR);");
                        Cursor c = db.rawQuery("SELECT * FROM details", null);
                        Cursor d = db.rawQuery("SELECT * FROM Locations", null);
                        if (c.getCount() <= 1) {
                            while (c.moveToNext()) {
                                LoginMobile = c.getString(1);
                            }
                        }
                        if (d.getCount() <= 1) {
                            while (d.moveToNext()) {
                                lati = d.getString(0);
                                longi = d.getString(1);
                                accur = d.getString(2);
                            }
                        }
                        db.close();
                        insertToDatabase(PSname,GPName,Village,InsepectionDept,Mclean,LoginMobile,SBMDesc,FilePath,lati,longi,accur );

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "कृपया आवश्यक कॉलम की पूर्ति करे", Toast.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                }
            }
        });

        btnlocalsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (( !inputVillageName.getText().toString().equals(""))&&( !inputPanchayatSamiti.getSelectedItem().toString().equals("---चयन करे---")) &&( !inputGPName.getSelectedItem().toString().equals("---चयन करे---")) && (!FilePath.equals("")) )
                {
                    if ( !inputVillageName.getText().toString().equals("") ){
                        String PSname,GPName,Village,InsepectionDept,Mclean,SBMDesc ,MACAdd;
                        btnlocalsub.setEnabled(false);
                        PSname = inputPanchayatSamiti.getSelectedItem().toString();
                        GPName = inputGPName.getSelectedItem().toString();
                        Village = inputVillageName.getText().toString();
                        InsepectionDept = inputPosition.getSelectedItem().toString();
                        Mclean = inputPlace.getSelectedItem().toString();
                        //MACAdd = getMacaddress();
                        if ( inputPosition.getSelectedItem().toString().equals("अन्य") ){
                            SBMDesc = inputdesc.getText().toString();
                        }else {
                            SBMDesc= "NA";
                        }
                        //LoginMobile = getIntent().getStringExtra("One");
                        SQLiteDatabase db;
                        String lati="", longi="",accur="" ;
                        db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS details(name VARCHAR,number VARCHAR);");
                        db.execSQL("CREATE TABLE IF NOT EXISTS Locations (latitude VARCHAR,longitude VARCHAR,accurecy VARCHAR);");
                        Cursor c = db.rawQuery("SELECT * FROM details", null);
                        Cursor d = db.rawQuery("SELECT * FROM Locations", null);
                        if (c.getCount() <= 1) {
                            while (c.moveToNext()) {
                                LoginMobile = c.getString(1);
                            }
                        }
                        if (d.getCount() <= 1) {
                            while (d.moveToNext()) {
                                lati = d.getString(0);
                                longi = d.getString(1);
                                accur = d.getString(2);
                            }
                        }
                        db.close();
                        SaveOnLocalDB(PSname,GPName,Village,InsepectionDept,Mclean,LoginMobile,SBMDesc,FilePath,lati,longi,accur);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "कृपया आवश्यक कॉलम की पूर्ति करे", Toast.LENGTH_SHORT).show();
                }
                btnlocalsub.setEnabled(true);
            }
        });
    }

    public void SaveOnLocalDB(String PSName,String GPName,String Village,String InsepectionDept,String Mclean,String LoginMobile,String SBMDesc,String FilePath,String lat,String longi,String accur) {
        SQLiteDatabase db;
        db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS SBM(PSName VARCHAR,GPName VARCHAR,Village  VARCHAR,InspectDept VARCHAR,Mclean  VARCHAR,LoginMobile VARCHAR,SBMDesc VARCHAR,FilePath VARCHAR,lat VARCHAR,longi VARCHAR,accure VARCHAR);");
        Cursor c = db.rawQuery("SELECT * FROM SBM ", null);
        if (c.getCount() < 3)
        {
            db.execSQL("INSERT INTO SBM VALUES('" + PSName + "','" + GPName + "','" + Village + "','" + InsepectionDept + "','" + Mclean + "','" + LoginMobile + "','" + SBMDesc + "','" + FilePath + "','" + lat + "','" + longi + "','" + accur + "');");
            Intent upanel = new Intent(Sbm.this, Sbm.class);
            //upanel.putExtra("One",LoginMobile);
            startActivity(upanel);
            finish();
        }
        else
        {
            showMessage("Details","कृपया Local के डाटा को सर्वर पर अपलोड करे");
        }
        db.close();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    private void fillarray() {
        // TODO Auto-generated method stub
        pslist.clear();
        pslist.add("---चयन करे---");
        pslist.add("करौली");pslist.add("हिन्डोन");
        pslist.add("सपोटरा");pslist.add("टोडाभीम");pslist.add("मंडरायल");
        pslist.add("नादौती");

        select.clear();
        select.add("---चयन करे---");

        kar.clear();
        kar.add("---चयन करे---");
        kar.add("करौली शहर");
        kar.add("अतेवा");
        kar.add("कंचनपुर");
        kar.add("करसाई");
        kar.add("काशीपुरा");
        kar.add("कैलादेवी");
        kar.add("कोंडर");
        kar.add("कोटा");
        kar.add("कोटा छावर");
        kar.add("खुंडा");
        kar.add("खुवनगर");
        kar.add("खेडिया");
        kar.add("खोहरी");
        kar.add("गुडला");
        kar.add("गुनसेरा");
        kar.add("गुवरेडा");
        kar.add("गुरेई");
        kar.add("चैनपुर");
        kar.add("जमूरा");
        kar.add("जहॉंगीरपुर");
        kar.add("डाण्डा");
        kar.add("डुकाबली");
        kar.add("तुलसीपुरा");
        kar.add("नारायणा");
        kar.add("परीता");
        kar.add("पिपरानी");
        kar.add("फतेहपुर");
        kar.add("बीजलपुर");
        kar.add("भावली ");
        kar.add("महोली");
        kar.add("मामचारी");
        kar.add("मासलपुर");
        kar.add("मॉची");
        kar.add("रघुवंशी");
        kar.add("रतियापुरा");
        kar.add("राजौर");
        kar.add("रामपुर");
        kar.add("रूंधपुरा");
        kar.add("रौंड कला");
        kar.add("लोहर्रा");
        kar.add("ससेड़ी");
        kar.add("सायपुर");
        kar.add("सीलौती");
        kar.add("सेंमरदा");
        kar.add("सैगरपुरा");
        kar.add("हरनगर");





        hind.clear();
        hind.add("---चयन करे---");
        hind.add("हिंडोन शहर");
        hind.add("अकबरपुर");
        hind.add("अलीपुरा");
        hind.add("इरनिया");
        hind.add("कटकड़");
        hind.add("करसोली");
        hind.add("काचरौली");
        hind.add("कोटरी");
        hind.add("क्यारदा खुर्द");
        hind.add("खरेटा");
        hind.add("खेड़ली गुजर");
        hind.add("खेड़ा");
        hind.add("खेड़ी हैवत");
        hind.add("गांवड़ा मीना");
        hind.add("गांवडी मीणा");
        hind.add("गुनसार");
        hind.add("घोंसला");
        hind.add("चांदनगांव");
        hind.add("चिनायटा");
        hind.add("जगर");
        hind.add("जटनगला");
        hind.add("जटवाड़ा");
        hind.add("झारेड़ा");
        hind.add("टोडूपुरा");
        hind.add("ढहरा");
        hind.add("ढिंढोरा");
        hind.add("दानालपुर");
        hind.add("नंगला मीणा");
        hind.add("पटोंदा");
        hind.add("पालनपुर");
        hind.add("पाली");
        hind.add("फुलवाड़ा");
        hind.add("बझेंड़ा");
        hind.add("बरगमा");
        hind.add("बाईजट़");
        hind.add("बाजना कला");
        hind.add("भंगों");
        hind.add("भुकरावली");
        hind.add("मण्डावरा");
        hind.add("महु इब्राहिमपुर");
        hind.add("महू खास");
        hind.add("मोठियापुरा");
        hind.add("रेवई");
        hind.add("लहचोड़ा");
        hind.add("वनकी");
        hind.add("विजयपुरा");
        hind.add("शेरपुर");
        hind.add("श्री महावीर जी");
        hind.add("सनेट");
        hind.add("सिकरौदा मीना");
        hind.add("सूरोठ");
        hind.add("सोमली");
        hind.add("हुक्मीखेडा");

        spt.clear();
        spt.add("---चयन करे---");
        spt.add("अमरगढ");
        spt.add("अमरवाड़");
        spt.add("इनायती");
        spt.add("एकट");
        spt.add("औड़च");
        spt.add("काचरौदा");
        spt.add("कालागुढा");
        spt.add("कुड़गाव");
        spt.add("खेडला");
        spt.add("गज्जूपुरा");
        spt.add("चौड़ागांव");
        spt.add("जाखौदा");
        spt.add("जीरौता");
        spt.add("जोड़ली");
        spt.add("डाबरा");
        spt.add("डीकोली कला");
        spt.add("दौलतपुरा");
        spt.add("नारौली");
        spt.add("निभेरा");
        spt.add("बकना");
        spt.add("बगीदा");
        spt.add("बड़ौदा");
        spt.add("बाजना");
        spt.add("बापौती");
        spt.add("बालौती");
        spt.add("भरतून");
        spt.add("महमदपुर");
        spt.add("लेदिया");
        spt.add("लुलोज");
        spt.add("सपोटरा");
        spt.add("सलेमपुर");
        spt.add("हरिया का मंदिर");
        spt.add("हाड़ौती");

        tdm.clear();
        tdm.add("---चयन करे---");
        tdm.add("टोडाभीम शहर");
        tdm.add("अजीजपुर");
        tdm.add("उद्रेन");
        tdm.add("कंजौली");
        tdm.add("कटारा अजीज");
        tdm.add("कमालपुरा");
        tdm.add("करीरी");
        tdm.add("किरवाड़ा");
        tdm.add("कुढावल");
        tdm.add("खेड़ी");
        tdm.add("खोहरा");
        tdm.add("गोरडा");
        tdm.add("जगदीशपुरा");
        tdm.add("जोैल");
        tdm.add("झाड़ीसा");
        tdm.add("तिघरिया");
        tdm.add("देवलेन");
        tdm.add("धवान");
        tdm.add("नांगल शेरपुर");
        tdm.add("नागल माडल");
        tdm.add("निसूरा");
        tdm.add("पदमपुरा");
        tdm.add("पहाड़ी");
        tdm.add("पाडला खालसा");
        tdm.add("बालघाट");
        tdm.add("बौल");
        tdm.add("भजेड़ा");
        tdm.add("भण्डारी अन्दरूनी");
        tdm.add("भनकपुरा");
        tdm.add("भोपुर");
        tdm.add("मडेरू");
        tdm.add("महमदपुर");
        tdm.add("महस्वा");
        tdm.add("माचड़ी");
        tdm.add("मातासूला");
        tdm.add("मानोज");
        tdm.add("मूण्डिया");
        tdm.add("मोरड़ा");
        tdm.add("रानौली");
        tdm.add("लपावली");
        tdm.add("शहराकर");
        tdm.add("शेखपुरा");
        tdm.add("साकरवाडा");
        tdm.add("सिंघानिया");


        ndt.clear();
        ndt.add("---चयन करे---");
        ndt.add("कुंजेला");
        ndt.add("कैमरी");
        ndt.add("कैमला");
        ndt.add("कैमा");
        ndt.add("गढखेड़ा");
        ndt.add("गदमोरा");
        ndt.add("गुढाचन्द्रजी");
        ndt.add("चिरावडा");
        ndt.add("जीतकीपुर");
        ndt.add("ढहरिया");
        ndt.add("तालचिड़ा");
        ndt.add("तिमाबा");
        ndt.add("तेसगाव");
        ndt.add("दलपुरा");ndt.add("धोैलेता");
        ndt.add("नादौती");ndt.add("पाल");
        ndt.add("बड़ागाव");ndt.add("बरदाला");
        ndt.add("बागौर");ndt.add("बाड़ाराजपुर");
        ndt.add("भीलापाडा");ndt.add("मेदेकपुरा");
        ndt.add("राजाहेड़ा");ndt.add("रायसना");
        ndt.add("रौसी");ndt.add("शहर");
        ndt.add("सलावद");ndt.add("सोप");


        mnd.clear();
        mnd.add("---चयन करे---");
        mnd.add("ओंड");
        mnd.add("करणपुर");
        mnd.add("कसेड़");
        mnd.add("गढ़ी का गांव");
        mnd.add("गुरदह");
        mnd.add("चन्देलीपुरा");
        mnd.add("टोडा");
        mnd.add("दरगमा");
        mnd.add("धोरेटा");
        mnd.add("नानपुर");
        mnd.add("नीदर");
        mnd.add("पांचोली");
        mnd.add("बहादुरपुर");
        mnd.add("बाटदा");
        mnd.add("बुगडार");
        mnd.add("भांकरी");
        mnd.add("मंडरायल");
        mnd.add("महाराजपुर");
        mnd.add("मांगेपुरा");
        mnd.add("रानीपुरा");
        mnd.add("राहिर");
        mnd.add("रोधई");    mnd.add("लांगरा");

        vibhag.clear();
        vibhag.add("SBM");


        process.clear();
        process.add("स्थान का चयन");
        process.add("कार्य प्रारम्भ");
        process.add("कार्य पूर्ण");
        process.add("कार्य अपूर्ण एवं रुका हुआ");
        process.add("कार्य प्रगिति पर");

        postionwork.clear();
        postionwork.add("Morning Follow Up");
        postionwork.add("छात्र/ छात्रा रैली");
        postionwork.add("उक्त दोनों");
        postionwork.add("अन्य");



        afood.clear();
        afood.add("अच्छी");
        afood.add("अच्छी नहीं");
        afood.add("MDM नहीं बन रहा");

        aplace.clear();
        aplace.add("सही है");
        aplace.add("सही नहीं");

        akitchen.clear();
        akitchen.add("NGO द्वारा MDM");
        akitchen.add("हाँ");
        akitchen.add("नहीं");

    }


    // get the selected dropdown list value
    public void addListenerOnButton() {

    }
    private void insertToDatabase(String name, String add,String village ,String inspectdep ,String ofname,String mob,String typeW ,String SituW ,String lati, String longi,String accur){
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
                String paramAccur = params[10];

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
                nameValuePairs.add(new BasicNameValuePair("accur", paramAccur));
                UserFunctions userFunction = new UserFunctions();
                String json = userFunction.registerSbmdata(paramPSName, paramGPName, paramVillage, paramInsepectionDept, paramOname,paramLoginMobile,paramType,paramSituation,paramLat,paramLong,paramAccur);

                return json;
            }

            @Override
            protected void onPostExecute(String result) {
                showAlert(result);
                super.onPostExecute(result);
            }
            private void showAlert(String message) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Sbm.this);
                builder.setMessage(message).setTitle("Response from Servers")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent upanel = new Intent(Sbm.this, Sbm.class);
                                //upanel.putExtra("One",LoginMobile);
                                startActivity(upanel);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, add ,village,inspectdep,ofname,mob,typeW,SituW,lati,longi,accur);
    }
}


