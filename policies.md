# Policies for the Nextcloud Extension
This document explains how to define policies for a file sharing using the Nextcloud.


## Policy parameters
The following table explains wich parameters can be used. The values use the configuration of the Nextcloud itself.

| Parameter name       | Description                      | Options                                                                                                       | Mandatory                                            |
|----------------------|----------------------------------|----------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| `shareWith`          | Identification of the user/group we want to share the file with | user / group id / email address / circleID / conversation name with which the file should be shared            | Yes                                                  |
| `shareType`          | Type of sharing, this option must match the previous option. For example: if `user` has been chosen, then shareType should be 0                 | 0 = user; 1 = group; 3 = public link; 4 = email; 6 = federated cloud share; 7 = circle; 10 = Talk conversation | Yes             |
| `permissionType`    |   The permission that we want to set to the shared file                         | 1 = read; 2 = update; 4 = create; 8 = delete; 16 = share; 31 = all (default: 31, for public shares: 1          | Yes |
| `expirationTime` |          Set a expire date of the shared file. This argument expects a well formatted date string, e.g. ‘YYYY-MM-DD’                        | - | No |                                                              
