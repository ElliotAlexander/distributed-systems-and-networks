import sys
import socket

message="ep1e16"

server_address = ('wsn.ecs.soton.ac.uk', 5005)
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print "Starting up socket on port 5005. "
sock.connect(server_address)
sock.sendall(message)

while 1:
    data = sock.recvfrom(1024)
    print 'Server reply : ' + data[0]
