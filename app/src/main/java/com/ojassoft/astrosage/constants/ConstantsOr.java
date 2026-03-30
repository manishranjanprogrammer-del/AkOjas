package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

/**
 * Supplies full Odia panchang labels so local Panchang calculations do not fall back to English.
 */
public class ConstantsOr implements IConstants {
    @Override
    public String getMasas(int index) {
        return masas[index];
    }

    @Override
    public String getVaras(int index) {
        return varas[index];
    }

    @Override
    public String getVarasSwami(int index) {
        return vaarsSwami[index];
    }

    @Override
    public String getSamvats(int index) {
        return samvats[index];
    }

    @Override
    public String getRitus(int index) {
        return ritus[index];
    }

    @Override
    public String getKaranas(int index) {
        return karanas[index];
    }

    @Override
    public String getPakshas(int index) {
        return pakshas[index];
    }

    @Override
    public String getAyanas(int index) {
        return ayanas[index];
    }

    @Override
    public String getDishas(int index) {
        return dishas[index];
    }

    @Override
    public String getNakshatra(int index) {
        return nakshatra[index];
    }

    @Override
    public String getYoga(int index) {
        return yoga[index];
    }

    @Override
    public String getMoonSign(int index) {
        return rashi[index];
    }

    @Override
    public String getTithi(int index) {
        return tithi[index];
    }

    @Override
    public String getChandraBala(int index) {
        return rashi[index];
    }

    @Override
    public String getTaraBala(int index) {
        return nakshatra[index];
    }

    @Override
    public String[] getChandraBala() {
        return rashi;
    }

    @Override
    public String[] getTaraBala() {
        return nakshatra;
    }

    @Override
    public String getMonthName(int index) {
        return monthname[index];
    }

    @Override
    public String getMonthNameSort(int index) {
        return monthnameSort[index];
    }

    @Override
    public String getDayName(int index) {
        return dayname[index];
    }

    @Override
    public String getExString(int index) {
        return exstring[index];
    }

    @Override
    public String getChoghadiaDayName(int index) {
        return choghadiaDayName[index];
    }

    @Override
    public String getChoghadiaNightName(int index) {
        return choghadiaNightName[index];
    }

    @Override
    public String getLagna(int index) {
        return rashi2[index];
    }

    @Override
    public String getLagnaNature(int index) {
        return rashiNature[index];
    }

    @Override
    public String getRashi(int index) {
        return rashi2[index];
    }

