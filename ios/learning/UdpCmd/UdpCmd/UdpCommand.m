@implementation UdpCommand
- (NSString *)toString {
    NSString *result = [NSString stringWithFormat:@"%d-%@", _commandType, [_cmdMessage toString]];
    return result;
}
@end
