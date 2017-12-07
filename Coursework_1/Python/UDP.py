import sys
import socket


server_address = ('wsn.ecs.soton.ac.uk', 5005)
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print "Starting up socket on port 5005. "
sock.connect(server_address)

response = ""
try:
    message="ep1e16"
    sock.sendall(message)

    while 1:
        d = sock.recvfrom(1024)
        reply = d[0]
        addr = d[1]

        print 'Response : ' + reply

except socket.error, msg:
    print 'Error code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
