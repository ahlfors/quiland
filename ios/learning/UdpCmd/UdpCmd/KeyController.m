#import "KeyController.h"
#import "KeyInputMessage.h"

@implementation KeyController

- (void)process:(BaseMessage *)controlInfo {
    if ([controlInfo isKindOfClass:[KeyInputMessage class]]) {
        KeyInputMessage *cmdMessage = (KeyInputMessage *) controlInfo;
        NSLog(@"processing code=%d action=%d", cmdMessage.keyCode, cmdMessage.keyAction);
    }
}
@end
