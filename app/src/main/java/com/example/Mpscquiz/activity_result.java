package com.example.Mpscquiz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class activity_result extends AppCompatActivity implements View.OnClickListener {
    TextView correct, incorrect, attempted, score, you;
    int  incorr = 0, attempt = 0, scor = 0;
    private ImageView img;
    private TextView tvPerc;
    Button btn_generate;
    RelativeLayout ll_pdflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String corr = intent.getStringExtra("result");
        int cor = Integer.parseInt(corr);

        attempt = intent.getIntExtra("attempt", 0);
        attempt-=1;
        incorr = attempt - cor;
        scor = 2 * cor;
        correct =  findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        attempted = findViewById(R.id.attempted);
        score =  findViewById(R.id.score);
        you = findViewById(R.id.you);
        btn_generate = findViewById(R.id.btn_generate);
        ll_pdflayout =  findViewById(R.id.relativeLayout3);

        fn_permission();

        attempted.setText("  " + attempt);
        correct.setText("  " + corr);
        incorrect.setText("  " + incorr);
        score.setText("Score :   " + scor);
        if (scor <= 10)
            you.setText("You Need Improvement");
        else if (scor <= 20)
            you.setText("You are an average Quizzer");
        else
            you.setText("You are a brilliant  Quizzer ");

        img = findViewById(R.id.imageView);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        rotate.setRepeatCount(1);
        img.startAnimation(rotate);
        double perc = scor*3.33;
        DecimalFormat df = new DecimalFormat("##.###");
        tvPerc =  findViewById(R.id.tvPerc);
        tvPerc.setText(""+df.format(perc)+" %");

        btn_generate.setOnClickListener(this);

    }
    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(activity_result.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(activity_result.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
            if ((ActivityCompat.shouldShowRequestPermissionRationale(activity_result.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(activity_result.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generate:

                if (boolean_save) {
                    Intent intent = new Intent(getApplicationContext(), PDFViewActivity.class);
                    startActivity(intent);

                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(activity_result.this);
                        progressDialog.setMessage("Please wait");
                        bitmap = loadBitmapFromView(ll_pdflayout, ll_pdflayout.getWidth(), ll_pdflayout.getHeight());
                        createPdf();
                    } else {

                    }

                    createPdf();
                    break;
                }
        }
    }
    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
        // paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , paint);
        document.finishPage(page);

        // write the document content
        try {
            File dir=new File("/sdcard/Mpsc_Quiz");
            if(!dir.exists())
                dir.mkdirs();
            File filePath = new File(dir,"/result.pdf");
            if(! filePath.exists())
                filePath.createNewFile();
            document.writeTo(new FileOutputStream(filePath));
            btn_generate.setText("Check PDF");
            boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }finally {
            // close the document
            document.close();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,}, REQUEST_PERMISSIONS);
        }

    }
}

