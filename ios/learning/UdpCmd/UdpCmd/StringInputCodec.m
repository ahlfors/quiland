#import "StringInputCodec.h"
#import "StringInputMessage.h"

@implementation StringInputCodec

- (NSData *)encode:(BaseMessage *)cmdMessage {
    if ([cmdMessage isKindOfClass:[StringInputMessage class]]) {
        StringInputMessage *stringMessage = (StringInputMessage *) cmdMessage;
        NSData *data = [stringMessage.value dataUsingEncoding:NSUTF8StringEncoding];
        return data;
    } else {
        return NULL;
    }
}

- (BaseMessage *)decode:(NSData *)data {
    NSString *value = [NSString stringWithUTF8String:[data bytes]];
    StringInputMessage *stringMessage = [[StringInputMessage alloc] init:value];
    return stringMessage;
}


@end
