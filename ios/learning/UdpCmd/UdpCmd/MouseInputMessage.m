#import "MouseInputMessage.h"

@implementation MouseInputMessage
- (NSString *)toString {
    return [NSString localizedStringWithFormat:@"TYPE=%d,ARG1=%d,ARG2=%d", self.cmdType, _arg1, _arg2];
}

- (MouseInputMessage *)init:(int)action arg1:(int)arg1 arg2:(int)arg2 {
    self = [super init];
    self.cmdType = MOUSE;
    [self setMouseAction:action];
    [self setArg1:arg1];
    [self setArg2:arg2];
    return self;
}
@end
