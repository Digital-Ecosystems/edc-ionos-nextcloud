rootProject.name = "nextcloud"

include(":edc-ionos-nextcloud-extension:nextcloud-core")
include(":edc-ionos-nextcloud-extension:nextcloud-dataplane")
include(":edc-ionos-nextcloud-extension:nextcloud-provision")
include(":connector")
include("example:file-transfer-between-2-nextcloud:consumer")
include("example:file-transfer-between-2-nextcloud:provider")
include("example:file-transfer-ionoss3-nextcloud:consumer")
include("example:file-transfer-ionoss3-nextcloud:provider")