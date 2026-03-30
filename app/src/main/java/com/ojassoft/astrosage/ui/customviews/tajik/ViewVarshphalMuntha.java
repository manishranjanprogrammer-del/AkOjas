package com.ojassoft.astrosage.ui.customviews.tajik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewVarshphalMuntha extends View {

    String title;
    String munthaInBhava;
    CScreenConstants SCREEN_CONSTANTS;
    int languageCode;
    int y1, xLength;
    float xCoordinate;
    int startX = 10;
    //float topHeadingY;
    int munthaBhav = 0;

    public ViewVarshphalMuntha(Context context, String title, String munthaInBhava, CScreenConstants SCREEN_CONSTANTS, int languageCode) {
        super(context);
        this.title = title;
        this.munthaInBhava = munthaInBhava;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        this.languageCode = languageCode;
//		topHeadingY = SCREEN_CONSTANTS.DeviceScreenHeight/17;
        initVariables();

    }

    private void initVariables() {
        y1 = (int) (this.SCREEN_CONSTANTS.DeviceScreenHeight / 18);
        xLength = (int) (this.SCREEN_CONSTANTS.DeviceScreenWidth / 4);
        xCoordinate = startX + (1.3f) * xLength;
        munthaBhav = Integer.valueOf(this.munthaInBhava.trim());
        munthaBhav--;
    }

    private void showMuntha(Canvas canvas) {
        if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH)
            canvas.drawRect(3, y1 * 2 - CGlobalVariables.SMALL_DEVICE_TOP, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 * 2 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.TableRow_Background_Color);
        else {
            canvas.drawRect(3, y1 * 2 - CGlobalVariables.TOP, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 * 2 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.TableRow_Background_Color);
        }
        int xPos = (int) ((canvas.getWidth() / 2));
        canvas.drawText(this.title + " " + this.munthaInBhava, xPos, y1 * 2, SCREEN_CONSTANTS.CenterHeadingColor);

//		canvas.drawText(this.title+" "+this.munthaInBhava , xCoordinate, y1*2,SCREEN_CONSTANTS.NewHeadingColor);
        //==================Print Muntha Prediction===============================
        RectF rect = new RectF(startX, y1 * 3, SCREEN_CONSTANTS.NewDeviceScreenWidth, SCREEN_CONSTANTS.DeviceScreenHeight);
        StaticLayout sl = null;
        if (languageCode == CGlobalVariables.ENGLISH)
            sl = new StaticLayout(getMunthaPredictionInEnglish(munthaBhav), new TextPaint(SCREEN_CONSTANTS.TextColor), (int) rect.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        else if (languageCode == CGlobalVariables.HINDI)
            sl = new StaticLayout(getMunthaPredictionInHindi(munthaBhav), new TextPaint(SCREEN_CONSTANTS.TextColor), (int) rect.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(rect.left, rect.top);
        sl.draw(canvas);
        canvas.restore();
        //==================Print Muntha Prediction===============================
    }

    private String getMunthaPredictionInEnglish(int munthaBhav) {
        String[] munthaPredictionEnglish = {"This is a time of action for you. Unexpected gifts and gains will pour in from different sectors for you.  It gives the native career betterment and all round prosperity. Your opponents will not dare to interrupt your way and you will get your share of attraction and reputation.  You will get favor from rules, superiors and higher authorities. You will have a sound health and physique. This year also indicates gain of vehicle.",
                "The native gets exceptional gains and wealth from the very beginning. This may be by the way of lottery, speculation, and in shares etc. Friends and well-wishers might support and cooperate with you in all your dealings. You will earn good money through business dealings. You will gain position and status. You will be well respected and enjoys good meals. ",
                "The rivals and opponents will not dare to face the native.  Legal battles will be in your favor.  You will enjoy name, fame, money and success in financial matters.  Good support from brothers and relatives is on the cards. You will be visiting religious places and get help from people. You will get success for your endeavors and efforts.",
                "Some ups and downs in money matters and position are on the cards for Muntha. There may be heavy financial losses and loss of property.  Money matters should be taken care of.  Keep your temper in check from getting into embarrassing situations as there are chances of disputes with close associates and relatives.  You need to keep a check on your health because sickness in on the card. ",
                "This a good position for Muntha. Your intelligence will help you earn accolades from various quarters of life. You might shine brightly in profession and business. Birth of a child in the family will bring happiness and joy for you.  This period is characterized by wisdom and religious learning for you. You will have visit to the religious places or the places of recreation. You will get honor and appreciate from rules and higher authorities.",
                "It would be advisable to maintain cordial relations with relatives. A health check is necessary. A prolonged illness is speculated. Your enemies will leave no stone unturned to harm you, so better to keep safe distance from them. Family members health may disturb your peace of mind. Financial condition may not be good and you may get into debt. There can be expenditure and loss due to thieves and disputes. There will be disputes and disagreements with the authorities.",
                "You may suffer due to health complications of your spouse. You will find it difficult to adjust with your associates and seniors.  Progeny related problems are on the cards. The native faces hardships in every sphere of life. Silly quarrels, misunderstanding and arguments in love life or married life are seen. There will be disagreement with your life-partner and family members. Health problems are also on the cards. Mental control is very much required during this period, as you may desire to do something unethical.",
                "You may hear news of the death of a close relative or family member. You are advised to take proper care of yourself as there are chances of suffering from diseases. There is a loss of wealth, loss of confidence, fruitless and mental worries. People jealousy for you can cause problems. Chances of financial loss due to theft are also there. You may also indulge in bad company and bad habits. ",
                "The position of Muntha is very favorable to you. Just let go and enjoy the happiness that comes your way. At last you can relax and enjoy the success and the results of the hard work you had been doing for a long time. This period will also bring you in the midst of famous people. Gain from foreign lands will build your status. Gains from superiors and higher authorities are also indicated. You will get happiness from the life partner and children. There will be religious ceremony at home and name, fame and luck due to that.",
                "You will enjoy all the prosperity and comfort. This is best position for Muntha where there is fulfillment of desires and you lead a contented life. Your fame and reputation will be on an increase. You will get promotion or improvement in status. You will be favored by ministers and government. You will help relatives and society. ",
                "You are learning new ways of maintaining harmony in your individuality at work and around friends and family. You will be benefitted by friends and your brother. You gain from royal favors or favors from higher authorities. Changes you experience in your life will be deeply felt and lasting. You will maintain sound health. Your wishes will be fulfilled. ",
                "You may get indulge into sudden losses financially. Failure in attempts will make you feel frustrated. You will have to slog as the work burden will be too much. There is displacement, transfer and trouble in foreign lands. There are chances of falling into bad companies so, better aware of that. Your health will be weak and you will be caught by many diseases. Your social reputation can also be hampered. There will be disputes with good people in the society."
        };

        return munthaPredictionEnglish[munthaBhav];
    }

    private String getMunthaPredictionInHindi(int munthaBhav) {
        String[] munthaPredictionHindi = {";g ,d vuqdwy fLFkfr gSA vkidks csgrj dWfjvj laHkkouk,a vkSj prqeqZ[k mUufr ds volj feysaxsA vius fojksf/k;ksa dks ijkLr djus esa vki l{ke gksaxsA vkidk eku c<+sxkA vkidks csgrj LokLF; izkIr gksxk vkSj vkfFkZd n`f\"V ls vki vf/kd le`) gksaxsA",
                "vkdfLed /ku izkfIr dk ;ksx gSA ;g /ku ykWVjh] ’ks;j esa lV~Vsckth vkfn ek/;eksa ls izkIRk gks ldrk gSA fofHkUu O;olkf;d lkSnksa ls vki vf/kd /ku izkIr djus esa l{ke gksaxsA iqjkus ?kj dh [kjhn fcŘh ls Hkh vkidks /ku feysxkA eku lEeku dh izkfIr gksxh] csgrj Hkkstu dk vkuan ysaxs vkSj iz’kklfud lg;ksx izkIr djsaxsA",
                "vki vius fojksf/k;ksa vkSj 'k=qvksa dks ijkLr djus esa l{ke gksaxsA dkuwuh yM+kb;ksa esa fot; feysxhA vkidks csgrj LokLF; dk ykHk feysxk vkSj uke o nke nksuksa dk vkuan ysaxsA vkids u;s fe= cusaxs vkSj vkidks Hkkb;ksa o cguksa dk lg;ksx izkIr gksxkA",
                "eqaFkk ds fy, ;g fLFkfr vuqdwy ugha gSA ;g o\"kZ vkids fy, ijs’kkfu;ksa Hkjk jgsxkA ekrk dh otg ls vki rukoxzLr jgsaxsA vius ?kfu\"B lg;ksfx;ksa o fj’rsnkjksa ls fookn dh laHkkouk gSA in [kksus vFkok ?kVus dh Hkh laHkkouk,a gSaA",
                "eqaFkk ds fy, ;g ,d vuqdwy fLFkfr gSA vki dqN lkekftd o iquhr dk;ksZa esa Hkkx ysaxsA CkPps ds tUe vFkok cPpksa dh otg ls [kqf’k;kWa izkIr gksaxhA dWfjvj dh n`f\"V ls le; csgrj gSA lEeku dh izkfIr gksxhA Ĺaps inksa ij cSBs yksxksa ls vkidk laidZ c<sxkA",
                "LokLF; laca/kh vusd ijs’kkfu;kWa vkidks rukoxzLr djsaxhA dqN fudV fj’rsnkj xaHkhj :i ls chekj gks ldrs gSaA fojksf/k;ksa ls [krjk gSA laHko gS fd pksjh dh otg ls vkidks uqdlku gksA 'k=q dh fdlh xfrfo/kh dh otg ls pksV yxus dk Hkh Mj gSA vkids dk;ZLFky ij vkids fojksf/k;ksa dks iz’kklfud lg;ksx feyus dh otg ls vki ekufld ;a=.kk ds f’kdkj gks ldrs  gSaA",
                "vkids thou lkFkh dks dbZ LokLF; laca/kh ijs’kkfu;kˇ gksaxhA ekufld :i ls vR;f/kd rukoxzLr jgsaxs vkSj oSokfgd vFkok izsethou esa dqN vugksuh gks ldrh gSA [kpksZa esa c<ksRrjh gksxhA in [kksus dk [krjk jgsxk vkSj vkids ofj\"Bksa ds vkids fojks/k esa tkus dh Hkh laHkkouk gSA thou ds gj eksM+ ij vkidks fnDdrksa dk lkeuk djuk iM+ ldrk gSA",
                "vkids fy, ;g dfBu le; gSA e`R;q :ih jk{kl vkids fudV eaMjkrk jgsxkA dbZ chekfj;kWa gks ldrh gsSA fudV fj’rsnkj dh vkdfLed e`R;q Hkh laHko gSA nq?kZVuk dk ;ksx gSA /ku dh] vkRefo’okl dh gkfu gksxhA ekufld ruko jgsxkA vkidk LFkkukarj.k fiz;tuksa ls nwj fdlh vutkuh lh txg gks ldrk gSA",
                "eqaFkk dh ;g fLFkfr vkidks o\"kZ Hkj mUufr vkSj lQyrk iznku djsxhA vkidh bPNkvksa dh iwfrZ gksxhA ?kj esa mRlo lk ekgkSy jgsxkA thou lkFkh ls] cPpksa ls lq[k feysxk vkSj vkidk uke pkjksa fn’kk essa QSysxkA fons’kh tehu ls Hkh vk; laHko gSA",
                "vkidks dWfjvj esa csgrj volj feysxsA blds vykok tehu o okgu dh izkfIr gksxhA vkjke dh oLrqvksa dk miHkksx djsaxsA mPp inksa ls lg;ksx feysxk vkSj vkids in o eku esa c<ksRrjh gksxhA vkidh bPNkvksa dh iwfrZ gksxhA",
                "vki csgrj LokLF;] yksdfiz;rk] fe=rk o lq[kn ikfjokfjd thou dk vkuan ysaxsA vkidks mPp iz’kklfud vFkok jktdh; inksa ls lg;ksx feysxkA vkidh bPNkvksa dh iwfrZ gksxh vkSj vkidh leL;kvksa dk var gskxkA",
                "[kpksZ esa c<ksRrjh gksxh vkSj os viuh gnsa ikj dj tk,axsA vkfFkZd n`f\"V ls ijs’kkfu;ksa Hkjk le; gSA /ku gkfu o foRrh; nqxZerk dk lkeuk djuk iM+ ldrk gSA vki dqlaxfr esa dqN O;oluksa ds f’kdkj gks ldrs gSaA vkidks viuh ifjJe dk Qy ugha feysxkA in gkfu vFkok LFkkukarj.k dh Hkh laHkkouk gSA"
        };

        return munthaPredictionHindi[munthaBhav];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        printTopHeading(canvas);
        showMuntha(canvas);
    }

    private void printTopHeading(Canvas _canvas) {
        if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
            _canvas.drawRect(3, y1 - CGlobalVariables.SMALL_DEVICE_TOP - 2, this.SCREEN_CONSTANTS.DeviceScreenWidth, y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.TableHeading_Background_Color);
            _canvas.drawLine(3, y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.KundliLineColor);
        } else {
            _canvas.drawRect(3, y1 - CGlobalVariables.TOP - 2, this.SCREEN_CONSTANTS.DeviceScreenWidth, y1 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.TableHeading_Background_Color);
            _canvas.drawLine(3, y1 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.KundliLineColor);
        }
        int xPos = (int) ((_canvas.getWidth() / 2));
        _canvas.drawText(getResources().getString(R.string.munth_top_heading), xPos, y1, SCREEN_CONSTANTS.CenterHeadingColor);

    }

}
