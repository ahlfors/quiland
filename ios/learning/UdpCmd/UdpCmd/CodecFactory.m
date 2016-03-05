#import "CodecFactory.h"
#import "MouseInputCodec.h"
#import "StringInputCodec.h"
#import "VolumeInputCodec.h"

@implementation CodecFactory
NSMutableDictionary *dictionary;

+ (void)initDirectory {
    dictionary = [NSMutableDictionary dictionaryWithCapacity:4];
    [dictionary setObject:[[KeyInputCodec alloc] init] forKey:[NSString stringWithFormat:@"%d", KEY]];
    [dictionary setObject:[[MouseInputCodec alloc] init] forKey:[NSString stringWithFormat:@"%d", MOUSE]];
    [dictionary setObject:[[StringInputCodec alloc] init] forKey:[NSString stringWithFormat:@"%d", STRINGS]];
    [dictionary setObject:[[VolumeInputCodec alloc] init] forKey:[NSString stringWithFormat:@"%d", VOLUME]];
}

+ (id <MessageCodec>)getInstance:(int)cmdKey {
    if (dictionary == NULL) {
        [CodecFactory initDirectory];
    }
    return [dictionary objectForKey:[NSString stringWithFormat:@"%d", cmdKey]];
}

@end
