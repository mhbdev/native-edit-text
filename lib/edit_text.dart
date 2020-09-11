import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';

typedef void EditTextCreatedCallback(EditTextController controller);

class EditText extends StatefulWidget {
  const EditText({
    Key key,
    @required this.onEditTextCreated,
  }) : super(key: key);

  final EditTextCreatedCallback onEditTextCreated;

  @override
  State<StatefulWidget> createState() => _EditTextState();
}

class _EditTextState extends State<EditText> {
  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
          viewType: 'net.apexteam.nativeedittext/edittext',
          onPlatformViewCreated: _onPlatformViewCreated,
          creationParamsCodec: const StandardMessageCodec());
    }
    return Text(
        '$defaultTargetPlatform is not yet supported by the NativeEditText plugin');
  }

  void _onPlatformViewCreated(int id) {
    if (widget.onEditTextCreated == null) {
      return;
    }
    widget.onEditTextCreated(new EditTextController._(id));
  }
}

class EditTextController {
  EditTextController._(int id)
      : _channel = MethodChannel('net.apexteam.nativeedittext/edittext_$id');

  final MethodChannel _channel;

  Future<void> setText(String text) async {
    assert(text != null);
    return _channel.invokeMethod('setText', text);
  }

  Future<String> getText() async {
    return _channel.invokeMethod('getText');
  }
}
