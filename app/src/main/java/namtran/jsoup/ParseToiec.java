package namtran.jsoup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NamTran on 1/9/2017.
 */

public class ParseToiec extends AppCompatActivity implements View.OnClickListener {

    private static final String URL600 = "http://600tuvungtoeic.com/images/";

    TextView txtmeta;
    Button btnGetData,btnexel;
    List<Toiec> listToiec;

    List<UrlToiec> listURL = ToiecURL.listURLToiec();
    StringBuilder builder;
    int paper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_toiec);

        txtmeta = (TextView) findViewById(R.id.txtmeta);
        btnGetData = (Button) findViewById(R.id.btnGetData);
        btnexel = (Button) findViewById(R.id.btnexel);
        btnGetData.setOnClickListener(this);
        btnexel.setOnClickListener(this);
        listToiec = new ArrayList<>();
        builder = new StringBuilder();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetData){
            txtmeta.setText("" + listToiec.size());
            for (int i =0; i<listURL.size();i++){
                new ParseToiecTask(i).execute(listURL.get(i));
            }
        }else if (v.getId() == R.id.btnexel){

        }
    }



    class ParseToiecTask extends AsyncTask<UrlToiec, Void, List<Toiec>> {

        Document doc;
        Elements words;

        int paper;

        public ParseToiecTask(int paper) {
            this.paper = paper;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtmeta.setText("Please wait...");
        }

        protected List<Toiec> doInBackground(UrlToiec... params) {
            List<Toiec> toiecs = new ArrayList<>();
            String url = params[0].getUrl();
            try {
                doc = Jsoup.connect(url).get();
                words = doc.body().getElementsByClass("tuvung");
                for (Element element : words){
                    Toiec toiec = new Toiec();
                    toiec.setEnglishtopic(params[0].getTitleEng());
                    toiec.setVietnamTopic(params[0].getTitleVi());
                    toiec.setImageTopic(params[0].getImageTitle());
                    Document document = Jsoup.parse(element.html());
                    String vocabulary = document.body().getElementsByClass("hinhanh").get(0).html().split("\" src=\"")[0].split("title=\"")[1];
                    toiec.setVocabulary(vocabulary);
                    Log.d("Paper","paper : " + paper + "\n" + "word :" + vocabulary);

                    if (vocabulary.equals("da") || vocabulary.equals("adsa") || vocabulary.equals("dsadsad"))
                        break;

                    String image = builder.append(URL600).append(vocabulary.replaceAll(" ","_").toLowerCase()).toString();
                    toiec.setImg(image);

                    String noidung = document.body().getElementsByClass("noidung").html();
                    String noidunggiaithich = noidung.split("<span class=\"bold\">Giải thích: </span>")[1];

                    String giaithich = noidunggiaithich.split("<span class=\"bold\">Từ loại: </span>")[0];
                    toiec.setEnglishMean(giaithich);

                    String noidungtuloai = noidunggiaithich.split("<span class=\"bold\">Từ loại: </span>")[1];

                    String tuloai = noidungtuloai.split("<span class=\"bold\">Ví dụ: </span>")[0];
                    toiec.setVietnamMean(tuloai);

                    String vidu = noidungtuloai.split("<span class=\"bold\">Ví dụ: </span>")[1].split("<audio controls>")[0];
                    if (vidu.contains("<hr>")){
                        toiec.setEnglishExample(vidu.split("<hr>")[0]);
                        toiec.setVietnamExample(vidu.split("<hr>")[1]);
                    }else if (vidu.contains("<b>")){
                        toiec.setEnglishExample(vidu.split("<b>")[0]);
                        toiec.setVietnamExample(vidu.split("<b>")[1]);
                    }

                    toiecs.add(toiec);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return toiecs;
        }

        protected void onPostExecute(List<Toiec> result) {
            super.onPostExecute(result);
            if (result.size() > 0){
                listToiec.addAll(result);
                txtmeta.setText("" + listToiec.size());
                for (Toiec toiec : result){
                    Log.d("Toiec",toiec.toString());
                }
            }
        }
    }
}
