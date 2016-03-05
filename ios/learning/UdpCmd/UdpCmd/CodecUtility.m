#import "CodecUtility.h"


@implementation CodecUtility

+ (NSData *)encode:(BaseMessage *)udpCommand {
    NSMutableData *data = [[NSMutableData alloc] init];
    int commandType = NSSwapBigIntToHost(udpCommand.cmdType);
    const void *address = &commandType;
    int len = sizeof(commandType);
    [data appendBytes:address length:len];
    NSData *cmd = [[CodecFactory getInstance:udpCommand.cmdType] encode:udpCommand];
    [data appendData:cmd];
    return data;
}

+ (BaseMessage *)decode:(NSData *)data {
    int commandType;
    [data getBytes:&commandType length:sizeof(commandType)];
    int type = NSSwapBigIntToHost(commandType);
    NSString *cmdType = [CodecUtility convertToString:type];
    if (nil == cmdType) {
        return nil;
    }
    NSInteger begin = sizeof(commandType);
    NSInteger len = [data length] - begin;

    BaseMessage *cmd = [[CodecFactory getInstance:type] decode:[data subdataWithRange:NSMakeRange(begin, len)]];
    return cmd;
}

+ (NSString *)convertToString:(CommandType)commandType {
    NSString *result = nil;
    switch (commandType) {
        case KEY:
            result = @"KEY";
            break;
        case MOUSE:
            result = @"MOUSE";
            break;
        case STRINGS:
            result = @"STRING";
            break;
        case VOLUME:
            result = @"VOLUME";
            break;
        default:
            result = nil;
    }
    NSLog(@"%@", result);
    return result;
}

@end
