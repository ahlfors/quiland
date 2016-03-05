#import <Foundation/Foundation.h>
#import "CodecFactory.h"

@interface CodecUtility : NSObject
+ (NSData *)encode:(BaseMessage *)udpCommand;

+ (BaseMessage *)decode:(NSData *)data;

@end
