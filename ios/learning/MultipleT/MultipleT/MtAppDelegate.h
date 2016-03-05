//
//  MtAppDelegate.h
//  MultipleT
//
//  Created by feuyeux@gmail.com on 14-2-24.
//  Copyright (c) 2014年 feuyeux@gmail.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MtAppDelegate : UIResponder <UIApplicationDelegate>

@property(strong, nonatomic) UIWindow *window;

@property(readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property(readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property(readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;

- (NSURL *)applicationDocumentsDirectory;

@end
