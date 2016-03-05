#import <Foundation/Foundation.h>

enum InputType {
    KEY = 1000, MOUSE = 2000, STRINGS = 3000, VOLUME = 8001
};
typedef enum InputType CommandType;

@interface BaseMessage : NSObject
@property int cmdType;

- (NSString *)toString;
@end