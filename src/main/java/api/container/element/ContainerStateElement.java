package api.container.element;

public class ContainerStateElement {
    /*
        "status": "Stopped",
        "status_code": 102,
        "disk": null,
        "memory": {
            "usage": 0,
            "usage_peak": 0,
            "swap_usage": 0,
            "swap_usage_peak": 0
        },
        "network": null,
        "pid": 0,
        "processes": 0,
        "cpu": {
            "usage": 0
        }



        or

        {
            "status": "Running",
            "status_code": 103,
            "disk": {},
            "memory": {
                "usage": 118321152,
                "usage_peak": 146370560,
                "swap_usage": 0,
                "swap_usage_peak": 0
            },
            "network": {
                "eth0": {
                    "addresses": [
                        {
                            "family": "inet",
                            "address": "10.71.10.127",
                            "netmask": "24",
                            "scope": "global"
                        },
                        {
                            "family": "inet6",
                            "address": "fd42:5a33:c9ef:1cdf:216:3eff:fed4:1f94",
                            "netmask": "64",
                            "scope": "global"
                        },
                        {
                            "family": "inet6",
                            "address": "fe80::216:3eff:fed4:1f94",
                            "netmask": "64",
                            "scope": "link"
                        }
                    ],
                    "counters": {
                        "bytes_received": 18986,
                        "bytes_sent": 9405,
                        "packets_received": 121,
                        "packets_sent": 88
                    },
                    "hwaddr": "00:16:3e:d4:1f:94",
                    "host_name": "vethGJV221",
                    "mtu": 1500,
                    "state": "up",
                    "type": "broadcast"
                },
                "lo": {
                    "addresses": [
                        {
                            "family": "inet",
                            "address": "127.0.0.1",
                            "netmask": "8",
                            "scope": "local"
                        },
                        {
                            "family": "inet6",
                            "address": "::1",
                            "netmask": "128",
                            "scope": "local"
                        }
                    ],
                    "counters": {
                        "bytes_received": 1296,
                        "bytes_sent": 1296,
                        "packets_received": 16,
                        "packets_sent": 16
                    },
                    "hwaddr": "",
                    "host_name": "",
                    "mtu": 65536,
                    "state": "up",
                    "type": "loopback"
                }
            },
            "pid": 26778,
            "processes": 16,
            "cpu": {
                "usage": 11971030032
            }
        }
     */

    private String status;
    private Object network;
    private int pid;
    private int processes;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getNetwork() {
        return network;
    }

    public void setNetwork(Object network) {
        this.network = network;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getProcesses() {
        return processes;
    }

    public void setProcesses(int processes) {
        this.processes = processes;
    }

}
