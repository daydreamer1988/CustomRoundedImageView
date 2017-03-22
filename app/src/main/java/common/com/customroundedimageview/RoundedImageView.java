package common.com.customroundedimageview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Austin on 2017/3/22.
 */

public class RoundedImageView extends ImageView {

    private Paint mPaint;

    private Paint mBorderPaint;

    private BitmapShader mShader;

    private Matrix mMatrix;

    private RectF mRectF;

    private RectF mBorderRectf;

    private Drawable mDrawable;


    private float mBorderWidth = -1;

    private int mBorderColor;
    private static final String DEFAULT_BORDER_COLOR = "#FF444444";

    private float mCornerRadius;
    private static final float DEFAULT_CORNER_RADIUS = Resources.getSystem().getDisplayMetrics().density * 10;



    private int mType;

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUNDED = 1;

    private static final int DEFAULT_TYPE = TYPE_ROUNDED;

    private int mBgColor = -1;

    private int mCircleRadius;

    /**
     * 边框不会完全将背景覆盖，需要一定的内边距
     */
    private float paddingOffset = 4;


    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mDrawable = getDrawable();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mMatrix = new Matrix();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView);

        mType = typedArray.getInt(R.styleable.RoundedImageView_type, DEFAULT_TYPE);

        mCornerRadius = typedArray.getDimension(R.styleable.RoundedImageView_cornerRadius, DEFAULT_CORNER_RADIUS);

        mBorderWidth = typedArray.getDimension(R.styleable.RoundedImageView_borderWidth, -1);

        mBorderColor = typedArray.getColor(R.styleable.RoundedImageView_borderColor, Color.parseColor(DEFAULT_BORDER_COLOR));

        if (mBorderWidth != -1) {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            mBorderPaint.setColor(mBorderColor);
        }

        typedArray.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mType == TYPE_CIRCLE) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int min = Math.min(widthSize, heightSize);
            setMeasuredDimension(min, min);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(mDrawable==null) return;
        setUpShader();

        if(mBgColor!=-1){
            mPaint.setColor(mBgColor);
        }

        if (mType == TYPE_CIRCLE) {
            canvas.drawCircle(getWidth()/2, getHeight()/2, mCircleRadius, mPaint);
            if (mBorderWidth != -1) {
                canvas.drawCircle(getWidth()/2, getHeight()/2, mCircleRadius-mBorderWidth/2, mBorderPaint);
            }
        }else if(mType == TYPE_ROUNDED){
            canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mPaint);
            if (mBorderWidth != -1) {
                canvas.drawRoundRect(mBorderRectf, mCornerRadius-mBorderWidth/2, mCornerRadius-mBorderWidth/2, mBorderPaint);
            }
        }

    }

    private void setUpShader() {
        Bitmap bitmap = drawableToBitmap(mDrawable);

        if(bitmap==null) return;

        float scale = 0f;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float widthScale = getWidth()*1.0f / width;
        float heightScale = getHeight()*1.0f / height;

        scale = Math.max(widthScale, heightScale);


        //这种方法不行？
      /*  mMatrix.preTranslate(-width/2, -height/2);

        mMatrix.setScale(scale, scale);

        mMatrix.postTranslate(getWidth()/2, getHeight()/2);
*/

        mMatrix.setScale(scale, scale);

        if (scale == widthScale) {
            mMatrix.postTranslate(0,  -(height*widthScale-getHeight())/2);
        } else if (scale == heightScale){
            mMatrix.postTranslate(-(width * heightScale - getWidth()) / 2, 0);
        }

        mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mShader.setLocalMatrix(mMatrix);

        mPaint.setShader(mShader);
    }

    private Bitmap drawableToBitmap(Drawable mDrawable) {
        if(mDrawable instanceof BitmapDrawable){
            BitmapDrawable bitmap = (BitmapDrawable) mDrawable;
            return bitmap.getBitmap();
        }

        if (mDrawable instanceof ColorDrawable) {
            ColorDrawable bitmap = (ColorDrawable) mDrawable;
            mBgColor = bitmap.getColor();
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());

        mDrawable.draw(canvas);

        return bitmap;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0+ paddingOffset, 0+paddingOffset, w-paddingOffset, h-paddingOffset);
        mBorderRectf = new RectF(0+mBorderWidth/2, 0 + mBorderWidth/2, w-mBorderWidth/2, h-mBorderWidth/2);
        mCircleRadius = Math.min(w/2, h/2);
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
        invalidate();
    }




}


















