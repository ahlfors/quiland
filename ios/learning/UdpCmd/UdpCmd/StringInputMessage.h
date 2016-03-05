#import <Foundation/Foundation.h>
#import "BaseMessage.h"

@interface StringInputMessage : BaseMessage
@property NSString *value;

- (StringInputMessage *)init:(NSString *)value;
@end
