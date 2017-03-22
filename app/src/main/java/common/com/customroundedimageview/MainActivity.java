package common.com.customroundedimageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private RoundedImageView mIv1;
    private RoundedImageView mIv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mIv1 = (RoundedImageView) findViewById(R.id.iv1);
        mIv2 = (RoundedImageView) findViewById(R.id.iv2);

        mIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIv1.setType(Math.abs(mIv1.getType() - 1));
            }
        });

    }
}
