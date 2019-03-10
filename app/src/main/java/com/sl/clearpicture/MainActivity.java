package com.sl.clearpicture;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.sl.clearpicture.adapter.ImageAdapter;
import com.sl.clearpicture.api.ConfigURL;
import com.sl.clearpicture.api.DataManager;
import com.sl.clearpicture.interfaces.ParsedNdefRecord;
import com.sl.clearpicture.model.ProductDetailResponse;
import com.sl.clearpicture.model.SurveyCallResponse;
import com.sl.clearpicture.model.TagVerificationResponse;
import com.sl.clearpicture.utils.JudiProgressDialog;
import com.sl.clearpicture.utils.MessageEvent;
import com.sl.clearpicture.utils.NdefMessageParser;
import com.sl.clearpicture.utils.Prefs;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private TextView text,tvVerification;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private ImageView ivStatusIcon;
    private TextView tvAuthenticity;
    private Button btnProdDetail,btnContinue;
    private JudiProgressDialog progressDialog;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.tvData);
        tvVerification = (TextView) findViewById(R.id.tvVerification);
        tvAuthenticity = findViewById(R.id.tvAuthenticity);
        nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        ivStatusIcon = findViewById(R.id.ivStatusIcon);
        btnProdDetail = findViewById(R.id.btnProdDetail);
        btnContinue = findViewById(R.id.btnContinue);
        progressDialog = new JudiProgressDialog(MainActivity.this);
        prefs = new Prefs(MainActivity.this);

        if (nfcAdapter == null) {
           tvVerification.setText("Your device is not supporting for NFC");
            tvVerification.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.colorRed));
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            showNfcSettings();


        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        btnContinue.setVisibility(View.GONE);
    }


    private void showNfcSettings(){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
        alertbox.setTitle("Info");
        alertbox.setCancelable(false);
        alertbox.setMessage("Please enable NFC");
        alertbox.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            }
        });

        alertbox.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showNfcSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = new NdefMessage[]{};

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            }
//            else {
//                byte[] empty = new byte[0];
//                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
//                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                byte[] payload = dumpTagData(tag).getBytes();
//                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
//                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
//                msgs = new NdefMessage[] {msg};
//            }

            displayMsgs(msgs);
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

        text.setText(builder.toString());
        Log.i("ndef-->",builder.toString());
        progressDialog.show();
        DataManager.getInstance().verifyTag(listener(),errorListener(),builder.toString().trim());
