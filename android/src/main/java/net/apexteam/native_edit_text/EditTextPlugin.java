package net.apexteam.native_edit_text;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class EditTextPlugin implements PlatformView, MethodChannel.MethodCallHandler {

    private final EditText editText;
    private final MethodChannel methodChannel;

    public EditTextPlugin(Context context, BinaryMessenger messenger, int id) {
        editText = new EditText(context);
        methodChannel = new MethodChannel(messenger, "net.apexteam.nativeedittext/edittext_" + id);
        methodChannel.setMethodCallHandler(this);
    }

    public static void registerWith(@NonNull FlutterEngine engine) {
        engine
                .getPlatformViewsController()
                .getRegistry()
                .registerViewFactory(
                        "net.apexteam.nativeedittext/edittext", new EditTextFactory(engine.getDartExecutor().getBinaryMessenger()));
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "setText":
                setText(call, result);
                break;
            case "getText":
                getText(result);
                break;
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            default:
                result.notImplemented();
        }

    }

    private void setText(@NonNull MethodCall methodCall, @NonNull MethodChannel.Result result) {
        String text = (String) methodCall.arguments;
        editText.setText(text);
        editText.setSelection(text.length());
        result.success(null);
    }

    private void getText(@NonNull MethodChannel.Result result) {
        String text = editText.getText().toString();
        result.success(text);
    }

    @Override
    public View getView() {
        return editText;
    }

    @Override
    public void dispose() { }
}