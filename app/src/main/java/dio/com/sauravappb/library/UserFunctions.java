package dio.com.sauravappb.library;


import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;


import dio.com.sauravappb.AndroidMultiPartEntity;


public class UserFunctions {
    long totalSize = 0;
    private JSONParser jsonParser;


    private static String loginURL    = "http://karauli.topblogwriter.com/nirikshan/";
    private static String registerURL = "http://karauli.topblogwriter.com/nirikshan/";
    private static String forpassURL = "http://karauli.topblogwriter.com/nirikshan/";
    private static String chgpassURL = "http://karauli.topblogwriter.com/nirikshan/";
    private static String registerSBMURL = "http://karauli.topblogwriter.com/nirikshan/fileUpload.php";
    private static String registerMDMURL = "http://karauli.topblogwriter.com/nirikshan/fileUploadMDM.php";
    private static String registerOfficeURL = "http://karauli.topblogwriter.com/nirikshan/fileUploadOffice.php";
    private static String registerWorkURL = "http://karauli.topblogwriter.com/nirikshan/fileUploadWork.php";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String register_sbm = "registerSBM";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";


    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }




    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }

    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }

     /**
      * Function to  Register
      **/
     @SuppressWarnings("deprecation")
    public String registerUser(String PSName, String GPName, String Village, String Inspect, String offname ,String mob,String type,String Situation, String mfood,String mclean, String mkitchen ,String mgrade){
        // Building Parameters
        String status = "";
        String tagfinal = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("PSName", PSName));
        params.add(new BasicNameValuePair("GPName", GPName));
        params.add(new BasicNameValuePair("VillageName", Village));
        params.add(new BasicNameValuePair("InspectDept", Inspect));
        params.add(new BasicNameValuePair("OffName", offname));
        params.add(new BasicNameValuePair("Mob", mob));
        params.add(new BasicNameValuePair("Wtype", type));
        params.add(new BasicNameValuePair("WSituation", Situation));
        params.add(new BasicNameValuePair("mfood", mfood));
        params.add(new BasicNameValuePair("mclean", mclean));
        params.add(new BasicNameValuePair("mkitchen", mkitchen));
        params.add(new BasicNameValuePair("mgrade", mgrade));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        try {
            status = json.getString("success");
            tagfinal = json.getString("tag");
        }
            catch(JSONException ex) {
            ex.printStackTrace();
        }
        if (status.equals("1")&&(tagfinal.equals("register"))){
            return "Success";
        }else {
            return "Failed";
        }
    }
    public String registerMdmdata(String PSName, String GPName, String Village, String Inspect, String offname ,String mob,String type,String Situation, String mfood,String mclean, String mkitchen ,String mgrade ,String filepath,String lat,String longi,String accur){
        String responseString= "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("PSName", PSName));
        params.add(new BasicNameValuePair("GPName", GPName));
        params.add(new BasicNameValuePair("VillageName", Village));
        params.add(new BasicNameValuePair("InspectDept", Inspect));
        params.add(new BasicNameValuePair("OffName", offname));
        params.add(new BasicNameValuePair("Mob", mob));
        params.add(new BasicNameValuePair("Wtype", type));
        params.add(new BasicNameValuePair("WSituation", Situation));
        params.add(new BasicNameValuePair("mfood", mfood));
        params.add(new BasicNameValuePair("mclean", mclean));
        params.add(new BasicNameValuePair("mkitchen", mkitchen));
        params.add(new BasicNameValuePair("mgrade", mgrade));
        params.add(new BasicNameValuePair("filep", filepath));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("longi", longi));
        params.add(new BasicNameValuePair("accur", accur));

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(registerMDMURL);

        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            File sourceFile = new File(filepath);
            Charset chars = Charset.forName("UTF-8");
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));

            // Extra parameters if you want to pass to server
            entity.addPart("psname", new StringBody(PSName,chars));
            entity.addPart("gpname", new StringBody(GPName,chars));
            entity.addPart("VillageName", new StringBody(Village,chars));
            entity.addPart("InspectDept", new StringBody(Inspect,chars));
            entity.addPart("OffName", new StringBody(offname,chars));
            entity.addPart("Mob", new StringBody(mob,chars));
            entity.addPart("Wtype", new StringBody(type,chars));

            entity.addPart("Situation", new StringBody(Situation,chars));
            entity.addPart("mfood", new StringBody(mfood,chars));
            entity.addPart("mclean", new StringBody(mclean,chars));
            entity.addPart("mkitchen", new StringBody(mkitchen,chars));
            entity.addPart("mgrade", new StringBody(mgrade,chars));
            entity.addPart("lat", new StringBody(lat,chars));
            entity.addPart("longi", new StringBody(longi,chars));
            entity.addPart("accur", new StringBody(accur,chars));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;

    }

    public String registerSbmdata(String PSName, String GPName, String Village, String Inspect, String offname ,String mob,String type,String filep,String lat, String longi, String accur ){
        // Building Parameters
        String status = "";
        String tagfinal = "";
        String responseString= "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("PSName", PSName));
        params.add(new BasicNameValuePair("GPName", GPName));
        params.add(new BasicNameValuePair("VillageName", Village));
        params.add(new BasicNameValuePair("InspectDept", Inspect));
        params.add(new BasicNameValuePair("OffName", offname));
        params.add(new BasicNameValuePair("Mob", mob));
        params.add(new BasicNameValuePair("Wtype", type));
        params.add(new BasicNameValuePair("WSituation", filep));
        params.add(new BasicNameValuePair("Lat", lat));
        params.add(new BasicNameValuePair("Longi", longi));
        params.add(new BasicNameValuePair("Accur", accur));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(registerSBMURL);

        try {
//            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                    new AndroidMultiPartEntity.ProgressListener() {
//                        @Override
//                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
//                        }
//                    });
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            File sourceFile = new File(filep);
            Charset chars = Charset.forName("UTF-8");
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));

            // Extra parameters if you want to pass to server
            entity.addPart("psname", new StringBody(PSName,chars));
            entity.addPart("gpname", new StringBody(GPName,chars));
            entity.addPart("vname", new StringBody(Village,chars));
            entity.addPart("activity", new StringBody(Inspect,chars));
            entity.addPart("cleaning", new StringBody(offname,chars));
            entity.addPart("mob", new StringBody(mob));
            entity.addPart("anayvivran", new StringBody(type,chars));
            entity.addPart("lat", new StringBody(lat));
            entity.addPart("longi", new StringBody(longi));
            entity.addPart("accur", new StringBody(accur));


            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;

    }

    public String registerOfficedata(String... params){
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
        String responseString= "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(registerWorkURL);
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            File sourceFile = new File(paramFilePath);
            Charset chars = Charset.forName("UTF-8");
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));

            // Extra parameters if you want to pass to server
            entity.addPart("psname", new StringBody(paramPSName,chars));
            entity.addPart("gpname", new StringBody(paramGPName,chars));
            entity.addPart("vname", new StringBody(paramVillage,chars));
            entity.addPart("inspect", new StringBody(paramInsepectionDept,chars));
            entity.addPart("oname", new StringBody(paramofficeName,chars));
            entity.addPart("oclean", new StringBody(paramofficeclean,chars));
            entity.addPart("totalemp", new StringBody(paramtotalemp,chars));
            entity.addPart("woinfo", new StringBody(paramwoinfoemp,chars));
            entity.addPart("absent", new StringBody(paramabsentemp,chars));
            entity.addPart("listemp", new StringBody(paramlistemp,chars));
            entity.addPart("slipFac", new StringBody(paramslipFac,chars));
            entity.addPart("build", new StringBody(parambuilding,chars));
            entity.addPart("space", new StringBody(paramspace,chars));
            entity.addPart("stay", new StringBody(paramstay,chars));
            entity.addPart("descriptn", new StringBody(paramdescriptn,chars));
            entity.addPart("mob", new StringBody(paramLoginMobile,chars));
            entity.addPart("lati", new StringBody(paramLati,chars));
            entity.addPart("longi", new StringBody(paramLongi,chars));
            entity.addPart("accur", new StringBody(paramAccur,chars));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;
    }


    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

