#import "UdpCmdMainViewController.h"
#import "UdpCmdClient.h"
#import "KeyInputMessage.h"
#import "StringInputMessage.h"
#import "MouseInputMessage.h"
#import "VolumeMessage.h"

@interface UdpCmdMainViewController ()
@property(weak, nonatomic) IBOutlet UITextField *keyText;
@property(weak, nonatomic) IBOutlet UITextField *xText;
@property(weak, nonatomic) IBOutlet UITextField *yText;
@property(weak, nonatomic) IBOutlet UITextField *strInput;
@property(weak, nonatomic) IBOutlet UITextField *volumeText;
@property(weak, nonatomic) IBOutlet UILabel *resultLabel;
@property(weak, nonatomic) IBOutlet UITextField *serverText;

@end

@implementation UdpCmdMainViewController
UdpCmdClient *client;

- (IBAction)connectBtn:(id)sender {
    [client callback:self.resultLabel];
}

- (IBAction)keyBtn:(id)sender {
    self.resultLabel.text = @"sending key command";
    KeyInputMessage *cmd = [[KeyInputMessage alloc] init:[self.keyText.text intValue] action:1999];
    [client send:self.serverText.text port:udpPort command:cmd];
}

- (IBAction)mouseBtn:(id)sender {
    self.resultLabel.text = @"sending mouse command";
    MouseInputMessage *cmd = [[MouseInputMessage alloc] init:1234 arg1:[self.xText.text intValue] arg2:[self.yText.text intValue]];
    [client send:self.serverText.text port:udpPort command:cmd];
}

- (IBAction)strBtn:(id)sender {
    self.resultLabel.text = @"sending string command";
    StringInputMessage *cmd = [[StringInputMessage alloc] init:self.strInput.text];
    [client send:self.serverText.text port:udpPort command:cmd];
}

- (IBAction)volume:(id)sender {
    self.resultLabel.text = @"sending volume command";
    VolumeMessage *cmd = [[VolumeMessage alloc] init:[self.volumeText.text intValue]];
    [client send:self.serverText.text port:udpPort command:cmd];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    client = [[UdpCmdClient alloc] initSocket:broadcastPort];
    [client broadcast:broadcastPort message:@"F"];

    dispatch_queue_t queue = dispatch_queue_create("name", NULL);
    dispatch_async(queue, ^{
        while (client.serverArray.count <= 0) {
            NSLog(@"Waiting for broadcasting response ...");
            sleep(1);
        }

        dispatch_sync(dispatch_get_main_queue(), ^{
            Boolean isMain = [NSThread isMainThread];
            if (isMain) {
                NSMutableArray *serverArray = client.serverArray;
                for (int i = 0; i < serverArray.count; i++) {
                    self.serverText.text = [serverArray objectAtIndex:i];
                    //TODO replace with list component
                    break;
                }
            }
        });
    });
}
@end