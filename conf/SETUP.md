1. Create /opt/wol-proxy, give the current user permissions, e.g. `chmod -R alex:alex /opt/wol-proxy`
2. Copy the services to /etc/systemd/system/
3. `sudo systemctl daemon-reload`
4. Give the user permission to reload the daemon, `sudo visudo -f /etc/sudoers.d/alex`:

```
alex ALL = (root) NOPASSWD: /bin/systemctl restart wol-proxy
```
5. Make sure the daemon starts on boot:

```
sudo systemctl enable wol-proxy
```
