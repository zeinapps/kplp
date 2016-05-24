package zein.apps.imageloader;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

public class ImageBorder {
   

    public static Bitmap RoundedImg(Bitmap bitmap, float radius) {
	    if (bitmap == null || bitmap.isRecycled()) {
	        return null;
	    }
	   
	    Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	    BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
	            TileMode.CLAMP);
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(shader);

	    Canvas canvas = new Canvas(canvasBitmap);

	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            radius, paint);

	    return canvasBitmap;
	}
    
    public static Bitmap Border(Bitmap bitmap, float radius) {
    	
    	Bitmap bitmapRounded = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    	Canvas canvas = new Canvas(bitmapRounded);
    	Paint paint = new Paint();
    	paint.setAntiAlias(true);
    	paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    	canvas.drawRoundRect((new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight())), radius, radius, paint);


        return bitmapRounded;
         }
	

}