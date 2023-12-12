package com.ionos.edc.core;

import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provides;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.security.CertificateResolver;
import org.eclipse.edc.spi.security.PrivateKeyResolver;
import com.ionos.edc.nextcloudapi.NextCloudApi;

@Extension(value = NextcloudExtensionCore.NAME)
public class NextcloudExtensionCore implements ServiceExtension {
    public static final String NAME = "Nextcloud";

    @Inject
    private Vault vault;

    @Inject
    private Monitor monitor;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        NextCloudApi nextApi = new NextCloudApi();
        monitor.info("initiate "+this.NAME+" core");

       // context.registerService(NextcloudExtensionCore.class, nextApi);
    }
}
