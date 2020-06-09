package com.example.Mpscquiz;

public class User {
     private String Name,Email,gender;
     public int logicalm=0,englishm=0,historym=0,geographym=0,agriculturem=0,politicm=0,human_resm=0,sciencem=0,economicsm=0,current_affairm=0;
      public User()
      {

      }
        public User(String name,String email, String Gender, int Logicalm ,int Englishm,int Historym , int Geographym , int Agriculturem, int Politicm, int Human_resm, int Sciencem, int Economicsm,int Current_affairm )
        {
            this.Name=name;
            this.Email=email;
            this.gender=Gender;
            this.logicalm=Logicalm;
            this.englishm=Englishm;
            this.historym=Historym;
            this.geographym=Geographym;
            this.agriculturem=Agriculturem;
            this.politicm=Politicm;
            this.human_resm=Human_resm;
            this.sciencem=Sciencem;
            this.economicsm=Economicsm;
            this.current_affairm=Current_affairm;
        }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String Gender) {
        gender = Gender;
    }

    public int getLogicalm() {
        return logicalm;
    }

    public void setLogicalm(int Logicalm) {
        logicalm = Logicalm;
    }

    public int getEnglishm() {
        return englishm;
    }

    public void setEnglishm(int Englishm) {
        englishm = Englishm;
    }

    public int getHistorym() {
        return historym;
    }

    public void setHistorym(int Historym) {
        historym = Historym;
    }

    public int getGeographym() {
        return geographym;
    }

    public void setGeographym(int Geographym) {
        geographym = Geographym;
    }

    public int getAgriculturem() {
        return agriculturem;
    }

    public void setAgriculturem(int Agriculturem) {
        agriculturem = Agriculturem;
    }

    public int getPoliticm() {
        return politicm;
    }

    public void setPoliticm(int Politicm) {
        politicm = Politicm;
    }

    public int getHuman_resm() {
        return human_resm;
    }

    public void setHuman_resm(int Human_resm) {
        human_resm = Human_resm;
    }

    public int getSciencem() {
        return sciencem;
    }

    public void setSciencem(int Sciencem) {
        sciencem = Sciencem;
    }

    public int getEconomicsm() {
        return economicsm;
    }

    public void setEconomicsm(int Economicsm) {
        economicsm = Economicsm;
    }

    public int getCurrent_affairm() {
        return current_affairm;
    }

    public void setCurrent_affairm(int Current_affairm) {
        current_affairm = Current_affairm;
    }


}
