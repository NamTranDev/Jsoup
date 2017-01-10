package namtran.jsoup;

/**
 * Created by NamTran on 1/9/2017.
 */

public class Toiec {

    String englishtopic;
    String vietnamTopic;
    String imageTopic;
    String vocabulary;
    String transliterationUS;
    String transliterationUK;
    String englishMean;
    String vietnamMean;
    String fromCategory;
    String englishExample;
    String vietnamExample;
    String audioUS;
    String audioUK;
    String img;

    public String getEnglishtopic() {
        return englishtopic;
    }

    public void setEnglishtopic(String englishtopic) {
        this.englishtopic = englishtopic;
    }

    public String getVietnamTopic() {
        return vietnamTopic;
    }

    public void setVietnamTopic(String vietnamTopic) {
        this.vietnamTopic = vietnamTopic;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public String getTransliterationUS() {
        return transliterationUS;
    }

    public void setTransliterationUS(String transliterationUS) {
        this.transliterationUS = transliterationUS;
    }

    public String getTransliterationUK() {
        return transliterationUK;
    }

    public void setTransliterationUK(String transliterationUK) {
        this.transliterationUK = transliterationUK;
    }

    public String getEnglishMean() {
        return englishMean;
    }

    public void setEnglishMean(String englishMean) {
        this.englishMean = englishMean;
    }

    public String getVietnamMean() {
        return vietnamMean;
    }

    public void setVietnamMean(String vietnamMean) {
        this.vietnamMean = vietnamMean;
    }

    public String getFromCategory() {
        return fromCategory;
    }

    public void setFromCategory(String fromCategory) {
        this.fromCategory = fromCategory;
    }

    public String getEnglishExample() {
        return englishExample;
    }

    public void setEnglishExample(String englishExample) {
        this.englishExample = englishExample;
    }

    public String getVietnamExample() {
        return vietnamExample;
    }

    public void setVietnamExample(String vietnamExample) {
        this.vietnamExample = vietnamExample;
    }

    public String getAudioUS() {
        return audioUS;
    }

    public void setAudioUS(String audioUS) {
        this.audioUS = audioUS;
    }

    public String getAudioUK() {
        return audioUK;
    }

    public void setAudioUK(String audioUK) {
        this.audioUK = audioUK;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImageTopic() {
        return imageTopic;
    }

    public void setImageTopic(String imageTopic) {
        this.imageTopic = imageTopic;
    }

    @Override
    public String toString() {
        return "Toiec{" +
                "englishtopic='" + englishtopic + '\'' +
                ", vietnamTopic='" + vietnamTopic + '\'' +
                ", imageTopic='" + imageTopic + '\'' +
                ", vocabulary='" + vocabulary + '\'' +
                ", transliterationUS='" + transliterationUS + '\'' +
                ", transliterationUK='" + transliterationUK + '\'' +
                ", englishMean='" + englishMean + '\'' +
                ", vietnamMean='" + vietnamMean + '\'' +
                ", fromCategory='" + fromCategory + '\'' +
                ", englishExample='" + englishExample + '\'' +
                ", vietnamExample='" + vietnamExample + '\'' +
                ", audioUS='" + audioUS + '\'' +
                ", audioUK='" + audioUK + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}