    public final String[] masas = {"ଚୈତ୍ର", "ବୈଶାଖ", "ଜ୍ୟେଷ୍ଠ", "ଆଷାଢ଼", "ଶ୍ରାବଣ", "ଭାଦ୍ରବ", "ଆଶ୍ୱିନ", "କାର୍ତ୍ତିକ", "ମାର୍ଗଶୀର୍ଷ", "ପୌଷ", "ମାଘ", "ଫାଲ୍ଗୁନ"};
    public final String[] varas = {"ରବିବାର", "ସୋମବାର", "ମଙ୍ଗଳବାର", "ବୁଧବାର", "ଗୁରୁବାର", "ଶୁକ୍ରବାର", "ଶନିବାର"};
    public final String[] vaarsSwami = {"NA", "ସୂର୍ଯ୍ୟ", "ଚନ୍ଦ୍ର", "ମଙ୍ଗଳ", "ବୁଧ", "ବୃହସ୍ପତି", "ଶୁକ୍ର", "ଶନି"};
    public final String[] samvats = {"ନା", "ପ୍ରଭବ", "ବିଭବ", "ଶୁକ୍ଳ", "ପ୍ରମୋଦ", "ପ୍ରଜାପତି", "ଆଙ୍ଗିରସ", "ଶ୍ରୀମୁଖ", "ଭାବ",
            "ଯୁବ", "ଧାତା", "ଇଶ୍ୱର", "ବହୁଧାନ୍ୟ", "ପ୍ରମାଥୀ", "ବିକ୍ରମ", "ବୃଷ", "ଚିତ୍ରଭାନୁ",
            "ସ୍ୱଭାନୁ", "ତାରଣ", "ପାର୍ଥିବ", "ବ୍ୟୟ", "ସର୍ବଜିତ", "ସର୍ବଧାରୀ", "ବିରୋଧୀ", "ବିକୃତି",
            "ଖର", "ନନ୍ଦନ", "ବିଜୟ", "ଜୟ", "ମନ୍ମଥ", "ଦୁର୍ମୁଖ", "ହେବିଳମ୍ବୀ", "ବିଲମ୍ବୀ",
            "ବିକାରୀ", "ଶାର୍ୱରୀ", "ପ୍ଲବ", "ଶୁଭକୃତ", "ଶୋଭକୃତ", "କ୍ରୋଧୀ", "ବିଶ୍ୱାବସୁ", "ପରାଭବ",
            "ପ୍ଲବଙ୍ଗ", "କୀଲକ", "ସୌମ୍ୟ", "ସାଧାରଣ", "ବିରୋଧକୃତ", "ପରିଧାବୀ", "ପ୍ରମାଦୀଚ", "ଆନନ୍ଦ", "ରାକ୍ଷସ", "ନଳ",
            "ପିଙ୍ଗଳ", "କାଳୟୁକ୍ତି", "ସିଦ୍ଧାର୍ଥୀ", "ରୌଦ୍ର", "ଦୁର୍ମତି", "ଦୁନ୍ଦୁଭି", "ରୁଧିରୋଦ୍ଗାରୀ", "ରକ୍ତାକ୍ଷୀ", "କ୍ରୋଧନ", "ଅକ୍ଷୟ"};
    public final String[] ritus = {"ବସନ୍ତ", "ଗ୍ରୀଷ୍ମ", "ବର୍ଷା", "ଶରତ", "ହେମନ୍ତ", "ଶିଶିର"};
    public final String[] karanas = {"NA", "କିନ୍ତୁଘ୍ନ", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ବବ", "ବାଳବ", "କୌଳବ", "ତୈତିଳ", "ଗର", "ବଣିଜ", "ବିଷ୍ଟି", "ଶକୁନି", "ଚତୁଷ୍ପାଦ", "ନାଗ"};
    public final String[] pakshas = {"ଶୁକ୍ଳ", "କୃଷ୍ଣ"};
    public final String[] ayanas = {"ଉତ୍ତରାୟଣ", "ଦକ୍ଷିଣାୟଣ"};
    public final String[] dishas = {"ପୂର୍ବ", "ପଶ୍ଚିମ", "ଉତ୍ତର", "ଦକ୍ଷିଣ"};
    public final String[] nakshatra = {"NA", "ଅଶ୍ୱିନୀ", "ଭରଣୀ", "କୃତ୍ତିକା", "ରୋହିଣୀ", "ମୃଗଶିରା", "ଆର୍ଦ୍ରା", "ପୁନର୍ବସୁ", "ପୁଷ୍ୟା", "ଅଶ୍ଳେଷା", "ମଘା", "ପୂର୍ବଫାଲ୍ଗୁନୀ", "ଉତ୍ତରଫାଲ୍ଗୁନୀ", "ହସ୍ତା", "ଚିତ୍ରା", "ସ୍ୱାତୀ", "ବିଶାଖା", "ଅନୁରାଧା", "ଜ୍ୟେଷ୍ଠା", "ମୂଳା", "ପୂର୍ବାଷାଢ଼ା", "ଉତ୍ତରାଷାଢ଼ା", "ଶ୍ରବଣା", "ଧନିଷ୍ଠା", "ଶତଭିଷା", "ପୂର୍ବଭାଦ୍ରପଦା", "ଉତ୍ତରଭାଦ୍ରପଦା", "ରେବତୀ"};
    public final String[] yoga = {"NA", "ବିଷ୍କୁମ୍ଭ", "ପ୍ରୀତି", "ଆୟୁଷ୍ମାନ", "ସୌଭାଗ୍ୟ", "ଶୋଭନ", "ଅତିଗଣ୍ଡ", "ସୁକର୍ମା", "ଧୃତି", "ଶୂଳ", "ଗଣ୍ଡ", "ବୃଦ୍ଧି", "ଧ୍ରୁବ",
            "ବ୍ୟାଘାତ", "ହର୍ଷଣ", "ବଜ୍ର", "ସିଦ୍ଧି", "ବ୍ୟତିପାତ", "ବରୀୟାନ", "ପରିଘ", "ଶିବ", "ସିଦ୍ଧ", "ସାଧ୍ୟ", "ଶୁଭ", "ଶୁକ୍ଳ", "ବ୍ରହ୍ମ", "ଇନ୍ଦ୍ର", "ବୈଧୃତି"};
    public final String[] tithi = {"NA", "ପ୍ରତିପଦା", "ଦ୍ୱିତୀୟା", "ତୃତୀୟା", "ଚତୁର୍ଥୀ", "ପଞ୍ଚମୀ", "ଷଷ୍ଠୀ", "ସପ୍ତମୀ", "ଅଷ୍ଟମୀ", "ନବମୀ", "ଦଶମୀ", "ଏକାଦଶୀ", "ଦ୍ୱାଦଶୀ", "ତ୍ରୟୋଦଶୀ", "ଚତୁର୍ଦଶୀ",
            "ପୂର୍ଣ୍ଣିମା", "ପ୍ରତିପଦା", "ଦ୍ୱିତୀୟା", "ତୃତୀୟା", "ଚତୁର୍ଥୀ", "ପଞ୍ଚମୀ", "ଷଷ୍ଠୀ", "ସପ୍ତମୀ", "ଅଷ୍ଟମୀ", "ନବମୀ", "ଦଶମୀ", "ଏକାଦଶୀ", "ଦ୍ୱାଦଶୀ", "ତ୍ରୟୋଦଶୀ", "ଚତୁର୍ଦଶୀ", "ଅମାବାସ୍ୟା"};
    public final String[] rashi = {"NA", "ମେଷ", "ବୃଷ", "ମିଥୁନ", "କର୍କଟ", "ସିଂହ", "କନ୍ୟା", "ତୁଳା", "ବିଛା", "ଧନୁ", "ମକର", "କୁମ୍ଭ", "ମୀନ"};
    public final String[] rashi2 = {"NA", "ମେଷ", "ବୃଷ", "ମିଥୁନ", "କର୍କଟ", "ସିଂହ", "କନ୍ୟା", "ତୁଳା", "ବିଛା", "ଧନୁ", "ମକର", "କୁମ୍ଭ", "ମୀନ"};
    public final String[] rashiNature = {"NA", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ"};
    public final String[] monthname = {"NA", "ଜାନୁଆରୀ", "ଫେବ୍ରୁଆରୀ", "ମାର୍ଚ୍ଚ", "ଏପ୍ରିଲ୍", "ମେ", "ଜୁନ୍", "ଜୁଲାଇ", "ଅଗଷ୍ଟ", "ସେପ୍ଟେମ୍ବର", "ଅକ୍ଟୋବର", "ନଭେମ୍ବର", "ଡିସେମ୍ବର"};
    public final String[] monthnameSort = {"NA", "ଜାନୁ", "ଫେବ୍ରୁ", "ମାର୍ଚ୍ଚ", "ଏପ୍ରି", "ମେ", "ଜୁନ୍", "ଜୁଲାଇ", "ଅଗଷ୍ଟ", "ସେପ୍ଟେ", "ଅକ୍ଟୋ", "ନଭେ", "ଡିସେ"};
    public final String[] dayname = {"NA", "ରବିବାର", "ସୋମବାର", "ମଙ୍ଗଳବାର", "ବୁଧବାର", "ଗୁରୁବାର", "ଶୁକ୍ରବାର", "ଶନିବାର"};
    public final String[] choghadiaDayName = {"ଉଦ୍ବେଗ", "ଚଳ", "ଲାଭ", "ଅମୃତ", "କାଳ", "ଶୁଭ", "ରୋଗ"};
    public final String[] choghadiaNightName = {"ଶୁଭ", "ଅମୃତ", "ଚଳ", "ରୋଗ", "କାଳ", "ଲାଭ", "ଉଦ୍ବେଗ"};
    public final String[] exstring = {"କିଛି ନାହିଁ", "ଚନ୍ଦ୍ରୋଦୟ ନାହିଁ", "ଚନ୍ଦ୍ରାସ୍ତ ନାହିଁ", "(ଅଧିକ)"};
}
