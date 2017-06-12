package swk.application.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ForbiddenScreen extends Activity {
	// Splash screen timer

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forbidden);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		TextView mail = (TextView) findViewById(R.id.forbidden_activity_mail);
		mail.setText(Html
				.fromHtml("<a href=\"mailto:m.endres@stadtwerke-konstanz.de\">M. Endres<br/></a>"));
		mail.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
