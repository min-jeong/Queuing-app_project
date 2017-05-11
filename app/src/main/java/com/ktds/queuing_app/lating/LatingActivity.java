package com.ktds.queuing_app.lating;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ktds.queuing_app.R;
import com.ktds.queuing_app.Util.HttpClient;
import com.ktds.queuing_app.Util.beacon.BeaconManagerStore;
import com.ktds.queuing_app.vo.QueuingVO;

public class LatingActivity extends ActionBarActivity {

    private LatingTask latingTask;

    private RatingBar ratingBar;
    private TextView tvRating;

    private Button btnSubmit;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lating);

        tvRating = (TextView) findViewById(R.id.tvRating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감.
        ratingBar.setStepSize((float) 0.5);
        // 처음보여줄때(색깔이 한개도없음) default 값은 0.
        ratingBar.setRating((float) 2.5);
        //true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능.
        ratingBar.setIsIndicator(false);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRating.setText(rating+"");
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(LatingActivity.this);
                dialog  .setTitle("평점 주기 알림")
                        .setMessage("평점 설정이 완료 되었습니다.\n 전송하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                QueuingVO queuingVO = BeaconManagerStore.getInstance().getQueuingVO();
                                // TO-DO : branchId, regid , rating
                                latingTask = new LatingTask();
                                latingTask.execute(queuingVO.getBranchId(), queuingVO.getRegId(), queuingVO.getToken(), tvRating.getText().toString());
                                Toast.makeText(LatingActivity.this, "전송이 완료됬습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LatingActivity.this, "전송을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LatingActivity.this, "앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent("xyz"));
                finish();
            }
        });
    }

    public class LatingTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String branchId = params[0];
            String regId = params[1];
            String token = params[2];
            String starPoint = params[3];

            // HTTP 요청 준비 작업
            HttpClient.Builder client = new HttpClient.Builder("POST", "http://172.20.10.5:8080/Queuing-web/m/setStarPoint");

            // 파라미터를 전송한다.
            client.addOrReplaceParameter("branchId", branchId);
            client.addOrReplaceParameter("regId", regId);
            client.addOrReplaceParameter("token", token);
            client.addOrReplaceParameter("starPoint", starPoint);

            // HTTP 요청 전송
            HttpClient post = client.create();
            post.request();

            return post.getBody();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "설문조사를 해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
            sendBroadcast(new Intent("xyz"));
            finish();

        }
    }

}