#import "UdpCmdClient.h"
#import <ifaddrs.h>
#import <arpa/inet.h>

@interface UdpCmdClient ()

@end

@implementation UdpCmdClient

UInt16 const broadcastPort = 9080;
UInt16 const udpPort = 9139;

UILabel *callbackLabel;

- (UdpCmdClient *)initSocket:(UInt16)port {
    AsyncUdpSocket *tempSocket = [[AsyncUdpSocket alloc] initIPv4];
    self.socket = tempSocket;
    [self.socket setDelegate:self];
    tempSocket = nil;

    NSError *error = nil;
    [self.socket bindToPort:port error:&error];
    [self.socket enableBroadcast:YES error:&error];
    //[self.socket joinMulticastGroup:@"127.0.0.1" error:&error];
    [self.socket receiveWithTimeout:-1 tag:0];

    self.serverArray = [[NSMutableArray alloc] init];
    return self;
}

- (void)send:(NSString *)host port:(UInt16)port command:(BaseMessage *)cmd {
    NSLog(@"sending to %@:%d", host, port);
    [self.socket sendData:[CodecUtility encode:cmd]
                   toHost:host
                     port:port
              withTimeout:-1
                      tag:0];
}

- (void)broadcast:(UInt16)port message:(NSString *)msg {
    NSLog(@"broadcasting on %d", port);
    [self.socket sendData:[msg dataUsingEncoding:NSUTF8StringEncoding]
                   toHost:@"255.255.255.255"
                     port:port
              withTimeout:-1
                      tag:0];
    NSLog(@"broadcast done.");
}


// Get IP Address
- (NSString *)getIPAddress {
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while (temp_addr != NULL) {
            if (temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if ([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *) temp_addr->ifa_addr)->sin_addr)];
                }
            }
            temp_addr = temp_addr->ifa_next;
        }
    }
    // Free memory
    freeifaddrs(interfaces);
    return address;
}


- (BOOL)onUdpSocket:(AsyncUdpSocket *)sock didReceiveData:(NSData *)data withTag:(long)tag fromHost:(NSString *)host port:(UInt16)port {
    NSLog(@"receiving from: %@", host);
    [self.socket receiveWithTimeout:-1 tag:0];
    BaseMessage *cmd = [CodecUtility decode:data];
    if (nil == cmd) {
        @synchronized (self.serverArray) {
            BOOL existed = [self.serverArray containsObject:host];
            if (!existed) {
                NSString *ip = [self getIPAddress];
                if (![ip isEqualToString:host]) {
                    [self.serverArray addObject:host];
                }
            }
        }
        NSString *message = [NSString stringWithUTF8String:[data bytes]];
        NSLog(@"receiving data: %@", message);
    } else {
        NSLog(@"receiving data: %@", [cmd toString]);
        callbackLabel.text = [cmd toString];
    }
    return YES;
}

- (void)onUdpSocket:(AsyncUdpSocket *)sock didSendDataWithTag:(long)tag {
    NSLog(@"Message send success!");
}

- (void)onUdpSocket:(AsyncUdpSocket *)sock didNotReceiveDataWithTag:(long)tag dueToError:(NSError *)error {
    NSLog(@"Message not received for error: %@", error);
}

- (void)onUdpSocket:(AsyncUdpSocket *)sock didNotSendDataWithTag:(long)tag dueToError:(NSError *)error {
    NSLog(@"Message not send for error: %@", error);
}

- (void)onUdpSocketDidClose:(AsyncUdpSocket *)sock {
    NSLog(@"socket closed!");
}

- (void)callback:(UILabel *)label {
    callbackLabel = label;
}
@end

