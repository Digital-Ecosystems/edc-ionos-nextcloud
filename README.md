### Nextcloud Extension for Eclipse Dataspace Connector

This repository contains the Nextcloud Extension that works with the Eclipse Dataspace Connector allowing operations into the Nextcloud.

Disclaimer: The code of this repo is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible for determining the appropriateness of using or redistributing this code and assume any risks associated with Your exercise of permissions. For more information check the License.


## Building and Running

```bash
git clone [TBD]
cd EDC-IONOS-NEXTCLOUD
./gradlew clean build
```

```bash
cd connector
java -Dedc.fs.config=resources/config.properties -jar build/libs/dataspace-connector.jar
```

## Based on the following

- [https://github.com/eclipse-dataspaceconnector/DataSpaceConnector](https://github.com/eclipse-dataspaceconnector/DataSpaceConnector) - v0.1.2;
- [International Data Spaces](https://www.internationaldataspaces.org);
- [GAIA-X](https://gaia-x.eu) project;

## Requirements

You will need the following:
- Nextcloud account;
- Java Development Kit (JDK) 17 or higher;
- Docker;
- GIT;
- Linux shell or PowerShell;


## Folders Description

### `connector`
[TBD]

### `nextcloud-extension`
Contains the source code of the Nextcloud Extension.

### `example`
- Contains an example with a file transfer process between two Nextcloud.
- Contains an example with a file sharing process in one Nextcloud with different users.
- Contains an example with a file transfer process between one Nextcloud and one Ionos S3 bucket.


### `gradle/wrapper`
Contains gradle's files required for the building process.

## Dependencies and Configurations
### Dependencies
The extension has the following dependencies:

| Module name                                         | Description                                                                     |
|-----------------------------------------------------|---------------------------------------------------------------------------------|
| `edc-iono-nextcloud-extension:nextcloud-provision`  | Provisioning operations for Nextcloud                                           |
| `edc-iono-nextcloud-extension:nextcloud-data-plane` | Copy data do and from Nextcloud                                                 |
| `org.eclipse.edc:api-observability`                 | Health data regarding the state of the connector                                |
| `org.eclipse.edc:auth-tokenbased`                   | Securing the API                                                                |
| `org.eclipse.edc:api-control-plane-core`            | Main features of the control plane                                              | 
| `org.eclipse.edc:configuration-filesystem`          | Configuration file features                                                     | 
| `org.eclipse.edc:http`                              | HTTP support                                                                    | 
| `org.eclipse.edc:data-management-api`               | EDC asset and contract management                                               |
| `org.eclipse.edc:data-plane-core`                   | Main features of the data plane                                                 |
| `org.eclipse.edc:data-plane-selector-client`        | Offers several implementations for the data plane selector                      |
| `org.eclipse.edc:data-plane-selector-core`          | Main features of the data plane selector                                        |
| `org.eclipse.edc:transfer-data-plane`               | Provides services for delegating data transfer to the Data Plane                |
| `org.eclipse.edc:control-plane-api`                 | Contains all absolutely essential building that is necessary to run a connector |
| `org.eclipse.edc:dsp`                               | Data protocol                                                                   |

### Configurations
It is required to configure an `username` and a `password` from the Nextcloud service.

The credentials can be found/configured in one of the following:
- Vault;
- Properties file;
- Java arguments;

It is required to configure those parameters:

| Parameter name                          | Description| Mandatory |
|-----------------------------------------|--------------|--|
| `edc.ionos.nextcloud.username`                   | Nexcloud Username          | Yes |
| `edc.ionos.nextcloud.password`             | Nexcloud Password          | Yes |
| `edc.ionos.nextcloud.endpoint`    | Nexcloud endpoint address  |Yes  |


## Building and Running

```bash
git clone [TBD]
cd EDC-IONOS-Extension
./gradlew clean build
```

```bash
cd connector
java -Dedc.fs.config=resources/config.properties -jar build/libs/dataspace-connector.jar
```

## Example
In order to see a working example, go to the example folder.
