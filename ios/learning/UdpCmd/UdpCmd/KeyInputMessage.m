#import "KeyInputMessage.h"

@implementation KeyInputMessage

- (NSString *)toString {
    return [NSString localizedStringWithFormat:@"TYPE=%d,KEY-CODE=%d,KEY-ACTION=%d", self.cmdType, _keyCode, _keyAction];
}

- (KeyInputMessage *)init:(int)keyCode action:(int)keyAction {
    self = [super init];
    self.cmdType = KEY;
    [self setKeyCode:keyCode];
    [self setKeyAction:keyAction];
    return self;
}
@end
