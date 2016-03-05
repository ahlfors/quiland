//
//  MtWorker.h
//  MultipleT
//
//  Created by feuyeux@gmail.com on 14-2-24.
//  Copyright (c) 2014年 feuyeux@gmail.com. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MtWorker : NSObject
@property(nonatomic, strong) NSMutableArray *cacheArray;

- (void)fill:(NSString *)value;

- (void)iter;

@end
