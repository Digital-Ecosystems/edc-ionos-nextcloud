rootProject.name = "nextcloud"

include(":edc-ionos-nextcloud-extension:nextcloud-core")
include(":edc-ionos-nextcloud-extension:nextcloud-dataplane")
include(":edc-ionos-nextcloud-extension:nextcloud-provision")
include(":connector")
include(":launcher:consumer")
include(":launcher:provider")
include(":launcher:file-transfer-ionosS3-nextcloud:consumer")
include(":launcher:file-transfer-ionosS3-nextcloud:provider")