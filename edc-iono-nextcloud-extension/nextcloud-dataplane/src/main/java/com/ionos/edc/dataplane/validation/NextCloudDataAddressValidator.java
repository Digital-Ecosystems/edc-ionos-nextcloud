package com.ionos.edc.dataplane.validation;

import com.ionos.edc.schema.NextcloudSchema;
import org.eclipse.edc.spi.types.domain.DataAddress;

import org.eclipse.edc.util.string.StringUtils;
import org.eclipse.edc.validator.spi.ValidationResult;
import org.eclipse.edc.validator.spi.Validator;
import org.eclipse.edc.validator.spi.Violation;

import com.ionos.edc.schema.NextcloudSchema.*;
public class NextCloudDataAddressValidator  implements Validator<DataAddress> {
    @Override
    public ValidationResult validate(DataAddress input) {
        if (StringUtils.isNullOrBlank(input.getStringProperty(NextcloudSchema.FILE_PATH, null))) {
            return ValidationResult.failure(Violation.violation("Must contain property '%s'".formatted(NextcloudSchema.FILE_PATH), NextcloudSchema.FILE_PATH));
        }
        if (StringUtils.isNullOrBlank(input.getStringProperty(NextcloudSchema.FILE_NAME, null))) {
            return ValidationResult.failure(Violation.violation("Must contain property '%s'".formatted(NextcloudSchema.FILE_NAME), NextcloudSchema.FILE_NAME));
        }
        return ValidationResult.success();
    }
}
