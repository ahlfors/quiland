#import <Foundation/Foundation.h>
#import "BaseMessage.h"

@interface MouseInputMessage : BaseMessage

@property int mouseAction;
@property int arg1;
@property int arg2;

- (MouseInputMessage *)init:(int)action arg1:(int)arg1 arg2:(int)arg2;
@end
