#import "VolumeInputCodec.h"
#import "VolumeMessage.h"

@implementation VolumeInputCodec {

}

- (NSData *)encode:(BaseMessage *)cmdMessage {
    if ([cmdMessage isKindOfClass:[VolumeMessage class]]) {
        VolumeMessage *volumeMessage = (VolumeMessage *) cmdMessage;
        NSMutableData *data = [[NSMutableData alloc] init];
        int volume = NSSwapBigIntToHost(volumeMessage.volume);
        [data appendBytes:&volume length:sizeof(volume)];
        return data;
    } else {
        return NULL;
    }
}

- (BaseMessage *)decode:(NSData *)data {
    int volume;
    [data getBytes:&volume length:sizeof(volume)];
    VolumeMessage *volumeMessage = [[VolumeMessage alloc] init:NSSwapBigIntToHost(volume)];
    return volumeMessage;
}

@end