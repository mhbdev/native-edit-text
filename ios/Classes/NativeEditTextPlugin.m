#import "NativeEditTextPlugin.h"
#if __has_include(<native_edit_text/native_edit_text-Swift.h>)
#import <native_edit_text/native_edit_text-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "native_edit_text-Swift.h"
#endif

@implementation NativeEditTextPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftNativeEditTextPlugin registerWithRegistrar:registrar];
}
@end
