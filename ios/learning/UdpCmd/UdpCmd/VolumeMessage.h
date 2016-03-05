#import <Foundation/Foundation.h>
#import "BaseMessage.h"

@interface VolumeMessage : BaseMessage
@property int volume;

- (VolumeMessage *)init:(int)volume;
@end
