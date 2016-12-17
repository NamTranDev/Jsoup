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
import org.jsoup.nodes.Element;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MyTask mt;
    GetCauHoiAnhVan getCauHoiAnhVan;
    TextView txtmeta;
    Button btnGetData,btnexel;
    //List<String> listURL = AppSetting.listURLMonSinhHeSinhThai();
    //List<String> listURL = AppSetting.listURLMonSinhSinhThaiHocQuanXa();
    //List<String> listURL = AppSetting.listURLMonSinhSinhThaiHocCaTheVaQuanThe();
    //List<String> listURL = AppSetting.listURLBangChungVaCoCheTienHoa();
    //List<String> listURL = AppSetting.listURLDiTruyenHocNguoi();
    //List<String> listURL = AppSetting.linkURLUngDungDiTruyenHocVaoChonGiong();
    //List<String> listURL = AppSetting.listURLDiTruyenHocQuanThe();
    //List<String> listURL = AppSetting.listURLDiTruyenLienKetGioiTinhTeBaoGenVaDaHieu();
    //List<String> listURL = AppSetting.listURLDiTruyenLienKetGenVaHoanViGen();
    //List<String> listURL = AppSetting.listURLDiTruyenTuongTacGen();
    //List<String> listURL = AppSetting.listURLDiTruyenMenden();
    //List<String> listURL = AppSetting.lisURLCoCheBienDiCapDoTeBao();
    //List<String> listURL = AppSetting.listURLVatChatDiTruyenVaCoCheDiTruyenCapDoTeBao();
    //List<String> listURL = AppSetting.listURLCoCheBienDiCapDoPhanTu();
    //List<String> listURL = AppSetting.listURLCoCheDiTruyenCapDoPhanTu();
    //List<String> listURL = AppSetting.listURLVatChatDiTruyenCapDoPhanTu();
    //List<String> listURL = AppSetting.listURLPhatSinhSuSongVaPhatSinhLoaiNguoi();
    //List<String> listURL = AppSetting.listURLTongHopQuyLuatDiTruyen();
    //List<String> listURL = AppSetting.listURLDaoDongCoHoc();
    //List<String> listURL = AppSetting.listURLSongCoVaSongAm();
    //List<String> listURL = AppSetting.listURLDongDienXoayChieu();
    //List<String> listURL = AppSetting.listURLDaoDongVaSongDienTu();
    //List<String> listURL = AppSetting.listURLSongAnhSang();
    //List<String> listURL = AppSetting.listURLLuongTuAnhSang();
    //List<String> listURL = AppSetting.listURLHatNhanNguyenTu();
    //List<String> listURL = AppSetting.listURLCoHocVatRan();
    //List<String> listURL = AppSetting.listURLTuViMoDenViMo();
    //List<String> listURL = AppSetting.listURLEsteLipitXaphong();
    //List<String> listURL = AppSetting.listURLDanXuatHalogenAncolPhenol();
    //List<String> listURL = AppSetting.listURLAndehitXetonAxitCacbonxilic();
    //List<String> listURL = AppSetting.listURLCacbohidrat();
    //List<String> listURL = AppSetting.listURLAminAminoAxitProtein();
    //List<String> listURL = AppSetting.listURLPolimeVaVatLieuPolime();
    //List<String> listURL = AppSetting.listURLDaiCuongVeHoaHuuCoVaHydrocarbon();
    //List<String> listURL = AppSetting.listURLTongHopCacPhuongPhapGiaiNhanh();
    //List<String> listURL = AppSetting.listURLDaiCuongVeKimLoai();
    //List<String> listURL = AppSetting.listURLKimLoaiKiemKiemThoVaNhom();
    //List<String> listURL = AppSetting.listURLSatCromDong();
    //List<String> listURL = AppSetting.listURLNhanBietPhanBietTinhChe();
    //List<String> listURL = AppSetting.listURLCauTaoNguyenTuBangTuanHoan();
    //List<String> listURL = AppSetting.listURLLienKetHoaHoc();
    //List<String> listURL = AppSetting.listURLPhanUngOxiHoaKhu();
    //List<String> listURL = AppSetting.listURLSuDienLy();
    //List<String> listURL = AppSetting.listURLTocDoPhanUngVaCanBangHoaHoc();
    //List<String> listURL = AppSetting.listURLPhiKim();
    //List<String> listURL = AppSetting.listURLAxitBazoVaMuoi();
    //List<String> listURL = AppSetting.listURLTongHopHoaHuuCo();
    //List<String> listURL = AppSetting.listURLLyThuyetTongHop();
    //List<String> listURL = AppSetting.listURLVietBacToHuu();
    //List<String> listURL = AppSetting.listURLTayTienQuangDung();
    //List<String> listURL = AppSetting.listURLHamSoVaCacBaiToanLienQuan();
    //List<String> listURL = AppSetting.listURLHinhHocKhongGian();
    //List<String> listURL = AppSetting.listURLPhuongTrinhBatPhuongTrinhVaHePhuongTrinhMuLogarit();
    //List<String> listURL = AppSetting.listURLNguyenHamTichPhanVaUngDung();
    //List<String> listURL = AppSetting.listURLHinhGiaiTichTrongKhongGian();
    //List<String> listURL = AppSetting.listURLSoPhuc();
    //List<String> listURL = AppSetting.listURLHinhGiaiTichPhang();
    //List<String> listURL = AppSetting.listURLPhuongTrinhHePhuongTrinhBatPhuongTrinhLuongGiac();
    //List<String> listURL = AppSetting.listURLToHopXacXuat();
    //List<String> listURL = AppSetting.listURLPhuongTrinhBatPhuongTrinhVaHePhuongTrinhDaiSo();
    //List<String> listURL = AppSetting.listURLBatDangThucGiaTriLonNhatVaNhoNhat();
    List<String> listURL = AppSetting.listURLTuDongNghiaTuTraiNghia();
    //List<String> listURL = AppSetting.listURLDangBaiTimLoiSai();
    List<CauHoi> listCauHoi;
    static List<CauHoiAnhVan> listCauHoiAnhVan;
    int b = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtmeta = (TextView) findViewById(R.id.txtmeta);
        btnGetData = (Button) findViewById(R.id.btnGetData);
        btnexel = (Button) findViewById(R.id.btnexel);
        btnGetData.setOnClickListener(this);
        btnexel.setOnClickListener(this);
        listCauHoi = new ArrayList<>();
        listCauHoiAnhVan = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGetData){
            txtmeta.setText("" + listCauHoiAnhVan.size());
            for (int i =0; i<listURL.size();i++){
                //mt = new MyTask();
                //mt.execute(listURL.get(i));
                getCauHoiAnhVan = new GetCauHoiAnhVan(i);
                getCauHoiAnhVan.execute(listURL.get(i));
            }
        }else if (v.getId() == R.id.btnexel){
            txtmeta.setText("please wait");
            xuatFileExelMonAnhVan("MonAnhVanTuDongNghiaTuTraiNghia");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listCauHoi.size()>0){
            for (int i=0;i<listCauHoi.size();i++){
                Log.d("CauHoi", "CauHoi " + i + " : " + listCauHoi.get(i).toString());
            }
        }
        if (listCauHoiAnhVan.size()>0){
            for (int i=0;i<listCauHoiAnhVan.size();i++){
                Log.d("CauHoi", "CauHoi " + i + " : " + listCauHoiAnhVan.get(i).toString());
            }
        }
    }

    private void xuatFileExel(String filename){
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
                WritableSheet sheet = workbook.createSheet("ListCauHoi", 0);
                int arraySize = listCauHoi.size();
                Label[][] headerArray = new Label[1][5];
                Label[][] rowsArray = new Label[arraySize + 1][5];
                headerArray[0][0] = new Label(0, 0, "Cau Hoi");
                headerArray[0][1] = new Label(1, 0, "Dap An A");
                headerArray[0][2] = new Label(2, 0, "Dap An B");
                headerArray[0][3] = new Label(3, 0, "Dap An C");
                headerArray[0][4] = new Label(4, 0, "Dap An D");
                CauHoi cauHoi = new CauHoi();
                for (int i = 0; i < arraySize; i++){
                    if (i < arraySize) {
                        cauHoi = listCauHoi.get(i);
                    }
                    int j = i + 1;
                    rowsArray[j][0] = new Label(0, j,cauHoi.getCauHoi());
                    rowsArray[j][1] = new Label(1, j,cauHoi.getDapAnA());
                    rowsArray[j][2] = new Label(2, j,cauHoi.getDapAnB());
                    rowsArray[j][3] = new Label(3, j,cauHoi.getDapAnC());
                    rowsArray[j][4] = new Label(4, j,cauHoi.getDapAnD());
                }

                // Add header cells
                for (int i = 0; i < 5; i++) {
                    sheet.addCell(headerArray[0][i]);
                }

                // Add data cells
                for (int i = 1; i <= arraySize; i++) {
                    for (int j = 0; j < 5; j++) {
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
                        "CauHoi");
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
                WritableSheet sheet = workbook.createSheet("ListCauHoi", 0);
                int arraySize = listCauHoiAnhVan.size();
                Label[][] headerArray = new Label[1][7];
                Label[][] rowsArray = new Label[arraySize + 1][7];
                headerArray[0][0] = new Label(0, 0, "Cau Hoi");
                headerArray[0][1] = new Label(1, 0, "Dang Cau Hoi");
                headerArray[0][2] = new Label(2, 0, "Tieu De Cau Hoi");
                headerArray[0][3] = new Label(3, 0, "Dap An A");
                headerArray[0][4] = new Label(4, 0, "Dap An B");
                headerArray[0][5] = new Label(5, 0, "Dap An C");
                headerArray[0][6] = new Label(6, 0, "Dap An D");
                CauHoiAnhVan cauHoi = new CauHoiAnhVan();
                for (int i = 0; i < arraySize; i++){
                    if (i < arraySize) {
                        cauHoi = listCauHoiAnhVan.get(i);
                    }
                    int j = i + 1;
                    rowsArray[j][0] = new Label(0, j,cauHoi.getCauHoi());
                    rowsArray[j][1] = new Label(1, j,cauHoi.getDangCauHoi());
                    rowsArray[j][2] = new Label(2, j,cauHoi.getTieuDeCauHoi());
                    rowsArray[j][3] = new Label(3, j,cauHoi.getDapAnA());
                    rowsArray[j][4] = new Label(4, j,cauHoi.getDapAnB());
                    rowsArray[j][5] = new Label(5, j,cauHoi.getDapAnC());
                    rowsArray[j][6] = new Label(6, j,cauHoi.getDapAnD());
                }

                // Add header cells
                for (int i = 0; i < 7; i++) {
                    sheet.addCell(headerArray[0][i]);
                }

                // Add data cells
                for (int i = 1; i <= arraySize; i++) {
                    for (int j = 0; j < 7; j++) {
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
                        "CauHoi");
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

    class MyTask extends AsyncTask<String, Void, Elements> {
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
                words = doc.body().getElementsByClass("filltext");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return words;
        }

        protected void onPostExecute(Elements result) {
            super.onPostExecute(result);

            for (Element element : result){
                CauHoi cauHoi = new CauHoi();

                Document document = Jsoup.parse(element.html());
                //have answer

                String[] a = element.html().split("<input type=\"radio\" value=\"1\"");

                String[] e = a[0].split("<p class=\"mab5\"");

                if (e[0].contains("</strong>")){
                    b++;
                    String[] h = e[0].split("</strong>");
                    String CauHoi = "<p>" + h[1];
                    cauHoi.setCauHoi(CauHoi);

                    Log.d("DapAn","CAUHOI : " + b + " " + CauHoi);
                }

                Elements dapan = document.getElementsByClass("mab5");

                int i = 0;
                for (Element element1 : dapan){
                    i++;
                    if (i == 1){
                        String DapAn = element1.html();
                        String[] DapAnA = DapAn.split(";\">");
                        Log.d("DapAn","DapAn A: " + DapAnA[1]);
                        cauHoi.setDapAnA(DapAnA[1]);
                    }if (i == 2){
                        String DapAn = element1.html();
                        String[] DapAnB = DapAn.split(";\">");
                        Log.d("DapAn","DapAn B: " + DapAnB[1]);
                        cauHoi.setDapAnB(DapAnB[1]);
                    }if (i == 3){
                        String DapAn = element1.html();
                        String[] DapAnC = DapAn.split(";\">");
                        Log.d("DapAn","DapAn A: " + DapAnC[1]);
                        cauHoi.setDapAnC(DapAnC[1]);
                    }if (i == 4){
                        String DapAn = element1.html();
                        String[] DapAnD = DapAn.split(";\">");
                        Log.d("DapAn","DapAn D: " + DapAnD[1]);
                        cauHoi.setDapAnD(DapAnD[1]);
                    }

                }
                if (cauHoi.getCauHoi() != null){

                    /*if (cauHoi.getDapAnA() != null && cauHoi.getDapAnB() != null && cauHoi.getDapAnC() != null && cauHoi.getDapAnD() != null){
                        if (!cauHoi.getDapAnA().contains("<img src=") && !cauHoi.getDapAnB().contains("<img src=") && !cauHoi.getDapAnC().contains("<img src=") && !cauHoi.getDapAnD().contains("<img src=")) {
                            listCauHoi.add(cauHoi);
                        }
                    }*/
                    listCauHoi.add(cauHoi);
                }
            }

            txtmeta.setText("" + listCauHoi.size() );
        }
    }

    class GetCauHoiAnhVan extends AsyncTask<String, Void, String[]> {
        Document doc;
        String[] result;
        int position;

        public GetCauHoiAnhVan(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtmeta.setText("Please wait...");
        }

        protected String[] doInBackground(String... params) {
            String url = params[0];
            try {
                doc = Jsoup.connect(url).get();
                if (doc.body().html().contains("<div class=\"boder clearfix\">"))
                    result = doc.body().html().split("<div class=\"boder clearfix\">");
                else if (doc.body().html().contains("<div class=\"nobor clearfix\">"))
                    result = doc.body().html().split("<div class=\"nobor clearfix\">");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            for (int i = 0; i < result.length ;i++){
                if (i == 0)
                    continue;
                if (!result[i].contains("<p style=\"text-align: justify;\">")){
                    String[] a = result[i].split("</p>\n" +
                            "        <p></p> \n" +
                            "        <div id=\"question");
                    String[] e;
                    if (a[0].contains("<p><strong>")){
                        e = a[0].split("<p><strong>");
                        String DangCauHoi = e[1];
                        new GetCauHoiAnhVan2(DangCauHoi,"").execute(result[i]);
                    }else if (a[0].contains(":</strong></p>")){
                        e = a[0].split(":</strong></p>");
                        if (e[1].contains("\"><strong>")){
                            String[] d = e[1].split("\"><strong>");
                            String DangCauHoi = d[1];
                            new GetCauHoiAnhVan2(DangCauHoi,"").execute(result[i]);
                        }else {
                            String DangCauHoi = e[1];
                            new GetCauHoiAnhVan2(DangCauHoi,"").execute(result[i]);
                        }

                    }else {
                        Log.d("Error","" + position + "\n" + i);
                        Log.d("Error",a[0]);
                    }
                }else {
                    String[] a = result[i].split("<p style=\"text-align: justify;\">");
                    String[] e = a[1].split("</p>\n" +
                            "        <p></p> \n" +
                            "        <div id=\"question");
                    String DangCauHoi = e[0];
                    new GetCauHoiAnhVan2(DangCauHoi,"").execute(result[i]);
                }

            }
        }
    }

    class GetCauHoiAnhVan2 extends AsyncTask<String, Void, Elements> {
        Document doc;
        Elements words;
        String TieuCauHoi;
        String DangCauHoi;

        public GetCauHoiAnhVan2(String tieuCauHoi, String dangCauHoi) {
            TieuCauHoi = tieuCauHoi;
            DangCauHoi = dangCauHoi;
        }

        protected Elements doInBackground(String... params) {
            String url = params[0];
            doc = Jsoup.parse(url);
            words = doc.body().getElementsByClass("filltext");
            return words;
        }

        protected void onPostExecute(Elements result) {
            super.onPostExecute(result);

            for (Element element : result){
                CauHoiAnhVan cauHoi = new CauHoiAnhVan();

                if (DangCauHoi != "")
                    cauHoi.setDangCauHoi(DangCauHoi);
                if (TieuCauHoi != "")
                    cauHoi.setTieuDeCauHoi(TieuCauHoi);

                Document document = Jsoup.parse(element.html());
                //have answer

                String[] a = element.html().split("<input type=\"radio\" value=\"1\"");

                String[] e = a[0].split("<p class=\"mab5\"");

                Log.d("CauHoi",e[0]);

                if (e[0].contains("<p><strong class=\"fl\">")){
                    b++;
                    String[] h = e[0].split(": </strong>");
                    String CauHoi = "<p>" + h[1];
                    cauHoi.setCauHoi(CauHoi);

                    Log.d("DapAn","CAUHOI : " + b + " " + CauHoi);
                }

                Elements dapan = document.getElementsByClass("mab5");

                int i = 0;
                for (Element element1 : dapan){
                    i++;
                    if (i == 1){
                        String DapAn = element1.html();
                        String[] DapAnA = DapAn.split(";\">");
                        Log.d("DapAn","DapAn A: " + DapAnA[1]);
                        cauHoi.setDapAnA(DapAnA[1]);
                    }if (i == 2){
                        String DapAn = element1.html();
                        String[] DapAnB = DapAn.split(";\">");
                        Log.d("DapAn","DapAn B: " + DapAnB[1]);
                        cauHoi.setDapAnB(DapAnB[1]);
                    }if (i == 3){
                        String DapAn = element1.html();
                        String[] DapAnC = DapAn.split(";\">");
                        Log.d("DapAn","DapAn A: " + DapAnC[1]);
                        cauHoi.setDapAnC(DapAnC[1]);
                    }if (i == 4){
                        String DapAn = element1.html();
                        String[] DapAnD = DapAn.split(";\">");
                        Log.d("DapAn","DapAn D: " + DapAnD[1]);
                        cauHoi.setDapAnD(DapAnD[1]);
                    }

                }
                if (cauHoi.getCauHoi() != null){

                    /*if (cauHoi.getDapAnA() != null && cauHoi.getDapAnB() != null && cauHoi.getDapAnC() != null && cauHoi.getDapAnD() != null){
                        if (!cauHoi.getDapAnA().contains("<img src=") && !cauHoi.getDapAnB().contains("<img src=") && !cauHoi.getDapAnC().contains("<img src=") && !cauHoi.getDapAnD().contains("<img src=")) {
                            listCauHoi.add(cauHoi);
                        }
                    }*/

                    Log.d("CAUHOIAV",cauHoi.toString());
                    listCauHoiAnhVan.add(cauHoi);
                }
            }

            txtmeta.setText("" + listCauHoiAnhVan.size() );
        }
    }
}
