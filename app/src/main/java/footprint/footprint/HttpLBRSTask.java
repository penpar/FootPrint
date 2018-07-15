package footprint.footprint;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;


public class HttpLBRSTask extends AsyncTask<String, String, LinkedList<LBRS>> {

    /**
     *
     * 추천 마커 받아오기 완성됨
     */

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LinkedList<LBRS> doInBackground(String... values) {
        // TODO Auto-generated method stub
        Log.d("LBRS", "THREAD START");
        LinkedList<LBRS> receiveData = new LinkedList<>();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        InputStream inputStream;
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
        HttpPost httpPost = new HttpPost("http://52.79.139.48/han/distance.php");

        try {

            Log.d("LBRS", "SETMODE PASS");
            builder.addTextBody("latitude", values[0], ContentType.create("Multipart/related", "utf8"));
            builder.addTextBody("longitude", values[1], ContentType.create("Multipart/related", "utf8"));
            builder.addTextBody("radian", values[2], ContentType.create("Multipart/related", "utf8"));
            builder.addTextBody("minRcmd", values[3], ContentType.create("Multipart/related", "utf8"));
            builder.addTextBody("timeSpectrum", values[4], ContentType.create("Multipart/related", "utf8"));
            builder.addTextBody("minTime", values[5], ContentType.create("Multipart/related", "utf8"));
            builder.addTextBody("maxTime", values[6], ContentType.create("Multipart/related", "utf8"));


            Log.d("LBRS", "ADDTEXT PASS");

            Log.d("LBRS", "HTTPPOST PASS");

            httpPost.setEntity(builder.build());

            Log.d("LBRS", "SETENTITY PASS");

            HttpResponse httpResponse = httpClient.execute(httpPost);

            Log.d("LBRS", "HTTPRESPONSE PASS");

            HttpEntity httpEntity = httpResponse.getEntity();

            Log.d("LBRS", "HTTPENTITY PASS");

            inputStream = httpEntity.getContent();

            Log.d("LBRS", "INPUTSTREAM PASS");

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf8"));

            int i = 0, j = 0, k = 0, l = 0;
            int id;
            double lati;
            double longi;
            int cnt;

            String line;


            if(!(line = bufferedReader.readLine()).equals("END")) {

                while(true) {
                    Log.d("BUFFERLINE", line);
                    id = Integer.parseInt(line.substring(0, (i = line.indexOf(";"))));
                    lati = Double.parseDouble(line.substring(i + 1, (j = line.indexOf(";", i + 1))));
                    longi = Double.parseDouble(line.substring(j + 1, (k = line.indexOf(";", j + 1))));
                    cnt = Integer.parseInt(line.substring(k + 1, (l = line.indexOf(";", k + 1))));


                    Log.d("BUFFERLINE", id + " " + lati + " " + longi);

                    receiveData.add(new LBRS(id, lati, longi, cnt));
                    String subString = line;

                    line = subString.substring(l + 1);
                    if(line.indexOf("END",0) == 0) {
                        break;
                    }
                    Log.d("BUFFERLINE", line);

                }

                //Log.d("BUFFER", line);


            }


            Log.d("LBRS", "SUCCESS_FUNCTION");
            return receiveData;

            //Log.d("LBRS", stringBuilder.toString());


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

        Log.d("LBRS", "FAIL_FUNCTION");
        return null;

    }


    @Override
    protected void onPostExecute(LinkedList<LBRS> result) {
        super.onPostExecute(result);

    }



}
