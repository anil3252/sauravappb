package dio.com.sauravappb;

import android.Manifest;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dio.com.sauravappb.library.UserFunctions;


public class Work extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int CAMERA_REQUEST = 1;

    ImageView mimageView;
    Spinner inputPanchayatSamiti ,inputGPName ,inputVisitDept,inputOfficeClean,inputListEmp,inputSlip;
    EditText inputVillageName,inputOfficeName,inputTotalEmp,inputWithOutinfoEmp,inputAcsentEmp,inputdiscription,inputWorkProcess ;
    Button btnRegister;
    LinearLayout container;
    TextView textViewWork,test ;
    String FilePath ="" ;
    String LoginMobile = "000";
    int prevpos=8;

    ArrayList<String> pslist = new ArrayList<String>();
    ArrayList<String> select = new ArrayList<String>();
    ArrayList<String> kar = new ArrayList<String>();
    ArrayList<String> hind = new ArrayList<String>();
    ArrayList<String> spt = new ArrayList<String>();
    ArrayList<String> mnd = new ArrayList<String>();
    ArrayList<String> ndt = new ArrayList<String>();
    ArrayList<String> tdm = new ArrayList<String>();
    ArrayList<String> vibhag = new ArrayList<String>();
    ArrayList<String> officeclean = new ArrayList<String>();
    ArrayList<String> emplist = new ArrayList<String>();
    ArrayList<String> slip = new ArrayList<String>();
    ArrayList<String> avility = new ArrayList<String>();
    ArrayList<String> space = new ArrayList<String>();
    ArrayList<String> spaceelem = new ArrayList<String>();
    ArrayList<String> stay = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        mimageView = (ImageView) this.findViewById(R.id.image_from_camera);
        test = (TextView) findViewById(R.id.testsrv);
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
                dest.compress(Bitmap.CompressFormat.JPEG, 90, new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/timestamped")));
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish() ;
        // System.exit(0);
    }
    public void addListenerOnSpinnerItemSelection() {


        inputPanchayatSamiti = (Spinner) findViewById(R.id.psname);
        inputGPName = (Spinner) findViewById(R.id.gpname);
        inputVillageName = (EditText) findViewById(R.id.villagename);
        inputVisitDept = (Spinner) findViewById(R.id.visitdept);
        inputOfficeName = (EditText) findViewById(R.id.oname);
        inputOfficeClean = (Spinner) findViewById(R.id.clean);
        inputTotalEmp = (EditText) findViewById(R.id.no_emp);
        inputWithOutinfoEmp = (EditText) findViewById(R.id.no_wi_emp);
        inputAcsentEmp = (EditText) findViewById(R.id.no_ab_emp);
        inputListEmp = (Spinner) findViewById(R.id.list_emp);
        inputSlip = (Spinner) findViewById(R.id.candidate_slip);
        //inputAvilityOffice = (Spinner) findViewById(R.id.owner);
        //inputSpace = (Spinner) findViewById(R.id.space);
        //inputStayEmp = (Spinner) findViewById(R.id.stay_emp);
        inputdiscription = (EditText) findViewById(R.id.describe);
        btnRegister = (Button) findViewById(R.id.btnSubmit);

        container = (LinearLayout)findViewById(R.id.containerWork);
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.workrow, null);
        inputWorkProcess = (EditText) findViewById(R.id.processwork);
        textViewWork = (TextView) findViewById(R.id.saurav);
        fillarray();
        inputPanchayatSamiti.setAdapter(new ArrayAdapter<String>(Work.this,
                android.R.layout.simple_dropdown_item_1line, pslist));
        inputVisitDept.setAdapter(new ArrayAdapter<String>(Work.this,
                android.R.layout.simple_dropdown_item_1line, vibhag));
        inputOfficeClean.setAdapter(new ArrayAdapter<String>(Work.this,
                android.R.layout.simple_dropdown_item_1line, officeclean));
        inputListEmp.setAdapter(new ArrayAdapter<String>(Work.this,
                android.R.layout.simple_dropdown_item_1line, emplist));
        inputSlip.setAdapter(new ArrayAdapter<String>(Work.this,
                android.R.layout.simple_dropdown_item_1line, slip));
        //inputAvilityOffice.setAdapter(new ArrayAdapter<String>(Work.this,
                //android.R.layout.simple_dropdown_item_1line, avility));
