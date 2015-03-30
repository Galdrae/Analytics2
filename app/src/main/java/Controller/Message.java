package Controller;

import android.widget.Toast;
import android.content.Context;

/**
 * Created by Deo's Friend on 3/13/2015.
 */
public class Message {
    public static void message(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
