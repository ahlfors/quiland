#import <Foundation/Foundation.h>
#import "MessageCodec.h"
#import "KeyInputCodec.h"

@interface CodecFactory : NSObject

+ (id <MessageCodec>)getInstance:(int)commandType;

@end
