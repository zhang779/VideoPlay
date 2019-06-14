package cn.appoa.aframework.utils;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.volley.VolleyError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class AtyUtils {

    public static boolean isDebug = true;

    /** Log打印 */
    public static void i(String tag, String msg) {
        if (isDebug) {
            if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0)
                return;
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.i(tag, msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }
                Log.i(tag, msg);// 打印剩余日志
            }
        }
    }

    /** 打印错误信息 */
    public static void e(String tag, VolleyError error) {
        if (error != null) {
            if (error.toString() != null)
                i(tag, error.toString());
            if (error.getMessage() != null)
                i(tag, error.getMessage());
            if (error.networkResponse != null && error.networkResponse.data != null)
                i(tag, new String(error.networkResponse.data));
        }
    }

    /** dp转px */
    public static int dip2px(Context context, float dipValue) {
        if (context == null)
            return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /** 保留两位小数 */
    public static String get2Point(double point) {
        String format = new DecimalFormat("#.00").format(point);
        if (format.startsWith(".")) {
            return "0" + format;
        }
        return format;
    }

    /** 设置焦点 */
    public static void setFocus(View view) {
        if (view != null) {
            view.requestFocus();
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
        }
    }

    /** Toast */
    private static Toast toast = null;

    /** 短时间Toast */
    public static void showShort(Context context, CharSequence message, boolean isCenter) {
        if (context == null)
            return;
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    /** 长时间Toast */
    public static void showLong(Context context, CharSequence message, boolean isCenter) {
        if (context == null)
            return;
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    /** 短时间Toast */
    public static void showShort(Context context, int resId, boolean isCenter) {
        if (context == null)
            return;
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    /** 长时间Toast */
    public static void showLong(Context context, int resId, boolean isCenter) {
        if (context == null)
            return;
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        } else {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    /** TextView是否为空 */
    public static boolean isTextEmpty(TextView textView) {
        if (textView != null) {
            String msg = textView.getText().toString().trim();
            return TextUtils.isEmpty(msg);
        }
        return true;
    }

    /** 获取TextView的字符串 */
    public static String getText(TextView textView) {
        String text = "";
        if (textView != null) {
            if (isTextEmpty(textView)) {
                text = "";
            } else {
                text = textView.getText().toString().trim();
            }
        }
        return text;
    }

    /** 两个TextView的字符串是否相等 */
    public static boolean isSameText(TextView textView1, TextView textView2) {
        if (textView1 != null && textView2 != null) {
            String msg1 = textView1.getText().toString().trim();
            String msg2 = textView2.getText().toString().trim();
            if (!TextUtils.isEmpty(msg1) && !TextUtils.isEmpty(msg2)) {
                return TextUtils.equals(msg1, msg2);
            }
        }
        return false;
    }

    /** 是否是手机号 */
    public static boolean isMobile(String phone) {
        boolean isMobile = false;
        if (!TextUtils.isEmpty(phone) && phone.length() == 11 && phone.startsWith("1")) {
            isMobile = true;
        }
        return isMobile;
    }

    /** 格式化手机号 */
    public static String formatMobile(String phone) {
        if (isMobile(phone)) {
            return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        } else {
            return phone;
        }
    }

    /** 拨打电话 */
    public static void callPhone(Activity activity, String phone) {
        if (activity == null)
            return;
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            activity.startActivity(intent);
        }
    }

    /** 发送短信 */
    public static void sendSms(Activity activity, String phone, String sms_body) {
        if (activity == null)
            return;
        if (!TextUtils.isEmpty(phone)) {
            Uri smsToUri = Uri.parse("smsto://" + phone);
            Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
            if (!TextUtils.isEmpty(sms_body)) {
                intent.putExtra("sms_body", sms_body);
            }
            activity.startActivity(intent);
        }
    }

    /** 打开浏览器 */
    public static void openBrowser(Activity mActivity, String url) {
        if (mActivity == null)
            return;
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            mActivity.startActivity(intent);
        }
    }

    /** 查询物流信息 */
    public static void searchCourier(Activity mActivity, String Courier, String CourierNO) {
        if (mActivity != null && !TextUtils.isEmpty(Courier) && !TextUtils.isEmpty(CourierNO)) {
            openBrowser(mActivity, "https://m.kuaidi100.com/index_all.html?type=" + Courier + "&postid=" + CourierNO);
        }
    }

    /** base64解密字符串 */
    public static String base64ToText(String base64, boolean isFromHtml) {
        Spanned text = null;
        if (!TextUtils.isEmpty(base64)) {
            String source = new String(Base64.decode(base64.getBytes(), Base64.DEFAULT));
            if (isFromHtml) {
                text = Html.fromHtml(source);
            } else {
                text = new SpannableString(source);
            }
        }
        if (text == null) {
            return base64;
        } else {
            return text.toString();
        }
    }

    /** 设置未读消息数 */
    public static void setUnReadCount(int count, TextView tv) {
        if (tv == null) {
            return;
        }
        if (count <= 0) {
            tv.setVisibility(View.INVISIBLE);
            tv.setText(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            if (count > 99) {
                tv.setText("99+");
            } else {
                tv.setText(count + "");
            }
        }
    }

    /** 获得屏幕高度 */
    public static int getScreenHeight(Context context) {
        if (context == null)
            return 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /** 获得屏幕宽度 */
    public static int getScreenWidth(Context context) {
        if (context == null)
            return 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /** 获得状态栏的高度 */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        if (context != null) {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
                statusHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /** 弹出键盘 */
    public static void showSoftInput(Context context) {
        if (context == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /** 隐藏键盘 */
    public static void closeSoftInput(Context context) {
        if (context == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 输入法不遮挡界面 */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        if (root != null && scrollToView != null) {
            root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect rect = new Rect();
                    // 获取root在窗体的可视区域
                    root.getWindowVisibleDisplayFrame(rect);
                    // 获取root在窗体的不可视区域高�?被其他View遮挡的区域高�?
                    int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                    // 若不可视区域高度大于100，则键盘显示
                    if (rootInvisibleHeight > 100) {
                        int[] location = new int[2];
                        // 获取scrollToView在窗体的坐标
                        scrollToView.getLocationInWindow(location);
                        // 计算root滚动高度，使scrollToView在可见区域的底部
                        int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                        root.scrollTo(0, srollHeight);
                    } else {
                        // 键盘隐藏
                        root.scrollTo(0, 0);
                    }
                }
            });
        }
    }

    /** 清除全部缓存 */
    public static void clearAllCache(Context context) {
        if (context == null)
            return;
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /** 删除文件夹内文件 */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /** 计算缓存大小 */
    public static String getTotalCacheSize(Context context) throws Exception {
        if (context == null)
            return "";
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /** 获取文件大小 */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        if (file != null) {
            try {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /** 格式化单位 */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /** app是否在前台 */
    @SuppressWarnings({ "deprecation" })
    public static boolean isAppRunningForeground(Context context) {
        if (context == null)
            return false;
        ActivityManager localActivityManager = (ActivityManager) context.getSystemService("activity");
        try {
            List<RunningTaskInfo> localList = localActivityManager.getRunningTasks(1);
            if ((localList == null) || (localList.size() < 1))
                return false;
            boolean bool = context.getPackageName().equalsIgnoreCase(
                    ((ActivityManager.RunningTaskInfo) localList.get(0)).baseActivity.getPackageName());
            i("isAppRunningForeground", "app running in foregroud：" + (bool));
            return bool;
        } catch (SecurityException e) {
            i("isAppRunningForeground", "Apk doesn't hold GET_TASKS permission");
            e.printStackTrace();
        }
        return false;
    }

    /** 获取版本号 */
    public static int getVersionCode(Context cxt) {
        if (cxt == null)
            return 0;
        // 获取packagemanager的实例
        PackageManager packageManager = cxt.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        int version = -1;
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(cxt.getPackageName(), 0);
            version = packInfo.versionCode;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /** 获取版本名 */
    public static String getVersionName(Context cxt) {
        if (cxt == null)
            return "";
        // 获取packagemanager的实例
        PackageManager packageManager = cxt.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        String version = "";
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(cxt.getPackageName(), 0);
            version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /** 检测是否安装 */
    public static boolean checkApkExist(Context context, String packageName) {
        if (context == null)
            return false;
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            @SuppressWarnings("unused")
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 分享图片到微信
     *
     * @param context
     * @param isTimeLine
     *            是否分享到朋友圈
     * @param imageUris
     *            图片uri
     * @param text
     *            文字
     */
    public static void sendImageToWx(Context context, boolean isTimeLine, ArrayList<Uri> imageUris, String text) {
        if (context != null) {
            String packageName = "com.tencent.mm";
            if (checkApkExist(context, packageName)) {
                Intent intent = new Intent();
                ComponentName comp = null;
                if (isTimeLine) {
                    comp = new ComponentName(packageName, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                } else {
                    comp = new ComponentName(packageName, "com.tencent.mm.ui.tools.ShareImgUI");
                }
                if (comp != null) {
                    intent.setComponent(comp);
                }
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.putExtra("Kdescription", text);
                context.startActivity(intent);
            } else {
                AtyUtils.showShort(context, "未安装微信，请先下载安装微信", true);
            }
        }
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /** 年月日补0 */
    @SuppressLint("DefaultLocale")
    public static String formatInt(int value) {
        String result = value + "";
        if (value < 100) {
            result = String.format("%02d", value);
        }
        return result;
    }

    /** 格式化日期 */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatData(String time) {
        String formatData = "";
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date endDate = myFormatter.parse(time.toString());

            formatData = myFormatter1.format(endDate);
            return formatData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatData;
    }

    /** 弹跳动画 */
    public static void startTranslateAnim(View v, AnimationListener listener) {
        if (v == null) {
            return;
        }
        AnimationSet animup = new AnimationSet(true);
        TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.4f);
        mytranslateanimup0.setDuration(100);
        TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.4f);
        mytranslateanimup1.setDuration(100);
        mytranslateanimup1.setStartOffset(100);
        mytranslateanimup1.setAnimationListener(listener);
        animup.addAnimation(mytranslateanimup0);
        animup.addAnimation(mytranslateanimup1);
        v.startAnimation(animup);
    }

    /**
     * 交换list的两个元素
     *
     * @param list
     * @param oldPosition
     * @param newPosition
     */
    public static <T> void swaList(List<T> list, int oldPosition, int newPosition) {
        if (list != null && list.size() > 0 //
                && oldPosition >= 0 && oldPosition < list.size()//
                && newPosition >= 0 && newPosition < list.size()//
                && oldPosition != newPosition) {
            // 向前移动，前面的元素需要向后移动
            if (oldPosition < newPosition) {
                for (int i = oldPosition; i < newPosition; i++) {
                    Collections.swap(list, i, i + 1);
                }
            }
            // 向后移动，后面的元素需要向前移动
            if (oldPosition > newPosition) {
                for (int i = oldPosition; i > newPosition; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
        }
    }

    /**
     * 按页数分割list
     *
     * @param list
     * @param pageSize
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        List<List<T>> listArray = new ArrayList<List<T>>();
        List<T> subList = null;
        for (int i = 0; i < list.size(); i++) {
            if (i % pageSize == 0) {// 每次到达页大小的边界就重新申请一个subList
                subList = new ArrayList<T>();
                listArray.add(subList);
            }
            subList.add(list.get(i));
        }
        return listArray;
    }

    /**
     * 通知媒体库更新文件
     *
     * @param context
     * @param filePath
     *            文件全路径
     */
    public static void scanFile(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath) || //
                new File(filePath) == null || !new File(filePath).exists()) {
            return;
        }
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }

    /**
     * 指定关键词变色
     *
     * @param color
     *            关键词颜色
     * @param text
     *            文本
     * @param keyword
     *            关键词
     * @return
     */
    public static SpannableString matcherSearchKey(int color, String text, String keyword) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(keyword)) {
            return null;
        }
        String string = text.toLowerCase();
        String key = keyword.toLowerCase();

        Pattern pattern = Pattern.compile(key);
        Matcher matcher = pattern.matcher(string);

        SpannableString ss = new SpannableString(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
}
