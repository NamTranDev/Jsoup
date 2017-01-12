package namtran.jsoup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    private static final String URL600IMAGE = "http://600tuvungtoeic.com/images/";
    private static final String URL600MP3 = "http://600tuvungtoeic.com/audio/";
    private static final String URLCAMBRIDGE = "http://dictionary.cambridge.org/dictionary/english/";

    TextView txtmeta,txtcount;
    Button btnGetData, btnUpdateData, btnEncodeAudioAndImage, btnTest, btnJsonArray, btnExel,btnNextURL;
    ImageView imgTopic, imgWord;
    List<Toiec> listToiec;
    List<Toiec> listToiecAll;

    List<String> listVocabularyDontParse;

    List<UrlToiec> listURL = ToiecURL.listURLToiec();

    int count = 0;
    int countURL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_toiec);

        txtcount = (TextView) findViewById(R.id.count);
        txtmeta = (TextView) findViewById(R.id.txtmeta);
        btnGetData = (Button) findViewById(R.id.btnGetData);
        btnUpdateData = (Button) findViewById(R.id.btnUpdateData);
        btnEncodeAudioAndImage = (Button) findViewById(R.id.btnEncodeAudioAndImage);
        btnJsonArray = (Button) findViewById(R.id.btnJsonArray);
        btnExel = (Button) findViewById(R.id.btnExel);
        btnNextURL = (Button) findViewById(R.id.btnNextURL);
        imgTopic = (ImageView) findViewById(R.id.imgTopic);
        imgWord = (ImageView) findViewById(R.id.imgWord);
        btnTest = (Button) findViewById(R.id.btnTest);
        btnGetData.setOnClickListener(this);
        btnUpdateData.setOnClickListener(this);
        btnEncodeAudioAndImage.setOnClickListener(this);
        btnExel.setOnClickListener(this);
        btnJsonArray.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        listToiec = new ArrayList<>();
        listVocabularyDontParse = new ArrayList<>();
        listToiecAll = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetData) {
            btnGetData.setEnabled(false);
            txtmeta.setText("" + listToiec.size());
            new ParseToiecTask(countURL).execute(listURL.get(countURL));
        } else if (v.getId() == R.id.btnUpdateData) {
            btnUpdateData.setEnabled(false);
            if (listToiec.size() > 0) {
                for (int i = 0; i < listToiec.size(); i++) {
                    new ParseToiecTaskII(i).execute(listToiec.get(i));
                }
            }
        } else if (v.getId() == R.id.btnEncodeAudioAndImage) {
            btnEncodeAudioAndImage.setEnabled(false);
            if (listToiec.size() > 0) {
                for (int i = 0; i < listToiec.size(); i++) {
                    new DecodeBase64(i).execute(listToiec.get(i));
                }
            }
        } else if (v.getId() == R.id.btnTest) {
            if (listToiec.size() > 0) {
                if (count > listToiec.size() - 1)
                    count = 0;
                new DisplayImageAndAudio().execute(listToiec.get(count));
                count ++;
                txtcount.setText("" + count);
            }
        }else if (v.getId() == R.id.btnJsonArray) {
            if (listToiec.size() > 0) {
                String json = new Gson().toJson(listToiec);
                final Dialog dialog = new Dialog(ParseToiec.this);
                dialog.setContentView(R.layout.dialog_choose_list);
                dialog.setTitle("Choose List");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button btnGallery = (Button) dialog.findViewById(R.id.chat_firebase_dialog_btn_gallery);
                btnGallery.setText("listToiec" + listToiec.size());
                Button btnCamera = (Button) dialog.findViewById(R.id.chat_firebase_dialog_btn_camera);
                btnCamera.setText("listToiecAll" + listToiecAll.size());
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        btnJsonArray.setEnabled(false);
                    }
                });
                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        btnJsonArray.setEnabled(false);
                    }
                });
            }
        }else if (v.getId() == R.id.btnExel) {
            if (listToiec.size() > 0) {
                final Dialog dialog = new Dialog(ParseToiec.this);
                dialog.setContentView(R.layout.dialog_choose_list);
                dialog.setTitle("Choose List");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button btnGallery = (Button) dialog.findViewById(R.id.chat_firebase_dialog_btn_gallery);
                btnGallery.setText("listToiec" + listToiec.size());
                Button btnCamera = (Button) dialog.findViewById(R.id.chat_firebase_dialog_btn_camera);
                btnCamera.setText("listToiecAll" + listToiecAll.size());
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        btnExel.setEnabled(false);
                    }
                });
                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        btnExel.setEnabled(false);
                    }
                });
            }
        }else if (v.getId() == R.id.btnNextURL) {
            if (countURL < listURL.size() - 1){
                countURL ++;
                count = 0;

                listToiecAll.addAll(listToiec);
                listToiec.clear();
                btnNextURL.setVisibility(View.GONE);
                btnExel.setVisibility(View.GONE);
                btnJsonArray.setVisibility(View.GONE);
                btnTest.setVisibility(View.GONE);
                btnEncodeAudioAndImage.setVisibility(View.GONE);
                btnUpdateData.setVisibility(View.GONE);

                btnNextURL.setEnabled(true);
                btnExel.setEnabled(true);
                btnJsonArray.setEnabled(true);
                btnTest.setEnabled(true);
                btnEncodeAudioAndImage.setEnabled(true);
                btnUpdateData.setEnabled(true);
                btnGetData.setEnabled(true);
            }else {
                Toast.makeText(ParseToiec.this,"Max URL",Toast.LENGTH_SHORT).show();
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

                    String image = URL600IMAGE + vocabulary.replaceAll(" ", "_").toLowerCase() + ".jpg";
                    toiec.setImg(image);

                    String noidung = document.body().getElementsByClass("noidung").html();

                    if (vocabulary.contains(" ") || vocabulary.equals("delicately") || vocabulary.equals("flexibly")
                            || vocabulary.equals("sufficiently") || vocabulary.equals("cautiously") || vocabulary.equals("aggressively")
                            || vocabulary.equals("inconsiderately") || vocabulary.equals("uniformly") || vocabulary.equals("systematically")
                            || vocabulary.equals("daringly") || vocabulary.equals("rely") || vocabulary.equals("elegance")
                            || vocabulary.equals("embarkation") || vocabulary.equals("refer")) {
                        String mp3UK = "";
                        if (vocabulary.contains(" ")) {
                            mp3UK = URL600MP3 + vocabulary.replace(" ", "_") + ".mp3";
                        } else {
                            mp3UK = URL600MP3 + vocabulary + ".mp3";
                        }

                        String phienAmUK = noidung.split("<span style=\"color: red;\">")[1].split("</span>")[0];
                        toiec.setAudioUK(mp3UK);
                        toiec.setAudioUS(mp3UK);
                        toiec.setTransliterationUK(phienAmUK);
                        toiec.setTransliterationUS(phienAmUK);
                    }

                    String noidunggiaithich = noidung.split("<span class=\"bold\">Giải thích: </span>")[1];

                    String giaithich = noidunggiaithich.split("<span class=\"bold\">Từ loại: </span>")[0];
                    if (giaithich.contains("<br>"))
                        giaithich = giaithich.replace("<br>", "");
                    toiec.setEnglishMean(giaithich);

                    String noidungtuloai = noidunggiaithich.split("<span class=\"bold\">Từ loại: </span>")[1];

                    String tuloai = noidungtuloai.split("<span class=\"bold\">Ví dụ: </span>")[0];

                    if (tuloai.contains("(adj):")) {
                        String VietnamExample = tuloai.replace("(adj):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adj)");
                    } else if (tuloai.contains("(v):")) {
                        String VietnamExample = tuloai.replace("(v):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(v)");
                    } else if (tuloai.contains("(n):")) {
                        String VietnamExample = tuloai.replace("(n):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n)");
                    } else if (tuloai.contains("(v, n):")) {
                        String VietnamExample = tuloai.replace("(v, n):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(v, n)");
                    } else if (tuloai.contains("(adv):")) {
                        String VietnamExample = tuloai.replace("(adv):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adv)");
                    } else if (tuloai.contains("(adj, n):")) {
                        String VietnamExample = tuloai.replace("(adj, n):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adj, n)");
                    } else if (tuloai.contains("(n,v):")) {
                        String VietnamExample = tuloai.replace("(n,v):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n,v)");
                    } else if (tuloai.contains("(n, v):")) {
                        String VietnamExample = tuloai.replace("(n, v):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n,v)");
                    } else if (tuloai.contains("(v,n):")) {
                        String VietnamExample = tuloai.replace("(v,n):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n,v)");
                    } else if (tuloai.contains("(n, v ):")) {
                        String VietnamExample = tuloai.replace("(n, v ):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(n, v )");
                    } else if (tuloai.contains("(adj, v):")) {
                        String VietnamExample = tuloai.replace("(adj, v):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(adj, v)");
                    } else if (tuloai.contains("(perp):")) {
                        String VietnamExample = tuloai.replace("(perp):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(perp)");
                    } else if (tuloai.contains("(to become wider):")) {
                        String VietnamExample = tuloai.replace("(to become wider):", "");
                        if (VietnamExample.contains("<br>"))
                            VietnamExample = VietnamExample.replace("<br>", "");
                        toiec.setVietnamMean(VietnamExample);
                        toiec.setFromCategory("(to become wider)");
                    } else {
                        Log.d("No FromCATEGORY", tuloai);
                    }

                    String vidu = noidungtuloai.split("<span class=\"bold\">Ví dụ: </span>")[1].split("<audio controls>")[0];
                    if (vidu.contains("<hr>")) {
                        String EngExample = vidu.split("<hr>")[0];
                        if (EngExample.contains("<br>"))
                            EngExample = EngExample.replace("<br>", "");
                        toiec.setEnglishExample(EngExample);
                        String ViExample = vidu.split("<hr>")[1];
                        if (ViExample.contains("<br>"))
                            ViExample = ViExample.replace("<br>", "");
                        toiec.setVietnamExample(ViExample);
                    } else if (vidu.contains("<b>")) {
                        String EngExample = vidu.split("<b>")[0];
                        if (EngExample.contains("<br>"))
                            EngExample = EngExample.replace("<br>", "");
                        toiec.setEnglishExample(EngExample);
                        String ViExample = vidu.split("<b>")[1];
                        if (ViExample.contains("<br>"))
                            ViExample = ViExample.replace("<br>", "");
                        toiec.setVietnamExample(ViExample);
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
            if (toiec.getAudioUK() != null && !TextUtils.isEmpty(toiec.audioUK)) {
                return toiec;
            } else {
                URL = URLCAMBRIDGE + vocabulary.toLowerCase();
                toiec = parseMP3(toiec, vocabulary, URL, US, UK, word);
            }
            return toiec;
        }

        @Override
        protected void onPostExecute(Toiec toiec) {
            super.onPostExecute(toiec);
            txtmeta.setText("" + (word + 1));
            if (word == listToiec.size() - 1) {
                btnEncodeAudioAndImage.setVisibility(View.VISIBLE);
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
            Log.d("Error", "number : " + (word + 1) + "\n" + vocabulary);
            listVocabularyDontParse.add(vocabulary);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.d("Error", "number : " + (word + 1) + "\n" + vocabulary);
            listVocabularyDontParse.add(vocabulary);
        } catch (IndexOutOfBoundsException e) {
            Log.d("Error", "number : " + (word + 1) + "\n" + vocabulary);
            listVocabularyDontParse.add(vocabulary);
        }
        return toiec;
    }

    class DecodeBase64 extends AsyncTask<Toiec, Void, Toiec> {

        int number;

        public DecodeBase64(int number) {
            this.number = number;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtmeta.setText("Please wait...");
        }

        @Override
        protected Toiec doInBackground(Toiec... params) {
            Toiec toiec = params[0];
            String urlImageTopic = toiec.getImageTopic();
            String urlImageWord = toiec.getImg();
            String urlUS = toiec.getAudioUS();
            String urlUK = toiec.getAudioUK();

            String encodeTopic = stringBase64(urlImageTopic, number);
            toiec.setImageTopic(encodeTopic);
            String encodeWord = stringBase64(urlImageWord, number);
            toiec.setImg(encodeWord);
            String encodeUS = stringBase64(urlUS, number);
            toiec.setAudioUS(encodeUS);
            String encodeUK = stringBase64(urlUK, number);
            toiec.setAudioUK(encodeUK);
            return toiec;
        }

        @Override
        protected void onPostExecute(Toiec toiec) {
            super.onPostExecute(toiec);
            txtmeta.setText("" + (number + 1));
            if (number == listToiec.size() - 1) {
                btnTest.setVisibility(View.VISIBLE);
            }
        }
    }

    private String stringBase64(String url, int number) {

        String encoded = "";
        URL downloadURL = null;
        HttpURLConnection conn = null;

        try {
            downloadURL = new URL(url);

            conn = (HttpURLConnection) downloadURL
                    .openConnection();

            InputStream is = conn.getInputStream();

            Log.d("GetByte", "Start Get Byte");

            byte[] bytes;
            byte[] buffer = new byte[1024];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = is.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("GetByte", "End Get Byte");
            bytes = output.toByteArray();

            encoded = Base64.encodeToString(bytes, 0);

        } catch (MalformedURLException e) {
            Log.d("ErrorEncode", "" + number);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("ErrorEncode", "" + number);
            e.printStackTrace();
        }
        return encoded;

    }

    class DisplayImageAndAudio extends AsyncTask<Toiec, Void, Void> {

        @Override
        protected Void doInBackground(Toiec... params) {


            String US = "data:audio/mp3;base64," + params[0].getAudioUS();
            String UK = "data:audio/mp3;base64," + params[0].getAudioUK();

            byte[] decodedTopic = Base64.decode(params[0].getImageTopic(), Base64.DEFAULT);
            final Bitmap decodedByteTopic = BitmapFactory.decodeByteArray(decodedTopic, 0, decodedTopic.length);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgTopic.setImageBitmap(decodedByteTopic);
                }
            });

            byte[] decodedWord = Base64.decode(params[0].getImg(), Base64.DEFAULT);
            final Bitmap decodedByteWord = BitmapFactory.decodeByteArray(decodedWord, 0, decodedWord.length);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgWord.setImageBitmap(decodedByteWord);
                }
            });

            MediaPlayer mediaPlayerUS = new MediaPlayer();
            final MediaPlayer mediaPlayerUK = new MediaPlayer();

            try {
                mediaPlayerUS.setDataSource(US);
                mediaPlayerUS.prepare();

                mediaPlayerUK.setDataSource(UK);
                mediaPlayerUK.prepare();

                mediaPlayerUS.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            Thread.sleep(2000);
                            mediaPlayerUK.start();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });

                mediaPlayerUS.start();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private void xuatFileExelMonAnhVan(String filename,List<Toiec> listToiec){
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
                WritableSheet sheet = workbook.createSheet("ListToiec", 0);
                int arraySize = listToiec.size();
                Label[][] headerArray = new Label[1][14];
                Label[][] rowsArray = new Label[arraySize + 1][4];
                headerArray[0][0] = new Label(0, 0, "englishtopic");
                headerArray[0][1] = new Label(1, 0, "vietnamTopic");
                headerArray[0][2] = new Label(2, 0, "imageTopic");
                headerArray[0][3] = new Label(3, 0, "vocabulary");
                headerArray[0][4] = new Label(4, 0, "transliterationUS");
                headerArray[0][5] = new Label(5, 0, "transliterationUK");
                headerArray[0][6] = new Label(6, 0, "englishMean");
                headerArray[0][7] = new Label(7, 0, "vietnamMean");
                headerArray[0][8] = new Label(8, 0, "fromCategory");
                headerArray[0][9] = new Label(9, 0, "englishExample");
                headerArray[0][10] = new Label(10, 0, "vietnamExample");
                headerArray[0][11] = new Label(11, 0, "audioUS");
                headerArray[0][12] = new Label(12, 0, "audioUK");
                headerArray[0][13] = new Label(13, 0, "img");

                Toiec toiec = new Toiec();
                for (int i = 0; i < arraySize; i++){
                    if (i < arraySize) {
                        toiec = listToiec.get(i);
                    }
                    int j = i + 1;
                    rowsArray[j][0] = new Label(0, j,toiec.getEnglishtopic());
                    rowsArray[j][1] = new Label(1, j,toiec.getVietnamTopic());
                    rowsArray[j][2] = new Label(2, j,toiec.getImageTopic());
                    rowsArray[j][3] = new Label(3, j,toiec.getVocabulary());
                    rowsArray[j][4] = new Label(4, j,toiec.getTransliterationUS());
                    rowsArray[j][5] = new Label(5, j,toiec.getTransliterationUK());
                    rowsArray[j][6] = new Label(6, j,toiec.getEnglishMean());
                    rowsArray[j][7] = new Label(7, j,toiec.getVietnamMean());
                    rowsArray[j][8] = new Label(8, j,toiec.getFromCategory());
                    rowsArray[j][9] = new Label(9, j,toiec.getEnglishExample());
                    rowsArray[j][10] = new Label(10, j,toiec.getVietnamExample());
                    rowsArray[j][11] = new Label(11, j,toiec.getAudioUS());
                    rowsArray[j][12] = new Label(12, j,toiec.getAudioUK());
                    rowsArray[j][13] = new Label(13, j,toiec.getImg());

                }

                // Add header cells
                for (int i = 0; i < 14; i++) {
                    sheet.addCell(headerArray[0][i]);
                }

                // Add data cells
                for (int i = 1; i <= arraySize; i++) {
                    for (int j = 0; j < 14; j++) {
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
                        "Toiec");
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
}
