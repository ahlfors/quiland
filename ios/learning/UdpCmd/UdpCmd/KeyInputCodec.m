#import "KeyInputCodec.h"
#import "KeyInputMessage.h"

@implementation KeyInputCodec

- (NSData *)encode:(BaseMessage *)cmdMessage {
    if ([cmdMessage isKindOfClass:[KeyInputMessage class]]) {
        KeyInputMessage *keyMessage = (KeyInputMessage *) cmdMessage;
        NSMutableData *data = [[NSMutableData alloc] init];
        int code = NSSwapBigIntToHost(keyMessage.keyCode);
        [data appendBytes:&code length:sizeof(code)];
        int action = NSSwapBigIntToHost(keyMessage.keyAction);
        [data appendBytes:&action length:sizeof(action)];
        return data;
    } else {
        return NULL;
    }
}

- (BaseMessage *)decode:(NSData *)data {
    int code;
    [data getBytes:&code length:sizeof(code)];
    int code2 = NSSwapBigIntToHost(code);
    int action;
    NSRange range = NSMakeRange(4, 4);
    [data getBytes:&action range:range];
    int action2 = NSSwapBigIntToHost(action);
    KeyInputMessage *keyControlInfo = [[KeyInputMessage alloc] init:code2 action:action2];
    return keyControlInfo;
}
@end
