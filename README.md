### Nextcloud Extension for the EDC Connector

This repository contains the Nextcloud Extension that works with the Eclipse Dataspace Components Connector allowing operations into the Nextcloud.

Nextcloud, a solution from [IONOS](https://www.ionos.com) is an open source file sync and share solution, which is installed on your server. With this solution, you can store files like documents, calendars, contacts and photos. To know more about Nextcloud check this [link](https://www.ionos.com/cloud/cloud-apps/nextcloud). 

Disclaimer: The code of this repo is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible for determining the appropriateness of using or redistributing this code and assume any risks associated with Your exercise of permissions. For more information check the License.


## Based on the following

- [https://github.com/eclipse-edc/Connector](https://github.com/eclipse-edc/Connector) - v0.4.1;
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

Contains the necessary requirements for one to build the Connector with the Nextcloud Extension.

### `edc-ionos-nextcloud-extension`

Contains the source code of the Nextcloud Extension.

### `gradle`

Contains the gradle files used for the building process.

### `hashicorp`

Contains a docker file to run an instance of an Hashicorp vault to store secrets.

### `launcher`

Contains a simple example of a file transfer between between 2 connectors using the Nextcloud Extension.

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
| `core-spi`                                          | Contains the interfaces related to the core classes                             |
| `management-api-configuration`                      | In order to use the EDC management API                                          |
| `web-spi`                                           | To interact with the web interfaces                                             | 
| `transfer-spi`                                      | To interact with the transfer interfaces                                        |


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

### Transfer options

This extension allows two ways of doing a file transfer between 2 connectors in a Dataspace:
- Transfering the file: the file is copied `bit by bit` into the consumer's Nextcloud storage. In this scenario, two instances of Nextcloud are used, one for the provider and another one for the consumer;
- Sharing the file: the file is `NOT` copied but instead a share mechanism from Nexctcloud is used thus allowing the access of the file for the consumer directly through the Nextcloud of the provider. In this scenario, only one instance of Nextcloud is used; 

Related to the latter point, this extension allows the possibility of defining a share policy using several parameters like for instance a `time limit` to access the file. Check the [policies](policies.md) file.


## Building and Running

```bash
git clone [COPY THE URL OF THIS REPO]
cd edc-ionos-nextcloud
./gradlew clean build
```

```bash
cd connector
java -Dedc.fs.config=resources/config.properties -jar build/libs/dataspace-connector.jar
```

## Example

To see this extension in action please check the [samples](https://github.com/Digital-Ecosystems/edc-ionos-samples) repository.
