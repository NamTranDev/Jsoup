package namtran.jsoup;

/**
 * Created by Nam Tran on 26-Apr-16.
 */
public class CauHoiAnhVan {

    String CauHoi;
    String DangCauHoi;
    String TieuDeCauHoi;
    String DapAnA;
    String DapAnB;
    String DapAnC;
    String DapAnD;

    public String getDangCauHoi() {
        return DangCauHoi;
    }

    public void setDangCauHoi(String dangCauHoi) {
        DangCauHoi = dangCauHoi;
    }

    public String getTieuDeCauHoi() {
        return TieuDeCauHoi;
    }

    public void setTieuDeCauHoi(String tieuDeCauHoi) {
        TieuDeCauHoi = tieuDeCauHoi;
    }

    public String getCauHoi() {
        return CauHoi;
    }

    public void setCauHoi(String cauHoi) {
        CauHoi = cauHoi;
    }

    public String getDapAnA() {
        return DapAnA;
    }

    public void setDapAnA(String dapAnA) {
        DapAnA = dapAnA;
    }

    public String getDapAnB() {
        return DapAnB;
    }

    public void setDapAnB(String dapAnB) {
        DapAnB = dapAnB;
    }

    public String getDapAnC() {
        return DapAnC;
    }

    public void setDapAnC(String dapAnC) {
        DapAnC = dapAnC;
    }

    public String getDapAnD() {
        return DapAnD;
    }

    public void setDapAnD(String dapAnD) {
        DapAnD = dapAnD;
    }

    @Override
    public String toString() {
        return "CauHoiAnhVan{" +
                "CauHoi='" + CauHoi + '\'' +
                ", DangCauHoi='" + DangCauHoi + '\'' +
                ", TieuDeCauHoi='" + TieuDeCauHoi + '\'' +
                ", DapAnA='" + DapAnA + '\'' +
                ", DapAnB='" + DapAnB + '\'' +
                ", DapAnC='" + DapAnC + '\'' +
                ", DapAnD='" + DapAnD + '\'' +
                '}';
    }
}
