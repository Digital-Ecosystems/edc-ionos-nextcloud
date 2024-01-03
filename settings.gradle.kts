rootProject.name = "nextcloud"

include(":edc-iono-nextcloud-extension:nextcloud-core")
include(":edc-iono-nextcloud-extension:nextcloud-dataplane")
include(":edc-iono-nextcloud-extension:nextcloud-provision")
include(":connector")
include("example:file-transfer-between-2-nextcloud:consumer")
include("example:file-transfer-between-2-nextcloud:provider")
include("example:file-transfer-ionoss3-nextcloud:consumer")
include("example:file-transfer-ionoss3-nextcloud:provider")