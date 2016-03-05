#import <UIKit/UIKit.h>
#import "AsyncUdpSocket.h"
#import "AsyncSocket.h"
#import "CodecUtility.h"

@interface UdpCmdClient : NSObject <AsyncSocketDelegate>
@property(nonatomic, strong) AsyncUdpSocket *socket;
@property(nonatomic, strong) NSMutableArray *serverArray;
extern UInt16 const broadcastPort;
extern UInt16 const udpPort;

- (UdpCmdClient *)initSocket:(UInt16)port;

- (void)send:(NSString *)host port:(UInt16)port command:(BaseMessage *)cmd;

- (void)broadcast:(UInt16)port message:(NSString *)msg;

//TODO it should be an interface to handle the callback
- (void)callback:(UILabel *)label;
@end
