#import "VolumeMessage.h"

@implementation VolumeMessage

- (NSString *)toString {
    return [NSString localizedStringWithFormat:@"TYPE=%d,VOLUME=%d", self.cmdType, _volume];
}

- (VolumeMessage *)init:(int)volume {
    self = [super init];
    self.cmdType = VOLUME;
    [self setVolume:volume];
    return self;
}
@end
