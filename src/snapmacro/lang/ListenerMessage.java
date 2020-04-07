package snapmacro.lang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ListenerMessage {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void showStreamMessage(StreamListener listener, String message){
        if(listener != null){
            listener.getStreamListener(message);
        }
    }

    public static void showDebugMessage(DebuggerListener listener, String message, DebugType type){
        if(listener != null){
            Date date = new Date();
            String datedMessage = dateFormat.format(date) + ":\t" + message + "\n";
            listener.getDebugMessages(datedMessage, type);
        }
    }
}
