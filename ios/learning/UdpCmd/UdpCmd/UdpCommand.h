#import <Foundation/Foundation.h>
#import "BaseMessage.h"

enum InputType {
    KEY = 1000, MOUSE = 2000, STRINGS = 3000, VOLUME = 8001
};
typedef enum InputType CommandType;

@interface UdpCommand : NSObject
@property(nonatomic) CommandType commandType;
@property BaseMessage *cmdMessage;

- (NSString *)toString;
@end
