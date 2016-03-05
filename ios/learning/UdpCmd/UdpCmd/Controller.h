#import <Foundation/Foundation.h>
#import "BaseMessage.h"

@protocol Controller <NSObject>

- (void)process:(BaseMessage *)controlInfo;

@end
