package skinConditionsTracker.View;

import skinConditionsTracker.Model.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastCreator {
	private Context context;
	
	/**
	 * A function that takes in a string and displays this
	 * string in a toast. Whenever information is needed to be
	 * displayed we create a string and use this method to give
	 * the user feedback.
	 * 
	 * @param string
	 */
	public ToastCreator(Context ctx) {
        this.context = ctx;
    }

	public void toaster(String string) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		
		View layout = (ViewGroup) inflater.inflate(R.layout.toast_layout, null);

		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		image.setImageResource(R.drawable.info_notice);
		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText(string);

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();	
	}
}
