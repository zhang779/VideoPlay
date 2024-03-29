package cn.appoa.aframework.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import cn.appoa.aframework.R;

/**
 * 自定义ImageView控件，实现了圆角和边框，以及按下变色
 */
public class SuperImageView extends ImageView {
    // paint when user press 图片按下的画笔
    private Paint pressPaint;
    // 图片的宽高
    private int width;
    private int height;

    // default bitmap config 定义 Bitmap 的默认配置
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    @SuppressWarnings("unused")
    private static final int COLORDRAWABLE_DIMENSION = 1;

    // border color 边框颜色
    private int borderColor;
    // width of border 边框宽度
    private int borderWidth;
    // alpha when pressed 按下的透明度
    private int pressAlpha;
    // color when pressed 按下的颜色
    private int pressColor;
    // radius 矩形圆角半径
    private int radius;
    // rectangle or round, 1 is circle, 2 is rectangle 图片类型1圆形2矩形
    private int shapeType;

    public SuperImageView(Context context) {
        super(context);
        init(context, null);
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // init the value 初始化默认值
        borderWidth = 0;
        borderColor = 0xddffffff;
        pressAlpha = 0x42;
        pressAlpha = 0;
        pressColor = 0x42000000;
        pressColor = 0x00000000;
        radius = 16;
        shapeType = 0;

        // get attribute of SuperImageView 获取控件的属性值
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SuperImageView);
            borderColor = array.getColor(R.styleable.SuperImageView_super_image_view_border_color, borderColor);
            borderWidth = array.getDimensionPixelOffset(R.styleable.SuperImageView_super_image_view_border_width,
                    borderWidth);
            pressAlpha = array.getInteger(R.styleable.SuperImageView_super_image_view_press_alpha, pressAlpha);
            pressColor = array.getColor(R.styleable.SuperImageView_super_image_view_press_color, pressColor);
            radius = array.getDimensionPixelOffset(R.styleable.SuperImageView_super_image_view_radius, radius);
            shapeType = array.getInteger(R.styleable.SuperImageView_super_image_view_shape_type, shapeType);
            array.recycle();
        }

        // set paint when pressed 按下的画笔设置
        pressPaint = new Paint();
        pressPaint.setAntiAlias(true);
        pressPaint.setStyle(Paint.Style.FILL);
        pressPaint.setColor(pressColor);
        pressPaint.setAlpha(0);
        pressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        setClickable(false);
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (shapeType == 0) {
            super.onDraw(canvas);
            return;
        }
        // 获取当前控件的 drawable
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        // the width and height is in xml file 这里 get 回来的宽度和高度是当前控件相对应的宽度和高度（在
        // xml 设置）
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        drawDrawable(canvas, bitmap);

        drawPress(canvas);
        drawBorder(canvas);
    }

    /**
     * draw Rounded Rectangle 实现圆角的绘制
     *
     * @param canvas
     * @param bitmap
     */
    private void drawDrawable(Canvas canvas, Bitmap bitmap) {
        // 画笔
        Paint paint = new Paint();
        // 颜色设置
        paint.setColor(0xffffffff);
        // 抗锯齿
        paint.setAntiAlias(true); // smooths out the edges of what is being
        // drawn
        // Paint 的 Xfermode，PorterDuff.Mode.SRC_IN 取两层图像的交集部门, 只显示上层图像。
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        // set flags 标志
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, width, height, null, saveFlags);

        if (shapeType == 1) {
            // 画遮罩，画出来就是一个和空间大小相匹配的圆（这里在半径上 -1 是为了不让图片超出边框）
            canvas.drawCircle(width / 2, height / 2, width / 2 - 1, paint);
        } else if (shapeType == 2) {
            // 当ShapeType == 2 时 图片为圆角矩形 （这里在宽高上 -1 是为了不让图片超出边框）
            RectF rectf = new RectF(1, 1, getWidth() - 1, getHeight() - 1);
            canvas.drawRoundRect(rectf, radius + 1, radius + 1, paint);
        }

        paint.setXfermode(xfermode);
        // 空间的大小 / bitmap 的大小 = bitmap 缩放的倍数
        float scaleWidth = ((float) getWidth()) / bitmap.getWidth();
        float scaleHeight = ((float) getHeight()) / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // bitmap scale bitmap 缩放
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        // draw 上去
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }

    /**
     * draw the effect when pressed 绘制控件的按下效果
     *
     * @param canvas
     */
    private void drawPress(Canvas canvas) {
        // check is rectangle or circle 这里根据类型判断绘制的效果是圆形还是矩形
        if (shapeType == 1) {
            // 当ShapeType == 1 时 图片为圆形 （这里在半径上 -1 是为了不让图片超出边框）
            canvas.drawCircle(width / 2, height / 2, width / 2 - 1, pressPaint);
        } else if (shapeType == 2) {
            // 当ShapeType == 2 时 图片为圆角矩形 （这里在宽高上 -1 是为了不让图片超出边框）
            RectF rectF = new RectF(1, 1, width - 1, height - 1);
            canvas.drawRoundRect(rectF, radius + 1, radius + 1, pressPaint);
        }
    }

    /**
     * draw customized border 绘制自定义控件边框
     *
     * @param canvas
     */
    private void drawBorder(Canvas canvas) {
        if (borderWidth > 0) {
            Paint paint = new Paint();
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setAntiAlias(true);
            // // check is rectangle or circle 根据控件类型的属性去绘制圆形或者矩形
            if (shapeType == 1) {
                canvas.drawCircle(width / 2, height / 2, (width - borderWidth) / 2, paint);
            } else if (shapeType == 2) {
                // 当ShapeType = 2 时 图片为圆角矩形
                RectF rectf = new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2,
                        getHeight() - borderWidth / 2);
                canvas.drawRoundRect(rectf, radius, radius, paint);
            }
        }
    }

    /**
     * monitor the size change 重写父类的 onSizeChanged 方法，检测控件宽高的变化
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    /**
     * monitor if touched 重写 onTouchEvent 监听方法，用来监听自定义控件是否被触摸
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressPaint.setAlpha(pressAlpha);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                pressPaint.setAlpha(0);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            default:
                pressPaint.setAlpha(0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * set border color 设置边框颜色
     *
     * @param borderColor
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    /**
     * set border width 设置边框宽度
     *
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * set alpha when pressed 设置图片按下颜色透明度
     *
     * @param pressAlpha
     */
    public void setPressAlpha(int pressAlpha) {
        this.pressAlpha = pressAlpha;
    }

    /**
     * set color when pressed 设置图片按下的颜色
     *
     * @param pressColor
     */
    public void setPressColor(int pressColor) {
        this.pressColor = pressColor;
    }

    /**
     * set radius 设置圆角半径
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    /**
     * set shape 设置形状类型
     *
     * @param shapeType
     */
    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
        invalidate();
    }
}
