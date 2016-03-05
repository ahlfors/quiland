#import <Foundation/Foundation.h>
#import "BaseMessage.h"

@interface KeyInputMessage : BaseMessage

@property int keyCode;
@property int keyAction;

- (KeyInputMessage *)init:(int)keyCode action:(int)keyAction;
@end