//        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//        startActivity(intent);
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        };
    }

    private Response.Listener<JSONObject> listener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ndef-->",response.toString());
                progressDialog.dismiss();
                Gson gson = new Gson();
                final TagVerificationResponse verificationResponse = gson.fromJson(response.toString(),TagVerificationResponse.class);
                if(verificationResponse != null) {
                    if(verificationResponse.getStatusCode() == 200) {
                        if(verificationResponse.getContent().getTitle().toLowerCase().contains("sorry")){
                            ivStatusIcon.setVisibility(View.INVISIBLE);
                            btnContinue.setVisibility(View.INVISIBLE);
                            btnProdDetail.setVisibility(View.INVISIBLE);
                        }else{
                            ivStatusIcon.setVisibility(View.VISIBLE);
                            btnProdDetail.setVisibility(View.VISIBLE);
                            ivStatusIcon.setImageResource(R.drawable.circle_success);
                            btnContinue.setVisibility(View.VISIBLE);
                        }

                        tvVerification.setText(verificationResponse.getContent().getTitle());
                        tvAuthenticity.setText(verificationResponse.getContent().getMessage());
                        prefs.setAuth(verificationResponse.getContent().getAuthCode());
                        btnContinue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressDialog.show();
                                DataManager.getInstance().callSurvey(surveyCallListener(),surveyCallErrorListener(),verificationResponse.getContent().getServerId());
                            }
                        });

                        if(verificationResponse.getContent().getProductId() != null && !verificationResponse.getContent().getProductId().isEmpty()){
                            btnProdDetail.setVisibility(View.VISIBLE);
                            btnProdDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    progressDialog.show();
                                    DataManager.getInstance().getProductDetails(productDetailSuccesListener(),productDetailErrorListener(),verificationResponse.getContent().getProductId());
                                }
                            });

                        }else{
                            btnProdDetail.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        };
    }

    private Response.ErrorListener productDetailErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        };
    }

    private Response.Listener<JSONObject> productDetailSuccesListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("details-->",response.toString());
                Gson gson = new Gson();
                ProductDetailResponse detailResponse = gson.fromJson(response.toString(),ProductDetailResponse.class);

                if(detailResponse != null){
                    if(detailResponse.getStatusCode() == 200){
                        String imageUrl = ConfigURL.PRODUCT_IMAGE.replace("{productId}",detailResponse.getProductContent().getId());
                        final DialogPlus dialog = DialogPlus.newDialog(MainActivity.this)
                                .setContentHolder(new ViewHolder(R.layout.content_product_detasils))
                                //.setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                                .create();
//                        ImageView ivProduct = (ImageView) dialog.findViewById(R.id.ivProduct);
                        ImageAdapter mAdapter = new ImageAdapter(MainActivity.this, detailResponse.getProductContent().getImageObjects());


                        ViewPager mPager = (ViewPager) dialog.findViewById(R.id.pager);
                        final String url = getVideoId(detailResponse.getProductContent().getVideoUrl());
                        mPager.setAdapter(mAdapter);

                        final CircleIndicator indicator = (CircleIndicator) dialog.findViewById(R.id.indicator);
                        indicator.setViewPager(mPager);

                        TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
                        TextView tvCode = (TextView) dialog.findViewById(R.id.tvCode);
                        TextView tvDesc = (TextView) dialog.findViewById(R.id.tvDesc);
                        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
                        TextView tvBacth = (TextView) dialog.findViewById(R.id.tvBatch);
                        TextView tvEcpire = (TextView) dialog.findViewById(R.id.tvEcpire);
                        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
                        Button btnYouTube = (Button) dialog.findViewById(R.id.btnYouTube);
                        ImageView ivClose  = (ImageView) dialog.findViewById(R.id.ivClose);
//                        Picasso.with(MainActivity.this)
//                                .load(imageUrl)
//                                .into(ivProduct);
                        tvName.setText(detailResponse.getProductContent().getName());
                        tvCode.setText(detailResponse.getProductContent().getCode());
                        tvDesc.setText(detailResponse.getProductContent().getDescription());
                        tvBacth.setText(""+detailResponse.getProductContent().getBatchNumber());
                        tvEcpire.setText(detailResponse.getProductContent().getExpireDate());
                        btnDone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        btnYouTube.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainActivity.this,YoutubePlayerActivity.class);
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        });
                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                }
            }
        };
    }
    private final static String expression = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})\\W  ";
    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group(1);
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }
    private Response.ErrorListener surveyCallErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        };
    }

    private Response.Listener<JSONObject> surveyCallListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("O-->",response.toString());
                progressDialog.dismiss();
                Gson gson = new Gson();
                SurveyCallResponse surveyCallResponse = gson.fromJson(response.toString(),SurveyCallResponse.class);
                if(surveyCallResponse != null) {
                    if(surveyCallResponse.getStatusCode() == 200) {
                        Intent intent = new Intent(MainActivity.this, SurveyIntermediateActivity.class);
                        intent.putExtra("survey", surveyCallResponse);
                        startActivity(intent);
                    }
                }
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageEvent event) {
        if (event.getResultCode() == MessageEvent.REFRESH_SCAN) {
            ivStatusIcon.setImageResource(R.drawable.circle_normal);
            tvVerification.setText("Ready to Scan");
            tvAuthenticity.setText("Hold Device Near The NFC Tag");
            btnProdDetail.setVisibility(View.INVISIBLE);
            btnContinue.setVisibility(View.INVISIBLE);

        EventBus.getDefault().removeStickyEvent(event);
        }
    }
}