//        inputSpace.setAdapter(new ArrayAdapter<String>(Office.this,
//                android.R.layout.simple_dropdown_item_1line, space));
        //inputStayEmp.setAdapter(new ArrayAdapter<String>(Work.this,
          //      android.R.layout.simple_dropdown_item_1line, stay));


        inputPanchayatSamiti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                // TODO Auto-generated method stub
                if (pos == 0) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, select));
                }
                if (pos == 1) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, kar));
                }
                if(pos == 2){
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, hind));
                }
                if (pos == 3) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, spt));
                }
                if(pos == 4){
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, tdm));
                }
                if (pos == 5) {
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, mnd));
                }
                if(pos == 6){
                    inputGPName.setAdapter(new ArrayAdapter<String>(
                            Work.this,
                            android.R.layout.simple_dropdown_item_1line, ndt));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        inputOfficeClean.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                if(pos==2 || pos ==4){
                    if(prevpos==2 || prevpos ==4){
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                    container.addView(addView);
                    prevpos = pos;

                }else
                if((prevpos ==4 || prevpos ==2) &&(pos!=4 || pos!=2)){
                    ((LinearLayout)addView.getParent()).removeView(addView);
                    prevpos =pos ;
                }
                if (inputOfficeClean.getSelectedItem().toString().equals("कार्य प्रगतिरत")){
                    textViewWork = (TextView) findViewById(R.id.saurav);
                    textViewWork.setText("  प्रगति%");
                    inputWorkProcess = (EditText) findViewById(R.id.processwork);
                    inputWorkProcess.setInputType(InputType.TYPE_CLASS_NUMBER);

                }else if(inputOfficeClean.getSelectedItem().toString().equals("अपूर्ण एव रुका हुआ")){
                    textViewWork = (TextView) findViewById(R.id.saurav);
                    textViewWork.setText("  कारण");
                    inputWorkProcess = (EditText) findViewById(R.id.processwork);
                    inputWorkProcess.setInputType(InputType.TYPE_CLASS_TEXT);
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
                        String PSname,GPName,Village,InsepectionDept,officeName,officeclean,totalemp,woinfoemp,absentemp
                                , listemp,slipFac,building,space,stay,descriptn;
                        btnRegister.setEnabled(false);

                        PSname = inputPanchayatSamiti.getSelectedItem().toString();
                        GPName = inputGPName.getSelectedItem().toString();
                        Village =inputVillageName.getText().toString();
                        InsepectionDept =inputVisitDept.getSelectedItem().toString();
                        officeName = inputOfficeName.getText().toString();
                        officeclean= inputOfficeClean.getSelectedItem().toString();
                        totalemp = inputTotalEmp.getText().toString();
                        woinfoemp = inputWithOutinfoEmp.getText().toString();
                        absentemp = inputAcsentEmp.getText().toString();
                        listemp =inputListEmp.getSelectedItem().toString();
                        slipFac =inputSlip.getSelectedItem().toString();
                        space = "NA";
                        stay= "NA";
                        if ( inputOfficeClean.getSelectedItem().toString().equals("कार्य प्रगतिरत") ||  inputOfficeClean.getSelectedItem().toString().equals("अपूर्ण एव रुका हुआ")  ){
                            building = inputWorkProcess.getText().toString();
                        }else {
                            building= "NA";
                        }
                        descriptn = inputdiscription.getText().toString();
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
                        insertToDatabase(PSname,GPName,Village,InsepectionDept,officeName,officeclean,totalemp,woinfoemp,absentemp
                                ,listemp,slipFac,building,space,stay,descriptn,LoginMobile,FilePath,lati,longi,accur);
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
    }

    private void fillarray() {

        pslist.clear();
        pslist.add("--चयन करे--");
        pslist.add("करौली");pslist.add("हिन्डोन");
        pslist.add("सपोटरा");pslist.add("टोडाभीम");pslist.add("मंडरायल");pslist.add("नादौती");

        select.clear();
        select.add("--चयन करे--");

        kar.clear();
        kar.add("--चयन करे--");
        kar.add("करौली शहर");kar.add("अतेवा");kar.add("कंचनपुर");kar.add("करसाई");
        kar.add("काशीपुरा");kar.add("कैलादेवी");kar.add("कोंडर");kar.add("कोटा");
        kar.add("कोटा छावर");kar.add("खुंडा");kar.add("खुवनगर");kar.add("खेडिया");
        kar.add("खोहरी");kar.add("गुडला");kar.add("गुनसेरा");kar.add("गुवरेडा");
        kar.add("गुरेई");kar.add("चैनपुर");kar.add("जमूरा");kar.add("जहॉंगीरपुर");
        kar.add("डाण्डा");kar.add("डुकाबली");kar.add("तुलसीपुरा");
        kar.add("नारायणा");kar.add("परीता");kar.add("पिपरानी");kar.add("फतेहपुर");
        kar.add("बीजलपुर");kar.add("भावली ");kar.add("महोली");kar.add("मामचारी");
        kar.add("मासलपुर");kar.add("मॉची");kar.add("रघुवंशी");kar.add("रतियापुरा");
        kar.add("राजौर");kar.add("रामपुर");kar.add("रूंधपुरा");kar.add("रौंड कला");
        kar.add("लोहर्रा");kar.add("ससेड़ी");kar.add("सायपुर");kar.add("सीलौती");kar.add("सेंमरदा");
        kar.add("सैगरपुरा");kar.add("हरनगर");





        hind.clear();
        hind.add("--चयन करे--");
        hind.add("हिंडोन शहर");hind.add("अकबरपुर");hind.add("अलीपुरा");hind.add("इरनिया");hind.add("कटकड़");
        hind.add("करसोली");hind.add("काचरौली");hind.add("कोटरी");hind.add("क्यारदा खुर्द");
        hind.add("खरेटा");hind.add("खेड़ली गुजर");hind.add("खेड़ा");hind.add("खेड़ी हैवत");hind.add("गांवड़ा मीना");
        hind.add("गांवडी मीणा");hind.add("गुनसार");hind.add("घोंसला");hind.add("चांदनगांव");hind.add("चिनायटा");
        hind.add("जगर");hind.add("जटनगला");hind.add("जटवाड़ा");hind.add("झारेड़ा");hind.add("टोडूपुरा");
        hind.add("ढहरा");hind.add("ढिंढोरा");hind.add("दानालपुर");hind.add("नंगला मीणा");hind.add("पटोंदा");
        hind.add("पालनपुर");hind.add("पाली");hind.add("फुलवाड़ा");hind.add("बझेंड़ा");hind.add("बरगमा");
        hind.add("बाईजट़");hind.add("बाजना कला");hind.add("भंगों");hind.add("भुकरावली");
        hind.add("मण्डावरा");hind.add("महु इब्राहिमपुर");hind.add("महू खास");hind.add("मोठियापुरा");
        hind.add("रेवई");hind.add("लहचोड़ा");hind.add("वनकी");hind.add("विजयपुरा");
        hind.add("शेरपुर");hind.add("श्री महावीर जी");hind.add("सनेट");hind.add("सिकरौदा मीना");
        hind.add("सूरोठ");hind.add("सोमली");hind.add("हुक्मीखेडा");

        spt.clear();
        spt.add("--चयन करे--");
        spt.add("अमरगढ");spt.add("अमरवाड़");spt.add("इनायती");spt.add("एकट");spt.add("औड़च");
        spt.add("काचरौदा");spt.add("कालागुढा");spt.add("कुड़गाव");spt.add("खेडला");
        spt.add("गज्जूपुरा");spt.add("चौड़ागांव");spt.add("जाखौदा");spt.add("जीरौता");spt.add("जोड़ली");
        spt.add("डाबरा");spt.add("डीकोली कला");spt.add("दौलतपुरा");spt.add("नारौली");
        spt.add("निभेरा");spt.add("बकना");spt.add("बगीदा");spt.add("बड़ौदा");spt.add("बाजना");spt.add("बापौती");
        spt.add("बालौती");spt.add("भरतून");spt.add("महमदपुर");spt.add("लेदिया");
        spt.add("लुलोज");spt.add("सपोटरा");spt.add("सलेमपुर");spt.add("हरिया का मंदिर");spt.add("हाड़ौती");

        tdm.clear();
        tdm.add("--चयन करे--");
        tdm.add("टोडाभीम शहर");tdm.add("अजीजपुर");tdm.add("उद्रेन");tdm.add("कंजौली");
        tdm.add("कटारा अजीज");tdm.add("कमालपुरा");tdm.add("करीरी");tdm.add("किरवाड़ा");
        tdm.add("कुढावल");tdm.add("खेड़ी");tdm.add("खोहरा");tdm.add("गोरडा");
        tdm.add("जगदीशपुरा");tdm.add("जोैल");tdm.add("झाड़ीसा");tdm.add("तिघरिया");
        tdm.add("देवलेन");tdm.add("धवान");tdm.add("नांगल शेरपुर");tdm.add("नागल माडल");
        tdm.add("निसूरा");tdm.add("पदमपुरा");tdm.add("पहाड़ी");tdm.add("पाडला खालसा");
        tdm.add("बालघाट");tdm.add("बौल");tdm.add("भजेड़ा");tdm.add("भण्डारी अन्दरूनी");
        tdm.add("भनकपुरा");tdm.add("भोपुर");tdm.add("मडेरू");tdm.add("महमदपुर");
        tdm.add("महस्वा");tdm.add("माचड़ी");tdm.add("मातासूला");tdm.add("मानोज");
        tdm.add("मूण्डिया");tdm.add("मोरड़ा");tdm.add("रानौली");tdm.add("लपावली");
        tdm.add("शहराकर");tdm.add("शेखपुरा");tdm.add("साकरवाडा");tdm.add("सिंघानिया");


        ndt.clear();
        ndt.add("--चयन करे--");
        ndt.add("कुंजेला");ndt.add("कैमरी");ndt.add("कैमला");ndt.add("कैमा");
        ndt.add("गढखेड़ा");ndt.add("गदमोरा");ndt.add("गुढाचन्द्रजी");ndt.add("चिरावडा");
        ndt.add("जीतकीपुर");ndt.add("ढहरिया");ndt.add("तालचिड़ा");ndt.add("तिमाबा");
        ndt.add("तेसगाव");ndt.add("दलपुरा");ndt.add("धोैलेता");
        ndt.add("नादौती");ndt.add("पाल");ndt.add("बड़ागाव");ndt.add("बरदाला");
        ndt.add("बागौर");ndt.add("बाड़ाराजपुर");ndt.add("भीलापाडा");ndt.add("मेदेकपुरा");
        ndt.add("राजाहेड़ा");ndt.add("रायसना");ndt.add("रौसी");ndt.add("शहर");
        ndt.add("सलावद");ndt.add("सोप");


        mnd.clear();
        mnd.add("--चयन करे--");
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
        vibhag.add("--चयन करे--");
        vibhag.add("सांख्यिकी विभाग");
        vibhag.add("जिला परिषद");
        vibhag.add("पंचायत समिति");
        vibhag.add("चिकित्सा विभाग");
        vibhag.add("कृषि");
        vibhag.add("पशुपालन");
        vibhag.add("महिला एवं बाल विकास");
        vibhag.add("प्राथमिक शिक्षा");
        vibhag.add("माध्यमिक शिक्षा");
        vibhag.add("रसद");
        vibhag.add("सिंचाई विभाग ");
        vibhag.add("MJSA");
        vibhag.add("श्रम विभाग");
        vibhag.add("कौशल विकास");
        vibhag.add("खनिज");
        vibhag.add("पी एच ई डी");
        vibhag.add("P W D विभाग");
        vibhag.add("समाज कल्याण");
        vibhag.add("कोष / उपकोष");
        vibhag.add("नगर परिषद");
        vibhag.add("अन्य विभाग");

        officeclean.clear();
        officeclean.add("--चयन करे--");
        officeclean.add("कार्य प्रारंभ");
        officeclean.add("कार्य प्रगतिरत");
        officeclean.add("कार्य पूर्ण");
        officeclean.add("अपूर्ण एव रुका हुआ");

        emplist.clear();
        emplist.add("--चयन करे--");
        emplist.add("हाँ");
        emplist.add("नही");

        slip.clear();
        slip.add("--चयन करे--");
        slip.add("हाँ");
        slip.add("नहीं");

        avility.clear();
        avility.add("--चयन करे--");
        avility.add("सरकारी");
        avility.add("किराये पर");

        space.clear();
        space.add("--चयन करे--");
        space.add("पर्याप्त");
        space.add("अपर्याप्त");

        spaceelem.clear();
        spaceelem.add("--चयन करे--");
        spaceelem.add("पर्याप्त उपलब्ध");
        spaceelem.add("खेल मैदान आवश्यक");
        spaceelem.add("कमरों की आवश्यकता");



        stay.clear();
        stay.add("--चयन करे--");
        stay.add("हाँ");
        stay.add("नही");
    }
    public void addListenerOnButton() {

    }


    private void insertToDatabase(String PSname,String GPName,String Village,String InsepectionDept,String oName,String oclean,String totalemp,String woinfoemp,String absentemp
            ,String listemp,String slipFac,String building,String ospace,String ostay,String descriptn,String Mob,String FPath,String lati,String longi,String accur){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramPSName = params[0];
                String paramGPName = params[1];
                String paramVillage = params[2];
                String paramInsepectionDept = params[3];
                String paramofficeName = params[4];
                String paramofficeclean = params[5];
                String paramtotalemp = params[6];
                String paramwoinfoemp = params[7];
                String paramabsentemp = params[8];
                String paramlistemp = params[9];
                String paramslipFac = params[10];
                String parambuilding = params[11];
                String paramspace = params[12];
                String paramstay = params[13];
                String paramdescriptn = params[14];
                String paramLoginMobile = params[15];
                String paramFilePath = params[16];
                String paramLati = params[17];
                String paramLongi = params[18];
                String paramAccur = params[19];

                UserFunctions userFunction = new UserFunctions();
                String json = userFunction.registerOfficedata(paramPSName,paramGPName,paramVillage,paramInsepectionDept,paramofficeName,paramofficeclean,paramtotalemp,paramwoinfoemp,paramabsentemp
                        ,paramlistemp,paramslipFac,parambuilding,paramspace,paramstay,paramdescriptn,paramLoginMobile,paramFilePath,paramLati,paramLongi,paramAccur);

                return json;
            }

            @Override
            protected void onPostExecute(String result) {
                showAlert(result);
                super.onPostExecute(result);
            }
            private void showAlert(String message) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Work.this);
                builder.setMessage(message).setTitle("Response from Servers")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent upanel = new Intent(Work.this, Work.class);
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
        sendPostReqAsyncTask.execute(PSname,GPName,Village,InsepectionDept,oName,oclean,totalemp,woinfoemp,absentemp,listemp,slipFac,building,ospace,ostay,descriptn,Mob,FPath,lati,longi,accur);
    }

}
