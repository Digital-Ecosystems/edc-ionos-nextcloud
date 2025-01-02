rootProject.name = "nextcloud"

include(":edc-ionos-nextcloud-extension:nextcloud-core")
include(":edc-ionos-nextcloud-extension:nextcloud-dataplane")
include(":edc-ionos-nextcloud-extension:nextcloud-provision")
include(":launcher:base:connector")
include(":launcher:dev:consumer")
include(":launcher:dev:provider")
include(":launcher:dev:file-transfer-ionosS3-nextcloud:consumer")
include(":launcher:dev:file-transfer-ionosS3-nextcloud:provider")

include(":launcher:prod:connector")
include(":launcher:prod:connector-persistence")