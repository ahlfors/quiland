#import "MouseInputCodec.h"
#import "MouseInputMessage.h"

@implementation MouseInputCodec

- (NSData *)encode:(BaseMessage *)cmdMessage {
    if ([cmdMessage isKindOfClass:[MouseInputMessage class]]) {
        MouseInputMessage *mouseMessage = (MouseInputMessage *) cmdMessage;
        NSMutableData *data = [[NSMutableData alloc] init];
        int action = NSSwapBigIntToHost(mouseMessage.mouseAction);
        [data appendBytes:&action length:sizeof(action)];
        int arg1 = NSSwapBigIntToHost(mouseMessage.arg1);
        [data appendBytes:&arg1 length:sizeof(arg1)];
        int arg2 = NSSwapBigIntToHost(mouseMessage.arg2);
        [data appendBytes:&arg2 length:sizeof(arg2)];
        return data;
    } else {
        return NULL;
    }
}

- (BaseMessage *)decode:(NSData *)data {
    int action;
    [data getBytes:&action length:sizeof(action)];
    int arg1;
    NSRange range = NSMakeRange(4, 4);
    [data getBytes:&arg1 range:range];
    int arg2;
    NSRange range2 = NSMakeRange(8, 4);
    [data getBytes:&arg2 range:range2];
    MouseInputMessage *mouseMessage = [[MouseInputMessage alloc] init:NSSwapBigIntToHost(action) arg1:NSSwapBigIntToHost(arg1) arg2:NSSwapBigIntToHost(arg2)];
    return mouseMessage;
}
@end
