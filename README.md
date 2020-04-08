# wol-proxy

This project has three main pieces of functionality:

1. Send a Wake-on-Lan (WoL) packet to a device on your network (`--wol`)
2. A proxy server that listens for webhook requests
3. A client/server mode for communicating between the proxy and a client.

The ideal setup:

1. A publicly visible server that hosts the webhook listener and the server
2. A Raspberry Pi, connected to the device you want to WoL, that acts as a
   client

In my case, I setup an Nginx forwarder to the webhook listener. So you'd have:

"OK Google, turn my PC on"

```
ifttt.com -> proxy-and-server.com/wol
proxy-and-server.com/wol -> localhost:proxy-port   # via nginx
localhost:proxy-port -> localhost:server           # it's the same app though
localhost:server -> raspberry-pi:client
raspberry-pi:client -> WoL
```

The client/server are intended to keep a persistent connection. The server
regularly cleans up stale connections. The client will (eventually) reconnect
if it hasn't received a ping in a few cycles.
