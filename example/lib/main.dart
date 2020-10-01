import 'package:flutter/material.dart';
import 'package:native_edit_text/native_edit_text.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  EditTextController _controller;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Builder(
          builder: (context) => Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Container(
                    width: MediaQuery.of(context).size.width,
                    height: 50,
                    decoration:
                        BoxDecoration(border: Border.all(color: Colors.red)),
                    child: Material(
                      child: TextField(
                        maxLines: 1,
                      ),
                    )
                    // EditText(
                    //   onEditTextCreated: (controller) {
                    //     setState(() {
                    //       _controller = controller;
                    //     });
                    //   },
                    // ),
                    ),
                FutureBuilder(
                  future: _controller?.getText(),
                  builder: (context, snapshot) => Text(snapshot.hasData
                      ? snapshot.data.toString()
                      : 'Loading...'),
                ),
                RaisedButton(
                  onPressed: () {
                    if (_controller != null) _controller.text = 'TESTIG';
                  },
                  child: Text('TEST'),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
