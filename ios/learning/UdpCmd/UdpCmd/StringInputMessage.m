#import "StringInputMessage.h"

@implementation StringInputMessage
- (NSString *)toString {
    return [NSString localizedStringWithFormat:@"TYPE=%d,VALUE=%@", self.cmdType, _value];
}

- (StringInputMessage *)init:(NSString *)value {
    self = [super init];
    self.cmdType = STRINGS;
    [self setValue:value];
    return self;
}
@end
