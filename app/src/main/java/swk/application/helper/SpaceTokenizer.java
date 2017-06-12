package swk.application.helper;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

/*
 * Diese Klasse �ndert den Defaultumgang mit den Autovervollst�ndigungsfelder!
 * Sobald ein Item ausgew�hlt wurde wurde es eingef�gt und ein automatisch ein Komma angeh�ngt
 * sodass die Auswahl erweitert werden kann. Im Fall dieser App ist das aber unn�tig bzw falsch da nur
 * ein Datensatz ausgew�hlt werden soll--> Ersetzung von , durch NoSpace
 * 
 */

public class SpaceTokenizer implements Tokenizer {

	@Override
	public int findTokenStart(CharSequence text, int cursor) {
		int i = cursor;

		while (i > 0 && text.charAt(i - 1) != ' ') {
			i--;
		}
		while (i < cursor && text.charAt(i) == ' ') {
			i++;
		}

		return i;
	}

	@Override
	public int findTokenEnd(CharSequence text, int cursor) {
		int i = cursor;
		int len = text.length();

		while (i < len) {
			if (text.charAt(i) == ' ') {
				return i;
			} else {
				i++;
			}
		}

		return len;
	}

	@Override
	public CharSequence terminateToken(CharSequence text) {
		int i = text.length();

		while (i > 0 && text.charAt(i - 1) == ' ') {
			i--;
		}

		if (i > 0 && text.charAt(i - 1) == ' ') {
			return text;
		} else {
			if (text instanceof Spanned) {
				SpannableString sp = new SpannableString(text + " ");
				TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
						Object.class, sp, 0);
				return sp;
			} else {
				return text + " ";
			}
		}
	}
}
