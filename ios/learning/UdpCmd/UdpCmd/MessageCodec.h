#import <Foundation/Foundation.h>
#import "BaseMessage.h"

@protocol MessageCodec <NSObject>

- (NSData *)encode:(BaseMessage *)cmdMessage;

- (BaseMessage *)decode:(NSData *)data;
@end
