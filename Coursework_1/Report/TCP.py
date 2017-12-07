import sys
import socket

server_address = ('wsn.ecs.soton.ac.uk', 5002)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print "Starting up socket on port 5002. "
sock.connect(server_address)
response = ""
try:
    message="ep1e16"
    sock.sendall(message)

    next_response = sock.recv(16)
    while next_response != '':
        response += next_response
        next_response = sock.recv(16)

finally:
    print "Response : %s" % response
