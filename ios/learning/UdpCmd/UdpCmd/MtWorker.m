//
// Created by feuyeux@gmail.com on 14-2-24.
// Copyright (c) 2014 feuyeux@gmail.com. All rights reserved.
//

#import "MtWorker.h"


@implementation MtWorker
- (void)fill:(NSString *)value {
    if (!self.cacheArray) {
        self.cacheArray = [[NSMutableArray alloc] init];
    }
    @synchronized (self.cacheArray) {
        [self.cacheArray addObject:value];
    }
    NSLog(@"fill: %@", value);
}

- (void)iter {
    while (self.cacheArray.count <= 0) {
        sleep(1000);
        NSLog(@"waiting...");
    }
    @synchronized (self.cacheArray) {
        for (int i = 0; i < self.cacheArray.count; i++) {
            NSLog(@"get: %@", [self.cacheArray objectAtIndex:i]);
        }
    }
}

@end