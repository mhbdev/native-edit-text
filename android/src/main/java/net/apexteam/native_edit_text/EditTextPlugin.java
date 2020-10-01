package net.apexteam.native_edit_text;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.view.FlutterMain;

public class EditTextPlugin implements PlatformView, MethodChannel.MethodCallHandler, EventChannel.StreamHandler {

    private final EditText editText;
    private final MethodChannel methodChannel;
    private final Context mContext;
    private final EventChannel textChangeEventChannel;
    TextWatcher textWatcher;


    public EditTextPlugin(Context context, BinaryMessenger messenger, int id) {
        mContext = context;
        editText = new EditText(context);
        InputMethodManager mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        textChangeEventChannel = new EventChannel(messenger, "net.apexteam.nativeedittext/edittext_" + id + "/text_change");
        textChangeEventChannel.setStreamHandler(this);
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

        });
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

    private void setFont(MethodCall methodCall, MethodChannel.Result result) {
        String asset = (String) methodCall.arguments;
        final Typeface t = Typeface.createFromAsset(mContext.getAssets(), FlutterMain.getLookupKeyForAsset(asset));
        editText.setTypeface(t);
        result.success(true);
    }

    @Override
    public View getView() {
        return editText;
    }

    @Override
    public void dispose() { }

    @Override
    public void onListen(Object arguments, final EventChannel.EventSink eventSink) {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                eventSink.success(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editText.addTextChangedListener(textWatcher);
    }

    @Override
    public void onCancel(Object arguments) {
        editText.removeTextChangedListener(textWatcher);
        textWatcher = null;
    }
}