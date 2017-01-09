package namtran.jsoup;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by NamTran on 1/9/2017.
 */

public class ParseToiec extends AppCompatActivity implements View.OnClickListener {

    TextView txtmeta;
    Button btnGetData,btnexel;
    List<Toiec> listToiec;

    List<String> listURL = LuyenThiURL.listURLDangBaiDienTuVaoCau();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtmeta = (TextView) findViewById(R.id.txtmeta);
        btnGetData = (Button) findViewById(R.id.btnGetData);
        btnexel = (Button) findViewById(R.id.btnexel);
        btnGetData.setOnClickListener(this);
        btnexel.setOnClickListener(this);
        listToiec = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetData){
            txtmeta.setText("" + listToiec.size());
            for (int i =0; i<listURL.size();i++){

            }
        }else if (v.getId() == R.id.btnexel){
            txtmeta.setText("please wait");
            xuatFileExelMonAnhVan("ToiecWord");
        }
    }

    private void xuatFileExelMonAnhVan(String filename){
        String Fnamexls = filename + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Download");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);

            try {
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("ListWordToiec", 0);
                int arraySize = listToiec.size();
                Label[][] headerArray = new Label[1][8];
                Label[][] rowsArray = new Label[arraySize + 1][8];
                headerArray[0][0] = new Label(0, 0, "Vocabulary");
                headerArray[0][1] = new Label(1, 0, "Transliteration");
                headerArray[0][2] = new Label(2, 0, "EnglishMean");
                headerArray[0][3] = new Label(3, 0, "VietnamMean");
                headerArray[0][4] = new Label(4, 0, "FromCategory");
                headerArray[0][5] = new Label(5, 0, "EnglishExample");
                headerArray[0][6] = new Label(6, 0, "VietnamExample");
                headerArray[0][7] = new Label(7, 0, "Audio");
                Toiec toiec = new Toiec();
                for (int i = 0; i < arraySize; i++){
                    if (i < arraySize) {
                        toiec = listToiec.get(i);
                    }
                    int j = i + 1;
                    rowsArray[j][0] = new Label(0, j,toiec.getVocabulary());
                    rowsArray[j][1] = new Label(1, j,toiec.getTransliteration());
                    rowsArray[j][2] = new Label(2, j,toiec.getEnglishMean());
                    rowsArray[j][3] = new Label(3, j,toiec.getVietnamMean());
                    rowsArray[j][4] = new Label(4, j,toiec.getFromCategory());
                    rowsArray[j][5] = new Label(5, j,toiec.getEnglishExample());
                    rowsArray[j][6] = new Label(6, j,toiec.getVietnamExample());
                    rowsArray[j][7] = new Label(7, j,toiec.getAudio());
                }

                // Add header cells
                for (int i = 0; i < 8; i++) {
                    sheet.addCell(headerArray[0][i]);
                }

                // Add data cells
                for (int i = 1; i <= arraySize; i++) {
                    for (int j = 0; j < 8; j++) {
                        sheet.addCell(rowsArray[i][j]);
                    }
                }

                // create workbook
                workbook.write();

                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

                // Open mail
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "ListToiec");
                if (file != null) {
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); // attach
                }
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                txtmeta.setText("OK");

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ParseToiecTask extends AsyncTask<String, Void, Elements> {

        Document doc;
        Elements words;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtmeta.setText("Please wait...");
        }

        protected Elements doInBackground(String... params) {
            String url = params[0];
            try {
                doc = Jsoup.connect(url).get();
                words = doc.body().getElementsByClass("tuvung");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return words;
        }

        protected void onPostExecute(Elements result) {
            super.onPostExecute(result);
            Log.d("Word",result.toString());
        }
    }
}
