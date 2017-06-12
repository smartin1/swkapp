package swk.application.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher.ViewFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import swk.application.activities.R;
/**
 * Nur zum TEST! f�r Bemerkungsfeld
 * @author endres
 *
 */
public class CameraFragment extends Fragment implements ViewFactory {
	// ---the images to display---
	// Integer[] imageIDs = {
	// R.drawable.connect,
	// R.drawable.disconnect,
	// R.drawable.familiekabel,
	// };

	ArrayList<Drawable> arrayList = new ArrayList<Drawable>();
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
	private ImageSwitcher imageSwitcher;
	private EditText editText;
	private Button cameraButton;
	private Button newButton;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		arrayList.add(getResources().getDrawable(R.drawable.connect));
		arrayList.add(getResources().getDrawable(R.drawable.disconnect));
		arrayList.add(getResources().getDrawable(R.drawable.familiekabel));
		final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_camera, container, false);
		cameraButton = (Button) linearLayout.findViewById(R.id.camerabutton);
		imageSwitcher = (ImageSwitcher) linearLayout
				.findViewById(R.id.switcher1);
		imageSwitcher.setFactory(this);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				getActivity(), android.R.anim.fade_in));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				getActivity(), android.R.anim.fade_out));
		editText = (EditText) linearLayout.findViewById(R.id.cameratext);
		cameraButton.setText("Bild hinzuf�gen");
		editText.setText("TEST");
		// button.setText("Camera starten");

		newButton = (Button) linearLayout.findViewById(R.id.cameranextbutton);
		newButton.setText("Erzeuge neue Notiz");
		final Gallery gallery = (Gallery) linearLayout
				.findViewById(R.id.gallery1);

		gallery.setAdapter(new ImageAdapter(getActivity()));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				imageSwitcher.setImageDrawable(arrayList.get(position));

			}
		});

		cameraButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				gallery.setAdapter(new ImageAdapter(getActivity()));
				imageSwitcher.invalidate();
				gallery.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {

						imageSwitcher.setImageDrawable(arrayList.get(position));
					}
				});

			}
		});
		return linearLayout;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {

				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				// convert byte array to Bitmap

				Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
						byteArray.length);

				arrayList.add(new BitmapDrawable(getResources(), bitmap));
				// imageView.setImageBitmap(bitmap);

			}
		}
	}

	@Override
	public View makeView() {
		ImageView imageView = new ImageView(getActivity());
		imageView.setBackgroundColor(0xFFFFFF);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		return imageView;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;

		public ImageAdapter(Context c) {
			context = c;

			// ---setting the style---
			TypedArray a = getActivity().obtainStyledAttributes(
					R.styleable.Gallery1);
			itemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		// ---returns the number of images---
		@Override
		public int getCount() {
			return arrayList.size();
		}

		// ---returns the ID of an item---
		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// ---returns an ImageView view---
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			imageView.setImageDrawable(arrayList.get(position));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
			imageView.setBackgroundColor(0xFFFFFF);
			// imageView.setBackgroundResource(itemBackground);
			return imageView;
		}
	}
}