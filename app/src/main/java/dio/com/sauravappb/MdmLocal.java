package dio.com.sauravappb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import dio.com.sauravappb.Mdm;
import dio.com.sauravappb.library.UserFunctions;

public class MdmLocal {

    public String status = "Data Not Insert";

    public void insertServerDB(String PSname, String GPName,String  Village,String  InsepectionDept,String  Oname,String  LoginMobile,String  Wtype,String  WSituation,String  Mfood,String  Mclean,String  Mkitchen,String  Mgrade,String  FilePath,String  lati,String  longi,String  accur){

        insertToDatabase(PSname,GPName,Village,InsepectionDept,Oname,LoginMobile,Wtype,WSituation,Mfood,Mclean,Mkitchen,Mgrade, FilePath,lati,longi,accur);

    }
    private void insertToDatabase(String name, String add,String village ,String inspectdep ,String ofname,String mob,String typeW ,String SituW, String Mfood ,String Mclean, String Mkitchen,String Mgrade, String FilePath,String lat,String longi,String accur){
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
                String paramFood = params[8];
                String paramClean = params[9];
                String paramKitchen = params[10];
                String paramGrade = params[11];
                String paramFilePath = params[12];
                String paramLat = params[13];
                String paramLongi = params[14];
                String paramAccur = params[15];

                UserFunctions userFunction = new UserFunctions();
                String json = userFunction.registerMdmdata(paramPSName, paramGPName, paramVillage, paramInsepectionDept, paramOname,paramLoginMobile,paramType,paramSituation,paramFood,paramClean,paramKitchen,paramGrade,paramFilePath,paramLat,paramLongi,paramAccur);
                return json;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, add ,village,inspectdep,ofname,mob,typeW,SituW,Mfood,Mclean,Mkitchen,Mgrade,FilePath,lat,longi,accur);
    }
}
