package namtran.jsoup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

    private static final String URL600IMAGE = "http://600tuvungtoeic.com/images/";
    private static final String URL600MP3 = "http://600tuvungtoeic.com/audio/";
    private static final String URLCAMBRIDGE = "http://dictionary.cambridge.org/dictionary/english/";

    TextView txtmeta;
    Button btnGetData, btnUpdateData;
    List<Toiec> listToiec;
    List<Toiec> listToiec2;

    List<String> listVocabularyDontParse;

    List<UrlToiec> listURL = ToiecURL.listURLToiec();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_toiec);

        txtmeta = (TextView) findViewById(R.id.txtmeta);
        btnGetData = (Button) findViewById(R.id.btnGetData);
        btnUpdateData = (Button) findViewById(R.id.btnUpdateData);
        btnGetData.setOnClickListener(this);
        btnUpdateData.setOnClickListener(this);
        listToiec = new ArrayList<>();
        listToiec2 = new ArrayList<>();
        listVocabularyDontParse = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetData) {
            btnGetData.setEnabled(false);
            txtmeta.setText("" + listToiec.size());
            for (int i = 0; i < listURL.size(); i++) {
                new ParseToiecTask(i).execute(listURL.get(i));
            }
        } else if (v.getId() == R.id.btnUpdateData) {
            btnUpdateData.setEnabled(false);
            if (listToiec.size() > 0) {
                for (int i = 0; i < listToiec.size(); i++) {
                    new ParseToiecTaskII(i).execute(listToiec.get(i));
                }
            }
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
                for (Element element : words) {
                    Toiec toiec = new Toiec();
                    toiec.setEnglishtopic(params[0].getTitleEng());
                    toiec.setVietnamTopic(params[0].getTitleVi());
                    toiec.setImageTopic(params[0].getImageTitle());
                    Document document = Jsoup.parse(element.html());
                    String vocabulary = document.body().getElementsByClass("hinhanh").get(0).html().split("\" src=\"")[0].split("title=\"")[1];
                    toiec.setVocabulary(vocabulary);
                    Log.d("Paper", "paper : " + paper + "\n" + "word :" + vocabulary);

                    if (vocabulary.equals("da") || vocabulary.equals("adsa") || vocabulary.equals("dsadsad"))
                        break;

                    String image = URL600IMAGE + vocabulary.replaceAll(" ", "_").toLowerCase();
                    toiec.setImg(image);

                    String noidung = document.body().getElementsByClass("noidung").html();

                    if (vocabulary.contains(" ") || vocabulary.equals("delicately")|| vocabulary.equals("flexibly")
                            || vocabulary.equals("sufficiently")|| vocabulary.equals("cautiously")|| vocabulary.equals("aggressively")
                            || vocabulary.equals("inconsiderately")|| vocabulary.equals("uniformly")|| vocabulary.equals("systematically")
                            || vocabulary.equals("daringly")|| vocabulary.equals("rely")|| vocabulary.equals("elegance")
                            || vocabulary.equals("embarkation")|| vocabulary.equals("refer")){
                        String mp3UK= "";
                        if (vocabulary.contains(" ")){
                            mp3UK = URL600MP3 + vocabulary.replace(" ","_") + ".mp3";
                        }else {
                            mp3UK = URL600MP3 + vocabulary + ".mp3";
                        }

                        String phienAmUK = noidung.split("<span style=\"color: red;\">")[1].split("</span>")[0];
                        toiec.setAudioUK(mp3UK);
                        toiec.setTransliterationUK(phienAmUK);
                    }

                    String noidunggiaithich = noidung.split("<span class=\"bold\">Giải thích: </span>")[1];

                    String giaithich = noidunggiaithich.split("<span class=\"bold\">Từ loại: </span>")[0];
                    toiec.setEnglishMean(giaithich);

                    String noidungtuloai = noidunggiaithich.split("<span class=\"bold\">Từ loại: </span>")[1];

                    String tuloai = noidungtuloai.split("<span class=\"bold\">Ví dụ: </span>")[0];

                    if (tuloai.contains("(adj):")){
                        String VietnamExample = tuloai.replace("(adj):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adj)");
                    }else if (tuloai.contains("(v):")){
                        String VietnamExample = tuloai.replace("(v):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(v)");
                    }else if (tuloai.contains("(n):")){
                        String VietnamExample = tuloai.replace("(n):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n)");
                    }else if (tuloai.contains("(v, n):")){
                        String VietnamExample = tuloai.replace("(v, n):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(v, n)");
                    }else if (tuloai.contains("(adv):")){
                        String VietnamExample = tuloai.replace("(adv):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adv)");
                    }else if (tuloai.contains("(adj, n):")){
                        String VietnamExample = tuloai.replace("(adj, n):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adj, n)");
                    }else if (tuloai.contains("(n,v):")){
                        String VietnamExample = tuloai.replace("(n,v):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n,v)");
                    }else if (tuloai.contains("(n, v):")){
                        String VietnamExample = tuloai.replace("(n, v):","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n,v)");
                    }else if (tuloai.contains("(v,n):")){
                        String VietnamExample = tuloai.replace("(v,n)","");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample.replace("<br>","");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n,v)");
                    }else {
                        Log.d("No FromCATEGORY",tuloai);
                    }

                    String vidu = noidungtuloai.split("<span class=\"bold\">Ví dụ: </span>")[1].split("<audio controls>")[0];
                    if (vidu.contains("<hr>")) {
                        toiec.setEnglishExample(vidu.split("<hr>")[0]);
                        toiec.setVietnamExample(vidu.split("<hr>")[1]);
                    } else if (vidu.contains("<b>")) {
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
            if (result.size() > 0) {
                if (paper == listURL.size() - 1)
                    btnUpdateData.setVisibility(View.VISIBLE);
                listToiec.addAll(result);
                txtmeta.setText("" + listToiec.size());
                for (Toiec toiec : result) {
                    Log.d("Toiec", toiec.toString());
                }
            }
        }
    }

    class ParseToiecTaskII extends AsyncTask<Toiec, Void, Toiec> {

        int word;

        public ParseToiecTaskII(int word) {
            this.word = word;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtmeta.setText("Please wait...");
        }

        @Override
        protected Toiec doInBackground(Toiec... params) {
            Toiec toiec = params[0];
            String vocabulary = toiec.getVocabulary();
            Element US = null;
            Element UK = null;
            String URL = "";
            if (toiec.getAudioUK() != null && !TextUtils.isEmpty(toiec.audioUK)){
                return toiec;
            }else {
                URL = URLCAMBRIDGE + vocabulary.toLowerCase();
                toiec = parseMP3(toiec, vocabulary, URL, US, UK, word);
            }
            return toiec;
        }

        @Override
        protected void onPostExecute(Toiec toiec) {
            super.onPostExecute(toiec);
            txtmeta.setText("" + (word + 1));
            if (toiec != null)
                listToiec2.add(toiec);
            if (word == listToiec.size() - 1) {
                for (Toiec toiec1 : listToiec)
                    Log.d("Toiec Cambridge", toiec1.toString());
            }
        }
    }

    private Toiec parseMP3(Toiec toiec, String vocabulary, String URL, Element US, Element UK, int word) {
        try {
            Document doc = Jsoup.connect(URL).get();
            if (doc.body().getElementsByClass("pron-info").size() == 1) {
                if (doc.body().getElementsByClass("pron-info").html().contains("class=\"us\"")) {
                    US = doc.body().getElementsByClass("pron-info").get(0);
                    String mp3US = US.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];

                    String phienAmUS = US.getElementsByClass("pron").html();
                    if (phienAmUS.contains("</span>"))
                        phienAmUS = phienAmUS.replace("</span>", "");
                    if (phienAmUS.contains(" class=\"ipa\""))
                        phienAmUS = phienAmUS.replace(" class=\"ipa\"", "");
                    if (phienAmUS.contains(" class=\"sp\""))
                        phienAmUS = phienAmUS.replace(" class=\"sp\"", "");

                    toiec.setAudioUS(mp3US);
                    toiec.setTransliterationUS(phienAmUS);
                } else if (doc.body().getElementsByClass("pron-info").html().contains("class=\"uk\"")) {
                    UK = doc.body().getElementsByClass("pron-info").get(0);
                    String mp3UK = UK.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];

                    String phienAmUK = UK.getElementsByClass("pron").html();
                    if (phienAmUK.contains("</span>"))
                        phienAmUK = phienAmUK.replace("</span>", "");
                    if (phienAmUK.contains(" class=\"ipa\""))
                        phienAmUK = phienAmUK.replace(" class=\"ipa\"", "");
                    if (phienAmUK.contains(" class=\"sp\""))
                        phienAmUK = phienAmUK.replace(" class=\"sp\"", "");

                    toiec.setAudioUK(mp3UK);
                    toiec.setTransliterationUK(phienAmUK);
                }
            } else if (doc.body().getElementsByClass("pron-info").size() > 1) {
                UK = doc.body().getElementsByClass("pron-info").get(0);
                if (doc.body().getElementsByClass("pron-info").get(1).html().contains(".mp3")) {
                    US = doc.body().getElementsByClass("pron-info").get(1);
                } else {
                    US = doc.body().getElementsByClass("pron-info").get(2);
                }

                String mp3UK = UK.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];
                String mp3US = US.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];

                String phienAmUK = UK.getElementsByClass("pron").html();
                if (phienAmUK.contains("</span>"))
                    phienAmUK = phienAmUK.replace("</span>", "");
                if (phienAmUK.contains(" class=\"ipa\""))
                    phienAmUK = phienAmUK.replace(" class=\"ipa\"", "");
                if (phienAmUK.contains(" class=\"sp\""))
                    phienAmUK = phienAmUK.replace(" class=\"sp\"", "");

                String phienAmUS = US.getElementsByClass("pron").html();
                if (phienAmUS.contains("</span>"))
                    phienAmUS = phienAmUS.replace("</span>", "");
                if (phienAmUS.contains(" class=\"ipa\""))
                    phienAmUS = phienAmUS.replace(" class=\"ipa\"", "");
                if (phienAmUS.contains(" class=\"sp\""))
                    phienAmUS = phienAmUS.replace(" class=\"sp\"", "");

                Log.d("CAMBRIDGE", "mp3UK : " + mp3UK + "\n" + "mp3US : " + mp3US + "\n" + "phienAmUK : " + phienAmUK + "\n" + "phienAmUS : " + phienAmUS);

                toiec.setAudioUK(mp3US);
                toiec.setAudioUS(mp3UK);
                toiec.setTransliterationUK(phienAmUK);
                toiec.setTransliterationUS(phienAmUS);
            } else {
                Log.d("Error", "number : " + word + 1 + "\n" + vocabulary);
                String URL2 = URLCAMBRIDGE + vocabulary.toLowerCase() + "?q=" + vocabulary.toLowerCase();
                Document doc2 = Jsoup.connect(URL2).get();
                if (doc2.body().getElementsByClass("pron-info").size() == 1) {
                    if (doc2.body().getElementsByClass("pron-info").html().contains("class=\"us\"")) {
                        US = doc.body().getElementsByClass("pron-info").get(0);
                        String mp3US = US.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];

                        String phienAmUS = US.getElementsByClass("pron").html();
                        if (phienAmUS.contains("</span>"))
                            phienAmUS = phienAmUS.replace("</span>", "");
                        if (phienAmUS.contains(" class=\"ipa\""))
                            phienAmUS = phienAmUS.replace(" class=\"ipa\"", "");
                        if (phienAmUS.contains(" class=\"sp\""))
                            phienAmUS = phienAmUS.replace(" class=\"sp\"", "");

                        toiec.setAudioUS(mp3US);
                        toiec.setTransliterationUS(phienAmUS);
                    } else if (doc2.body().getElementsByClass("pron-info").html().contains("class=\"uk\"")) {
                        UK = doc.body().getElementsByClass("pron-info").get(0);
                        String mp3UK = UK.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];

                        String phienAmUK = UK.getElementsByClass("pron").html();
                        if (phienAmUK.contains("</span>"))
                            phienAmUK = phienAmUK.replace("</span>", "");
                        if (phienAmUK.contains(" class=\"ipa\""))
                            phienAmUK = phienAmUK.replace(" class=\"ipa\"", "");
                        if (phienAmUK.contains(" class=\"sp\""))
                            phienAmUK = phienAmUK.replace(" class=\"sp\"", "");

                        toiec.setAudioUK(mp3UK);
                        toiec.setTransliterationUK(phienAmUK);
                    }
                } else if (doc2.body().getElementsByClass("pron-info").size() > 1) {
                    UK = doc.body().getElementsByClass("pron-info").get(0);
                    if (doc.body().getElementsByClass("pron-info").get(1).html().contains(".mp3")) {
                        US = doc.body().getElementsByClass("pron-info").get(1);
                    } else {
                        US = doc.body().getElementsByClass("pron-info").get(2);
                    }

                    String mp3UK = UK.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];
                    String mp3US = US.html().split("data-src-mp3=\"")[1].split("\" data-src-ogg")[0];

                    String phienAmUK = UK.getElementsByClass("pron").html();
                    if (phienAmUK.contains("</span>"))
                        phienAmUK = phienAmUK.replace("</span>", "");
                    if (phienAmUK.contains(" class=\"ipa\""))
                        phienAmUK = phienAmUK.replace(" class=\"ipa\"", "");
                    if (phienAmUK.contains(" class=\"sp\""))
                        phienAmUK = phienAmUK.replace(" class=\"sp\"", "");

                    String phienAmUS = US.getElementsByClass("pron").html();
                    if (phienAmUS.contains("</span>"))
                        phienAmUS = phienAmUS.replace("</span>", "");
                    if (phienAmUS.contains(" class=\"ipa\""))
                        phienAmUS = phienAmUS.replace(" class=\"ipa\"", "");
                    if (phienAmUS.contains(" class=\"sp\""))
                        phienAmUS = phienAmUS.replace(" class=\"sp\"", "");

                    Log.d("CAMBRIDGE", "mp3UK : " + mp3UK + "\n" + "mp3US : " + mp3US + "\n" + "phienAmUK : " + phienAmUK + "\n" + "phienAmUS : " + phienAmUS);

                    toiec.setAudioUK(mp3US);
                    toiec.setAudioUS(mp3UK);
                    toiec.setTransliterationUK(phienAmUK);
                    toiec.setTransliterationUS(phienAmUS);
                } else {
                    Log.d("Error", "number : " + (word + 1) + "\n" + vocabulary);
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            listVocabularyDontParse.add(vocabulary);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.d("Error", "number : " + (word + 1) + "\n" + vocabulary);
            listVocabularyDontParse.add(vocabulary);
        } catch (IndexOutOfBoundsException e){
            Log.d("Error", "number : " + (word + 1) + "\n" + vocabulary);
            listVocabularyDontParse.add(vocabulary);
        }
        return toiec;
    }
}
