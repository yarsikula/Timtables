package cz.uhk.tables;

import com.google.gson.annotations.SerializedName;

public class RozvrhovaAkce {
    public String roakIdno;
    public String nazev;
    public String predmet;
    public String den;
    public String mistnost;
    public Ucitel ucitel;

    // times need a timewrapper
    public TimeWrapper hodinaSkutOd;
    public TimeWrapper hodinaSkutDo;

    public Ucitel getUcitel(){
        return ucitel;
    }

    public void setUcitel(Ucitel ucitel){
        this.ucitel = ucitel;
    }

    public class Ucitel {
        @SerializedName("jmeno")
        public String jmeno;
        public String prijmeni;
        public String titulPred;
        public String titulZa;

        public String getJmeno(){
            return jmeno;
        }

        public void setJmeno(){
            this.jmeno = jmeno;
        }
    }

    public static class TimeWrapper {
        public String value;
    }
}
