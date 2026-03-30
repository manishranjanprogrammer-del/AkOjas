package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class work for numerology calculation
 */

public class NumerologyCalculatorModel implements Parcelable {

    public static final Creator<NumerologyCalculatorModel> CREATOR = new Creator<NumerologyCalculatorModel>() {
        @Override
        public NumerologyCalculatorModel createFromParcel(Parcel in) {
            return new NumerologyCalculatorModel(in);
        }

        @Override
        public NumerologyCalculatorModel[] newArray(int size) {
            return new NumerologyCalculatorModel[size];
        }
    };
    private String rashi;
    private String alphabet;
    private String tatva;
    private String partOfBody;
    private String ratna;
    private String disha;
    private String profession;
    private String days;
    private String country;
    private String city;
    private String fDate;
    private String bDate;
    private String disease;
    private String age;
    private String care;
    private String yantra;
    private String person;
    private String yantraLink;
    private String color;
    private String planet;
    private String god;
    private String fast;
    private String mantra;
    private String number;
    private String textDestiny;
    private String place;
    private String time;
    private String health;
    private String career;
    private String fastdetail;
    private String yantraDetail;
    private String mulankDesc;

    public NumerologyCalculatorModel() {

    }

    protected NumerologyCalculatorModel(Parcel in) {
        rashi = in.readString();
        alphabet = in.readString();
        tatva = in.readString();
        partOfBody = in.readString();
        ratna = in.readString();
        disha = in.readString();
        profession = in.readString();
        days = in.readString();
        country = in.readString();
        city = in.readString();
        fDate = in.readString();
        bDate = in.readString();
        disease = in.readString();
        age = in.readString();
        care = in.readString();
        yantra = in.readString();
        person = in.readString();
        yantraLink = in.readString();
        color = in.readString();
        planet = in.readString();
        god = in.readString();
        fast = in.readString();
        mantra = in.readString();
        number = in.readString();
        textDestiny = in.readString();
        place = in.readString();
        time = in.readString();
        health = in.readString();
        career = in.readString();
        fastdetail = in.readString();
        yantraDetail = in.readString();
        mulankDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rashi);
        dest.writeString(alphabet);
        dest.writeString(tatva);
        dest.writeString(partOfBody);
        dest.writeString(ratna);
        dest.writeString(disha);
        dest.writeString(profession);
        dest.writeString(days);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(fDate);
        dest.writeString(bDate);
        dest.writeString(disease);
        dest.writeString(age);
        dest.writeString(care);
        dest.writeString(yantra);
        dest.writeString(person);
        dest.writeString(yantraLink);
        dest.writeString(color);
        dest.writeString(planet);
        dest.writeString(god);
        dest.writeString(fast);
        dest.writeString(mantra);
        dest.writeString(number);
        dest.writeString(textDestiny);
        dest.writeString(place);
        dest.writeString(time);
        dest.writeString(health);
        dest.writeString(career);
        dest.writeString(fastdetail);
        dest.writeString(yantraDetail);
        dest.writeString(mulankDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRashi() {
        return rashi;
    }

    public void setRashi(String rashi) {
        this.rashi = rashi;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public String getTatva() {
        return tatva;
    }

    public void setTatva(String tatva) {
        this.tatva = tatva;
    }

    public String getPartOfBody() {
        return partOfBody;
    }

    public void setPartOfBody(String partOfBody) {
        this.partOfBody = partOfBody;
    }

    public String getRatna() {
        return ratna;
    }

    public void setRatna(String ratna) {
        this.ratna = ratna;
    }

    public String getDisha() {
        return disha;
    }

    public void setDisha(String disha) {
        this.disha = disha;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getfDate() {
        return fDate;
    }

    public void setfDate(String fDate) {
        this.fDate = fDate;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }

    public String getYantra() {
        return yantra;
    }

    public void setYantra(String yantra) {
        this.yantra = yantra;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getYantraLink() {
        return yantraLink;
    }

    public void setYantraLink(String yantraLink) {
        this.yantraLink = yantraLink;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getGod() {
        return god;
    }

    public void setGod(String god) {
        this.god = god;
    }

    public String getFast() {
        return fast;
    }

    public void setFast(String fast) {
        this.fast = fast;
    }

    public String getMantra() {
        return mantra;
    }

    public void setMantra(String mantra) {
        this.mantra = mantra;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTextDestiny() {
        return textDestiny;
    }

    public void setTextDestiny(String textDestiny) {
        this.textDestiny = textDestiny;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getFastdetail() {
        return fastdetail;
    }

    public void setFastdetail(String fastdetail) {
        this.fastdetail = fastdetail;
    }

    public String getYantraDetail() {
        return yantraDetail;
    }

    public void setYantraDetail(String yantraDetail) {
        this.yantraDetail = yantraDetail;
    }

    public String getMulankDesc() {
        return mulankDesc;
    }

    public void setMulankDesc(String mulankDesc) {
        this.mulankDesc = mulankDesc;
    }
}
